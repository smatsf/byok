package mil.dod.dha.byok.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import mil.dod.dha.byok.config.SalesforceConfigurationProperties;
import mil.dod.dha.byok.models.SalesforceLoginResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class BearerTokenUtilities {
    private BearerTokenUtilities() { }

    private static final String TOKEN_URL =  "https://login.salesforce.com/services/oauth2/token";

    public static SalesforceLoginResult loginToSalesforce(CloseableHttpClient closeableHttpClient, SalesforceConfigurationProperties salesforceConfigurationProperties, ObjectMapper objectMapper) throws Exception {
        List<NameValuePair> loginParams = new ArrayList<>();
        loginParams.add(new BasicNameValuePair("client_id", salesforceConfigurationProperties.getConsumerKey()));
        loginParams.add(new BasicNameValuePair("client_secret", salesforceConfigurationProperties.getConsumerSecret()));
        loginParams.add(new BasicNameValuePair("grant_type", "client_credentials"));
        loginParams.add(new BasicNameValuePair("username", salesforceConfigurationProperties.getUsername()));
        loginParams.add(new BasicNameValuePair("password", salesforceConfigurationProperties.getPassword()));
        HttpPost post = new HttpPost(TOKEN_URL);
        post.setEntity(new UrlEncodedFormEntity(loginParams,java.nio.charset.StandardCharsets.UTF_8));

	System.out.println("Http - "+post);
        HttpResponse httpResponse = closeableHttpClient.execute(post);
	System.out.println("Httpres - "+httpResponse);
        SalesforceLoginResult salesforceLoginResult = objectMapper.readValue(httpResponse.getEntity().getContent(), SalesforceLoginResult.class);

//        log.debug("salesforceLoginResult={}", salesforceLoginResult);
        System.out.println("BT-salesforceLoginResult={}"+ salesforceLoginResult);
        return salesforceLoginResult;
    }
}


