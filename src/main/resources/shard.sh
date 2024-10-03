cat ulyss12.txt | tr '\r\n' ' ' > ulyss12-sharded-tmp.txt
./shard-helper.sh
rm ulyss12-sharded-tmp.txt