package mil.dod.dha.byok;
import java.security.MessageDigest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import mil.dod.dha.byok.shield.platformencryption.*;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.*;
import javax.crypto.*;
import java.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import java.security.*;
import java.security.*;
import javax.crypto.*;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import static org.jose4j.jwa.AlgorithmConstraints.ConstraintType.WHITELIST;
@SpringBootTest
public class FipsRSAOAEP{
	@Test
	public void Test1(){
	try {
		 // 1. Generate RSA Key Pair (Private and Public Key)
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);  // Use a 2048-bit RSA key
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
	    Provider bcProvider = new BouncyCastleProvider();
            // 2. Set up the cipher for encryption (RSA/OAEP)
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding",bcProvider);
            
            // 3. Encrypt a message using the public key
            String message = "This is a test message for RSA-OAEP encryption!";
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());

            // Output encrypted message as Base64 string
            String encryptedMessageBase64 = Base64.getEncoder().encodeToString(encryptedMessage);
            System.out.println("Encrypted Message (Base64): " + encryptedMessageBase64);

            // 4. Decrypt the message using the private key
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

            // Output the decrypted message
            String decryptedMessageStr = new String(decryptedMessage);
            System.out.println("Decrypted Message: " + decryptedMessageStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	 @Test
        public void Test2(){
        try {
                 // 1. Generate RSA Key Pair (Private and Public Key)
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);  // Use a 2048-bit RSA key
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            Provider bcProvider = new BouncyCastleProvider();
Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            // 2. Set up the cipher for encryption (RSA/OAEP)
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding",bcProvider);

            // 3. Encrypt a message using the public key
            String message = "This is a test message for RSA-OAEP encryption!";
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());

            // Output encrypted message as Base64 string
            String encryptedMessageBase64 = Base64.getEncoder().encodeToString(encryptedMessage);
            System.out.println("Encrypted Message (Base64): " + encryptedMessageBase64);

            // 4. Decrypt the message using the private key
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

            // Output the decrypted message
            String decryptedMessageStr = new String(decryptedMessage);
            System.out.println("Decrypted Message: " + decryptedMessageStr);


            JsonWebEncryption jwe = new JsonWebEncryption();
            jwe.setAlgorithmConstraints(new AlgorithmConstraints(WHITELIST, KeyManagementAlgorithmIdentifiers.RSA_OAEP));
            jwe.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(WHITELIST, ContentEncryptionAlgorithmIdentifiers.AES_256_GCM));

            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
	    String kid="123";
	    String key="123Key";
            String compactSerialization = null;
            jwe.setKeyIdHeaderValue(kid);
            jwe.setKey(keyPair.getPublic());
            jwe.setPlaintext(key);
            compactSerialization = jwe.getCompactSerialization();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
}
