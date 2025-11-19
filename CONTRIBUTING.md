
# **CONTRIBUTING.md**

### *Developer Contribution Guide — Offline Exam Creator & Doer (Java Swing MVC)*

> A structured engineering guideline crafted for professional Java developers contributing to this project.

---

---

# 🚀 **1. Project Architecture Overview**

This project follows a clean **modular MVC architecture** with additional layers for clarity:

```
offline-exam-system/
│
└── src/main/java/com/offlineexam/
    ├── model/        → Data structures (POJOs)
    ├── dao/          → Data logic (in-memory / DB-ready)
    ├── service/      → Business logic
    ├── controller/   → Event handlers
    └── view/         → Swing UI components
```

### 🧠 **High-level Interaction Flow**

```
    [View] → User Input
       ↓
  [Controller] → Handles events
       ↓
  [Service] → Business rules
       ↓
    [DAO] → Data storage (memory / DB)
       ↓
   [Model] → Updated data
       ↓
 [View] → UI refreshed
```

---

---

# 🎨 **2. UI/UX Engineering Standards**

The project uses a **light professional UI theme** suitable for exam systems.

### 🟦 **UI Colors**

| Component         | Color     |
| ----------------- | --------- |
| Background Panels | `#F5F6FA` |
| Primary Buttons   | `#2F80ED` |
| Text              | `#222D3A` |
| Borders           | `#D9DDE3` |

### 🧩 **Swing Component Rules**

* Use **JPanel + BorderLayout/CardLayout** for dashboards.
* Keep **Login/Signup** centered with margin spacing:
  `setBorder(new EmptyBorder(20, 20, 20, 20));`
* Prefer **JTable** for data lists (teachers, courses, requests).
* Icons should be 18–24 px.
* For dialogs:
  `JOptionPane.showMessageDialog(parent, message, title, type);`

### 📐 **Layout Patterns**

Use these consistent layouts:

#### **Dashboard Layout**

```
+---------------------------------------------------------+
| Header: Title + User Role                               |
+----------------------+----------------------------------+
| Navigation Sidebar  |  Content Panel (Card Layout)      |
| (buttons: Home,     |  Loads dynamic screens:           |
|  Exams, Courses…)   |  - Manage Courses                 |
|                     |  - Create Exam                    |
|                     |  - Enrollment Requests            |
+----------------------+----------------------------------+
```

---

---

# 🔧 **3. Coding Standards**

### **Java Syntax Rules**

* Indentation: **4 spaces**
* No wildcard imports (`import java.util.*` ❌)
* Public class per file
* Methods max size: **40 lines**
* Avoid business logic inside UI classes

### **Naming Conventions**

| Type      | Format           | Example               |
| --------- | ---------------- | --------------------- |
| Class     | PascalCase       | `AdminDashboard`      |
| Variables | camelCase        | `studentListModel`    |
| Constants | UPPER_SNAKE_CASE | `MAX_QUESTIONS`       |
| Packages  | lowercase        | `com.offlineexam.dao` |

---

---

# 🧪 **4. Testing Guidelines**

### **UI Testing Checklist**

* [ ] Login works for admin/teacher/student
* [ ] Signup validates empty inputs
* [ ] Buttons trigger correct controller
* [ ] Tables refresh correctly
* [ ] Enrollment workflow works end-to-end

### **Functional Testing Cheat Table**

| Feature              | Test Case              | Expected Behavior           |
| -------------------- | ---------------------- | --------------------------- |
| Admin adds teacher   | Fill info → Save       | Teacher appears under list  |
| Admin assigns course | Select course → Assign | "AssignedTeacherID" updated |
| Teacher creates exam | Add → Save             | Exam appears in exam list   |
| Student enrolls      | Click enroll           | Request appears as PENDING  |
| Teacher approves     | Click Approve          | Request becomes APPROVED    |

---

---

# 🌳 **5. Branching Strategy**

We use a simplified Git workflow:

```
main (stable releases)
│
├── dev (integration)
│
├── feature/<feature-name>
│       Example: feature/exam-creation-ui
```

### **Pull Request Requirements**

* Title must follow **conventional commit syntax**:

  * `feat: added exam creation form`
  * `fix: corrected enrollment validation`
  * `refactor: cleaner DAO architecture`

---

---

# 🚀 **6. Performance & Code Quality Rules**

### ✔️ Allowed

* Reusing Swing components
* Using CardLayout for dynamic UI
* Keeping logic in Service layer

### ❌ Not Allowed

* Hardcoding large datasets
* Mixing UI + business logic
* Long methods that do multiple things
* Creating unnecessary threads

---

---

# 🔒 **7. Security Guidelines**

* Never log passwords to console.
* Validate user input before saving.
* Keep role-based restrictions strict:

  * Admin → Full access
  * Teacher → Courses they handle
  * Student → Only their own data

---

---

# 🤝 **8. Steps to Contribute**

### **Step 1: Fork the repository**

```
https://github.com/<yourname>/offline-exam-system
```

### **Step 2: Clone your fork**

```
git clone https://github.com/<yourname>/offline-exam-system
```

### **Step 3: Create a feature branch**

```
git checkout -b feature/add-question-editor
```

### **Step 4: Commit with proper messages**

```
feat: added MCQ question editor for teacher dashboard
```

### **Step 5: Push changes**

```
git push origin feature/add-question-editor
```

### **Step 6: Open Pull Request**

Explain:

* What changed
* Why it changed
* How to test it

---

---

# 📄 **9. License**

By contributing, you agree that your code will be part of this project's open-source license.

---

# 🙌 **10. Thank You**

