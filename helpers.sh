docker compose run --rm --entrypoint /bin/sh minio-setup -c "\
  mc alias set myminio http://minio:9000 access minioSecret123 && \
  mc rb --force myminio/ingest-data 2>/dev/null || true && \
  mc mb myminio/ingest-data && \
  mc event add myminio/ingest-data arn:minio:sqs::1:kafka --event put && \
  echo '---- RESULTAT ----' && \
  mc event ls myminio/ingest-data"


docker compose exec kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server kafka:29092 --topic minio-events --from-beginning

docker compose run --rm --entrypoint /bin/sh minio-setup -c "\
  mc alias set myminio http://minio:9000 access minioSecret123 && \
  mc admin config set myminio notify_kafka:1 brokers=kafka:29092 topic=minio-events format=namespace && \
  echo 'Configuration injectée. Redémarrage du service MinIO...' && \
  mc admin service restart myminio"

docker compose run --rm --entrypoint /bin/sh minio-setup -c "mc alias set myminio http://minio:9000 access minioSecret123 && mc admin trace myminio"