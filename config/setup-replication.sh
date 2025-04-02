#!/bin/bash

echo "⏳ Waiting for MariaDB Master & Slave to be ready..."
sleep 10  # 추가 대기 (안정성)

echo "🔐 Creating replication user on Master..."
mysql -h mariadb-master -uroot -proot -e "
  CREATE USER IF NOT EXISTS 'repl'@'%' IDENTIFIED BY 'replpassword';
  GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
  FLUSH PRIVILEGES;
"

echo "🔄 Getting Master status..."
MASTER_STATUS=$(mysql -h mariadb-master -uroot -proot -e "SHOW MASTER STATUS\G" | grep -E 'File|Position')

MASTER_LOG_FILE=$(echo "$MASTER_STATUS" | grep 'File' | awk '{print $2}')
MASTER_LOG_POS=$(echo "$MASTER_STATUS" | grep 'Position' | awk '{print $2}')

echo "📌 Master Log File: $MASTER_LOG_FILE, Position: $MASTER_LOG_POS"

echo "🔗 Connecting Slave to Master..."
mysql -h mariadb-slave -uroot -proot -e "
  STOP SLAVE;
  RESET SLAVE;
  CHANGE MASTER TO
    MASTER_HOST='mariadb-master',
    MASTER_USER='repl',
    MASTER_PASSWORD='replpassword',
    MASTER_LOG_FILE='$MASTER_LOG_FILE',
    MASTER_LOG_POS=$MASTER_LOG_POS;
  START SLAVE;
"

echo "✅ Replication setup completed!"
echo "🛑 Exiting replication setup container."
exit 0  # 실행 후 정상 종료