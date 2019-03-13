package transactions

import java.util.UUID

case class BankTransaction(organizationId: UUID, bankTransactionId: Int, bankAccountId: Int)
