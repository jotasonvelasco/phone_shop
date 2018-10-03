#!/bin/bash
#docker rm -f $(docker ps -aq)
docker rm -f inventory
docker rm -f orderProcessor
docker rm -f postgresOrderProcessor
docker rm -f postgresInventory
docker-compose up -d