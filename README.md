# Explore With Me  
## About  
This application is designed to host events by users that can take place anywhere in the world. And also for the opportunity to take part in them.  
The program can identify familiar places that have been entered into the database by the administrator.  
## Applications  
### main-service  
* Main application  
### stats  
* stats-dto - contains DTO for stats  
* stats-client - contains RestTemplate  
* stats-server - application for StatsServer
### Postman  
Package postman contains base test for geography feature  
  
## How to run the application using Docker  
Run *mvn clean package* to build the applications and create the docker image locally.  
Run *docker-compose up* to start the applications.  
##  Technology stack  
* SpringBoot
* Docker
* Hibernate
* PostgreSQL
* REST
* Apache Maven  

Pull Request
https://github.com/Anna-Vashchenkova/java-explore-with-me/pull/3
