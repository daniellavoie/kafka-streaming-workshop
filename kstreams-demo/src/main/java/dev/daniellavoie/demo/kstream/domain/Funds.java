package dev.daniellavoie.demo.kstream.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Funds {
	private final String account;
	private final BigDecimal balance;

	@JsonCreator
	public Funds(@JsonProperty("account") String account, @JsonProperty("balance") BigDecimal balance) {
		this.account = account;
		this.balance = balance;
	}

	public String getAccount() {
		return account;
	}

	public BigDecimal getBalance() {
		return balance;
	}
}
