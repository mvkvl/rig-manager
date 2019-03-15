package ws.slink.mine.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ws.slink.mine.type.Crypto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PriceInfo {

    private Crypto crypto;
    private Map<String, Double> prices = new HashMap<>();

    public PriceInfo() {
    }
    public PriceInfo(Crypto crypto) {
        this.crypto = crypto;
    }

    public Crypto getCrypto() {
        return crypto;
    }
    public Map<String, Double> getPrices() {
        return prices;
    }
    public Double getPrice(String currency) {
        return prices.get(currency);
    }
    public Double setPrice(String currency, Double value) {
        return prices.put(currency, value);
    }
    public String toString() {
        return crypto.toString() + ": " + prices;
    }

    public PriceInfo crypto(Crypto crypto) {
        this.crypto = crypto;
        return this;
    }
    public PriceInfo prices(Map<String, Double> prices) {
        this.prices = prices;
        return this;
    }
    public PriceInfo prices(String pricesJsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> map = new HashMap<>();
        try {
            this.prices = mapper.readValue(pricesJsonStr, new TypeReference<Map<String, Double>>(){});
        } catch (IOException e) {
            this.prices = new HashMap<>();
            e.printStackTrace();
        }
        return this;
    }
}
