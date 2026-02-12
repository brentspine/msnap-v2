-- Add optional Discord custom emoji ID for a cape
-- Stored as a string so it can hold snowflake IDs

ALTER TABLE `cape`
    ADD COLUMN `discord_emoji` VARCHAR(32) NULL AFTER `image`;

