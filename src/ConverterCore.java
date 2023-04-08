import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.io.BufferedReader;
import com.google.gson.Gson;

public class ConverterCore {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    public BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    public ConverterCore() {
        Map<String, Object> value = this.getUserInput();
        int amount = (int) value.get("amount");
        Map<String, Double> response = this.getExchangeRates((String) value.get("fromCurrency"));
        System.out.println(response.get(value.get("toCurrency")) * amount);
    }

    public Map<String, Object> getUserInput() {
        String fromCurrency = "", toCurrency = "";
        int fromAmount = 0;

        try {
            System.out.println("Please enter currency code from which you want to convert from: ");
            fromCurrency = this.console.readLine();
            System.out.println("Please enter currency code to which you want to convert: ");
            toCurrency = this.console.readLine();
            System.out.println("Please enter " + fromCurrency + " amount: ");
            fromAmount = Integer.parseInt(this.console.readLine());
        } catch (IOException exception) {
            System.out.print(exception.toString());
        }

        return Map.of("fromCurrency", fromCurrency,"amount", fromAmount, "toCurrency", toCurrency);
    }

    public Map<String, Double> getExchangeRates(String currency) {
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://open.er-api.com/v6/latest/" + currency)).GET().build();
            response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(URISyntaxException | InterruptedException | IOException exception) {
            System.out.println(exception.toString());
        }
        return (Map) gson.fromJson(response.body(), Map.class).get("rates");
    }
}
