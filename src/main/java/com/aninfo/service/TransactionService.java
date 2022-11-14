package com.aninfo.service;

import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Transaction createTransaction(Transaction transaction) {

        Account account = accountRepository.findAccountByCbu(transaction.getCBU());

        if ((transaction.getAmount() < 0) && (account.getBalance() - transaction.getAmount() < 0)) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        if (transaction.getAmount() == 0) {
            throw new DepositNegativeSumException("Cannot deposit anything");
        }

        Double newBalance = account.getBalance() + transaction.getAmount();

        account.setBalance(newBalance);
        accountRepository.save(account);

        return transactionRepository.save(transaction);
    }

    public Collection<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

//    public Optional<List<Transaction>> findById(Long cbu) {
//        return transactionRepository.findById(cbu);
//    }

    public void deleteById(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

}
