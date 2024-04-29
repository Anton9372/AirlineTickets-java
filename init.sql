CREATE TABLE Airline (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE Flight (
                        id SERIAL PRIMARY KEY,
                        departureTown VARCHAR(255) NOT NULL,
                        arrivalTown VARCHAR(255) NOT NULL,
                        departureDateTime TIMESTAMP NOT NULL,
                        airline_id BIGINT,
                        FOREIGN KEY (airline_id) REFERENCES Airline(id)
);

CREATE TABLE Ticket (
                        id SERIAL PRIMARY KEY,
                        price BIGINT NOT NULL,
                        isReserved BOOLEAN NOT NULL,
                        flight_id BIGINT,
                        FOREIGN KEY (flight_id) REFERENCES Flight(id)
);

CREATE TABLE Passenger (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           passportNumber VARCHAR(255) NOT NULL
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
