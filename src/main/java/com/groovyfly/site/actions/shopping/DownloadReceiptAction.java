/*
 * DownloadReceiptAction.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;

import com.groovyfly.admin.service.configuration.ConfigurationServiceIF;
import com.groovyfly.admin.service.sales.OrdersServiceIF;
import com.groovyfly.admin.structures.sales.Order;
import com.groovyfly.admin.structures.sales.OrderItem;
import com.groovyfly.common.structures.Address;
import com.groovyfly.common.structures.Company;
import com.groovyfly.site.actions.BaseSiteAction;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Chris Hatton
 */
public class DownloadReceiptAction extends BaseSiteAction implements ServletContextAware {

    private static final long serialVersionUID = 3581393622179756833L;
    
    private static Logger log = Logger.getLogger(DownloadReceiptAction.class.getName());
    
    private ConfigurationServiceIF configurationServiceIF;
    
    private OrdersServiceIF ordersServiceIF;
    
    private ServletContext context;
    
    private String orderNumber;
    
    private Company company;
    
    private Order order;
    
    private Document document;
    
    private Font font10Bold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    
    private Font font24Bold = new Font(Font.FontFamily.TIMES_ROMAN, 24, Font.BOLD);
    
    private Font font10Normal = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    
    private InputStream inputStream;
    
    private int contentLength;
    
    private String filename;
    
    /*
     * This will be set by the String IOC container.
     */
    public void setConfigurationServiceIF(ConfigurationServiceIF configurationServiceIF) {
        this.configurationServiceIF = configurationServiceIF;
    }

    /*
     * This will be set by the String IOC container.
     */
    public void setOrdersServiceIF(OrdersServiceIF ordersServiceIF) {
        this.ordersServiceIF = ordersServiceIF;
    }

    /* 
     * @see org.apache.struts2.util.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    @Override
    public void setServletContext(ServletContext context) {
        this.context = context;
    }

    /* 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() {
        log.info("Download receipt: " + orderNumber);
        
        try {
            this.company = this.configurationServiceIF.getCompany();
            this.order = this.ordersServiceIF.getOrder(orderNumber);
            
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            this.document = new Document();
            PdfWriter.getInstance(document, buffer);
            this.document.open();
            this.createReceipt();
            this.document.close();
            this.inputStream  =  new ByteArrayInputStream(buffer.toByteArray());
            this.contentLength = buffer.size();
            this.filename = "groovy-fly-receipt-" + orderNumber;
            
        } catch (DocumentException e) {
            e.printStackTrace();
            return ActionSupport.ERROR;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ActionSupport.ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return ActionSupport.ERROR;
        } catch(Exception e) {
            e.printStackTrace();
            return ActionSupport.ERROR;
        }

        return ActionSupport.SUCCESS;
    }
      
    private void createReceipt() throws DocumentException, MalformedURLException, IOException {
        this.addHeader();
        this.addOrderAndContact();
        this.addBillingAndShipping();
    }
    
    private void addHeader() throws DocumentException, MalformedURLException, IOException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(5f);
        
        Phrase documentTitlePhrase = new Phrase("Sales Receipt", font24Bold);
        PdfPCell documentTitleCell = new PdfPCell(documentTitlePhrase);
        documentTitleCell.setBorder(PdfPCell.NO_BORDER);
        documentTitleCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        
        String filePath = context.getRealPath("/");
        Image logo = Image.getInstance(filePath + "/images/groovy-fly-logo.png");
        logo.scalePercent(40);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(PdfPCell.NO_BORDER);
        logoCell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        logoCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        
        headerTable.addCell(documentTitleCell);
        headerTable.addCell(logoCell);
        headerTable.addCell(this.getAddressTable("Company Address", company.getName(), company.getAddress()));
        headerTable.addCell(this.getCompanyInfoTable());
        
        document.add(headerTable);
        
    }

    private PdfPTable getAddressTable(String addressTitle, String addresseeName, Address address) {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        headerTable.setWidthPercentage(100);
        
        PdfPCell headerCell = new PdfPCell(new Phrase(addressTitle));
        headerCell.setPaddingBottom(4f);
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        
        Paragraph companyInfoPar = new Paragraph();
        companyInfoPar.setAlignment(Paragraph.ALIGN_LEFT);
        Phrase companyName = new Phrase(addresseeName, font10Bold);
        companyInfoPar.add(companyName);
        companyInfoPar.add(Chunk.NEWLINE);
        
        if (address.getLine1() != null) {
            companyInfoPar.add(new Phrase(address.getLine1(), font10Normal));
            companyInfoPar.add(Chunk.NEWLINE);
        }
        if (address.getLine2() != null) {
            companyInfoPar.add(new Phrase(address.getLine2(), font10Normal));
            companyInfoPar.add(Chunk.NEWLINE);
        }
        if (address.getLine3() != null) {
            companyInfoPar.add(new Phrase(address.getLine3(), font10Normal));
            companyInfoPar.add(Chunk.NEWLINE);
        }
        if (address.getLine4() != null) {
            companyInfoPar.add(new Phrase(address.getLine4(), font10Normal));
            companyInfoPar.add(Chunk.NEWLINE);
        }

        companyInfoPar.add(new Phrase(address.getCountry(), font10Normal));
        companyInfoPar.add(Chunk.NEWLINE);
        companyInfoPar.add(new Phrase(address.getPostcode(), font10Normal));
        PdfPCell companyInfoCell = new PdfPCell(companyInfoPar);
        companyInfoCell.setBorder(PdfPCell.NO_BORDER);

        headerTable.addCell(headerCell);
        headerTable.addCell(companyInfoCell);
        
        return headerTable;
    }
    
    private PdfPTable getCompanyInfoTable() throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        headerTable.setWidthPercentage(100);
        headerTable.setTotalWidth(new float[] {0.2f, 0.8f});
        
        PdfPCell headerCell = new PdfPCell(new Phrase("Contact Info"));
        headerCell.setPaddingBottom(4f);
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        headerCell.setColspan(2);
        
        headerTable.addCell(headerCell);
        headerTable.addCell(new Phrase("Email:", font10Normal));
        headerTable.addCell(new Phrase(company.getEmail(), font10Normal));
        headerTable.addCell(new Phrase("Website:", font10Normal));
        headerTable.addCell(new Phrase(company.getWebsiteUrl(), font10Normal));
        headerTable.addCell(new Phrase("Phone:", font10Normal));
        headerTable.addCell(new Phrase(company.getPhonenNumber(), font10Normal));
        
        return headerTable;
    }
    
    private void addOrderAndContact() throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(5f);
        headerTable.setSpacingAfter(5f);
        
        headerTable.addCell(this.getAddressTable("Shipping Address", order.getShippingAddress().getName(), order.getShippingAddress()));
        headerTable.addCell(this.getOrderInfoTable());
        
        document.add(headerTable);
    }
    
    private PdfPTable getOrderInfoTable() throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        headerTable.setWidthPercentage(100);
        headerTable.setTotalWidth(new float[] {0.3f, 0.7f});
        
        PdfPCell headerCell = new PdfPCell(new Phrase("Order Info"));
        headerCell.setPaddingBottom(4f);
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        headerCell.setColspan(2);
        
        headerTable.addCell(headerCell);
        headerTable.addCell(new Phrase("Order Date:", font10Normal));
        
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        
        headerTable.addCell(new Phrase(df.format(order.getDateCreated()), font10Normal));
        headerTable.addCell(new Phrase("Order numer:", font10Normal));
        headerTable.addCell(new Phrase(order.getOrderId(), font10Normal));
        headerTable.addCell(new Phrase("Payment Method:", font10Normal));
        headerTable.addCell(new Phrase(order.getPaymentProcessor(), font10Normal));
        
        return headerTable;
    }
    
    private void addBillingAndShipping() throws DocumentException {
        String currencyCode = order.getOrderItems().get(0).getCurrencyCode();
        Currency instance = Currency.getInstance(currencyCode);
        String currencySymbol = instance.getSymbol();
        
        DecimalFormat nf = (DecimalFormat) NumberFormat.getCurrencyInstance(this.getLocale());
        DecimalFormatSymbols symbols = nf.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(""); // Don't use null.
        nf.setDecimalFormatSymbols(symbols);
        
        
        PdfPTable headerTable = new PdfPTable(4);
        headerTable.setWidthPercentage(100);
        headerTable.setTotalWidth(new float[] {0.45f, 0.15f, 0.225f, 0.2f});
        
        PdfPCell headerCell1 = new PdfPCell(new Phrase("Description"));
        headerCell1.setPaddingBottom(4f);
        headerCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        
        PdfPCell headerCell2 = new PdfPCell(new Phrase("Quantity"));
        headerCell2.setPaddingBottom(4f);
        headerCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        
        PdfPCell headerCell3 = new PdfPCell(new Phrase("Item Price ("+ currencySymbol + ")"));
        headerCell3.setPaddingBottom(4f);
        headerCell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
        
        PdfPCell headerCell4 = new PdfPCell(new Phrase("Total Price (" + currencySymbol + ")"));
        headerCell4.setPaddingBottom(4f);
        headerCell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
        
        headerTable.addCell(headerCell1);
        headerTable.addCell(headerCell2);
        headerTable.addCell(headerCell3);
        headerTable.addCell(headerCell4);
        
        headerTable.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

        for (OrderItem oi : order.getOrderItems()) {
            headerTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            headerTable.addCell(oi.getName());
            headerTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            headerTable.addCell(Integer.toString(oi.getQuantity()));
            headerTable.addCell(nf.format(oi.getUnitPrice().doubleValue()));
            headerTable.addCell(nf.format(oi.getTotalPrice().doubleValue()));
        }
        
        headerTable.getDefaultCell().setPaddingTop(5f);
        headerTable.getDefaultCell().setPaddingBottom(5f);
        headerTable.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        headerTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        
        PdfPCell headerCell11 = new PdfPCell(new Phrase("Sub-total (" + currencySymbol + "):"));
        headerCell11.setColspan(3);
        headerCell11.setPaddingTop(5f);
        headerCell11.setPaddingBottom(5f);
        headerCell11.setBorder(PdfPCell.NO_BORDER);
        headerCell11.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        headerTable.addCell(headerCell11);
        
        headerTable.addCell(nf.format(order.getSubTotalAmount().doubleValue()));
        
        PdfPCell headerCell22 = new PdfPCell(new Phrase("Discount (" + currencySymbol + "):"));
        headerCell22.setColspan(3);
        headerCell22.setPaddingTop(5f);
        headerCell22.setPaddingBottom(5f);
        headerCell22.setBorder(PdfPCell.NO_BORDER);
        headerCell22.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        headerTable.addCell(headerCell22);
        
        headerTable.addCell(nf.format(order.getDiscountAmount().doubleValue()));
        
        PdfPCell headerCell33 = new PdfPCell(new Phrase("Vat @ 20% (" + currencySymbol + "):"));
        headerCell33.setColspan(3);
        headerCell33.setPaddingTop(5f);
        headerCell33.setPaddingBottom(5f);
        headerCell33.setBorder(PdfPCell.NO_BORDER);
        headerCell33.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        headerTable.addCell(headerCell33);
        
        headerTable.addCell(nf.format(order.getVatAmount().doubleValue()));
        
        PdfPCell headerCell55 = new PdfPCell(new Phrase("Postage and Packing (" + currencySymbol + "):"));
        headerCell55.setColspan(3);
        headerCell55.setPaddingTop(5f);
        headerCell55.setPaddingBottom(5f);
        headerCell55.setBorder(PdfPCell.NO_BORDER);
        headerCell55.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        headerTable.addCell(headerCell55);
        
        headerTable.addCell(nf.format(order.getPostageAndPacking().doubleValue()));
        
        PdfPCell headerCell44 = new PdfPCell(new Phrase("Total (" + currencySymbol + "):"));
        headerCell44.setColspan(3);
        headerCell44.setPaddingTop(5f);
        headerCell44.setPaddingBottom(5f);
        headerCell44.setBorder(PdfPCell.NO_BORDER);
        headerCell44.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        headerTable.addCell(headerCell44);

        
        headerTable.getDefaultCell().setPaddingTop(8f);
        headerTable.getDefaultCell().setPaddingBottom(8f);
        headerTable.getDefaultCell().setBorderWidthTop(2f);
        headerTable.getDefaultCell().setBorderWidthBottom(2f);
        headerTable.addCell(nf.format(order.getTotal().doubleValue()));
        
        document.add(headerTable);
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getContentLength() {
        return contentLength;
    }
    
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
