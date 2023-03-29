--liquibase formatted sql

--changeset easylaneof:add-chat-table
CREATE TABLE chat (
    id BIGSERIAL PRIMARY KEY
);

--changeset easylaneof:add-link-table
CREATE TABLE link (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(2048) NOT NULL, -- https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
    chat_id BIGSERIAL REFERENCES chat(id),

    UNIQUE (chat_id, url)
);
