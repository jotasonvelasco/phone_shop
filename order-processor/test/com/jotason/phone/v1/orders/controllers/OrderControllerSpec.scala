package com.jotason.phone.v1.orders.controllers

import akka.stream.ActorMaterializer
import com.jotason.phone.v1.orders.dtos.OrderCreatedDto
import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks
import utils.InMemoryDatabaseFlatSpec
import utils.json._
import play.api.test._
import play.api.test.Helpers._


class OrderControllerSpec extends InMemoryDatabaseFlatSpec
  with PropertyChecks
  with Matchers {

  private val orderController = app.injector.instanceOf[OrderController]

  implicit val system = app.actorSystem
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val order_path = "/v1/orders"

  "order rest api" should "create an order" in {
    val futureResult = orderController.save().apply(FakeRequest("POST", order_path)
      .withHeaders("Content-Type" -> "application/json")
      .withBody(createValidNewOrderJson))

    val result = contentAsJson(futureResult).validate[OrderCreatedDto].get

    status(futureResult) shouldBe CREATED
    result.orderId should not be 0
    result.location should not be ""
  }

  "order rest api" should "not create an order if the list of items is empty" in {
    val futureResult = orderController.save().apply(FakeRequest("POST", order_path)
      .withHeaders("Content-Type" -> "application/json")
      .withBody(createNoItemsNewOrderJson))

    status(futureResult) shouldBe BAD_REQUEST
  }

  "order rest api" should "not create an order if any of the phones requested is not in catalog" in {
    val futureResult = orderController.save().apply(FakeRequest("POST", order_path)
      .withHeaders("Content-Type" -> "application/json")
      .withBody(createNotCatalogedPhoneNewOrderJson))

    status(futureResult) shouldBe BAD_REQUEST
    // Requested phone id 111 is not in catalog
    contentAsString(futureResult) should include ("111")
  }

  "order rest api" should "not create an order if the phones quantity is 0" in {
    val futureResult = orderController.save().apply(FakeRequest("POST", order_path)
      .withHeaders("Content-Type" -> "application/json")
      .withBody(createQuantityZeroPhoneNewOrderJson))

    status(futureResult) shouldBe BAD_REQUEST
  }

  "order rest api" should "not create an order if the customer name is empty" in {
    val futureResult = orderController.save().apply(FakeRequest("POST", order_path)
      .withHeaders("Content-Type" -> "application/json")
      .withBody(createNotNameNewOrderJson))

    status(futureResult) shouldBe BAD_REQUEST
  }

  "order rest api" should "not create an order if the customer email is invalid" in {
    val futureResult = orderController.save().apply(FakeRequest("POST", order_path)
      .withHeaders("Content-Type" -> "application/json")
      .withBody(createEmailInvalidNewOrderJson))

    status(futureResult) shouldBe BAD_REQUEST
  }

  "order rest api" should "return an order if exists" in {
    // First, create an order and get the orderId generated
    val futureCreateResult = orderController.save().apply(FakeRequest("POST", order_path)
      .withHeaders("Content-Type" -> "application/json")
      .withBody(createValidNewOrderJson))
    val orderId = (contentAsJson(futureCreateResult) \ "orderId").as[Long]

    // Get the order created
    val futureGetOrderResult = orderController.get(orderId).apply(FakeRequest("GET", s"$order_path/"))
    status(futureGetOrderResult) shouldBe OK
    val resultOrder = contentAsJson(futureGetOrderResult)

    (resultOrder \ "lineItems") shouldBe (orderJson \ "lineItems")
    (resultOrder \ "totalPrice") shouldBe (orderJson \ "totalPrice")

  }

  "order rest api" should "not return an order if not exists" in {
    val futureResult = orderController.get(113).apply(FakeRequest("GET", s"$order_path/"))
    status(futureResult) shouldBe NOT_FOUND
    contentAsString(futureResult) should include ("Order not found")
  }

}
