--liquibase formatted sql

--changeset easylaneof:add-chat-table
CREATE TABLE chat (
    id BIGSERIAL PRIMARY KEY
);

--changeset easylaneof:add-link-table
CREATE TABLE link (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(2048) NOT NULL UNIQUE, -- https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
    updated_at timestamptz NOT NULL
);

--changeset easylaneof:add-chat_link-table
CREATE TABLE chat_link (
    chat_id BIGINT REFERENCES chat(id) NOT NULL,
    link_id BIGINT REFERENCES link(id) NOT NULL
);
