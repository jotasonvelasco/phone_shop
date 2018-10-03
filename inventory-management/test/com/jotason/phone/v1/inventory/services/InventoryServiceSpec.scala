package com.jotason.phone.v1.inventory.services

import com.jotason.phone.v1.inventory.dtos.PhoneDto
import com.jotason.phone.v1.inventory.repository.{InventoryRepository, SlickInventoryRepository}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future
import utils.json._

class InventoryServiceSpec extends FlatSpec
  with ScalaFutures
  with Matchers
  with MockitoSugar {


  "inventory service" should "return the catalog empty when it has not items" in {
    val mockInventoryRepo = mock[InventoryRepository]
    when(mockInventoryRepo.all()).thenReturn(Future.successful(Seq.empty[PhoneDto]))
    val inventoryService = new InventoryServiceImpl(mockInventoryRepo)

    whenReady(inventoryService.getPhoneCatalog()){ result =>
      result shouldBe Seq.empty[PhoneDto]
    }
  }

  "inventory service" should "return the catalog when it has items" in {
    val catalog = catalogJson.validate[Seq[PhoneDto]].get
    val mockInventoryRepo = mock[InventoryRepository]
    when(mockInventoryRepo.all()).thenReturn(Future.successful(catalog))
    val inventoryService = new InventoryServiceImpl(mockInventoryRepo)

    whenReady(inventoryService.getPhoneCatalog()){ result =>
      result shouldBe catalog
    }
  }

}
