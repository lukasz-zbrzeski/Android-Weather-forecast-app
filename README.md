# Android Weather Forecast App

Mobilna aplikacja pogodowa na Androida napisana w **Kotlinie** z użyciem **Jetpack Compose**.  
Aplikacja pobiera aktualną pogodę i prognozę dla wybranego miasta, zapisuje dane lokalnie do trybu offline oraz umożliwia konfigurację odświeżania i jednostek.  

## Opis projektu

Projekt pozwala użytkownikowi sprawdzać:
- aktualną pogodę dla wybranego miasta
- prognozę pogody na kolejne dni
- dodatkowe informacje pogodowe, takie jak wilgotność, ciśnienie i wiatr

Aplikacja obsługuje także:
- zapis ulubionych lokalizacji
- zapamiętywanie ostatnio wybranego miasta
- automatyczne odświeżanie danych
- zapis danych pogodowych do plików lokalnych
- działanie awaryjne bez internetu na podstawie ostatnio zapisanych danych

## Funkcje

### Pogoda i prognoza
- pobieranie aktualnej pogody dla miasta
- pobieranie prognozy pogody
- prezentacja temperatury, opisu pogody, ciśnienia, wilgotności i wiatru
- wyświetlanie ikon pogodowych
- prognoza prezentowana w uproszczonym widoku dziennym

### Ustawienia
- wybór jednostek
- wybór interwału automatycznego odświeżania
- ręczne odświeżanie danych
- zapis konfiguracji użytkownika w `DataStore`

### Miasta
- lista ulubionych lokalizacji
- zapamiętywanie ostatnio wybranego miasta

### Tryb offline
- wykrywanie braku internetu
- odczyt wcześniej zapisanych danych pogodowych z plików lokalnych
- komunikat o możliwej nieaktualności danych

### Automatyczne odświeżanie
- odświeżanie w tle z użyciem `WorkManager`
- cykliczne pobieranie pogody według wybranego interwału

## Technologie

- **Kotlin**
- **Jetpack Compose**
- **Material / Material 3**
- **Navigation Compose**
- **Retrofit**
- **Gson**
- **DataStore Preferences**
- **WorkManager**
- **Coil**
- **Accompanist Pager**

## Najważniejsze elementy projektu

- `MainActivity` – uruchamia aplikację, inicjuje `WeatherViewModel` i nawigację
- `MainScreen` – ekran główny z danymi pogodowymi
- `SettingsScreen` – ekran ustawień aplikacji
- `WeatherViewModel` – pobiera pogodę i prognozę, obsługuje cache i odświeżanie
- `WeatherWorker` – cykliczna aktualizacja pogody w tle
- `PreferencesManager` – zapis ustawień i ulubionych lokalizacji
- `WeatherStorage` – zapis i odczyt danych pogodowych z pamięci lokalnej
- `RetrofitInstance` / `WeatherApi` – warstwa komunikacji z API

## Źródło danych pogodowych

Aplikacja korzysta z endpointów:
- `weather`
- `forecast`

udostępnianych przez API OpenWeather przez `Retrofit`.

## Uruchomienie projektu

1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/lukasz-zbrzeski/Android-Weather-forecast-app.git
   ```
2. Otwórz projekt w Android Studio.
3. Poczekaj na synchronizację Gradle.
4. Uruchom aplikację na emulatorze lub urządzeniu z Androidem 14 lub nowszym.
