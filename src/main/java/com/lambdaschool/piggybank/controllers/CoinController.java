package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.Coin;
import com.lambdaschool.piggybank.repositories.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    // http://localhost:2019/population/total
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
}
