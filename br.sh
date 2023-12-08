# load ENVs
. .local-env

# build
mvn clean package

# run 
java -jar target/hypher-backend-service-0.0.1-SNAPSHOT.jar