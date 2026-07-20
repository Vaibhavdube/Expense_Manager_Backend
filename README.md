<div align="center">

# 💰 Expense Manager Backend

### Secure • Scalable • Production Ready REST API

Built with **Java 21**, **Spring Boot**, **Spring Security**, **JWT**, **PostgreSQL**, and **Hibernate**

---

# 🌐 Live Demo

🚀 **Try the Application**

### 🎨 Frontend (Netlify)

👉 https://vaibhavdubey07.netlify.app/

### 🎨 Backend (render)

👉 https://expense-manager07.onrender.com/api/v2.0/health

---

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-green?style=for-the-badge&logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-Authentication-blueviolet?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql)
![Render](https://img.shields.io/badge/Render-Deployed-black?style=for-the-badge)

</div>

---

# 📖 About Project

Expense Manager Backend is a secure and scalable REST API that helps users manage their personal finances efficiently. It provides secure authentication, income and expense management, dashboard analytics, cloud image uploads, email verification, and Excel report generation while following modern backend development practices.

---

# ✨ Features

| Authentication | Financial Management | Productivity |
|---------------|----------------------|--------------|
| ✅ JWT Authentication | 💰 Income Management | 📊 Dashboard Analytics |
| ✅ Email Verification | 💸 Expense Management | 📄 Excel Export |
| ✅ BCrypt Password Encryption | 📂 Category Management | 📩 Email Reports |
| ✅ Spring Security | ☁ Cloudinary Upload | 🌐 REST APIs |

---

# 🖼 Screenshots

> **📌 Add screenshots inside the `/images` folder**

## Login API

![Login API](https://github.com/Vaibhavdube/Expense_Manager_Backend/blob/master/Screenshot%202026-07-20%20201618.png)

## Postman Testing

![postman](https://github.com/Vaibhavdube/Expense_Manager_Backend/blob/master/Screenshot%202026-07-20%20213446.png)

---

## Email Verification

![Email](https://github.com/Vaibhavdube/Expense_Manager_Backend/blob/master/Screenshot%202026-07-20%20202409.png)

---

## PostgreSQL Database

![Database](https://github.com/Vaibhavdube/Expense_Manager_Backend/blob/master/Screenshot%202026-07-20%20201425.png)

## Backend IntelliJ
![Backend](https://github.com/Vaibhavdube/Expense_Manager_Backend/blob/master/Screenshot%202026-07-20%20201739.png)

---

# 🛠 Tech Stack

### Backend

- ☕ Java 21
- 🍃 Spring Boot 3
- 🔐 Spring Security
- 🛡 JWT Authentication
- 🗄 Hibernate
- 📦 Spring Data JPA

### Database

- 🐘 PostgreSQL

### Cloud Services

- ☁ Cloudinary
- 📧 Brevo Email API

### Reporting

- 📄 Apache POI

### Build Tool

- 📦 Maven

---

# 🏗 System Architecture

```text
                React Frontend
                      │
                      ▼
               REST Controllers
                      │
                      ▼
              Spring Security
                      │
                      ▼
                 JWT Filter
                      │
                      ▼
               Service Layer
                      │
                      ▼
             Repository Layer
                      │
                      ▼
                PostgreSQL DB
```

---

# 🔐 Authentication Flow

```text
Register User
      │
      ▼
Generate Verification Token
      │
      ▼
Send Email via Brevo
      │
      ▼
Verify Email
      │
      ▼
Login
      │
      ▼
Generate JWT Token
      │
      ▼
Access Protected APIs
```

---

# 📦 REST APIs

| Method | Endpoint | Description |
|:------:|----------|-------------|
| POST | `/register` | Register User |
| GET | `/activate` | Verify Email |
| POST | `/login` | User Login |
| GET | `/profile` | User Profile |
| GET | `/dashboard` | Dashboard Analytics |
| GET | `/categories` | Get Categories |
| POST | `/categories` | Add Category |
| GET | `/incomes` | Get Income |
| POST | `/incomes` | Add Income |
| GET | `/expenses` | Get Expenses |
| POST | `/expenses` | Add Expense |
| GET | `/excel/download/income` | Download Income Report |
| GET | `/excel/download/expense` | Download Expense Report |

---

# 🚀 Challenges Solved

✔ Stateless Authentication using JWT

✔ Secure Password Encryption with BCrypt

✔ Email Verification using Brevo API

✔ Cloudinary Image Upload

✔ Dashboard Analytics

✔ Excel Report Generation

✔ Email Attachment Support

✔ Production Deployment on Render

✔ REST API Design

✔ Secure Endpoint Protection

---

# 📈 Future Improvements

- 🤖 AI Financial Advisor
- 📸 OCR Receipt Scanner
- 🔔 Smart Expense Reminder
- 📊 Expense Prediction
- 💹 Budget Planner
- 📱 Mobile Application

---

# ☁ Deployment

| Service | Platform |
|----------|----------|
| Backend | Render |
| Database | PostgreSQL |
| Images | Cloudinary |
| Email | Brevo |

---

# 👨‍💻 Developer

### Vaibhav Dubey

Java Full Stack Developer

📧 **Email**

Vaibhav07154@gmail.com

💼 **LinkedIn**

https://www.linkedin.com/in/vaibhav-dubey-8362a2408

🐙 **GitHub**

https://github.com/Vaibhavdube

---

<div align="center">

### ⭐ If you found this project useful, consider giving it a Star ⭐

</div>
