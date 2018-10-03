#!/bin/bash
docker stop orderProcessor
docker stop inventory
docker stop postgresOrderProcessor
docker stop postgresInventory
docker rm orderProcessor
docker rm inventory
docker rm postgresOrderProcessor
docker rm postgresInventory
docker ps