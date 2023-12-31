
CREATE TABLE reservations (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	identifier VARCHAR(255) NOT NULL DEFAULT (UUID()),
	code VARCHAR(255) NOT NULL, -- should be UNIQUE & indexed
	description VARCHAR(255),
	start_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
	cancel_date TIMESTAMP DEFAULT NULL,
	complete_date TIMESTAMP DEFAULT NULL,
	status VARCHAR(255) NOT NULL DEFAULT 'NOT_STARTED',
	customer_id INT NOT NULL,
	saloon_id INT NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);



