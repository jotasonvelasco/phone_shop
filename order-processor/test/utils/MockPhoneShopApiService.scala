package utils

import com.jotason.phone.v1.orders.services.client.PhoneShopApiService
import play.api.libs.json.JsValue
import utils.json.catalogJson

import scala.concurrent.Future

class MockPhoneShopApiService extends PhoneShopApiService{
  override def get(url: String): Future[JsValue] = Future.successful(catalogJson)
}
