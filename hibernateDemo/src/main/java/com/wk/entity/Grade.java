package com.wk.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 22:29 2019/12/31
 * @modifier:
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cname", length = 255, nullable = false, unique = false)
    private String cname;

    @Column(name = "address", length = 255, nullable = false)
    private String addr;
}
