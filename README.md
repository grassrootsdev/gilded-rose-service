# gilded-rose-service
Sample service to review and buy items from The Gilded Rose

Disclaimer 1: This is my first attempt at implementing service-level security. At Expedia, all security is done at the net scaler/load balancer level, or through a proxy like Kong/Apigee. Keeping that in mind, I decided to use a framework I have much less experience with, in order to play around with security options beyond simple tokens. I know the assignment discouraged using new frameworks, but I've been wanting to learn more about Spring MVC and Spring Security, and decided this would be a good opportunity. Without the security requirement, I would have used basic jax-rs, which i work with daily.

Disclaimer 2: Because of my lack of experience with API security, I borrowed heavily from several sources, which are listed below.

Being a very simple service, I decided to use RESTful URL requests, and simple json for responses.

Sample Request to Login:
GET serverName:8090/login

Sample Request to Buy item "FirstItem":
POST serverName:8090/items/FirstItem

Sample Success Response:
{
  "success": true,
  "txnId": "eccb5b73-1dd7-4eba-8885-6fa72f4c7b60",
  "message": "Thank you for purchasing FirstItem"
}

Sample Unauthorized Response:
{
  "timestamp": 1490565566314,
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/items/FirstItem"
}

Regarding authentication, I settled on Spring Security. I havne't used it before, but since I'm familiar with Spring, it didn't take too long to figure out. 

Sources:
https://spring.io/guides/gs/securing-web/#scratch
Spring In Action - Fourth Edition