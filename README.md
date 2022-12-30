# Fitness Keeper - Monitor Fitness Activities, Make Fitness a Habit and Stay Motivated!

  

## Description

  

Fitness Keeper is a web application designed to help users track and monitor various activities that promote health and wellness.


## Table of contents
1. [Features](#Features)
2. [System requirement for local development environment](#System requirement for local development environment)
3. [Development setup](#Development setup)
4. [Demo GIFs](#Demo GIFs)
	- [Creating a user](#Creating a user)
	- [Using features](#Using features)
5. [Technology](#Technology)
6. [Documentation Endpoints](#Technology)
  

## Features

  

Fitness Keeper provides the following features:

  

- BMI tracking

- Water intake (Currently backend only. Check swagger for docs)

- Food calorie consumption

- General activity monitoring

  

## System requirement for local development environment

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

  

### Creating a user

  

A user profile needs to be created to use any features of the Fitness Keeper.


<h1 align="center" style="display: block; font-size: 2.5em; font-weight: bold; margin-block-start: 1em; margin-block-end: 1em;">
<a name="logo"><img align="center" src=https://github.com/rukman7/HealthTrackerREST/blob/master/assets/fitness_tracker_user_create.gif alt="AREG SDK Home" style="width:100%;height:100%"/></a>
  <br /><br /><strong>User creation</strong>
</h1>

### Using features

Activity tracking is one of the features of Fitness Keeper. The usage is simple as show in the GIFs


<h1 align="center" style="display: block; font-size: 2.5em; font-weight: bold; margin-block-start: 1em; margin-block-end: 1em;">
<a name="logo"><img align="center" src="https://github.com/rukman7/HealthTrackerREST/blob/master/assets/fitness_tracker_activity_create.gif" style="width:80%;height:80%"/></a>
  <br /><br /><strong>Activity tracking</strong>
</h1>
  

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