CREATE table "QUESTIONS" (
    "ID"         NUMBER NOT NULL,
    "QUESTION"   VARCHAR2(256),
    "ANSWER"     VARCHAR2(256),
    "POINTS"     NUMBER,
    constraint  "QUESTIONS_PK" primary key ("ID")
)
/

CREATE sequence "QUESTIONS_SEQ"
/

CREATE trigger "BI_QUESTIONS"
  before insert on "QUESTIONS"
  for each row
begin
  if :NEW."ID" is null then
    select "QUESTIONS_SEQ".nextval into :NEW."ID" from dual;
  end if;
end;
/
