create type "TransactionCategoryType" as enum ('INCOME', 'EXPENSE', 'TRANSFER');

create table "TransactionCategories"
(
  "OrganizationId" uuid not null references "Organizations"("OrganizationId"),
  "CategoryId" serial,
  "Name" varchar(200) not null,
  "CategoryType" "TransactionCategoryType" not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "CategoryId")
);


create table "TransactionClassificationPatterns"
(
  "OrganizationId" uuid not null references "Organizations"("OrganizationId"),
  "ClassificationPatternId" serial,
  "CategoryId" int not null,
  "MatchContent" varchar(250) not null,
  "MinRank" float not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "ClassificationPatternId"),
  constraint "TransactionClassificationPatterns_TransactionCategories_fkey" foreign key ("OrganizationId", "CategoryId")
    references "TransactionCategories"("OrganizationId", "CategoryId")
);

create table "Ledger"
(
  "OrganizationId" uuid not null references "Organizations"("OrganizationId"),
  "EquityAccountId" int not null,
  primary key ("OrganizationId"),
  constraint "Ledger_Accounts_EquityAccount_fkey" foreign key ("OrganizationId", "EquityAccountId")
    references "Accounts"("OrganizationId", "AccountId")
);

create table "LedgerTransactions"
(
  "OrganizationId" uuid not null,
  "LedgerTransactionId" bigserial,
  "DebitAccountId" int not null,
  "DebitTransactionId" bigint not null,
  "CreditAccountId" int not null,
  "CreditTransactionId" bigint not null,
  "CategoryId" int not null,
  "Amount" numeric(14,2),
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "LedgerTransactionId"),
  constraint "LedgerTransactions_Transactions_DebitTransaction_fkey" foreign key ("OrganizationId", "DebitAccountId", "DebitTransactionId")
    references "Transactions"("OrganizationId", "AccountId", "TransactionId"),
  constraint "LedgerTransactions_Transactions_CreditTransaction_fkey" foreign key ("OrganizationId", "CreditAccountId", "CreditTransactionId")
    references "Transactions"("OrganizationId", "AccountId", "TransactionId"),
  constraint "LedgerTransactions_TransactionCategories_fkey" foreign key ("OrganizationId", "CategoryId")
    references "TransactionCategories"("OrganizationId", "CategoryId")
);
