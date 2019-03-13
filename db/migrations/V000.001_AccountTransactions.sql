create table "BankAccounts" (
  "AccountId" serial primary key,
  "Description" varchar(100) not null,
  "Identifier" varchar(30)
);

create type "BankTransactionType" as enum ('DEBIT', 'CREDIT');

create table "BankTransactions" (
  "BankTransactionId" serial primary key,
  "AccountId" int references "BankAccounts"("AccountId"),
  "Date" timestamp not null,
  "Transaction" varchar,
  "Amount" numeric(14,2)
)
