package org.example;

import org.example.configuration.Dao.Product;
import org.example.configuration.Dao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SpringBootApplication
@RestController
@RequestMapping("/product")
public class MyApplication {
    @Autowired
    private ProjectDao dao;

    @PostMapping("/save")
    public Product save(@RequestBody Product product) {
        return dao.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return dao.findAll();
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable int id) {
        return dao.findProductById(id);
    }
    @DeleteMapping("/{id}")
    public String remove(@PathVariable int id){
        return dao.deleteProduct(id);
    }

    @PostMapping("/update/{id}")
    public String update(@RequestBody Product product , @PathVariable int id){
        return dao.updateProduct(product , id);
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);

//

    }
}
