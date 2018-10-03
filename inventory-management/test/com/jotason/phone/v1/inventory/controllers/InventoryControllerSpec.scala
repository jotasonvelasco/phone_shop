package com.jotason.phone.v1.inventory.controllers

import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks
import utils.InMemoryDatabaseFlatSpec
import utils.json._
import play.api.test._
import play.api.test.Helpers._


class InventoryControllerSpec extends InMemoryDatabaseFlatSpec
  with PropertyChecks
  with Matchers {

  private val inventoryController = app.injector.instanceOf[InventoryController]
  private val inventory_path = "/v1/phones"

  "inventory rest api" should "return the catalog" in {
    val futureResult = inventoryController.list().apply(FakeRequest("GET", inventory_path))
    status(futureResult) shouldBe OK
    contentAsJson(futureResult) shouldBe catalogJson
  }

  "inventory rest api" should "return a phone if exists" in {
    val futureResult = inventoryController.get(1).apply(FakeRequest("GET", s"$inventory_path/"))
    status(futureResult) shouldBe OK
    contentAsJson(futureResult) shouldBe phoneJson
  }

  "inventory rest api" should "not return a phone if not exists" in {
    val futureResult = inventoryController.get(112).apply(FakeRequest("GET", s"$inventory_path/"))
    status(futureResult) shouldBe NOT_FOUND
    contentAsString(futureResult) should include ("Phone not found")
  }

}
