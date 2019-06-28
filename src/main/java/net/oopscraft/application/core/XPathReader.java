package net.oopscraft.application.core;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathReader {

	DocumentBuilderFactory dbf = null;
	DocumentBuilder db = null;
	Document document = null;
	XPath xPath = null;
	
	/**
	 * constructor from file descriptor
	 * @param xmlFile
	 * @throws Exception
	 */
	public XPathReader(File xmlFile) throws Exception {
		this.dbf = createDefaultDocumentBuilderFactory();
		this.db = dbf.newDocumentBuilder();
		this.document = db.parse(xmlFile);
		this.xPath = XPathFactory.newInstance().newXPath();
	}
	
	/**
	 * constructor from InputStream
	 * @param is
	 * @throws Exception
	 */
	public XPathReader(InputStream is) throws Exception {
		this.dbf = createDefaultDocumentBuilderFactory();
		this.db = dbf.newDocumentBuilder();
		this.document = db.parse(is);
		this.xPath = XPathFactory.newInstance().newXPath();
	}
	
	/*
	 * createDefaultDocumentBuilderFactory
	 */
	private DocumentBuilderFactory createDefaultDocumentBuilderFactory() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setIgnoringComments(false);
		return documentBuilderFactory;
	}
	
	/**
	 * getting element
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public Object getElement(String expression) throws Exception {
		return xPath.evaluate(expression, document, XPathConstants.NODESET);
	}
	
	/**
	 * hasElement
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public boolean hasElement(String expression) throws Exception {
		NodeList nodeList = (NodeList) xPath.evaluate(expression, document, XPathConstants.NODESET);
		if(nodeList.getLength() > 0) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * getting text contents
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public String getTextContent(String expression) throws Exception {
		if(hasElement(expression)) {
			return ((Node)xPath.evaluate(expression, document, XPathConstants.NODE)).getTextContent();
		}else {
			return null;
		}
	}
	

	
	

}
