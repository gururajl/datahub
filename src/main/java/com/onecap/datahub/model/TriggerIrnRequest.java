package com.onecap.datahub.model;

public class TriggerIrnRequest {
    private String gstin;
    private ReturnPeriod returnPeriod;
    private SupplierType supplierType;
    private InvoiceType invoiceType;

    public TriggerIrnRequest(String gstin, ReturnPeriod returnPeriod, SupplierType supplierType, InvoiceType invoiceType) {
        this.gstin = gstin;
        this.returnPeriod = returnPeriod;
        this.supplierType = supplierType;
        this.invoiceType = invoiceType;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public ReturnPeriod getReturnPeriod() {
        return returnPeriod;
    }

    public void setReturnPeriod(ReturnPeriod returnPeriod) {
        this.returnPeriod = returnPeriod;
    }

    public SupplierType getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(SupplierType supplierType) {
        this.supplierType = supplierType;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public enum SupplierType {
        B2B, B2C, EXPORT
    }

    public enum InvoiceType {
        SALES, PURCHASE, CREDIT_NOTE, DEBIT_NOTE
    }
} 