package com.jotason.phone.v1.inventory.services

import com.jotason.phone.v1.inventory.dtos.PhoneDto
import com.jotason.phone.v1.inventory.repository.InventoryRepository
import javax.inject.Inject

import scala.concurrent.Future

/**
  * Service for operations with Inventory
  */
trait InventoryService {

  /**
    * Get a phone corresponding with a given id.
    *
    * @param id the phone id
    * @return a future wrapping the phone
    *         Returns Future[None] if the phone is not found
    *
    */
  def getPhoneById(id: Int): Future[Option[PhoneDto]]

  /**
    * Get all the phones in the catalog.
    *
    * @return a future wrapping a sequence of all the phones in the catalog
    */
  def getPhoneCatalog(): Future[Seq[PhoneDto]]

}

class InventoryServiceImpl @Inject()(repo: InventoryRepository) extends InventoryService {

  def getPhoneById(id: Int): Future[Option[PhoneDto]] = repo.lookup(id)

  def getPhoneCatalog(): Future[Seq[PhoneDto]] = repo.all()

}

