package com.onecap.datahub.model;

import java.util.List;

public class Invoice {
    private String irn;
    private String version;
    private List<Item> itemList;
    private String ackNo;
    private ChargesDetails chargesDetails;
    private SellerDetails sellerDetails;
    private DocumentDetails documentDetails;
    private BuyerDetails buyerDetails;
    private String acknowledgementDate;
    private TransactionDetails transactionDetails;

    // Getters and Setters
    public String getIrn() {
        return irn;
    }

    public void setIrn(String irn) {
        this.irn = irn;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public String getAckNo() {
        return ackNo;
    }

    public void setAckNo(String ackNo) {
        this.ackNo = ackNo;
    }

    public ChargesDetails getChargesDetails() {
        return chargesDetails;
    }

    public void setChargesDetails(ChargesDetails chargesDetails) {
        this.chargesDetails = chargesDetails;
    }

    public SellerDetails getSellerDetails() {
        return sellerDetails;
    }

    public void setSellerDetails(SellerDetails sellerDetails) {
        this.sellerDetails = sellerDetails;
    }

    public DocumentDetails getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(DocumentDetails documentDetails) {
        this.documentDetails = documentDetails;
    }

    public BuyerDetails getBuyerDetails() {
        return buyerDetails;
    }

    public void setBuyerDetails(BuyerDetails buyerDetails) {
        this.buyerDetails = buyerDetails;
    }

    public String getAcknowledgementDate() {
        return acknowledgementDate;
    }

    public void setAcknowledgementDate(String acknowledgementDate) {
        this.acknowledgementDate = acknowledgementDate;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public static class Item {
        // Define fields as per API response
        // Getters and Setters
    }

    public static class ChargesDetails {
        private Double cgstValue;
        private Double sgstValue;
        private Double igstValue;
        private Double assesableValue;
        private Double discount;
        private Double othCharges;
        private Double roundOffAmount;
        private Double cessValue;
        private Double totalInvoiceValue;

        // Getters and Setters
        public Double getCgstValue() {
            return cgstValue;
        }

        public void setCgstValue(Double cgstValue) {
            this.cgstValue = cgstValue;
        }

        public Double getSgstValue() {
            return sgstValue;
        }

        public void setSgstValue(Double sgstValue) {
            this.sgstValue = sgstValue;
        }

        public Double getIgstValue() {
            return igstValue;
        }

        public void setIgstValue(Double igstValue) {
            this.igstValue = igstValue;
        }

        public Double getAssesableValue() {
            return assesableValue;
        }

        public void setAssesableValue(Double assesableValue) {
            this.assesableValue = assesableValue;
        }

        public Double getDiscount() {
            return discount;
        }

        public void setDiscount(Double discount) {
            this.discount = discount;
        }

        public Double getOthCharges() {
            return othCharges;
        }

        public void setOthCharges(Double othCharges) {
            this.othCharges = othCharges;
        }

        public Double getRoundOffAmount() {
            return roundOffAmount;
        }

        public void setRoundOffAmount(Double roundOffAmount) {
            this.roundOffAmount = roundOffAmount;
        }

        public Double getCessValue() {
            return cessValue;
        }

        public void setCessValue(Double cessValue) {
            this.cessValue = cessValue;
        }

        public Double getTotalInvoiceValue() {
            return totalInvoiceValue;
        }

        public void setTotalInvoiceValue(Double totalInvoiceValue) {
            this.totalInvoiceValue = totalInvoiceValue;
        }
    }

    public static class SellerDetails {
        private String sellerGstin;
        private String legalName;
        private String address1;
        private String address2;
        private String sellerLocation;
        private int pin;
        private String stateCode;

        // Getters and Setters
        public String getSellerGstin() {
            return sellerGstin;
        }

        public void setSellerGstin(String sellerGstin) {
            this.sellerGstin = sellerGstin;
        }

        public String getLegalName() {
            return legalName;
        }

        public void setLegalName(String legalName) {
            this.legalName = legalName;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getSellerLocation() {
            return sellerLocation;
        }

        public void setSellerLocation(String sellerLocation) {
            this.sellerLocation = sellerLocation;
        }

        public int getPin() {
            return pin;
        }

        public void setPin(int pin) {
            this.pin = pin;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }
    }

    public static class DocumentDetails {
        private String documentType;
        private String supplierDocumentNumber;
        private String documentDate;

        // Getters and Setters
        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getSupplierDocumentNumber() {
            return supplierDocumentNumber;
        }

        public void setSupplierDocumentNumber(String supplierDocumentNumber) {
            this.supplierDocumentNumber = supplierDocumentNumber;
        }

        public String getDocumentDate() {
            return documentDate;
        }

        public void setDocumentDate(String documentDate) {
            this.documentDate = documentDate;
        }
    }

    public static class BuyerDetails {
        private String buyerGstin;
        private String legalName;
        private String address1;
        private String address2;
        private String buyerLocation;
        private int pin;
        private String stateCode;
        private String placeOfSupply;

        // Getters and Setters
        public String getBuyerGstin() {
            return buyerGstin;
        }

        public void setBuyerGstin(String buyerGstin) {
            this.buyerGstin = buyerGstin;
        }

        public String getLegalName() {
            return legalName;
        }

        public void setLegalName(String legalName) {
            this.legalName = legalName;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getBuyerLocation() {
            return buyerLocation;
        }

        public void setBuyerLocation(String buyerLocation) {
            this.buyerLocation = buyerLocation;
        }

        public int getPin() {
            return pin;
        }

        public void setPin(int pin) {
            this.pin = pin;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getPlaceOfSupply() {
            return placeOfSupply;
        }

        public void setPlaceOfSupply(String placeOfSupply) {
            this.placeOfSupply = placeOfSupply;
        }
    }

    public static class TransactionDetails {
        private String taxSchema;
        private String supplierType;
        private String reverseCharge;
        private String intraStateButChargableToIgst;

        // Getters and Setters
        public String getTaxSchema() {
            return taxSchema;
        }

        public void setTaxSchema(String taxSchema) {
            this.taxSchema = taxSchema;
        }

        public String getSupplierType() {
            return supplierType;
        }

        public void setSupplierType(String supplierType) {
            this.supplierType = supplierType;
        }

        public String getReverseCharge() {
            return reverseCharge;
        }

        public void setReverseCharge(String reverseCharge) {
            this.reverseCharge = reverseCharge;
        }

        public String getIntraStateButChargableToIgst() {
            return intraStateButChargableToIgst;
        }

        public void setIntraStateButChargableToIgst(String intraStateButChargableToIgst) {
            this.intraStateButChargableToIgst = intraStateButChargableToIgst;
        }
    }
} 