package com.testing.piggybank;

import com.testing.piggybank.model.Account;
import com.testing.piggybank.model.Transaction;
import com.testing.piggybank.transaction.TransactionService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class TransactionServiceTest {

    @Test
    public void test_filterNoTransaction() {
        TransactionService ts = new TransactionService(null, null, null);

        List<Transaction> result = ts.filterAndLimitTransactions(Collections.emptyList(), 1L, 10);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void test_filterTransaction() {
        TransactionService ts = new TransactionService(null, null, null);
        Transaction transaction = new Transaction();
        Account account = new Account();
        account.setId(1);
        transaction.setReceiverAccount(account);
        List<Transaction> transactionList = List.of(transaction);
        List<Transaction> result = ts.filterAndLimitTransactions(Collections.emptyList(), 1, 99);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void test_filterTransactionMatch() {
        TransactionService ts = new TransactionService(null, null, null);

        Transaction transaction = new Transaction();
        Account account = new Account();
        account.setId(1); // Receiver account ID matches filter ID.
        transaction.setReceiverAccount(account);

        List<Transaction> transactionList = List.of(transaction);
        List<Transaction> result = ts.filterAndLimitTransactions(transactionList, 1L, 10);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(transaction, result.get(0));
    }

    @Test
    public void test_limitTransaction() {
        TransactionService ts = new TransactionService(null, null, null);

        Account account = new Account();
        account.setId(1);

        Transaction transaction1 = new Transaction();
        transaction1.setReceiverAccount(account);

        Transaction transaction2 = new Transaction();
        transaction2.setReceiverAccount(account);

        List<Transaction> transactionList = List.of(transaction1, transaction2);
        List<Transaction> result = ts.filterAndLimitTransactions(transactionList, 1L, 1);

        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void test_nullInputs() {
        TransactionService ts = new TransactionService(null, null, null);

        List<Transaction> result = ts.filterAndLimitTransactions(null, 1L, 10);
        Assertions.assertEquals(0, result.size());
    }
}