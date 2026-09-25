# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Android app "Jojo's Cup Of Jo" — a coffee shop app, single-module Gradle project (`:app`). Five
bottom-nav tabs: Home, Menu, Merch, Team, Cart. Package/namespace is `com.example.jojos_cup_of_jo`.

<!-- TODO: describe the product intent — who this is for, what a customer is meant to do with it. -->

Planned beyond this app: a web version and an in-store desktop POS, all three eventually sharing one
server-side database (sales, inventory, employees). Nothing in this repo talks to a network yet —
all content comes from `data/SampleDataProvider`.

<!-- TODO: link the database schema here once it's typed up. -->

## Commands

All commands run from the project directory (`~/AndroidStudioProjects/Jojos_Cup_Of_Jo`) via the
wrapper. This directory is its own git repo, pushed to `github.com/ReneG18/Jojos_Cup_Of_Jo`.

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
./gradlew testDebugUnitTest --tests 'com.example.jojos_cup_of_jo.data.CartRepositoryTest'
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

## Architecture

```
com/example/jojos_cup_of_jo/
├── MainActivity.java          — hosts the 5 tabs, implements TabHost
├── data/
│   ├── SampleDataProvider.java  — all app content, hardcoded (the seam for a real data source)
│   └── CartRepository.java      — in-memory cart singleton
├── model/                     — Product, CartItem, TeamMember, StoreInfo + two enums
└── ui/
    ├── TabHost.java           — interface letting a Fragment request a tab switch
    ├── home/, product/, team/, cart/, util/
```

**Navigation is hand-rolled, not Jetpack Navigation.** `MainActivity` holds a static
`LinkedHashMap<Integer, Supplier<Fragment>>` of tab id → fragment factory, `add`s all five fragments
once, then toggles them with `hide()`/`show()`. Consequences worth knowing:

- Fragments are **never destroyed on tab switch**, so `onViewCreated` runs once per app launch. Data
  edits in `SampleDataProvider` need a full app restart to show, not just a tab switch.
- Data loading is **synchronous on the main thread**. Fine while content is hardcoded; it is the thing
  to fix first when a real data source arrives.
- A fragment requests a tab switch via the `TabHost` interface (`HomeFragment`'s seasonal cards,
  `CartFragment`'s empty-state CTA) rather than depending on `MainActivity` directly.

`SampleDataProvider` is a static utility with no state and no `Context`; every method rebuilds its list
from literals. `CartRepository` is a process-lifetime static singleton with a hand-rolled listener list
(`CartListener`) — **no persistence at all**, so closing the app loses the cart. It deliberately imports
nothing from `android.*` so it stays host-unit-testable.

**No ViewModels, no LiveData, no `androidx.lifecycle`** anywhere yet.

### Menu and Merch lists

Both tabs are the same `ProductListFragment`, switched by a `ProductType` in a fragment argument.
`ProductSections.build()` turns a flat product list into headed sections — seasonal items first, then
one section per `ProductCategory`, in first-appearance order — and `ProductAdapter` renders the result
as two view types (`ProductListRow.TYPE_HEADER` / `TYPE_PRODUCT`).

A product is featured on Home by setting `seasonal = true` on it in `SampleDataProvider`; the Home
getters find it by that flag rather than duplicating the literal. Home hides the card if nothing is
marked seasonal.

<!-- TODO: note anything about Cart/checkout flow worth remembering. -->

## UI stack

Classic **Views + XML**, not Compose: ConstraintLayout/LinearLayout inside `app/src/main/res/layout/`,
themed with `Theme.Material3.DayNight.NoActionBar` (light in `values/themes.xml`, dark override in
`values-night/themes.xml`). `MainActivity` extends `AppCompatActivity`. **ViewBinding** is enabled
(`buildFeatures { viewBinding = true }`) and used by every fragment and adapter; fragments null out
`binding` in `onDestroyView`.

Products render as real photographs: `ui/util/ProductArt.photoRes()` maps a product id to a 600x600
JPEG in `res/drawable-nodpi/photo_*.jpg`, shown `centerCrop` inside a `MaterialCardView` so the corners
match the surrounding cards. Sources and licences are in `PHOTO_CREDITS.md` at the repo root; unknown
ids fall back to `ic_product_placeholder`. Team avatars still use the colored-swatch-plus-initials
placeholder from `ui/util/PlaceholderStyle`, which is now that class's only caller.

<!-- TODO: if you add real product photos, say where they live and how they're keyed to products. -->

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

The Gradle root project name is `Jojo\'s_Cup_Of_Jo` (underscores) while the user-facing `app_name` in
`res/values/strings.xml` is `Jojo\'s Cup Of Jo` (spaces). These deliberately differ — don't "fix" one
to match the other. The apostrophe must stay escaped (`\'`) in both files.

<!-- TODO: anything else future-you will forget — conventions, gotchas, things you tried that didn't work. -->
