-- liquibase formatted sql
-- changeset pfb:vector.sql runOnChange:true

CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS vector_store
(
    id        UUID PRIMARY KEY,
    content   TEXT NOT NULL,
    metadata  JSONB,
    embedding VECTOR(3072) -- Replace it with your vector dimension
);
