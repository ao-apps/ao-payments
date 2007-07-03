/**
 * WsVaultSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVault;

public interface WsVaultSoap_PortType extends java.rmi.Remote {
    public java.lang.String INSERT_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String DATA) throws java.rmi.RemoteException;
    public boolean UPDATE_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID, java.lang.String DATA) throws java.rmi.RemoteException;
    public java.lang.String SELECT_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID) throws java.rmi.RemoteException;
    public boolean DELETE_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID) throws java.rmi.RemoteException;
    public boolean VERIFY_SERVICE(java.lang.String m_ID, java.lang.String m_KEY) throws java.rmi.RemoteException;
    public com.aoindustries.creditcards.sagePayments.wsVault.INSERT_CREDIT_CARD_DATAResponseINSERT_CREDIT_CARD_DATAResult INSERT_CREDIT_CARD_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String CARDNUMBER, java.lang.String EXPIRATION_DATE) throws java.rmi.RemoteException;
    public com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_DATAResponseUPDATE_CREDIT_CARD_DATAResult UPDATE_CREDIT_CARD_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID, java.lang.String CARDNUMBER, java.lang.String EXPIRATION_DATE) throws java.rmi.RemoteException;
    public com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult UPDATE_CREDIT_CARD_EXPIRATION_DATE(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String GUID, java.lang.String EXPIRATION_DATE) throws java.rmi.RemoteException;
    public com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[] INSERT_BULK_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String[] DATA) throws java.rmi.RemoteException;
    public com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[] SELECT_BULK_DATA(java.lang.String m_ID, java.lang.String m_KEY, java.lang.String[] GUID) throws java.rmi.RemoteException;
}
