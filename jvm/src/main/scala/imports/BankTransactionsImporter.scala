package imports

import java.io.InputStream

import cats.effect.IO

trait BankTransactionsImporter {
  def importAccountRecords(inputStream: InputStream): IO[BankAccountTransactionsRecord]
}
