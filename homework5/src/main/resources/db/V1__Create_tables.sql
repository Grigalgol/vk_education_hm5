CREATE TABLE product
(
    name          VARCHAR NOT NULL,
    internal_code INT     NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY (internal_code)
);

CREATE TABLE organization
(
    name            VARCHAR NOT NULL,
    inn             INT     NOT NULL,
    payment_account VARCHAR NOT NULL,
    CONSTRAINT organization_pk PRIMARY KEY (inn)
);

CREATE TABLE invoice
(
    id                  SERIAL    NOT NULL,
    date                TIMESTAMP NOT NULL,
    organization_sender INT       NOT NULL REFERENCES organization (inn) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT invoice_pk PRIMARY KEY (id)
);

CREATE TABLE invoice_item
(
    price      INT NOT NULL,
    product    INT NOT NULL REFERENCES product (internal_code) ON UPDATE CASCADE ON DELETE CASCADE,
    count      INT NOT NULL,
    id_invoice INT NOT NULL REFERENCES invoice (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT invoice_item_pk PRIMARY KEY (id_invoice, product)
);