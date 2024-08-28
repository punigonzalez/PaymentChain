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

    @GetMapping("/customer/transactions")
    public List<Transaction> get(@RequestParam(name="accountIban") String accountIban) {
        return transactionRepository.findByAccountIban(accountIban);
    }

    //editar una transaccion por id
    @PutMapping("/{id}")
    public ResponseEntity<?> editById(@PathVariable (name="id") Long id, @RequestBody Transaction input) {
        Transaction find = transactionRepository.findById(id).get();
        if (find != null) {
            find.setAmount(input.getAmount());
            find.setChannel(input.getChannel());
            find.setDate(input.getDate());
            find.setDescription(input.getDescription());
            find.setFee(input.getFee());
            find.setAccountIban(input.getAccountIban());
            find.setReference(input.getReference());
            find.setStatus(input.getStatus());
        }
        Transaction save = transactionRepository.save(find);
        return ResponseEntity.ok(save);
    }

    //crear una transaccion
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction input){
        Transaction save = transactionRepository.save(input);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Optional<Transaction> findById = transactionRepository.findById(id);
        if(findById.get() != null){
            transactionRepository.delete(findById.get());
        }
        return ResponseEntity.ok().build();
    }

}
