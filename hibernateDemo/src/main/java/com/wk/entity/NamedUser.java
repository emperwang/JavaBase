package com.wk.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.io.Serializable;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 16:08 2020/1/10
 * @modifier:
 */
@Setter
@Getter
@ToString
@Entity
@Table(name = "userh")
@NamedQueries({
        @NamedQuery(
                name = "NA_HQL_GET_ALL",
                query = "from User"
        ),
        @NamedQuery(
                name = "NA_HQL_GET_BY_ID",
                query = "FROM User where id = :id"
        )
}
)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "NA_SQL_GET_ALL",
                query = "select * from userh"
        ),
        @NamedNativeQuery(
                name = "NA_SQL_GET_BY_ID",
                query = "select * from userh where id =:id"
        )
}
)
public class NamedUser implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 255, nullable = true, unique = false)
    private String name;
    @Column(name = "age")
    private Integer age;

    @Column(name = "address")
    private String address;
}
