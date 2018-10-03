package com.jotason.phone.v1.orders.repository

private[repository] object dbModel {

  /**
    * Represents an order in db
    * @param id the order id
    * @param customerName the customer name
    * @param customerSurname the surname name
    * @param customerEmail the customer mail
    * @param totalPrice total price of the order
    */
  case class OrderRow(
                       id: Option[Long],
                       customerName: String,
                       customerSurname: String,
                       customerEmail: String,
                       totalPrice: Double
                     )

  /**
    * Represents an order line item in db
    * @param id the line id
    * @param phoneId the phone id
    * @param quantity the quantity of phones
    * @param subtotal the price of the line item (quantity * price of a single phone)
    * @param orderId the order id which belongs to
    */
  case class OrderLineItemRow(
                               id: Option[Long],
                               phoneId: Int,
                               quantity: Int,
                               subtotal: Double,
                               orderId: Long
                             )
}
