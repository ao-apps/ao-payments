/**
 * TRANSACTION_PROCESSINGLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class TRANSACTION_PROCESSINGLocator extends org.apache.axis.client.Service implements com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSING {

    public TRANSACTION_PROCESSINGLocator() {
    }


    public TRANSACTION_PROCESSINGLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TRANSACTION_PROCESSINGLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TRANSACTION_PROCESSINGSoap
    private java.lang.String TRANSACTION_PROCESSINGSoap_address = "https://www.sagepayments.net/web_services/vterm_extensions/transaction_processing.asmx";

    public java.lang.String getTRANSACTION_PROCESSINGSoapAddress() {
        return TRANSACTION_PROCESSINGSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TRANSACTION_PROCESSINGSoapWSDDServiceName = "TRANSACTION_PROCESSINGSoap";

    public java.lang.String getTRANSACTION_PROCESSINGSoapWSDDServiceName() {
        return TRANSACTION_PROCESSINGSoapWSDDServiceName;
    }

    public void setTRANSACTION_PROCESSINGSoapWSDDServiceName(java.lang.String name) {
        TRANSACTION_PROCESSINGSoapWSDDServiceName = name;
    }

    public com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSINGSoap_PortType getTRANSACTION_PROCESSINGSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TRANSACTION_PROCESSINGSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTRANSACTION_PROCESSINGSoap(endpoint);
    }

    public com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSINGSoap_PortType getTRANSACTION_PROCESSINGSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSINGSoap_BindingStub _stub = new com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSINGSoap_BindingStub(portAddress, this);
            _stub.setPortName(getTRANSACTION_PROCESSINGSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTRANSACTION_PROCESSINGSoapEndpointAddress(java.lang.String address) {
        TRANSACTION_PROCESSINGSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSINGSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSINGSoap_BindingStub _stub = new com.aoindustries.creditcards.sagePayments.transaction_processing.TRANSACTION_PROCESSINGSoap_BindingStub(new java.net.URL(TRANSACTION_PROCESSINGSoap_address), this);
                _stub.setPortName(getTRANSACTION_PROCESSINGSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TRANSACTION_PROCESSINGSoap".equals(inputPortName)) {
            return getTRANSACTION_PROCESSINGSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "TRANSACTION_PROCESSING");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "TRANSACTION_PROCESSINGSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TRANSACTION_PROCESSINGSoap".equals(portName)) {
            setTRANSACTION_PROCESSINGSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
