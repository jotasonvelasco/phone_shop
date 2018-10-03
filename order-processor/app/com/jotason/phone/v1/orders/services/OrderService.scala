package com.jotason.phone.v1.orders.services

import com.jotason.phone.v1.orders.dtos.{CreateNewOrderDto, OrderCreatedDto, OrderDto}
import com.jotason.phone.v1.orders.exceptions.exceptions.{DiscontinuedPhonesException, ParsingPhonesException}
import com.jotason.phone.v1.orders.repository.OrderRepository
import com.jotason.phone.v1.orders.services.client.PhoneShopApiService
import com.jotason.phone.v1.orders.services.dto.PhoneDto
import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logger}

import scala.None
import scala.concurrent.Future

/**
  * Service for operations with Orders
  */
trait OrderService {

  /**
    * Create a new order.
    *
    * @param newOrder Order to create
    * @return a future wrapping the result of the new order created
    */
  def createOrder(newOrder: CreateNewOrderDto): Future[OrderCreatedDto]

  /**
    * Get an order corresponding with a given id.
    *
    * @param id the order id
    * @return a future wrapping the order
    *         Returns Future[None] if the order is not found
    *
    */
  def getOrderById(id: Long): Future[Option[OrderDto]]

}


@Singleton
class OrderServiceImpl @Inject()(
                                  repo: OrderRepository,
                                  configuration: Configuration,
                                  phoneShopService: PhoneShopApiService)(implicit ec: ApiClientIOExecutionContext)
  extends OrderService {

  private val logger = Logger(getClass)


  def createOrder(newOrder: CreateNewOrderDto): Future[OrderCreatedDto] = {

    val inventoryUrl = configuration.get[String]("service.inventory.url")

    // Call inventory api for getting the catalog
    logger.debug("Calling inventory api")
    val futureCatalog: Future[Seq[PhoneDto]] = phoneShopService.get(inventoryUrl).map { jsonResponse =>
      jsonResponse.validate[Seq[PhoneDto]].fold(
        error => {
          val errorMsg = "Error parsing inventory api response"
          logger.error(s"$errorMsg: $error")
          throw new ParsingPhonesException(errorMsg)
        },
        catalog => {
          logger.debug(s"Catalog obtained from inventory api: $catalog")
          Future.successful(catalog)
          catalog
        }
      )
    }

    // Filter the phones requested form the catalog
    val futurePhoneRequestedMap: Future[Map[Int, Option[PhoneDto]]] = futureCatalog.map { catalog =>
      newOrder.lineItems.map { line =>
        (line.phoneId, catalog.find(_.id == line.phoneId))
      }.toMap
    }

    futurePhoneRequestedMap.flatMap { phoneMap =>
      // Check if all phones ordered are available in the catalog
      if (phoneMap.values.forall(_.isDefined)) {
        repo.create(newOrder, phoneMap.values.flatten.toList)
      } else {
        val errorMsg = s"Phones not found in catalog: ${phoneMap.filterNot(_._2.isDefined).keys}"
        throw new DiscontinuedPhonesException(errorMsg)
      }
    }
  }

  def getOrderById(id: Long): Future[Option[OrderDto]] = repo.lookup(id)
}
