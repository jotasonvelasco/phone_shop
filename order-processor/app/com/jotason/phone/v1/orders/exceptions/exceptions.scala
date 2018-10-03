package com.jotason.phone.v1.orders.exceptions

object exceptions {

  class DBException(msg: String, cause: Throwable) extends Exception(msg, cause)
  class DiscontinuedPhonesException(msg: String) extends Exception(msg)
  class ParsingPhonesException(msg: String) extends Exception(msg)
  class PhoneShopApiClientException(msg: String) extends Exception(msg)

}
