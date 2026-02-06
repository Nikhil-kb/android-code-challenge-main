# android-code-challenge
This repo is for the Android coding interview for new developers :)

## What was improved

- Added **Hilt dependency injection** for app-wide dependency graph management.
- Refactored data mapping into a dedicated mapper (`FeedPostMapper`) to keep repository logic focused.
- Kept architecture layered:
    - `presentation` (Compose + ViewModel)
    - `domain` (use case + repository contract)
    - `data` (repository + mapper + credential provider)
    - `di` (Hilt modules and qualifiers)
- Removed hardcoded API credentials from resources.
- Added BuildConfig-based configuration for endpoint and credentials via Gradle properties.
- Added/updated unit tests for mapper and repository behavior.

## Configuration

Add these properties in either:
- `~/.gradle/gradle.properties`, or
- project `local.properties` / `gradle.properties`.

```properties
CHALLENGE_BASE_URL=https://northamerica-northeast1-league-engineering-hiring.cloudfunctions.net/mobile-challenge-api/
CHALLENGE_API_USERNAME=hello
CHALLENGE_API_PASSWORD=world
```

> Credentials are intentionally not stored in source resources.

## Tech stack

- Kotlin
- Jetpack Compose
- Material 3 theme from `ui.theme`
- Coroutines
- Retrofit + Gson converter
- Hilt (DI)
- JUnit + coroutines-test

## Build & test

```bash
./gradlew :kotlin_app:test
./gradlew :kotlin_app:assembleDebug
```

## Notes for reviewers

- `MainActivity` now receives `PostsViewModel` through Hilt (`hiltViewModel()`).
- `ChallengeApplication` initializes Hilt graph.
- `NetworkPostsRepository` validates credentials are present before login.
- `FeedPostMapper` has dedicated unit tests for avatar fallback behavior.
