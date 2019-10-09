package com.wk.digester.demo;

import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DigesterDemo {
    // 属性set/get方法，假设解析出来的school对象放在这
    private School school;

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
    private final static String filePath = "F:\\github_code\\Mine\\JavaBase\\JavaBaseProject\\src\\main\\resources\\digester\\school.xml";
    private void digester() throws IOException, SAXException {
        // 读取文件
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        InputSource inputSource = new InputSource(file.toURI().toURL().toString());
        inputSource.setByteStream(inputStream);

        String absolutePath = file.getAbsolutePath();
        String canonicalPath = file.getCanonicalPath();
        //System.out.println("absolutePath : "+absolutePath + ", canonicalPath:"+canonicalPath);

        // 创建digester 对象
        Digester digester = new Digester();
        // 是否需要使用DTD验证xml文档的合法性
        digester.setValidating(false);
        // 将当前对象放到对象堆的最顶层，这也是这个类为什么要有school属性的原因
        digester.push(this);
        /**
         * 下面开始为digester创建匹配规则
         * School,School/Grade, School/Grade/Class 分别对应school.xml中的School,Grade,Class
         */
        // 为School创建规则
        /**
         * addObjectCreate(String pattern, String className,String attributeName)
         * pattern : 匹配的节点
         * classname: 该节点对应的默认实体类
         * attributeName: 如果该节点有className属性，用className的值替换默认实体类
         *
         * digester 匹配到School节点后
         * 1. 如果School节点没有className属性，将创建com.wk.digester.demo.School 对象
         * 2. 如果School节点有className属性，将创建指定的className属性值的对象
         */
        digester.addObjectCreate("School",School.class.getName(),"className");
        // 将指定节点的属性映射到对象,即将school节点的name属性映射到School.java
        digester.addSetProperties("School");
        /**
         * addSetNext(String pattern, String methodName,String paramType)
         * pattern: 匹配的节点
         * methodName: 调用父节点的方法
         * paramType: 父节点方法接收的参数类型
         * digester匹配到school节点后,将调用DigesterDemo的setSchool方法,参数类型为school对象
         */
        digester.addSetNext("School","setSchool",School.class.getName());

        // 为Grade创建规则
        digester.addObjectCreate("School/Grade",Grade.class.getName(),"className");
        digester.addSetProperties("School/Grade");
        digester.addSetNext("School/Grade","addGrade",Grade.class.getName());

        digester.addObjectCreate("School/Grade/Class",Class.class.getName(),"className");
        digester.addSetProperties("School/Grade/Class");
        digester.addSetNext("School/Grade/Class","addClass",Class.class.getName());
        // 解析输入源
        digester.parse(inputSource);
    }
    // 只是将School对象进行控制台输出
    private void print(School s) {
        if (s != null) {
            System.out.println(s.getName() + "有" + s.getGrades().length + "个年级");
            for (int i = 0; i < s.getGrades().length; i++) {
                if (s.getGrades()[i] != null) {
                    Grade g = s.getGrades()[i];
                    System.out.println(g.getName() + "年级 有 " + g.getClasses().length + "个班：");
                    for (int j = 0; j < g.getClasses().length; j++) {
                        if (g.getClasses()[j] != null) {
                            Class c = g.getClasses()[j];
                            System.out.println(c.getName() + "班有" + c.getNumber() + "人");
                        }
                    }
                }
            }
        }
    }
    public static void main(String[] args) throws Exception{
        DigesterDemo digesterDemo = new DigesterDemo();
        digesterDemo.digester();

        digesterDemo.print(digesterDemo.getSchool());
    }
}
