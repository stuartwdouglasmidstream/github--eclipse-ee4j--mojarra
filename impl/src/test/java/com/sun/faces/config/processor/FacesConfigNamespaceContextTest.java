package com.sun.faces.config.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FacesConfigNamespaceContextTest {

	@Test
	public void testJavaEENSWithoutParameter() throws ParserConfigurationException, XPathExpressionException {
		Document docFlowConfig = createFacesConfig("simple", "http://xmlns.jcp.org/xml/ns/javaee", "2.2");
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new FacesConfigNamespaceContext());
		NodeList returns = (NodeList) xpath.evaluate(".//ns1:flow-return", docFlowConfig, XPathConstants.NODESET);
		assertNotNull(returns);
		// xpath fails to return nodes because namespace does not match
		assertEquals(0, returns.getLength());
	}

	@Test
	public void testJavaEENSWithParameter() throws ParserConfigurationException, XPathExpressionException {
		Document docFlowConfig = createFacesConfig("simple", "http://xmlns.jcp.org/xml/ns/javaee", "2.2");
		String namespace = docFlowConfig.getDocumentElement().getNamespaceURI();
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new FacesConfigNamespaceContext(namespace));
		NodeList returns = (NodeList) xpath.evaluate(".//ns1:flow-return", docFlowConfig, XPathConstants.NODESET);
		assertNotNull(returns);
		assertEquals(1, returns.getLength());
	}

	@Test
	public void testJakartaEENSWithoutParameter() throws ParserConfigurationException, XPathExpressionException {
		Document docFlowConfig = createFacesConfig("simple", "https://jakarta.ee/xml/ns/jakartaee", "3.0");
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new FacesConfigNamespaceContext());
		NodeList returns = (NodeList) xpath.evaluate(".//ns1:flow-return", docFlowConfig, XPathConstants.NODESET);
		assertNotNull(returns);
		assertEquals(1, returns.getLength());
	}

	@Test
	public void testJakartaEENSWithParameter() throws ParserConfigurationException, XPathExpressionException {
		Document docFlowConfig = createFacesConfig("simple", "https://jakarta.ee/xml/ns/jakartaee", "3.0");
		String namespace = docFlowConfig.getDocumentElement().getNamespaceURI();
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new FacesConfigNamespaceContext(namespace));
		NodeList returns = (NodeList) xpath.evaluate(".//ns1:flow-return", docFlowConfig, XPathConstants.NODESET);
		assertNotNull(returns);
		assertEquals(1, returns.getLength());
	}

	private Document createFacesConfig(String flowName, String namespace, String version)
			throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document docFlowConfig = documentBuilder.newDocument();
		Element eleFacesConfig = docFlowConfig.createElementNS(namespace, "faces-config");
		eleFacesConfig.setAttribute("version", version);
		Element flowDefinition = docFlowConfig.createElementNS(namespace, "flow-definition");
		flowDefinition.setAttribute("id", flowName);
		eleFacesConfig.appendChild(flowDefinition);
		final String flowReturnStr = flowName + "-return";

		Element flowReturn = docFlowConfig.createElementNS(namespace, "flow-return");
		flowReturn.setAttribute("id", flowReturnStr);
		flowDefinition.appendChild(flowReturn);

		Element fromOutcome = docFlowConfig.createElementNS(namespace, "from-outcome");
		flowReturn.appendChild(fromOutcome);
		fromOutcome.setTextContent("/" + flowReturnStr);
		docFlowConfig.appendChild(eleFacesConfig);
		return docFlowConfig;
	}

}
