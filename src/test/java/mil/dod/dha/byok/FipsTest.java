package mil.dod.dha.byok;
import java.security.MessageDigest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import mil.dod.dha.byok.shield.platformencryption.*;
import javax.crypto.SecretKey;
import java.security.*;
@SpringBootTest
public class FipsTest {
	@Test
    public  void Test1() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest("test".getBytes("UTF-8"));
        System.out.println(jakarta.xml.bind.DatatypeConverter.printHexBinary(hash));
    }
    @Test
    public  void Test2() throws Exception {
	KeyMaterialGenerator kgen = new KeyMaterialGenerator();
	  // Generate a 256-bit AES key
        SecretKey secretKey = kgen.generateAESKey();

        // Load RSA public key from a certificate file
        PublicKey publicKey =kgen.loadPublicKeyFromCert("keystore.jks","password","mycert");
        if(publicKey!=null){
        // Encrypt the AES key using RSA with SHA-1 OAEP padding
        String encryptedKeySHA1 = kgen.encryptKey(secretKey, publicKey, "SHA-1");      
        // String encryptedKeySHA1 = kgen.encryptKey(secretKey, publicKey, "SHA-256");

        // Encrypt the AES key using RSA with SHA-512 OAEP padding
        String encryptedKeySHA512 = kgen.encryptKey(secretKey, publicKey, "SHA-512");

        System.out.println("Encrypted Key (SHA-1 OAEP): " + encryptedKeySHA1);
        System.out.println("Encrypted Key (SHA-512 OAEP): " + encryptedKeySHA512);
       }
   }

}

