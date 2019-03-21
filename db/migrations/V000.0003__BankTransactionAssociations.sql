create table "BankAccountGedgerAssociation"
(
  "OrganizationId" uuid not null,
  "BankAccountId" serial,
  "LedgerId" int not null,
  "AccountId" int not null,
  "CreatedOn" timestamp not null default now(),
  unique ("OrganizationId", "BankAccountId", "LedgerId", "AccountId"),
  foreign key ("OrganizationId", "BankAccountId") references "BankAccounts"("OrganizationId", "BankAccountId"),
  foreign key ("OrganizationId", "LedgerId", "AccountId") references "LedgerAccounts"("OrganizationId", "LedgerId", "AccountId")
);

create table "BankTransactionLedgerAssociations"
(
  "OrganizationId" uuid not null,
  "BankAccountId" int not null,
  "BankTransactionId" int not null,
  "LedgerId" int not null,
  "AccountId" int not null,
  "TransactionId" int not null,
  "CreatedOn" timestamp not null default now(),
  unique ("OrganizationId", "BankAccountId", "BankTransactionId", "LedgerId", "AccountId", "TransactionId"),
  foreign key ("OrganizationId", "BankAccountId", "BankTransactionId") references "BankTransactions"("OrganizationId", "BankAccountId", "BankTransactionId"),
  foreign key ("OrganizationId", "LedgerId", "AccountId", "TransactionId") references "LedgerTransactions"("OrganizationId", "LedgerId", "AccountId", "TransactionId")
);

create table "BankTransactionDescriptionClassification"
(
  "OrganizationId" uuid not null,
  "LedgerId" int not null,
  "AccountId" int not null,
  "ClassificationId" serial,
  "MatchContent" varchar(250) not null,
  "MinRank" float not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "ClassificationId"),
  foreign key ("OrganizationId", "LedgerId", "AccountId") references "LedgerAccounts"("OrganizationId", "LedgerId", "AccountId")
);
