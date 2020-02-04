package dev.daniellavoie.demo.kstream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.daniellavoie.demo.kstream.domain.Funds;
import dev.daniellavoie.demo.kstream.domain.Transaction;
import dev.daniellavoie.demo.kstream.domain.TransactionResult;
import dev.daniellavoie.demo.kstream.topic.FundsStoreConfiguration;
import dev.daniellavoie.demo.kstream.topic.TopicConfiguration;
import dev.daniellavoie.demo.kstream.topic.TransactionFailedConfiguration;
import dev.daniellavoie.demo.kstream.topic.TransactionRequestConfiguration;
import dev.daniellavoie.demo.kstream.topic.TransactionSuccessConfiguration;

@Configuration
@ConditionalOnProperty(name = "kafka.streams.enabled", matchIfMissing = true)
public class KStreamConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(KStreamConfig.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

	private final StreamsBuilder streamsBuilder;
	private final TransactionFailedConfiguration transactionFailedConfiguration;
	private final TransactionRequestConfiguration transactionRequestConfiguration;
	private final TransactionSuccessConfiguration transactionSuccessConfiguration;
	private final FundsStoreConfiguration fundsStoreConfiguration;

	public KStreamConfig(StreamsBuilder streamsBuilder, TransactionFailedConfiguration transactionFailedConfiguration,
			TransactionRequestConfiguration transactionRequestConfiguration,
			TransactionSuccessConfiguration transactionSuccessConfiguration, AdminClient adminClient,
			FundsStoreConfiguration fundsStoreConfiguration,
			@Value("${topics.auto-create:false}") boolean autoCreateTopics) {
		this.streamsBuilder = streamsBuilder;
		this.transactionFailedConfiguration = transactionFailedConfiguration;
		this.transactionRequestConfiguration = transactionRequestConfiguration;
		this.transactionSuccessConfiguration = transactionSuccessConfiguration;
		this.fundsStoreConfiguration = fundsStoreConfiguration;

		if (autoCreateTopics) {
			createTopicIfMissing(transactionFailedConfiguration, adminClient);
			createTopicIfMissing(transactionRequestConfiguration, adminClient);
			createTopicIfMissing(transactionSuccessConfiguration, adminClient);
		}
	}

	@Bean
	public Topology topology() {
		streamsBuilder.addStateStore(
				Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(fundsStoreConfiguration.getName()),
						Serdes.String(), new JsonSerde<Funds>(Funds.class, OBJECT_MAPPER)));

		defineStreams(streamsBuilder);

		Topology topology = streamsBuilder.build();

		LOGGER.trace("Topology description : {}", topology.describe());

		return topology;
	}

	private TransactionTransformer transactionTransformer() {
		return new TransactionTransformer(fundsStoreConfiguration.getName());
	}
	
	private void defineStreams(StreamsBuilder streamsBuilder) {
		KStream<String, Transaction> transactionStream = streamsBuilder
				.stream(transactionRequestConfiguration.getName());
		

		KStream<String, TransactionResult> resultStream = transactionStream

				.transformValues(this::transactionTransformer, fundsStoreConfiguration.getName());

		resultStream

				.filter(this::success)

				.to(transactionSuccessConfiguration.getName());

		resultStream

				.filterNot(this::success)

				.to(transactionFailedConfiguration.getName());
	}

	private boolean success(String account, TransactionResult result) {
		return result.isSuccess();
	}

	private void createTopicIfMissing(TopicConfiguration topicConfiguration, AdminClient adminClient) {
		try {
			if (!adminClient.listTopics().names().get().stream()
					.filter(existingTopic -> existingTopic.equals(topicConfiguration.getName())).findAny()
					.isPresent()) {
				LOGGER.info("Creating topic {}.", topicConfiguration.getName());

				NewTopic topic = new NewTopic(topicConfiguration.getName(), topicConfiguration.getPartitions(),
						topicConfiguration.getReplicationFactor());

				topic.configs(new HashMap<>());
				topic.configs().put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);

				adminClient.createTopics(Arrays.asList(topic)).all().get();
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
