package mil.dod.dha.byok.interceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.*;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);
    private final String loggedStartTimeKey = "_loggedStartingTime";
    @Override
    public boolean preHandle(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, Object handler) {
        System.out.println("Endpoint accessed: " + request.getRequestURI());
       long startTime = System.currentTimeMillis();
       request.setAttribute(loggedStartTimeKey, startTime);
       log.info("Request Started: method={} path={}", request.getMethod(), request.getRequestURI());
        return true;
    }

   @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
       long loggedStartTime = (long) request.getAttribute(loggedStartTimeKey);
       long endTime = System.currentTimeMillis();
       long timeTakenMs = endTime - loggedStartTime;
       log.info("Request Completed: method={} path={} timeTaken={} (milliseconds)", request.getMethod(), request.getRequestURI(), timeTakenMs);
   }
}
