package com.paymentchain.customer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.repository.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;  // Asegúrate de importar el correcto
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping ("/customer")
@Tag(name="Customer", description = "Crud de customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    private final WebClient.Builder webClientBuilder;

    public CustomerController(WebClient.Builder webClientBuilder){
        this.webClientBuilder =  webClientBuilder;
     }

    HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
            .option(ChannelOption.SO_KEEPALIVE,true)
            .option(EpollChannelOption.TCP_KEEPIDLE,300)
            .option(EpollChannelOption.TCP_KEEPINTVL,60)
            .responseTimeout(Duration.ofSeconds(1))
            .doOnConnected(connection ->{
                connection.addHandlerLast(new ReadTimeoutHandler(5000 , TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));

            });




    @GetMapping
    @Operation(summary = "Lista de clientes")
    public List<Customer> list(){
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por id")
    public Optional<Customer> get(@PathVariable ("id") Long id){
        return customerRepository.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar cliente por id")
    public ResponseEntity<?> put(@PathVariable ("id") Long id, @RequestBody Customer input){
        Customer save = customerRepository.save(input);
        return ResponseEntity.ok(save);

    }

    @PostMapping
    @Operation(summary = "Crear cliente")
    public ResponseEntity<?> post(@RequestBody Customer input){
    input.getProducts().forEach(x->x.setCustomer(input));
    Customer save = customerRepository.save(input);
    return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente por id")
    public ResponseEntity<?> delete(@PathVariable ("id") Long id){
        Optional<Customer> findById = customerRepository.findById(id);
        if(findById.get()!=null){
            customerRepository.delete(findById.get());
        }
    return ResponseEntity.ok().build();
}

private String getProductName(Long id){
         WebClient build = webClientBuilder
                 .clientConnector(new ReactorClientHttpConnector(client))
                 .baseUrl("http://localhost:8083/product")
                 .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                 .defaultUriVariables(Collections.singletonMap("url","http://localhost:8083/product"))
                 .build();

    JsonNode block = build.method(HttpMethod.GET).uri("/"+id)
            .retrieve().bodyToMono(JsonNode.class).block();

    String name = block.get("name").asText();
    return name;
    }














}


