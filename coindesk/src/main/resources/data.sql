DROP TABLE IF EXISTS coin;

CREATE TABLE coin (
  code VARCHAR(250) NOT NULL PRIMARY KEY,
  chineseCode VARCHAR(250) NOT NULL
);

INSERT INTO coin (code, chineseCode) VALUES
  ('USD', '美金'),
  ('GBP', '英鎊'),
  ('EUR', '歐元');