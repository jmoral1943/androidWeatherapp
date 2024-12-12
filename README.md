# Weather App

## Overview
The Weather App is an Android application that provides weather updates for cities and locations using the OpenWeatherMap API. The app is built with a modern Android development stack, focusing on Clean Architecture principles, and leveraging Jetpack Compose for UI and Navigation.

---

## Features
- Fetch current weather data for a location.
- Fetch weather for a 5-day forecast
- Search for cities using the OpenWeatherMap Geocoding API.
- Display weather data including temperature, highs, and lows.
- Save favorite cities to a local database using Room DB.
- Modern and responsive UI built with Jetpack Compose.

---

## Tech Stack

### **Languages and Frameworks**
- **Kotlin**: The primary programming language.
- **Jetpack Compose**: For building modern, declarative UI.

### **Networking**
- **Retrofit**: To handle API requests.
- **OpenWeatherMap API**: For weather data and geocoding.

### **Architecture**
- **Clean Architecture**: Organized into layers:
  - **Presentation**: Handles UI and user interactions (Jetpack Compose).
  - **Domain**: Contains business logic and use cases.
  - **Data**: Manages API calls and local database (Room).

### **Database**
- **Room DB**: For storing and retrieving favorite cities and their weather data.

### **Navigation**
- **Jetpack Compose Navigation**: For managing app screens.

---

## API Integration

The app uses the OpenWeatherMap API for weather data and city search:
- **Current Weather API**: Retrieves weather information for a given latitude and longitude.
- **Geocoding API**: Searches for cities and provides their coordinates.

### API Key Setup
To use the OpenWeatherMap API, follow these steps:
1. Obtain an API key from the [OpenWeatherMap website](https://openweathermap.org/api).
2. Locate your `gradle.properties` file:
   - On most systems, it can be found at `/Users/YOUR_NAME/.gradle/`. If it does not exist, you can create it manually in this directory.
3. Add your API key to the file:
   ```properties
   OPEN_WEATHER_MAP_API_KEY=your_api_key_here

