package otherConcepts;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WeatherApp {

    public static void main(String[] args) {
        // Custom thread pool for handling async tasks.
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Step 1: Fetch current weather.
        CompletableFuture<String> currentWeatherFuture = CompletableFuture.supplyAsync(WeatherApp::fetchCurrentWeather, executor);

        // Step 2: Fetch weather forecast.
        CompletableFuture<String> weatherForecastFuture = CompletableFuture.supplyAsync(WeatherApp::fetchWeatherForecast, executor);

        // Step 3: Fetch air quality data.
        CompletableFuture<String> airQualityFuture = CompletableFuture.supplyAsync(WeatherApp::fetchAirQuality, executor);

        // Step 4: Combine all results
        CompletableFuture<String> weatherSummaryFuture = currentWeatherFuture
                .thenCombine(weatherForecastFuture, (currentWeather, forecast) ->
                        "Current Weather: " + currentWeather + "\nForecast: " + forecast
                )
                .thenCombine(airQualityFuture, (weatherSummary, airQuality) ->
                        weatherSummary + "\nAir Quality: " + airQuality
                );

        // Step 5: Handle result or exception
        weatherSummaryFuture
                .thenAccept(summary -> System.out.println("Weather Summary:\n" + summary))
                .exceptionally(ex -> {
                    System.err.println("Failed to fetch weather data: " + ex.getMessage());
                    return null;
                });

        // Wait for tasks to complete before shutting down
        try {
            weatherSummaryFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("Error waiting for tasks to complete: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    // Mock method to fetch current weather
    private static String fetchCurrentWeather() {
        simulateNetworkDelay();
        return "Sunny, 25°C";
    }

    // Mock method to fetch weather forecast
    private static String fetchWeatherForecast() {
        simulateNetworkDelay();
        return "Rainy tomorrow, 20°C";
    }

    // Mock method to fetch air quality
    private static String fetchAirQuality() {
        simulateNetworkDelay();
        return "AQI 50 (Good)";
    }

    // Simulates network delay
    private static void simulateNetworkDelay() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

