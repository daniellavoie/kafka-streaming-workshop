package dev.daniellavoie.demo.kstream.data.domain.transaction;

import java.util.UUID;

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
	public Mono<Void> publishTransaction(TransactionRequest transactionRequest) {
		return Mono
				.fromFuture(transactionTemplate
						.send(transactionRequestConfiguration.getName(), transactionRequest.getAccount(),
								new Transaction(UUID.randomUUID().toString(), transactionRequest.getAccount(),
										transactionRequest.getAmount(), transactionRequest.getType(),
										transactionRequest.getCurrency(), transactionRequest.getCountry()))
						.completable())
				.then();
	}

}
