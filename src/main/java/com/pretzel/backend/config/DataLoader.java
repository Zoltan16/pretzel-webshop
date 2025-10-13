package com.pretzel.backend.config;

import com.pretzel.backend.model.Product;
import com.pretzel.backend.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            // ha nincs adat, töltsünk mintákat
            if (productRepository.count() == 0) {
                productRepository.save(new Product(
                        "Sós perec",
                        "pretzel",
                        450,
                        "assets/img/sample1.jpg",
                        "Frissen sült, ropogós sós perec."
                ));

                productRepository.save(new Product(
                        "Csokis perec",
                        "pretzel",
                        520,
                        "assets/img/sample2.jpg",
                        "Csokoládé mázzal bevont perec."
                ));

                productRepository.save(new Product(
                        "Fahéjas csiga",
                        "dessert",
                        390,
                        "assets/img/sample3.jpg",
                        "Illatos fahéjas csiga."
                ));

                productRepository.save(new Product(
                        "Édes muffin",
                        "dessert",
                        420,
                        "assets/img/sample4.jpg",
                        "Puha, édes muffin."
                ));

                productRepository.save(new Product(
                        "Gülü eper plüss",
                        "merch",
                        1990,
                        "assets/img/sample5.jpg",
                        "Puha epres plüssfigura."
                ));

                productRepository.save(new Product(
                        "Pretzel bögre",
                        "merch",
                        2490,
                        "assets/img/sample6.jpg",
                        "Kerámia bögre logóval."
                ));
            }
        };
    }
}
