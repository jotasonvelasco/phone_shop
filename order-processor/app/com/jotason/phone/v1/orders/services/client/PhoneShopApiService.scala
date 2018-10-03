package com.jotason.phone.v1.orders.services.client

import com.jotason.phone.v1.orders.exceptions.exceptions.PhoneShopApiClientException
import com.jotason.phone.v1.orders.services.ApiClientIOExecutionContext
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient

import scala.concurrent.Future

trait PhoneShopApiService {
  def get(url: String): Future[JsValue]
}

@Singleton
class PhoneShopApiServiceImpl @Inject()(ws: WSClient)(implicit ec: ApiClientIOExecutionContext)
  extends PhoneShopApiService {

  private val logger = Logger(getClass)

  @throws(classOf[PhoneShopApiClientException])
  def get(url: String): Future[JsValue] = {
    ws.url(url).get().map { response =>
      response.status match {
        case Status.OK =>
          logger.debug(
            s"Response received from inventory api with status 200 and data: \n ${Json.prettyPrint(response.json)}")
          response.json
        case status =>
          val errorMsg = s"Error calling inventory api | status : $status"
          logger.error(errorMsg)
          throw new PhoneShopApiClientException(errorMsg)
      }
    }.recover {
      case ex: Exception =>
        val errorMsg = s"Error calling inventory api: ${ex.getMessage}"
        logger.error(errorMsg)
        throw new PhoneShopApiClientException(errorMsg)
    }
  }
}
