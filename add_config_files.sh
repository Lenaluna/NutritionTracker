#!/bin/bash

# Projektname und Basisverzeichnis
PROJECT_NAME="NutritionTracker"
CONFIG_DIR="src/main/resources"

# Gehe sicher, dass das Verzeichnis existiert
mkdir -p $CONFIG_DIR

# application-dev.yml hinzufügen
cat > $CONFIG_DIR/application-dev.yml <<EOL
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  jpa:
    database: H2
    generate-ddl: true
    show-sql: true
    hibernate:
      ddl-auto: create
      enable_lazy_load_no_trans: true # lazy loading
      naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
      database-platform: org.hibernate.dialect.H2Dialect
  datasource:
    driverClassName: org.h2.Driver
    url: "jdbc:h2:mem:devDB"
    username: sa
    password: ""
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 100MB

logging.level.org:
  springframework.web: DEBUG
  springframework.jps: INFO
  hibernate: INFO

project-name: NutritionTracker (Development)
EOL

# application-integration-test.yml hinzufügen
cat > $CONFIG_DIR/application-integration-test.yml <<EOL
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  jpa:
    database: POSTGRESQL
    generate-ddl: true
    show-sql: true
    hibernate:
      ddl-auto: update
      enable_lazy_load_no_trans: true # lazy loading
      naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
      database-platform: org.hibernate.dialect.PostgreSQLDialect
  datasource:
    driverClassName: org.postgresql.Driver
    url: "jdbc:postgresql://localhost:5432/paf2024"
    username: paf2024
    password: paf2024
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 100MB

logging.level.org:
  springframework.web: DEBUG
  springframework.jps: INFO
  hibernate: INFO

project-name: NutritionTracker (Integration Test)
EOL

echo "Konfigurationsdateien für Dev und Integration Test erfolgreich erstellt!"
