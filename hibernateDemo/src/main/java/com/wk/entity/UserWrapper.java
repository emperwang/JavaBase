package com.wk.entity;

import lombok.*;

import javax.persistence.*;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 11:49 2020/1/10
 * @modifier:
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userh")
public class UserWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", length = 255, nullable = false,unique = false)
    private String name;
}
