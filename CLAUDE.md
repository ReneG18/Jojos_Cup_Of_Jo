# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Android app "Jojo's Cup Of Jo" — a single-module Gradle project (`:app`) currently at the Android Studio
template stage: one `MainActivity`, a Hello World layout, and the two example tests. Package/namespace is
`com.example.jojos_cup_of_jo`.

## Commands

All commands run from the project directory (`~/AndroidStudioProjects/Jojos_Cup_Of_Jo`) via the
wrapper. Note this directory is **not** its own git repo — the enclosing repo is `/Users/rene`, and
no file in this project is tracked in it, so `git log` here shows unrelated home-directory history.

```bash
./gradlew assembleDebug          # build debug APK
./gradlew installDebug           # build + install on a connected device/emulator
./gradlew test                   # host-side unit tests (app/src/test)
./gradlew connectedAndroidTest   # instrumented tests (app/src/androidTest); needs a device
./gradlew lint                   # Android Lint; report at app/build/reports/lint-results-debug.html
./gradlew clean
```

Single unit test:

```bash
./gradlew testDebugUnitTest --tests 'com.example.jojos_cup_of_jo.ExampleUnitTest.addition_isCorrect'
```

Single instrumented test:

```bash
./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.jojos_cup_of_jo.ExampleInstrumentedTest
```

## Toolchain — read before touching build files

This project is on **AGP 9.4.1 / Gradle 9.6**, whose build DSL differs from the AGP 7/8 syntax most
examples online still use. Do not "fix" the build files back to the older forms:

- `compileSdk { version = release(37) }` — block form, not `compileSdk = 37`.
- `buildTypes { release { optimization { enable = false } } }` — replaces `isMinifyEnabled` /
  `proguardFiles`. R8 is currently **off** for release.
- R8 keep rules go in `app/src/main/keepRules/*.keep` (AGP concatenates every file in that directory);
  there is no `proguard-rules.pro`.
- `gradle/gradle-daemon-jvm.properties` pins the daemon to **JDK 25**, auto-provisioned through the
  foojay resolver declared in `settings.gradle.kts`. Compilation targets Java 11 source/target.
- The configuration cache is enabled in `gradle.properties`; build logic that reads state at execution
  time will fail the build rather than silently work.

Dependencies and plugin versions live in the version catalog at `gradle/libs.versions.toml` and are
referenced as `libs.*` — add them there, never as inline coordinate strings in `app/build.gradle.kts`.
`dependencyResolutionManagement` is `FAIL_ON_PROJECT_REPOS`, so repositories can only be declared in
`settings.gradle.kts`.

## UI stack

Classic **Views + XML**, not Compose: ConstraintLayout inside `app/src/main/res/layout/`, themed with
`Theme.Material3.DayNight.NoActionBar` (light in `values/themes.xml`, dark override in
`values-night/themes.xml`). `MainActivity` extends `AppCompatActivity`.

Sources are **Java**; no Kotlin plugin is applied, so a `.kt` file will not compile until
`org.jetbrains.kotlin.android` is added to the catalog and to `app/build.gradle.kts`. (`activity-ktx` is
on the classpath but only its Java-callable surface is usable today.)

`MainActivity` opts into edge-to-edge (`EdgeToEdge.enable`) and applies system-bar insets as padding to
the root view, which must keep the id `@+id/main` for that lookup to resolve. New screens that go
edge-to-edge need the same insets handling or content will sit under the status/navigation bars.
`androidx.core` (`ViewCompat`, `Insets`, `WindowInsetsCompat`) arrives transitively via `appcompat` and
is not declared directly.

`minSdk` is 28, so APIs above that need a version guard or desugaring.

## Naming quirk

The display name and Gradle root project name are `Jojo's_Cup_Of_Jo` — the apostrophe must stay escaped
(`\'`) in both `settings.gradle.kts` and `res/values/strings.xml`.
