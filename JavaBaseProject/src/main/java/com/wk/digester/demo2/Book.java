package com.wk.digester.demo2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class Book {
    private String title;
    private String anothor;
    private List<Chapter> chapters = new ArrayList<>();

    /**
     *  演示 addCallMethod / addCallParam 的使用
     */
    public void setBookInfo(String title,String anothor){
        this.title = title;
        this.anothor = anothor;
    }

    public void addChapter(Chapter chapter){
        chapters.add(chapter);
    }
}
