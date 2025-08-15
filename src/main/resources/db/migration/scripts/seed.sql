-- liquibase formatted sql

-- changeset insert test data:2

INSERT INTO users (id, username, password, role) VALUES (
    1, 
    'test_usr', 
    '5e3e5e464d1552eea98c80bd87a729fad0d0e8a9d4a0d27b7d6cd296dfa18992c6dc8ed5c4421b1b18541a41bf87af37', 
    'USER'
);

INSERT INTO users (id, username, password, role) VALUES (
    2, 
    'test_admin', 
    '982a003fc1a90091b99255bbcc2d6d77d1b0d55f28f5b989f77d960a9359341a967e4d4c34c49d26aa8c21dc73a1dd09', 
    'ADMIN'
);

INSERT INTO cards (number, masked_number, expired_at, status, balance, owner_id) VALUES (
    'B0OMhna5uQ7f77CoiZkz18vV3ATWOt9Mj2gsLsdaeJg=', 
    '**** **** **** 0336', 
    '12/25', 
    'ACTIVE', 
    1000.00, 
    1
);
