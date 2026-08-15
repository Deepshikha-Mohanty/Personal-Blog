# 📝 Personal Blog

A simple, single-user personal blogging app. Log in with credentials stored
in `application.properties`, then create, browse, search, edit, and delete
your own blog posts through a clean, responsive UI.

- **Backend:** Spring Boot (Java 21), Spring MVC, Spring Data JPA, Spring
  Security, MySQL
- **Frontend:** Server-rendered Thymeleaf templates + plain HTML/CSS/JS
  (no frontend framework, no build step)

---

## Features

- 🔐 Single-user login (credentials come from `application.properties`, not a
  database table)
- 🏠 Home feed of all your blog posts, newest first
- 🔍 Search by title or genre
- ✍️ Create new posts
- ✏️ Edit existing posts
- 🗑️ Delete posts (with a confirmation dialog)
- 📖 A dedicated reading page per post
- 🌗 Light/dark theme toggle (remembered across visits)
- 📱 Responsive layout with a mobile "new post" shortcut button
- 🌐 A small JSON REST API under `/api/**` for programmatic access

---

## Tech stack

| Layer      | Technology                                              |
|------------|----------------------------------------------------------|
| Language   | Java 21                                                   |
| Framework  | Spring Boot 3.5, Spring MVC, Spring Data JPA, Spring Security |
| Database   | MySQL 8                                                   |
| Templates  | Thymeleaf                                                 |
| Frontend   | Vanilla HTML, CSS, JavaScript (no npm/build step)         |
| Build tool | Maven (wrapper included — `mvnw` / `mvnw.cmd`)            |

---

## Project structure

```
personalblog/
├── pom.xml
├── mvnw, mvnw.cmd                     # Maven wrapper — no local Maven install needed
└── src/
    ├── main/
    │   ├── java/projects/personalblog/
    │   │   ├── PersonalblogApplication.java   # Spring Boot entry point
    │   │   ├── SecurityConfig.java            # Login, logout, and access rules
    │   │   ├── controller/
    │   │   │   ├── WebController.java         # Serves the HTML pages (Model → Thymeleaf)
    │   │   │   └── Blogcontroller.java        # JSON REST API (/api/**)
    │   │   ├── model/Blog.java                # JPA entity
    │   │   ├── repositories/blogrepository.java
    │   │   └── services/blogservice.java
    │   └── resources/
    │       ├── application.properties         # DB connection + login credentials
    │       ├── templates/                     # Thymeleaf pages
    │       │   ├── login.html
    │       │   ├── home.html
    │       │   ├── add_blog.html
    │       │   ├── edit_blog.html
    │       │   └── blog.html
    │       └── static/
    │           ├── css/style.css              # Shared design system
    │           └── js/app.js                  # Theme toggle, confirm modal, etc.
    └── test/java/projects/personalblog/       # Basic Spring Boot test
```

---

## Prerequisites

- **Java 21** (`java -version` to check)
- **MySQL 8** running locally (or reachable), with a database created for
  this app
- No need to install Maven separately — use the included wrapper (`./mvnw`
  on macOS/Linux, `mvnw.cmd` on Windows)

---

## Setup

### 1. Create the database

```sql
CREATE DATABASE blogdb;
```

The app uses `spring.jpa.hibernate.ddl-auto=update`, so the `blog` table is
created automatically on first run — no manual schema/migration needed.

### 2. Configure `src/main/resources/application.properties`

```properties
spring.application.name=personalblog

# Database connection
spring.datasource.url=jdbc:mysql://localhost:3306/blogdb
spring.datasource.username=root
spring.datasource.password=root123

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# The one and only login account for this blog
spring.security.user.name=admin
spring.security.user.password=admin123
spring.security.user.roles=USER
```

Update the datasource URL/username/password to match your local MySQL
setup, and change the login username/password to whatever you'd like to
use.

> ⚠️ **Security note:** these values are in plain text. That's fine for
> local/personal use, but if you ever deploy this publicly, move the
> password out to an environment variable
> (`spring.datasource.password=${DB_PASSWORD}`) instead of committing it.

### 3. Run the app

```bash
./mvnw spring-boot:run
```

(Windows: `mvnw.cmd spring-boot:run`)

The app starts on **http://localhost:8080**.

### 4. Log in

Go to `http://localhost:8080`, which redirects to the login page. Use the
username/password you set in `application.properties` (defaults: `admin` /
`admin123`). On success you land on the home page with your posts.

---

## Pages / routes

| Method | Path              | Description                          |
|--------|-------------------|---------------------------------------|
| GET    | `/login`          | Login page                            |
| POST   | `/login`          | Handled by Spring Security            |
| POST   | `/logout`         | Logs out, redirects to login          |
| GET    | `/home`           | List all posts, or `?keyword=` search |
| GET    | `/add-blog`       | Show "new post" form                  |
| POST   | `/blogs`          | Create a post                         |
| GET    | `/blog/{id}`      | View a single post                    |
| GET    | `/edit-blog/{id}` | Show "edit post" form                 |
| POST   | `/update-blog`    | Save edits to a post                  |
| POST   | `/delete-blog/{id}` | Delete a post                       |

## REST API

A small JSON API is available under `/api` (open, no login required — meant
for programmatic/AJAX use):

| Method | Path                        | Description                     |
|--------|-----------------------------|-----------------------------------|
| GET    | `/api/`                     | All blogs (JSON)                |
| GET    | `/api/blog/id/{id}`         | Single blog by ID               |
| GET    | `/api/blog/title/{title}`   | Single blog by exact title      |
| GET    | `/api/blog/genre/{genre}`   | Blogs filtered by genre         |
| GET    | `/api/blog/search/{keyword}`| Blogs matching title or genre   |
| POST   | `/api/blog/add`             | Create a blog (JSON body)       |
| PUT    | `/api/blog/update/{id}`     | Update a blog (JSON body)       |
| DELETE | `/api/blog/remove/{id}`     | Delete a blog                   |

---

## Notes on the frontend

The UI is intentionally dependency-free — no React/Vue/build tooling. Every
page is a Thymeleaf template rendered server-side, styled by one shared
`style.css` and enhanced by one shared `app.js` for:

- theme (light/dark) toggling, persisted via `localStorage`
- a custom delete-confirmation modal (instead of the browser's plain
  `confirm()`)
- live character counters on the title/content fields
- automatic color-coding of genre tags
- a reading-progress bar on the post detail page

Because everything is server-rendered, the app works fully without
JavaScript — the JS only adds polish on top.

---

## Troubleshooting

- **Login page loads but has no styling** — make sure `/css/**` and
  `/js/**` are listed as `permitAll()` in `SecurityConfig.java` (they are,
  by default, in this project) so the stylesheet can load before you're
  authenticated.
- **`Communications link failure` / can't connect to DB** — confirm MySQL is
  running and the `spring.datasource.url` port/database name match your
  local setup.
- **Login always fails** — double-check `spring.security.user.name` and
  `spring.security.user.password` in `application.properties`; there's no
  database-backed user table, this is the only account.
