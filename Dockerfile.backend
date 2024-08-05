# Use an official Java runtime as a parent image
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Define build arguments
ARG AWS_SECRET_KEY
ARG AWS_BUCKET_URL
ARG AWS_ACCESS_KEY

# Set environment variables
ENV AWS_SECRET_KEY=${AWS_SECRET_KEY}
ENV AWS_BUCKET_URL=${AWS_BUCKET_URL}
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY}

# Copy the application JAR file into the container
COPY build/libs/BookFlex-0.0.1-SNAPSHOT.jar /app/

# Verify the file was copied
RUN ls -l /app/

# Expose port 8080
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "BookFlex-0.0.1-SNAPSHOT.jar"]
