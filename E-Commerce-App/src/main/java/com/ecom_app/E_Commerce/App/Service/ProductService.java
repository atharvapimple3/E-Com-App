package com.ecom_app.E_Commerce.App.Service;

import com.ecom_app.E_Commerce.App.Model.Product;
import com.ecom_app.E_Commerce.App.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProduct(int prodID) {
        return productRepo.findById(prodID).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile image) {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        try {
            product.setImageData(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productRepo.save(product);
    }

    public Product getProductByID(int prodID) {
        return productRepo.getById(prodID);
    }


    public Product updateProduct(Product product, MultipartFile image) {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        try {
            product.setImageData(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productRepo.save(product);
    }

    public void deleteProduct(int prodID) {
        productRepo.deleteById(prodID);
    }


    public List<Product> searchProducts(String keyword) {
        return productRepo.searchByKeyord(keyword);
    }
}
