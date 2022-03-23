
echo mvn clean -U -DskipTests
mvn clean -U -DskipTests

echo mvn mybatis-generator:generate -X -e
mvn mybatis-generator:generate -X -e
