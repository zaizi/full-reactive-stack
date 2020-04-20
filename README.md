# Full Reactive Stack [![Build Status](https://travis-ci.org/mechero/full-reactive-stack.svg?branch=master)](https://travis-ci.org/mechero/full-reactive-stack)

This repository contains backend and frontend projects that make use of **Reactive Web** patterns,
as explained in the [Full Reactive Stack](https://thepracticaldeveloper.com/full-reactive-stack)
series of posts.

![Full Reactive Stack Overview](resources/reactive_overview.png)

## Components

### Spring Boot Reactive Web

This is a Spring Boot 2.0 application that retrieves data using **Spring Reactive Web (WebFlux)**,
instead of using the standard synchronous MVC framework. It connects to a MongoDB database in a reactive
way too.

### Angular Reactive

This simple Angular JS application consumes the controller on the backend side using a reactive approach,
 Server-Sent Events, so data is loaded on screen as soon as it's available.

### Docker

The docker folder contains a `docker-compose` file that runs the Mongo database, the backend application
and the AngularJS application. It also contains a simplified version, `docker-compose-mongo-only.yml`, which
runs only the MongoDB instance, in case you want to run the applications without docker.

### How to run the project

### MongoDB

* Go to `docker` folder

* Run `docker-compose up`

### Spring-boot app

* Go to `spring-boot-reactive-web` folder

* Run `./mvnw spring-boot:run`

### Angular app

* Go to `angular-reactive` folder

* Run `npm install`

* Run `npm run ng serve` then go to `http://localhost:4200/`
