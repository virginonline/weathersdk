# WeatherSDK — Quick Start


```java
import com.virginonline.weather.core.Mode;
import com.virginonline.weather.core.WeatherSDKFactory;
import com.virginonline.weather.core.WeatherSDK;
import com.virginonline.weather.model.WeatherResponse;

public class Main {
  public static void main(String[] args) {
    String apiKey = System.getenv("OPENWEATHER_API_KEY");
    if (apiKey == null) {
      System.err.println("Set OPENWEATHER_API_KEY environment variable");
      return;
    }

    WeatherSDK sdk = WeatherSDKFactory.getInstance(apiKey, Mode.ON_DEMAND);
    try {
      WeatherResponse r = sdk.getWeather("London");
      System.out.println(r);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      sdk.delete();
    }
  }
}
```