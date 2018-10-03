
import com.google.inject.AbstractModule
import com.jotason.phone.v1.orders.repository.{OrderRepository, SlickOrderRepository}
import com.jotason.phone.v1.orders.services.client.{PhoneShopApiService, PhoneShopApiServiceImpl}
import com.jotason.phone.v1.orders.services.{OrderService, OrderServiceImpl}
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}

/**
  * Manage DI with Guice
  */
class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule {

  override def configure() = {
    bind(classOf[OrderService]).to(classOf[OrderServiceImpl])
    bind(classOf[OrderRepository]).to(classOf[SlickOrderRepository])
    bind(classOf[OrderRepository]).to(classOf[SlickOrderRepository])
    bind(classOf[PhoneShopApiService]).to(classOf[PhoneShopApiServiceImpl])
  }
}
