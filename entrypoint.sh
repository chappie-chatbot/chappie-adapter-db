#!/bin/bash

cd /chappie

git clone https://github.com/chappie-chatbot/chappie-common.git chappie-common
git clone https://github.com/chappie-chatbot/chappie-adapter-db.git chappie-adapter-db

cd chappie-common
git checkout master
git pull
mvn clean install -DskipTests
cd ../
cd chappie-adapter-db
git checkout master
git pull
mvn clean install -DskipTests

java -jar $(ls /chappie/chappie-adapter-db/target/chappie-adapter-db*.jar | head -n 1)

