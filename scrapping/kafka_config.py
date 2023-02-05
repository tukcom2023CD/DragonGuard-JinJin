from base_url import KAFKA_BASE_URL
from kafka import KafkaProducer 
from json import dumps

producer = KafkaProducer(acks=0,
            compression_type='gzip',
            bootstrap_servers=[KAFKA_BASE_URL],
            value_serializer=lambda x: dumps(x).encode('utf-8'),
            retries=1
          )
