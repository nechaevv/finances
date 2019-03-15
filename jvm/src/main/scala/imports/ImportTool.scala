package imports

import java.util.UUID

import cats.effect.{ExitCode, IO, IOApp}

object ImportTool extends IOApp {
  private val log = org.log4s.getLogger

  override def run(args: List[String]): IO[ExitCode] = args match {
    case orgId :: mode :: fileName :: Nil ⇒ doImportFile(UUID.fromString(orgId), mode, fileName)
    case _ ⇒ IO {
      log.error("Incorrect parameter list")
      ExitCode.Error
    }
  }

  def doImportFile(orgId: UUID, mode: String, fileName: String): IO[ExitCode] = {
    mode match {
      case "ofx-sgml" ⇒
    }
  }

}
