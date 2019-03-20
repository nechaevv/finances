package com.github.nechaevv.finances.imports
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import cats.effect.IO
import org.parboiled2._

object OfxSgmlImporter extends BankTransactionsImporter {
  private val log = org.log4s.getLogger

  private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
  override def importAccountRecords(data: Array[Byte]): IO[BankAccountTransactionsRecord] = IO({
    import Parser.DeliveryScheme.Either
    new OfxSgmlParser(ParserInput(data)).root.run()
      .map(ofxData ⇒
        BankAccountTransactionsRecord(
          ofxData.bankData.fid,
          ofxData.transactions.map(tx ⇒ {
            val postedDate = LocalDate.from(dateFormatter.parse(tx.datePosted.take(8))).atStartOfDay()
            val txDate = tx.dateUser.fold(postedDate)(dt ⇒ {
              LocalDate.from(dateFormatter.parse(dt.take(8))).atStartOfDay()
            })
            BankTransactionRecord(
              ofxData.accountId,
              tx.id,
              txDate,
              postedDate,
              BigDecimal(tx.amount),
              unescape(tx.name)
            )
          }),
          None
        )
      )
  }).flatMap(IO.fromEither)

  def unescape(s: String): String = {
    scala.xml.Utility.Escapes.escMap.foldLeft(s)((s,kv) ⇒ s.replaceAll(kv._2, kv._1.toString))
  }
}

case class OfxData(bankData: BankData, accountId: String, transactions: Seq[TransactionData])
case class BankData(org: String, fid: String)
case class TransactionData(datePosted: String, dateUser: Option[String], amount: String, id: String, name: String)

class OfxSgmlParser(val input: ParserInput) extends Parser {
  import CharPredicate._
  import OfxParser._

  def root = rule {
    zeroOrMore(header) ~
      zeroOrMore(&(!fiTag) ~ anyTag) ~
      fiTag ~
      zeroOrMore(&(!acctIdTag) ~ anyTag) ~
      acctIdTag ~
      zeroOrMore(&(!transactionTag) ~ anyTag) ~
      oneOrMore(transactionTag) ~> OfxData
  }

  def header = rule { oneOrMore(AlphaNum) ~ ':' ~ oneOrMore(AlphaNum) ~ oneOrMore(NewLine) }

  def transactionTag = rule {
    "<STMTTRN>" ~ zeroOrMore(WhiteSpaceChar) ~
    zeroOrMore(&(!datePostedTag) ~ anyTag) ~
    datePostedTag ~
    dateUserTag.? ~
    zeroOrMore(&(!amountTag) ~ anyTag) ~
    amountTag ~
    zeroOrMore(&(!trnIdTag) ~ anyTag) ~
    trnIdTag ~
    zeroOrMore(&(!nameTag) ~ anyTag) ~
    nameTag ~
    zeroOrMore(&(!"</STMTTRN>") ~ anyTag) ~
    "</STMTTRN>" ~ zeroOrMore(WhiteSpaceChar) ~> TransactionData
  }

  def fiTag = rule { "<FI>" ~ zeroOrMore(WhiteSpaceChar) ~ orgTag ~ fidTag ~ "</FI>" ~ zeroOrMore(WhiteSpaceChar) ~> BankData }

  def orgTag = rule { "<ORG>" ~ capture(text) ~ zeroOrMore(WhiteSpaceChar) }
  def fidTag = rule { "<FID>" ~ capture(text) ~ zeroOrMore(WhiteSpaceChar) }

  def acctIdTag = rule { "<ACCTID>" ~ capture(text) ~ zeroOrMore(WhiteSpaceChar) }

  def anyTag = rule { '<' ~ text /* ~ optional('/') ~ oneOrMore(AlphaNum + '.') ~ '>' ~ text */ ~ zeroOrMore(WhiteSpaceChar)}

  def datePostedTag = rule { "<DTPOSTED>" ~ capture(date) ~ text ~ zeroOrMore(WhiteSpaceChar) }
  def dateUserTag = rule { "<DTUSER>" ~ capture(date) ~ text ~ zeroOrMore(WhiteSpaceChar) }
  def amountTag = rule { "<TRNAMT>" ~ capture(number) ~ zeroOrMore(WhiteSpaceChar) }
  def trnIdTag = rule { "<FITID>" ~ capture(text) ~ zeroOrMore(WhiteSpaceChar) }
  def nameTag = rule { "<NAME>" ~ capture(text) ~ zeroOrMore(WhiteSpaceChar)}

  def number = rule { optional('-') ~ oneOrMore(Digit) ~ optional('.' ~ oneOrMore(Digit)) }
  def date = rule { oneOrMore(Digit) }
  def text = rule { zeroOrMore(All -- Seq('\n','\r','<')) }

}

object OfxParser {
  val WhiteSpaceChar = CharPredicate(" \n\r\t\f")
  val NewLine = CharPredicate("\n\r")
}
