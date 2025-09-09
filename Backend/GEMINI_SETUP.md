# EduMate - Gemini AI Integration Setup Guide

This guide will help you set up Gemini AI integration for generating quizzes and flashcards from uploaded notes.

## Prerequisites

1. **Java 17** or higher
2. **Maven 3.6+**
3. **MySQL 8.0+**
4. **MongoDB 6.0+**
5. **Node.js 18+**
6. **Google AI Studio Account** (for Gemini API key)

## Step 1: Get Gemini API Key

1. Go to [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Sign in with your Google account
3. Create a new API key
4. Copy the API key (keep it secure!)

## Step 2: Backend Configuration

### 1. Update application.properties

Replace `YOUR_GEMINI_API_KEY_HERE` in `src/main/resources/application.properties` with your actual Gemini API key:

```properties
gemini.api.key=YOUR_ACTUAL_GEMINI_API_KEY
```

### 2. Database Setup

**MySQL:**
```sql
CREATE DATABASE edumate_db;
CREATE USER 'edumate'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON edumate_db.* TO 'edumate'@'localhost';
FLUSH PRIVILEGES;
```

**MongoDB:**
- Install MongoDB and start the service
- The application will create the database automatically

### 3. Build and Run Backend

```bash
cd Backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:3001`

## Step 3: Frontend Setup

### 1. Install Dependencies

```bash
cd Frontend
npm install
```

### 2. Environment Configuration (Optional)

Create a `.env` file in the Frontend directory if you want to use a different API URL:

```env
VITE_API_URL=http://localhost:3001/api
```

### 3. Start Frontend

```bash
npm run dev
```

The frontend will start on `http://localhost:5173` (or `http://localhost:3000`)

## Step 4: How to Use

### 1. Access the Application

1. Open your browser and go to `http://localhost:5173`
2. Register or login to your account
3. Navigate to the "Notes" section

### 2. Generate AI Content

1. **Create Notes:** Enter a title and content for your study notes
2. **Generate Content:** Choose from the following options:
   - **📝 Generate Summary:** Creates a comprehensive summary
   - **❓ Generate Quiz:** Creates multiple-choice questions (5 questions)
   - **🗂️ Generate Flashcards:** Creates study flashcards (10 cards)
   - **✨ Generate All:** Creates all content types at once

### 3. View Generated Content

- Switch to the "Generated Content" tab to view AI-generated materials
- Content is formatted and color-coded by type

## API Endpoints

### AI Generation Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/ai/generate-quiz` | Generate quiz questions |
| POST | `/api/ai/generate-flashcards` | Generate flashcards |
| POST | `/api/ai/generate-summary` | Generate summary |
| POST | `/api/ai/generate-all` | Generate all content types |

### Request Body Format

```json
{
  "noteContent": "Your study notes content here...",
  "numberOfItems": 5,
  "noteId": "optional-note-id",
  "userId": "optional-user-id"
}
```

### Response Format

```json
{
  "content": "Generated AI content as JSON or text",
  "type": "quiz|flashcards|summary",
  "success": true,
  "errorMessage": null
}
```

## Generated Content Formats

### Quiz Format
```json
[
  {
    "question": "What is the main concept?",
    "options": ["Option A", "Option B", "Option C", "Option D"],
    "correctAnswer": "A",
    "explanation": "Explanation of the correct answer"
  }
]
```

### Flashcard Format
```json
[
  {
    "front": "Question or term",
    "back": "Answer or definition",
    "category": "Topic category"
  }
]
```

### Summary Format
```
Plain text summary with bullet points and structured content
```

## Troubleshooting

### Common Issues

1. **API Key Error:** Make sure your Gemini API key is valid and has quota remaining
2. **CORS Issues:** Ensure the frontend URL is in the allowed origins list
3. **Database Connection:** Check MySQL and MongoDB are running and accessible
4. **Port Conflicts:** Make sure ports 3001 (backend) and 5173 (frontend) are available

### Error Messages

- `"Gemini API request failed"` - Check API key and internet connection
- `"Note content is required"` - Ensure you've entered text in the notes field
- `"Failed to generate content"` - Check backend logs for detailed error information

## Development Notes

### Code Structure

- **Backend Service:** `GeminiAIService.java` handles all AI API calls
- **Controller:** `AIGenerationController.java` exposes REST endpoints
- **Frontend Service:** `aiService.js` handles frontend API communication
- **UI Component:** Enhanced `Notes.jsx` with AI generation features

### Customization

You can customize:
- Number of quiz questions (default: 5)
- Number of flashcards (default: 10)
- Prompt templates in `GeminiAIService.java`
- UI styling and layout

## Security Considerations

1. **API Key Security:** Never commit API keys to version control
2. **Environment Variables:** Use environment variables for sensitive config
3. **Rate Limiting:** Consider implementing rate limiting for AI endpoints
4. **Authentication:** Ensure AI endpoints are properly authenticated

## Performance Tips

1. **Caching:** Consider caching generated content to reduce API calls
2. **Async Processing:** For large content, consider background processing
3. **Error Handling:** Implement retry logic for transient failures
4. **Monitoring:** Monitor API usage and costs

## Next Steps

1. Add user authentication to AI endpoints
2. Implement content saving/persistence
3. Add more content types (e.g., study guides, practice tests)
4. Integrate with existing Note, Quiz, and Flashcard entities
5. Add content export/sharing features

## Support

For issues or questions:
1. Check the application logs in the console
2. Verify all services are running correctly
3. Test API endpoints directly using tools like Postman
4. Check Gemini API documentation for updates
