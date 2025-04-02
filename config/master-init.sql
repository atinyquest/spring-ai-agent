-- 슬레이브 계정 생성 (존재하면 무시)
CREATE USER IF NOT EXISTS 'repl'@'%' IDENTIFIED BY 'replpassword';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;