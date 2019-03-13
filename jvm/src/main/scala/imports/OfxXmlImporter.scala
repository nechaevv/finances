package imports
import java.io.InputStream

import cats.effect.IO

import scala.xml.XML

object OfxXmlImporter extends BankTransactionsImporter {
  override def importAccountRecords(inputStream: InputStream): IO[BankAccountTransactionsRecord] = IO {
    val xml = XML.load(inputStream)
    //val bankId =
    ???
  }
}
