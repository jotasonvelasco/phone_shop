package com.jotason.phone.v1.orders.repository

import com.jotason.phone.v1.orders.dtos.{CreateNewOrderDto, OrderCreatedDto, OrderDto, OrderLineItemDto}
import com.jotason.phone.v1.orders.exceptions.exceptions.DBException
import com.jotason.phone.v1.orders.repository.dbModel.{OrderLineItemRow, OrderRow}
import com.jotason.phone.v1.orders.services.dto.PhoneDto
import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logger}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

/**
  * Order repository
  */
trait OrderRepository extends ConfigurationLike {

  /**
    * Create an order in the DB
    *
    * @param newOrder Order to create
    * @param phonesOrderedInCatalog Phone ordered in catalog (used for calculating the cost of the order)
    * @return a future wrapping the order created
    */
  def create(newOrder: CreateNewOrderDto, phonesOrderedInCatalog: List[PhoneDto]): Future[OrderCreatedDto]

  /**
    * Lookup an order corresponding with a given id in the DB
    *
    * @param id the order id
    * @return a future wrapping the order
    *         Returns Future[None] if the order is not found
    *
    */
  def lookup(id: Long): Future[Option[OrderDto]]

}

/**
  * Order repository implemented with Slick.
  */
@Singleton
class SlickOrderRepository @Inject()(
                                      protected val dbConfigProvider: DatabaseConfigProvider,
                                      val configuration: Configuration)(implicit ec: DBIOExecutionContext)
  extends OrderRepository
    with TableInfoProvider
    with URLBuilder
    with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val logger = Logger(getClass)


  def create(newOrder: CreateNewOrderDto, phonesOrderedInCatalog: List[PhoneDto]): Future[OrderCreatedDto] = {
    /**
      * Calculate total price of the order and a list of subtotal of line items
      * For instance: (701.00, List( (1, 500.50), (2, 200.50)) )
      *                   ↑           ↑     ↑      ↑     ↑
      *                 total        id    subT    id   subT
      */
    val (totalPrice, subtotalList) = newOrder.lineItems.foldLeft((0.0, List.empty[(Int, Double)])) { (acc, line) =>
      val lineItemSubtotal = phonesOrderedInCatalog.filter(_.id == line.phoneId).head.price * line.quantity
      (lineItemSubtotal + acc._1, (line.phoneId, lineItemSubtotal) :: acc._2)
    }

    logger.debug(s"Creating insert for order")
    val insertOrderRow = (orders returning orders.map(_.id)) +=
      OrderRow(
        id= None,
        customerName = newOrder.customerName,
        customerSurname = newOrder.customerSurname,
        customerEmail = newOrder.customerEmail,
        totalPrice = totalPrice)

    val insertOrderLineItemList = (generatedId: Long) =>  orderLineItems ++= newOrder.lineItems.map { lineDto =>
      logger.debug(s"Creating insert for line: $lineDto")
      OrderLineItemRow(
        id = None,
        phoneId = lineDto.phoneId,
        quantity = lineDto.quantity,
        subtotal = subtotalList.filter(_._1 == lineDto.phoneId).map(_._2).head,
        orderId = generatedId
      )
    }

    // Build a transaction for the two inserts
    val transactionalInsert = (for {
      generatedOrderId <- insertOrderRow
      _ <- insertOrderLineItemList(generatedOrderId)
    } yield generatedOrderId ).transactionally

    logger.debug(s"Inserting order in db: $newOrder")
    db.run(transactionalInsert).map { generatedOrderId =>
      logger.debug(s"Order created with id: $generatedOrderId")
      OrderCreatedDto(generatedOrderId, buildOrderUrl(generatedOrderId))
    }.recover(dbRecover)

  }

  def lookup(id: Long): Future[Option[OrderDto]] = {
    // Build the query
    val query = for {
      order <- orders if order.id === id
      line <- orderLineItems if line.orderId === order.id
    } yield (order, line)

    // Execute the query
    logger.debug(s"Searching order from db: $id")
    val futureResult: Future[Seq[(OrderRow, OrderLineItemRow)]] = db.run(query.result)

    // Create DTO
    val maybeFutureOrderPar: Future[Option[(OrderRow, Seq[OrderLineItemRow])]] = futureResult.map { result =>
      result.groupBy(_._1).map {
        case (k, v) => (k, v.map(_._2))
      }.headOption
    }

    maybeFutureOrderPar.map {
      case Some(orderPar) =>
        Some(orderRowToOrderDto(orderPar._1, orderPar._2))
      case None =>
        logger.debug(s"Order not found: $id")
        None
    }.recover(dbRecover)

  }

  /**
    PF for recovering from a database error
   */
  private def dbRecover[U]: PartialFunction[Throwable, U] = {
    case ex: Exception =>
      logger.error("Database error", ex)
      throw new DBException(s"Database error | msg = ${ex.getMessage}", ex)
  }

  private def orderRowToOrderDto(orderRow: OrderRow, orderLineItemsRowList: Seq[OrderLineItemRow]) = {
    logger.debug(s"Mapping OrderRow to OrderDto: $orderRow")
    OrderDto(
      orderId = orderRow.id,
      customerName = orderRow.customerName,
      customerSurname = orderRow.customerSurname,
      customerEmail = orderRow.customerEmail,
      lineItems = orderLineItemsRowList.map(orderLineItemRowToOrderLineItemDto).toList,
      totalPrice = orderRow.totalPrice
    )
  }

  private def orderLineItemRowToOrderLineItemDto(orderLineItemRow: OrderLineItemRow) = {
    logger.debug(s"Mapping OrderLineItemRow to OrderLineItemDto: $orderLineItemRow")
    OrderLineItemDto(
      phoneId = orderLineItemRow.phoneId,
      quantity = orderLineItemRow.quantity,
      subtotal = orderLineItemRow.subtotal,
      phoneReference = buildPhoneUrl(orderLineItemRow.phoneId)
    )
  }



}


