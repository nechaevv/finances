create table "Organizations"
(
  "OrganizationId" uuid primary key,
  "Name" varchar(200),
  "CreatedOn" timestamp not null default now()
);

create table "Counterparties"
(
  "OrganizationId" uuid not null references "Organizations"("OrganizationId"),
  "CounterpartyId" serial,
  "Name" varchar(200) not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "CounterpartyId")
);

create table "Banks"
(
  "OrganizationId" uuid not null,
  "BankCounterpartyId" int not null,
  "ReferenceId" varchar(50) unique,
  primary key ("OrganizationId", "BankCounterpartyId"),
  constraint "Banks_Counterparties_fkey"  foreign key ("OrganizationId", "BankCounterpartyId")
    references "Counterparties"("OrganizationId", "CounterpartyId"),
  unique ("OrganizationId", "ReferenceId")
);
