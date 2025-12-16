# 🌍 Translation Service - Spring Boot Edition

Production-ready translation service built with **Spring Boot** supporting **Google Cloud Translate**, **OpenAI**, and **Local GPU** with easy engine switching.

## ✨ Features

✅ **Spring Boot 3.2.0** - Latest version with Java 17
✅ **3 Translation Engines** - Google, OpenAI, Local
✅ **Easy Configuration** - Switch engines via `application.yml`
✅ **RESTful API** - Full CRUD with validation
✅ **Exception Handling** - Global error handling
✅ **Batch Processing** - Translate 1-100 texts
✅ **Health Checks** - Monitor service status
✅ **Comprehensive Tests** - JUnit 5 with MockMvc

## 🏗️ Architecture

### Project Structure

```
translation-service-springboot/
├── src/
│   ├── main/
│   │   ├── java/com/translation/
│   │   │   ├── TranslationServiceApplication.java  # Main entry point
│   │   │   ├── config/                             # Configuration
│   │   │   │   ├── TranslationEngine.java
│   │   │   │   └── TranslationProperties.java
│   │   │   ├── controller/                         # REST endpoints
│   │   │   │   └── TranslationController.java
│   │   │   ├── service/                            # Business logic
│   │   │   │   └── TranslationService.java
│   │   │   ├── integration/                        # Translation providers
│   │   │   │   ├── TranslationProvider.java        # Interface
│   │   │   │   ├── GoogleTranslationProvider.java
│   │   │   │   ├── OpenAITranslationProvider.java
│   │   │   │   ├── LocalTranslationProvider.java
│   │   │   │   └── TranslationProviderFactory.java
│   │   │   ├── dto/                                # Data Transfer Objects
│   │   │   │   └── TranslationDtos.java
│   │   │   └── exception/                          # Exception handling
│   │   │       ├── TranslationException.java
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       └── application.yml                     # Configuration
│   └── test/
│       └── java/com/translation/
│           └── TranslationServiceApplicationTests.java
└── pom.xml                                         # Maven dependencies
```

## 🚀 Quick Start

### Prerequisites

- JDK 17+
- Maven 3.8+
- Google Cloud Account (for GCP)

### Installation

#### 1. Clone/Extract Project

```bash
unzip translation-service-springboot.zip
cd translation-service-springboot
```

#### 2. Configure Google Cloud Translate

Follow the same steps as FastAPI version:
1. Create GCP Project
2. Enable Cloud Translation API
3. Create Service Account
4. Download JSON credentials

#### 3. Configure Application

Edit `src/main/resources/application.yml`:

```yaml
translation:
  engine: google  # or openai, local
  google:
    project-id: YOUR_PROJECT_ID
    credentials-path: ./google-credentials.json
```

#### 4. Build

```bash
mvn clean package
```

#### 5. Run

```bash
mvn spring-boot:run
```

Or use the JAR:

```bash
java -jar target/translation-service-1.0.0.jar
```

Visit: `http://localhost:8080/api/translate/info`

## 📋 API Endpoints

### Translate Single Text

```bash
POST /api/translate/

Request:
{
  "text": "Hello, how are you?",
  "sourceLanguage": "en",
  "targetLanguage": "es"
}

Response:
{
  "originalText": "Hello, how are you?",
  "translatedText": "Hola, ¿cómo estás?",
  "sourceLanguage": "en",
  "targetLanguage": "es",
  "engine": "GoogleTranslationProvider",
  "timestamp": "2024-01-01T12:00:00"
}
```

### Batch Translate

```bash
POST /api/translate/batch

Request:
{
  "texts": ["Hello", "Good morning", "Thank you"],
  "sourceLanguage": "en",
  "targetLanguage": "es"
}

Response:
{
  "originalTexts": [...],
  "translatedTexts": ["Hola", "Buenos días", "Gracias"],
  "count": 3,
  "engine": "GoogleTranslationProvider",
  "timestamp": "2024-01-01T12:00:00"
}
```

### Get Supported Languages

```bash
GET /api/translate/languages

Response:
{
  "languages": {
    "en": "English",
    "es": "Spanish",
    ...
  },
  "engine": "GoogleTranslationProvider",
  "total": 12
}
```

### Health Check

```bash
GET /api/translate/health

Response:
{
  "healthy": true,
  "engine": "GoogleTranslationProvider",
  "timestamp": "2024-01-01T12:00:00",
  "responseTimeMs": 245.32
}
```

## 🔧 Configuration

### Using Google Cloud Translate

```yaml
translation:
  engine: google
  google:
    project-id: my-project-123456
    credentials-path: ./google-credentials.json
```

### Using OpenAI

```yaml
translation:
  engine: openai
  openai:
    api-key: sk-your-api-key
    model: gpt-3.5-turbo
```

### Using Local GPU

```yaml
translation:
  engine: local
  local:
    model-name: facebook/nllb-200-distilled-600M
    device: cuda  # or cpu
    precision: float32
    batch-size: 8
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=TranslationServiceApplicationTests

# Run with coverage
mvn test jacoco:report
```

## 🚀 Deployment

### Docker

```dockerfile
FROM openjdk:17-slim
WORKDIR /app
COPY target/translation-service-1.0.0.jar app.jar
COPY google-credentials.json .
COPY application.yml .
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build:

```bash
docker build -t translation-service:1.0 .
docker run -p 8080:8080 translation-service:1.0
```

## 📊 Technology Stack

- **Framework:** Spring Boot 3.2.0
- **Language:** Java 17
- **Build:** Maven
- **APIs:**
  - Google Cloud Translate
  - OpenAI GPT
  - DJL (for local models)
- **Testing:** JUnit 5, MockMvc
- **Logging:** SLF4J

## 🔒 Security

- Input validation with Bean Validation
- CORS enabled for all origins
- Exception handling with error codes
- Sensitive credentials in environment variables

## 📈 Performance

- No database (stateless design)
- Batch processing support
- Async-ready architecture
- Spring Boot auto-configuration

## 🆘 Troubleshooting

### Port Already in Use

```bash
# Change port in application.yml
server:
  port: 8081
```

### Google Credentials Not Found

```bash
# Ensure credentials file is in project root
# Or set full path in application.yml
translation:
  google:
    credentials-path: /absolute/path/to/credentials.json
```

### Maven Build Fails

```bash
# Clean and rebuild
mvn clean install
```

## 📞 Support

- **Documentation:** See inline code comments
- **API Docs:** http://localhost:8080/api/translate/info
- **Tests:** Check `src/test/` folder
- **Config:** See `application.yml`

## 🎉 Summary

You now have a **production-ready translation service** in **Spring Boot** with:

✅ **3 translation engines** ready to use
✅ **Easy configuration** via `application.yml`
✅ **RESTful API** fully documented
✅ **Comprehensive testing**
✅ **Docker support**
✅ **Professional code structure**

---

**Happy translating! 🌍**

Start with `mvn spring-boot:run` and visit `http://localhost:8080/api/translate/languages`
