package com.jotason.phone.v1.orders.dtos

import play.api.libs.json._

/**
  DTO's for showing an order
  */


case class OrderDto(
                     orderId: Option[Long],
                     customerName: String,
                     customerSurname: String,
                     customerEmail: String,
                     lineItems: List[OrderLineItemDto],
                     totalPrice: Double
                   )

object OrderDto {
  implicit val orderDtoFormat: Format[OrderDto] = Json.format[OrderDto]
}

case class OrderLineItemDto(phoneId: Int, quantity: Int, subtotal: Double, phoneReference: String)

object OrderLineItemDto {
  implicit val orderLineItemDtoFormat : Format[OrderLineItemDto] = Json.format[OrderLineItemDto]
}

