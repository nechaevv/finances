create table "Transactions"
(
  "OrganizationId" uuid not null,
  "AccountId" int not null,
  "TransactionId" bigserial,
  "TransactionDate" timestamp not null,
  "Description" varchar(250),
  "Amount" numeric(14,2) not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "AccountId", "TransactionId"),
  constraint "Transactions_Accounts_fkey" foreign key ("OrganizationId", "AccountId")
    references "Accounts"("OrganizationId", "AccountId")
);

create table "BankTransactions"
(
  "OrganizationId" uuid not null,
  "BankAccountId" int not null,
  "BankTransactionId" bigint not null,
  "ReferenceId" varchar(50),
  primary key ("OrganizationId", "BankAccountId", "BankTransactionId"),
  constraint "BankTransactions_Transactions_fkey" foreign key ("OrganizationId", "BankAccountId", "BankTransactionId")
    references "Transactions"("OrganizationId", "AccountId", "TransactionId"),
  constraint "BankTransactions_BankAccounts_fkey" foreign key ("OrganizationId", "BankAccountId")
    references "BankAccounts"("OrganizationId", "BankAccountId"),
  unique ("OrganizationId", "BankAccountId", "ReferenceId")
);
