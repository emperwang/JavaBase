package com.wk.digester.demo2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class Library {
    private String name;
    private List<Book> bookList = new ArrayList<>();

    public void addBook(Book book){
        bookList.add(book);
    }
}
