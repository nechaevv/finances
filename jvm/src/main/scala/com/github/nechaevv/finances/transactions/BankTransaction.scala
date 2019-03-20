package com.github.nechaevv.finances.transactions

import java.util.UUID

case class BankTransaction
(
  organizationId: UUID,
  bankTransactionId: Int,
  bankAccountId: Int,
  description: String,
  referenceId: Option[String]
)
