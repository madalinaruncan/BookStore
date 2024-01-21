package ro.ubb.bookstore.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.ubb.bookstore.domain.Client;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientXMLRepository extends InMemoryRepository<Long, Client> {
    private String xmlClientFile;


    public ClientXMLRepository(Validator<Client> clientValidator, String xmlClientFile) throws IOException, ParserConfigurationException, SAXException {
        super(clientValidator);
        this.xmlClientFile = xmlClientFile;
        loadData();
    }

    private List<Client> loadData() throws IOException, SAXException, ParserConfigurationException {
        ArrayList<Client> clients = new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(this.xmlClientFile);
        Element clientsElement = document.getDocumentElement();
        NodeList nodeList = clientsElement.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node clientNode = nodeList.item(i);
            if (clientNode instanceof Element) {
                Element clientElement = (Element) clientNode;
                Long id = Long.valueOf(clientElement.getAttribute("id"));
                Client client = getClientFromClientNode(clientElement);
                client.setId(id);
                clients.add(client);
            }
        }

        for (Client c : clients) {
            super.save(c);
        }
        return clients;
    }

    private static Client getClientFromClientNode(Element clientElement) {
        Client client = new Client();


        String firstname = getTextContentFromTag("firstname", clientElement);
        client.setFirstname(firstname);

        String lastname = getTextContentFromTag("lastname", clientElement);
        client.setLastname(lastname);

        return client;
    }

    private static String getTextContentFromTag(String tagName, Element clientElement) {
        NodeList tagList = clientElement.getElementsByTagName(tagName);
        Node titleNode = tagList.item(0);
        return titleNode.getTextContent();
    }

    public Optional<Client> save(Client entity) throws ValidatorException {
        Optional<Client> optional = super.save(entity);
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

    private static void saveToXml(Client client) throws IOException, TransformerException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("./data/clients.xml");
        Element clientsElement = document.getDocumentElement();

        Element newClientElement = document.createElement("client");
        clientsElement.appendChild(newClientElement);

        newClientElement.setAttribute("id", String.valueOf(client.getId()));


        Element newFirstnameElement = document.createElement("firstname");
        newFirstnameElement.setTextContent(client.getFirstname());
        newClientElement.appendChild(newFirstnameElement);

        Element newLastnameElement = document.createElement("lastname");
        newLastnameElement.setTextContent(client.getLastname());
        newClientElement.appendChild(newLastnameElement);


        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream("./data/clients.xml")));
    }

    public Optional<Client> delete(Long id) {
        Optional<Client> deletedClient = super.delete(id);
        if (deletedClient.isPresent()) {
            try {
                rewriteFile();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        }
        return deletedClient;
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

        Element clientElement = document.createElement("client");
        document.appendChild(clientElement);

        for (Client c : super.findAll()) {
            createNodes(document, clientElement, c);
        }

    }
    private void createNodes(Document document, Element clientsElement, Client client) throws TransformerException, FileNotFoundException {

        Element newClientElement = document.createElement("client");

        clientsElement.appendChild(newClientElement);

        newClientElement.setAttribute("id", String.valueOf(client.getId()));

        Element newFirstnameElement = document.createElement("firstname");
        newFirstnameElement.setTextContent(client.getFirstname());
        newClientElement.appendChild(newFirstnameElement);

        Element newLastnameElement = document.createElement("lastname");
        newLastnameElement.setTextContent(client.getLastname());
        newClientElement.appendChild(newLastnameElement);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream("./data/clients.xml")));
    }
}
