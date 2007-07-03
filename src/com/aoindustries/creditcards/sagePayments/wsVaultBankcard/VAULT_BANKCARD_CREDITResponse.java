/**
 * VAULT_BANKCARD_CREDITResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVaultBankcard;

public class VAULT_BANKCARD_CREDITResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult VAULT_BANKCARD_CREDITResult;

    public VAULT_BANKCARD_CREDITResponse() {
    }

    public VAULT_BANKCARD_CREDITResponse(
           com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult VAULT_BANKCARD_CREDITResult) {
           this.VAULT_BANKCARD_CREDITResult = VAULT_BANKCARD_CREDITResult;
    }


    /**
     * Gets the VAULT_BANKCARD_CREDITResult value for this VAULT_BANKCARD_CREDITResponse.
     * 
     * @return VAULT_BANKCARD_CREDITResult
     */
    public com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult getVAULT_BANKCARD_CREDITResult() {
        return VAULT_BANKCARD_CREDITResult;
    }


    /**
     * Sets the VAULT_BANKCARD_CREDITResult value for this VAULT_BANKCARD_CREDITResponse.
     * 
     * @param VAULT_BANKCARD_CREDITResult
     */
    public void setVAULT_BANKCARD_CREDITResult(com.aoindustries.creditcards.sagePayments.wsVaultBankcard.VAULT_BANKCARD_CREDITResponseVAULT_BANKCARD_CREDITResult VAULT_BANKCARD_CREDITResult) {
        this.VAULT_BANKCARD_CREDITResult = VAULT_BANKCARD_CREDITResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VAULT_BANKCARD_CREDITResponse)) return false;
        VAULT_BANKCARD_CREDITResponse other = (VAULT_BANKCARD_CREDITResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VAULT_BANKCARD_CREDITResult==null && other.getVAULT_BANKCARD_CREDITResult()==null) || 
             (this.VAULT_BANKCARD_CREDITResult!=null &&
              this.VAULT_BANKCARD_CREDITResult.equals(other.getVAULT_BANKCARD_CREDITResult())));
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
        if (getVAULT_BANKCARD_CREDITResult() != null) {
            _hashCode += getVAULT_BANKCARD_CREDITResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VAULT_BANKCARD_CREDITResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">VAULT_BANKCARD_CREDITResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VAULT_BANKCARD_CREDITResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", "VAULT_BANKCARD_CREDITResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVaultBankcard", ">>VAULT_BANKCARD_CREDITResponse>VAULT_BANKCARD_CREDITResult"));
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
