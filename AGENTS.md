# Agent Guidelines for Meta Quest Quick Launcher

## Build Commands
- Build debug APK: `./gradlew assembleDebug`
- Build release APK: `./gradlew assembleRelease`
- Full build: `./gradlew build`

## Test Commands
- Run all tests: `./gradlew test`
- Run unit tests: `./gradlew testDebugUnitTest`
- Run single test: `./gradlew testDebugUnitTest --tests="*.TestClass.testMethod"`

## Lint Commands
- Lint code: `./gradlew lint`
- Check style: `./gradlew ktlintCheck`
- Auto-fix style: `./gradlew ktlintFormat`

## Code Style Guidelines
- **Language**: Kotlin with Jetpack Compose
- **Imports**: Android SDK first, then third-party, then project packages
- **Naming**: camelCase for variables/functions, PascalCase for classes/interfaces
- **Types**: Use explicit types, prefer `val` over `var`, use nullable types judiciously
- **Error Handling**: Use try/catch for exceptions, Result/Sealed classes for async operations
- **Formatting**: 4-space indentation, max 120 chars per line, trailing commas in multi-line structures
- **Architecture**: MVVM pattern, dependency injection with Hilt, coroutines for async work