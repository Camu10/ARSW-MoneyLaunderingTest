package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    public static AtomicInteger amountOfFilesProcessed;
    public static boolean pausa;
    public static Object monitor = new Object();
    public static int numHilos = 5;
    public static AtomicInteger hilosVivos = new AtomicInteger(numHilos);

    public MoneyLaundering()
    {
        pausa=false;
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
    }

    public void processTransactionData()
    {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();

        int step = amountOfFilesTotal/numHilos , sobrante = amountOfFilesTotal%numHilos,ini=0,fin=step;


        List<MoneyLaunderingThread> lista = new ArrayList<>();

        for(int i=0;i<numHilos;i++){
            if(i == numHilos-1) {
                fin+=sobrante;

            }
            List<File> aux = new ArrayList<>();
            for(int j = ini;j< fin ;j++){
                aux.add(transactionFiles.get(j));
            }

            lista.add(new MoneyLaunderingThread(aux,transactionReader,transactionAnalyzer));
            ini=fin;
            fin+=step;
        }


        for(MoneyLaunderingThread hilo: lista){
            hilo.start();
        }

/*
        for(File transactionFile : transactionFiles)
        {            
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);

            for(Transaction transaction : transactions)
            {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
        amountOfFilesProcessed.incrementAndGet();*/
    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args) throws InterruptedException {
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        moneyLaundering.processTransactionData();


        //MoneyLaunderingThread a1 = new MoneyLaunderingThread();
        while(hilosVivos.get()>0)
        {
            System.out.println("Los hilos estan "+ ((pausa)?"detenidos":"activos"));
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
                break;
            if (!pausa){
                pausa = true;
                String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
                List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
                String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
                message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
                System.out.println(message);
            }else{
                pausa = false;
                synchronized (monitor){
                    monitor.notifyAll();
                }
            }

        }
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);
        System.exit(0);

    }


}
