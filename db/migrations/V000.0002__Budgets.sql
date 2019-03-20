create table "TransactionClassifiers"
(
  "OrganizationId" uuid,
  "ClassifierId" serial,
  "Name" varchar(50) not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "ClassifierId")
);

create table "TransactionCategories"
(
  "OrganizationId" uuid,
  "CategoryId" serial,
  "ClassifierId" int not null,
  "Name" varchar(50) not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "CategoryId"),
  foreign key ("OrganizationId", "ClassifierId") references "TransactionClassifiers"("OrganizationId", "ClassifierId")
);

create table "BankTransactionClassifications"
(
  "OrganizationId" uuid,
  "BankTransactionId" int not null,
  "ClassifierId" int not null,
  "CategoryId" int not null,
  "CreatedOn" timestamp not null default now(),
  foreign key ("OrganizationId", "BankTransactionId") references "BankTransactions"("OrganizationId", "BankTransactionId"),
  foreign key ("OrganizationId", "ClassifierId", "CategoryId") references "TransactionCategories"("OrganizationId", "ClassifierId", "CategoryId"),
  unique ("OrganizationId", "BankTransactionId", "ClassifierId")
);

create table "Budgets"
(
  "OrganizationId" uuid,
  "BudgetId" serial,
  "Name" varchar(50) not null,
  "ClassifierId" int not null,
  "CreatedOn" timestamp not null default now(),
  primary key ("OrganizationId", "BudgetId"),
  foreign key ("OrganizationId", "ClassifierId") references "TransactionClassifiers"("OrganizationId", "ClassifierId")
);

