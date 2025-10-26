
# 🧮 Offline Exam Creator and Doer with Device

A **LAN-based offline examination system** built with **Java Swing**, designed for environments without reliable internet.  
It enables **admins**, **teachers**, and **students** to securely manage, create, and take exams within a local network.

---

## 🧭 Overview

| Role | Responsibilities |
|------|-------------------|
| 👑 **Admin** | Creates users, manages courses, assigns teachers |
| 👩‍🏫 **Teacher** | Creates exams, adds questions, approves student enrollment |
| 👨‍🎓 **Student** | Views available courses, enrolls, and attempts exams |

💾 Works fully **offline** (LAN-based) but supports **Oracle DB sync** when connected.


## 🧱 Project Structure

```

offline-exam-system/
│
├── src/
│   └── com/offlineexam/
│       ├── model/
│       │   ├── User.java
│       │   ├── Course.java
│       │   ├── Exam.java
│       │   ├── Question.java
│       │   ├── Option.java
│       │   ├── Result.java
│       │   └── EnrollmentRequest.java
│       │
│       ├── dao/
│       │   └── DBConnection.java
│       │
│       ├── service/
│       │   ├── AuthService.java
│       │   ├── CourseService.java
│       │   ├── ExamService.java
│       │   ├── RequestService.java
│       │   └── ResultService.java
│       │
│       ├── view/
│       │   ├── LoginFrame.java
│       │   ├── SignupFrame.java
│       │   ├── AdminDashboard.java
│       │   ├── TeacherDashboard.java
│       │   ├── StudentDashboard.java
│       │   ├── ExamCreatorFrame.java
│       │   └── ExamAttemptFrame.java
│       │
│       ├── controller/
│       │   ├── LoginController.java
│       │   ├── AdminController.java
│       │   ├── TeacherController.java
│       │   └── StudentController.java
│       │
│       └── Main.java
│
└── resources/
└── application.properties

```

---

## 🎨 Design Overview

### 🧩 1. **MVC Architecture**
```

┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│   Model      │◄────►│  Controller  │◄────►│    View       │
│ (Data layer) │       │ (Logic flow) │       │ (UI Frames)  │
└──────────────┘       └──────────────┘       └──────────────┘

````

- **Model:** Defines entities (`User`, `Course`, `Exam`, etc.)
- **Controller:** Handles logic and data flow
- **View:** Swing-based GUI for interaction

---

### 🔁 2. **System Workflow**

```mermaid
flowchart TD
    A[Login Screen] -->|Admin| B[Admin Dashboard]
    A -->|Teacher| C[Teacher Dashboard]
    A -->|Student| D[Student Dashboard]
    
    B --> E[Manage Users & Courses]
    C --> F[Create Exam / Approve Requests]
    D --> G[Enroll & Take Exam]
    G --> H[Submit Exam & View Result]
````

### 🖥️ 4. **UI Preview (Wireframe Sketch)**

#### **Login Page**

```
+--------------------------------------+
| 🔒 Offline Exam System               |
|--------------------------------------|
| Username: [______________]           |
| Password: [______________]           |
| Role: [Admin ▼]                     |
| [ Login ]  [ Signup ]                |
+--------------------------------------+
```

#### **Teacher Dashboard**

```
+------------------------------------------------------+
| [Home] [Create Exam] [Approve Students] [Logout]     |
|------------------------------------------------------|
| 📝 My Courses:                                       |
|  - Object-Oriented Programming                       |
|  - Database Systems                                  |
|------------------------------------------------------|
| Actions: [Create New Exam] [View Submissions]        |
+------------------------------------------------------+
```

#### **Student Exam Attempt**

```
+------------------------------------------------------+
| Exam: Database Systems - Final                       |
|------------------------------------------------------|
| Q1: What does SQL stand for?                         |
| ( ) Simple Query Language                            |
| ( ) Structured Query Language ✅                     |
| ( ) Sequential Query Logic                           |
|------------------------------------------------------|
| [Next ➜]                           [Submit ✅]      |
+------------------------------------------------------+
```

---

4. **Run**

   ```bash
   Main.java
   ```

5. **Default Admin Login**

   ```
   Username: admin
   Password: admin
   ```

---


## 🔮 Future Enhancements

* ☁️ Cloud synchronization (Oracle / Firebase)
* 📱 Mobile app companion
* 📊 Data analytics dashboard
* 🔐 Stronger encryption and session management
* 🧾 Export reports to PDF / Excel

---

