CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE
    );

INSERT INTO users (name, email) VALUES
                                    ('Nguyen Van A', 'nva@example.com'),
                                    ('Tran Thi B', 'ttb@example.com');