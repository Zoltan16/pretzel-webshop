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
                        "Csokis muffin",
                        "dessert",
                        420,
                        "assets/img/sample4.jpg",
                        "Puha, édes muffin."
                ));

                productRepository.save(new Product(
                        "Őrülten csokis muffin",
                        "dessert",
                        440,
                        "assets/img/sample44.jpg",
                        "Már majdnek túl csokis muffin."
                ));

                productRepository.save(new Product(
                        "Sajtos pogácsa",
                        "pretzel",
                        350,
                        "assets/img/sajtos-pogacsa.jpg",
                        "Finom, ropogós pogácsa."
                ));

                productRepository.save(new Product(
                        "Tepertős pogácsa",
                        "pretzel",
                        350,
                        "assets/img/tepertos-pogi.jpg",
                        "Friss, ropogós pogácsa."
                ));

                productRepository.save(new Product(
                        "Diós kifli",
                        "dessert",
                        360,
                        "assets/img/dios-kifli.jpg",
                        "Friss diós kifli."
                ));

                productRepository.save(new Product(
                        "Mákos kifli",
                        "dessert",
                        360,
                        "assets/img/makos-kifli.jpg",
                        "Frissen sült mákos kifli."
                ));

                productRepository.save(new Product(
                        "Ishler",
                        "dessert",
                        360,
                        "assets/img/ishler.jpg",
                        "Omlós, gyümölcsös ishler."
                ));

                productRepository.save(new Product(
                        "Meggyes pite",
                        "dessert",
                        750,
                        "assets/img/meggyes-pite.jpg",
                        "Omlós, meggyes pite."
                ));

                productRepository.save(new Product(
                        "Almalekváros pite",
                        "dessert",
                        660,
                        "assets/img/almalekvaros-pite.jpg",
                        "Házi almalekváros pite."
                ));

                productRepository.save(new Product(
                        "Linzer karika",
                        "dessert",
                        450,
                        "assets/img/barackos-linzer.jpg",
                        "Baracklekváros linzer karika."
                ));

                productRepository.save(new Product(
                        "Kókuszgolyó",
                        "dessert",
                        410,
                        "assets/img/kokuszgolyo.jpg",
                        "Kakaós kókuszgolyó."
                ));

                productRepository.save(new Product(
                        "Mandulás kókuszgolyó",
                        "dessert",
                        410,
                        "assets/img/mandulas-kokuszgolyo.jpg",
                        "Mandulás kókuszgolyó."
                ));

                productRepository.save(new Product(
                        "Makaron válogatás",
                        "dessert",
                        6000,
                        "assets/img/makaron.jpg",
                        "Hat darabos macaron válogatás."
                ));

                productRepository.save(new Product(
                        "Óriási csokis keksz",
                        "dessert",
                        350,
                        "assets/img/csokis-keksz.jpg",
                        "Az igazi amerikai csokis keksz."
                ));

                productRepository.save(new Product(
                        "Croissant",
                        "dessert",
                        390,
                        "assets/img/croissant.jpg",
                        "Puha, vajas croissant."
                ));

                productRepository.save(new Product(
                        "Csokis croissant",
                        "dessert",
                        400,
                        "assets/img/cs-croissant.jpg",
                        "Csokoládéval töltött croissant."
                ));

                productRepository.save(new Product(
                        "Diós bejgli szelet",
                        "dessert",
                        390,
                        "assets/img/dios-b.jpg",
                        "Friss diós bejgli."
                ));

                productRepository.save(new Product(
                        "Mákos bejgli szelet",
                        "dessert",
                        390,
                        "assets/img/makos-b.jpg",
                        "Friss mákos bejgli."
                ));

                productRepository.save(new Product(
                        "Málnás kürtös kalács",
                        "dessert",
                        4000,
                        "assets/img/kurt-m.jpg",
                        "Friss kürtös."
                ));

                productRepository.save(new Product(
                        "Fahéjas kürtös kalács",
                        "dessert",
                        4000,
                        "assets/img/kurt-f.jpg",
                        "Friss, ropogós kürtös."
                ));

                productRepository.save(new Product(
                        "Pisztáciás kürtös kalács",
                        "dessert",
                        4000,
                        "assets/img/kurt-p.jpg",
                        "Omlós kürtös."
                ));

                productRepository.save(new Product(
                        "Croissant gumicukor",
                        "dessert",
                        1500,
                        "assets/img/gumicuki2.png",
                        "Croissant alakú édes gumicuki."
                ));

                productRepository.save(new Product(
                        "Fánk gumicukor",
                        "dessert",
                        1500,
                        "assets/img/gumicuki3.png",
                        "Fánk alakú savanyú gumicuki."
                ));

                productRepository.save(new Product(
                        "Eper gumicukor",
                        "dessert",
                        1500,
                        "assets/img/gumicuki4.png",
                        "Eper alakú savanyú gumicuki."
                ));






                productRepository.save(new Product(
                        "Gülü eper plüss",
                        "merch",
                        2990,
                        "assets/img/sample5.jpg",
                        "Puha epres plüssfigura."
                ));

                productRepository.save(new Product(
                        "Pretzel bögre",
                        "merch",
                        2500,
                        "assets/img/sample6.jpg",
                        "Kerámia bögre."
                ));

                productRepository.save(new Product(
                        "Pretzel kávés bögre",
                        "merch",
                        2490,
                        "assets/img/pohar22.jpg",
                        "Kerámia bögre mini péksütikkel."
                ));

                productRepository.save(new Product(
                        "Pretzel tányér",
                        "merch",
                        2490,
                        "assets/img/tanyer.jpg",
                        "Kerámia tányér."
                ));

                productRepository.save(new Product(
                        "Pretzel Time póló",
                        "merch",
                        3990,
                        "assets/img/sample7.jpg",
                        "Pamut póló."
                ));

                productRepository.save(new Product(
                        "Sós perec kitűző",
                        "merch",
                        2500,
                        "assets/img/perec-kituzo.jpg",
                        "Perec formájú kitűző."
                ));

                productRepository.save(new Product(
                        "Croissant kitűző",
                        "merch",
                        2500,
                        "assets/img/croissant-kituzo.jpg",
                        "Croissant formájú kitűző."
                ));

                productRepository.save(new Product(
                        "Fánk kitűző",
                        "merch",
                        2500,
                        "assets/img/fank-kituzo.jpg",
                        "Fánk formájú kitűző."
                ));

                productRepository.save(new Product(
                        "Perec nyaklánc",
                        "merch",
                        2800,
                        "assets/img/perec-ny.jpg",
                        "Mini perec nyaklánc."
                ));

                productRepository.save(new Product(
                        "Bejgli kulcstartó",
                        "merch",
                        2500,
                        "assets/img/bejgli-kulcs.jpg",
                        "Mini bejglis kulcstartó."
                ));

                productRepository.save(new Product(
                        "Bejgli mintás sakpa",
                        "merch",
                        2900,
                        "assets/img/sapi.jpg",
                        "Meleg sapi, tökéletes télére."
                ));

                productRepository.save(new Product(
                        "Kakaós csiga porcukorral kulcstartó",
                        "merch",
                        2500,
                        "assets/img/kakaos-k.jpg",
                        "Mini kakaós csiga kulcstartó."
                ));

                productRepository.save(new Product(
                        "Nápolyi kulcstartó",
                        "merch",
                        2500,
                        "assets/img/napolyi-k.jpg",
                        "Mini nápolyi kulcstartó."
                ));

                productRepository.save(new Product(
                        "Dobostorta kulcstartó",
                        "merch",
                        2500,
                        "assets/img/dobos-k.jpg",
                        "Mini dobostorta kulcstartó."
                ));

                productRepository.save(new Product(
                        "Krémes kulcstartó",
                        "merch",
                        2500,
                        "assets/img/kremes-k.jpg",
                        "Mini krémes süti kulcstartó."
                ));

                productRepository.save(new Product(
                        "Perec mintás kutya nyakörv",
                        "merch",
                        2100,
                        "assets/img/nyak.jpg",
                        "Strapabíró nyakörv a mindennapkorra."
                ));

                productRepository.save(new Product(
                        "Fánk mintás kutya nyakörv",
                        "merch",
                        2100,
                        "assets/img/nyak1.jpg",
                        "Strapabíró nyakörv a mindennapkorra."
                ));

                productRepository.save(new Product(
                        "Süti mintás kutya nyakörv",
                        "merch",
                        2100,
                        "assets/img/nyak2.jpg",
                        "Strapabíró nyakörv a mindennapkorra."
                ));

                productRepository.save(new Product(
                        "Nasi mintás sál",
                        "merch",
                        2200,
                        "assets/img/sal1.jpg",
                        "Pamut sál."
                ));

                productRepository.save(new Product(
                        "Pizza és cica mintás sál",
                        "merch",
                        2200,
                        "assets/img/sal2.jpg",
                        "Pamut sál."
                ));
                
                productRepository.save(new Product(
                        "Péksüti mintás sál",
                        "merch",
                        1800,
                        "assets/img/sal33.jpg",
                        "Pamut sál."
                ));




            }
        };
    }
}
