# ip-management-service

IP management service is service that can be used to manage IP Address in the different pools. This service has capability 
to FREE/RESERVED/BLACKLIST IP's in the pool.
# Project structure
  * Backend - Spring boot framework used to build the backend service and for content deliver used inbuilt tomcat apache
   server which us running on default port 8080.
  * Database - Use h2 memory database used for this project

# How to use this?
  - Use has need to clone this repo at host machine and start tomcat server.

# Features
    This service has provide five operation. Below is the list of service and corresponding the definition.
    
|  HTTP METHOD | END POINT   |  DESCRIPTION |
|---|---|---|
|  POST | http://localhost:8080/ip-mgmt-service/generateAndReserveIPAddress  | This end point will reserve generate IP from given pool  |
|  PUT | http://localhost:8080/ip-mgmt-service/reserveIPAddress  | This endpoint will reserve IP address |
|  PUT | http://localhost:8080/ip-mgmt-service/blacklistIPAddress  | This endpoint will blacklist IP in pool if provided IP within range |
|  PUT | http://localhost:8080/ip-mgmt-service/freeIPAddress  | This end point will free IP from reservation if IP is not blacklisted |
|  POST | http://localhost:8080/ip-mgmt-service/getIPInfo  | This will return IP Address information |
|  GET | http://127.0.01:9001/actuator/health  | This will provide service heath status. |

# Swagger documentation: 
 User can open below link for more detail information about services and request parameters.
 http://localhost:8080/swagger-ui.htm
 ### Maintainer 
 Raj K Upadhyay

