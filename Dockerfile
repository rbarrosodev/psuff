# Use the official MySQL Docker image
FROM mysql:latest

# Set the root password (change 'password' to your desired password)
ENV MYSQL_ROOT_PASSWORD=digorbr910

# Create a new database (optional)
ENV MYSQL_DATABASE=psuff

# Copy a custom SQL script to initialize the database (optional)
COPY ./init.sql /docker-entrypoint-initdb.d/

# Expose MySQL port (default is 3306)
EXPOSE 3306
