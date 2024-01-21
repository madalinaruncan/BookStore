package ro.ubb.bookstore.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.ubb.bookstore.domain.Book;
import ro.ubb.bookstore.domain.validators.Validator;
import ro.ubb.bookstore.domain.validators.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class BookXMLRepository extends InMemoryRepository<Long, Book> {

    private String xmlBookFile;


    public BookXMLRepository(Validator<Book> bookValidator, String xmlBookFile) throws IOException, ParserConfigurationException, SAXException {
        super(bookValidator);
        this.xmlBookFile = xmlBookFile;
        loadData();
    }

    private List<Book> loadData() throws IOException, SAXException, ParserConfigurationException {
        ArrayList<Book> books = new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(this.xmlBookFile);
        Element booksElement = document.getDocumentElement();
        NodeList nodeList = booksElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node bookNode = nodeList.item(i);
            if (bookNode instanceof Element) {
                Element bookElement = (Element) bookNode;
                Long id = Long.valueOf(bookElement.getAttribute("id"));
                Book book = getBookFromBookNode(bookElement);
                book.setId(id);
                books.add(book);
            }
        }

        for(Book b : books) {
            super.save(b);
        }
        return books;
    }



    private static Book getBookFromBookNode(Element bookElement) {
        Book book = new Book();

        String title = getTextContentFromTag("title", bookElement);
        book.setTitle(title);

        String author_firstname = getTextContentFromTag("author_firstname", bookElement);
        book.setAuthor_firstname(author_firstname);

        String author_lastname = getTextContentFromTag("author_lastname", bookElement);
        book.setAuthor_lastname(author_lastname);

        String release_year = getTextContentFromTag("release_year", bookElement);
        book.setRelease_year(Integer.parseInt(release_year));

        String price = getTextContentFromTag("price", bookElement);
        book.setPrice(Double.parseDouble(price));

        return book;
    }

    private static String getTextContentFromTag(String tagName, Element bookElement) {
        NodeList tagList = bookElement.getElementsByTagName(tagName);
        Node titleNode = tagList.item(0);
        return titleNode.getTextContent();
    }

    public Optional<Book> save(Book entity) throws ValidatorException {
        Optional<Book> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        try {
            saveToXml(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private static void saveToXml(Book book) throws IOException, TransformerException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("./data/books.xml");
        Element booksElement = document.getDocumentElement();

        Element newBookElement = document.createElement("book");
        booksElement.appendChild(newBookElement);

        newBookElement.setAttribute("id", String.valueOf(book.getId()));

        Element newTitleElement = document.createElement("title");
        newTitleElement.setTextContent(book.getTitle());
        newBookElement.appendChild(newTitleElement);

        Element newAuthorFirstnameElement = document.createElement("author_firstname");
        newAuthorFirstnameElement.setTextContent(book.getAuthor_firstname());
        newBookElement.appendChild(newAuthorFirstnameElement);

        Element newAuthorLastnameElement = document.createElement("author_lastname");
        newAuthorLastnameElement.setTextContent(book.getAuthor_lastname());
        newBookElement.appendChild(newAuthorLastnameElement);

        Element newReleaseYearElement = document.createElement("release_year");
        newReleaseYearElement.setTextContent(String.valueOf(book.getRelease_year()));
        newBookElement.appendChild(newReleaseYearElement);

        Element newPriceElement = document.createElement("price");
        newPriceElement.setTextContent(String.valueOf(book.getPrice()));
        newBookElement.appendChild(newPriceElement);


        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream("./data/books.xml")));
    }

    public Optional<Book> delete(Long id) {
    Optional<Book> deletedBook = super.delete(id);
    if(deletedBook.isPresent()){
        try {
            rewriteFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
    return deletedBook;
    }

    private void rewriteFile() throws FileNotFoundException, TransformerException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document = documentBuilder.newDocument();

        Element bookElement = document.createElement("book");
        document.appendChild(bookElement);

        for(Book b: super.findAll()){
            createNodes(document, bookElement, b);
        }

    }

    private void createNodes(Document document, Element booksElement, Book book) throws TransformerException, FileNotFoundException {

    Element newBookElement = document.createElement("book");

        booksElement.appendChild(newBookElement);

        newBookElement.setAttribute("id", String.valueOf(book.getId()));

        Element newTitleElement = document.createElement("title");
        newTitleElement.setTextContent(book.getTitle());
        newBookElement.appendChild(newTitleElement);

        Element newAuthorFirstnameElement = document.createElement("author_firstname");
        newAuthorFirstnameElement.setTextContent(book.getAuthor_firstname());
        newBookElement.appendChild(newAuthorFirstnameElement);

        Element newAuthorLastnameElement = document.createElement("author_lastname");
        newAuthorLastnameElement.setTextContent(book.getAuthor_lastname());
        newBookElement.appendChild(newAuthorLastnameElement);

        Element newReleaseYearElement = document.createElement("release_year");
        newReleaseYearElement.setTextContent(String.valueOf(book.getRelease_year()));
        newBookElement.appendChild(newReleaseYearElement);

        Element newPriceElement = document.createElement("price");
        newPriceElement.setTextContent(String.valueOf(book.getPrice()));
        newBookElement.appendChild(newPriceElement);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream("./data/books.xml")));
    }




}
