create table users(
	id int(11) not null auto_increment,
	username varchar(45) not null,
	imei varchar(200) not null,
	contacts text not null,
	primary key (id)
);

create table images(
	id int(11) not null auto_increment,
	sndr_id int(11) not null,
	rcvr_id int(11) not null,
	image_path varchar(255) not null,
	viewed enum('0','1') default '0' not null,
	date_created timestamp not null,
	primary key (id),
	foreign key (sndr_id) references users(id),
	foreign key (rcvr_id) references users(id)
);