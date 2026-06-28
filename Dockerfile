# ---------- Etapa 1: build ----------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiamos lo necesario para instalar las dependencias
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copiaos el codigo fuente y se compila :D
COPY src ./src
# Nos saltamos los test ya que de eso se encarga CI :D
RUN ./mvnw clean package -DskipTests -B

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Usa un usuario sin privilegios para correr el proyecto
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

#Exponemos el puerto
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]