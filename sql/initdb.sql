set global max_connections = 1000;

create database if not exists edu;
use edu;


 
drop table if exists study_record;
drop table if exists prerequisites;
drop table if exists offering;
drop table if exists student;
drop table if exists course;
drop table if exists term;

create table term (
	name varchar(20) not null,
	start_date date not null,

	primary key (name)
);

create table course (
	id varchar(20) not null,
	name varchar(50) not null,
	units integer not null,

	primary key (id)
);

create table offering (
	id varchar(20) not null,
	course_id varchar(20) not null,
	section integer default 1,
	exam_date date,
	term_name varchar(20) not null,
	
	primary key (id),
	
	constraint course_fk foreign key(course_id) references course(id),
	constraint term_fk foreign key(term_name) references term(name)
);

create table student (
	id varchar(20) not null,
	name varchar(50) not null,

	primary key (id)
);

create table study_record (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	student_id varchar(20) not null,
	offering_id varchar (20) not null,
	grade double default 0.0,
	
	primary key (id),
	
	constraint student_fk foreign key(student_id) references student(id),
	constraint offering_fk foreign key(offering_id) references offering(id)
);
	
create table prerequisites (
	course_id varchar(20) not null,
	pre_id varchar(20) not null,
	
	primary key (course_id, pre_id),
	
	constraint p_course_fk foreign key(course_id) references course(id),
	constraint pre_fk foreign key(pre_id) references course(id)
);

delete from study_record;
delete from student;
delete from prerequisites;
delete from offering;
delete from course;
delete from term;

insert into term values('88-89-1', '2009-09-23');
insert into term values('88-89-2', '2010-02-06');

insert into course values('ds', 'DS', 3);
insert into course values('ap', 'AP', 3);
insert into course values('dm', 'DM', 3);
insert into course values('math1', 'MATH1', 3);
insert into course values('stat', 'STAT', 3);
insert into course values('math2', 'MATH2', 3);
insert into course values('lang', 'LANG', 3);

insert into offering values('ap1', 'ap', 1, '2010-01-06', '88-89-1'); 
insert into offering values('dm1', 'dm', 1, '2010-06-07', '88-89-1'); 
insert into offering values('math11', 'math1', 1, '2010-06-08', '88-89-1');
insert into offering values('math21', 'math2', 1, '2010-06-09', '88-89-1');
insert into offering values('stat1', 'stat', 1, '2010-06-10', '88-89-1');
insert into offering values('ds1', 'ds', 1, '2010-06-11', '88-89-1');

insert into offering values('ap2', 'ap', 1, '2010-06-06', '88-89-2'); 
insert into offering values('dm2', 'dm', 1, '2010-06-07', '88-89-2'); 
insert into offering values('math12', 'math1', 1, '2010-06-08', '88-89-2');
insert into offering values('math22', 'math2', 1, '2010-06-08', '88-89-2');
insert into offering values('stat2', 'stat', 1, '2010-06-09', '88-89-2');
insert into offering values('ds2', 'ds', 1, '2010-06-10', '88-89-2');
insert into offering values('lang2a', 'lang', 1, '2010-06-11', '88-89-2');
insert into offering values('lang2b', 'lang', 2, '2010-06-08', '88-89-2');

insert into student values('bebe', 'Bebe');
insert into student values('xi', 'Xi');

insert into study_record(student_id,offering_id,grade) values('bebe', 'ap1', 18.0);
insert into study_record(student_id,offering_id,grade) values('bebe', 'stat1', 12.0);
insert into study_record(student_id,offering_id,grade) values('bebe', 'math11', 8.4);

insert into prerequisites values ('ds', 'ap');
insert into prerequisites values ('ds', 'dm');
insert into prerequisites values ('dm', 'math1');
insert into prerequisites values ('math2', 'math1');

