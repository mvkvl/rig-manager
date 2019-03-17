package ws.slink.mine.influxdb;

import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;
import ws.slink.mine.type.Crypto;

@Service
public class InfluxDBReader {

    private static final Logger logger = LoggerFactory.getLogger(InfluxDBReader.class);

    @Value("${influxdb.metric.balance.wallet}")
    private String balanceKey;

    @Value("${influxdb.metric.price}")
    private String priceKey;

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    public double cryptoBalance(Crypto crypto, String wallet) {
        final Query q = new Query("SELECT last(\"" + wallet + "\") FROM \"" + balanceKey + "\" WHERE \"crypto\" = '" + crypto + "'", influxDBTemplate.getDatabase());
        QueryResult queryResult = influxDBTemplate.query(q);
        try {
            return Double.parseDouble(queryResult.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return 0.0;
    }

    public double cryptoPrice(Crypto crypto, String currency) {
        final Query q = new Query("SELECT last(\"" + currency.toLowerCase() + "\") FROM \"" + priceKey + "\" WHERE \"crypto\" = '" + crypto + "'", influxDBTemplate.getDatabase());
        QueryResult queryResult = influxDBTemplate.query(q);
        try {
            return Double.parseDouble(queryResult.getResults().get(0).getSeries().get(0).getValues().get(0).get(1).toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return 0.0;
    }

}
