FROM openjdk:8-jre
ENTRYPOINT ["/usr/bin/java", "-jar", "controle-ponto.jar"]
ARG JAR_FILE
ADD target/${JAR_FILE} controle-ponto.jar