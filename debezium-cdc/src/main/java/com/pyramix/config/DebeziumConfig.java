package com.pyramix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DebeziumConfig {

	public DebeziumConfig() {
		log.info("DebeziumConfig created");
	}

    @Bean
    io.debezium.config.Configuration customerConnector() {
	    return io.debezium.config.Configuration.create()
	        .with("name", "customer-mysql-connector")
	        .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
	        .with("tasks.max", "1")
	        .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
	        .with("offset.storage.file.filename", "/pyramix/dbz/dbz_offsets.dat")
	        .with("offset.flush.interval.ms", "60000")
	        .with("database.hostname", "192.168.100.203")
	        .with("database.port", "3306")
	        .with("database.user", "root")
	        .with("database.password", "password")
	        .with("database.dbname", "e021_swi_core")
	        .with("database.include.list", "e021_swi_core")
	        .with("table.include.list", "e021_swi_core.customer_order")
	        .with("include.schema.changes", "false")
	        .with("database.server.id", "2")
	        .with("database.server.name", "swi_prd_db")
	        // .with("schema.include.list", "e021_swi_core")
	        .with("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory")
	        .with("schema.history.internal.file.filename", "/pyramix/dbz/dbz_history.dat")
	        .with("schema.history.internal.kafka.bootstrap.servers", "localhost:9092")
	        .with("topic.prefix", "mysql-topic")
	        // .with("schema.history.internal.kafka.topic", "my-topic-01")
	        // .with("auto.create.topics.enable", "true")
	        .with("topic.creation.enable", "true")
	        .with("topic.creation.default.replication.factor", "3")
	        .with("topic.creation.default.partitions", "10")
	        .with("topic.creation.default.cleanup.policy", "compact")
	        .with("topic.creation.default.compression.type", "lz4")
	        .build();
	}	

}
