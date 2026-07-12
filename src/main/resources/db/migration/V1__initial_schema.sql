-- ON UPDATE CASCADE:
-- If the parent key is updated, the corresponding foreign key values in the child table are automatically updated.

-- ON DELETE RESTRICT:
-- Prevents deletion of a parent row if matching rows exist in the child table.

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);


CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,

    role_id BIGINT,

    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);