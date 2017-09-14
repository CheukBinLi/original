package com.cheuks.bin.original.common.util.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cheuks.bin.original.common.util.ReflectionCache;
import com.cheuks.bin.original.common.util.ReflectionUtil;
import com.cheuks.bin.original.common.util.conver.ObjectFill;

/***
 * xml数据填充
 * 
 * @author Ben-Book
 *
 */
public class XmlReaderAll extends DefaultHandler {

    private ReflectionCache reflectionCache = ReflectionCache.newInstance();

    private ReflectionUtil reflectionUtil = ReflectionUtil.instance();

    private final ObjectFill objectFill = new ObjectFill();

    private static XmlReaderAll INSTANCE;

    public static XmlReaderAll newInstance() {
        if (null == INSTANCE) {
            synchronized (XmlReaderAll.class) {
                if (null == INSTANCE) {
                    INSTANCE = new XmlReaderAll();
                }
            }
        }
        return INSTANCE;
    }

    SAXParserFactory factory = SAXParserFactory.newInstance();

    SAXParser parser = null;
    {
        try {
            parser = factory.newSAXParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T paddingModel(byte[] bytes, Class<T> obj) throws NoSuchFieldException, SecurityException, ParserConfigurationException, SAXException, IOException, InstantiationException, IllegalAccessException {
        return new XmlReaderAll().padding(bytes, obj);
    }

    public synchronized <T> T padding(byte[] bytes, Class<T> obj) throws ParserConfigurationException, SAXException, IOException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException {
        return padding(bytes, obj.newInstance());
    }

    public synchronized <T> T padding(byte[] bytes, T obj) throws ParserConfigurationException, SAXException, IOException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException {
        link.clear();
        T result = obj;
        link.addFirst(new Node(null, null, result));
        XmlReaderAll handler = this;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        InputSource is = new InputSource(in);
        is.setEncoding("utf-8");
        parser.parse(is, handler);

        T t = (T) result;
        // 译放obj
        result = null;
        return t;
    }

    static class Node {
        private String tagName;

        private Field field;

        private Object obj;

        public String getTagName() {
            return tagName;
        }

        public Node setTagName(String tagName) {
            this.tagName = tagName;
            return this;
        }

        public Object getObj() {
            return obj;
        }

        public Node setObj(Object obj) {
            this.obj = obj;
            return this;
        }

        public Field getField() {
            return field;
        }

        public Node setField(Field field) {
            this.field = field;
            return this;
        }

        public Node(String tagName, Field field, Object obj) {
            super();
            this.tagName = tagName;
            this.field = field;
            this.obj = obj;
        }
    }

    LinkedList<Node> link = new LinkedList<XmlReaderAll.Node>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Object X = link.getLast().getObj();
        Field f = null;
        Object o = null;
        try {
            try {
                // f = X.getClass().getDeclaredField(qName);
                f = reflectionCache.getFieldByMap(X.getClass(), qName);
            } catch (Exception e) {
                e.printStackTrace();
                // f = X.getClass().getSuperclass().getDeclaredField(qName);
                f = reflectionCache.getFieldByMap(X.getClass().getSuperclass(), qName);
            }
            if (null == f)
                return;
            f.setAccessible(true);
            o = f.get(X);
            if (null == o && f.getType().isPrimitive() && !reflectionUtil.isWrapperClass(f.getType()))
                f.set(X, o = Class.forName(f.getType().getName()).newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != f || null != o)
            link.addLast(new Node(qName, f, o));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (!link.isEmpty() && null != link.getLast().getTagName() && link.getLast().getTagName().equals(qName))
            link.removeLast();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (link.size() == 1)
            return;
        Node node = link.removeLast();
        Object o = link.getLast().getObj();
        Field f = node.getField();
        try {
            f.setAccessible(true);
            // f.set(o, new String(ch, start, length));
            f.set(o, objectFill.getValue(f.getType(), new String(ch, start, length)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
