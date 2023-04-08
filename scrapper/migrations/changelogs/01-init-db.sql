--liquibase formatted sql

--changeset easylaneof:add-chat-table
CREATE TABLE IF NOT EXISTS chat (
    id BIGINT PRIMARY KEY
);

--changeset easylaneof:add-link-table
CREATE TABLE IF NOT EXISTS link (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(2048) NOT NULL UNIQUE, -- https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
    updated_at timestamptz NOT NULL
);

--changeset easylaneof:add-chat_link-table
CREATE TABLE IF NOT EXISTS chat_link (
    chat_id BIGINT REFERENCES chat(id) NOT NULL,
    link_id BIGINT REFERENCES link(id) NOT NULL,

    PRIMARY KEY (chat_id, link_id)
);
