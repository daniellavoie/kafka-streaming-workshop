package dev.daniellavoie.demo.kstream;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.daniellavoie.demo.kstream.domain.Funds;
import dev.daniellavoie.demo.kstream.domain.Transaction;
import dev.daniellavoie.demo.kstream.domain.TransactionResult;

public class TransactionTransformer implements ValueTransformer<Transaction, TransactionResult> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionTransformer.class);

	private final String stateStoreName;

	private KeyValueStore<String, Funds> store;

	public TransactionTransformer(String stateStoreName) {
		this.stateStoreName = stateStoreName;
	}

	@Override
	public void close() {

	}

	private Funds createEmptyFunds(String account) {
		Funds funds = new Funds(account, BigDecimal.ZERO);

		store.put(account, funds);

		return funds;
	}

	private Funds depositFunds(Transaction transaction) {
		return updateFunds(transaction.getAccount(), transaction.getAmount());
	}

	private Funds getFunds(String account) {
		return Optional.ofNullable(store.get(account))

				.orElseGet(() -> createEmptyFunds(account));
	}

	private boolean hasEnoughFunds(Transaction transaction) {
		return getFunds(transaction.getAccount()).getBalance().compareTo(transaction.getAmount()) != -1;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void init(ProcessorContext context) {
		store = (KeyValueStore<String, Funds>) context.getStateStore(stateStoreName);
	}

	@Override
	public TransactionResult transform(Transaction transaction) {
		// TODO - Implement business logic to process transactions.
		
		return null;
	}

	private Funds updateFunds(String account, BigDecimal amount) {
		Funds funds = new Funds(account, getFunds(account).getBalance().add(amount));

		LOGGER.info("Updating funds for account {} with {}. Current balance is {}.", account, amount,
				funds.getBalance());

		store.put(account, funds);

		return funds;
	}

	private Funds withdrawFunds(Transaction transaction) {
		return updateFunds(transaction.getAccount(), transaction.getAmount().negate());
	}
}
