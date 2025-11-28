# EduMate Deployment Guide

Complete guide to deploy the EduMate learning platform for free using modern cloud services.

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Architecture](#architecture)
- [Database Setup](#database-setup)
- [Backend Deployment](#backend-deployment)
- [Frontend Deployment](#frontend-deployment)
- [Environment Configuration](#environment-configuration)
- [Testing the Deployment](#testing-the-deployment)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Project Overview

**EduMate** is a full-stack learning platform with:
- **Frontend**: React 19 + Vite + TailwindCSS
- **Backend**: Spring Boot 3.2 + Java 17
- **Databases**: MySQL (relational data) + MongoDB (document storage)
- **Authentication**: JWT-based security

---

## ✅ Prerequisites

Before deploying, ensure you have:
- Git installed
- GitHub account (for code hosting)
- Basic understanding of environment variables
- Access to the services mentioned below

---

## 🏗️ Architecture

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│   Frontend  │─────▶│   Backend    │─────▶│  Databases  │
│  (Vercel)   │      │  (Railway)   │      │  (Railway)  │
└─────────────┘      └──────────────┘      └─────────────┘
```

---

## 🗄️ Database Setup

### Option 1: Railway (Recommended - Free Tier Available)

Railway provides free hosting for both MySQL and MongoDB databases.

#### Step 1: Create Railway Account
1. Go to [Railway.app](https://railway.app)
2. Sign up with your GitHub account
3. Verify your email

#### Step 2: Create MySQL Database
1. Click **"New Project"**
2. Select **"Provision MySQL"**
3. Once created, click on the MySQL service
4. Go to **"Variables"** tab and note:
   - `MYSQLHOST`
   - `MYSQLPORT`
   - `MYSQLDATABASE`
   - `MYSQLUSER`
   - `MYSQLPASSWORD`
5. Go to **"Settings"** tab
6. Under **"Networking"**, click **"Generate Domain"** to get a public URL

#### Step 3: Create MongoDB Database
1. In the same project, click **"New"** → **"Database"** → **"Add MongoDB"**
2. Once created, click on the MongoDB service
3. Go to **"Variables"** tab and note:
   - `MONGO_URL` (connection string)
4. Generate a public domain if needed

### Option 2: Free Cloud Database Providers

#### MySQL Alternatives:
- **Aiven** (500MB free): https://aiven.io
- **PlanetScale** (5GB free): https://planetscale.com
- **Clever Cloud** (256MB free): https://clever-cloud.com

#### MongoDB Alternatives:
- **MongoDB Atlas** (512MB free): https://www.mongodb.com/cloud/atlas
  1. Create account and cluster
  2. Click **"Connect"** → **"Connect your application"**
  3. Copy connection string
  4. Replace `<password>` with your database password
  5. Whitelist IP: `0.0.0.0/0` (allow all) in Network Access

---

## 🚀 Backend Deployment

### Option 1: Railway (Recommended)

#### Step 1: Prepare Backend
1. Push your code to GitHub if not already done:
```bash
cd Backend
git init
git add .
git commit -m "Initial backend commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/edumate-backend.git
git push -u origin main
```

#### Step 2: Deploy to Railway
1. Go to Railway dashboard
2. Click **"New Project"** → **"Deploy from GitHub repo"**
3. Select your backend repository
4. Railway will auto-detect Spring Boot application

#### Step 3: Configure Environment Variables
In Railway project settings, add these variables:
```
SPRING_DATASOURCE_URL=jdbc:mysql://<MYSQLHOST>:<MYSQLPORT>/<MYSQLDATABASE>?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=<MYSQLUSER>
SPRING_DATASOURCE_PASSWORD=<MYSQLPASSWORD>
SPRING_DATA_MONGODB_URI=<MONGO_URL>
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
JWT_EXPIRATION=86400000
CORS_ALLOWED_ORIGINS=https://your-frontend-url.vercel.app
SERVER_PORT=8080
```

#### Step 4: Update application.yml (Backend)
Create `src/main/resources/application-prod.yml`:
```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  application:
    name: edumate-backend
  
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
  
  data:
    mongodb:
      uri: ${SPRING_DATA_MONGODB_URI}
      auto-index-creation: true

jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION}

cors:
  allowed-origins: ${CORS_ALLOWED_ORIGINS}
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true

logging:
  level:
    com.edumate: INFO
    org.springframework.security: WARN
```

#### Step 5: Set Active Profile
In Railway, add:
```
SPRING_PROFILES_ACTIVE=prod
```

#### Step 6: Generate Domain
1. Go to **"Settings"** → **"Networking"**
2. Click **"Generate Domain"**
3. Note your backend URL (e.g., `https://edumate-backend.up.railway.app`)

### Option 2: Render.com (Free Tier)

1. Sign up at [Render.com](https://render.com)
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository
4. Configure:
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`
   - Add environment variables as above
5. Deploy

---

## 🎨 Frontend Deployment

### Option 1: Vercel (Recommended)

#### Step 1: Prepare Frontend
1. Update API endpoint in your frontend code
2. Create `.env.production` file in Frontend directory:
```env
VITE_API_BASE_URL=https://your-backend-url.railway.app
```

#### Step 2: Update API Configuration
Create or update `Frontend/src/config/api.js`:
```javascript
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:3001';

export default API_BASE_URL;
```

Update your API calls to use this configuration:
```javascript
import API_BASE_URL from './config/api';

fetch(`${API_BASE_URL}/api/users/login`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(credentials)
});
```

#### Step 3: Deploy to Vercel
1. Install Vercel CLI (optional):
```bash
npm install -g vercel
```

2. **Option A: Via CLI**
```bash
cd Frontend
vercel
# Follow prompts
```

3. **Option B: Via Web Dashboard**
   - Go to [Vercel.com](https://vercel.com)
   - Click **"New Project"**
   - Import from GitHub
   - Select your frontend repository
   - Configure:
     - **Framework Preset**: Vite
     - **Build Command**: `npm run build`
     - **Output Directory**: `dist`
   - Add environment variable:
     - `VITE_API_BASE_URL` = `https://your-backend-url.railway.app`
   - Click **"Deploy"**

#### Step 4: Update Backend CORS
Update Railway backend environment variable:
```
CORS_ALLOWED_ORIGINS=https://your-frontend-name.vercel.app
```

### Option 2: Netlify (Alternative)

1. Sign up at [Netlify.com](https://netlify.com)
2. Click **"Add new site"** → **"Import from Git"**
3. Select repository
4. Configure:
   - **Build command**: `npm run build`
   - **Publish directory**: `dist`
5. Add environment variables in **Site settings** → **Environment variables**
6. Deploy

### Option 3: Cloudflare Pages

1. Go to [Cloudflare Pages](https://pages.cloudflare.com)
2. Click **"Create a project"**
3. Connect GitHub repository
4. Configure build settings:
   - **Framework preset**: Vite
   - **Build command**: `npm run build`
   - **Build output**: `dist`
5. Add environment variables
6. Deploy

---

## ⚙️ Environment Configuration

### Backend Environment Variables Summary
```
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://host:port/database?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=your_mysql_user
SPRING_DATASOURCE_PASSWORD=your_mysql_password
SPRING_DATA_MONGODB_URI=mongodb://user:password@host:port/database

# Security
JWT_SECRET=change-this-to-a-long-random-string
JWT_EXPIRATION=86400000

# CORS
CORS_ALLOWED_ORIGINS=https://your-frontend.vercel.app

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

### Frontend Environment Variables Summary
```
VITE_API_BASE_URL=https://your-backend.railway.app
```

---

## 🧪 Testing the Deployment

### 1. Check Backend Health
```bash
curl https://your-backend.railway.app/actuator/health
```

### 2. Test API Endpoints
```bash
# Test registration
curl -X POST https://your-backend.railway.app/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"Test123!"}'

# Test login
curl -X POST https://your-backend.railway.app/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test123!"}'
```

### 3. Check Frontend
Visit `https://your-frontend.vercel.app` and test:
- Homepage loads correctly
- User registration works
- User login works
- API calls are successful

### 4. Monitor Logs
- **Railway**: Go to your service → **"Deployments"** → Click latest deployment → View logs
- **Vercel**: Go to your project → **"Deployments"** → Click latest → **"Functions"** tab for logs

---

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. CORS Errors
**Error**: `Access to fetch at 'backend-url' from origin 'frontend-url' has been blocked by CORS policy`

**Solution**:
- Ensure `CORS_ALLOWED_ORIGINS` in backend includes your exact frontend URL
- Check for trailing slashes (should NOT have trailing slash)
- Verify CORS configuration in Spring Boot

#### 2. Database Connection Errors
**Error**: `Unable to connect to database`

**Solutions**:
- Verify database credentials in environment variables
- Check if database service is running
- For Railway: Ensure both backend and database are in the same project
- Whitelist IP addresses in database network settings
- For MongoDB Atlas: Set Network Access to `0.0.0.0/0`

#### 3. Build Failures

**Backend Build Fails**:
```bash
# Check Java version
java -version  # Should be 17

# Try local build first
mvn clean package -DskipTests
```

**Frontend Build Fails**:
```bash
# Clear cache and rebuild
rm -rf node_modules package-lock.json
npm install
npm run build
```

#### 4. Environment Variables Not Loading
- Verify variable names are correct (case-sensitive)
- Restart/redeploy service after adding variables
- Check for typos in variable names
- For Vite: Ensure variables start with `VITE_`

#### 5. JWT Authentication Errors
**Error**: `Invalid JWT signature`

**Solution**:
- Ensure `JWT_SECRET` is set and matches in production
- Check token expiration time
- Verify token is being sent in Authorization header: `Bearer <token>`

#### 6. Port Issues on Railway
**Error**: `Port already in use`

**Solution**:
- Railway automatically assigns a port via `PORT` environment variable
- Update application to use: `server.port=${PORT:8080}`

#### 7. MySQL Connection Timeout
**Solutions**:
- Add `&connectTimeout=60000&socketTimeout=60000` to JDBC URL
- Check if MySQL instance is sleeping (common in free tiers)
- Implement connection pooling in application

---

## 🚀 Optional Enhancements

### 1. Custom Domain (Vercel)
1. Go to Vercel project settings
2. Click **"Domains"**
3. Add your custom domain
4. Update DNS records as instructed

### 2. SSL/HTTPS
- Both Vercel and Railway provide automatic SSL certificates
- Ensure your API calls use `https://` protocol

### 3. CI/CD Pipeline
Both platforms support automatic deployments:
- **Vercel**: Auto-deploys on push to main branch
- **Railway**: Auto-deploys on push to connected branch

### 4. Database Backups
- **Railway**: Enable automatic backups in database settings
- **MongoDB Atlas**: Automatic backups included in free tier

### 5. Monitoring
- **Railway**: Built-in metrics and logs
- **Vercel**: Analytics available in dashboard
- **External**: Consider [UptimeRobot](https://uptimerobot.com) for uptime monitoring (free)

---

## 💰 Cost Breakdown (Free Tier Limits)

### Railway
- **Database**: 500MB MySQL + 500MB MongoDB
- **Compute**: 500 hours/month execution time
- **Bandwidth**: 100GB/month

### Vercel
- **Bandwidth**: 100GB/month
- **Build minutes**: Unlimited for personal projects
- **Domains**: Unlimited

### MongoDB Atlas (if used separately)
- **Storage**: 512MB
- **RAM**: Shared
- **Connections**: 500 concurrent

### Scaling Considerations
When you exceed free tier limits:
- Railway: $5/month minimum
- Vercel: $20/month Pro plan
- MongoDB Atlas: Starting at $9/month

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Vite Deployment Guide](https://vitejs.dev/guide/static-deploy.html)
- [Railway Documentation](https://docs.railway.app)
- [Vercel Documentation](https://vercel.com/docs)
- [MongoDB Atlas Documentation](https://docs.atlas.mongodb.com)

---

## 📝 Deployment Checklist

### Pre-Deployment
- [ ] Code pushed to GitHub
- [ ] Environment variables documented
- [ ] Database credentials secured
- [ ] Production configuration files created
- [ ] API endpoints tested locally

### Database Setup
- [ ] MySQL database created
- [ ] MongoDB database created
- [ ] Connection strings obtained
- [ ] Network access configured

### Backend Deployment
- [ ] Service deployed on Railway/Render
- [ ] Environment variables configured
- [ ] Public domain generated
- [ ] Health endpoint accessible
- [ ] Database connections verified

### Frontend Deployment
- [ ] API base URL updated
- [ ] Deployed to Vercel/Netlify
- [ ] Environment variables set
- [ ] Build successful
- [ ] Application accessible

### Post-Deployment
- [ ] CORS configured correctly
- [ ] All API endpoints tested
- [ ] User registration/login working
- [ ] Logs monitored for errors
- [ ] Performance checked

---

## 🎉 Success!

Your EduMate application should now be live and accessible to users worldwide, completely free of charge!

**Frontend URL**: `https://your-app.vercel.app`  
**Backend URL**: `https://your-backend.railway.app`

For questions or issues, refer to the troubleshooting section above or check the documentation links provided.

---

*Last Updated: November 2025*

