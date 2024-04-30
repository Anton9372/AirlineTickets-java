CREATE TABLE Airline (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE Flight (
                        id SERIAL PRIMARY KEY,
                        departure_town VARCHAR(255) NOT NULL,
                        arrival_town VARCHAR(255) NOT NULL,
                        departure_date_time TIMESTAMP NOT NULL,
                        airline_id BIGINT,
                        FOREIGN KEY (airline_id) REFERENCES Airline(id)
);

CREATE TABLE Ticket (
                        id SERIAL PRIMARY KEY,
                        price BIGINT NOT NULL,
                        is_reserved BOOLEAN NOT NULL,
                        flight_id BIGINT,
                        FOREIGN KEY (flight_id) REFERENCES Flight(id)
);

CREATE TABLE Passenger (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           passport_number VARCHAR(255) NOT NULL
);

CREATE TABLE Reservation (
                             id SERIAL PRIMARY KEY,
                             passenger_id BIGINT,
                             ticket_id BIGINT,
                             FOREIGN KEY (passenger_id) REFERENCES Passenger(id),
                             FOREIGN KEY (ticket_id) REFERENCES Ticket(id)
);

CREATE TABLE flight_history (
                                flight_id BIGINT,
                                passenger_id BIGINT,
                                FOREIGN KEY (flight_id) REFERENCES Flight(id),
                                FOREIGN KEY (passenger_id) REFERENCES Passenger(id)
);
