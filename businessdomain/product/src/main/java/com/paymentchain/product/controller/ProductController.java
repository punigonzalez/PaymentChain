package com.paymentchain.product.controller;

import com.paymentchain.product.entities.Product;
import com.paymentchain.product.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/product")
@Tag(name="Productos", description = "Crud de productos")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // devolver lista productos
    @GetMapping
    @Operation(summary = "Lista productos")
    public List<Product> list(){
        return productRepository.findAll();
    }

    //devolver producto por id
    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por id")
    public Optional<Product> get(@PathVariable ("id")Long id){
        return productRepository.findById(id);
    }
    // editar producto por id
    @PutMapping("/{id}")
    @Operation(summary = "Editar producto por id")
    public ResponseEntity<?> put(@PathVariable ("id") Long id, @RequestBody Product input){
        Product save = productRepository.save(input);
        return ResponseEntity.ok(save);

    }

    // crear un producto nuevo
    @PostMapping
    @Operation(summary = "Crear producto")
    public ResponseEntity<?> post(@RequestBody Product input){
        Product save = productRepository.save(input);
        return ResponseEntity.ok(save);
    }

    // eliminar prodcuto por id
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto por id")
    public void delete(@PathVariable ("id")Long id){
            productRepository.deleteById(id);

}}


