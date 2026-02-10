#!/bin/bash
export M2_HOME=/opt/maven-mvnd-1.0.2-linux-amd64
export M2=$M2_HOME/bin
export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m -Djdk.tls.client.protocols=TLSv1.2 -Dhttps.protocols=TLSv1.2 -Dhttps.cipherSuites=ECDHE-RSA-AES128-GCM-SHA256 -Djavax.net.debug=ssl:handshake"
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-17.0.15.0.6-2.el9.x86_64
export CATALINA_HOME=/opt/tomcat-native-2.0.8-src
export PATH=$M2:$PATH

export MVND_HOME=/opt/maven-mvnd-1.0.2-linux-amd64
export PATH=$MVND_HOME/bin:$PATH
sudo bash $MVND_HOME/bin/mvnd-bash-completion.bash
export TMPDIR=/opt/mvnd-tmp
export _JAVA_OPTIONS='-Djava.io.tmpdir=$TMPDIR  -Djdk.lang.Process.launchMechanism=vfork'

 sudo bash /opt/maven-mvnd-1.0.2-linux-amd64/bin/mvn --debug clean package -Djava.io.tmpdir=/opt/mvnd-tmp 

