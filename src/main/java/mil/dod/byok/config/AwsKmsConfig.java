package mil.dod.dha.byok.config;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.kms.KmsAsyncClient;
@Configuration
public class AwsKmsConfig {

    @Value("${aws.region}")
    private String appRegion;
    @Bean
    public KmsClient kmsClient() {
        return KmsClient.builder()
                        .region(Region.of(appRegion)) // Replace with your region
                        //.credentialsProvider(DefaultCredentialsProvider.create())
			.credentialsProvider( ProfileCredentialsProvider.create("kms-user-profile"))
                        .build();
    }
    @Bean
    public KmsAsyncClient kmsAsyncClient() {
        return KmsAsyncClient.builder()
                        .region(Region.of(appRegion)) // Replace with your region
                        //.credentialsProvider(DefaultCredentialsProvider.create())
			.credentialsProvider( ProfileCredentialsProvider.create("kms-user-profile"))
                        .build();
    }
}

