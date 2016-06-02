#!/bin/sh
env SPARK_HOME=/opt/mapr/spark/spark-1.6.1 /opt/mapr/spark/spark-1.6.1/bin/spark-submit --master yarn-cluster \
--conf spark.akka.frameSize=200 --files /etc/hive/conf/hive-site.xml \
--jars /home/a78084/.ivy2/cache/org.apache.spark/spark-streaming-twitter_2.10/jars/spark-streaming-twitter_2.10-1.5.0.jar,\
/home/a78084/.ivy2/cache/edu.stanford.nlp/stanford-corenlp/jars/stanford-corenlp-3.6.0.jar,\
/home/a78084/.ivy2/cache/edu.stanford.nlp/stanford-corenlp/jars/stanford-corenlp-3.6.0-models.jar,\
/home/a78084/.ivy2/cache/com.typesafe.play/play-ws_2.10/jars/play-ws_2.10-2.4.3.jar,\
/home/a78084/.ivy2/cache/com.ning/async-http-client/jars/async-http-client-1.9.21.jar,\
/home/a78084/.ivy2/cache/io.netty/netty/bundles/netty-3.10.1.Final.jar,\
/home/a78084/.ivy2/cache/org.jboss.netty/netty/bundles/netty-3.2.2.Final.jar,\
/home/a78084/twitter_demo/twitter_streaming_demo/lib/httpclient-4.5.2.jar,\
/home/a78084/twitter_demo/twitter_streaming_demo/lib/httpcore-4.4.4.jar,\
/home/a78084/.ivy2/cache/commons-codec/commons-codec/jars/commons-codec-1.10.jar,\
/home/a78084/.ivy2/cache/com.googlecode.efficient-java-matrix-library/ejml/jars/ejml-0.23.jar \
--num-executors 1 --executor-cores 5 --driver-memory 3G --executor-memory 10G target/scala-2.10/spark-h_2.10-0.1.jar
