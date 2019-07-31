package com.wk.XMLUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 *  sax解析
 *  sax是由事件驱动解析，相当于从上往下，可以继承DefaultHandler重写里面的解析方法
 */
public class SAXParse extends DefaultHandler {
    private static final Logger logger = LoggerFactory.getLogger(SAXParse.class);
    // 开始解析一个元素
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        logger.info("start parse element:{}",qName);
    }
    // 解析完成一个元素
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        logger.info("end parse element:{}",qName);
    }
    // 元素的context值
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String context = new String(ch, start, length);
        logger.info("element context is:{}",context);
    }

    public static void main(String[] args) {
        File file = new File("E:/test.xml");
        SAXParse saxParse = new SAXParse();
        // 创建sax解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            // 创建sax解析类
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file,saxParse);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
