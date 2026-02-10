package mil.dod.dha.byok;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.security.*;
import java.security.SecureRandom;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import mil.dod.dha.byok.shield.platformencryption.KeyMaterialGenerator;
import mil.dod.dha.byok.shield.platformencryption.util.KeyRepresentation;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.UUID;
@SpringBootTest
public class FipsAesCbcTest {
	@Test
    public  void Test1() throws Exception {
//Security.setProperty("crypto.policy", "limited");
        // Step 1: Generate an AES Key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // Use 256-bit keys for strong encryption (ensure JDK supports this key size)
        SecretKey secretKey = keyGen.generateKey();

        System.out.println("Generated AES Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

        // Step 2: Generate an Initialization Vector (IV)
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16]; // AES block size is 16 bytes
        secureRandom.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        System.out.println("Generated IV: " + Base64.getEncoder().encodeToString(iv));

        // Step 3: Initialize the Cipher in AES/CBC/PKCS5Padding Mode
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Step 4: Encrypt a Sample Plaintext
        String plaintext = "This is a FIPS-compliant AES CBC test.";
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));

        System.out.println("Encrypted Ciphertext: " + Base64.getEncoder().encodeToString(ciphertext));

        // Step 5: Decrypt the Ciphertext
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decryptedText = cipher.doFinal(ciphertext);

        System.out.println("Decrypted Text: " + new String(decryptedText, "UTF-8"));
    }
    @Test
    public  void Test2() throws Exception {
       
        KeyMaterialGenerator keyMaterialGenerator = new KeyMaterialGenerator();
        // Generate a 256-bit AES key
        SecretKey secretKey = keyMaterialGenerator.generateAESKey();
         
        // Load RSA public key from a certificate file
        PublicKey publicKey = keyMaterialGenerator.loadPublicKeyFromCert("keystore.jks","password","mycert");
        
        // Encrypt the AES key using RSA with SHA-1 OAEP padding
        String encryptedKeySHA1 = keyMaterialGenerator.encryptKey(secretKey, publicKey, "SHA-1");
        
        // Encrypt the AES key using RSA with SHA-512 OAEP padding
        String encryptedKeySHA512 = keyMaterialGenerator.encryptKey(secretKey, publicKey, "SHA-512");
        
        System.out.println("Encrypted Key (SHA-1 OAEP): " + encryptedKeySHA1);
        System.out.println("Encrypted Key (SHA-512 OAEP): " + encryptedKeySHA512);
	String kid= UUID.randomUUID().toString();
        KeyRepresentation rep =new KeyRepresentation(kid,secretKey.getEncoded(),publicKey);
	rep.getJwe();
    }
}


