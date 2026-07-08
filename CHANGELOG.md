# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Fixed
- PlayerTweakScreen crash when changing Resize Mode (`#2`)
  - Missing `.value` for `selectedResizeMode.uiText`
  - `IllegalArgumentException: Unsupported type`

### Changed
- Improved retry logic for header loading (20 → 50 attempts)
- Lowered minimum vote count threshold for content filtering

---

## [1.0.0] - 2026-07-08

### Added
- Initial debug fork release
- Pre-configured TMDB API key
- GitHub Actions CI/CD pipeline
- Auto-release workflow
- Crash reporting to GitHub Issues
- English-only build (reduced APK size)
- ARM64-only build (optimized for modern devices)

### Fixed
- NullPointerException in `GetMediaLinksUseCase` (`#147`)
  - Null check for `tmdbId` before fetching watch providers
  - Safe handling of WebView instantiation
- "Failed to find suitable header item" error on home screen
  - Simplified header selection to use `FilmSearchItem` directly

### Changed
- Increased retry attempts from 20 to 50 for header loading
- Reduced minimum vote count threshold from 250 to 50

---

## [0.0.1] - 2026-07-08

### Added
- Forked from [flixclusiveorg/Flixclusive](https://github.com/flixclusiveorg/Flixclusive)
- Initial setup and configuration

[Unreleased]: https://github.com/buddhistblueberry/Flixclusive/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/buddhistblueberry/Flixclusive/releases/tag/v1.0.0
[0.0.1]: https://github.com/buddhistblueberry/Flixclusive/releases/tag/v0.0.1
