package com.jotason.phone.v1.orders.dtos

import play.api.libs.json.{Format, Json}

/**
  DTO for showing the result of a new order created.
  */

case class OrderCreatedDto(orderId: Long, location: String)

object OrderCreatedDto {
  implicit val orderCreatedFormat: Format[OrderCreatedDto] = Json.format[OrderCreatedDto]
}
