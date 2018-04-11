# --- !Ups

SET REFERENTIAL_INTEGRITY FALSE;

INSERT INTO student (id,name, stud_id) VALUES (0,'Ann',345533);
INSERT INTO student (id,name,stud_id) VALUES (1,'Tom',345534);
INSERT INTO student (id,name,stud_id) VALUES (2,'Helen',345535);
INSERT INTO student (id,name,stud_id) VALUES (3,'Helen',345536);

DROP SEQUENCE IF EXISTS student_seq;
CREATE SEQUENCE student_seq START WITH 10;

SET REFERENTIAL_INTEGRITY TRUE;