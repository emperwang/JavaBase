package com.wk.digester.demo2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class DigesterDemo2 {
    private final static String filePath = "F:\\github_code\\Mine\\JavaBase\\JavaBaseProject\\src\\main\\resources\\digester\\library.xml";
    public void digester2() throws IOException, SAXException {
        File file = new File(filePath);
        URI uri = file.toURI();
        String s = file.toURI().toURL().toString();
        System.out.println("uri = "+uri + ", url = "+ s);
        FileInputStream inputStream = new FileInputStream(file);
        InputSource inputSource = new InputSource(s);

        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("library",Library.class);
        // 设置School的属性 以及 bean中的field对应关系
        digester.addSetProperties("library","name","name");
        // 也可以一次设置多个属性
        // digester.addSetProperties("School");

        digester.addObjectCreate("library/book",Book.class);
        // 添加属性,可以添加多个属性
        // digester.addSetProperties("library/Grade");

        // 调用方法来指定值
        /**
         * addCallMethod(String pattern, String methodName,int paramCount)
         * pattern: 匹配的节点
         * methodName: 方法名字
         * paramCount: 参数个数
         *
         * addCallParam(String pattern, int paramIndex,String attributeName)
         * pattern:  模式匹配节点
         * paramIndex: 对应第几个参数
         * attributeName: 属性名字
         */
        digester.addCallMethod("library/book","setBookInfo",2);
        digester.addCallParam("library/book",0,"title");
        digester.addCallParam("library/book",1,"author");
        // 把book添加到 实例栈中已经存在的 library
        digester.addSetNext("library/book","addBook",Book.class.getName());

        // 解析chapter
        digester.addObjectCreate("library/book/chapter",Chapter.class);
        /**
         *  addBeanPropertySetter 是将子节点转换为对象的属相,当对象的属性和子节点的名字不一样时用来指定对象的属性名
         *  该方法类似于addSetproperties,只不过它时用String rule规则指定标签的值来调用对戏那个的setter
         */
        //digester.addBeanPropertySetter("library/book/chapter/no");
        digester.addBeanPropertySetter("library/book/chapter/no","no");
        /**
         *  addCallMethod(String rule,String methodName,int paraNumber)
         *  此方法同样是设置对象的属性,但是方式更加灵活,不需要对象具有setter
         *  当paramNumber=0时,可以单独使用,不然需要配置addCallParam方法
         */
        digester.addCallMethod("library/book/chapter/caption","setCaption",0);
        // 上面方法等价于下面
        // digester.addBeanPropertySetter("library/book/chapter/caption");

        // 把chapter添加到Book
        digester.addSetNext("library/book/chapter","addChapter",Chapter.class.getName());

        // 解析文件
        Library library = (Library) digester.parse(inputSource);

        log.info("library : "+library);
    }

    public static void main(String[] args) throws IOException, SAXException {
        DigesterDemo2 digesterDemo2 = new DigesterDemo2();
        digesterDemo2.digester2();
    }
}
