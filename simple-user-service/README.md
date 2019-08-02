# easy-user-service

Implements a REST interface with Java EE 8 (JAX-RS, JPA) to manage users in an in-memory relational database. The application
tries to avoid using to many dependencies to other libraries. It was verified with Payara 5.x and H2 database automatically installed
with Payara.

It provides the following endpoints:

| Endpoint                                           | Method | Description                                       |
|----------------------------------------------------|--------|---------------------------------------------------|
| simple-user-service/api/users                      | GET    | List all users available in the database.         |
| simple-user-service/api/users?lastName=searchvalue | GET    | Lookup users where lastName like = xxx.           |
| simple-user-service/api/users/{id}                 | GET    | Lookup a user with a specified unique identifier. |
| simple-user-service/api/users                      | POST   | Adds a new user to the database.                  |
| simple-user-service/api/users/{id}                 | PUT    | Updates an existing user in the database.         |
| simple-user-service/api/users/batch                | POST   | Adds all provided users within the database.      |
| simple-user-service/api/users/batch                | DELETE | Deletes all users from the database.              |

Examples also provided as SoapUI project. Please take a look into ./soapui folder.

# Build

```
mvn clean package
```

**Note:** Consider the Data Source name if necessary: "jdbc/__default" (see persistence.xml)

# Usage

1. Start Java EE runtime environment

```
docker run -p 4848:4848 -p 8080:8080 bimalo/payara-full-oracle:latest
```

2. Deploy the war file into the runtime environment

3. Open SoapUI and try the provided example requests.
