package com.jotason.phone.v1.orders.repository


/**
  * Builder utility for building URLs
  */
trait URLBuilder { this: ConfigurationLike =>

  private val hostDomainInventory = "inventory.host.domain"
  private val hostPortInventory = "inventory.host.port"

  private val hostDomainOrderProcessor = "order_processor.host.domain"
  private val hostPortOrderProcessor = "order_processor.host.port"


  /**
    * Build the absolute url of a phone
    * @param phoneId the phone id
    * @return absolute url of a phone
    */
  def buildPhoneUrl(phoneId: Int): String = {
    val port = configuration.get[String](hostPortInventory)
    val domain = configuration.get[String](hostDomainInventory)
    s"http://$domain:$port/v1/phones/$phoneId"
  }

  /**
    * Build the absolute url of an order
    * @param orderId the order id
    * @return absolute url of the order
    */
  def buildOrderUrl(orderId: Long): String = {
    val port = configuration.get[String](hostPortOrderProcessor)
    val domain = configuration.get[String](hostDomainOrderProcessor)
    s"http://$domain:$port/v1/orders/$orderId"
  }


}
