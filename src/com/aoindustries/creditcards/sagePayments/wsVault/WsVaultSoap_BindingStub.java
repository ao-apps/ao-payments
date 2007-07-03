/**
 * WsVaultSoap_BindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVault;

public class WsVaultSoap_BindingStub extends org.apache.axis.client.Stub implements com.aoindustries.creditcards.sagePayments.wsVault.WsVaultSoap_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[10];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERT_DATA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "DATA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "INSERT_DATAResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATE_DATA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "DATA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "UPDATE_DATAResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECT_DATA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "SELECT_DATAResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETE_DATA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "DELETE_DATAResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("VERIFY_SERVICE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "VERIFY_SERVICEResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERT_CREDIT_CARD_DATA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "CARDNUMBER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "EXPIRATION_DATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">>INSERT_CREDIT_CARD_DATAResponse>INSERT_CREDIT_CARD_DATAResult"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVault.INSERT_CREDIT_CARD_DATAResponseINSERT_CREDIT_CARD_DATAResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "INSERT_CREDIT_CARD_DATAResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATE_CREDIT_CARD_DATA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "CARDNUMBER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "EXPIRATION_DATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">>UPDATE_CREDIT_CARD_DATAResponse>UPDATE_CREDIT_CARD_DATAResult"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATAResponseUPDATE_CREDIT_CARD_DATAResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "UPDATE_CREDIT_CARD_DATAResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATE_CREDIT_CARD_EXPIRATION_DATE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "EXPIRATION_DATE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">>UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse>UPDATE_CREDIT_CARD_EXPIRATION_DATEResult"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "UPDATE_CREDIT_CARD_EXPIRATION_DATEResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERT_BULK_DATA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "DATA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "ArrayOfBULK_RESULT"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "INSERT_BULK_DATAResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "BULK_RESULT"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECT_BULK_DATA");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "GUID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "ArrayOfBULK_RESULT"));
        oper.setReturnClass(com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "SELECT_BULK_DATAResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "BULK_RESULT"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    public WsVaultSoap_BindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public WsVaultSoap_BindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public WsVaultSoap_BindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault/AbstractTypes", "StringArray");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">>INSERT_CREDIT_CARD_DATAResponse>INSERT_CREDIT_CARD_DATAResult");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.INSERT_CREDIT_CARD_DATAResponseINSERT_CREDIT_CARD_DATAResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">>UPDATE_CREDIT_CARD_DATAResponse>UPDATE_CREDIT_CARD_DATAResult");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATAResponseUPDATE_CREDIT_CARD_DATAResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">>UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse>UPDATE_CREDIT_CARD_EXPIRATION_DATEResult");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">DataSet");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.DataSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">INSERT_BULK_DATA");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.INSERT_BULK_DATA.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">INSERT_BULK_DATAResponse");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.INSERT_BULK_DATAResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">SELECT_BULK_DATA");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.SELECT_BULK_DATA.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">SELECT_BULK_DATAResponse");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.SELECT_BULK_DATAResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">UPDATE_CREDIT_CARD_DATA");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATA.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">UPDATE_CREDIT_CARD_DATAResponse");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATAResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">UPDATE_CREDIT_CARD_EXPIRATION_DATE");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATE.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "ArrayOfBULK_RESULT");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "BULK_RESULT");
            qName2 = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "BULK_RESULT");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "ArrayOfString");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "string");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "BULK_RESULT");
            cachedSerQNames.add(qName);
            cls = com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT.class;
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

    public java.lang.String INSERT_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String DATA) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/INSERT_DATA");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "INSERT_DATA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, DATA});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean UPDATE_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID, java.lang.String DATA) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/UPDATE_DATA");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "UPDATE_DATA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, GUID, DATA});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String SELECT_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/SELECT_DATA");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "SELECT_DATA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, GUID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean DELETE_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/DELETE_DATA");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "DELETE_DATA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, GUID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean VERIFY_SERVICE(java.lang.String m_ID, java.lang.String m_KEY) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/VERIFY_SERVICE");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "VERIFY_SERVICE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.aoindustries.creditcards.sagePayments.wsVault.INSERT_CREDIT_CARD_DATAResponseINSERT_CREDIT_CARD_DATAResult INSERT_CREDIT_CARD_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String CARDNUMBER, java.lang.String EXPIRATION_DATE) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/INSERT_CREDIT_CARD_DATA");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "INSERT_CREDIT_CARD_DATA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, CARDNUMBER, EXPIRATION_DATE});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVault.INSERT_CREDIT_CARD_DATAResponseINSERT_CREDIT_CARD_DATAResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVault.INSERT_CREDIT_CARD_DATAResponseINSERT_CREDIT_CARD_DATAResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVault.INSERT_CREDIT_CARD_DATAResponseINSERT_CREDIT_CARD_DATAResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATAResponseUPDATE_CREDIT_CARD_DATAResult UPDATE_CREDIT_CARD_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID, java.lang.String CARDNUMBER, java.lang.String EXPIRATION_DATE) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/UPDATE_CREDIT_CARD_DATA");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "UPDATE_CREDIT_CARD_DATA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, GUID, CARDNUMBER, EXPIRATION_DATE});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATAResponseUPDATE_CREDIT_CARD_DATAResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATAResponseUPDATE_CREDIT_CARD_DATAResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATAResponseUPDATE_CREDIT_CARD_DATAResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult UPDATE_CREDIT_CARD_EXPIRATION_DATE(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID, java.lang.String EXPIRATION_DATE) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/UPDATE_CREDIT_CARD_EXPIRATION_DATE");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "UPDATE_CREDIT_CARD_EXPIRATION_DATE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, GUID, EXPIRATION_DATE});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[] INSERT_BULK_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String[] DATA) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/INSERT_BULK_DATA");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "INSERT_BULK_DATA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, DATA});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[] SELECT_BULK_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String[] GUID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("https://www.sagepayments.net/web_services/wsVault/wsVault/SELECT_BULK_DATA");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "SELECT_BULK_DATA"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {m_ID, m_KEY, GUID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
