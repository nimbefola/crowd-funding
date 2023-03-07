
CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

DROP TABLE IF EXISTS  project;
create TABLE project (
    "id" VARCHAR(255) NOT NULL,
    "created" TIMESTAMP NOT NULL,
    "updated" TIMESTAMP NOT NULL,
    "version" INT8 DEFAULT 0,
    "account_id" VARCHAR(255) NOT NULL,
    "title" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL,
    "supporting_image_url" VARCHAR(255) NULL,
    "amount_expected" VARCHAR(255) NULL,
    "amount_contributed" VARCHAR(255) NULL,
    "end_date" VARCHAR(255) NULL,
    "status" VARCHAR(255)  NULL,
    primary key (id)
);