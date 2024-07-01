package org.example.LeapTour;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.LeapTour.mapper")
public class LeapTourApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeapTourApplication.class, args);
    }

}
