package com.journaldev.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaxXMLReader {

    public static void main(String[] args) throws FileNotFoundException {
        String sourceFile = "src/com/journaldev/xml/employee.xml";

        final FileInputStream source = new FileInputStream(sourceFile);
        List<Employee> empList = parseXML(source);

        empList.forEach(System.out::println);
    }

    private static List<Employee> parseXML(InputStream source) {
        List<Employee> empList = new ArrayList<>();
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(source);
            while (xmlEventReader.hasNext()){
                makeNextEmployee(xmlEventReader).ifPresent(empList::add);
            }
        } catch (XMLStreamException e) { e.printStackTrace(); }
        return empList;
    }

    private static Optional<Employee> makeNextEmployee(XMLEventReader xmlEventReader)
            throws XMLStreamException
    {
        Employee emp = null;
        while (xmlEventReader.hasNext()) {
            XMLEvent event = xmlEventReader.nextEvent();
            if (event.isStartElement())
            {
                StartElement startElement = event.asStartElement();
                if (elementIs(startElement, "Employee")) {
                    emp = new Employee();
                    Attribute id = startElement.getAttributeByName(new QName("id"));
                    emp.setId(Integer.parseInt(id.getValue()));
                }
                else if (elementIs(startElement, "age")) {
                    event = xmlEventReader.nextEvent();
                    emp.setAge(Integer.parseInt(event.asCharacters().getData()));
                } else if (elementIs(startElement, "name")) {
                    event = xmlEventReader.nextEvent();
                    emp.setName(event.asCharacters().getData());
                } else if (elementIs(startElement, "gender")) {
                    event = xmlEventReader.nextEvent();
                    emp.setGender(event.asCharacters().getData());
                } else if (elementIs(startElement, "role")) {
                    event = xmlEventReader.nextEvent();
                    emp.setRole(event.asCharacters().getData());
                }
            } else if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (elementIs(endElement, "Employee")) {
                    return Optional.of(emp);
                }
            }
        }
        return Optional.empty();
    }

    private static boolean elementIs(EndElement startElement, String employee) {
        return startElement.getName().getLocalPart().equals(employee);
    }
    private static boolean elementIs(StartElement startElement, String employee) {
        return startElement.getName().getLocalPart().equals(employee);
    }

}
