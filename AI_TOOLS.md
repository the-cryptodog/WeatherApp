# AI Tools Used

## Tools

### Claude (Anthropic)
- Architecture design: MVVM + Clean Architecture module structure
- Code generation: All Kotlin source files including ViewModel, Repository, Compose UI
- API integration: Open-Meteo API setup and DTO design
- Navigation: Jetpack Navigation Compose with BottomBar setup

## API
- **Open-Meteo** (https://open-meteo.com): Free weather API, no API key required
- Endpoint: `https://api.open-meteo.com/v1/forecast`

## How AI Helped
Claude generated the initial architecture and boilerplate code.
All business logic, data flow (MVVM + StateFlow), and module structure
were reviewed and understood before implementation.