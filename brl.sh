# introduction
INFOC='\033[1;36m'
echo "${INFOC}Note: May fail to connect to db if VPN on!"

# load ENVs
. .local-env

# build
mvn clean package

# run 
java -jar -Dspring.profiles.active=local target/hypher-backend-service-0.0.1-SNAPSHOT.jar