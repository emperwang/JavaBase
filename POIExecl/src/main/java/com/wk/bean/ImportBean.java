package com.wk.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ImportBean {
    private Integer ticketNumber;
    private String detectionNumber;
    private String name;
    private String school;
    private Integer chineseScore;
    private Integer mathScore;
    private Integer societyScore;
    private Integer scienceScore;
    private Integer englishScore;
}




