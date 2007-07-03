/**
 * VAULT_BANKCARD_AUTHONLYResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVaultBankcard;

public class VAULT_BANKCARD_AUTHONLYResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult VAULT_BANKCARD_AUTHONLYResult;

    public VAULT_BANKCARD_AUTHONLYResponse() {
    }

    public VAULT_BANKCARD_AUTHONLYResponse(
           com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult VAULT_BANKCARD_AUTHONLYResult) {
           this.VAULT_BANKCARD_AUTHONLYResult = VAULT_BANKCARD_AUTHONLYResult;
    }


    /**
     * Gets the VAULT_BANKCARD_AUTHONLYResult value for this VAULT_BANKCARD_AUTHONLYResponse.
     * 
     * @return VAULT_BANKCARD_AUTHONLYResult
     */
    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult getVAULT_BANKCARD_AUTHONLYResult() {
        return VAULT_BANKCARD_AUTHONLYResult;
    }


    /**
     * Sets the VAULT_BANKCARD_AUTHONLYResult value for this VAULT_BANKCARD_AUTHONLYResponse.
     * 
     * @param VAULT_BANKCARD_AUTHONLYResult
     */
    public void setVAULT_BANKCARD_AUTHONLYResult(com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_AUTHONLYResponseVAULT_BANKCARD_AUTHONLYResult VAULT_BANKCARD_AUTHONLYResult) {
        this.VAULT_BANKCARD_AUTHONLYResult = VAULT_BANKCARD_AUTHONLYResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VAULT_BANKCARD_AUTHONLYResponse)) return false;
        VAULT_BANKCARD_AUTHONLYResponse other = (VAULT_BANKCARD_AUTHONLYResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VAULT_BANKCARD_AUTHONLYResult==null && other.getVAULT_BANKCARD_AUTHONLYResult()==null) || 
             (this.VAULT_BANKCARD_AUTHONLYResult!=null &&
              this.VAULT_BANKCARD_AUTHONLYResult.equals(other.getVAULT_BANKCARD_AUTHONLYResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getVAULT_BANKCARD_AUTHONLYResult() != null) {
            _hashCode += getVAULT_BANKCARD_AUTHONLYResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VAULT_BANKCARD_AUTHONLYResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_AUTHONLYResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VAULT_BANKCARD_AUTHONLYResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_AUTHONLYResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_AUTHONLYResponse>VAULT_BANKCARD_AUTHONLYResult"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
