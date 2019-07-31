package com.wk.XMLUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 *  使用sax来创建一个xml文件
 */
public class CreateXmlBySax {
    private static final Logger logger = LoggerFactory.getLogger(CreateXmlBySax.class);

    /**
     * 使用SAX创建xml文件
     * 创建好的文件
     * <?xml version="1.0" encoding="UTF-8"?><root>
     <person attr="one">
     <people attr="one">one child one people</people>
     <people attr="two">one child two people</people>
     </person>
     <person attr="two">
     <people attr="one">one child one people</people>
     <people attr="two">one child two people</people>
     </person>
     </root>

     */
    public void createXMLBySAX(File file) {
        try {
            SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            Properties properties = new Properties();
            properties.put(OutputKeys.INDENT,"yes"); // 换行
            properties.put(OutputKeys.ENCODING,"UTF-8"); // 字符集
            transformer.setOutputProperties(properties);
            // 文件输出流
            StreamResult result = new StreamResult(new FileOutputStream(file));
            handler.setResult(result);
            // 创建属性
            AttributesImpl attr = new AttributesImpl();
            // 打开dom对象
            handler.startDocument();
            // 创建元素 startElement(String uri, String localName,String qName, Attributes atts)
            handler.startElement("","","root",attr);
            // 每次创建节点前,先清空属性,然后再放置属性
            attr.clear();
            // 设置属性 addAttribute(String uri, String localName, String qName, String type, String value)
            attr.addAttribute("","","attr","","one");

            // 根节点第一个子节点 person
            handler.startElement("","","person",attr);
                attr.clear();
                attr.addAttribute("","","attr","","one");
                // 第一个子节点的第一个子节点 people
                handler.startElement("","","people",attr);
                char[] context = "one child one people".toCharArray();
                handler.characters(context,0,context.length);
                handler.endElement("","","people");
                // 第一个子节点的第二个子节点 people
                attr.clear();
                attr.addAttribute("","","attr","","two");
                handler.startElement("","","people",attr);
                char[] context2 = "one child two people".toCharArray();
                handler.characters(context2,0,context2.length);
                handler.endElement("","","people");
            handler.endElement("","","person");

            // 根节点第二个子节点 person
            attr.clear();
            // 设置属性 addAttribute(String uri, String localName, String qName, String type, String value)
            attr.addAttribute("","","attr","","two");
            handler.startElement("","","person",attr);
                attr.clear();
                attr.addAttribute("","","attr","","one");
                // 第一个子节点的第一个子节点 people
                handler.startElement("","","people",attr);
                char[] context3 = "one child one people".toCharArray();
                handler.characters(context3,0,context3.length);
                handler.endElement("","","people");
                // 第一个子节点的第二个子节点 people
                attr.clear();
                attr.addAttribute("","","attr","","two");
                handler.startElement("","","people",attr);
                char[] context4 = "one child two people".toCharArray();
                handler.characters(context4,0,context4.length);
                handler.endElement("","","people");
            handler.endElement("","","person");
            // 结束handler
            handler.endElement("","","root");
            // 关闭dom对象
            handler.endDocument();
        } catch (TransformerConfigurationException e) {
            logger.error(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "E:/test.xml";
        File file = new File(filePath);
        CreateXmlBySax createXmlBySax = new CreateXmlBySax();
        createXmlBySax.createXMLBySAX(file);
    }
}
