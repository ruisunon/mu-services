
CREATE TABLE credentials (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	identifier VARCHAR(255) NOT NULL DEFAULT RANDOM_UUID(),
	username VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	role VARCHAR(255) NOT NULL,
	is_enabled BOOLEAN NOT NULL DEFAULT false,
	is_account_non_expired BOOLEAN NOT NULL DEFAULT true,
	is_account_non_locked BOOLEAN NOT NULL DEFAULT true,
	is_credentials_non_expired BOOLEAN NOT NULL DEFAULT true,
	created_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT,
	updated_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT
);



