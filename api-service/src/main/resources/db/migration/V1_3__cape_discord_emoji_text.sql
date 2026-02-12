-- Store discord emoji as raw mention text, e.g. <:name:123> or <a:name:123>

ALTER TABLE `cape`
    MODIFY COLUMN `discord_emoji` VARCHAR(255) NULL;

