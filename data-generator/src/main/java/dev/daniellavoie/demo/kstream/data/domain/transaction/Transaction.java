package dev.daniellavoie.demo.kstream.data.domain.transaction;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
	public enum Type {
		DEPOSIT, WITHDRAW
	}

	private final String guid;
	private final String account;
	private final BigDecimal amount;
	private final Type type;
	private final String currency;
	private final String country;

	@JsonCreator
	public Transaction(@JsonProperty("guid") String guid, @JsonProperty("account") String account,
			@JsonProperty("amount") BigDecimal amount, @JsonProperty("type") Type type,
			@JsonProperty("currency") String currency, @JsonProperty("country") String country) {
		this.guid = guid;
		this.account = account;
		this.amount = amount;
		this.type = type;
		this.currency = currency;
		this.country = country;
	}

	public String getGuid() {
		return guid;
	}

	public String getAccount() {
		return account;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Type getType() {
		return type;
	}

	public String getCurrency() {
		return currency;
	}

	public String getCountry() {
		return country;
	}

}
