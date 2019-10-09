package com.wk.digester.demo;

public class School {
    private String name;
    private Grade grades[] = new Grade[0];
    private final Object servericeLock = new Object();

    public void addGrade(Grade g){
        synchronized (servericeLock){
            Grade[] results = new Grade[this.grades.length + 1];
            System.arraycopy(grades,0,results,0,grades.length);
            results[grades.length] = g;
            grades = results;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade[] getGrades() {
        return grades;
    }

    public void setGrades(Grade[] grades) {
        this.grades = grades;
    }
}
