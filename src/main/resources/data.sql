-- Файл выполнится при запуске Spring Boot

-- Очистка таблиц (осторожно!)
DELETE FROM bookings;
DELETE FROM room_unavailable_dates;
DELETE FROM rooms;
DELETE FROM hotels;
DELETE FROM users;

-- Сброс sequence (если используем автоинкремент)
ALTER SEQUENCE hotels_id_seq RESTART WITH 1;
ALTER SEQUENCE rooms_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE bookings_id_seq RESTART WITH 1;

-- Пользователи
INSERT INTO users (username, password, email, role)
VALUES
    ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lOBslKciF5JZDq', 'admin@booking.com', 'ADMIN'),
    ('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lOBslKciF5JZDq', 'user1@booking.com', 'USER'),
    ('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lOBslKciF5JZDq', 'user2@booking.com', 'USER');

-- Отели
INSERT INTO hotels (name, advertisement_title, city, address, distance_from_center, rating, rating_count)
VALUES
    ('Grand Hotel', 'Лучший отель в центре города', 'Москва', 'ул. Тверская, д. 1', 0.5, 4.7, 125),
    ('River View', 'Отель с видом на реку', 'Санкт-Петербург', 'наб. реки Мойки, д. 15', 1.2, 4.5, 89),
    ('Mountain Resort', 'Горный курорт для отдыха', 'Сочи', 'ул. Курортная, д. 45', 3.5, 4.8, 67),
    ('City Business', 'Отель для деловых поездок', 'Казань', 'ул. Баумана, д. 22', 0.8, 4.3, 42),
    ('Seaside Paradise', 'Пляжный отель у моря', 'Ялта', 'ул. Набережная, д. 10', 0.3, 4.9, 156);

-- Комнаты
INSERT INTO rooms (name, description, number, price, max_people, hotel_id)
VALUES
    ('Люкс с видом на Кремль', 'Просторный номер с панорамным видом', '101', 15000.00, 2, 1),
    ('Стандартный двухместный', 'Уютный номер для двоих', '102', 8000.00, 2, 1),
    ('Президентский люкс', 'Роскошный номер с джакузи', '201', 25000.00, 4, 1),
    ('Номер с балконом', 'Номер с видом на реку Мойку', '301', 12000.00, 3, 2),
    ('Эконом номер', 'Комфортный бюджетный вариант', '302', 5000.00, 2, 2),
    ('Семейный номер', 'Просторный номер для семьи', '401', 18000.00, 4, 3),
    ('Номер с камином', 'Уютный номер с камином', '402', 22000.00, 2, 3),
    ('Бизнес-люкс', 'Номер для деловых переговоров', '501', 16000.00, 2, 4),
    ('Стандарт', 'Комфортабельный номер', '502', 9000.00, 2, 4),
    ('Пляжный бунгало', 'Отдельный домик у моря', '601', 28000.00, 3, 5),
    ('Номер с террасой', 'Номер с собственной террасой', '602', 19000.00, 2, 5);

-- Бронирования
INSERT INTO bookings (check_in_date, check_out_date, room_id, user_id)
VALUES
    ('2024-03-01', '2024-03-05', 1, 2),
    ('2024-03-10', '2024-03-15', 2, 2),
    ('2024-03-20', '2024-03-25', 3, 3),
    ('2024-04-01', '2024-04-07', 4, 3),
    ('2024-04-15', '2024-04-20', 5, 2);