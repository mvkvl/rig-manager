package ws.slink.mine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ws.slink.mine.error.NotAuthenticatedException;
import ws.slink.mine.model.ServiceRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    Environment env;

    public AuthInterceptor(Environment env) {
        this.env = env;
    }

    @Value("${time.delta}")
    private long timeDelta;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String jsonStr = request.getReader()
                                .lines()
                                .collect(Collectors.joining(System.lineSeparator()));
        ServiceRequest sreq = new ObjectMapper().readValue(jsonStr, ServiceRequest.class);
        logger.trace("REQUEST: {}", sreq);
//        System.out.println("---------- interceptor ---------");
//        System.out.println(sreq);
//        System.out.println("--------------------------------");

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String curTimeStr = dateFormatGmt.format(new java.util.Date());

        long curTS = dateFormatGmt.parse(curTimeStr).getTime();
        long reqTS = dateFormatGmt.parse(sreq.time).getTime();

        logger.trace("cur time: {}", curTimeStr);
        logger.trace("req time: {}", sreq.time);

        logger.trace("curTS: {}", curTS);
        logger.trace("reqTS: {}", reqTS);

//        System.out.println(curTS);
//        System.out.println(reqTS);

        String hmac = getHMAC(sreq.time, sreq.client);

        logger.trace("c.hmac: {}", hmac);
        logger.trace("g.hmac: {}", sreq.hmac);

        if (!hmac.equals(sreq.hmac) ||
            (Math.abs(curTS - reqTS) > timeDelta * 1000)) {
            throw new NotAuthenticatedException("command not authenticated");
        }

        return true;
    }

    @Override public void postHandle( HttpServletRequest request, HttpServletResponse response,
                            Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("---method executed---");
    }
    @Override public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
//        System.out.println("---Request Completed---");
    }

    private String getHMAC(String timeStr, String client) {
        String key = env.getProperty("keys.clients." + client);
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key)
                  .hmacHex(String.format("%s.%s", client, timeStr));
    }

}

