# Use an official Java runtime as a parent image
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY build/libs/BookFlex-0.0.1-SNAPSHOT.jar /app/

# Verify the file was copied
RUN ls -l /app/

# Expose port 8080
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "BookFlex-0.0.1-SNAPSHOT.jar"]
