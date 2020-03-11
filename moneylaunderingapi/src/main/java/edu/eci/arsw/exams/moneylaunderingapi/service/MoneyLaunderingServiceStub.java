package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    List<SuspectAccount> suspend;

    public MoneyLaunderingServiceStub(){
        suspend = new CopyOnWriteArrayList<>();
        SuspectAccount a1 = new SuspectAccount("1" ,1);
        SuspectAccount a2 = new SuspectAccount("2",2);
        SuspectAccount a3 = new SuspectAccount("3",3);
        suspend.add(a1);suspend.add(a2);suspend.add(a3);
    }
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) {
        SuspectAccount account=getAccountStatus(suspectAccount.getAccountId());
        account.setAmountOfSmallTransactions(suspectAccount.getAmountOfSmallTransactions());
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) {
        for (SuspectAccount fp : suspend){
            if (fp.getAccountId().equals(accountId)){
                return fp;
            }
        }
        return new SuspectAccount("0",0);
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        //TODO
        return suspend;
    }

    @Override
    public void addSuspend(SuspectAccount suspectAccount)
    {
        for(int i=0;i<suspend.size();i++){
            if(suspectAccount.getAccountId().equals(suspend.get(i).getAccountId())){
                System.out.println("nop");
            }
        }
        suspend.add(suspectAccount);
    }
}
