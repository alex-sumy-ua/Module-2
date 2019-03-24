CREATE table "USERS" (
    "ID"               NUMBER NOT NULL,
    "NAME"             VARCHAR2(30) NOT NULL,
    "MAX_TOTAL_POINTS" NUMBER,
    constraint  "USERS_PK" primary key ("ID")
)
/

CREATE sequence "USERS_SEQ"
/

CREATE trigger "BI_USERS"
  before insert on "USERS"
  for each row
begin
  if :NEW."ID" is null then
    select "USERS_SEQ".nextval into :NEW."ID" from dual;
  end if;
end;
/
