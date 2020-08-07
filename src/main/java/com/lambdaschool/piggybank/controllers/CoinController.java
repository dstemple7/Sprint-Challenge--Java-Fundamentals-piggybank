package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.Coin;
import com.lambdaschool.piggybank.repositories.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CoinController
{
    @Autowired
    CoinRepository corepos;

    private List<Coin> findCoins(List<Coin> myList, CheckCoin tester)
    {
        List<Coin> tempList = new ArrayList<>();

        for (Coin c : myList)
        {
            if (tester.test(c))
            {
                tempList.add(c);
            }
        }
        return tempList;
    }

    // http://localhost:2019/total
    @GetMapping(value = "/total", produces = {"application/json"})
    public ResponseEntity<?> totalValue()
    {
        List<Coin> myList = new ArrayList<>();
        corepos.findAll().iterator().forEachRemaining(myList::add);
        double totalValue = 0;
        for (Coin c : myList)
        {
            if(c.getQuantity()>1)
            {
                System.out.println(c.getQuantity() + " " + c.getNameplural());
            }else{
                System.out.println(c.getQuantity() + " " + c.getName());
            }
            totalValue += c.getValue()*c.getQuantity();
        }
        System.out.println("The piggy bank holds " + totalValue);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // http://localhost:2019/money/{amount}
    @GetMapping(value = "/money/{amount}")
    public ResponseEntity<?> removeCoins(@PathVariable double amount)
    {
       List<Coin> myList = new ArrayList<>();
       corepos.findAll().iterator().forEachRemaining(myList::add);
       double newAmount = amount;
       double total=0;
       for(Coin c : myList)
       {
           total += c.getQuantity()*c.getValue();
       }
       if(amount > total)
       {
           System.out.println("Money not available");
           return new ResponseEntity<>(HttpStatus.OK);
       }else{
           for(Coin c : myList)
           {
               if(c.getQuantity()*c.getValue() <= newAmount)
               {
                   newAmount -= c.getValue()*c.getQuantity();
                   c.setQuantity(0);
               }else if(c.getValue() < newAmount && c.getQuantity()>0)
               {
                   newAmount -= c.getValue();
                   c.setQuantity(c.getQuantity()-1);
               }
           }
       }
       total = 0;
       for (Coin c : myList)
        {
            if(c.getQuantity()*c.getValue()==4.00)
            {
                System.out.println("$4");
            }else if(c.getQuantity()*c.getValue()==1.00)
            {System.out.println("$1");
            }else if(c.getQuantity()>1)
            {
                System.out.println(c.getQuantity() + " " + c.getNameplural());
            }else if(c.getQuantity()==1){
                System.out.println(c.getQuantity() + " " + c.getName());
            }
            total += c.getValue()*c.getQuantity();
        }
        System.out.println("The piggy bank holds $" + total);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
