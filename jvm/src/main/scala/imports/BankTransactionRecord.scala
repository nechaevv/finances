package imports

import java.time.LocalDateTime

case class BankAccountTransactionsRecord
(
  bankReferenceId: String,
  transactions: Seq[BankTransactionRecord],
  balance: Option[AccountBalanceRecord],
)

case class BankTransactionRecord
(

  accountReferenceId: String,
  transactionReferenceId: String,
  transactionDate: LocalDateTime,
  postedDate: LocalDateTime, amount: BigDecimal
)

case class AccountBalanceRecord
(
  amount: BigDecimal,
  asOfDate: LocalDateTime
)
