package com.dod.dha.byok;
import org.springframework.beans.factory.annotation.Autowired;
import mil.dod.dha.byok.service.KmsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.kms.*;
import software.amazon.awssdk.services.kms.model.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import software.amazon.awssdk.services.kms.KmsClient;
import mil.dod.dha.byok.shield.platformencryption.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
@SpringBootTest
public class KMSServiceTest{

 @Autowired
    private KmsService kmsService;

    @MockBean
    private KmsClient kmsClient; // Assuming you have a KMSClient interface
/*
 @Test
    public void testEncryptData() {
        String plaintext = "sensitive data";
        String ciphertext = "encrypted data";

        // Mock the KMS client behavior
        when(kmsClient.encrypt(plaintext)).thenReturn(ciphertext);
	
        // Call the service method
        String result = kmsService.encryptData(kmsService.createKey(),plaintext);

        // Assert the result
        assertEquals(ciphertext, result);
    } */
 public  String convertByteBufferToString(ByteBuffer buffer, Charset charset) {
        byte[] bytes;
        if (buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes, charset);
    }
/*    @Test
    public  void Test1() throws Exception {
      assertNotNull(kmsService.listKeys());
	for(String s :kmsService.listKeys()){
	KeyMaterialWrapper wrapper =new KeyMaterialWrapper();
         System.out.println("kid-"+s); 
	wrapper.WrapKey(s);
         }
    }*/
}
