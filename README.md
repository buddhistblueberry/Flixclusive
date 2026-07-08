![Flixclusive](https://i.imgur.com/tizcKbi.png)

<div align="center">

# Flixclusive

**A free, open-source media player and stream aggregator for Android.**

[![Debug Build](https://img.shields.io/github/actions/workflow/status/buddhistblueberry/Flixclusive/debug-build.yml?branch=main&style=flat-square&label=Build)](https://github.com/buddhistblueberry/Flixclusive/actions)
[![License](https://img.shields.io/github/license/buddhistblueberry/Flixclusive?style=flat-square)](https://github.com/buddhistblueberry/Flixclusive/blob/main/LICENSE)
[![Release](https://img.shields.io/github/v/release/buddhistblueberry/Flixclusive?style=flat-square)](https://github.com/buddhistblueberry/Flixclusive/releases)

[Download](https://github.com/buddhistblueberry/Flixclusive/releases/latest) • [Discord](https://discord.gg/7yPSPveReu) • [Original Project](https://github.com/flixclusiveorg/Flixclusive)

</div>

---

## ⚠️ Fork Notice

This is a **debug fork** of [flixclusiveorg/Flixclusive](https://github.com/flixclusiveorg/Flixclusive), maintained for testing and bug fixing.

If you enjoy Flixclusive, please support the original project by starring the repo and joining their Discord.

---

## Features

- 🎬 **Stream Aggregation** — Search and discover content from multiple sources
- 📱 **Material Design** — Modern UI built with Jetpack Compose
- 🔧 **Customizable Player** — Adjust playback settings to your preference
- 🌐 **Provider System** — Extensible architecture for adding content sources
- 📊 **Watch History** — Track what you've watched
- 🔖 **Favorites** — Save content for later
- 🌙 **Dark Mode** — Easy on the eyes

## Installation

1. Download the latest APK from [Releases](https://github.com/buddhistblueberry/Flixclusive/releases/latest)
2. Enable **Install from unknown sources** in your device settings
3. Open the APK and tap Install

> **Note:** This build is optimized for ARM64 devices (most modern Android phones).

## Building from Source

```bash
# Clone the repository
git clone https://github.com/buddhistblueberry/Flixclusive.git
cd Flixclusive

# Build debug APK
./gradlew :app-mobile:assembleDebug
```

**Requirements:**
- JDK 17+
- Android SDK (API 34+)
- Gradle 9.1.0+

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Kotlin |
| UI | Jetpack Compose |
| DI | Hilt |
| Build | Gradle (Kotlin DSL) |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 |

## Project Structure

```
Flixclusive/
├── app-mobile/          # Main Android app module
├── core/                # Core utilities and common code
├── data/                # Data layer (repositories, data sources)
├── domain/              # Domain layer (use cases, models)
├── feature/             # Feature modules (ui screens)
├── build-logic/         # Gradle build conventions
└── gradle/              # Gradle wrapper and config
```

## Contributing

This is a debug fork focused on stability fixes. For contributions to the main project, visit [flixclusiveorg/Flixclusive](https://github.com/flixclusiveorg/Flixclusive).

For bug reports specific to this fork, use the [Issues](https://github.com/buddhistblueberry/Flixclusive/issues) page.

## Disclaimer

This app **does not** host or provide any media content. It is a tool for organizing and accessing publicly available streams.

## License

See [LICENSE](LICENSE) for details.

---

<div align="center">

**Built with ❤️ by the community**

[Original Project](https://github.com/flixclusiveorg/Flixclusive) • [Discord](https://discord.gg/7yPSPveReu)

</div>
