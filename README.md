# spring-rest-service

SPRING BOOT REST SERVICE FOR JSON FILE MODIFICATION
---

[![Build Status](https://travis-ci.org/shafikul/spring-rest-service.svg?branch=master)](https://travis-ci.org/shafikul/spring-rest-service)


## HOW TO RUN 

At first clone this repository https://github.com/shafikul/spring-rest-service.git  

cd into spring-rest-service folder. Inside src directory find ***application.properties*** file and modify properties  

```doc
  app.fileDir={enter your directory name}    
  app.fileName={enter your json file name}  
  app.notification=on {Only if you want to get notification for employee delete}
```

In order to get notification in Linux/MacOS set GOOGLE_APPLICATION_CREDENTIALS in environment variable

```sh
  export GOOGLE_APPLICATION_CREDENTIALS="/home/user/Downloads/[FILE_NAME].json"
```

For windows follow approprite guide line to set the environment variable

Before running double check the environment variable set properly or not e.g.

 ```sh 
    echo $GOOGLE_APPLICATION_CREDENTIALS 
 ```

Then browse to root folder and run ***mvn clean package***  

spring-rest-service-0.0.1.jar file will be created in target folder  

Run the jar ***java -jar spring-rest-service-0.0.1.jar***  

Application will be started in ***http://localhost:8080***  


## API DOCUMENTATION

Go to https://github.com/shafikul/spring-rest-service/wiki/API-DOCUMENTATION
