package com.jotason.phone.v1.orders.repository

import com.jotason.phone.v1.orders.repository.dbModel.{OrderLineItemRow, OrderRow}
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
  * Provide access to orders/order_line_items tables
  */
private[repository] trait TableInfoProvider { this: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  /**
    * The starting point for all queries on the orders table.
    */
  protected val orders = TableQuery[OrderTable]

  /**
    * The starting point for all queries on the order_line_items table.
    */
  protected val orderLineItems = TableQuery[OrderLineItemTable]


  /**
    * The orders table definition
    */
  protected class OrderTable(tag: Tag) extends Table[OrderRow](tag, "orders") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    /** The customer_name column */
    def customerName = column[String]("customer_name")

    /** The customer_surname column */
    def customerSurname = column[String]("customer_surname")

    /** The customer_email column */
    def customerEmail = column[String]("customer_email")

    /** The total_price column */
    def totalPrice = column[Double]("total_price")

    /**
      * This is the tables default "projection".
      *
      * It defines how the columns are converted to and from the OrderRow object.
      */
    def * =
      (id.?, customerName, customerSurname, customerEmail, totalPrice) <>
        ((OrderRow.apply _).tupled, OrderRow.unapply)
  }


  /**
    * The order_line_items table definition
    */
  protected class OrderLineItemTable(tag: Tag) extends Table[OrderLineItemRow](tag, "order_line_items") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    /** The phone_id column */
    def phoneId = column[Int]("phone_id")

    /** The customer_surname column */
    def quantity = column[Int]("quantity")

    /** The customer_surname column */
    def subtotal = column[Double]("subtotal")

    def orderId = column[Long]("order_id")

    /**
      * This is the tables default "projection".
      *
      * It defines how the columns are converted to and from the OrderLineItemRow object.
      */
    def * =
      (id.?, phoneId, quantity, subtotal, orderId) <>
        ((OrderLineItemRow.apply _).tupled, OrderLineItemRow.unapply)

    def order =
      foreignKey("order_id", orderId, orders)(_.id)
  }

}
