# Getting Started

This is a code challenge project for AIPS group in Seek.

## Description
This project is trying to parse a machine generated file which contains times and the number of cars passed in half-hourly manner.

## Running the project
First create a sample data file. Sample data should be look like the following:
```
2016-12-01T06:00:00 14 
2016-12-01T06:30:00 15 
2016-12-01T07:00:00 25 
2016-12-01T07:30:00 46 
```
Then put the file in a folder and set the path in the application.properties file.

## Build the project
This java application is written with java8 and should be build by maven. So build it using:
```
mvn clean package
```

Simply run the program by:
```
mvn spring-boot:run
```

## Notes
* The result is printed in the console, because it was not clear in the code challenge documentation where should I return them.
* The 3 contiguous half hours could be contiguous by time, or by order in the file, as the documentation mentioned that 
this file is generated by machine, so I assumed that all the data is available, so I ignored checking the contiguous time periods. 
