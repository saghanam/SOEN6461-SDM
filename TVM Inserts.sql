INSERT INTO customers VALUES( 'CVER2345', 'Rishab Kumar', 29, 'rk@gm.com', 'pwd2345', '1247569232', 0 );
INSERT INTO customers VALUES( 'CVER2346', 'Saghana Mahesh', 22, 'sm@gm.com', 'pwd2346', '1245669433', 0 );
INSERT INTO customers VALUES( 'CVER2347', 'Naman Kumar', 22, 'nk@gm.com', 'pwd2347', '1245453633', 0 );
INSERT INTO customers VALUES( 'CVER2348', 'Yong Tang', 22, 'yt@gm.com', 'pwd2348', '1242222633', 0 );
INSERT INTO customers VALUES( 'CVER2349', 'Nasrin Maarefi', 22, 'nm@gm.com', 'pwd2349', '1242529633', 0 );
INSERT INTO customers VALUES( 'CVER2350', 'Mansi Lakhani', 22, 'ml@gm.com', 'pwd2350', '1942545633', 0 );

INSERT INTO bikes VALUES( 'BIXI1001', 'B00');
INSERT INTO bikes VALUES( 'BIXI1002','B00');
INSERT INTO bikes VALUES( 'BIXI1003','B00');
INSERT INTO bikes VALUES( 'BIXI1004','B00');
INSERT INTO bikes VALUES( 'BIXI1005' ,'B00');
INSERT INTO bikes VALUES( 'BIXI1006','B00');
INSERT INTO bikes VALUES( 'BIXI1007','B00');
INSERT INTO bikes VALUES( 'BIXI1008','B00');
INSERT INTO bikes VALUES( 'BIXI1009','B00');

INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DVER0001',  'BIXI1001');
INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DVER0002',  'BIXI1002');
INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DVER0003',  'BIXI1003');
INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DATW0001',  'BIXI1004');
INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DATW0002',  'BIXI1005');
INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DATW0003',  'BIXI1006');
INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DOUT0001',  'BIXI1007');
INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DOUT0002',  'BIXI1008');
INSERT INTO docks( dock_id,  bike_id ) VALUES( 'DOUT0003',  'BIXI1009');

INSERT INTO stations VALUES( 'VER', 'DVER0001' ); 
INSERT INTO stations VALUES( 'VER', 'DVER0002' );
INSERT INTO stations VALUES( 'VER', 'DVER0003' );
INSERT INTO stations VALUES( 'ATW', 'DATW0001' );
INSERT INTO stations VALUES( 'ATW', 'DATW0002' );
INSERT INTO stations VALUES( 'ATW', 'DATW0003' );
INSERT INTO stations VALUES( 'OUT', 'DOUT0001' );
INSERT INTO stations VALUES( 'OUT', 'DOUT0002' );
INSERT INTO stations VALUES( 'OUT', 'DOUT0003' );

INSERT INTO auth VALUES( 'CVER2345',  'pwd2345' );
INSERT INTO auth VALUES( 'CVER2346',  'pwd2346' );
INSERT INTO auth VALUES( 'CVER2347',  'pwd2347' );
INSERT INTO auth VALUES( 'CVER2348',  'pwd2348' );
INSERT INTO auth VALUES( 'CVER2349',  'pwd2349' );
INSERT INTO auth VALUES( 'CVER2350',  'pwd2350' );

