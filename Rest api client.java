import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {
    // Replace with your OpenWeatherMap API key
    private static final String API_KEY = "your_api_key_here";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        String city = "London";
        String response = fetchWeatherData(city);
        if (response != null) {
            parseAndDisplayWeather(response);
        } else {
            System.out.println("Error fetching weather data.");
        }
    }

    private static String fetchWeatherData(String city) {
        try {
            String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { // HTTP OK
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();
                return content.toString();
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parseAndDisplayWeather(String response) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);

            String cityName = (String) json.get("name");
            JSONObject main = (JSONObject) json.get("main");
            double temp = (double) main.get("temp");
            long humidity = (long) main.get("humidity");

            JSONObject weather = (JSONObject) ((org.json.simple.JSONArray) json.get("weather")).get(0);
            String description = (String) weather.get("description");

            System.out.println("Weather in " + cityName + ":");
            System.out.println("Temperature: " + temp + "°C");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Description: " + description);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
