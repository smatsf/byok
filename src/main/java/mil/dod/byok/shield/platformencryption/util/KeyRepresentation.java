/*
 * Copyright (c) 2018, salesforce.com, inc.
 * All rights reserved.
 * Licensed under the BSD 3-Clause license.
 * For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package mil.dod.dha.byok.shield.platformencryption.util;

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
import java.security.PublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.jose4j.jwa.JceProviderTestSupport;
//import org.jose4j.jwa.JceProviderTestSupport.RunnableTest;
public class KeyRepresentation {

    private String kid;
    private byte[] key;
    private PublicKey wrappingKey;

    public KeyRepresentation(String kid, byte[] key, PublicKey wrappingKey) {
        this.kid = kid;
        this.key = key;
        this.wrappingKey = wrappingKey;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public void setWrappingKey(PublicKey wrappingKey) {
        this.wrappingKey = wrappingKey;
    }

    public String getKid() {
        return kid;
    }

    //Note: This is the critical piece of code that properly formats the JWE as expected by CacheOnlyKeys
    public String getJwe() {
        String compactSerialization = null;
        try {
Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	    Provider bcProvider = new BouncyCastleProvider();
            // 2. Set up the cipher for encryption (RSA/OAEP)
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding",bcProvider);

            // 3. Encrypt a message using the public key
            String message = "This is a test message for RSA-OAEP encryption!";
            cipher.init(Cipher.ENCRYPT_MODE, wrappingKey);
            
	    JsonWebEncryption jwe = new JsonWebEncryption();
	    jwe.setAlgorithmConstraints(new AlgorithmConstraints(WHITELIST, KeyManagementAlgorithmIdentifiers.RSA_OAEP));
            jwe.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(WHITELIST, ContentEncryptionAlgorithmIdentifiers.AES_256_GCM));
             
            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
            jwe.setKeyIdHeaderValue(kid);
            jwe.setKey(wrappingKey);
            jwe.setPlaintext(key);
            compactSerialization = jwe.getCompactSerialization();
        } catch (JoseException e) {
            System.out.println(e);
        
  	} catch (Exception e) {
            e.printStackTrace();
        }
        return compactSerialization;
    }


    //Note: This is the critical piece of code that properly formats the JSON format that carries the kid and JWE
    public String toString() {
        StringBuffer jsonRepresentation = new StringBuffer("{");
        jsonRepresentation.append("\"kid\" : \"" + kid + "\",");
        jsonRepresentation.append("\"jwe\" : \"" + getJwe() + "\"}");
        return jsonRepresentation.toString();
    }


}
