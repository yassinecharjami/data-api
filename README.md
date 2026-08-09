# data-api

A Spring Boot API for managing pipeline configurations, uploading files to MinIO, and tracking pipeline run status.

## Overview

This project provides a lightweight orchestration API for:
- creating pipeline configurations
- generating MinIO presigned upload URLs
- tracking upload run status
- processing MinIO upload events through Kafka

The service uses:
- Spring Boot 4.0.7
- Spring Data JPA with PostgreSQL
- Spring Kafka for event consumption
- MinIO for object storage
- Prometheus and Grafana for monitoring

## Key Components

- `PipelineController` — REST endpoints for pipeline config and upload flows
- `PipelineConfigService` — persists pipeline configuration metadata
- `PipelineRunService` — creates pipeline runs and returns MinIO presigned URLs
- `MinioService` — generates presigned PUT URLs for MinIO
- `MinioEventWorker` — consumes Kafka events and marks runs as completed
- `MinioConfig` — provides `MinioClient` bean configuration

## API Endpoints

### Create a pipeline configuration

`POST /pipeline/config`

Request body:
```json
{
  "format": "json",
  "source": "local",
  "destination": "minio",
  "team": "data-team"
}
```

Response:
- `201 Created` with saved `PipelineConfig`

### Get pipeline configuration

`GET /pipeline/{pipelineId}/config`

Response:
- `200 OK` with `PipelineConfig`

### Initiate file upload

`POST /pipeline/{pipelineId}/upload`

Response:
- `200 OK` with `UploadResponse`
- contains `runId` and `presignedUrl`

### Check run status

`GET /pipeline/{pipelineId}/run/{runId}/status`

Response:
- `200 OK` with `RunStatusResponse`
- status values are from `PipelineStatus`

### Upload file using presigned URL (test helper)

`POST /pipeline/test-upload`

Form fields:
- `file` — file upload
- `presignedUrl` — presigned URL returned by `/pipeline/{pipelineId}/upload`

## Configuration

Application properties are defined in `src/main/resources/application.properties`.

Important values:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=secret
spring.kafka.bootstrap-servers=kafka:29092
spring.kafka.consumer.group-id=data-core-group-v2
minio.url=http://localhost:9000
minio.access.key=access
minio.secret.key=minioSecret123
minio.bucket.name=ingest-data
```

## Docker Compose

The repository includes `compose.yaml` to run the required infrastructure:
- PostgreSQL
- Kafka
- MinIO
- MinIO client setup
- Prometheus
- Grafana

Start services with:

```bash
docker compose -f compose.yaml up --detach --wait
```

Stop and remove them with:

```bash
docker compose -f compose.yaml down
```

## Build and Run

From the project root:

```bash
./mvnw clean package
./mvnw spring-boot:run
```

Or run the packaged jar:

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

## Notes

- The project uses Spring Boot auto-configuration for web, JPA, Kafka, and validation.
- MinIO bucket name is configured as `ingest-data`.
- The event workflow expects MinIO to emit Kafka `minio-events` on file upload.
- The `PipelineConfigRequest` only allows `json`, `csv`, or `parquet` formats and the `local` source / `minio` destination.

## Swagger / OpenAPI

The project includes SpringDoc OpenAPI UI. Once the app is running, the API docs are typically available at:

- `/swagger-ui.html`
- or `/swagger-ui/index.html`

## Testing

Run tests with:

```bash
./mvnw test
```

