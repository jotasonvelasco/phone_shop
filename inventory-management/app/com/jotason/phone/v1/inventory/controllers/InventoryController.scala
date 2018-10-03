package com.jotason.phone.v1.inventory.controllers

import com.jotason.phone.v1.inventory.exceptions.exceptions.DBException
import com.jotason.phone.v1.inventory.services.InventoryService
import javax.inject.Inject
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
  * Rest api controller for phones in the inventory
  */
class InventoryController @Inject()(cc: ControllerComponents, service: InventoryService)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  private val logger = Logger(getClass)

  /**
    * Return all the phones in the catalog.
    * @return A json list with all the phones in the catalog
    *         If the catalog is empty returns an empty json list
    */
  def list() = Action.async { implicit request =>
    logger.trace("Listing catalog")
    service.getPhoneCatalog().map { catalog =>
      val catalogJson = Json.toJson(catalog)
      logger.info(s"Catalog obtained:\n ${Json.prettyPrint(catalogJson)}")
      Ok(Json.toJson(catalogJson))
    }.recover(recoverPF)
  }

  /**
    * Return the phone corresponding with the given id
    * @param id the phone id
    * @return A json object with the phone requested
    *         If not exist returns NOT_FOUND
    */
  def get(id: Int) = Action.async { implicit request =>
    logger.trace(s"Searching phone id: $id")
    service.getPhoneById(id).map {
      case Some(phone) =>
        val phoneJson = Json.toJson(phone)
        logger.info(s"Phone found:\n ${Json.prettyPrint(phoneJson)}")
        Ok(phoneJson)
      case None =>
        val msg = s"Phone not found for id: $id"
        logger.info(msg)
        NotFound(Json.obj("message" -> s"Phone not found with id = $id"))
    }.recover(recoverPF)
  }

  private def recoverPF: PartialFunction[Throwable, Result] = {
    case ex: DBException => BadGateway(Json.obj("exception" -> ex.getMessage))
    case ex: Exception =>
      logger.error("An exception ocurred", ex)
      InternalServerError(Json.obj("exception" -> ex.getMessage))
  }
}
