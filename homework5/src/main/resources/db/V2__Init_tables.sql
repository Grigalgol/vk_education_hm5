INSERT INTO product(name, internal_code)
VALUES ('lego', 100),
       ('barbie', 101),
       ('apple', 200),
       ('banana', 201);

INSERT INTO organization(name, inn, payment_account)
VALUES ('toy shop', 55555, 450012),
       ('fruit shop', 12345, 123124);

INSERT INTO invoice(id, date, organization_sender)
VALUES (1, '2022-11-05 12:00:00', 12345),
       (2, '2022-11-06 13:00:00', 12345),
       (3, '2022-11-07 14:00:00', 12345),
       (4, '2022-01-01 11:00:00', 55555),
       (5, '2022-01-02 15:00:00', 55555);

INSERT INTO invoice_item(id, id_invoice, product, price, count)
VALUES (1, 1, 200, 2000, 100),
       (2, 2, 201, 10000, 250),
       (3, 3, 200, 8500, 300),
       (4, 4, 100, 25000, 5),
       (5, 5, 101, 3000, 10);

