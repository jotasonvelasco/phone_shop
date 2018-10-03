package com.jotason.phone.v1.inventory.repository

import com.jotason.phone.v1.inventory.repository.dbModel.PhoneRow
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

/**
  * Provide access to phones table
  */
trait TableInfoProvider extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  /**
    * The starting point for all queries on the phone table.
    */
  protected val phones = TableQuery[PhoneTable]

  protected val queryById = Compiled((id: Rep[Int]) => phones.filter(_.id === id))

  /**
    * The phones table definition
    */
  protected class PhoneTable(tag: Tag) extends Table[PhoneRow](tag, "phones") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def name = column[String]("name")

    /** The description column */
    def description = column[String]("description")

    /** The imageRef column */
    def imageRef = column[String]("image_ref")

    /** The price column */
    def price = column[Double]("price")

    /**
      * This is the tables default "projection".
      *
      * It defines how the columns are converted to and from the Phone object.
      */
    def * = (id, name, description, imageRef, price) <> ((PhoneRow.apply _).tupled, PhoneRow.unapply)
  }

}

