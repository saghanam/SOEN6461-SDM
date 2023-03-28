create database tvm;

create table customers(
	customer_id varchar(15) PRIMARY KEY,
    c_name varchar(25) NOT NULL,
    c_age int,
    c_mail varchar(35),
    c_password varchar(10),
    c_phone int,
    outstanding_bill int DEFAULT(0)
);

create table bikes(
	bike_id varchar(10) PRIMARY KEY,
    model_id varchar(10) default('B00')
);

create table trips(
	trip_id varchar(25) PRIMARY KEY,
    bike_id varchar(10),
    customer_id varchar(15),
    trip_start datetime,
    trip_end datetime,
    tt_mod int,
    tbill int,
    activeFlag bool,
    FOREIGN KEY (bike_id) REFERENCES bikes(bike_id),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

create table unlock_codes(
	unlock_code int PRIMARY KEY,
    start_time datetime
    
);

create table docks(
	dock_id varchar(10) PRIMARY KEY,
    unlock_code int(5),
    bike_id varchar(10),
    FOREIGN KEY (unlock_code) REFERENCES unlock_codes(unlock_code)
);

create table stations(
	station_code varchar(8) PRIMARY KEY,
    dock_id varchar(10),
    FOREIGN KEY (dock_id) REFERENCES docks(dock_id)
);

create table auth(
		customer_id varchar(15),
        pwd varchar(15)
);


    
