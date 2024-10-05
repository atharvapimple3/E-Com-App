package com.ecom_app.E_Commerce.App.Controller;

import com.ecom_app.E_Commerce.App.Model.Product;
import com.ecom_app.E_Commerce.App.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public ResponseEntity <List<Product>> getProducts ()
    {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping("/product/{prodID}")
    public ResponseEntity<Product> getProduct(@PathVariable("prodID") int prodID) {
        Product product = productService.getProduct(prodID);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product , @RequestPart MultipartFile imageFile)
    {
        Product savedProduct = null;
        try
        {
            savedProduct = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/product/{prodID}/image")
    public ResponseEntity<byte[]> getImageByID(@PathVariable("prodID") int prodID)
    {
        Product productOpt = productService.getProductByID(prodID);
        if (productOpt != null && productOpt.getImageData() != null)
        {
            byte[] imageData = productOpt.getImageData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Set appropriate image type
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @PutMapping("/product/{prodID}")
    public ResponseEntity<String> updateProduct(@PathVariable("prodID") int prodID,
                                                @RequestPart Product product,
                                                @RequestPart MultipartFile imageFile) {
        System.out.println("Updating product with ID: " + prodID);
        System.out.println("Product data: " + product);
        System.out.println("Image file: " + (imageFile != null ? imageFile.getOriginalFilename() : "No file"));

        try {
            // Proceed with the update logic
            Product updatedProduct = productService.updateProduct(product, imageFile);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/product/{prodID}")
    public ResponseEntity<String> deleteProduct(@PathVariable("prodID") int prodID)
    {
        Product product = productService.getProductByID(prodID);

        if (product == null)
        {
            return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
        }

        if(product.isProductAvailable())
        {
            productService.deleteProduct(prodID);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword)
    {
        List<Product> products = productService.searchProducts(keyword);
        System.out.println("Searching with "+ keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }




}
