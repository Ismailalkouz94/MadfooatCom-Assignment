package com.madfooat.billinquiry;

import com.madfooat.billinquiry.base.BaseValidation;
import com.madfooat.billinquiry.domain.Bill;
import com.madfooat.billinquiry.exceptions.InvalidBillInquiryResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLParseBillInquiryResponse implements ParseBillInquiryResponse {
    @Override
    public List<Bill> parse(String billerResponse) throws InvalidBillInquiryResponse {
        // Write your implementation
        List<Bill> billList = new ArrayList<>();
        Bill bill = null;
        BaseValidation baseValidation = new BaseValidation();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(billerResponse)));

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("bill");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node nNode = nodeList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    bill = new Bill();

                    bill.setDueDate(baseValidation.checkDateFormat(eElement.getElementsByTagName("dueDate").item(0).getTextContent()));
                    bill.setDueAmount(new BigDecimal(eElement.getElementsByTagName("dueAmount").item(0).getTextContent()));
                    if (eElement.getElementsByTagName("fees").item(0) != null)
                        bill.setFees(new BigDecimal(eElement.getElementsByTagName("fees").item(0).getTextContent()));
                }
                billList.add(bill);
            }
            baseValidation.validator(billList);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return billList;
    }
}
