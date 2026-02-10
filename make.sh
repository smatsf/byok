#!/bin/bash
export M2_HOME=/opt/maven-mvnd-1.0.2-linux-amd64
export M2=$M2_HOME/bin
export CIPHER_SUITES='TLS_AES_256_GCM_SHA384(0x1302),TLS_AES_128_GCM_SHA256(0x1301), TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384(0xC02C), TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256(0xC02B), TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384(0xC030), TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256(0xC02F), TLS_DHE_RSA_WITH_AES_256_GCM_SHA384(0x009F), TLS_DHE_RSA_WITH_AES_128_GCM_SHA256(0x009E), TLS_EMPTY_RENEGOTIATION_INFO_SCSV(0x00FF)'
#export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m -Djdk.tls.client.protocols=TLSv1.2 -Dhttps.protocols=TLSv1.2 -Dhttps.cipherSuites='TLS_AES_256_GCM_SHA384(0x1302), TLS_AES_128_GCM_SHA256(0x1301), TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384(0xC02C), TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256(0xC02B), TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384(0xC030), TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256(0xC02F), TLS_DHE_RSA_WITH_AES_256_GCM_SHA384(0x009F), TLS_DHE_RSA_WITH_AES_128_GCM_SHA256(0x009E), TLS_EMPTY_RENEGOTIATION_INFO_SCSV(0x00FF)' -Djavax.net.debug=ssl:handshake"
export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m -Dcom.redhat.fips=true -Djdk.tls.client.protocols=TLSv1.2 -Djdk.tls.namedGroups=secp256r1,secp384r1 -Djdk.httpcleint.HttpClient.log=all -Djdk.internal.httpclient.disableALPN=true -X logs" 
#export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m   -Djavax.net.debug=ssl,handshake -Djdk.tls.namedGroups=secp256r1,secp384r1 -Djdk.tls.client.protocols=TLSv1.2 -Dhttps.protocols=TLSv1.2 -X logs" 
#-Dcom.sun.net.ssl.checkRevocation=true -Docsp.enable=true"
#export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m   -Djavax.net.debug=ssl,handshake -Djdk.httpclient.allowRestrictedHeaders=connection,upgrade -Djdk.httpclient.HttpClient.log=all -Djdk.internal.httpclient.disableALPN=true"
#export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-17.0.15.0.6-2.el9.x86_64
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-21.0.7.0.6-1.el9.x86_64
export CATALINA_HOME=/opt/tomcat-native-2.0.8-src
export PATH=$M2:$PATH

export MVND_HOME=/opt/maven-mvnd-1.0.2-linux-amd64
export PATH=$MVND_HOME/bin:$PATH
sudo bash $MVND_HOME/bin/mvnd-bash-completion.bash
export TMPDIR=/opt/mvnd-tmp
export _JAVDA_OPTIONS='-Djava.io.tmpdir=/opt/mvnd-tmp -cp "/opt/java-libs/*.jar" -Djdk.lang.Process.launchMechanism=vfork -Djava.security.properties=../java.security'

 sudo bash /opt/maven-mvnd-1.0.2-linux-amd64/bin/mvnd.sh --debug clean package -Dmaven.test.skip=true -Djava.io.tmpdir=/opt/mvnd-tmp  -X 
# sudo bash /opt/maven-mvnd-1.0.2-linux-amd64/bin/mvnd.sh --debug clean package -Djava.io.tmpdir=/opt/mvnd-tmp   -Dhttps.cipherSuites=TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 -Djavax.net.debug=ssl:handshake $MAVEN_OPTS 
# sudo bash /opt/maven-mvnd-1.0.2-linux-amd64/bin/mvnd.sh --debug clean package -Djava.io.tmpdir=/opt/mvnd-tmp -Djavax.net.debug=ssl:handshake $MAVEN_OPTS  $_JAVA_OPTIONS

