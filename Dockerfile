# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
# Copiamos el pom y el código fuente
COPY pom.xml .
COPY src ./src
# Compilamos saltando las pruebas para que sea más rápido
RUN mvn clean package -DskipTests

# Etapa 2: Producción (Run)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Solo copiamos el .jar generado en la etapa anterior
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
# Exponemos el puerto
EXPOSE 8080
# Ejecutamos la aplicación
CMD ["java", "-jar", "app.jar"]