alter table if exists books drop constraint if exists FK9hsvoalyniowgt8fbufidqj3x;
alter table if exists books drop constraint if exists FKqhbjpnph6e33p08wfax7dd475;
alter table if exists notifications drop constraint if exists FK4dk13nbrwe1ua0tscs8pmxk1w;
alter table if exists promocodes drop constraint if exists FKctknjc9563kq9fa3yyx54vx6c;
alter table if exists purchased_user_books drop constraint if exists FKa8oj721wn9p2g7n6xa9vq00aw;
alter table if exists users_basket_books drop constraint if exists FKck7p552cy9ioyjdmsrh734b47;
alter table if exists users_basket_books drop constraint if exists FKnmv51ockkqqtorh9a6nifom03;
alter table if exists users_favorite_books drop constraint if exists FKri1qgsos75704kfd7wylun5kg;
alter table if exists users_favorite_books drop constraint if exists FK9btrtpic9pa113m7w2vqqxwd0;

drop table if exists books cascade;
drop table if exists genres cascade;
drop table if exists mailing_list cascade;
drop table if exists notifications cascade;
drop table if exists promocodes cascade;
drop table if exists purchased_user_books cascade;
drop table if exists users cascade;
drop table if exists users_basket_books cascade;
drop table if exists users_favorite_books cascade;
drop sequence if exists genre_seq;
drop sequence if exists hibernate_sequence;
drop sequence if exists promo_seq;

create table books (id int8 not null, audio_book varchar(255), audio_book_fragment varchar(255), author varchar(255), bestseller boolean not null, book_status varchar(255), book_type varchar(255), date_the_book_was_added_to_favorites date, description varchar(255), discount int4 not null, duration time, electronic_book varchar(255), fragment varchar(255), is_enabled boolean not null, is_new boolean not null, language varchar(255), main_image varchar(255), name varchar(255), page_size int4 not null, price int4 not null, published_date date, publishing_house varchar(255), quantity_of_book int4 not null, second_image varchar(255), third_image varchar(255), year_of_issue int4 not null, genre_id int8, owner_id int8, primary key (id));
create table genres (id int8 not null, name varchar(255), primary key (id));
create table mailing_list (id int8 generated by default as identity, email varchar(255), primary key (id));
create table notifications (id int8 generated by default as identity, book_id int8, book_status varchar(255), created_at date, description varchar(255), read boolean not null, vendor_id int8, primary key (id));
create table promocodes (id int8 not null, date_of_finish date, date_of_start date, discount int4 not null, name varchar(255), vendor_id int8, primary key (id));
create table purchased_user_books (id int8 generated by default as identity, book_author varchar(255), book_id int8, book_main_image varchar(255), book_name varchar(255), price int4 not null, promocode int4 not null, purchase_date date, quantity_of_book int4 not null, user_id int8, primary key (id));
create table users (id int8 generated by default as identity, created_at date, email varchar(255), first_name varchar(255), last_name varchar(255), password varchar(255), phone_number varchar(255), role varchar(255), primary key (id));
create table users_basket_books (book_id int8 not null, user_id int8 not null);
create table users_favorite_books (book_id int8 not null, user_id int8 not null);

alter table if exists books add constraint FK9hsvoalyniowgt8fbufidqj3x foreign key (genre_id) references genres;
alter table if exists books add constraint FKqhbjpnph6e33p08wfax7dd475 foreign key (owner_id) references users;
alter table if exists notifications add constraint FK4dk13nbrwe1ua0tscs8pmxk1w foreign key (vendor_id) references users;
alter table if exists promocodes add constraint FKctknjc9563kq9fa3yyx54vx6c foreign key (vendor_id) references users;
alter table if exists purchased_user_books add constraint FKa8oj721wn9p2g7n6xa9vq00aw foreign key (user_id) references users;
alter table if exists users_basket_books add constraint FKnmv51ockkqqtorh9a6nifom03 foreign key (book_id) references books;
alter table if exists users_basket_books add constraint FKck7p552cy9ioyjdmsrh734b47 foreign key (user_id) references users;
alter table if exists users_favorite_books add constraint FKri1qgsos75704kfd7wylun5kg foreign key (user_id) references users;
alter table if exists users_favorite_books add constraint FK9btrtpic9pa113m7w2vqqxwd0 foreign key (book_id) references books;