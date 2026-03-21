# WeatherApp

A weather forecast Android app built with Kotlin, Jetpack Compose and Clean Architecture.

---

## Features

- Current day weather with UV index, precipitation, sunrise and sunset
- 7-day weekly forecast
- City list grouped by region

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| Async | Coroutines + StateFlow |
| DI | Hilt |
| Network | Retrofit + Open-Meteo API |

---

## Project Structure
```
WeatherApp/
├── app/              # Entry point
├── core_data/        # Data layer — Repository, API, Models
└── feature_weather/  # UI layer — Screens, ViewModel
```

---

## API

[Open-Meteo](https://open-meteo.com) — Free weather API, no API key required.

---

## AI Tools Used

This project was built with the assistance of **Claude (Anthropic)**.

| Area | Details |
|---|---|
| Architecture | Clean Architecture structure and module setup |
| Code | Repository, ViewModel and Compose UI boilerplate |
| Debugging | Gradle build errors and navigation issues |
| UI Design | Japanese-inspired dark blue theme |

All generated code was reviewed and understood before implementation.
