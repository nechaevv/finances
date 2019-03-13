create table "Organizations"
(
  "OrganizationId" uuid primary key,
  "Name" varchar(200),
  "CreatedOn" timestamp not null default now()
);

create table "Banks"
(
  "BankId" serial primary key,
  "Name" varchar(100) not null,
  "ReferenceId" varchar(50) unique,
  "CreatedOn" timestamp not null default now()
);

create table "BankAccounts"
(
  "OrganizationId" uuid,
  "BankAccountId" serial,
  "Description" varchar(100) not null,
  "ReferenceId" varchar(30),
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "BankAccountId"),
  unique ("OrganizationId", "ReferenceId")
);

create table "BankTransactions"
(
  "OrganizationId" uuid not null,
  "BankTransactionId" serial,
  "BankAccountId" int not null,
  "TransactionDate" timestamp not null,
  "PostingDate" timestamp not null,
  "ReferenceId" varchar(50),
  "Description" varchar(250),
  "Amount" numeric(14,2) not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "BankTransactionId"),
  foreign key ("OrganizationId", "BankAccountId") references "BankAccounts"("OrganizationId", "BankAccountId"),
  unique ("OrganizationId", "BankAccountId", "ReferenceId")
)
