

DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsert$$
create procedure loopInsert()
BEGIN
  declare i int default 1;
  while i <= 500 do
     insert into bboard(created_time, bwriter, contents, hits, pass, title) 
            values
            (now(), '홍길동', concat(i, '내용입니다.'), 0, '1234', concat(i, '제목입니다.'));
     SET i = i + 1;       
  end while;
END$$
DELIMITER $$

CALL loopInsert;


DROP PROCEDURE loopInsert;