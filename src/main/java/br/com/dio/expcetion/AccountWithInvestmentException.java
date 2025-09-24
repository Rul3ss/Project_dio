package br.com.dio.expcetion;

public class AccountWithInvestmentException extends RuntimeException {
    public AccountWithInvestmentException(String message) {
        super(message);
    }
}
