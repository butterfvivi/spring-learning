for dir in redis-master redis-slave; do
 if [ "$dir" == "redis-master" ]; then
    port=6379
  elif [ "$dir" == "redis-slave" ]; then
    port=6380
  fi
echo "port $port
save 900 1
save 300 10
save 60 10000
dbfilename dump.rdb
dir /data
appendonly yes
appendfilename "appendonly.aof"
appendfsync everysec
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000" > /docker/redis/$dir/redis.conf;done