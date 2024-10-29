CREATE TABLE MAIN_A
(
    id        INT PRIMARY KEY,
    object_id VARCHAR NOT NULL
);

CREATE UNIQUE INDEX main_a_object_id_uindex
    ON MAIN_A (object_id);

CREATE TABLE SUB_A
(
    id      INT PRIMARY KEY,
    field_a VARCHAR,
    main_id INT REFERENCES MAIN_A (id)
);

CREATE INDEX sub_a_main_index ON SUB_A(main_id);

CREATE TABLE MAIN_B
(
    id        INT PRIMARY KEY,
    object_id VARCHAR NOT NULL
);

CREATE TABLE SUB_B
(
    id      INT PRIMARY KEY,
    field_b VARCHAR,
    main_id INT REFERENCES MAIN_B (id)
);

CREATE INDEX sub_b_main_index ON SUB_B (main_id);

CREATE UNIQUE INDEX main_b_object_id_uindex
    ON MAIN_B (object_id);

