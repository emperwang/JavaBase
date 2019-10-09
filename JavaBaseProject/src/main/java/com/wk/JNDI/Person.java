package com.wk.JNDI;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.rmi.Remote;

@Setter
@Getter
@ToString
public class Person implements Remote,Serializable {
    private String name;
    private String password;
}
