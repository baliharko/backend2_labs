create table account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    holder VARCHAR(255),
    balance DOUBLE
);

insert into account(holder, balance) VALUES ('Stefan', 250);
insert into account(holder, balance) VALUES ('Superman', 999999);