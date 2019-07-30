package com.wk.XMLUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomUtil {
    private static final Logger logger = LoggerFactory.getLogger(DomUtil.class);
    /**
     *  获取dom解析器
     * @return
     * @throws ParserConfigurationException
     */
    public static DocumentBuilder getDocumentBuilder(Boolean ignoreComments,Boolean ignoreElementContentWhiteSpace,Boolean namespaceAware) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringComments(ignoreComments);
        documentBuilderFactory.setIgnoringElementContentWhitespace(ignoreElementContentWhiteSpace);
        documentBuilderFactory.setNamespaceAware(namespaceAware);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder;
    }

    public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder;
    }

    /**
     *  把解析后的dom对象重新写入到xml文件中
     * @param document
     * @param filePath
     * @throws TransformerException
     */
    public static void writeDomToXMLFile(Document document,String filePath) throws TransformerException {
        // 获取transformer工厂类
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        // 实例化一个 Transformer工具
        Transformer transformer = transformerFactory.newTransformer();
        // 写入操作
        transformer.transform(new DOMSource(document),new StreamResult(filePath));
    }

    /**
     *  把一个xml文件解析为dom对象
     * @param xmlPath
     * @return
     * @throws ParserConfigurationException
     */
    public static Document parseXmlToDocument(String xmlPath) throws ParserConfigurationException, IOException, SAXException {
        Document document = getDocumentBuilder().parse(new File(xmlPath));
        return document;
    }

    /**
     *  得到某种节点的textContent
     * @param document
     * @param tagName
     * @return
     */
    public static List<String> getElementContentByTagName(Document document, String tagName){
        // 根据标签得到node
        NodeList elements = document.getElementsByTagName(tagName);
        List<String> lists = new ArrayList<>(elements.getLength());
        for (int i = 0; i < elements.getLength(); i++){
            Node node = elements.item(i);
            String textContent = node.getTextContent();
            lists.add(textContent);
        }
        return lists;
    }

    /**
     *  打印所有节点
     * @param root  根节点
     */
    public static void printAllNode(Node root){
        // 获取根节点
        //Element root = document.getDocumentElement();
        if (root.getNodeType() == Node.ELEMENT_NODE){
            logger.info(root.getNodeName());
        }
        NodeList childNodes = root.getChildNodes();
        for (int i = 0;i < childNodes.getLength();i++){
            Node item = childNodes.item(i);
        printAllNode(item);
        }
    }

    /**
     *  修改xml中某一个节点的内容
     * @param document xml对象
     * @param tagName 修改的节点名字
     * @param index  修改第几个节点
     * @param content 要写入的内容
     * @param xmlPath xml文件的路径
     */
    public static void updateNodeContent(Document document,String tagName,Integer index,String content,String xmlPath) throws TransformerException {
        // 获取所有节点
        NodeList elements = document.getElementsByTagName(tagName);
        // 获取对应的要修改的节点
        Node item = elements.item(index);
        item.setTextContent(content);
        // 修改完写回到文件中
        writeDomToXMLFile(document,xmlPath);
    }

    /**
     * *  向指定节点后添加一个节点
     * @param document xml对象
     * @param tagName 节点名字
     * @param index 第几个节点
     * @param content 节点内容
     * @param filePath xml路径
     * @param append 是否是追加
     */
    public static void addElement(Document document,String tagName,Integer index,String content,String filePath,Boolean append) throws TransformerException {
        NodeList elements = document.getElementsByTagName(tagName);
        Element element = document.createElement(tagName);
        element.setTextContent(content);
        Node item = elements.item(index);
        if (append) {
            // 追加到后面
            item.getParentNode().appendChild(element);
        }else{
            // 放到前边
            item.getParentNode().insertBefore(element,item);
        }
        // 写回文件
        writeDomToXMLFile(document,filePath);
    }

    /**
     *  删除指定tag的第几个节点
     * @param document xml对象
     * @param tagName 要删除的节点的tag名字
     * @param xmlPath xml的文件保存路径
     * @param index 要删除第几个节点
     */
    public static void deleteNode(Document document,String tagName,String xmlPath,Integer index) throws TransformerException {
        NodeList elements = document.getElementsByTagName(tagName);
        Node item = elements.item(index);
        // 删除操作
        item.getParentNode().removeChild(item);
        // 写回文件
        writeDomToXMLFile(document,xmlPath);
    }

    /**
     *  获取指定节点的属性名字
     * @param document
     * @param tagName tag名字
     * @param index 第几个节点
     * @param attributeName 属性名字
     * @return
     */
    public static String getNodeAttribute(Document document,String tagName,Integer index,String attributeName){
        NodeList elements = document.getElementsByTagName(tagName);

        Element item = (Element) elements.item(index);

        String attribute = item.getAttribute(attributeName);
        return attribute;
    }

    /**
     *  给指定节点添加一个属性值
     * @param document
     * @param tagName 节点
     * @param index 第几个节点
     * @param attribute 属性名
     * @param attributeValue 属性值
     */
    public static void addNodeAttribute(Document document,String tagName,Integer index,String attribute,String attributeValue,Boolean save,String xmlPath) throws TransformerException {
        Element item = (Element) document.getElementsByTagName(tagName).item(index);
        item.setAttribute(attribute,attributeValue);
        if (save){
            // 写回文件
            writeDomToXMLFile(document,xmlPath);
        }
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        String path = DomUtil.class.getClassLoader().getResource("SourceXml.xml").getPath();
        String DestPath = DomUtil.class.getClassLoader().getResource("DestXml.xml").getPath();
        logger.info(path);
        Document document = parseXmlToDocument(path);
        Node firstChild = document.getFirstChild();
        // 写入文件
        //writeDomToXMLFile(document,DestPath);
        // 遍历所有节点
        //printAllNode(firstChild);

        // 获取某个节点的content内容
        /*List<String> name = getElementContentByTagName(document, "name");
        for (String s : name) {
            logger.info(s);
        }*/

        // 更新节点内容
        //updateNodeContent(document,"name",1,"码云飞",DestPath);

        // 添加节点
        //addElement(document,"student",1,"菲菲",DestPath,false);

        // 删除节点
        //deleteNode(document,"name",DestPath,1);

        // 获取节点属性值
        String nodeAttribute = getNodeAttribute(document, "student", 1, "id");
        System.out.println(nodeAttribute);

        // 添加节点属性值
        addNodeAttribute(document,"student",1,"资产","数不清",true,DestPath);
    }
}
