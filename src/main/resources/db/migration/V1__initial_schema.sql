-- ON UPDATE CASCADE:
-- If the parent key is updated, the corresponding foreign key values in the child table are automatically updated.

-- ON DELETE RESTRICT:
-- Prevents deletion of a parent row if matching rows exist in the child table.

-- ============================================================
-- Step 1: Create Tables
-- ============================================================

-- PERMISSIONS Tbl
CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_permissions_name
        UNIQUE (name)
);

-- ROLES Tbl
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(50) NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_roles_name
        UNIQUE (name)
);

-- POSITIONS Tbl
CREATE TABLE positions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_positions_name
        UNIQUE (name)
);

-- USERS Tbl
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
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

    CONSTRAINT uq_users_email
        UNIQUE (email),

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ROLE_PERMISSIONS Tbl
CREATE TABLE role_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_role_permissions_role_permission
        UNIQUE (role_id, permission_id),

    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY (permission_id)
        REFERENCES permissions(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- --------------------
-- Security
-- --------------------
-- PASSWORD RESET TOKENS
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    token_hash VARCHAR(255) NOT NULL,
    expiry_date DATETIME NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,

    user_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_password_reset_tokens_token_hash
        UNIQUE (token_hash),

    CONSTRAINT fk_password_reset_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- CUSTOMERS Tbl
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

    -- Enforces User (1) : Customer (1)
    CONSTRAINT uq_customers_user
        UNIQUE (user_id),

    CONSTRAINT fk_customers_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- STAFF Tbl
CREATE TABLE staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    employee_no VARCHAR(50) NOT NULL,
    nic VARCHAR(20) NOT NULL,
    phone_no VARCHAR(20) NOT NULL,

    hire_date DATE NOT NULL,
    termination_date DATE NULL,

    employment_status ENUM (
        'ACTIVE',
        'INACTIVE',
        'TERMINATED'
    ) NOT NULL,

    user_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_staff_employee_no
        UNIQUE (employee_no),

    CONSTRAINT uq_staff_nic
        UNIQUE (nic),

    -- Enforces User (1) : Staff (1)
    CONSTRAINT uq_staff_user
        UNIQUE (user_id),

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

-- EVENTS Tbl
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    title VARCHAR(150) NOT NULL,
    description VARCHAR(1000) NOT NULL,

    event_date DATE NULL,
    event_time TIME NULL,

    venue VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,

    poster_image VARCHAR(255) NOT NULL,

    status VARCHAR(50) NOT NULL,

    staff_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT chk_events_capacity
        CHECK (capacity > 0),

    CONSTRAINT fk_events_staff
        FOREIGN KEY (staff_id)
        REFERENCES staff(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- SEATS Tbl
CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    seat_number VARCHAR(20) NOT NULL,
    section VARCHAR(50) NOT NULL,

    price DECIMAL(12,2) NOT NULL,

    status VARCHAR(50) NOT NULL,

    event_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT chk_seats_price
        CHECK (price >= 0),

    CONSTRAINT uq_seat_event_number
        UNIQUE (event_id, seat_number),

    CONSTRAINT fk_seats_event
        FOREIGN KEY (event_id)
        REFERENCES events(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- BOOKINGS Tbl
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    total_amount DECIMAL(12,2) NOT NULL,
    booking_date DATE NULL,

    status VARCHAR(50) NOT NULL,

    customer_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT chk_bookings_total_amount
        CHECK (total_amount >= 0),

    CONSTRAINT fk_bookings_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- BOOKING_SEATS Tbl
CREATE TABLE booking_seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    -- Prevent the same seat from being added twice
    -- to the same booking.
    CONSTRAINT uq_booking_seat
        UNIQUE (booking_id, seat_id),

    CONSTRAINT fk_booking_seats_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_booking_seats_seat
        FOREIGN KEY (seat_id)
        REFERENCES seats(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- PAYMENTS Tbl
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    amount DECIMAL(12,2) NOT NULL,

    payment_date DATETIME NULL,

    transaction_ref VARCHAR(100) NOT NULL,

    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,

    booking_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    created_by BIGINT NULL,
    updated_by BIGINT NULL,

    CONSTRAINT uq_payments_transaction_ref
        UNIQUE (transaction_ref),

    -- Enforces Booking (1) : Payment (1)
    CONSTRAINT uq_payments_booking
        UNIQUE (booking_id),

    CONSTRAINT chk_payments_amount
        CHECK (amount >= 0),

    CONSTRAINT fk_payments_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- ============================================================
-- Step 2: Add Audit FK
-- ============================================================

-- PERMISSIONS
ALTER TABLE permissions
ADD CONSTRAINT fk_permissions_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE permissions
ADD CONSTRAINT fk_permissions_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;


-- ROLES
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


-- ROLE_PERMISSIONS
ALTER TABLE role_permissions
ADD CONSTRAINT fk_role_permissions_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE role_permissions
ADD CONSTRAINT fk_role_permissions_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;


-- USERS
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


-- PASSWORD RESET TOKENS
ALTER TABLE password_reset_tokens
ADD CONSTRAINT fk_password_reset_tokens_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE password_reset_tokens
ADD CONSTRAINT fk_password_reset_tokens_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;


-- POSITIONS
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


-- CUSTOMERS
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


-- STAFF
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


-- EVENTS
ALTER TABLE events
ADD CONSTRAINT fk_events_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE events
ADD CONSTRAINT fk_events_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;


-- SEATS
ALTER TABLE seats
ADD CONSTRAINT fk_seats_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE seats
ADD CONSTRAINT fk_seats_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;


-- BOOKINGS
ALTER TABLE bookings
ADD CONSTRAINT fk_bookings_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE bookings
ADD CONSTRAINT fk_bookings_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;


-- BOOKING_SEATS
ALTER TABLE booking_seats
ADD CONSTRAINT fk_booking_seats_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE booking_seats
ADD CONSTRAINT fk_booking_seats_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;


-- PAYMENTS
ALTER TABLE payments
ADD CONSTRAINT fk_payments_created_by
FOREIGN KEY (created_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;

ALTER TABLE payments
ADD CONSTRAINT fk_payments_updated_by
FOREIGN KEY (updated_by)
REFERENCES users(id)
ON UPDATE CASCADE
ON DELETE RESTRICT;