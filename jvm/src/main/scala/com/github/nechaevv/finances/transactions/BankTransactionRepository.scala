package com.github.nechaevv.finances.transactions

import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}
import java.util.UUID

import cats.effect.IO
import cats.implicits._
import com.github.nechaevv.finances.imports.BankTransactionRecord
import doobie._
import doobie.implicits._
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.util.transactor.Transactor

class BankTransactionRepository()(implicit transactor: Transactor[IO]) {
  def updateRecords(orgId: UUID, transactions: Seq[BankTransactionRecord]): IO[Unit] = {
    transactions.groupBy(_.accountReferenceId).foldLeft(IO.pure[Unit](())) { (io, acctAndTxns) ⇒
      val (accountRefId, txns) = acctAndTxns
      io.flatMap(_ ⇒ (for {
        accountId ← sql"""select "BankAccountId" from "BankAccounts" where "ReferenceId"=$accountRefId """.query[Int].unique
        zone = ZoneId.systemDefault()
        transactions = txns.map(tx ⇒ (orgId, accountId, tx.transactionDate.atZone(zone).toInstant, tx.postedDate.atZone(zone).toInstant, tx.transactionReferenceId, tx.description, tx.amount))
        _ <- Update[(UUID, Int, Instant, Instant, String, String, BigDecimal)] (
          """insert into "BankTransactions"("OrganizationId", "BankAccountId", "TransactionDate", "PostingDate", "ReferenceId", "Description", "Amount")
            |values (?, ?, ?, ?, ?, ?, ?) on conflict do nothing""".stripMargin
        ).updateMany(transactions.toList)
      } yield ()).transact(transactor))
    }
  }
}
