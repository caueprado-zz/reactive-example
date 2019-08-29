package com.reactiveexample.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemTest {
    private String name;
    private String description;
    private LocalDate date;
    private LocalDateTime dateTime;
    private BigDecimal value;

    @Override
    public String toString() {
        return this.getName() + "," +
                this.getDescription() + "," +
                this.getDate() + "," +
                this.getDateTime() + "," +
                this.getValue() + "\n" ;
    }

    public String getHeader() {
        return "name,description,date,dateTime,value";
    }
}
