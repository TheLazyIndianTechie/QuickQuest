# Agent Guidelines for Meta Quest Quick Launcher

## Build Commands
- Build debug APK: `./gradlew assembleDebug`
- Build release APK: `./gradlew assembleRelease`
- Full build: `./gradlew build`
- Clean build: `./gradlew clean build`

## Test Commands
- Run all tests: `./gradlew test`
- Run unit tests: `./gradlew testDebugUnitTest`
- Run single test: `./gradlew testDebugUnitTest --tests="*.TestClass.testMethod"`
- Run instrumentation tests: `./gradlew connectedDebugAndroidTest`

## Lint Commands
- Lint code: `./gradlew lint`
- Check style: `./gradlew ktlintCheck`
- Auto-fix style: `./gradlew ktlintFormat`

## Development Commands
- Run on connected device: `./gradlew installDebug`
- Generate signed APK: `./gradlew assembleRelease`
- Check dependencies: `./gradlew dependencies`

## Code Style Guidelines
- **Language**: Kotlin with Jetpack Compose
- **Imports**: Android SDK first, then third-party, then project packages
- **Naming**: camelCase for variables/functions, PascalCase for classes/interfaces
- **Types**: Use explicit types, prefer `val` over `var`, use nullable types judiciously
- **Error Handling**: Use try/catch for exceptions, Result/Sealed classes for async operations
- **Formatting**: 4-space indentation, max 120 chars per line, trailing commas in multi-line structures
- **Architecture**: MVVM pattern, dependency injection with Hilt, coroutines for async work

## Development Status

### ✅ Completed Features
- Android project structure with Kotlin and Gradle
- MVVM architecture with ViewModels and use cases
- Glass-morphic launcher UI with search functionality
- PackageManager integration for app discovery
- Fuzzy search implementation using FuzzyWuzzy
- App launching via Android intents
- GitHub repository with CI/CD pipeline

### 🔄 Next Priority Tasks
1. **Meta Spatial SDK Integration**: Implement true overlay functionality for Quest
2. **Gesture Triggers**: Add pinch and wrist tap gesture support
3. **Button Triggers**: Implement controller button double-tap activation
4. **Performance Optimization**: Ensure <200ms popup appearance and <20MB memory usage
5. **Unit Tests**: Write comprehensive tests for core functionality
6. **Data Persistence**: Add local storage for user preferences and shortcuts

### 📋 Future Enhancements
- Custom shortcuts and app categories
- Recent apps history
- Web shortcuts support
- Voice activation
- Floating widgets (weather, calendar)
- Deep links and app parameters