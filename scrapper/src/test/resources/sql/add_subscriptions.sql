INSERT INTO chat(id)
VALUES (1),
       (2);

INSERT INTO link(id, url)
VALUES (1000, 'https://github.com/easylaneof/easylaneof'),
       (1001, 'https://stackoverflow.com/questions/123321/my-awesome-question');

INSERT INTO subscription(chat_id, link_id)
VALUES (1, 1000),
       (1, 1001),
       (2, 1000);
