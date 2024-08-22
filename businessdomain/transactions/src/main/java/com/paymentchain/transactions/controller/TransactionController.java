package com.paymentchain.transactions.controller;


import com.paymentchain.transactions.entities.Transaction;
import com.paymentchain.transactions.repository.TransactionRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transacciones", description = "Crud de transacciones")

public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    // devuelve todas las transacciones en una lista
    @GetMapping
    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }

    //filtra transaccion por id
    @GetMapping ("/{id}")
    public Optional<Transaction> findById(@PathVariable("id")Long id){
        return transactionRepository.findById(id);
    }

    //crear una transaccion
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction input){
        Transaction save = transactionRepository.save(input);
        return ResponseEntity.ok(save);
    }


    //editar una transaccion


}
