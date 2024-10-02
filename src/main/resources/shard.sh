cat ulyss12.txt | tr '\r\n' ' ' > ulyss12-sharded-tmp.txt
python3 shard.py
rm ulyss12-sharded-tmp.txt