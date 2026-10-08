# ============================================
# Etapa de build: compila el JAR con Maven
# ============================================
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia primero solo el pom.xml para cachear la descarga de dependencias
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ============================================
# Etapa runtime: JRE mínimo + JAR
# ============================================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Usuario sin privilegios
RUN useradd --system --create-home --shell /usr/sbin/nologin app

# Copia el JAR generado (finalName=app en el pom.xml)
COPY --from=build /app/target/app.jar app.jar

# Directorio de archivos subidos: se crea aquí y se usa como volumen
# para que los documentos sobrevivan a recrear el contenedor
RUN mkdir -p /app/uploads && chown -R app:app /app
VOLUME /app/uploads

USER app

EXPOSE 8080

# Variables de entorno requeridas en runtime (ver README/comandos de docker run):
#   SPRING_DATASOURCE_URL=jdbc:mysql://<host>:3306/sgp   (obligatorio: dentro del
#       contenedor "localhost" es el propio contenedor, no la máquina)
#   DB_USERNAME / DB_PASSWORD        credenciales de MySQL
#   JWT_SECRET                       clave para firmar los tokens
#   BOOTSTRAP_ADMIN_CORREO / BOOTSTRAP_ADMIN_PASSWORD   admin inicial opcional
#   SHOW_SQL=true                    muestra SQL en consola (solo desarrollo)
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
