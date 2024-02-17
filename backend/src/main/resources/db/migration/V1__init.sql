drop table if exists blockchain CASCADE
drop table if exists code_review CASCADE
drop table if exists commit CASCADE
drop table if exists dead_letter CASCADE
drop table if exists email CASCADE
drop table if exists filter CASCADE
drop table if exists git_organization CASCADE
drop table if exists git_organization_member CASCADE
drop table if exists git_repo CASCADE
drop table if exists git_repo_spark_line CASCADE
drop table if exists git_repo_member CASCADE
drop table if exists history CASCADE
drop table if exists issue CASCADE
drop table if exists member CASCADE
drop table if exists member_role CASCADE
drop table if exists organization CASCADE
drop table if exists pull_request CASCADE
drop table if exists result CASCADE
drop table if exists search CASCADE
drop sequence if exists hibernate_sequence
create sequence hibernate_sequence start with 1 increment by 1
create table blockchain (id bigint not null, address varchar(255), created_at timestamp, deleted_at timestamp, updated_at timestamp, contribute_type varchar(255), member_id BINARY(16), primary key (id))
create table code_review (id bigint not null, amount integer not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, year integer not null, member_id BINARY(16), primary key (id))
create table commit (id bigint not null, amount integer not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, year integer not null, member_id BINARY(16), primary key (id))
create table dead_letter (id BINARY(16) not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, group_id varchar(255), key_name varchar(255), offset_number bigint, partition_id integer, topic_name varchar(255) not null, value_object clob not null, primary key (id))
create table email (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, code integer not null, member_id binary not null, primary key (id))
create table filter (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, filter varchar(255) not null, search_id bigint, primary key (id))
create table git_organization (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, name varchar(255) not null, profile_image varchar(255), primary key (id))
create table git_organization_member (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, git_organization_id bigint, member_id BINARY(16), primary key (id))
create table git_repo (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, name varchar(255) not null, primary key (id))
create table git_repo_spark_line (git_repo_id bigint not null, spark_line integer)
create table git_repo_member (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, additions integer, commits integer, deletions integer, git_repo_id bigint, member_id BINARY(16), primary key (id))
create table history (id bigint not null, amount decimal(19,2), created_at timestamp, deleted_at timestamp, updated_at timestamp, transaction_hash varchar(255), blockchain_id bigint, primary key (id))
create table issue (id bigint not null, amount integer not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, year integer not null, member_id BINARY(16), primary key (id))
create table member (id BINARY(16) not null, auth_step varchar(255), created_at timestamp, deleted_at timestamp, updated_at timestamp, email_address varchar(255), github_id varchar(255) not null, github_token varchar(255), name varchar(255), profile_image varchar(255), refresh_token varchar(255), tier varchar(255), wallet_address varchar(255), organization_id bigint, primary key (id))
create table member_role (member_id BINARY(16) not null, role varchar(255))
create table organization (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, email_endpoint varchar(255) not null, name varchar(255) not null, organization_status varchar(255), organization_type varchar(255), primary key (id))
create table pull_request (id bigint not null, amount integer not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, year integer not null, member_id BINARY(16), primary key (id))
create table result (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, name varchar(255) not null, search_id bigint, primary key (id))
create table search (id bigint not null, created_at timestamp, deleted_at timestamp, updated_at timestamp, name varchar(255) not null, page integer, type varchar(255), primary key (id))
alter table git_organization add constraint UK_dgmbpppjwiyfd1mb0r2humfn9 unique (name)
alter table git_repo add constraint UK_gbc148so8fy7d3e5qyjh6td1i unique (name)
alter table member add constraint UK_6p926dfj54l7npjasf3kx2uxm unique (github_id)
alter table organization add constraint UK_8j5y8ipk73yx2joy9yr653c9t unique (name)
alter table blockchain add constraint FKqfhgfm927cup05nmlxup4ppnq foreign key (member_id) references member
alter table code_review add constraint FKkwyxcx28juq3tlq6c9jmh5e3t foreign key (member_id) references member
alter table commit add constraint FK3jowax64sao5snycq0qjxh1u6 foreign key (member_id) references member
alter table filter add constraint FK5trxckbdq8rtb7tq9qwmketku foreign key (search_id) references search
alter table git_organization_member add constraint FKld23w4vl9piv1ulxil13kct11 foreign key (git_organization_id) references git_organization
alter table git_organization_member add constraint FKrb4njwimavgawcl0rlrv8sowe foreign key (member_id) references member
alter table git_repo_spark_line add constraint FKol1jplw3fx5pddlownwld75ay foreign key (git_repo_id) references git_repo
alter table git_repo_member add constraint FKkifmaexwdu7qosrkxsrvyj0re foreign key (git_repo_id) references git_repo
alter table git_repo_member add constraint FKrtkod3rv4w21465r2m2jgooo1 foreign key (member_id) references member
alter table history add constraint FKtrtftn6dy02gshedfhrgxaeal foreign key (blockchain_id) references blockchain
alter table issue add constraint FKgj9b27brkevgyi6mit3uq92lp foreign key (member_id) references member
alter table member add constraint FKlfkumie1qe5t7usigehe0yjyk foreign key (organization_id) references organization
alter table member_role add constraint FK34g7epqlcxqloewku3aoqhhmg foreign key (member_id) references member
alter table pull_request add constraint FKmmd40qqpg2lia9n19hq9yv3bf foreign key (member_id) references member
