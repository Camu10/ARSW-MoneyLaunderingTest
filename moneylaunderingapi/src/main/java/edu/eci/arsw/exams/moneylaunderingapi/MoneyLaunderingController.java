package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MoneyLaunderingController
{
    @Autowired
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping( value = "/fraud-bank-accounts",method = GET )
    public ResponseEntity<?> offendingAccounts() {

        List<SuspectAccount> data = null;
        try{
            data = moneyLaunderingService.getSuspectAccounts();
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);

        }catch (Exception e){
            return new ResponseEntity<>("ERROR 500",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping( value = "/fraud-bank-account/{accountId}", method = GET )
    public ResponseEntity<?> getSuspendByAccount(@PathVariable("accountId") String accountid)
    {
        SuspectAccount data;
        try{
            data = moneyLaunderingService.getAccountStatus(accountid);
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("ERROR 500",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping( method = POST )
    public ResponseEntity<?> postSuspend(@RequestBody SuspectAccount fp)
    {
        try{
            moneyLaunderingService.addSuspend(fp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>("ERROR 500",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //TODO
}
