
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}
import com.jotason.phone.v1.inventory.repository.{InventoryRepository, SlickInventoryRepository}
import com.jotason.phone.v1.inventory.services.{InventoryService, InventoryServiceImpl}

/**
  * Manage DI with Guice
  */
class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule {

  override def configure() = {
    bind(classOf[InventoryRepository]).to(classOf[SlickInventoryRepository])
    bind(classOf[InventoryService]).to(classOf[InventoryServiceImpl])
  }
}
