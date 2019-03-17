package ws.slink.mine.tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

public class RequestTools {

    public static HttpEntity getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 Firefox/26.0");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        return entity;
    }
}
