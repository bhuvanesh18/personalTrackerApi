# Docker Setup for Personal Tracker API

This guide explains how to build and run the Personal Tracker API using Docker.

## Prerequisites

- Docker installed on your system
- Docker Compose (optional, for easier orchestration)

## Building the Docker Image

### Using Dockerfile

```bash
docker build -t personal-tracker-api:latest .
```

### Using Docker Compose

```bash
docker-compose build
```

## Running the Application

### Using Docker

```bash
docker run -p 8080:8080 --name personal-tracker-api personal-tracker-api:latest
```

### Using Docker Compose

```bash
docker-compose up
```

To run in detached mode:

```bash
docker-compose up -d
```

## Accessing the Application

Once the container is running, access the API at:

- **Health Check**: `http://localhost:8080/test`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Stopping the Application

### Docker Container

```bash
docker stop personal-tracker-api
```

### Docker Compose

```bash
docker-compose down
```

## Docker Features

- **Multi-stage build**: Reduces image size by separating build and runtime stages
- **Health checks**: Built-in health checks for container orchestration
- **JDK 21**: Uses Java 21 for optimal performance and compatibility
- **Minimal runtime**: Uses OpenJDK slim image for smaller footprint
- **.dockerignore**: Excludes unnecessary files from the build context

## Environment Variables

You can override the following environment variables:

- `SPRING_APPLICATION_NAME`: Application name (default: `personalTrackerAPI`)
- `SERVER_PORT`: Server port (default: `8080`)

Example:

```bash
docker run -p 8080:8080 \
  -e SPRING_APPLICATION_NAME=myapp \
  -e SERVER_PORT=8080 \
  personal-tracker-api:latest
```

## Troubleshooting

### View logs

```bash
docker logs personal-tracker-api
```

### View running containers

```bash
docker ps
```

### Remove image

```bash
docker rmi personal-tracker-api:latest
```
