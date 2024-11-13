-- liquibase formatted sql
-- changeset pfb:vector.sql runOnChange:true

CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS vector_store
(
    id        UUID PRIMARY KEY,
    content   TEXT NOT NULL,
    metadata  JSONB,
    embedding VECTOR(4096) -- Replace 4096 with your vector dimension
);
