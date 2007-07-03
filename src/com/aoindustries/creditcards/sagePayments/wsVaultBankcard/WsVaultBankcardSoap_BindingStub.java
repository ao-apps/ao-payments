/**
 * WsVaultBankcardSoap_BindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVaultBankcard;

public class WsVaultBankcardSoap_BindingStub extends org.apache.axis.client.Stub implements com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardSoap_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[4];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("VAULT_BANKCARD_SALE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_NAME"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_ADDRESS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_CITY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_STATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_ZIP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_COUNTRY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_EMAIL"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_CUSTOMER_NUMBER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_AMT"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_SHIPPING"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_TAX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_ORDERNUM"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_TELEPHONE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_FAX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_NAME"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_ADDRESS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_CITY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_STATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_ZIP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_COUNTRY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_SALEResponse>VAULT_BANKCARD_SALEResult"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_SALEResponseVAULT_BANKCARD_SALEResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_SALEResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("VAULT_BANKCARD_AUTHONLY");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_NAME"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_ADDRESS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_CITY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_STATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_ZIP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_COUNTRY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_EMAIL"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_CUSTOMER_NUMBER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_AMT"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_SHIPPING"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_TAX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_ORDERNUM"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_TELEPHONE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_FAX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_NAME"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_ADDRESS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_CITY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_STATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_ZIP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_COUNTRY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_AUTHONLYResponse>VAULT_BANKCARD_AUTHONLYResult"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_AUTHONLYResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("VAULT_BANKCARD_FORCE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_NAME"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_ADDRESS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_CITY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_STATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_ZIP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_COUNTRY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_EMAIL"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_CUSTOMER_NUMBER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_AMT"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_SHIPPING"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_TAX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_ORDERNUM"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_TELEPHONE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_FAX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_NAME"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_ADDRESS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_CITY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_STATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_ZIP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_COUNTRY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_FORCEResponse>VAULT_BANKCARD_FORCEResult"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_FORCEResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("VAULT_BANKCARD_CREDIT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_NAME"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_ADDRESS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_CITY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_STATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_ZIP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_COUNTRY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_EMAIL"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_CUSTOMER_NUMBER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_AMT"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_SHIPPING"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_TAX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "T_ORDERNUM"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_TELEPHONE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_FAX"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_NAME"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_ADDRESS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_CITY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_STATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_ZIP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "C_SHIP_COUNTRY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_CREDITResponse>VAULT_BANKCARD_CREDITResult"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_CREDITResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

    }

    public WsVaultBankcardSoap_BindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public WsVaultBankcardSoap_BindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public WsVaultBankcardSoap_BindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_AUTHONLYResponse>VAULT_BANKCARD_AUTHONLYResult");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_CREDITResponse>VAULT_BANKCARD_CREDITResult");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_FORCEResponse>VAULT_BANKCARD_FORCEResult");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_SALEResponse>VAULT_BANKCARD_SALEResult");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_SALEResponseVAULT_BANKCARD_SALEResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">DataSet");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.DataSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_AUTHONLY");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLY.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_AUTHONLYResponse");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_CREDIT");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDIT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_CREDITResponse");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_FORCE");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCE.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_FORCEResponse");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_SALEResponseVAULT_BANKCARD_SALEResult VAULT_BANKCARD_SALE(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String c_NAME, java.lang.String c_ADDRESS, java.lang.String c_CITY, java.lang.String c_STATE, java.lang.String c_ZIP, java.lang.String c_COUNTRY, java.lang.String c_EMAIL, java.lang.String GUID, java.lang.String t_CUSTOMER_NUMBER, java.lang.String t_AMT, java.lang.String t_SHIPPING, java.lang.String t_TAX, java.lang.String t_ORDERNUM, java.lang.String c_TELEPHONE, java.lang.String c_FAX, java.lang.String c_SHIP_NAME, java.lang.String c_SHIP_ADDRESS, java.lang.String c_SHIP_CITY, java.lang.String c_SHIP_STATE, java.lang.String c_SHIP_ZIP, java.lang.String c_SHIP_COUNTRY) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard/VAULT_BANKCARD_SALE");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_SALE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, c_NAME, c_ADDRESS, c_CITY, c_STATE, c_ZIP, c_COUNTRY, c_EMAIL, GUID, t_CUSTOMER_NUMBER, t_AMT, t_SHIPPING, t_TAX, t_ORDERNUM, c_TELEPHONE, c_FAX, c_SHIP_NAME, c_SHIP_ADDRESS, c_SHIP_CITY, c_SHIP_STATE, c_SHIP_ZIP, c_SHIP_COUNTRY});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_SALEResponseVAULT_BANKCARD_SALEResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_SALEResponseVAULT_BANKCARD_SALEResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_SALEResponseVAULT_BANKCARD_SALEResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult VAULT_BANKCARD_AUTHONLY(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String c_NAME, java.lang.String c_ADDRESS, java.lang.String c_CITY, java.lang.String c_STATE, java.lang.String c_ZIP, java.lang.String c_COUNTRY, java.lang.String c_EMAIL, java.lang.String GUID, java.lang.String t_CUSTOMER_NUMBER, java.lang.String t_AMT, java.lang.String t_SHIPPING, java.lang.String t_TAX, java.lang.String t_ORDERNUM, java.lang.String c_TELEPHONE, java.lang.String c_FAX, java.lang.String c_SHIP_NAME, java.lang.String c_SHIP_ADDRESS, java.lang.String c_SHIP_CITY, java.lang.String c_SHIP_STATE, java.lang.String c_SHIP_ZIP, java.lang.String c_SHIP_COUNTRY) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard/VAULT_BANKCARD_AUTHONLY");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_AUTHONLY"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, c_NAME, c_ADDRESS, c_CITY, c_STATE, c_ZIP, c_COUNTRY, c_EMAIL, GUID, t_CUSTOMER_NUMBER, t_AMT, t_SHIPPING, t_TAX, t_ORDERNUM, c_TELEPHONE, c_FAX, c_SHIP_NAME, c_SHIP_ADDRESS, c_SHIP_CITY, c_SHIP_STATE, c_SHIP_ZIP, c_SHIP_COUNTRY});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult VAULT_BANKCARD_FORCE(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String c_NAME, java.lang.String c_ADDRESS, java.lang.String c_CITY, java.lang.String c_STATE, java.lang.String c_ZIP, java.lang.String c_COUNTRY, java.lang.String c_EMAIL, java.lang.String GUID, java.lang.String t_CUSTOMER_NUMBER, java.lang.String t_AMT, java.lang.String t_SHIPPING, java.lang.String t_TAX, java.lang.String t_ORDERNUM, java.lang.String c_TELEPHONE, java.lang.String c_FAX, java.lang.String c_SHIP_NAME, java.lang.String c_SHIP_ADDRESS, java.lang.String c_SHIP_CITY, java.lang.String c_SHIP_STATE, java.lang.String c_SHIP_ZIP, java.lang.String c_SHIP_COUNTRY) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard/VAULT_BANKCARD_FORCE");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_FORCE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, c_NAME, c_ADDRESS, c_CITY, c_STATE, c_ZIP, c_COUNTRY, c_EMAIL, GUID, t_CUSTOMER_NUMBER, t_AMT, t_SHIPPING, t_TAX, t_ORDERNUM, c_TELEPHONE, c_FAX, c_SHIP_NAME, c_SHIP_ADDRESS, c_SHIP_CITY, c_SHIP_STATE, c_SHIP_ZIP, c_SHIP_COUNTRY});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult VAULT_BANKCARD_CREDIT(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String c_NAME, java.lang.String c_ADDRESS, java.lang.String c_CITY, java.lang.String c_STATE, java.lang.String c_ZIP, java.lang.String c_COUNTRY, java.lang.String c_EMAIL, java.lang.String GUID, java.lang.String t_CUSTOMER_NUMBER, java.lang.String t_AMT, java.lang.String t_SHIPPING, java.lang.String t_TAX, java.lang.String t_ORDERNUM, java.lang.String c_TELEPHONE, java.lang.String c_FAX, java.lang.String c_SHIP_NAME, java.lang.String c_SHIP_ADDRESS, java.lang.String c_SHIP_CITY, java.lang.String c_SHIP_STATE, java.lang.String c_SHIP_ZIP, java.lang.String c_SHIP_COUNTRY) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard/VAULT_BANKCARD_CREDIT");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_CREDIT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, c_NAME, c_ADDRESS, c_CITY, c_STATE, c_ZIP, c_COUNTRY, c_EMAIL, GUID, t_CUSTOMER_NUMBER, t_AMT, t_SHIPPING, t_TAX, t_ORDERNUM, c_TELEPHONE, c_FAX, c_SHIP_NAME, c_SHIP_ADDRESS, c_SHIP_CITY, c_SHIP_STATE, c_SHIP_ZIP, c_SHIP_COUNTRY});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
