/**
 * WsVaultLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVault;

public class WsVaultLocator extends org.apache.axis.client.Service implements com.aoindustries.creditcards.sagePayments.wsVault.WsVault {

    public WsVaultLocator() {
    }


    public WsVaultLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsVaultLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for wsVaultSoap
    private java.lang.String wsVaultSoap_address = "https://www.sagepayments.net/web_services/wsVault/wsVault.asmx";

    public java.lang.String getwsVaultSoapAddress() {
        return wsVaultSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String wsVaultSoapWSDDServiceName = "wsVaultSoap";

    public java.lang.String getwsVaultSoapWSDDServiceName() {
        return wsVaultSoapWSDDServiceName;
    }

    public void setwsVaultSoapWSDDServiceName(java.lang.String name) {
        wsVaultSoapWSDDServiceName = name;
    }

    public com.aoindustries.creditcards.sagePayments.wsVault.WsVaultSoap_PortType getwsVaultSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(wsVaultSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getwsVaultSoap(endpoint);
    }

    public com.aoindustries.creditcards.sagePayments.wsVault.WsVaultSoap_PortType getwsVaultSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.aoindustries.creditcards.sagePayments.wsVault.WsVaultSoap_BindingStub _stub = new com.aoindustries.creditcards.sagePayments.wsVault.WsVaultSoap_BindingStub(portAddress, this);
            _stub.setPortName(getwsVaultSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setwsVaultSoapEndpointAddress(java.lang.String address) {
        wsVaultSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.aoindustries.creditcards.sagePayments.wsVault.WsVaultSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.aoindustries.creditcards.sagePayments.wsVault.WsVaultSoap_BindingStub _stub = new com.aoindustries.creditcards.sagePayments.wsVault.WsVaultSoap_BindingStub(new java.net.URL(wsVaultSoap_address), this);
                _stub.setPortName(getwsVaultSoapWSDDServiceName());
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
        if ("wsVaultSoap".equals(inputPortName)) {
            return getwsVaultSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "wsVault");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "wsVaultSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("wsVaultSoap".equals(portName)) {
            setwsVaultSoapEndpointAddress(address);
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
