package com.jotason.phone.v1.inventory.repository

import com.jotason.phone.v1.inventory.dtos.PhoneDto
import com.jotason.phone.v1.inventory.exceptions.exceptions.DBException
import com.jotason.phone.v1.inventory.repository.dbModel.PhoneRow
import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Logger}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future


trait InventoryRepository extends ConfigurationLike {

  /**
    * List all the phones in the catalog.
    * @return A future wrapping a sequence of all the phones in the catalog.
    *         If catalog is empty return a future with an empty sequence
    */
  def all(): Future[Seq[PhoneDto]]

  /**
    * Return the phone with the id given
    * @param id the phone id
    * @return A future wrapping an Option with the phone for the id.
    *         If the id is not found return Future[None]
    */
  def lookup(id: Int): Future[Option[PhoneDto]]
}

/**
  * Inventory repository implemented with Slick.
  */
@Singleton
class SlickInventoryRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                                         val configuration: Configuration)(implicit ec: DBIOExecutionContext)
  extends TableInfoProvider
    with URLBuilder
    with InventoryRepository {

  import profile.api._

  private val logger = Logger(getClass)


  def all(): Future[Seq[PhoneDto]] = {
    logger.debug("Getting catalog from db")
    val future = db.run(phones.result)
    future.map { seq =>
      seq.map(phoneRowToPhone)
    }.recover(dbRecover)
  }

  def lookup(id: Int): Future[Option[PhoneDto]] = {
    logger.debug(s"Searching phone in db: $id")
    val future = db.run(queryById(id).result.headOption)
    future.map { maybeRow =>
      maybeRow.map(phoneRowToPhone)
    }.recover(dbRecover)
  }

  private def dbRecover[U]: PartialFunction[Throwable, U] = {
    case ex: Exception =>
      logger.error("Database error", ex)
      throw new DBException(s"Database error | msg = ${ex.getMessage}", ex)
  }

  /**
    * Map a PhoneRow(db domain) to a Phone
    *
    * @param phoneRow The PhoneRow to convert into
    * @return a Phone com.jotason.phone.v1.orders.dtos.controllers.dtos
    */
  private def phoneRowToPhone(phoneRow: PhoneRow) = {
    logger.debug(s"Mapping PhoneRow to PhoneDto: $phoneRow")
    val _imageUrl = buildImageUrl(phoneRow.imageRef)
    PhoneDto(
      id = phoneRow.id,
      name = phoneRow.name,
      description = phoneRow.description,
      imageUrl = _imageUrl,
      phoneRow.price
    )
  }

}

