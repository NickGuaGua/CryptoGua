<h1 align="center">CryptoGua</h1>

<p align="center">  
A modern Android cryptocurrency trading application built with Jetpack Compose and Clean Architecture principles.
</p>

## 🎯 Project Goals

This project demonstrates:

1. **Modern Android Development**: Latest Jetpack libraries and Kotlin features
2. **Clean Architecture**: Maintainable and testable code structure
3. **Real-time Applications**: WebSocket integration with proper error handling
4. **Comprehensive Testing**: Unit, integration, and UI test strategies
5. **Production-Ready Code**: Error handling, logging, and performance optimization

## 🧪 Demo

The app displays a markets view with Spot/Futures tabs, showing real-time prices via WebSocket
streaming with automatic reconnection.
<p align="center">
  <img src="/img/demo.gif" style="margin-right:30px;"/>
  <img src="/img/auto-reconnection.gif"/>
</p>

## 🏗️ Architecture

GuaPay is based on the MVVM architecture and the Repository pattern, and follows
the [Google's official architecture guidance](https://developer.android.com/topic/architecture).
<p align="center">
<img src="/img/img_architecture.png"/>
</p>

## 🚀 Technologies & Libraries

### Core Technologies

- **Kotlin** (2.2.20) - 100% Kotlin codebase
- **Jetpack Compose** (BOM 2025.09.01) - Modern declarative UI
- **Material Design 3** - Latest Material Design system
- **Android SDK** - Target 36, Min 30

### Architecture Components

- **Hilt** (2.57.1) - Dependency injection
- **Navigation Compose** (2.9.5) - Type-safe navigation
- **ViewModel & LiveData** - MVVM architecture
- **Kotlinx Coroutines** (1.10.2) - Asynchronous programming

### Networking & Real-time Data

- **Retrofit** (3.0.0) - REST API client
- **OkHttp** (5.1.0) - HTTP client with WebSocket support
- **Kotlinx Serialization** (1.9.0) - JSON serialization
- **WebSocket Client** - Custom real-time data streaming
- **Flow** - Reactive data streams

### Logging & Debugging

- **Timber** (5.0.1) - Structured logging

### Testing Technologies

- **JUnit 4** (4.13.2) - Testing framework
- **MockK** (1.13.8) - Kotlin mocking library
- **Kotlinx Coroutines Test** - Coroutine testing utilities
- **Compose UI Test** - Declarative UI testing

## 🔧 Build & Development

### Prerequisites

- Android Studio Narwhal 3 Feature Drop | 2025.1.3
- JDK 11 or newer
- Android SDK 36