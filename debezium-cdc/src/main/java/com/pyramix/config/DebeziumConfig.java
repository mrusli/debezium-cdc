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
	public io.debezium.config.Configuration customerConnector() {
	    return io.debezium.config.Configuration.create()
	        .with("name", "customer-mysql-connector")
	        .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
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
	        .with("topic.prefix", "mysql-topic")
	        .with("database.server.id", "2")
	        .with("database.server.name", "e021_swi_core")
	        .with("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory")
	        .with("schema.history.internal.file.filename", "/pyramix/dbz/dbz_history.dat")
	        // .with("schema.history.internal.kafka.bootstrap.servers", "localhost:9092")
	        // .with("schema.history.internal.kafka.topic", "my-topic-01")
	        .build();
	}	

}
