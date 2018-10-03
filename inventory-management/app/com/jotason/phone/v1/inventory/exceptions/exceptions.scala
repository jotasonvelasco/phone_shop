package com.jotason.phone.v1.inventory.exceptions

object exceptions {

  class DBException(msg: String, cause: Throwable) extends Exception(msg, cause)

}
