# Jojo's Cup Of Jo

[Add a one or two sentence description of the app here.]

## Screenshots

[Add screenshots or a screen recording here, e.g.:]

| Home | Menu | Cart |
| --- | --- | --- |
| ![Home](docs/screenshots/home.png) | ![Menu](docs/screenshots/menu.png) | ![Cart](docs/screenshots/cart.png) |

## Features

- [ ] [Feature one]
- [ ] [Feature two]
- [ ] [Feature three]

## Tech Stack

- Android, Java, Views + XML (Material3, ViewBinding)
- Single-module Gradle project (`:app`), AGP 9.4.1 / Gradle 9.6
- No backend yet — all content comes from `SampleDataProvider`

## Getting Started

### Prerequisites

- [Android Studio version]
- JDK 25 (auto-provisioned via the foojay resolver)
- minSdk 28

### Build & Run

```bash
./gradlew installDebug   # build + install on a connected device/emulator
```

See `CLAUDE.md` for the full command reference (tests, lint, single-test invocations).

## Project Structure

```
com/example/jojos_cup_of_jo/
├── MainActivity.java          — hosts the 5 tabs, implements TabHost
├── data/                      — SampleDataProvider, CartRepository
├── model/                     — Product, CartItem, TeamMember, StoreInfo
└── ui/                        — home/, product/, team/, cart/, util/
```

[Expand this section if you want more detail than CLAUDE.md's architecture notes.]

## Roadmap

[Add planned work here, e.g.:]

- [ ] Web version
- [ ] In-store desktop POS
- [ ] Shared server-side database (sales, inventory, employees)

## Contributing

[Add contribution guidelines here, or remove this section if the repo is solo/private.]

## License

[Add license here.]

## Contact

[Add contact info here.]
