# 🚀 PagePulse

A modern website auditing tool built with **Spring Boot**, **React**, and **Jsoup**. PagePulse analyzes any publicly accessible website and provides useful insights such as HTTP status, response time, page title, meta description, heading count, image accessibility, and word count.

---

## 📌 Features

* 🌐 Analyze any valid HTTP/HTTPS website
* ⚡ Measure page response time
* 📡 Display HTTP status code
* 📄 Extract page title
* 📝 Extract meta description
* 🔠 Count H1 headings
* 🖼️ Count images missing `alt` attributes
* 📚 Calculate total word count
* ❌ Graceful error handling for invalid URLs and connection failures
* 📱 Responsive React + Tailwind CSS interface
---

## 🛠️ Tech Stack

### Backend

* Java 21
* Spring Boot
* Maven
* Jsoup
* Lombok

### Frontend

* React
* Vite
* Tailwind CSS
* Axios

### Version Control

* Git
* GitHub

### Deployment

* Backend: Render
* Frontend: Vercel

---

## ⚙️ Backend Setup

Clone the repository:

```bash
git clone <YOUR_GITHUB_REPOSITORY>
```

Navigate to the backend:

```bash
cd pagepulse
```

Run the application:

```bash
./mvnw spring-boot:run
```

The backend will start on:

```text
http://localhost:8080
```

---

## 💻 Frontend Setup

Navigate to the frontend:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Create a `.env` file:

```env
VITE_API_URL=http://localhost:8080/api
```

Start the development server:

```bash
npm run dev
```

The frontend will be available at:

```text
http://localhost:5173
```

---

## 📡 API Endpoint

### POST `/api/audit`

#### Request

```json
{
  "url": "https://openai.com"
}
```

#### Response

```json
{
  "status": 200,
  "responseTime": 142,
  "title": "OpenAI",
  "metaDescription": "Creating safe AGI...",
  "h1Count": 1,
  "missingAltImages": 0,
  "wordCount": 1587
}
```
Design Decisions
1. Layered Architecture

The backend is divided into Controller, Service, Analyzer, and Fetcher layers instead of placing all logic inside the controller.

Reason

This keeps responsibilities separated, making the application easier to understand, test, and maintain. The controller only handles HTTP requests, while the service coordinates the workflow and specialized components perform fetching and HTML analysis.

2. Separate Web Fetching from HTML Analysis

The application uses two dedicated components:

WebFetcher – retrieves the webpage and measures response time.
HtmlAnalyzer – extracts information from the HTML document.
Reason

Fetching a webpage and analyzing its contents are independent concerns. Separating them makes each component reusable and allows future enhancements (such as SEO scoring or broken-link analysis) without changing the networking logic.

3. DTO-Based API Communication

The application uses dedicated DTOs (AuditRequest, AuditResponse, AnalysisDto, and FetchResult) rather than exposing internal classes directly.

Reason

DTOs provide a stable contract between the frontend and backend, prevent exposing implementation details, and make it easier to evolve the backend without breaking the client application.

---

## 📷 Screenshots

### Home Page

> Add a screenshot here

```
frontend/assets/home.png
```
<img width="725" height="446" alt="image" src="https://github.com/user-attachments/assets/2748f08b-671b-4807-b76b-75916313700d" />

---

### Analysis Result


```
frontend/assets/result.png
```

---

## 🌍 Live Demo

Frontend(vercel)

```
https://pagepulse-digital-heores.vercel.app/
```

Backend(render)

```
https://pagepulse-digital-heores.onrender.com
```

---

## 🧪 Test Cases

* ✅ Valid Website
* ✅ Invalid URL
* ✅ Website Not Found (404)
* ✅ Non-HTML Response
* ✅ Connection Timeout
* ✅ Empty Input Validation

---

## 🎯 Design Principles

This project follows several software engineering best practices:

* Layered Architecture
* Single Responsibility Principle (SRP)
* Separation of Concerns
* DTO Pattern
* Dependency Injection
* Global Exception Handling
* Reusable React Components


## 👨‍💻 Author

**Rahul Goswami**

B.Tech Information Technology Student

GitHub: https://github.com/rahulcod-3333

---

## 🙏 Acknowledgements

This project was developed as part of the **Digital Heroes Software Engineering Training Task**.

Special thanks to the open-source communities behind:

* Spring Boot
* React
* Tailwind CSS
* Jsoup
* Vite
* Lombok
