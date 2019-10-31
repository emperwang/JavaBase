package com.wk.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheUtil {
    private static Employee getFromDataBase(String empId){
        Employee e1 = new Employee("Mahesh", "Finance", "100");
        Employee e2 = new Employee("Rohan", "IT", "103");
        Employee e3 = new Employee("Sohan", "Admin", "110");
        Map<String,Employee> data = new HashMap<>();
        data.put("100",e1);
        data.put("103",e2);
        data.put("110",e3);
        System.out.println("Database hit for :" + empId);
        return data.get(empId);
    }

    public static void CacheUtil() throws ExecutionException {
        LoadingCache<String, Employee> cache = CacheBuilder.newBuilder()
                .maximumSize(100)    // 设置大小  以及 过去的策略
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Employee>() {
                    @Override
                    public Employee load(String empId) throws Exception {
                        return getFromDataBase(empId);
                    }
                });
        System.out.println("101 : " + cache.get("100"));
        System.out.println("103 : " + cache.get("100"));
        System.out.println("121 : " + cache.get("100"));
        System.out.println(" second call ,load from cache .......");
        System.out.println("101 : " + cache.get("100"));
        System.out.println("103 : " + cache.get("100"));
        System.out.println("121 : " + cache.get("100"));
    }

    public static void main(String[] args) throws ExecutionException {
        CacheUtil();
    }


    static  class Employee{
        String name;
        String dept;
        String empId;

        public Employee(String name, String dept, String empId) {
            this.name = name;
            this.dept = dept;
            this.empId = empId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDept() {
            return dept;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        public String getEmpId() {
            return empId;
        }

        public void setEmpId(String empId) {
            this.empId = empId;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", dept='" + dept + '\'' +
                    ", empId='" + empId + '\'' +
                    '}';
        }
    }

}
