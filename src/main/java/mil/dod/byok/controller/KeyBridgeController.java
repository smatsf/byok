package mil.dod.dha.byok.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import mil.dod.dha.byok.service.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KeyBridgeController {
    private final SalesforceService salesforceService;
    private final KmsService kmsService;

    public KeyBridgeController(SalesforceService salesforceService, KmsService kmsService) {
        this.salesforceService = salesforceService;
        this.kmsService = kmsService;
    }

    /*
     * @CrossOrigin(origins = "http://localhost:8443")
     * 
     * @GetMapping("/salesforce/{endpoint}")
     * public String getSalesforceData(@PathVariable String endpoint) {
     * return salesforceService.fetchSalesforceData(endpoint);
     * }
     */
    @CrossOrigin(origins = "http://localhost:8443")
    @GetMapping("/salesforce/downloadcert")
    public String getCertificate() throws java.lang.Exception {
        return salesforceService.downloadBYOKCertificate();
    }

    @CrossOrigin(origins = "http://localhost:8443")
    @GetMapping("/salesforce/keys")
    public List<String> getAllKeys() {
        return kmsService.listKeys();
    }

    @CrossOrigin(origins = "http://localhost:8443")
    @GetMapping("/salesforce/key/{keyId}")
    public String getKey(@PathVariable String keyId) {
        return kmsService.getKey(keyId);
    }

    @CrossOrigin(origins = "http://localhost:8443")
    @GetMapping("/external/keys/{keyId}/{systemId}")
    public String getJSONKey(@PathVariable String keyId, @PathVariable String systemId) {
        return kmsService.getJSONKey(keyId, systemId);
    }

   /* @CrossOrigin(origins = "http://localhost:8443")
    @GetMapping("/salesforce/key/{keyId}")
    public String getAllJsonKeysForTheSystem(@PathVariable String system) {
        return kmsService.getAllJsonKeysForTheSystem(system);
    }
    /*
     * @PostMapping("/kms/encrypt")
     * public byte[] encryptData(@RequestParam String keyId, @RequestBody String
     * data) {
     * return kmsService.encryptData(keyId,
     * data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
     * }
     * 
     * @PostMapping("/kms/decrypt")
     * public String decryptData(@RequestBody byte[] ciphertext) {
     * return new
     * String(kmsService.decryptData(java.nio.ByteBuffer.wrap(ciphertext)));
     * }
     */
}
