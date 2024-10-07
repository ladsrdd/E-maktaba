create table admin
(
    id       int auto_increment
        primary key,
    username varchar(255) null,
    password varchar(255) null
);

create table genre
(
    id          int auto_increment
        primary key,
    name        varchar(255) null,
    description varchar(255) null
);

create table book
(
    id       int auto_increment
        primary key,
    isbn     mediumtext   null,
    title    varchar(255) null,
    genreID  int          null,
    quantity int          null,
    constraint book_genre_id_fk
        foreign key (genreID) references genre (id)
            on delete set null
);

create table loan
(
    id         int auto_increment
        primary key,
    memberID   int                                                        null,
    bookID     int                                                        null,
    loanDate   date                                                       null,
    returnDate date                                                       null,
    status     enum ('ACTIVE', 'RETURNED', 'OVERDUE', 'RETURNED_OVERDUE') null
);

create table member
(
    id           int auto_increment
        primary key,
    cin          varchar(255) null,
    fullName     varchar(255) null,
    email        varchar(255) null,
    subStartDate date         null,
    subEndDate   date         null
);

create definer = root@localhost event update_overdue_loans on schedule
    every '1' DAY
        starts '2024-05-13 12:15:08'
    enable
    do
    BEGIN
        UPDATE loan
        SET status = 'overdue'
        WHERE returnDate < NOW() AND status != 'RETURNED' AND status != 'RETURNED (OVERDUE)';
    END;

