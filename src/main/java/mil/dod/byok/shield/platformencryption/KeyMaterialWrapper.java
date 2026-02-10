package mil.dod.dha.byok.shield.platformencryption;
import mil.dod.dha.byok.shield.platformencryption.util.*;
import org.apache.commons.cli.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.MGF1ParameterSpec;
//import java.security.spec.OAEPParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.PSource;
import java.io.*;
import org.springframework.context.*;
import java.net.*;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.services.kms.model.GenerateDataKeyRequest;
import software.amazon.awssdk.services.kms.model.GenerateDataKeyResponse;
import software.amazon.awssdk.services.kms.*;
import software.amazon.awssdk.services.kms.model.*;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.regions.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class KeyMaterialWrapper{
 private static final Logger logger = LoggerFactory.getLogger(KeyMaterialWrapper.class);
	PublicKey publicWrappingKey = null;
        String kid = null;
        CryptoUtils cryptoUtils = new CryptoUtils();
 @Autowired
    private final KmsClient kmsClient;
@Value("${aws.alias}")
String AWS_ALIAS ;
	String backupPath = File.separator+"home"+File.separator+"ec2-user"+File.separator+"keys"+File.separator;
 public KeyMaterialWrapper(KmsClient kmsClient) {
        this.kmsClient = kmsClient;
    }

	public String WrapKey(String kId){
	KeyRepresentation  keyRepresentation = null; 
                StringBuffer backup = new StringBuffer();
	File directory = new File(backupPath); // Replace with the actual path
	if (!directory.exists()) {
   	 if (directory.mkdirs()) { // Creates directory and parent directories if needed
        	logger.debug("Directory created successfully.");
    	} else {
        	logger.debug("Failed to create directory.");
        return  ""; // Stop if directory creation fails
    	}
	}
	this.kid = kId;
	 //Parse the public key used for RSA-OAEP wrapping
	try{

	KeyMaterialGenerator kgen = new KeyMaterialGenerator();
	  // Generate a 256-bit AES key
        SecretKey secretKey = kgen.generateAESKey();

        // Load RSA public key from a certificate file
         publicWrappingKey =kgen.loadPublicKeyFromCert("keystore.jks","password","mycert");
        if(publicWrappingKey!=null){
        // Encrypt the AES key using RSA with SHA-1 OAEP padding
        String encryptedKeySHA1 = kgen.encryptKey(secretKey, publicWrappingKey, "SHA-1");      
        // String encryptedKeySHA1 = kgen.encryptKey(secretKey, publicWrappingKey, "SHA-256");

        // Encrypt the AES key using RSA with SHA-512 OAEP padding
        String encryptedKeySHA512 = kgen.encryptKey(secretKey, publicWrappingKey, "SHA-512");

        logger.debug("Encrypted Key (SHA-1 OAEP): " + encryptedKeySHA1);
        logger.debug("Encrypted Key (SHA-512 OAEP): " + encryptedKeySHA512);
 	GenerateDataKeyRequest request = GenerateDataKeyRequest.builder()
		.keyId("alias/" + AWS_ALIAS).keySpec("AES_256").build();
                GenerateDataKeyResponse response = kmsClient.generateDataKey(request);
                String cipherText = new String(response.ciphertextBlob().asByteArray());//Hex.encodeHexString(response.ciphertextBlob());
                logger.debug("Generated KMS KeyId: " + response.keyId());

                //Write the wrapped key to a file in the Cache Only Key Representation
                keyRepresentation = new KeyRepresentation(kid, response.plaintext().asByteArray(), publicWrappingKey);
                FileOutputStream keyRepresentationFile = new FileOutputStream(backupPath+kid);
                keyRepresentationFile.write(keyRepresentation.toString().getBytes(StandardCharsets.UTF_8));
                keyRepresentationFile.close();

                logger.debug("Cache-Only Key representation written to file: " + kid);

                //Write the KMS generated key to a backup file (encypted)
                FileOutputStream backupFile = new FileOutputStream(backupPath+kid + ".backup");
        //        StringBuffer backup = new StringBuffer();
                backup.append("KeyId:");
                backup.append(response.keyId());
                backup.append("\nHex Encoded Encrypted Backup from KMS: ");
                backup.append(cipherText);
                backupFile.write(backup.toString().getBytes(StandardCharsets.UTF_8));
                backupFile.close();

                logger.debug("Encrypted backup of KMS generated key written to file: " + kid+ ".backup");

      

	}
      	
	 } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        
	} catch( Exception e ) {
            logger.error( "ERROR: " + e.getMessage() );
	}
	if(keyRepresentation!=null){
		return keyRepresentation.toString();
	}else{
		return "";
	}
}
}	
