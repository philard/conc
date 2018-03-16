package com.javarticles.jaxp;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class StAXEventReaderExample {
    public static void main(String[] args) throws XMLStreamException,
            FileNotFoundException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader("sample.xml",
                new FileInputStream("src/com/javarticles/jaxp/sample.xml"));

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement element = (StartElement) event;
                System.out.println("Start Element: " + element.getName());

                Iterator iterator = element.getAttributes();
                while (iterator.hasNext()) {
                    Attribute attribute = (Attribute) iterator.next();
                    QName name = attribute.getName();
                    String value = attribute.getValue();
                    System.out.println("Attribute name/value: " + name + "/"
                            + value);
                }
            }
            if (event.isEndElement()) {
                EndElement element = (EndElement) event;
                System.out.println("End element:" + element.getName());
            }
            if (event.isCharacters()) {
                Characters characters = (Characters) event;
                System.out.println("Text:[" + characters.getData() + "]");
            }
        }
    }
}
