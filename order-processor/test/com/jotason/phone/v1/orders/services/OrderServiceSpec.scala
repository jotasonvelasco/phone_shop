package com.jotason.phone.v1.orders.services

import com.jotason.phone.v1.orders.dtos.{CreateNewOrderDto,OrderDto}
import com.jotason.phone.v1.orders.exceptions.exceptions.DiscontinuedPhonesException
import org.scalatest.Matchers
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import utils.json._
import org.scalatest.prop.PropertyChecks
import utils.InMemoryDatabaseFlatSpec


class OrderServiceSpec extends InMemoryDatabaseFlatSpec
  with PropertyChecks
  with ScalaFutures
  with Matchers
  with IntegrationPatience
  with MockitoSugar {

  private val orderService = app.injector.instanceOf[OrderService]


  "order service" should "create an order when all the phones requested are in the catalog" in {
    val createNewOrder = createValidNewOrderJson.validate[CreateNewOrderDto].get

    whenReady(orderService.createOrder(createNewOrder)){ result =>
      result.orderId should not be 0
      result.location should not be empty
    }
  }

  "order service" should "not create an order when any phone requested are not the catalog" in {
    val createInvalidNewOrder = createNotCatalogedPhoneNewOrderJson.validate[CreateNewOrderDto].get

    val futureResult = orderService.createOrder(createInvalidNewOrder)
    whenReady(futureResult.failed){ result =>
      result shouldBe a [DiscontinuedPhonesException]

    }
  }

  "order service" should "return an order when the order exist" in {
    val order = orderJson.validate[OrderDto].get

    whenReady(orderService.getOrderById(1)){ result =>
      result shouldBe Some(order)
    }
  }

  "order service" should "not return an order when the order not exist" in {
    val order = orderJson.validate[OrderDto].get

    whenReady(orderService.getOrderById(111)){ result =>
      result shouldBe None
    }
  }

}
