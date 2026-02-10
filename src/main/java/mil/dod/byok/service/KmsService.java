package mil.dod.dha.byok.service;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import software.amazon.awssdk.core.SdkBytes;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.nio.ByteBuffer;
//import software.amazon.awssdk.*;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.services.kms.*;
import software.amazon.awssdk.services.kms.model.*;
import software.amazon.awssdk.regions.Region;
import org.springframework.beans.factory.annotation.Autowired;

import mil.dod.dha.byok.shield.platformencryption.*;
import software.amazon.awssdk.services.kms.paginators.ListKeysPublisher;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KmsService {
    private static final Logger logger = LoggerFactory.getLogger(KmsService.class);
    // Replace with your AWS credentials (or use IAM roles if running on AWS)
    @Autowired
    private final KmsClient kmsClient;
    // @Autowired
    // private final KmsAsyncClient kmsAsyncClient;

    @Autowired
    KeyMaterialWrapper keyWrapper;

    public KmsService(KmsClient kmsClient) {
        this.kmsClient = kmsClient;
    }
    // public KmsService(KmsAsyncClient kmsAsyncClient) {
    // this.kmsAsyncClient = kmsAsyncClient;
    // }

    // Create a KMS key
    public String createKey() {
        CreateKeyRequest createKeyRequest = CreateKeyRequest.builder().build();
        CreateKeyResponse response = kmsClient.createKey(createKeyRequest);
        return response.toString(); // Return the Key ID
    }

    // List keys in your account
    public List<String> listKeys() {
        // KmsAsyncClient kmsAsyncClient = this.kmsAsyncClient;
        ListKeysRequest listKeysRequest = ListKeysRequest.builder().limit(15).build();
        /*
         * The `subscribe` method is required when using paginator methods in the AWS
         * SDK
         * because paginator methods return an instance of a `ListKeysPublisher`, which
         * is
         * based on a reactive stream. This allows asynchronous retrieval of paginated
         * results as they become available. By subscribing to the stream, we can
         * process
         * each page of results as they are emitted.
         */
        // ListKeysPublisher keysPublisher =
        // kmsAsyncClient.listKeysPaginator(listKeysRequest);
        List<String> rlist = new ArrayList<String>();
        /*
         * CompletableFuture<Void> future = keysPublisher
         * .subscribe(r -> r.keys().forEach(key ->
         * // System.out.println("The key ARN is: " + key.keyArn() + ". The key Id is: "
         * + key.keyId())))
         * rlist.add(keyWrapper.WrapKey(key.keyId()))))
         * .whenComplete((result, exception) -> {
         * if (exception != null) {
         * System.err.println("Error occurred: " + exception.getMessage());
         * } else {
         * System.out.println("Successfully listed all keys.");
         * }
         * });
         * 
         * try {
         * future.join();
         * } catch (Exception e) {
         * System.err.println("Failed to list keys: " + e.getMessage());
         * }
         */
        ListKeysResponse lkresponse = kmsClient.listKeys(listKeysRequest);
        List<String> list = lkresponse.keys().stream()
                .map(KeyListEntry::keyId)
                .toList();
        // List<String> rlist = new ArrayList<String>();
        for (String key : list) {
            // logger.debug("Accessing - "+key);
            // System.out.println("Accessing - "+key);
            DescribeKeyRequest request = DescribeKeyRequest.builder()
                    .keyId(key)
                    .build();
            // Call AWS KMS to get key metadata
            DescribeKeyResponse response = kmsClient.describeKey(request);
            // System.out.println("Response - "+response);

            // Check if the key is enabled
            if (response.keyMetadata().keyState().toString().toUpperCase().equals("ENABLED")) {

                logger.debug("Key " + key + " is enabled");
                rlist.add(keyWrapper.WrapKey(key));
            } else {
                logger.debug("Key " + key + " is disabled");
            }
        }
        return rlist;
    }

    // Encrypt data using a KMS key
    public ByteBuffer encryptData(String keyId, String data) {
        EncryptRequest encryptRequest = EncryptRequest.builder()
                .keyId(keyId)
                .plaintext(SdkBytes.fromByteBuffer(ByteBuffer.wrap(data.getBytes())))
                .build();

        EncryptResponse response = kmsClient.encrypt(encryptRequest);
        return response.ciphertextBlob().asByteBuffer(); // Returns the encrypted data (ciphertext)
    }

    // Decrypt data using a KMS key
    public String decryptData(ByteBuffer ciphertext) {
        DecryptRequest decryptRequest = DecryptRequest.builder()
                // .ciphertextBlob(ciphertext)
                .build();

        DecryptResponse response = kmsClient.decrypt(decryptRequest);
        return new String(response.toString()); // Return the decrypted data as a string
    }

    public String getKey(String keyId) {
        for (String key : this.listKeys()) {
            if (key.equals(keyId))
                return keyWrapper.WrapKey(key);
        }
        return "";
    }

    public String getJSONKey(String keyId, String systemId) {
        // System.out.println("Accessing - "+key);
        DescribeKeyRequest request = DescribeKeyRequest.builder()
                .keyId(keyId)
                .build();
        // Call AWS KMS to get key metadata
        DescribeKeyResponse response = kmsClient.describeKey(request);
        // System.out.println("Response - "+response);

        // Check if the key is enabled
        if (response.keyMetadata().keyState().toString().toUpperCase().equals("ENABLED")) {

            logger.debug("Key " + keyId + " is enabled");

        } else {
            logger.debug("Key " + keyId + " is disabled");
        }
        return response.toString(); // Return the decrypted data as a string
    }

    public List<String> getAllJsonKeysForTheSystem(String systemId) {
        ListKeysRequest listKeysRequest = ListKeysRequest.builder().limit(15).build();

        List<String> rlist = new ArrayList<String>();

        ListKeysResponse lkresponse = kmsClient.listKeys(listKeysRequest);
        List<String> list = lkresponse.keys().stream()
                .map(KeyListEntry::keyId)
                .toList();
        // List<String> rlist = new ArrayList<String>();
        for (String key : list) {
            // logger.debug("Accessing - "+key);

            // System.out.println("Accessing - "+key);
            DescribeKeyRequest request = DescribeKeyRequest.builder()
                    .keyId(key)
                    .build();
            // Call AWS KMS to get key metadata
            DescribeKeyResponse response = kmsClient.describeKey(request);
            // System.out.println("Response - "+response);

            // Check if the key is enabled
            if (response.keyMetadata().keyState().toString().toUpperCase().equals("ENABLED")) {

                logger.debug("Key " + key + " is enabled");

            } else {
                logger.debug("Key " + key + " is disabled");
            }
            rlist.add(response.toString()); // Return the decrypted data as a string
        }
        return rlist;
    }
}
