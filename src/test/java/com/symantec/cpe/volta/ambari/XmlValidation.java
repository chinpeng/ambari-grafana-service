package com.symantec.cpe.volta.ambari;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlValidation {
	
	@Test
	public void testMetaInfoXML() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		
		SchemaFactory schemaFactory = SchemaFactory
		    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File("src/test/resources/metainfo.xsd"));
		Validator validator = schema.newValidator();

		DocumentBuilder builder = factory.newDocumentBuilder();
		
		File config = new File("src/service/metainfo.xml");

		System.out.println("Validating xml config template:"+config);
		// the "parse" method also validates XML, will throw an exception if misformatted
		InputSource source = new InputSource(config.getAbsolutePath());
		Document document = builder.parse(source);
		assertTrue(document!=null);
		
		validator.validate(new DOMSource(document));
	}

	@Test
	public void testXMLConfigurations() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		
		SchemaFactory schemaFactory = SchemaFactory
		    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File("src/test/resources/ambari-config.xsd"));
		Validator validator = schema.newValidator();

		DocumentBuilder builder = factory.newDocumentBuilder();

		File[] templateConfigs = new File("src/service/configuration/").listFiles(new FileFilter() {
			
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".xml");
			}
		});
		
		for(File config:templateConfigs) {
			System.out.println("Validating xml config template:"+config);
			// the "parse" method also validates XML, will throw an exception if misformatted
			InputSource source = new InputSource(config.getAbsolutePath());
			Document document = builder.parse(source);
			assertTrue(document!=null);
			
			validator.validate(new DOMSource(document));
		}
			
	}
	
}
