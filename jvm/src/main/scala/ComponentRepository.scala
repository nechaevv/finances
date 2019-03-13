import cats.effect.{ContextShift, IO}
import com.typesafe.config.{Config, ConfigFactory}
import doobie.util.transactor.Transactor

import scala.concurrent.ExecutionContext

object ComponentRepository {
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit lazy val config: Config = ConfigFactory.load()
  implicit lazy val transactor: Transactor[IO] = Transactor.fromDriverManager[IO](
    config.getString("db.driver"),
    config.getString("db.url"),
    config.getString("db.user"),
    config.getString("db.password")
  )
}
