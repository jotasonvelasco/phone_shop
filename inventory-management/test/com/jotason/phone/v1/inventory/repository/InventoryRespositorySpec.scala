package com.jotason.phone.v1.inventory.repository

import com.jotason.phone.v1.inventory.dtos.PhoneDto
import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks
import utils.InMemoryDatabaseFlatSpec

import utils.json._

class InventoryRespositorySpec extends InMemoryDatabaseFlatSpec
    with PropertyChecks
    with Matchers {


  private val inventoryRepo = app.injector.instanceOf[InventoryRepository]

  "inventory repository" should "return a phone when searching by id" in {
    val expectedPhone = phoneJson.validate[PhoneDto].get
      whenReady(inventoryRepo.lookup(1)){ result =>
        result shouldBe Some(expectedPhone)
      }
  }

  "inventory repository" should "not return a phone when searching by not exist id" in {
    whenReady(inventoryRepo.lookup(111)){ result =>
      result shouldBe None
    }
  }

  "inventory repository" should "return the catalog" in {
    val expectedCatalog = catalogJson.validate[List[PhoneDto]].get
    whenReady(inventoryRepo.all()){ result =>
      result.size shouldBe 3
      result shouldBe expectedCatalog
    }
  }

}


