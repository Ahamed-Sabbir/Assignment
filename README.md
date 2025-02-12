🛠 API Documentation
🔹 1. Login (Generate JWT)
Endpoint:
POST /auth/login

Request Body (JSON):

json
Copy
Edit
{
  "username": "sabbir",
  "password": "12345"
}
Response Example:

json
Copy
Edit
{
  "token": "your_jwt_token_here",
  "username": "sabbir",
  "Role": ["ROLE_ADMIN"]
}
💡 Use this JWT token in Authorization → Bearer Token for other API calls.

🔹 2. Register Admin (Only Supreme Admin)
Endpoint:
POST /auth/register/admin

🔐 Requires: ROLE_SUPREME_ADMIN

Request Body (JSON):

json
Copy
Edit
{
  "firstName": "Samia",
  "lastName": "Haque",
  "username": "samia",
  "password": "12345",
  "isActive": false
}
🔹 3. Register User (Both Supreme Admin & Admin)
Endpoint:
POST /auth/register/user

🔐 Requires: ROLE_SUPREME_ADMIN or ROLE_ADMIN

Request Body (JSON):

json
Copy
Edit
{
  "firstName": "YY",
  "lastName": "Ahamed",
  "username": "YY",
  "password": "12345",
  "isActive": false
}
🔹 4. Get Paginated User List
Endpoint:
GET /list?page=1&size=5

🔐 Requires: Authenticated user

Query Parameters:

page → Page number (starting from 0)
size → Number of users per page
🔹 5. Get Logged-in User Info
Endpoint:
GET /myInfo

🔐 Requires: Authenticated user

🔹 6. Search User by Username
Endpoint:
GET /user?username=YY

🔐 Requires: Authenticated user

Query Parameter:

username → The username of the user to search for.
🚀 Usage in Postman
Import Postman Collection:
Open Postman → Click Import → Select Assignment - AWT.postman_collection.json
Run the APIs in Sequence:
Login → Copy JWT → Use it for authentication in other APIs.
🛠 Technologies Used
Java 17
Spring Boot
Spring Security & JWT
H2 Database
Maven
Postman for API testing
