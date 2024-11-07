# Usa una base image per OpenJDK
FROM openjdk:21-jdk-slim

# Definisci la directory di lavoro all'interno del container
WORKDIR /opt

# Esponi la porta su cui l'applicazione sarà in ascolto
ENV PORT=8081
EXPOSE 8081

# Copia lo script wait-for-it.sh per attendere MySQL
COPY wait-for-it.sh /opt/wait-for-it.sh
RUN chmod +x /opt/wait-for-it.sh

# Copia il JAR della tua applicazione
COPY target/prenotazione-0.0.1-SNAPSHOT.jar /opt/app.jar

# Punto di ingresso del container, avvia l'applicazione e aspetta MySQL
ENTRYPOINT ["/opt/wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "app.jar"]

