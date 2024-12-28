package com.Walker.SpringEcom.controller;

import com.Walker.SpringEcom.model.Product;
import com.Walker.SpringEcom.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
       return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.ACCEPTED);
    }

    @GetMapping("products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product=productService.getProductById(id);

        if(product!=null) {
            return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, MultipartFile imageFile) {
        Product savedProduct=null;
        try{
            savedProduct=productService.addOrUpdateProduct(product,imageFile);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }
        catch (IOException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable int productId) {
        Product product=productService.getProductById(productId);
        if(product!=null) {
            return new ResponseEntity<>(product.getImageData(), HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,@RequestPart MultipartFile imageFile) {
        Product updatedProduct=null;
        try{
            updatedProduct=productService.addOrUpdateProduct(product,imageFile);
           return new ResponseEntity<>("Updated", HttpStatus.ACCEPTED);
        }
        catch (IOException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Product product=productService.getProductById(id);

        if(product!=null) {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.ACCEPTED);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam String keyword) {
        List<Product> products= productService.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.ACCEPTED);
    }

}
