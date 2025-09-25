# QuickQuest - Meta Quest Quick Launcher

A lightweight, gesture-triggered launcher app for Meta Quest headsets that provides fast search and launching of installed apps with a minimal overlay UI.

## Features

- **Glass-morphic Overlay UI**: Semi-transparent floating window that appears over current apps
- **Instant Search**: Fuzzy search through all installed applications
- **App Discovery**: Automatically detects and lists all installed apps (including sideloaded)
- **Quick Launch**: Tap to launch any discovered application
- **Quest Optimized**: Designed specifically for Meta Quest 2/3/3S/Pro

## Current Status

✅ **Completed:**
- Android project setup with Kotlin and Gradle
- MVVM architecture with ViewModels and use cases
- Glass-morphic launcher UI with search functionality
- PackageManager integration for app discovery
- Fuzzy search implementation
- App launching via Android intents
- GitHub repository with CI/CD pipeline

🔄 **In Progress:**
- Meta Spatial SDK integration for true overlay functionality
- Gesture and button trigger implementation
- Performance optimization
- Unit testing

📋 **Upcoming:**
- Custom shortcuts and app categories
- Data persistence for user preferences
- Device testing on Quest hardware
- Developer documentation

## Architecture

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Async**: Kotlin Coroutines
- **Spatial SDK**: Meta Spatial SDK for overlay functionality

## Project Structure

```
app/src/main/java/com/meta/quicklauncher/
├── di/                    # Dependency injection modules
├── domain/               # Business logic layer
│   ├── model/           # Data models
│   └── usecase/         # Use cases
├── ui/                  # Presentation layer
│   ├── launcher/        # Main launcher UI
│   └── theme/           # App theming
├── MainActivity.kt      # App entry point
├── QuickQuestApp.kt     # Main composable
└── QuickQuestApplication.kt # Hilt application class
```

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run single test
./gradlew testDebugUnitTest --tests="*.TestClass.testMethod"

# Lint code
./gradlew lint
```

## Requirements

- **Android API Level**: 29+ (Quest 2 minimum)
- **Kotlin**: 1.8.10+
- **Meta Quest**: Quest 2/3/3S/Pro with developer mode enabled
- **Meta Spatial SDK**: For overlay functionality

## Permissions

- `SYSTEM_ALERT_WINDOW`: For overlay functionality
- `QUERY_ALL_PACKAGES`: To discover installed applications
- `INTERNET`: For potential web shortcuts

## Development Setup

1. Clone the repository
2. Ensure Android SDK 31+ is installed
3. Accept Android SDK licenses: `sdkmanager --licenses`
4. Build the project: `./gradlew build`
5. Run on Quest device with developer mode enabled

## Contributing

1. Follow the established code style (see AGENTS.md)
2. Write tests for new functionality
3. Update documentation as needed
4. Test on actual Quest hardware

## License

This project is part of the Meta Quest ecosystem and follows Meta's licensing terms.