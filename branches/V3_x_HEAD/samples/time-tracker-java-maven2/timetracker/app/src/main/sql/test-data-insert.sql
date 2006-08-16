insert into TIMECARD values ( 1, 'Approved',  '2006/05/15', 'Timecard 01', 1, 2);
insert into TIMECARD values ( 2, 'Approved',  '2006/05/15', 'Timecard 02', 2, 3);
insert into TIMECARD values ( 3, 'Approved',  '2006/05/15', 'Timecard 03', 3, 4);
insert into TIMECARD values ( 4, 'Approved',  '2006/05/15', 'Timecard 04', 4, 1);

insert into TIMECARD values ( 5, 'Rejected',  '2006/05/22', 'Timecard 05', 1, 2);
insert into TIMECARD values ( 6, 'Rejected',  '2006/05/22', 'Timecard 06', 2, 3);
insert into TIMECARD values ( 7, 'Rejected',  '2006/05/22', 'Timecard 07', 3, 4);
insert into TIMECARD values ( 8, 'Rejected',  '2006/05/22', 'Timecard 08', 4, 1);

insert into TIMECARD values ( 9, 'Submitted', '2006/05/29', 'Timecard 09', 1, 2);
insert into TIMECARD values (10, 'Submitted', '2006/05/29', 'Timecard 10', 2, 3);
insert into TIMECARD values (11, 'Submitted', '2006/05/29', 'Timecard 11', 3, 4);
insert into TIMECARD values (12, 'Submitted', '2006/05/29', 'Timecard 12', 4, 1);

insert into TIMECARD values (13, 'Draft',     '2006/06/05', 'Timecard 13', null, 2);
insert into TIMECARD values (14, 'Draft',     '2006/06/05', 'Timecard 14', null, 3);
insert into TIMECARD values (15, 'Draft',     '2006/06/05', 'Timecard 15', null, 4);
insert into TIMECARD values (16, 'Draft',     '2006/06/05', 'Timecard 16', null, 1);

commit;

insert into TIME_ALLOCATION values ( 1, '2006/05/15 09:00', '2006/05/15 05:00', 1,  1);
insert into TIME_ALLOCATION values ( 2, '2006/05/16 09:00', '2006/05/16 05:00', 1,  1);
insert into TIME_ALLOCATION values ( 3, '2006/05/15 09:00', '2006/05/15 05:00', 2,  2);
insert into TIME_ALLOCATION values ( 4, '2006/05/16 09:00', '2006/05/16 05:00', 2,  2);
insert into TIME_ALLOCATION values ( 5, '2006/05/15 09:00', '2006/05/15 05:00', 3,  3);
insert into TIME_ALLOCATION values ( 6, '2006/05/16 09:00', '2006/05/16 05:00', 3,  3);
insert into TIME_ALLOCATION values ( 7, '2006/05/15 09:00', '2006/05/15 05:00', 4,  4);
insert into TIME_ALLOCATION values ( 8, '2006/05/16 09:00', '2006/05/16 05:00', 4,  4);

insert into TIME_ALLOCATION values (11, '2006/05/22 09:00', '2006/05/22 05:00', 5,  5);
insert into TIME_ALLOCATION values (12, '2006/05/23 09:00', '2006/05/23 05:00', 5,  5);
insert into TIME_ALLOCATION values (13, '2006/05/22 09:00', '2006/05/22 05:00', 1,  6);
insert into TIME_ALLOCATION values (14, '2006/05/23 09:00', '2006/05/23 05:00', 1,  6);
insert into TIME_ALLOCATION values (15, '2006/05/22 09:00', '2006/05/22 05:00', 2,  7);
insert into TIME_ALLOCATION values (16, '2006/05/23 09:00', '2006/05/23 05:00', 2,  7);
insert into TIME_ALLOCATION values (17, '2006/05/22 09:00', '2006/05/22 05:00', 3,  8);
insert into TIME_ALLOCATION values (18, '2006/05/23 09:00', '2006/05/23 05:00', 3,  8);

insert into TIME_ALLOCATION values (21, '2006/05/29 09:00', '2006/05/29 05:00', 4,  9);
insert into TIME_ALLOCATION values (22, '2006/05/30 09:00', '2006/05/30 05:00', 4,  9);
insert into TIME_ALLOCATION values (23, '2006/05/29 09:00', '2006/05/29 05:00', 5, 10);
insert into TIME_ALLOCATION values (24, '2006/05/30 09:00', '2006/05/30 05:00', 5, 10);
insert into TIME_ALLOCATION values (25, '2006/05/29 09:00', '2006/05/29 05:00', 1, 11);
insert into TIME_ALLOCATION values (26, '2006/05/30 09:00', '2006/05/30 05:00', 1, 11);
insert into TIME_ALLOCATION values (27, '2006/05/29 09:00', '2006/05/29 05:00', 2, 12);
insert into TIME_ALLOCATION values (28, '2006/05/30 09:00', '2006/05/30 05:00', 2, 12);

insert into TIME_ALLOCATION values (29, '2006/06/05 09:00', '2006/06/05 05:00', 3, 13);
insert into TIME_ALLOCATION values (30, '2006/06/06 09:00', '2006/06/06 05:00', 3, 13);
insert into TIME_ALLOCATION values (31, '2006/06/05 09:00', '2006/06/05 05:00', 4, 14);
insert into TIME_ALLOCATION values (32, '2006/06/06 09:00', '2006/06/06 05:00', 4, 14);
insert into TIME_ALLOCATION values (33, '2006/06/05 09:00', '2006/06/05 05:00', 5, 15);
insert into TIME_ALLOCATION values (34, '2006/06/06 09:00', '2006/06/06 05:00', 5, 15);
insert into TIME_ALLOCATION values (35, '2006/06/05 09:00', '2006/06/05 05:00', 1, 16);
insert into TIME_ALLOCATION values (36, '2006/06/06 09:00', '2006/06/06 05:00', 1, 16);

commit;