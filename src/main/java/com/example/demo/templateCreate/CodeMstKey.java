package com.example.demo.templateCreate;

import java.io.Serializable;

import javax.persistence.Column;

public class CodeMstKey implements Serializable {

    @Column (name="kbn1")
    private int kbn1;

    @Column (name="kbn2")
    private int kbn2;

    @Column (name="id")
    private int id;

    @Override
    public int hashCode(){
      return super.hashCode();
     }

    @Override
    public boolean equals(Object o){
      return super.equals(o);
    }
}