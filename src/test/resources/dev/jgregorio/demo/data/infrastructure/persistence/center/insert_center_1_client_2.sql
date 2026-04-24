-- Insert location first (required for foreign key)
INSERT INTO TEST.LOCATIONS (ID, NAME, CREATED_DATE, CREATED_BY)
VALUES (3, 'Test Location', CURRENT_TIMESTAMP, 1);
-- Insert center with all required fields
INSERT INTO TEST.CENTERS (
        ID,
        CLIENT_ID,
        NAME,
        ADDRESS,
        POSTAL_CODE,
        LOCATION_ID,
        CREATED_DATE,
        CREATED_BY
    )
VALUES (
        1,
        2,
        'Center 1',
        'Address 1',
        '12345',
        3,
        CURRENT_TIMESTAMP,
        1
    );