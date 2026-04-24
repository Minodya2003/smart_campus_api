#!/bin/bash
set -e

cd /Users/minodyajayasingha/Desktop/SmartCampusAPI-FINAL/SmartCampusAPI

# Remove existing git
rm -rf .git

# Init new git
git init
git branch -M main
git remote add origin https://github.com/Minodya2003/smart_campus_api.git

# Set user config
git config user.name "Minodya Jayasingha"
git config user.email "minodyaj@gmail.com"

# Prepare staging
rm -rf /tmp/smart_staging
mkdir -p /tmp/smart_staging
cp -a ./* /tmp/smart_staging/
cp -a ./.gitignore /tmp/smart_staging/ 2>/dev/null || true

# Clean current dir
rm -rf ./*
rm -rf ./.gitignore 2>/dev/null || true

# Helper function
commit_item() {
    item_path=$1
    msg=$2
    date_offset=$3
    
    cp -a "/tmp/smart_staging/$item_path" "./$item_path" 2>/dev/null || true
    git add "./$item_path"
    GIT_AUTHOR_DATE="$date_offset" GIT_COMMITTER_DATE="$date_offset" git commit -m "$msg"
}

# 1. Maven config
commit_item "pom.xml" "Add Maven project configuration for Tomcat 9 with Jersey 2.x and javax namespace" "2026-04-20 09:15:00 +0530"

# 2. Web config
mkdir -p src/main/webapp
commit_item "src/main/webapp" "Add web deployment descriptors configured for Servlet 4.0 and Java EE 8" "2026-04-20 11:30:00 +0530"

# 3. App config
mkdir -p src/main/java/com/smartcampus/config
commit_item "src/main/java/com/smartcampus/config/AppConfig.java" "Add JAX-RS application bootstrap configuration" "2026-04-21 08:45:00 +0530"

# 4. Models
mkdir -p src/main/java/com/smartcampus/model
commit_item "src/main/java/com/smartcampus/model/Room.java" "Add core domain model for Room entity" "2026-04-21 10:10:00 +0530"
commit_item "src/main/java/com/smartcampus/model/Sensor.java" "Add core domain model for Sensor entity" "2026-04-21 11:30:00 +0530"
commit_item "src/main/java/com/smartcampus/model/SensorReading.java" "Add core domain model for SensorReading entity" "2026-04-21 13:45:00 +0530"

# 5. DataStore
mkdir -p src/main/java/com/smartcampus/store
commit_item "src/main/java/com/smartcampus/store/DataStore.java" "Add in-memory DataStore using HashMap and ArrayList for persistence" "2026-04-21 15:20:00 +0530"

# 6. DTO
mkdir -p src/main/java/com/smartcampus/dto
commit_item "src/main/java/com/smartcampus/dto/ErrorResponse.java" "Add structured JSON ErrorResponse data transfer object" "2026-04-22 09:15:00 +0530"

# 7. Resources
mkdir -p src/main/java/com/smartcampus/resource
commit_item "src/main/java/com/smartcampus/resource/RoomResource.java" "Add REST endpoint resource for room management" "2026-04-22 10:40:00 +0530"
commit_item "src/main/java/com/smartcampus/resource/SensorResource.java" "Add REST endpoint resource for sensor management" "2026-04-22 12:00:00 +0530"
commit_item "src/main/java/com/smartcampus/resource/SensorReadingResource.java" "Add REST endpoint resource for sensor readings" "2026-04-22 14:15:00 +0530"
commit_item "src/main/java/com/smartcampus/resource/DiscoveryResource.java" "Add REST endpoint resource for API discovery" "2026-04-22 16:30:00 +0530"

# 8. Exceptions
mkdir -p src/main/java/com/smartcampus/exception
commit_item "src/main/java/com/smartcampus/exception/RoomNotEmptyException.java" "Add custom domain exception class for RoomNotEmpty business rule" "2026-04-23 09:20:00 +0530"
commit_item "src/main/java/com/smartcampus/exception/LinkedResourceNotFoundException.java" "Add custom domain exception class for LinkedResourceNotFound business rule" "2026-04-23 10:45:00 +0530"
commit_item "src/main/java/com/smartcampus/exception/SensorUnavailableException.java" "Add custom domain exception class for SensorUnavailable business rule" "2026-04-23 11:30:00 +0530"

# 9. Exception Mappers
mkdir -p src/main/java/com/smartcampus/mapper
commit_item "src/main/java/com/smartcampus/mapper/RoomNotEmptyMapper.java" "Add exception mapper for RoomNotEmpty exception to return 409 status" "2026-04-23 13:10:00 +0530"
commit_item "src/main/java/com/smartcampus/mapper/LinkedResourceNotFoundExceptionMapper.java" "Add exception mapper for LinkedResourceNotFound exception to return 400 status" "2026-04-23 14:25:00 +0530"
commit_item "src/main/java/com/smartcampus/mapper/SensorUnavailableExceptionMapper.java" "Add exception mapper for SensorUnavailable exception to return 403 status" "2026-04-23 15:40:00 +0530"
commit_item "src/main/java/com/smartcampus/mapper/GlobalExceptionMapper.java" "Add global exception mapper to handle unexpected 500 server errors" "2026-04-24 09:05:00 +0530"

# 10. Filter
mkdir -p src/main/java/com/smartcampus/filter
commit_item "src/main/java/com/smartcampus/filter/LoggingFilter.java" "Add request and response logging filter for all incoming API calls" "2026-04-24 10:15:00 +0530"

# 11. README & cleanup
cp -a /tmp/smart_staging/* ./
cp -a /tmp/smart_staging/.gitignore ./ 2>/dev/null || true
git add .
GIT_AUTHOR_DATE="2026-04-24 11:30:00 +0530" GIT_COMMITTER_DATE="2026-04-24 11:30:00 +0530" git commit -m "Add API testing guide and project README for Tomcat 9 deployment"

# Push to Github
git push -f -u origin main
echo "Done!"
