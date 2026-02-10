package mil.dod.dha.byok.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import mil.dod.dha.byok.interceptor.*;
import org.apache.http.impl.client.CloseableHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.*;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
@Configuration
public class WebConfig implements WebMvcConfigurer 
{
    @Autowired
    private RequestInterceptor requestInterceptor;
//    @Autowired
//    private NonceInterceptor nonceInterceptor;

    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
	registry.addInterceptor(getLoggingInterceptor()).addPathPatterns("/**");
  //      registry.addInterceptor(nonceInterceptor).addPathPatterns("/**"); // Apply to all endpoints
    }
 @Bean
   public RequestInterceptor getLoggingInterceptor() {
       return new RequestInterceptor();
   }

   @Bean
   public ObjectMapper objectMapper() {
       return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
   }

   @Bean
   public CloseableHttpClient closeableHttpClient() {return HttpClients.createDefault();}

}


