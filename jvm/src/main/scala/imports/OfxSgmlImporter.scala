package imports
import java.io.InputStream

import cats.effect.IO
import org.parboiled2._

/*
object OfxSgmlImporter extends BankTransactionsImporter {
  override def importAccountRecords(inputStream: InputStream): IO[BankAccountTransactionsRecord] = IO {
  }
}
*/
class OfxSgmlParser(val input: ParserInput) extends Parser {
  import CharPredicate._
  import OfxParser._

  def header = rule { capture(AlphaNum) ~ ':' ~ capture(AlphaNum) ~ zeroOrMore(WhiteSpaceChar) ~> Header.apply }

  def containerTag = rule { capture(oneOrMore(valueMap(ContainerTags))) ~ drop[Unit] }

  def number = rule { optional('-') ~ oneOrMore(Digit) ~ optional('.' + oneOrMore(Digit)) }
  def date = rule { oneOrMore(Digit) }
  def text = rule { zeroOrMore(!(CharPredicate('<') | EOI)) }


}

object OfxParser {
  val WhiteSpaceChar = CharPredicate(" \n\r\t\f")
  val ContainerTags: Map[String, Unit] = Map(Seq(
    "BANKMSGSRSV1",
    "BANKTRANLIST",
    "CCACCTFROM",
    "CCSTMTRS",
    "CCSTMTTRNRS",
    "CREDITCARDMSGSRSV1",
    "FI",
    "LEDGERBAL",
    "OFX",
    "SIGNONMSGSRSV1",
    "SONRS",
    "STATUS",
    "STMTTRN"
  ).map(_ → ()):_*)
}

case class OFX(headers: Seq[(String, String)], header: Tag, message: Tag)
case class Header(name: String, value: String)
trait Tag { def name: String }
case class ContainerTag(name: String, content: Seq[Tag]) extends Tag
case class ValueTag(name: String, value: String) extends Tag
