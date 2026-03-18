  CREATE TABLE addresses (
                  id bigserial PRIMARY KEY,
                  house_number VARCHAR(20) NOT NULL,
                  street VARCHAR(255) NOT NULL,
                  city VARCHAR(100) NOT NULL,
                  country VARCHAR(100) NOT NULL,
                  post_code VARCHAR(20)
              );

              CREATE TABLE contacts (
                  id bigserial PRIMARY KEY,
                  phone VARCHAR(50) NOT NULL,
                  email VARCHAR(255) NOT NULL
              );

              CREATE TABLE arrival_times (
                  id bigserial PRIMARY KEY,
                  check_in TIME NOT NULL,
                  check_out TIME NOT NULL
              );

              CREATE TABLE hotels (
                  id bigserial PRIMARY KEY,
                  name VARCHAR(255) NOT NULL,
                  description CLOB,
                  brand VARCHAR(100),
                  address_id bigint,
                  contacts_id bigint,
                  arrival_time_id bigint
              );

              CREATE TABLE amenities (
                  id bigserial PRIMARY KEY,
                  name VARCHAR(100) NOT NULL UNIQUE,
                  description VARCHAR(500)
              );

              CREATE TABLE hotel_amenities (
                  hotel_id bigint NOT NULL,
                  amenity_id bigint NOT NULL,
                  PRIMARY KEY (hotel_id, amenity_id)
              );

              ALTER TABLE hotels ADD CONSTRAINT fk_hotels_address FOREIGN KEY (address_id) REFERENCES addresses(id);
              ALTER TABLE hotels ADD CONSTRAINT fk_hotels_contacts FOREIGN KEY (contacts_id) REFERENCES contacts(id);
              ALTER TABLE hotels ADD CONSTRAINT fk_hotels_arrival_time FOREIGN KEY (arrival_time_id) REFERENCES arrival_times(id);
              ALTER TABLE hotel_amenities ADD CONSTRAINT fk_ha_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id);
              ALTER TABLE hotel_amenities ADD CONSTRAINT fk_ha_amenity FOREIGN KEY (amenity_id) REFERENCES amenities(id);

INSERT INTO addresses (house_number, street, city, country, post_code) VALUES
('9', 'Pobediteley Avenue', 'Minsk', 'Belarus', '220004');

INSERT INTO contacts (phone, email) VALUES
('+375 17 309-80-00', 'doubletreeminsk.info@hilton.com');

INSERT INTO arrival_times (check_in, check_out) VALUES
('14:00:00', '12:00:00');

INSERT INTO hotels (name, description, brand, address_id, contacts_id, arrival_time_id) VALUES
('DoubleTree by Hilton Minsk',
 'The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel''s 20th floor ...',
 'Hilton',
 1, 1, 1);

INSERT INTO amenities (name) VALUES
('Free parking'),
('Free WiFi'),
('Non-smoking rooms'),
('Concierge'),
('On-site restaurant'),
('Fitness center'),
('Pet-friendly rooms'),
('Room service'),
('Business center'),
('Meeting rooms');

INSERT INTO hotel_amenities (hotel_id, amenity_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(1, 6), (1, 7), (1, 8), (1, 9), (1, 10);