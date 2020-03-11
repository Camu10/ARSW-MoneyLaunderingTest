package edu.eci.arsw.moneylaundering;

import java.awt.event.MouseAdapter;
import java.io.File;
import java.security.PrivateKey;
import java.util.List;

public class MoneyLaunderingThread extends Thread{
    private List<File> transactionFiles;
    private TransactionReader transactionReader;
    private TransactionAnalyzer transactionAnalyzer;
    public MoneyLaunderingThread(List<File> transactionFiles,TransactionReader transactionReader,TransactionAnalyzer transactionAnalyzer){
        this.transactionFiles = transactionFiles;
        this.transactionReader = transactionReader;
        this.transactionAnalyzer=transactionAnalyzer;
    }

    public void run(){
        //System.out.println("W");
        for(File transactionFile : transactionFiles)
        {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);

            for(Transaction transaction : transactions)
            {
                synchronized (MoneyLaundering.monitor){
                    if(MoneyLaundering.pausa){
                        try {
                            MoneyLaundering.monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                transactionAnalyzer.addTransaction(transaction);
            }
            MoneyLaundering.amountOfFilesProcessed.incrementAndGet();
        }
        MoneyLaundering.hilosVivos.decrementAndGet();
    }
}
