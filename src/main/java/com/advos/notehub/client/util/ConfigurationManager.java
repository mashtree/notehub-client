package com.advos.notehub.client.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.advos.notehub.client.entity.Repository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author triyono
 */
public class ConfigurationManager {

    /**
     * create Note's directory and version marker
     *
     * @param selectedDirectory
     * @throws IOException
     */
    public void createFileConf(File selectedDirectory, String pathFile, Repository rep) throws IOException {
        /**
         * DIRECTORY'S STRUCTURE note's name - dir
         */
        FileModer fm = new FileModer();
        String fileName = selectedDirectory.getName() + ".txt";
        String htmlFileName = selectedDirectory.getName() + ".htm";
        String str = "<html dir=\"ltr\">\n"
                + "<head>\n"
                + "</head>\n"
                + "<body contenteditable=\"true\">\n"
                + "</body>\n"
                + "</html>";
        if (!new File(pathFile + "/" +fileName).exists()) {
            fm.writeFile("", pathFile + "/" + fileName);
        }
        if (!new File(pathFile + "/" +htmlFileName).exists()) {
            fm.writeFile(str, pathFile + "/" + htmlFileName);
        }

        //String dirImages = pathFile+"/images/";
        //String dirConf = pathFile+"/.conf/";
        String confContent = "0:" + selectedDirectory.getName() + ":0";
        String confFile = "conf";//dirConf+"notehub.xml";
        if (!new File(pathFile + "/" + confFile).exists()) {
            fm.writeFile(confContent, pathFile + "/" + confFile);
        }else{ //when conf file exist, put the server note's id information to local database
            List<String> content = fm.readFile(pathFile + "/" + confFile);
            if(content.size()>0){
                String[] conf = content.get(0).split(":");
                rep.setId_on_server(Integer.parseInt(conf[0]));
            }
        }

        //fm.createDirectory(dirConf);
        //fm.createDirectory(dirImages);
        //fm.writeFile("", confFile);
        //ConfigurationManager cm = new ConfigurationManager();
        //cm.createXMLFile(confFile, selectedDirectory.getName());
        //System.out.println(pathFile+"/.conf/"+confFile);
    }
    
    public void updateConfFile(String pathFile, Repository rep, int UID){
        try {
            FileModer fm = new FileModer();
            String confContent = rep.getId_on_server()+":" + rep.getName_repo() + ":"+UID;
            String confFile = "conf";
            fm.writeFile(confContent, pathFile + "/" + confFile);
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createXMLFile(File file, String noteName) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root
            Document doc = (Document) docBuilder.newDocument();
            Element rootElement = doc.createElement("configuration");
            doc.appendChild(rootElement);

            //name
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(noteName));
            rootElement.appendChild(name);

            //first commit
            Element firstCommit = doc.createElement("firstcommit");
            firstCommit.appendChild(doc.createTextNode("0000-00-00 00:00:00"));
            rootElement.appendChild(firstCommit);

            //user
            Element user = doc.createElement("user");
            user.appendChild(doc.createTextNode(""));
            rootElement.appendChild(user);

            //version 1.0
            Element version = doc.createElement("version");
            version.appendChild(doc.createTextNode("1.0"));
            rootElement.appendChild(version);

            //location (local)
            Element location = doc.createElement("location");
            location.appendChild(doc.createTextNode(file.getAbsolutePath()));
            rootElement.appendChild(location);

            //unique id
            Element id = doc.createElement("idDoc");
            id.appendChild(doc.createTextNode("0"));
            rootElement.appendChild(id);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            System.out.println("File saved");

        } catch (ParserConfigurationException pce) {
            System.out.println("Parser Configuration Exception : " + pce.getMessage());
        } catch (TransformerException tce) {
            System.out.println("Transformer Exception : " + tce.getMessage());
        }

    }

    public void createXMLFile(String fullFilePath, String noteName) {
        File file = new File(fullFilePath);
        createXMLFile(file, noteName);
    }

    public void readXMLFile(String fullFilePath) {
        File file = new File(fullFilePath);
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(file);
            doc.getDocumentElement().normalize();

            System.out.println("Root element : " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("configuration");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                System.out.println(node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println(element.getElementsByTagName("name").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("firstcommit").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("user").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("version").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("location").item(0).getTextContent());
                    System.out.println(element.getElementsByTagName("idDoc").item(0).getTextContent());
                }
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("Parser Configuration Exception : " + pce.getMessage());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

    }

    public void updateXMLFile(String fullFilePath) throws org.xml.sax.SAXException {
        File file = new File(fullFilePath);
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(file);

            // Get the root element
            Node company = doc.getFirstChild();

            // Get the staff element , it may not working if tag has spaces, or
            // whatever weird characters in front...it's better to use
            // getElementsByTagName() to get it directly.
            // Node staff = company.getFirstChild();
            // Get the staff element by tag name directly
            Node staff = doc.getElementsByTagName("staff").item(0);

            // update staff attribute
            NamedNodeMap attr = staff.getAttributes();
            Node nodeAttr = attr.getNamedItem("id");
            nodeAttr.setTextContent("2");

            // append a new node to staff
            Element age = doc.createElement("age");
            age.appendChild(doc.createTextNode("28"));
            staff.appendChild(age);

            // loop the staff child node
            NodeList list = staff.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                // get the salary element, and update the value
                if ("salary".equals(node.getNodeName())) {
                    node.setTextContent("2000000");
                }

                //remove firstname
                if ("firstname".equals(node.getNodeName())) {
                    staff.removeChild(node);
                }

            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            System.out.println("Done");

        } catch (ParserConfigurationException pce) {
            System.out.println("Parser Configuration Exception : " + pce.getMessage());
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void countElement(String element, String fullFilePath) {

    }

    public static void main(String[] args) {
        ConfigurationManager xm = new ConfigurationManager();
        String filepath = "C:\\Users\\aisyahumar\\Documents\\PENNSTATE\\Semester I\\COMP 512\\latihan\\notehub-client\\note.xml";
        xm.createXMLFile(filepath, "testxml");
        xm.readXMLFile(filepath);
    }
}
