 export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/opt/tomcat-native-2.0.8-src
 export LD_LIBRARY_PATH
 rm -fr /opt/netty-native
 mkdir -p /opt/netty-native
chown $(whoami):$(whoami) /opt/netty-native
 java -Djava.library.path=$LD_LIBRARY_PATH/lib \
	  -jar target/byok-0.0.1-SNAPSHOT.jar \
	  --spring.config.location=src/main/resources/application.yml \
	  -Xmx2048m -Xms1024m \
	  -Djava.awt.headless=true \
	  -Dio.netty.native.workdir=/opt/netty-native \
	  -Djdk.lang.Process.launchMechanism=vfork \
  	  -Dsecurity.overridePropertiesFile=true \
	  -Djavax.net.debug=ssl,handshake \
	  -Dhttp.keepAlive=false \
	  -Djava.security.debug=properties,provider -&

