/*
 * PayapDoExpressCheckout.java
 * 
 * Copyright (c) 2012 Groovy Fly
 */
package com.groovyfly.site.actions.shopping.paypal;

import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;

import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentReq;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentRequestType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentInfoType;
import urn.ebay.apis.eBLBaseComponents.PaymentStatusCodeType;
import urn.ebay.apis.eBLBaseComponents.PendingStatusCodeType;

import com.groovyfly.common.structures.ShoppingCart;

/**
 * @author Chris Hatton
 */
public class DoExpressCheckoutOperation extends ExpressCheckoutOperation {

    private String payerId;
    
    private String transactionId;
    
    private Date paymentDate;
    
    private String paymentStatus;
    
    private String pendingReason;
    
    private boolean waitingForFundsToClear;
    
    private String fundsToClearMessage;
    
    /**
     * Constructor
     */
    public DoExpressCheckoutOperation(String token, String contextRealPath, String payerId, ShoppingCart shoppingCart) {
        super(token, contextRealPath);
        this.payerId = payerId;
        super.shoppingCart = shoppingCart;
    }

    public void doOperation() throws Exception {
        PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(configPath);
        DoExpressCheckoutPaymentRequestType doCheckoutPaymentRequestType = new DoExpressCheckoutPaymentRequestType();
        DoExpressCheckoutPaymentRequestDetailsType details = new DoExpressCheckoutPaymentRequestDetailsType();

        details.setToken(token);
        details.setPayerID(payerId);
        details.setPaymentAction(PaymentActionCodeType.SALE);

        details.setPaymentDetails(super.getPaymentDetails());

        doCheckoutPaymentRequestType.setDoExpressCheckoutPaymentRequestDetails(details);
        DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
        doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doCheckoutPaymentRequestType);

        DoExpressCheckoutPaymentResponseType doCheckoutPaymentResponseType = service.doExpressCheckoutPayment(doExpressCheckoutPaymentReq);

        if (doCheckoutPaymentResponseType != null) {
            
            if (doCheckoutPaymentResponseType.getAck().toString().equalsIgnoreCase("SUCCESS")) {
                List<PaymentInfoType> paymentInfo = doCheckoutPaymentResponseType.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo();
                if (paymentInfo.size() == 1) {
                    this.transactionId = paymentInfo.get(0).getTransactionID();
                    String paymentDateUTCString = paymentInfo.get(0).getPaymentDate();
                    this.paymentDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(paymentDateUTCString).toGregorianCalendar().getTime();
                    PaymentStatusCodeType paymentStatus = paymentInfo.get(0).getPaymentStatus();
                    this.paymentStatus = paymentStatus.getValue();
                    PendingStatusCodeType pendingReason = paymentInfo.get(0).getPendingReason();
                    this.pendingReason = pendingReason.getValue();
                    
                    this.setMessagForFundsToClear(paymentStatus, pendingReason); 
                    
//                    BasicAmountType feeAmount = paymentInfo.get(0).getFeeAmount();
                    // TODO we will need this for accounting purposes 
                    
                } else {
                    this.errors = doCheckoutPaymentResponseType.getErrors();

                    throw new Exception("Expected one PaymentInfoType but got " + paymentInfo.size());
                }
            } else {
                this.errors = doCheckoutPaymentResponseType.getErrors();

                throw new Exception("Ack code is not success");
            }
        }
    }
    
    private void setMessagForFundsToClear(PaymentStatusCodeType paymentStatus, PendingStatusCodeType pendingReason) {
        /*
         * The two most common statuses are COMPLETE and PENDING. Paypal says not to post stuff until the status
         * is complete so we should give the user a message to tell them they might have to wait a bit until the
         * funds have cleared.
         */
        if (paymentStatus == PaymentStatusCodeType.COMPLETED) {
            this.waitingForFundsToClear = false;
            this.fundsToClearMessage = "";
        } else {
            this.waitingForFundsToClear = true;
            
            if (paymentStatus == PaymentStatusCodeType.PENDING) {
                switch (pendingReason) {
                case ECHECK:
                    this.fundsToClearMessage = "You have choosen to pay with eCheck. This takes between 5 and 7 working days for the funds to clear. We will package and post your order and email you as soon as the funds are cleared.";
                    break;
                case PAYMENTREVIEW:
                    this.fundsToClearMessage = "Your payment is being reviewed by Paypal. There may be a short delay until your payment clears. We will package and post your order and email you as soon as the funds are cleared.";
                    break;
                default:
                    // if anything else then just give a general message
                    this.fundsToClearMessage = "Your payment is currently pending. There may be a short delay until your payment clears. We will package and post your order and email you as soon as the funds are cleared.";
                    break;
                }
            } else {
                // if it is anything other than pending then just process as normal and contact paypal or the customer
                // and trouble-shot the problem
                this.waitingForFundsToClear = false;
                this.fundsToClearMessage = "";
            }
        }
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPendingReason() {
        return pendingReason;
    }

    public String getFundsToClearMessage() {
        return fundsToClearMessage;
    }

    public boolean isWaitingForFundsToClear() {
        return waitingForFundsToClear;
    }
}
