package dev.daniellavoie.demo.kstream.data.topic;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("topics.account")
public class AccountConfiguration extends TopicConfiguration {

}
