create database campusgigs;
use campusgigs;
create table department (
department_id int auto_increment primary key,
department_name varchar(100) not null);
create table skills (
skill_id int auto_increment primary key,
skill_name varchar(100) not null);
create table students (
student_id int auto_increment primary key,
student_name varchar(100) not null,
department_id int not null,
year_of_study int not null,
constraint fk_students_department foreign key (department_id) references department(department_id));
create table organization (
organization_id int auto_increment primary key,
organization_title varchar(100) not null,
verification_status enum('Pending','Verified','Rejected') default 'Pending');
create table client (
client_id int auto_increment primary key,
client_name varchar(100) not null,
organization_id int not null,
constraint fk_client_organization foreign key (organization_id) references organization(organization_id));
create table gigs (
gig_id int auto_increment primary key,
gig_title varchar(100) not null,
description varchar(1000) not null,
budget double not null,
status enum('Open','In Progress','Completed') not null default 'Open',
deadline date not null,
client_id int not null,
constraint fk_gigs_client foreign key (client_id) references client(client_id));
create table studentskills (
student_id int,
skill_id int,
constraint fk_studentskills_students foreign key (student_id) references students(student_id),
constraint fk_studentskills_skills foreign key (skill_id) references skills(skill_id));
create table gigskills (
gig_id int,
skill_id int,
constraint fk_gigskills_gigs foreign key (gig_id) references gigs(gig_id),
constraint fk_gigskills_skills foreign key (skill_id) references skills(skill_id));
create table application (
application_id int auto_increment primary key,
student_id int not null,
gig_id int not null,
proposal varchar(2000) not null,
constraint fk_application_students foreign key (student_id) references students(student_id),
constraint fk_application_gigs foreign key (gig_id) references gigs(gig_id));
create table reviews (
review_id int auto_increment primary key,
student_id int not null,
client_id int not null,
gig_id int not null,
rating enum('1','2','3','4','5') not null,
remarks varchar(2000) not null,
constraint fk_reviews_students foreign key (student_id) references students (student_id),
constraint fk_reviews_client foreign key (client_id) references client (client_id),
constraint fk_reviews_gigs foreign key (gig_id) references gigs (gig_id));
alter table client add column client_password varchar(50) not null;
alter table application add column approval varchar(15) default 'Waiting';
alter table gigs add column student_id int;
alter table students add column student_password varchar(50) not null;
alter table gigs add constraint fk_gigs_student foreign key (student_id) references students(student_id);
alter table application add constraint unique_student_gig unique (student_id, gig_id);
alter table reviews add constraint unique_gig_review unique (gig_id);
alter table department add constraint unique_department_name unique (department_name);
alter table skills add constraint unique_skills unique (skill_name);
alter table studentskills add constraint unique_student_skill unique (student_id,skill_id);
alter table gigskills add constraint gig_skill_composite unique (gig_id,skill_id);
insert into department (department_name) values
('Computer Science Engineering'),
('Information Technology'),
('Electronics and Communication Engineering'),
('Electrical Engineering'),
('Mechanical Engineering'),
('Civil Engineering');
insert into organization (organization_title,verification_status) values
('SAIL', 'Verified'),
('Tata Consultancy Services', 'Verified'),
('Infosys', 'Verified'),
('Wipro', 'Verified'),
('HCLTech', 'Verified'),
('Tech Mahindra', 'Verified'),
('Accenture', 'Verified'),
('IBM', 'Verified'),
('Microsoft', 'Verified'),
('Google', 'Verified'),
('Amazon', 'Verified'),
('Deloitte', 'Verified'),
('Cognizant', 'Verified'),
('Capgemini', 'Verified'),
('LTIMindtree', 'Verified'),
('PwC', 'Verified'),
('EY', 'Verified'),
('KPMG', 'Verified'),
('Flipkart', 'Verified'),
('Razorpay', 'Verified'),
('Zoho', 'Verified'),
('Prismforce','Verified');
insert into skills (skill_name) values
('Java'),
('Python'),
('C++'),
('SQL'),
('MySQL'),
('HTML'),
('CSS'),
('JavaScript'),
('React'),
('Spring Boot'),
('Data Analysis'),
('Machine Learning'),
('Graphic Design'),
('Video Editing'),
('Content Writing');
alter table client drop foreign key fk_client_organization;
alter table client add constraint fk_client_organization foreign key (organization_id) references organization(organization_id) on delete cascade;
alter table gigs drop foreign key fk_gigs_client;
alter table gigs add constraint fk_gigs_client foreign key (client_id) references client(client_id) on delete cascade;
alter table reviews drop foreign key fk_reviews_client;
alter table reviews add constraint fk_reviews_client foreign key (client_id) references client(client_id) on delete cascade;
alter table studentskills drop foreign key fk_studentskills_students;
alter table studentskills add constraint fk_studentskills_students foreign key (student_id) references students(student_id) on delete cascade;
alter table studentskills drop foreign key fk_studentskills_skills;
alter table studentskills add constraint fk_studentskills_skills foreign key (skill_id) references skills(skill_id) on delete cascade;
alter table gigskills drop foreign key fk_gigskills_gigs;
alter table gigskills add constraint fk_gigskills_gigs foreign key (gig_id) references gigs(gig_id) on delete cascade;
alter table gigskills drop foreign key fk_gigskills_skills;
alter table gigskills add constraint fk_gigskills_skills foreign key (skill_id) references skills(skill_id) on delete cascade;
alter table application drop foreign key fk_application_students;
alter table application add constraint fk_application_students foreign key (student_id) references students(student_id) on delete cascade;
alter table application drop foreign key fk_application_gigs;
alter table application add constraint fk_application_gigs foreign key (gig_id) references gigs(gig_id) on delete cascade;
alter table reviews drop foreign key fk_reviews_students;
alter table reviews add constraint fk_reviews_students foreign key (student_id) references students(student_id) on delete cascade;
alter table reviews drop foreign key fk_reviews_gigs;
alter table reviews add constraint fk_reviews_gigs foreign key (gig_id) references gigs(gig_id) on delete cascade;
alter table gigs drop foreign key fk_gigs_student;
alter table gigs add constraint fk_gigs_student foreign key (student_id) references students(student_id) on delete set null;
