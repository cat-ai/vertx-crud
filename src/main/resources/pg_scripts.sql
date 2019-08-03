create table client
(
	id integer default nextval('client_id'::regclass) not null
		constraint client_pkey
			primary key,
	email char(64),
	name char(64),
	nickname char(64)
);

alter table client owner to pguser;

create unique index client_email_uindex
	on client (email);

create unique index client_nickname_uindex
	on client (nickname);