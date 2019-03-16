package imports

import java.nio.file.{Files, Paths}
import java.util.UUID

import cats.effect._
import org.parboiled2.ParseError

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
        log.info(s"Loading transactions from $fileName")
        OfxSgmlImporter.importAccountRecords(Files.readAllBytes(Paths.get(fileName))).map(data ⇒ {
          log.info(s"Loaded ${data.transactions.length} records for ${data.bankReferenceId}")
          for(tx ← data.transactions) log.info(s"Transaction: $tx")
        ExitCode.Success
      }).handleErrorWith({
        case ex: ParseError ⇒
          log.error("Parse error: " + ex.toString())
          ex.traces.foreach(trace ⇒ log.error(trace.toString))
          IO(ExitCode.Error)
        case ex ⇒
          log.error(ex)("Error")
          IO(ExitCode.Error)
      })
    }
  }

}
