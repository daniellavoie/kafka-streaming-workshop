package dev.daniellavoie.demo.kstream.data.domain.transaction;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import dev.daniellavoie.demo.kstream.data.topic.TransactionRequestConfiguration;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {
	private final KafkaTemplate<String, Transaction> transactionTemplate;
	private final TransactionRequestConfiguration transactionRequestConfiguration;

	public TransactionServiceImpl(KafkaProperties kafkaProperties,
			TransactionRequestConfiguration transactionRequestConfiguration) {
		this.transactionRequestConfiguration = transactionRequestConfiguration;
		this.transactionTemplate = new KafkaTemplate<>(
				new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties()));
	}

	@Override
	public Mono<Void> publishTransaction(Transaction transaction) {
		return Mono.fromFuture(transactionTemplate
				.send(transactionRequestConfiguration.getName(), transaction.getAccount(), transaction).completable())
				.then();
	}

}
