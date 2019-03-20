package com.github.nechaevv.finances.imports

import cats.effect.IO

trait BankTransactionsImporter {
  def importAccountRecords(data: Array[Byte]): IO[BankAccountTransactionsRecord]
}
