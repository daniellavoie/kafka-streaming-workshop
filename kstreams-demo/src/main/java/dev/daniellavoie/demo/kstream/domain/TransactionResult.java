package dev.daniellavoie.demo.kstream.domain;

public class TransactionResult {
	public enum ErrorType {
		INSUFFICIENT_FUNDS
	}

	private final Transaction transaction;
	private final Funds funds;
	private final boolean success;
	private final ErrorType errorType;

	public TransactionResult(Transaction transaction, Funds funds, boolean success, ErrorType errorType) {
		this.transaction = transaction;
		this.funds = funds;
		this.success = success;
		this.errorType = errorType;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public Funds getFunds() {
		return funds;
	}

	public boolean isSuccess() {
		return success;
	}

	public ErrorType getErrorType() {
		return errorType;
	}
}
