/**
 * VAULT_BANKCARD_FORCEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVaultBankcard;

public class VAULT_BANKCARD_FORCEResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult VAULT_BANKCARD_FORCEResult;

    public VAULT_BANKCARD_FORCEResponse() {
    }

    public VAULT_BANKCARD_FORCEResponse(
           com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult VAULT_BANKCARD_FORCEResult) {
           this.VAULT_BANKCARD_FORCEResult = VAULT_BANKCARD_FORCEResult;
    }


    /**
     * Gets the VAULT_BANKCARD_FORCEResult value for this VAULT_BANKCARD_FORCEResponse.
     * 
     * @return VAULT_BANKCARD_FORCEResult
     */
    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult getVAULT_BANKCARD_FORCEResult() {
        return VAULT_BANKCARD_FORCEResult;
    }


    /**
     * Sets the VAULT_BANKCARD_FORCEResult value for this VAULT_BANKCARD_FORCEResponse.
     * 
     * @param VAULT_BANKCARD_FORCEResult
     */
    public void setVAULT_BANKCARD_FORCEResult(com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_FORCEResponseVAULT_BANKCARD_FORCEResult VAULT_BANKCARD_FORCEResult) {
        this.VAULT_BANKCARD_FORCEResult = VAULT_BANKCARD_FORCEResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VAULT_BANKCARD_FORCEResponse)) return false;
        VAULT_BANKCARD_FORCEResponse other = (VAULT_BANKCARD_FORCEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VAULT_BANKCARD_FORCEResult==null && other.getVAULT_BANKCARD_FORCEResult()==null) || 
             (this.VAULT_BANKCARD_FORCEResult!=null &&
              this.VAULT_BANKCARD_FORCEResult.equals(other.getVAULT_BANKCARD_FORCEResult())));
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
        if (getVAULT_BANKCARD_FORCEResult() != null) {
            _hashCode += getVAULT_BANKCARD_FORCEResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VAULT_BANKCARD_FORCEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_FORCEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VAULT_BANKCARD_FORCEResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_FORCEResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_FORCEResponse>VAULT_BANKCARD_FORCEResult"));
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
