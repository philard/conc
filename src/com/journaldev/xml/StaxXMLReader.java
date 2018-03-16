package com.journaldev.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.stream.*;

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

//            final Spliterator<XMLEvent> spliterator = Spliterators.spliteratorUnknownSize(xmlEventReader, Spliterator.ORDERED);
//            final Stream<XMLEvent> stream = StreamSupport.stream(spliterator, false);

            final Spliterator<XMLEvent> spliterator1 = ((Iterable<XMLEvent>) () -> xmlEventReader).spliterator();
            final Stream<XMLEvent> stream1 = StreamSupport.stream(spliterator1, false);


            BinaryOperator<XMLEvent> lam = (b, y) -> y;

            List<List<XMLEvent>> list = stream1
                    .collect(unorderedBatches(3, Collectors.toList()));
            list.forEach(System.out::println);


//            while (xmlEventReader.hasNext()){
//                makeNextEmployee(xmlEventReader).ifPresent(empList::add);
//            }
        } catch (XMLStreamException e) { e.printStackTrace(); }
        return empList;
    }


    public static <T, A, R> Collector<T, ?, R> unorderedBatches(
            int batchSize,
            Collector<List<T>, A, R> downstream) {
        class Acc {
            List<T> cur = new ArrayList<>();
            A acc = downstream.supplier().get();
        }
        BiConsumer<Acc, T> accumulator = (acc, t) -> {
            acc.cur.add(t);
            if(acc.cur.size() == batchSize) {
                downstream.accumulator().accept(acc.acc, acc.cur);
                acc.cur = new ArrayList<>();
            }
        };
        return Collector.of(Acc::new, accumulator,
                (acc1, acc2) -> {
                    acc1.acc = downstream.combiner().apply(acc1.acc, acc2.acc);
                    for(T t : acc2.cur) accumulator.accept(acc1, t);
                    return acc1;
                }, acc -> {
                    if(!acc.cur.isEmpty())
                        downstream.accumulator().accept(acc.acc, acc.cur);
                    return downstream.finisher().apply(acc.acc);
                }, Collector.Characteristics.UNORDERED);
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
            } else if (event.isEndElement() && eventIsEmployee(event)) {
                return Optional.of(emp);
            }
        }
        return Optional.empty();
    }

    private static boolean eventIsEmployee(XMLEvent event) {
        return event.asEndElement().getName().getLocalPart().equals("Employee");
    }
    private static boolean elementIs(StartElement startElement, String employee) {
        return startElement.getName().getLocalPart().equals(employee);
    }

}
