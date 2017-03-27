# gilded-rose-service
Sample service to review and buy items from The Gilded Rose
=============================
Disclaimer 1: This is my first attempt at implementing service-level security. At Expedia, all security is done at the net scaler/load balancer level, or through a proxy like Kong/Apigee. Keeping that in mind, I decided to use a framework I have much less experience with, in order to play around with security options beyond simple tokens. I know the assignment discouraged using new frameworks, but I've been wanting to learn more about Spring MVC and Spring Security, and decided this would be a good opportunity. Without the security requirement, I would have used basic jax-rs, which i work with daily.

Disclaimer 2: Because of my lack of experience with API security and UI, I borrowed heavily from several sources, which are listed below.

Disclaimer 3: I spent well over the 2-3 recommended hours... probably closer to 8 hours over two days. The additional time was mostly related to security, but also playing around with Spring MVC. I'm actually building a similar simple service for a friend in April, so used this opportunity to investigate some technologies I have been considering to use in that solution.

Service Documentation
--------------------
Run:
./gradlew bootRun

Build:
./gradlew build


Being a very simple service, I decided to use RESTful URL requests, and simple json for responses.

test username - user
test password - password

###Sample Request/Responses
Sample Request to Login:
GET serverName:8090/login

Sample Request to list Items
GET serverName:8090/items

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


###Authentication
I settled on Spring Security. I hadn't used it before, but since I'm familiar with Spring, it didn't take too long to figure out. Users can login via form at /login, or add user/password via Basic Auth in POST request to buy item. Credentials are only required for first request, after which user credentials are cached on server for 60 minutes. User can also wipe credentials by clicking on or calling /logout 

Credential store is currently in-memory, but could easly be replaced by secure db.

Credential are passed in via login form or via POST. The form is supposed to be secured by default via Spring Security. However, because I opened up login to be available via programatic request, they can be passed in via plaintext. We'd want to use https before deploying to prod.

###Design decisions
1) Fast fail. If spring is misconfigured, or injection fails for some reason, service should fail to start
2) Throw TransactionException 500 if unable to process user request, most likely due to downstream error. 
	Could send 200 with detailed error message, or 400 if item doesn't exist in catalog, but thought that might be overkill for this project.
3) I know very little about UI, so I essentially copied examples from two sources below. I was going to use just Swagger, but decided a simple UI would make the solution more robust. TODO: ask UI person to clean up, if this is customer facing
4) Used POST for buy() call, even though no body in request. Technically could be ok with GET, but it could alter the underlying data by adding txn to db, so decided on POST.

Quirks/Future Enhancements
---------------------------
1) The API works as expected, but the fact that i enabled Form login creates one interesting scenario: If user tries to buy an item without being logged in, the HTML login page is returned. I could change this by having a separate API login url, or having smarter login error handling, but both of those options would require a little more work.
2) The UI is incredibly basic. In particular, the /images url returns list of JSON objects. This works well for API, but is clunky for UI. I also return link for buy, trying to hateoas. However, this doesn't work well without a UI wrapper, since browsers assume you are trying to GET rather than the required POST.
3) Integration tests. Individual modules are unit tests, and since service is pretty starightforward, integration tests seemed like overkill. 

Sources
--------
https://spring.io/guides/gs/securing-web/#scratch
Spring In Action - Fourth Edition