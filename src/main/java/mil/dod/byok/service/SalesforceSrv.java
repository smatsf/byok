package mil.dod.dha.byok.service;
import mil.dod.dha.byok.utils.BearerTokenUtilities;
import mil.dod.dha.byok.utils.HttpUtils;
import mil.dod.dha.byok.models.*;
import mil.dod.dha.byok.config.*;
import mil.dod.dha.byok.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.security.MessageDigest;

import mil.dod.dha.byok.shield.platformencryption.*;
import javax.crypto.SecretKey;
@Slf4j
@RequiredArgsConstructor
@Service
public class SalesforceSrv{

 public static final String QUERY_PATH = "/services/data/v52.0/";

   // private final ContactEventPublisher contactEventPublisher;
    private final CloseableHttpClient closeableHttpClient;
    private final ObjectMapper objectMapper;
    private final SalesforceConfigurationProperties salesforceConfigurationProperties;
	public void test1() throws Exception{
	 SalesforceLoginResult salesforceLoginResult = BearerTokenUtilities.loginToSalesforce(closeableHttpClient, salesforceConfigurationProperties, objectMapper);
	System.out.println("Token - "+salesforceLoginResult.getInstanceUrl());
/*        URIBuilder builder = new URIBuilder(salesforceLoginResult.getInstanceUrl());
        builder.setPath(QUERY_PATH + "sobjects/Contact/" + id);

        HttpGet get = new HttpGet(builder.build());
        get.setHeader("Authorization", "Bearer " + salesforceLoginResult.getAccessToken());

        HttpResponse httpResponse = closeableHttpClient.execute(get);
        HttpUtils.checkResponse(httpResponse);
*/

		}
}


