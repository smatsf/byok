"# byok" 
BYOK - Key Material tenanat secret Cache only wrapper for Salesforce Shield
The purpose of this briefing is to provide an overview covering a Bring-Your-Own-Key (BYOK)
solution which leverages a Cache-Only Key Service.​

​The cache only solution for key management is focused on automated, highly available,
highly scalable API services catering to the needs of Salesforce Shield Platform Encryption.​

​Salesforce Cache-Only BYOK (Bring Your Own Key) key management leverages a cloud-based key service, such as AWS KMS, to securely store
and manage cryptographic keys outside of Salesforce. This approach bypasses Salesforce's key derivation servers, allowing keys to be directly
used for encryption and decryption operations. The keys are fetched on demand from AWS KMS via a secure connection, ensuring that sensitive
data is protected while minimizing the need for manual intervention and reducing the potential for human error. This method streamlines key
management while maintaining high security standards.​

​

Benefits: ​

Enhanced Security: Keys are stored and managed securely outside Salesforce in AWS KMS, reducing exposure to internal threats. Key
material is never stored on any Salesforce infrastructure and is only ever held in cache. ​

Direct Encryption/Decryption: Keys are used directly for encryption and decryption, bypassing Salesforce key derivation servers and
streamlining the process.​

​

Important Considerations: ​

Cached Key Material: Given the key material is not stored in the Salesforce instance, encryption/decryption activities will be dependent
upon accessing key material through the Cache-Only Key Service. Inability to access key material through the key service could
significantly impact the platform's ability to function. ​

Key Availability: Ensure continuous, secure access to the key service (e.g., AWS KMS) to prevent disruptions in encryption/decryption
operations. ​

Service Latency and Performance: The latency of the key service will become a key factor in the performance of the HFN Platform. ​

Compliance Requirements: Verify that the solution meets relevant regulatory and compliance standards for key management and data
protection.​

​Given that key material is only held in cache, the Cache-Only Key
Service will be required any time that cache is cleared. ​

​

Example Scenarios: ​

New Salesforce Releases ​

Expiring Key Material ​

Cache Eviction Policy ​

HFN Platform defined policies for triggering cache clearing. 

EC2 Instance – RHEL instance hosted in AWS Gov Cloud hosting the Cache-Only Key
Service java application. ​

AWS Autoscaling group – Scheduled scaling policies to scale the EC2 Instance. This is
needed to make the API highly available and scalable. If it's down for any reason can
cause performance and high latency for DMACS.​

AWS KMS – Cloud based key management server for generating , storing, and
managing key material. ​

Encryption Events for Logging and Auditing – Captures events related to success
or failure of a callout for key material ​

HFN Cache-Only Key Service Integration User – Integration user which enables connectivity
between Salesforce and the Cache-Only Keys Service​

HFN BYOK Certificate – Certificate needed for mTLS and Key management calls​

It will host the java application for the Cache-Only Key Service along with
bash scripts to run the key management functions​

Amazon EC2 AMI –RHEL 9 software required to set up and boot EC2
instance​

Role played: Hosts API based Key Services for key management
lifecycle​

FIPS Compliant JRE – Java runtime for openssl​

Flask API management Services – Tool to host the API for key lifecycle​


Contains collection of EC2 Instance for autoscaling and high availability
for key access​

Cache only solution rely heavily on EC2's availability otherwise DMACS
system comes to halt.​

Health check replacements and scaling policies.​

Maintains number of instances to desired capacity​

Policies enables upgrade or downgrade number of instances to meet
changing conditions.​

Spot instances let unused EC2 capacity at steep discounts relative to
On-Demand Services.​

Out of the box solution for Key Management​

Centrally manages keys ​

Define policies across integrated services and application from a
single point​

Encrypt data within Salesforce with AWS encryption SDK data
encryption library​

FIPS 140-2 compliant​

Could get pricy over the period if not managed properly

To enable monitoring and auditing of the key management activities undertaken by the
Salesforce platform, this event would be configured and pushed to the DHA Splunk instance
along with Real Time Event Monitoring and Threat Detection Events.  ​

​

HFN KeyManagementEvent– Captures events related to success or failure of a
callout for key material ​

RemoteKeyCalloutEvent - Helpful in debugging the failed callouts for replay
detection in case if a callout is fraudulently intercepted.​

This event needs to be registered in Salesforce subscription channel:
/event/RemoteKeyCalloutEvent​

Subscribe to events after insert Apex triggers and publish events to streaming API
for consumption in Splunk.​

​Profile and Permission Set configured and set to an integration user
who is authenticated, via a Connected App, via an OAuth 2.0 JWT
Bearer Token flow. ​

​

Details: ​

Ensure appropriate profiles have access to encrypted data with
View Encrypted Data permissions​

Use the Encryption Policy to enable encryption for selected fields​

