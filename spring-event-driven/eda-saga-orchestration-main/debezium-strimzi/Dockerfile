FROM quay.io/strimzi/kafka:0.29.0-kafka-3.0.1
# FROM quay.io/debezium/connect:2.0
ENV KAFKA_CONNECT_PLUGIN_PATH=/tmp/connect-plugins

RUN mkdir $KAFKA_CONNECT_PLUGIN_PATH &&\
    cd $KAFKA_CONNECT_PLUGIN_PATH &&\
    curl -sfSL https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/1.9.2.Final/debezium-connector-postgres-1.9.2.Final-plugin.tar.gz | tar xz
