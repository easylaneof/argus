INSERT INTO chat(id)
VALUES (1),
       (2);

INSERT INTO link(id, url, updated_at)
VALUES (1000, 'https://github.com/easylaneof/easylaneof', TIMESTAMP '1999-01-08 04:05:06'),
       (1001, 'https://stackoverflow.com/questions/123321/my-awesome-question', TIMESTAMP '2000-03-20 10:10:10');

INSERT INTO subscription(chat_id, link_id)
VALUES (1, 1000),
       (1, 1001),
       (2, 1000);
