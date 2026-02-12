# android-code-challenge
A Kotlin + Jetpack Compose application built as a clean-architecture sample. The project now follows a **feature-first, layered structure** with **MVVM** in the presentation layer and clear separation of responsibilities across data, domain, and UI.

## Highlights

- **Clean Architecture + MVVM** with feature-first packages.
- **Data layer** is responsible for network DTOs, mapping, and repositories.
- **Domain layer** contains business models and use cases.
- **Presentation layer** exposes UI state through ViewModels.
- **Optimized mapping** in the feed pipeline to avoid repeated list scans.
- **Hilt dependency injection** for app-wide wiring.

## Architecture at a glance

```
app/                          # Android entry points
core/                         # Networking utilities + API runner

data/
  posts/
    remote/model/             # Network DTOs (Account, User, Post, ...)
    mapper/                   # DTO -> domain model mappers
    repository/               # Repository implementations
  auth/                       # Credentials providers

domain/
  posts/
    model/                    # Core FeedPost model
    repository/               # Repository contracts
    usecase/                  # Use cases (GetPostsUseCase)

presentation/
  posts/                      # Compose UI, ViewModel, UI state

ui/theme/                     # Material theme
```

## Configuration

Add the following properties in **one** of these locations:
- `~/.gradle/gradle.properties`
- Project-level `local.properties` or `gradle.properties`

```properties
CHALLENGE_BASE_URL=https://northamerica-northeast1-league-engineering-hiring.cloudfunctions.net/mobile-challenge-api/
CHALLENGE_API_USERNAME=hello
CHALLENGE_API_PASSWORD=world
```

> Credentials are intentionally not stored in source resources.


## Build & test

```bash
./gradlew :kotlin_app:test
./gradlew :kotlin_app:assembleDebug
```

## Notes for reviewers
- `MainActivity` hosts Compose and receives the `PostsViewModel` via Hilt.
- `NetworkPostsRepository` authenticates once, fetches remote collections, and uses the mapper to compose feed data.
- `FeedPostMapper` now precomputes lookup maps to avoid repeated list scans.

## Tech stack

- Kotlin
- Jetpack Compose + Material 3
- Coroutines + Flow
- Retrofit + Gson
- Hilt
- JUnit + kotlinx-coroutines-test
