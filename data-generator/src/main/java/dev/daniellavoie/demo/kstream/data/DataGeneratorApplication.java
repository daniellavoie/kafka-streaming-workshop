package dev.daniellavoie.demo.kstream.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import dev.daniellavoie.demo.kstream.data.domain.account.AccountService;
import dev.daniellavoie.demo.kstream.data.topic.AccountConfiguration;
import dev.daniellavoie.demo.kstream.data.topic.TopicConfiguration;

@SpringBootApplication
public class DataGeneratorApplication implements CommandLineRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGeneratorApplication.class);

	private final AccountConfiguration accountConfiguration;
	private final AccountService accountService;
	private final AdminClient adminClient;

	public DataGeneratorApplication(AccountConfiguration accountConfiguration, AccountService accountService,
			KafkaProperties kafkaProperties) {
		this.accountConfiguration = accountConfiguration;
		this.accountService = accountService;
		this.adminClient = AdminClient.create(kafkaProperties.buildAdminProperties());
	}

	public static void main(String[] args) {
		SpringApplication.run(DataGeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createTopicIfMissing(accountConfiguration, adminClient);
		accountService.generateAccounts();
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