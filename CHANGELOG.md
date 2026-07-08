# Changelog - Flixclusive Debug Build

## [2.2.0-debug] - 2026-07-08

### 🔧 Bug Fixes
- **Fixed NullPointerException** in `GetMediaLinksUseCase` (#147)
  - Null check for `tmdbId` before fetching watch providers
  - Safe handling of WebView instantiation
- **Fixed "Failed to find suitable header item"** error on home screen
  - Simplified header selection to use `FilmSearchItem` directly
  - Increased retry attempts from 20 to 50
  - Reduced minimum vote count threshold from 250 to 50

### ⚡ Optimizations
- **English-only build** - Removed other languages to reduce APK size
- **arm64-only build** - Only built for ARM64 architecture (your device)

### 🎁 Features
- **Pre-configured TMDB API key** - No manual setup needed
- **Debug build** - Includes crash logging for debugging

### 📝 Documentation
- Added fork notice and vibe-coded disclaimer to README
- Added CHANGELOG.md

### 🏗️ Build System
- Simplified GitHub Actions workflow (skip tests for faster builds)
- Added Android SDK setup step
- Added APK artifact upload
- Fixed JAVA_HOME configuration

---

## Previous Changes (from upstream)
- See [flixclusiveorg/Flixclusive](https://github.com/flixclusiveorg/Flixclusive) for original changelog

---

## 🐛 Known Issues
- Provider loading may fail if no providers are configured
- Some UI elements may not render correctly on older Android versions

## 📱 Installation
1. Download `flixclusive-debug.apk` from [Releases](https://github.com/buddhistblueberry/Flixclusive/releases)
2. Enable "Install from unknown sources" in Settings
3. Open the APK and click Install
