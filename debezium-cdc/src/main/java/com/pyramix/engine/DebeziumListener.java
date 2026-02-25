package com.pyramix.engine;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import io.debezium.config.Configuration;
import io.debezium.data.Envelope.Operation;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DebeziumListener {

	private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
	private final Executor executor = Executors.newSingleThreadExecutor();
	
	public DebeziumListener(Configuration connectorConfiguration) {
		log.info("DebeziumListener created");
	    this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
	      .using(connectorConfiguration.asProperties())
	      .notifying(this::handleChangeEvent)
	      .build();
	}
	
	public void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordChangeEvent) {
		SourceRecord sourceRecord = sourceRecordChangeEvent.record();
		Struct sourceRecordChangeValue = (Struct) sourceRecord.value();
		
		if (sourceRecordChangeValue != null) {
			//String operationCode = (String) sourceRecordChangeValue.get("op");
			Operation operation = Operation.forCode((String) sourceRecordChangeValue.get("op"));			
			log.info(operation.name());
			
			if (operation != Operation.READ) {
				// Struct before 	= (Struct) sourceRecordChangeValue.get("before");
				// Struct after 	= (Struct) sourceRecordChangeValue.get("after");
				// log.info("BEFORE: "+before.toString());
				// log.info("AFTER : "+after.toString());
				
				String record = operation == Operation.DELETE ? sourceRecordChangeValue.get("before").toString() : sourceRecordChangeValue.get("after").toString();
				log.info(record);
			}
		}
		
		// var sourceRecord = sourceRecordChangeEvent.record();
		// log.info("Key = {}, Value = {}", sourceRecord.key(), sourceRecord.value());
		// var sourceRecordChangeValue = (Struct) sourceRecord.value();
		// log.info("SourceRecordChangeValue = '{}'", sourceRecordChangeValue);
	}
	
	@PostConstruct
	private void start() {
		this.executor.execute(debeziumEngine);
	}
	
	@PreDestroy
	private void stop() throws IOException {
		if (Objects.nonNull(this.debeziumEngine)) {
			this.debeziumEngine.close();
		}
	}
}
