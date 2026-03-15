CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL CHECK (status IN ('NEW', 'IN_PROGRESS', 'DONE')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);