# Fitness Keeper - Monitor fitness activities, make fitness a habit and stay motivated!

  

## Description

  

Fitness Keeper is a web application designed to help users track and monitor various activities that promote health and wellness.

  

## Features

  

Fitness Keeper provides the following features:

  

- BMI tracking

- Water intake (Currently backend only. Check swagger for docs)

- Food calorie consumption

- General activity monitoring

  

## System requirement for local devleopment environment

:bulb: Before you begin, make sure you have the following installed:

  

- Jdk version 11 or above

- Node.js version 16 or above

- Git

  
  

## Development setup

  

Fitness keeper is a single repository with UI and backend combined.

  

Follow these simple instructions to set up a local development environment.

  

1. Clone the repository and install dependencies:

  

```bash

git clone https://github.com/rukman7/HealthTrackerREST.git

```

  

2. Open the resulting folder and in an IDE like Injellij or Eclipse by importing as an existing maven project. Wait for maven to finish loading the dependency libraries.

  

3. Run `App.kt` and the Javalin server should start up and be running.

  

4. Use the URL to `http://localhost:8000` to access the application

  
  

## Demo GIFs

  

### Creating an user

  

An user profile needs to be created to use any features of the Fitness Keeper.

  

```

#link for user creation GIF here

```

### Using features

Activity tracking is one of the features of Fitness Keeper. The usage is simple as show in the GIFs

  

```

#link for activity usage GIF here

```

  
  

## Technology

  

Aspect | Description 
------- | --------------
Programming languages used |  Kotlin, Javascript, HTML, CSS, SQL
Backend | JDK 11
Frontend | Node.js 18, Vue.js, Bootstrap, HTML and CSS
Database | ElephantSQL cloud (Free tier)
CI/CD | Github workflows
Repository management | Github, Git
Hosting platform | Railway (Free tier)
IDE | Intellij Ultimate Edition, Visual studio code
Kotlin libraries | Javalin (server), Maven (dependency management), JDBC for database handling
Documentation | Swagger, reDoc - for APIs, Dokka for code documentation
SQL | pgAdmin4
System | Macbook pro M1, 2020
Testing | Backend: Junit, Unirest, H2, Talend API tester. Manual testing is done for Front end
CDN | Vue.js and Bootstrap
Linter | Sonartlint is used for Backend code analysis
Tested Browsers | Safari, Chrome, Brave

## Documentation Endpoints

```bash
#swagger
https://<host>:<port>/swagger-ui

#reDoc
https://<host>:<port>/redoc

#Doka
#Doka web style documentation is generated as an artifact for every build. 
#1. Select the repo
#2. Go to actions
#3. Select the last successful build
#4. Click on summary
#5. Download the zip file with name KDoc Documentation site'
#6. Unzip the file and open index.html file on a web browser. 
```

Sample Dokka documentation can be found here: [KDoc Documentation Site](https://github.com/rukman7/HealthTrackerREST/suites/10112016398/artifacts/493249309)