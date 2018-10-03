package com.jotason.phone.v1.orders.controllers

import com.jotason.phone.v1.orders.dtos.CreateNewOrderDto
import com.jotason.phone.v1.orders.exceptions.exceptions.{DBException, DiscontinuedPhonesException, ParsingPhonesException, PhoneShopApiClientException}
import com.jotason.phone.v1.orders.services.OrderService
import javax.inject.Inject
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

/**
  * Rest api controller for orders
  */
class OrderController @Inject()(cc: ControllerComponents, service: OrderService)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  private val logger = Logger(getClass)

  def save() = Action(parse.json).async { implicit request =>
    logger.trace("Saving order")
    val orderResult = request.body.validate[CreateNewOrderDto]
    orderResult.fold(
      errors => {
        logger.debug(s"Bad request")
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      newOrder => {
        service.createOrder(newOrder).map { orderCreatedDto =>
          val orderCreatedJson = Json.toJson(orderCreatedDto)
          logger.info(s"Order created:\n ${Json.prettyPrint(orderCreatedJson)}")
          Created(orderCreatedJson)
        }.recover(recoverPF)
      }
    )
  }


  def get(id: Long) = Action.async { implicit request =>
    logger.trace(s"Getting order: $id")
    service.getOrderById(id).map {
      case Some(order) =>
        val orderJson = Json.toJson(order)
        logger.info(s"Order found:\n ${Json.prettyPrint(orderJson)}")
        Ok(orderJson)
      case None =>
        logger.info(s"Order not found: $id")
        NotFound(Json.obj("message" -> s"Order not found | id = $id"))
    }.recover(recoverPF)
  }

  private def recoverPF: PartialFunction[Throwable, Result] = {
    case ex: DiscontinuedPhonesException => BadRequest(Json.obj("exception" -> ex.getMessage))
    case ex: ParsingPhonesException => InternalServerError(Json.obj("exception" -> ex.getMessage))
    case ex: DBException => BadGateway(Json.obj("exception" -> ex.getMessage))
    case ex: PhoneShopApiClientException => BadGateway(Json.obj("exception" -> ex.getMessage))
    case ex: Exception =>
      logger.error("An exception ocurred", ex)
      InternalServerError(Json.obj("exception" -> ex.getMessage))
  }

}
