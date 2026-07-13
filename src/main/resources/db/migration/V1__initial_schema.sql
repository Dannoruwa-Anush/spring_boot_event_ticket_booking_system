-- ON UPDATE CASCADE:
-- If the parent key is updated, the corresponding foreign key values in the child table are automatically updated.

-- ON DELETE RESTRICT:
-- Prevents deletion of a parent row if matching rows exist in the child table.

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,

    role_id BIGINT,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT uq_users_email UNIQUE (email),
    
    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);