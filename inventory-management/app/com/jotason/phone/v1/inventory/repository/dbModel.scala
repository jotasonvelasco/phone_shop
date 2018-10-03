package com.jotason.phone.v1.inventory.repository

private[repository] object dbModel {

  /**
    *  Represents a phone in db
    *
    * @param id the phone id
    * @param name the name
    * @param description a description
    * @param imageRef reference of an image
    * @param price the price of a single phone
    */
  case class PhoneRow(id: Int, name: String, description: String, imageRef: String, price: Double)

}
