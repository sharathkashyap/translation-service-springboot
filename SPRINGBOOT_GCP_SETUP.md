# 🚀 Spring Boot Translation Service - GCP Setup Guide

Step-by-step guide to run the translation service with **Google Cloud Translate as the default engine**.

---

## 📋 Prerequisites

- JDK 17+
- Maven 3.8+
- Google Cloud Account
- Translation Service Spring Boot files

---

## 🚀 Step-by-Step Setup

### **Part 1: Google Cloud Setup (15 minutes)**

Follow the same steps as the FastAPI version:

**STEP 1-5:** Create GCP Project, Enable API, Create Service Account, Download JSON, Get Project ID

(See GCP_SETUP_STEPS.txt or GCP_SETUP_GUIDE.md for detailed instructions)

**Result:** 
- `google-credentials.json` file downloaded
- Project ID copied (e.g., `my-translation-project-123456`)

---

### **Part 2: Local Setup (10 minutes)**

#### STEP 6: Extract Spring Boot Project

```bash
unzip translation-service-springboot.zip
cd translation-service-springboot
```

#### STEP 7: Copy Google Credentials

Place `google-credentials.json` in the project root:

```
translation-service-springboot/
├── google-credentials.json    ← Copy here
├── src/
├── pom.xml
└── README.md
```

#### STEP 8: Configure Application

Edit `src/main/resources/application.yml`:

```yaml
# Change this section:

translation:
  engine: google  # ✅ Set to Google
  google:
    project-id: YOUR_PROJECT_ID      # ← Replace with your project ID
    credentials-path: ./google-credentials.json  # ← Path to credentials file
  
  openai:
    api-key: ${OPENAI_API_KEY:}
  local:
    model-name: facebook/nllb-200-distilled-600M
```

**Example with actual values:**

```yaml
translation:
  engine: google
  google:
    project-id: my-translation-project-123456
    credentials-path: ./google-credentials.json
```

#### STEP 9: Verify File Structure

Your project should look like:

```
translation-service-springboot/
├── google-credentials.json
├── src/
│   ├── main/
│   │   ├── java/com/translation/
│   │   │   ├── TranslationServiceApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── integration/
│   │   │   ├── dto/
│   │   │   └── exception/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
└── README.md
```

---

### **Part 3: Build & Run (5 minutes)**

#### STEP 10: Build with Maven

```bash
mvn clean package
```

Wait for build to complete. You should see:

```
BUILD SUCCESS
```

#### STEP 11: Run the Application

**Option A: Using Maven Plugin**

```bash
mvn spring-boot:run
```

**Option B: Using JAR File**

```bash
java -jar target/translation-service-1.0.0.jar
```

#### STEP 12: Verify It's Running

You should see in console:

```
╔════════════════════════════════════════════════════════════════╗
║  🌍 Translation Service API Started Successfully 🌍           ║
║  API Documentation: http://localhost:8080/swagger-ui.html    ║
║  Health Check: http://localhost:8080/api/translate/health    ║
╚════════════════════════════════════════════════════════════════╝
```

---

### **Part 4: Test the Service (5 minutes)**

#### STEP 13: Test Health Check

```bash
curl http://localhost:8080/api/translate/health
```

Expected response:

```json
{
  "healthy": true,
  "engine": "GoogleTranslationProvider",
  "timestamp": "2024-01-01T12:00:00",
  "responseTimeMs": 245.32
}
```

#### STEP 14: Test Translation

```bash
curl -X POST http://localhost:8080/api/translate/ \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Hello world",
    "sourceLanguage": "en",
    "targetLanguage": "es"
  }'
```

Expected response:

```json
{
  "originalText": "Hello world",
  "translatedText": "Hola mundo",
  "sourceLanguage": "en",
  "targetLanguage": "es",
  "engine": "GoogleTranslationProvider",
  "timestamp": "2024-01-01T12:00:00"
}
```

#### STEP 15: Test Supported Languages

```bash
curl http://localhost:8080/api/translate/languages
```

Expected response includes list of supported languages.

---

## ✅ Verification Checklist

- [ ] JDK 17+ installed (`java -version`)
- [ ] Maven installed (`mvn -v`)
- [ ] Google Cloud Project created
- [ ] Cloud Translation API enabled
- [ ] Service account created with API access
- [ ] JSON credentials downloaded
- [ ] Project ID copied
- [ ] Spring Boot project extracted
- [ ] google-credentials.json placed in root
- [ ] application.yml configured with project ID
- [ ] `mvn clean package` successful
- [ ] `mvn spring-boot:run` or `java -jar` started successfully
- [ ] Health check returns `"healthy": true`
- [ ] Translation endpoint returns translated text

---

## 🎯 Endpoints to Test

### Service Info
```bash
curl http://localhost:8080/api/translate/info
```

### Translate Single Text
```bash
curl -X POST http://localhost:8080/api/translate/ \
  -H "Content-Type: application/json" \
  -d '{"text":"Hello","sourceLanguage":"en","targetLanguage":"es"}'
```

### Batch Translate
```bash
curl -X POST http://localhost:8080/api/translate/batch \
  -H "Content-Type: application/json" \
  -d '{
    "texts":["Hello","Hi","Bye"],
    "sourceLanguage":"en",
    "targetLanguage":"es"
  }'
```

### Get Languages
```bash
curl http://localhost:8080/api/translate/languages
```

### Health Check
```bash
curl http://localhost:8080/api/translate/health
```

---

## 💻 Project Structure Explained

| File/Folder | Purpose |
|-------------|---------|
| `TranslationServiceApplication.java` | Spring Boot entry point |
| `TranslationController.java` | REST API endpoints |
| `TranslationService.java` | Business logic |
| `TranslationProvider.java` | Interface for providers |
| `GoogleTranslationProvider.java` | Google Cloud implementation |
| `TranslationProviderFactory.java` | Creates correct provider |
| `application.yml` | **Configuration (engine choice here!)** |
| `pom.xml` | Maven dependencies |

---

## 🔧 Changing Configuration

### Switch to OpenAI

```yaml
translation:
  engine: openai
  openai:
    api-key: sk-your-api-key
    model: gpt-3.5-turbo
```

### Switch to Local GPU

```yaml
translation:
  engine: local
  local:
    model-name: facebook/nllb-200-distilled-600M
    device: cuda
```

### Change Port

```yaml
server:
  port: 9000  # Instead of 8080
```

### Change Log Level

```yaml
logging:
  level:
    root: INFO
    com.translation: DEBUG
```

---

## 🆘 Troubleshooting

### Build Fails

```bash
# Clear Maven cache
mvn clean install

# Or with skipping tests
mvn clean package -DskipTests
```

### Port 8080 Already in Use

Edit `application.yml`:

```yaml
server:
  port: 8081  # Change to different port
```

### Google Credentials Not Found

```bash
# Check file is in root directory
ls google-credentials.json

# Or use absolute path in application.yml
translation:
  google:
    credentials-path: /absolute/path/to/google-credentials.json
```

### Authentication Error

1. Verify `google-credentials.json` is valid
2. Check Google Cloud API is enabled
3. Verify service account has "Cloud Translation API User" role

### Connection Refused

1. Ensure app is running: `mvn spring-boot:run`
2. Check port: http://localhost:8080
3. If port changed, use correct port in requests

---

## 📊 Testing the Application

### Run Unit Tests

```bash
mvn test
```

### Run Specific Test

```bash
mvn test -Dtest=TranslationServiceApplicationTests
```

### Run with Coverage

```bash
mvn test jacoco:report
```

---

## 🚀 Production Deployment

### Create Fat JAR

```bash
mvn clean package
```

Result: `target/translation-service-1.0.0.jar`

### Run in Production

```bash
java -jar translation-service-1.0.0.jar
```

### Using Docker

```dockerfile
FROM openjdk:17-slim
WORKDIR /app
COPY target/translation-service-1.0.0.jar app.jar
COPY google-credentials.json .
COPY src/main/resources/application.yml .
CMD ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t translation-service:1.0 .
docker run -p 8080:8080 translation-service:1.0
```

---

## 💰 Google Cloud Pricing

- **Free:** 500,000 characters/month
- **Paid:** $16 per million characters

Monitor in GCP Console → Billing → Reports

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Google Cloud Translation API](https://cloud.google.com/translate/docs)
- [Maven Guide](https://maven.apache.org/guides/)
- [Java 17 Features](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

---

## ✨ Summary

Your Spring Boot translation service is now configured with **Google Cloud Translate**!

**Next Steps:**
1. Run: `mvn spring-boot:run`
2. Test: http://localhost:8080/api/translate/health
3. API Docs: http://localhost:8080/api/translate/info
4. Start translating!

---

**Happy translating with Spring Boot! 🌍**
