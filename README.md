# CSC4365 - Lab 7
Database Connectivity with JDBC

A README file which includes (a) the list of all members of the team, (b) any compilation/runtime instructions, including the names of environment variables used to pass
JDBC URL, username and password, and (c) information about any known bugs and/or
deficiencies.

## Members
* Richa Gadgil
* Makena Kong
* Whitney Larsen

## Database Used
mkong02

## Instructions
### Compile
1. export CLASSPATH=$CLASSPATH:mysql-connector-java-8.0.16.jar:.
2. javac InnReservations.java
### Run
1. java InnReservations

### JDBC Credentials
driver: com.mysql.cj.jdbc.Driver
url: jdbc:mysql://db.labthreesixfive.com/mkong02?autoReconnect=true&useSSL=false
username: mkong02
password: S19_CSC-365-012538483
database: mkong02
