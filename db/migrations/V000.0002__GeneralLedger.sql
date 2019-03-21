create table "GeneralLedgers"
(
  "OrganizationId" uuid not null,
  "LedgerId" serial,
  "Name" varchar(50) not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "LedgerId")
);

create table "LedgerAccounts"
(
  "OrganizationId" uuid not null,
  "LedgerId" int not null,
  "AccountId" serial,
  "Name" varchar(150) not null,
  "BalanceAccountId" int not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "LedgerId", "AccountId"),
  foreign key ("OrganizationId", "LedgerId") references "GeneralLedgers"("OrganizationId", "LedgerId")
);

create table "LedgerTransactions"
(
  "OrganizationId" uuid not null,
  "LedgerId" int not null,
  "AccountId" int not null,
  "TransactionId" int not null,
  "Amount" numeric(14,2) not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "LedgerId", "AccountId", "TransactionId"),
  foreign key ("OrganizationId", "LedgerId", "AccountId") references "LedgerAccounts"("OrganizationId", "LedgerId", "AccountId")
);
