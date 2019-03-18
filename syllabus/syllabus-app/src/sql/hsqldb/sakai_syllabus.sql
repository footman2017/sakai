alter table SAKAI_SYLLABUS_DATA drop constraint FK3BC123AA4FDCE067
alter table SAKAI_SYLLABUS_ATTACH drop constraint FK4BF41E45A09831E0
drop table SAKAI_SYLLABUS_DATA if exists
drop table SAKAI_SYLLABUS_ITEM if exists
drop table SAKAI_SYLLABUS_ATTACH if exists
create table SAKAI_SYLLABUS_DATA (
   id bigint generated by default as identity (start with 1),
   lockId integer not null,
   asset longvarchar,
   position integer not null,
   title varchar(256),
   xview varchar(16),
   status varchar(64),
   emailNotification varchar(128),
   surrogateKey bigint,
   methoda varchar(256),
)
create table SAKAI_SYLLABUS_ITEM (
   id bigint generated by default as identity (start with 1),
   lockId integer not null,
   userId varchar(36) not null,
   contextId varchar(36) not null,
   redirectURL varchar(512),
   unique (userId, contextId)
)
create table SAKAI_SYLLABUS_ATTACH (
   syllabusAttachId bigint generated by default as identity (start with 1),
   lockId integer not null,
   attachmentId varchar(256) not null,
   syllabusAttachName varchar(256) not null,
   syllabusAttachSize varchar(256),
   syllabusAttachType varchar(256),
   createdBy varchar(256),
   syllabusAttachUrl varchar(256) not null,
   lastModifiedBy varchar(256),
   syllabusId bigint
)
create index syllabus_position on SAKAI_SYLLABUS_DATA (position)
alter table SAKAI_SYLLABUS_DATA add constraint FK3BC123AA4FDCE067 foreign key (surrogateKey) references SAKAI_SYLLABUS_ITEM
create index syllabus_userId on SAKAI_SYLLABUS_ITEM (userId)
create index syllabus_contextId on SAKAI_SYLLABUS_ITEM (contextId)
alter table SAKAI_SYLLABUS_ATTACH add constraint FK4BF41E45A09831E0 foreign key (syllabusId) references SAKAI_SYLLABUS_DATA
