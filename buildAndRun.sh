#!/bin/sh
mvn clean package && docker build -t de.bimalo.test/easy-user-service .
docker rm -f easy-user-service || true && docker run -d -p 8080:8080 -p 4848:4848 --name easy-user-service de.bimalo.test/easy-user-service 
