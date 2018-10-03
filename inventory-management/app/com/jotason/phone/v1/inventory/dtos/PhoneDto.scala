package com.jotason.phone.v1.inventory.dtos

import play.api.libs.json._

/**
  DTO for showing a phone.
  */

case class PhoneDto(id: Int, name: String, description: String, imageUrl: String, price: Double)

object PhoneDto {
  implicit val phoneFormat = Json.format[PhoneDto]
}
