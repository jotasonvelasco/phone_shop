package com.jotason.phone.v1.orders.dtos

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
  DTO's for creaating a new order
  */

case class OrderLineItemRequestedDto(phoneId: Int, quantity: Int)

object OrderLineItemRequestedDto {

  val orderLineItemRequestedDtoReads : Reads[OrderLineItemRequestedDto] = (
    (JsPath \ "phoneId").read[Int] and
    (JsPath \ "quantity").read[Int](min(1))
  )(OrderLineItemRequestedDto.apply _)

  val orderLineItemRequestedDtoWrites: Writes[OrderLineItemRequestedDto] = Json.writes[OrderLineItemRequestedDto]

  implicit val orderLineItemRequestedDtoFormat: Format[OrderLineItemRequestedDto] =
    Format(orderLineItemRequestedDtoReads, orderLineItemRequestedDtoWrites)

}

case class CreateNewOrderDto(
                     customerName: String,
                     customerSurname: String,
                     customerEmail: String,
                     lineItems: List[OrderLineItemRequestedDto]
                   )

object CreateNewOrderDto {
  val createNewOrderDtoReads : Reads[CreateNewOrderDto] = (
    (JsPath \ "customerName").read[String](minLength[String](1)) and
    (JsPath \ "customerSurname").read[String](minLength[String](1)) and
    (JsPath \ "customerEmail").read[String](email) and
    (JsPath \ "lineItems").read[List[OrderLineItemRequestedDto]].
      filter(JsonValidationError("erorr.minLength"))(_.nonEmpty)
  )(CreateNewOrderDto.apply _)

  val createNewOrderDtoWrites: Writes[CreateNewOrderDto] = Json.writes[CreateNewOrderDto]

  implicit val createNewOrderDtoFormat: Format[CreateNewOrderDto] =
    Format(createNewOrderDtoReads, createNewOrderDtoWrites)

}