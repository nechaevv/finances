create table "Accounts"
(
  "OrganizationId" uuid not null references "Organizations"("OrganizationId"),
  "AccountId" serial,
  "Name" varchar(100) not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "AccountId")
);

create table "BankAccounts"
(
  "OrganizationId" uuid not null,
  "BankAccountId" int not null,
  "BankCounterpartyId" int not null,
  "ReferenceId" varchar(30),
  primary key ("OrganizationId", "BankAccountId"),
  constraint "BankAccounts_Accounts_fkey" foreign key ("OrganizationId", "BankAccountId")
    references "Accounts"("OrganizationId", "AccountId"),
  constraint "BankAccounts_Banks_fkey" foreign key ("OrganizationId", "BankCounterpartyId")
    references "Banks"("OrganizationId", "BankCounterpartyId"),
  unique ("OrganizationId", "BankCounterpartyId", "ReferenceId")
);

create table "DebtAccounts"
(
  "OrganizationId" uuid not null,
  "AccountId" int not null,
  "CounterpartyId" int not null,
  primary key ("OrganizationId", "AccountId"),
  constraint "DebtAccounts_Accounts_fkey" foreign key ("OrganizationId", "AccountId")
    references "Accounts"("OrganizationId", "AccountId"),
  constraint "DebtAccounts_Counterparties_fkey" foreign key ("OrganizationId", "CounterpartyId")
    references "Counterparties"("OrganizationId", "CounterpartyId")
)
