# EduMate - Complete Setup Manual

## 🎯 Project Overview

EduMate is a full-stack AI-powered learning management system with:

- **Backend**: Spring Boot with JWT authentication, MySQL + MongoDB
- **Frontend**: React with Vite, Tailwind CSS, protected routes
- **AI Integration**: Google Gemini API for content generation
- **Features**: ✅ User authentication, ✅ File upload, ✅ AI-generated summaries, ✅ AI MCQ generation, ✅ AI Flashcard generation, ✅ Project management, ✅ Interactive Quizzes, ✅ Flashcard study system, Reports (placeholder), Reminders (placeholder)

## 📋 Prerequisites Installation

### 1. Java 17+ Installation

**Windows:**

1. Download Java 17 from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
2. Install and add to PATH
3. Verify: Open Command Prompt and run `java -version`

**macOS:**

```bash
# Using Homebrew
brew install openjdk@17
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

**Linux (Ubuntu/Debian):**

```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

### 2. Maven 3.9+ Installation

**Windows:**

1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Add `C:\Program Files\Apache\maven\bin` to PATH
4. Verify: `mvn -version`

**macOS:**

```bash
brew install maven
```

**Linux:**

```bash
sudo apt install maven
```

### 3. Node.js 18+ Installation

**All Platforms:**

1. Download from [Node.js](https://nodejs.org/)
2. Install and verify: `node -v` and `npm -v`

### 4. MySQL 8.0+ Installation

**Windows:**

1. Download MySQL Installer from [MySQL](https://dev.mysql.com/downloads/installer/)
2. Install MySQL Server and MySQL Workbench
3. Set root password during installation

**macOS:**

```bash
brew install mysql
brew services start mysql
```

**Linux:**

```bash
sudo apt install mysql-server
sudo mysql_secure_installation
```

### 5. MongoDB 6.0+ Installation

**Windows:**

1. Download from [MongoDB Community Server](https://www.mongodb.com/try/download/community)
2. Install and start MongoDB service

**macOS:**

```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb/brew/mongodb-community
```

**Linux:**

```bash
# Import MongoDB public key
wget -qO - https://www.mongodb.org/static/pgp/server-6.0.asc | sudo apt-key add -
# Add MongoDB repository
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/6.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-6.0.list
# Install MongoDB
sudo apt update
sudo apt install mongodb-org
sudo systemctl start mongod
```

## 🗄️ Database Setup

### MySQL Database Setup

1. **Start MySQL Service:**

   - Windows: Start MySQL service from Services
   - macOS: `brew services start mysql`
   - Linux: `sudo systemctl start mysql`

2. **Create Database and User:**

```sql
-- Connect to MySQL as root
mysql -u root -p

-- Create database
CREATE DATABASE edumate_db;

-- Create user (optional - you can use root)
CREATE USER 'edumate_user'@'localhost' IDENTIFIED BY 'edumate_password';
GRANT ALL PRIVILEGES ON edumate_db.* TO 'edumate_user'@'localhost';
FLUSH PRIVILEGES;

-- Exit MySQL
EXIT;
```

### MongoDB Setup

1. **Start MongoDB Service:**

   - Windows: Start MongoDB service from Services
   - macOS: `brew services start mongodb-community`
   - Linux: `sudo systemctl start mongod`

2. **Verify MongoDB is running:**

```bash
# Connect to MongoDB (optional)
mongosh
# Type 'exit' to quit
```

## ⚙️ Configuration Changes

### Backend Configuration

**File:** `Backend/src/main/resources/application.yml`

**MySQL Configuration (Already Configured):**

The application is pre-configured to use the `edumate_user` account:

```yaml
datasource:
  url: jdbc:mysql://localhost:3306/edumate_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
  username: edumate_user
  password: edumate_password
```

**⚠️ If you used a different MySQL username/password, update these values in:**
`Backend/src/main/resources/application.yml`

**If you want to change ports or other settings:**

```yaml
server:
  port: 3001 # Change if port 3001 is occupied

cors:
  allowed-origins: http://localhost:5173 # Change if frontend runs on different port
```

### Frontend Configuration (Optional)

**File:** `Frontend/.env` (create this file if you want to override defaults)

```env
VITE_API_URL=http://localhost:3001/api
VITE_DEBUG=false
```

## 🚀 Running the Application

### Step 1: Start Backend

```bash
# Navigate to backend directory
cd "C:\Users\ankit\Desktop\New folder (2)\EduMate\Backend"

# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

**Expected Output:**

```
Started EduMateBackendApplication in X.XXX seconds (JVM running for X.XXX)
```

**Backend will be available at:** `http://localhost:3001`

### Step 2: Start Frontend (New Terminal)

```bash
# Navigate to frontend directory
cd "C:\Users\ankit\Desktop\New folder (2)\EduMate\Frontend"

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

**Expected Output:**

```
Local:   http://localhost:5173/
Network: http://192.168.x.x:5173/
```

**Frontend will be available at:** `http://localhost:5173`

## 🧪 Testing the Application

### 1. Access the Application

- Open browser and go to `http://localhost:5173`
- You should see the login page

### 2. Create an Account

- Click "Create one here" link
- Fill in the registration form:
  - First Name: `John`
  - Last Name: `Doe`
  - Email: `john@example.com`
  - Password: `password123`
- Click "Create Account"

### 3. Login

- Use the credentials you just created
- You should be redirected to the dashboard

### 4. Test Features

- **Dashboard**: Should show welcome message with your name
- **Profile**: Click "View Profile" to see your account details
- **Notes**: Upload a .txt file and test AI-generated summaries and MCQs
- **Projects**: Create and manage learning projects
- **Navigation**: Test all navigation links
- **Logout**: Click logout and verify you're redirected to login

### 5. Test AI Features

1. **Create a Project**: Go to Projects and create a new project
2. **Upload a File**: Go to Notes, click "Upload File", select a .txt file
3. **Generate Summary**: The AI will automatically generate a summary
4. **Generate MCQs**: Click "Generate MCQs" button on any note
5. **Generate Flashcards**: Click "Generate Flashcards" button on any note
6. **Take Quizzes**: Go to Quizzes page to take AI-generated quizzes
7. **Study Flashcards**: Go to Flashcards page to study with interactive flashcards

## ✅ Completed Features

### 🎯 Core Features (Fully Implemented)

- **✅ User Authentication**: Complete registration, login, JWT token management
- **✅ Project Management**: Create, edit, delete, and organize learning projects
- **✅ File Upload & Processing**: Upload .txt and .md files with automatic text extraction
- **✅ AI-Powered Summaries**: Gemini AI automatically generates summaries from uploaded content
- **✅ AI-Generated MCQs**: Create multiple choice questions from your notes
- **✅ AI-Generated Flashcards**: Generate study flashcards from your content
- **✅ Interactive Quizzes**: Take quizzes with progress tracking
- **✅ Flashcard Study System**: Interactive flashcard study with flip animations
- **✅ Responsive Design**: Mobile-friendly interface with Tailwind CSS
- **✅ Protected Routes**: Secure navigation with authentication checks
- **✅ RESTful APIs**: Complete backend API with proper error handling

### 🔧 Technical Features

- **✅ Spring Boot Backend**: Production-ready with proper configuration
- **✅ MySQL Integration**: Relational data storage for users, projects, notes
- **✅ MongoDB Integration**: Document storage for MCQs and AI-generated content
- **✅ JWT Security**: Secure authentication with token-based auth
- **✅ CORS Configuration**: Proper cross-origin resource sharing setup
- **✅ File Storage**: Local file storage with unique naming
- **✅ AI Integration**: Multiple Gemini API keys with load balancing
- **✅ React Frontend**: Modern React with Vite and hot reload
- **✅ State Management**: Proper state handling and API integration

### 🚧 Placeholder Features (Basic Implementation)

- **⚠️ Reports**: Basic page structure (analytics not implemented)
- **⚠️ Reminders**: Basic page structure (notification system not implemented)
- **⚠️ PDF/DOC Processing**: Placeholder text extraction (needs Apache PDFBox/POI)

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. Maven Command Not Found

**Error:** `'mvn' is not recognized as the name of a cmdlet`
**Solution:**

- Add Maven to your PATH environment variable
- Restart Command Prompt/PowerShell
- Verify with `mvn -version`

#### 2. MySQL Connection Failed

**Error:** `Access denied for user 'root'@'localhost'`
**Solutions:**

- Check if MySQL service is running
- Verify username/password in `application.yml`
- Try connecting manually: `mysql -u root -p`

#### 3. MongoDB Connection Failed

**Error:** `MongoDB connection failed`
**Solutions:**

- Check if MongoDB service is running
- Verify MongoDB is listening on port 27017
- Check MongoDB logs for errors

#### 4. Port Already in Use

**Error:** `Port 3001 is already in use`
**Solutions:**

- Change port in `application.yml`: `server.port: 3002`
- Kill process using the port: `netstat -ano | findstr :3001`

#### 5. Frontend Build Errors

**Error:** `npm install` fails
**Solutions:**

- Clear npm cache: `npm cache clean --force`
- Delete `node_modules` and `package-lock.json`
- Run `npm install` again

#### 6. CORS Errors

**Error:** `Access to fetch at 'http://localhost:3001' from origin 'http://localhost:5173' has been blocked by CORS policy`
**Solution:**

- Verify CORS configuration in `application.yml`
- Ensure frontend URL matches `cors.allowed-origins`
- Note: If frontend runs on port 5174 instead of 5173, this is automatically handled

#### 7. AI Service Configuration Error

**Error:** `Could not resolve placeholder 'ai.gemini.api-keys' in value "${ai.gemini.api-keys}"`
**Solution:**

- The AI service is pre-configured with hardcoded API key
- This error should not occur with current configuration
- If it does occur, verify `GeminiAIService.java` has the API key hardcoded

### Logs and Debugging

#### Backend Logs

- Check console output when running `mvn spring-boot:run`
- Look for "Started EduMateBackendApplication" message
- Check for any error messages in red

#### Frontend Logs

- Open browser Developer Tools (F12)
- Check Console tab for JavaScript errors
- Check Network tab for API call failures

#### Database Logs

**MySQL:**

```bash
# Check MySQL error log
# Windows: C:\ProgramData\MySQL\MySQL Server 8.0\Data\*.err
# macOS: /usr/local/var/mysql/*.err
# Linux: /var/log/mysql/error.log
```

**MongoDB:**

```bash
# Check MongoDB logs
# Windows: Check MongoDB service logs
# macOS: brew services list | grep mongodb
# Linux: sudo journalctl -u mongod
```

## 📚 API Endpoints

### Authentication Endpoints

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/verify` - Verify JWT token

### Protected Endpoints (Require Authentication)

- `GET /api/notes` - Get user's notes
- `POST /api/notes` - Create new note
- `GET /api/notes/{id}` - Get specific note
- `PUT /api/notes/{id}` - Update note
- `DELETE /api/notes/{id}` - Delete note
- `POST /api/notes/upload` - Upload file and generate AI summary
- `POST /api/notes/{id}/generate-mcqs` - Generate MCQs for a note
- `POST /api/notes/{id}/generate-flashcards` - Generate flashcards for a note
- `GET /api/projects` - Get user's projects
- `POST /api/projects` - Create new project
- `GET /api/projects/{id}` - Get specific project
- `PUT /api/projects/{id}` - Update project
- `DELETE /api/projects/{id}` - Delete project
- `GET /api/mcqs` - Get user's MCQs
- `GET /api/mcqs/note/{noteId}` - Get MCQs for a specific note
- `DELETE /api/mcqs/{id}` - Delete MCQ

## 🎉 Success Indicators

### Backend Success

✅ Maven compiles without errors  
✅ Spring Boot starts successfully  
✅ Database connections established  
✅ JWT authentication working  
✅ API endpoints responding

### Frontend Success

✅ npm install completes without errors  
✅ Development server starts on port 5173  
✅ Login/Register forms work  
✅ Protected routes redirect properly  
✅ API calls to backend succeed

### Full Application Success

✅ User can register new account  
✅ User can login with credentials  
✅ Dashboard displays user information  
✅ Navigation between pages works  
✅ Logout functionality works  
✅ No console errors in browser

## 🚨 Emergency Fixes

### If Backend Won't Start

1. Check Java version: `java -version`
2. Check Maven version: `mvn -version`
3. Clean and rebuild: `mvn clean install`
4. Check database connections
5. Verify all services are running

### If Frontend Won't Start

1. Check Node.js version: `node -v`
2. Clear cache: `npm cache clean --force`
3. Delete `node_modules` and reinstall
4. Check for port conflicts
5. Verify backend is running

### If Authentication Fails

1. Check JWT secret in `application.yml`
2. Verify database has user data
3. Check CORS configuration
4. Verify API endpoints are accessible

## 📞 Support

If you encounter issues not covered in this manual:

1. Check the console logs for error messages
2. Verify all prerequisites are installed correctly
3. Ensure all services (MySQL, MongoDB) are running
4. Check network connectivity between frontend and backend
5. Verify file paths and configurations

---

**🎯 Final Goal:** You should have a fully functional EduMate application running locally with user authentication, dashboard, and all basic features working correctly.
