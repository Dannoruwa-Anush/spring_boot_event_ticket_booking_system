-- ON UPDATE CASCADE:
-- If the parent key is updated, the corresponding foreign key values in the child table are automatically updated.

-- ON DELETE RESTRICT:
-- Prevents deletion of a parent row if matching rows exist in the child table.

-- ============================================================
-- Step 1: Create Tables
-- ============================================================
-- ROLES TB
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_roles_name UNIQUE (name)
);

-- USERS TB
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,

    -- Security Management
    must_change_password BOOLEAN NOT NULL DEFAULT FALSE,
    password_changed_at DATETIME NULL,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    account_locked_until DATETIME NULL,
    system_account BOOLEAN NOT NULL DEFAULT FALSE,

    role_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_users_email UNIQUE (email),

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- POSITIONS TB
CREATE TABLE positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_positions_name UNIQUE (name)
);

-- CUSTOMERS TB
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    phone_no VARCHAR(20) NOT NULL,

    user_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_customers_user UNIQUE (user_id),

    CONSTRAINT fk_customers_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- STAFF TB
CREATE TABLE staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_no VARCHAR(50) NOT NULL,
    nic VARCHAR(20) NOT NULL,
    phone_no VARCHAR(20) NOT NULL,
    hire_date DATE NOT NULL,
    termination_date DATE NULL,
    employment_status ENUM ('ACTIVE', 'INACTIVE', 'TERMINATED') NOT NULL,

    user_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_staff_employee_no UNIQUE (employee_no),
    CONSTRAINT uq_staff_nic UNIQUE (nic),
    CONSTRAINT uq_staff_user UNIQUE (user_id),

    CONSTRAINT chk_staff_employee_no
        CHECK (employee_no REGEXP '^EMP[0-9]+$'),

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

-- ============================================================
-- Step 2: Add Audit FK
-- ============================================================
-- Roles
ALTER TABLE roles
ADD CONSTRAINT fk_roles_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE roles
ADD CONSTRAINT fk_roles_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

-- Users
ALTER TABLE users
ADD CONSTRAINT fk_users_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE users
ADD CONSTRAINT fk_users_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

-- Positions
ALTER TABLE positions
ADD CONSTRAINT fk_positions_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE positions
ADD CONSTRAINT fk_positions_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

-- Customers
ALTER TABLE customers
ADD CONSTRAINT fk_customers_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE customers
ADD CONSTRAINT fk_customers_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

-- Staff
ALTER TABLE staff
ADD CONSTRAINT fk_staff_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE staff
ADD CONSTRAINT fk_staff_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;