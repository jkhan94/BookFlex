# Use an official Node.js runtime as a parent image
FROM node:20

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the application in production mode
RUN npm run build

# Install serve to serve the built application
RUN npm install -g serve

# Expose port 3000 for the application
EXPOSE 3000

# Define the command to run the application
CMD ["serve", "-s", "build", "-l", "3000"]
