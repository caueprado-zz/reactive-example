package com.reactiveexample.reactiveexample;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveExampleApplicationTests {

    protected final List<String> list = Arrays.asList("nike", "adidas", "mizuno", "nobalance", "");

    protected  List<String> names = Arrays.asList("Caue,28,Dev", "Snow,20,Warrior", "Tester,100,Tester", "Goku,28,Dev", "Arthur,30,King", "Darth Vader,60,Emperor");

    protected  List<String> mapModels(String user) {
        String[] infos = user.split(",");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }

        return Arrays.asList(infos);

//        return User.builder()
//                .name(infos[0])
//                .age(Integer.parseInt(infos[1]))
//                .job(infos[2]).build();
    }

//    @Data
//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public class User {
//        private String name;
//        private int age;
//        private String job;
//    }
}
