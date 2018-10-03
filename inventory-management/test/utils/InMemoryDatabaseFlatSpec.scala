package utils

import org.scalatest.FlatSpec
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

object InMemoryDatabaseFlatSpec {
  private val inMemoryDatabaseConfiguration: Map[String, Any] = Map(
    "slick.dbs.default.profile"     -> "slick.jdbc.H2Profile$",
    "slick.dbs.default.driver"      -> "slick.driver.H2Driver$",
    "slick.dbs.default.db.driver"   -> "org.h2.Driver",
    "slick.dbs.default.db.url"      -> "jdbc:h2:mem:play;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=FALSE",
    "slick.dbs.default.db.user"     -> "",
    "slick.dbs.default.db.password" -> "",
    "play.evolutions.enabled"       -> "true"
  )

}
abstract class InMemoryDatabaseFlatSpec extends FlatSpec with ScalaFutures with GuiceOneAppPerSuite {

  import InMemoryDatabaseFlatSpec._

  override def fakeApplication(): Application = {
    val builder = overrideDependencies(
      new GuiceApplicationBuilder()
        .configure(inMemoryDatabaseConfiguration)
    )
    builder.build()
  }

  def overrideDependencies(application: GuiceApplicationBuilder): GuiceApplicationBuilder = {
    application
  }

}
