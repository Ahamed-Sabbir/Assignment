Here is a **README.md** file based on Postman requests and project functionalities.  

---

## **Assignment - AWT**
---

## **🛠 Setup Instructions**
### **1️⃣ Clone the Repository**
```bash
git clone https://github.com/Ahamed-Sabbir/Assignment.git
```

### **2️⃣ Configure Database (H2)**
No extra setup is needed. The project uses an **H2 in-memory database**.

By default, the app runs on **`http://localhost:8080`**.

---

## **🛠 API Documentation**

### **🔹 1. Login (Generate JWT)**
**Endpoint:**  
`POST /auth/login`

**Request Body (JSON)**:
```json
{
  "username": "sabbir",
  "password": "12345"
}
```
**Response Example:**
```json
{
  "token": "your_jwt_token_here",
  "username": "sabbir",
  "Role": ["ROLE_ADMIN"]
}
```
💡 Use this **JWT token** in **Authorization → Bearer Token** for other API calls.

---

### **🔹 2. Register Admin (Only Supreme Admin)**
**Endpoint:**  
`POST /auth/register/admin`

🔐 **Requires:** `ROLE_SUPREME_ADMIN`

**Request Body (JSON)**:
```json
{
  "firstName": "Samia",
  "lastName": "Haque",
  "username": "samia",
  "password": "12345",
  "isActive": false
}
```

---

### **🔹 3. Register User (Both Supreme Admin & Admin)**
**Endpoint:**  
`POST /auth/register/user`

🔐 **Requires:** `ROLE_SUPREME_ADMIN` or `ROLE_ADMIN`

**Request Body (JSON)**:
```json
{
  "firstName": "YY",
  "lastName": "Ahamed",
  "username": "YY",
  "password": "12345",
  "isActive": false
}
```

---

### **🔹 4. Get Paginated User List**
**Endpoint:**  
`GET /list?page=1&size=5`

🔐 **Requires:** Authenticated user

**Query Parameters:**
- `page` → Page number (starting from 0)
- `size` → Number of users per page

---

### **🔹 5. Get Logged-in User Info**
**Endpoint:**  
`GET /myInfo`

🔐 **Requires:** Authenticated user

---

### **🔹 6. Search User by Username**
**Endpoint:**  
`GET /user?username=YY`

🔐 **Requires:** Authenticated user

**Query Parameter:**
- `username` → The username of the user to search for.

## **🛠 Technologies Used**
- **Java 17**
- **Spring Boot**
- **Spring Security & JWT**
- **H2 Database**
- **Gradle**
- **Postman for API testing**
