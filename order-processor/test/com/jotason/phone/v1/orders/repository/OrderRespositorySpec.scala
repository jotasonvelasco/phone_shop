package com.jotason.phone.v1.orders.repository

import com.jotason.phone.v1.orders.dtos.{CreateNewOrderDto, OrderCreatedDto, OrderDto}
import com.jotason.phone.v1.orders.services.dto.PhoneDto
import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks
import utils.InMemoryDatabaseFlatSpec
import utils.json._

class OrderRespositorySpec extends InMemoryDatabaseFlatSpec
  with PropertyChecks
  with Matchers {

  private val orderRepo = app.injector.instanceOf[OrderRepository]

  "order repository" should "create an order" in {
    val createNewOrder = createValidNewOrderJson.validate[CreateNewOrderDto].get
    val phonesOrderedInCatalog = catalogJson.validate[List[PhoneDto]].get
    val expectedOrderCreated = orderCreatedJson.validate[OrderCreatedDto].get

    whenReady(orderRepo .create(createNewOrder, phonesOrderedInCatalog)){ result =>
      result shouldBe expectedOrderCreated
    }
  }

  "order repository" should "return an order by id" in {
    val expectedOrder = Some(orderJson.validate[OrderDto].get)

    whenReady(orderRepo.lookup(1)){ result =>
      result shouldBe expectedOrder
    }
  }

  "order repository" should "not return an order when searching by not exist id" in {
    whenReady(orderRepo.lookup(666)){ result =>
      result shouldBe None
    }
  }

}
