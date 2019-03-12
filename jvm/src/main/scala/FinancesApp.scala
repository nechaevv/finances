import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.blaze.BlazeServerBuilder

object FinancesApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = BlazeServerBuilder[IO]
    .withNio2(true)
    .serve
    .compile.drain
    .map(_ ⇒ ExitCode.Success)
}
