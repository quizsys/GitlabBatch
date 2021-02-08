package com.example.demo.burnDown;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;

public class BurnDownKey implements Serializable {

    @Column (name="milestone")
    private String milestone;
    @Column (name="date")
    private LocalDate date;
    @Column (name="label")
    private String label;

    @Override
    public int hashCode(){
      return super.hashCode();
     }

    @Override
    public boolean equals(Object o){
      return super.equals(o);
    }
}
