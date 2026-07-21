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

CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    address VARCHAR(255) NOT NULL,
    date_of_birth VARCHAR(50) NOT NULL,
    phone_no VARCHAR(20) NOT NULL,

    user_id BIGINT NOT NULL UNIQUE,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT fk_customer_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE TABLE staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    employee_no VARCHAR(50) NOT NULL,
    nic VARCHAR(20) NOT NULL,
    phone_no VARCHAR(20) NOT NULL,

    hire_date DATETIME NOT NULL,
    termination_date DATETIME,

    employment_status VARCHAR(50),

    user_id BIGINT NOT NULL UNIQUE,
    position_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT uq_staff_employee_no UNIQUE (employee_no),
    CONSTRAINT chk_staff_employee_no CHECK (employee_no REGEXP '^EMP[0-9]+$'), -- format: EMP + number
    CONSTRAINT uq_staff_nic UNIQUE (nic),

    CONSTRAINT fk_staff_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_staff_position
        FOREIGN KEY (position_id)
        REFERENCES positions(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);