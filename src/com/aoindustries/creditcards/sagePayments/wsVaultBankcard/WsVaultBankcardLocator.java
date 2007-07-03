/**
 * WsVaultBankcardLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVaultBankcard;

public class WsVaultBankcardLocator extends org.apache.axis.client.Service implements com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcard {

    public WsVaultBankcardLocator() {
    }


    public WsVaultBankcardLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsVaultBankcardLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for wsVaultBankcardSoap
    private java.lang.String wsVaultBankcardSoap_address = "https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard.asmx";

    public java.lang.String getwsVaultBankcardSoapAddress() {
        return wsVaultBankcardSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String wsVaultBankcardSoapWSDDServiceName = "wsVaultBankcardSoap";

    public java.lang.String getwsVaultBankcardSoapWSDDServiceName() {
        return wsVaultBankcardSoapWSDDServiceName;
    }

    public void setwsVaultBankcardSoapWSDDServiceName(java.lang.String name) {
        wsVaultBankcardSoapWSDDServiceName = name;
    }

    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardSoap_PortType getwsVaultBankcardSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(wsVaultBankcardSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getwsVaultBankcardSoap(endpoint);
    }

    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardSoap_PortType getwsVaultBankcardSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardSoap_BindingStub _stub = new com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardSoap_BindingStub(portAddress, this);
            _stub.setPortName(getwsVaultBankcardSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setwsVaultBankcardSoapEndpointAddress(java.lang.String address) {
        wsVaultBankcardSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardSoap_BindingStub _stub = new com.aoindustries.creditcards.sagePayments.wsVaultBankcard.WsVaultBankcardSoap_BindingStub(new java.net.URL(wsVaultBankcardSoap_address), this);
                _stub.setPortName(getwsVaultBankcardSoapWSDDServiceName());
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
        if ("wsVaultBankcardSoap".equals(inputPortName)) {
            return getwsVaultBankcardSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "wsVaultBankcard");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "wsVaultBankcardSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("wsVaultBankcardSoap".equals(portName)) {
            setwsVaultBankcardSoapEndpointAddress(address);
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
