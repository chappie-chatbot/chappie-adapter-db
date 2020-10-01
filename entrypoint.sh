#!/bin/bash

cd /chappie
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

