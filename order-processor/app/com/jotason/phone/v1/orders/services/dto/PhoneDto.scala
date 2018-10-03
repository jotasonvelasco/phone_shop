package com.jotason.phone.v1.orders.services.dto

import play.api.libs.json.Json

/**
  * Phone DTO for getting phones from inventory api (Only interested in id)
  */
case class PhoneDto(id: Int, price: Double)

object PhoneDto {
  implicit val phoneReads = Json.reads[PhoneDto]
}