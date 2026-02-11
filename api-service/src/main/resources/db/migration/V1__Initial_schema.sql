-- =========================================================
-- MSnap V1 Initial Schema for MySQL
-- =========================================================

-- =========================================================
-- Independent/Reference Tables (no foreign key dependencies)
-- =========================================================

-- Sources: tracks where accounts/items came from
CREATE TABLE sources (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price_currency VARCHAR(3),
    price_minor BIGINT,
    timestamp BIGINT NOT NULL,
    note VARCHAR(4096),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Proxy providers
CREATE TABLE proxy_provider (
    id BIGINT NOT NULL AUTO_INCREMENT,
    provider_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Proxies
CREATE TABLE proxies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ip VARCHAR(100) NOT NULL,
    port INT NOT NULL,
    region TINYINT,
    username VARCHAR(100),
    password VARCHAR(120),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Capes
CREATE TABLE cape (
    alias VARCHAR(80) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    description TEXT,
    image VARCHAR(1024),
    updated_at DATETIME(6) NOT NULL,
    allows_codes BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (alias)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Minecraft servers
CREATE TABLE minecraft_server (
    id BIGINT NOT NULL AUTO_INCREMENT,
    server_ip VARCHAR(255) NOT NULL,
    server_port VARCHAR(60) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    internal_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX ix_minecraft_server_internal_name (internal_name),
    INDEX ix_minecraft_server_server_ip (server_ip)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Minecraft server currencies
CREATE TABLE minecraft_server_currency (
    id BIGINT NOT NULL AUTO_INCREMENT,
    server_id BIGINT NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    internal_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX ix_msc_server_internal_name (server_id, internal_name),
    CONSTRAINT fk_msc_server FOREIGN KEY (server_id) REFERENCES minecraft_server(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Minecraft server ranks
CREATE TABLE minecraft_server_rank (
    id BIGINT NOT NULL AUTO_INCREMENT,
    server_id BIGINT NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    internal_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX ix_msr_server_internal_name (server_id, internal_name),
    CONSTRAINT fk_msr_server FOREIGN KEY (server_id) REFERENCES minecraft_server(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Outlook emails
CREATE TABLE outlook_email (
    id BIGINT NOT NULL AUTO_INCREMENT,
    from_address VARCHAR(255) NOT NULL,
    to_address VARCHAR(255) NOT NULL,
    produced_at DATETIME(6) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body_preview VARCHAR(105) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Outlook information
CREATE TABLE outlook_information (
    id BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Outlook information mails sent join table
CREATE TABLE outlook_information_mails_sent (
    outlook_information_id BIGINT NOT NULL,
    outlook_email_id BIGINT NOT NULL,
    PRIMARY KEY (outlook_information_id, outlook_email_id),
    CONSTRAINT fk_oims_outlook_info FOREIGN KEY (outlook_information_id) REFERENCES outlook_information(id) ON DELETE CASCADE,
    CONSTRAINT fk_oims_email FOREIGN KEY (outlook_email_id) REFERENCES outlook_email(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Outlook information inbox join table
CREATE TABLE outlook_information_inbox (
    outlook_information_id BIGINT NOT NULL,
    outlook_email_id BIGINT NOT NULL,
    PRIMARY KEY (outlook_information_id, outlook_email_id),
    CONSTRAINT fk_oii_outlook_info FOREIGN KEY (outlook_information_id) REFERENCES outlook_information(id) ON DELETE CASCADE,
    CONSTRAINT fk_oii_email FOREIGN KEY (outlook_email_id) REFERENCES outlook_email(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Payment cards
CREATE TABLE microsoft_payment_cards (
    id BINARY(16) NOT NULL,
    type VARCHAR(100) NOT NULL,
    display VARCHAR(255) NOT NULL,
    address VARCHAR(512) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Microsoft orders
CREATE TABLE microsoft_orders (
    internal_id BINARY(16) NOT NULL,
    address VARCHAR(255),
    display_name VARCHAR(512),
    price_currency VARCHAR(3),
    price_minor BIGINT,
    order_date DATETIME(6),
    order_number VARCHAR(255),
    PRIMARY KEY (internal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Microsoft additional info
CREATE TABLE microsoft_additional_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    region VARCHAR(255),
    date_of_birth DATE,
    profile_image_url VARCHAR(1024),
    ms_wallet_balance_currency VARCHAR(3),
    ms_wallet_balance_minor BIGINT,
    ms_points INT DEFAULT 0,
    notes VARCHAR(4096),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Microsoft additional info games owned (element collection)
CREATE TABLE microsoft_additional_info_games_owned (
    microsoft_additional_info_id BIGINT NOT NULL,
    games_owned VARCHAR(255),
    CONSTRAINT fk_maigo_info FOREIGN KEY (microsoft_additional_info_id) REFERENCES microsoft_additional_info(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Microsoft additional info payment cards join table
CREATE TABLE microsoft_additional_info_payment_cards (
    microsoft_additional_info_id BIGINT NOT NULL,
    payment_card_id BINARY(16) NOT NULL,
    PRIMARY KEY (microsoft_additional_info_id, payment_card_id),
    CONSTRAINT fk_maipc_info FOREIGN KEY (microsoft_additional_info_id) REFERENCES microsoft_additional_info(id) ON DELETE CASCADE,
    CONSTRAINT fk_maipc_card FOREIGN KEY (payment_card_id) REFERENCES microsoft_payment_cards(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- Security Information (Inheritance: JOINED strategy)
-- =========================================================

-- Base security information table
CREATE TABLE security_information (
    id BIGINT NOT NULL AUTO_INCREMENT,
    security_type VARCHAR(31) NOT NULL,
    recovery_code VARCHAR(255),
    primary_email VARCHAR(255),
    alias_phone VARCHAR(255),
    app_password VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Security information aliases (element collection)
CREATE TABLE security_information_aliases (
    security_information_id BIGINT NOT NULL,
    alias VARCHAR(255),
    CONSTRAINT fk_sia_security_info FOREIGN KEY (security_information_id) REFERENCES security_information(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Initial security information (extends security_information)
CREATE TABLE initial_security_information (
    security_information_id BIGINT NOT NULL,
    mail_url VARCHAR(255),
    mail_password VARCHAR(255),
    PRIMARY KEY (security_information_id),
    CONSTRAINT fk_isi_security_info FOREIGN KEY (security_information_id) REFERENCES security_information(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- Account Activity (Inheritance: JOINED strategy)
-- =========================================================

-- Base account activity table
CREATE TABLE account_activity (
    id BIGINT NOT NULL AUTO_INCREMENT,
    done_at DATETIME(6),
    own BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Details changed activity (extends account_activity)
CREATE TABLE details_changed_activity (
    activity_id BIGINT NOT NULL,
    field VARCHAR(255),
    old_value VARCHAR(255),
    new_value VARCHAR(255),
    PRIMARY KEY (activity_id),
    CONSTRAINT fk_dca_activity FOREIGN KEY (activity_id) REFERENCES account_activity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- MS foreign sign in activity (extends account_activity)
CREATE TABLE ms_foreign_sign_in_activity (
    activity_id BIGINT NOT NULL,
    alias_used VARCHAR(255),
    ip_address VARCHAR(255),
    device_os VARCHAR(255),
    device_browser VARCHAR(255),
    location_info VARCHAR(255),
    location_latitude FLOAT,
    location_longitude FLOAT,
    action_list VARCHAR(2048),
    PRIMARY KEY (activity_id),
    CONSTRAINT fk_mfsia_activity FOREIGN KEY (activity_id) REFERENCES account_activity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- Minecraft Accounts
-- =========================================================

CREATE TABLE minecraft_accounts (
    internal_id BIGINT NOT NULL AUTO_INCREMENT,
    uuid BINARY(16) NOT NULL,
    username VARCHAR(60) NOT NULL,
    created_at DATETIME(6),
    minecoins INT DEFAULT 0,
    PRIMARY KEY (internal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Minecraft account capes join table (entity uses minecraft_account_proxies name but joins capes)
CREATE TABLE minecraft_account_proxies (
    minecraft_account_id BIGINT NOT NULL,
    cape_id VARCHAR(80) NOT NULL,
    PRIMARY KEY (minecraft_account_id, cape_id),
    CONSTRAINT fk_macp_minecraft_account FOREIGN KEY (minecraft_account_id) REFERENCES minecraft_accounts(internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_macp_cape FOREIGN KEY (cape_id) REFERENCES cape(alias) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Cape codes
CREATE TABLE cape_code (
    code VARCHAR(120) NOT NULL,
    cape_alias VARCHAR(80) NOT NULL,
    source_id BIGINT,
    image_url VARCHAR(1024),
    updated_at DATETIME(6) NOT NULL,
    redeemed_at DATETIME(6),
    redeemed_by_id BIGINT,
    PRIMARY KEY (code),
    CONSTRAINT fk_cc_cape FOREIGN KEY (cape_alias) REFERENCES cape(alias) ON DELETE CASCADE,
    CONSTRAINT fk_cc_source FOREIGN KEY (source_id) REFERENCES sources(id) ON DELETE SET NULL,
    CONSTRAINT fk_cc_redeemed_by FOREIGN KEY (redeemed_by_id) REFERENCES minecraft_accounts(internal_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- Microsoft Accounts (main entity)
-- =========================================================

CREATE TABLE microsoft_accounts (
    internal_id BINARY(16) NOT NULL,
    license_type VARCHAR(50),
    security_information_id BIGINT,
    initial_security_information_id BIGINT,
    outlook_information_id BIGINT,
    source_id BIGINT,
    minecraft_account_id BIGINT,
    status TINYINT,
    secured_at DATETIME(6),
    PRIMARY KEY (internal_id),
    CONSTRAINT fk_ma_security_info FOREIGN KEY (security_information_id) REFERENCES security_information(id) ON DELETE SET NULL,
    CONSTRAINT fk_ma_initial_security_info FOREIGN KEY (initial_security_information_id) REFERENCES security_information(id) ON DELETE SET NULL,
    CONSTRAINT fk_ma_outlook_info FOREIGN KEY (outlook_information_id) REFERENCES outlook_information(id) ON DELETE SET NULL,
    CONSTRAINT fk_ma_source FOREIGN KEY (source_id) REFERENCES sources(id) ON DELETE SET NULL,
    CONSTRAINT fk_ma_minecraft_account FOREIGN KEY (minecraft_account_id) REFERENCES minecraft_accounts(internal_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Microsoft account proxies join table
CREATE TABLE microsoft_account_proxies (
    microsoft_account_id BINARY(16) NOT NULL,
    proxy_id BIGINT NOT NULL,
    PRIMARY KEY (microsoft_account_id, proxy_id),
    CONSTRAINT fk_map_microsoft_account FOREIGN KEY (microsoft_account_id) REFERENCES microsoft_accounts(internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_map_proxy FOREIGN KEY (proxy_id) REFERENCES proxies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Microsoft account activities join table
CREATE TABLE microsoft_account_activities (
    microsoft_account_id BINARY(16) NOT NULL,
    activity_id BIGINT NOT NULL,
    PRIMARY KEY (microsoft_account_id, activity_id),
    CONSTRAINT fk_maa_microsoft_account FOREIGN KEY (microsoft_account_id) REFERENCES microsoft_accounts(internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_maa_activity FOREIGN KEY (activity_id) REFERENCES account_activity(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- Minecraft Server Account Info
-- =========================================================

CREATE TABLE minecraft_server_account_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    minecraft_server_id BIGINT NOT NULL,
    minecraft_account_id BIGINT NOT NULL,
    playtime_minutes INT NOT NULL DEFAULT 0,
    unban_timestamp DATETIME(6),
    highest_rank_id BIGINT,
    PRIMARY KEY (id),
    INDEX ix_msai_server_account (minecraft_server_id, minecraft_account_id),
    CONSTRAINT fk_msai_server FOREIGN KEY (minecraft_server_id) REFERENCES minecraft_server(id) ON DELETE CASCADE,
    CONSTRAINT fk_msai_minecraft_account FOREIGN KEY (minecraft_account_id) REFERENCES minecraft_accounts(internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_msai_highest_rank FOREIGN KEY (highest_rank_id) REFERENCES minecraft_server_rank(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Minecraft account server infos join table
CREATE TABLE minecraft_account_server_infos (
    minecraft_account_id BIGINT NOT NULL,
    server_info_id BIGINT NOT NULL,
    PRIMARY KEY (minecraft_account_id, server_info_id),
    CONSTRAINT fk_masi_minecraft_account FOREIGN KEY (minecraft_account_id) REFERENCES minecraft_accounts(internal_id) ON DELETE CASCADE,
    CONSTRAINT fk_masi_server_info FOREIGN KEY (server_info_id) REFERENCES minecraft_server_account_info(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Minecraft server account currencies
CREATE TABLE minecraft_server_account_currency (
    id BIGINT NOT NULL AUTO_INCREMENT,
    account_info_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL,
    amount INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uq_msac_account_currency UNIQUE (account_info_id, currency_id),
    CONSTRAINT fk_msac_account_info FOREIGN KEY (account_info_id) REFERENCES minecraft_server_account_info(id) ON DELETE CASCADE,
    CONSTRAINT fk_msac_currency FOREIGN KEY (currency_id) REFERENCES minecraft_server_currency(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;




