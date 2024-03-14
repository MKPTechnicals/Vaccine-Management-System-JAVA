# Vaccine Management System (Java)

![Java Badge](https://img.shields.io/badge/language-Java-orange.svg)
![MySQL Badge](https://img.shields.io/badge/database-MySQL-blue.svg)

## Description

The Vaccine Management System is a Java-based application designed to manage operations related to vaccination centers. It provides functionalities such as registration for vaccination, entry of vaccinated persons, and search capabilities.

## Features

- **Registration for Vaccination:** Allows individuals to register for vaccination by providing their personal details, including name, Aadhaar number, city, contact number, dose of vaccination, and vaccine type (Covaxin or Covishield).
  
- **Entry of Vaccinated Persons:** Enables authorized personnel to record the vaccination details of individuals, including their name, Aadhaar number, city, contact number, and vaccine type.

- **Search Functionality:** Allows users to search for vaccination records using the Aadhaar number of the individual. It provides details such as the name, Aadhaar number, city, contact number, dose of vaccination, and vaccine type.

## Usage

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/Vaccine-Management-System-JAVA.git
    ```

2. Set up the MySQL database:

    - Create a MySQL database.
    - Import the provided SQL schema to create the required tables.
    - Update the database credentials in the Java code (`App.java`) with your actual database credentials.

3. Compile and run the application:

    ```bash
    javac App.java
    java App
    ```

4. Follow the on-screen prompts to navigate through the application and utilize its features.

## Using Your Own Database Details

To use your own database details, follow these steps:

- **Create Database:** Create a MySQL database either locally or on a remote server.

- **Import Schema:** Import the provided SQL schema (`schema.sql`) into your database to create the required tables.

- **Update Credentials:** Update the database credentials in the Java code (`App.java`) with your actual database credentials. Modify the following line with your database URL, username, and password:

    ```java
    con = DriverManager.getConnection(
        "jdbc:mysql://your_database_host/your_database_name",
        "your_username",
        "your_password");
    ```
## Authors

- [@mkptechnicals](https://www.github.com/MKPTechnicals)

Feel free to customize the application to fit your specific requirements. You can modify the code, add new features, or improve existing functionalities based on your needs.
