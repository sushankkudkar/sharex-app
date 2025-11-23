#!/bin/bash

echo "======================================================"
echo "🔥 FULL CLEAN RESET (Docker + Volumes)"
echo "======================================================"

echo ""
echo "🛑 Stopping & Removing all containers + volumes..."
docker-compose down -v

echo ""
echo "🧹 Removing orphan networks (if any)..."
docker network prune -f

echo ""
echo "🚀 Starting Docker Compose fresh..."
docker-compose up -d

echo ""
echo "======================================================"
echo "🔥 CLEAN RESET COMPLETE"
echo "======================================================"
echo "✔ All containers recreated fresh"
echo "✔ All Postgres data wiped"
echo "✔ All Redis cache wiped"
echo "✔ All Kafka logs & topics wiped"
echo ""
echo "👉 Now start Spring Boot:"
echo "       ./gradlew bootRun"
echo "======================================================"
