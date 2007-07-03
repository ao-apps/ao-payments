/**
 * UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVault;

public class UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult UPDATE_CREDIT_CARD_EXPIRATION_DATEResult;

    public UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse() {
    }

    public UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse(
           com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult UPDATE_CREDIT_CARD_EXPIRATION_DATEResult) {
           this.UPDATE_CREDIT_CARD_EXPIRATION_DATEResult = UPDATE_CREDIT_CARD_EXPIRATION_DATEResult;
    }


    /**
     * Gets the UPDATE_CREDIT_CARD_EXPIRATION_DATEResult value for this UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse.
     * 
     * @return UPDATE_CREDIT_CARD_EXPIRATION_DATEResult
     */
    public com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult getUPDATE_CREDIT_CARD_EXPIRATION_DATEResult() {
        return UPDATE_CREDIT_CARD_EXPIRATION_DATEResult;
    }


    /**
     * Sets the UPDATE_CREDIT_CARD_EXPIRATION_DATEResult value for this UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse.
     * 
     * @param UPDATE_CREDIT_CARD_EXPIRATION_DATEResult
     */
    public void setUPDATE_CREDIT_CARD_EXPIRATION_DATEResult(com.aoindustries.creditcards.sagePayments.wsVault.UPDATE_CREDIT_CARD_EXPIRATION_DATEResponseUPDATE_CREDIT_CARD_EXPIRATION_DATEResult UPDATE_CREDIT_CARD_EXPIRATION_DATEResult) {
        this.UPDATE_CREDIT_CARD_EXPIRATION_DATEResult = UPDATE_CREDIT_CARD_EXPIRATION_DATEResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse)) return false;
        UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse other = (UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.UPDATE_CREDIT_CARD_EXPIRATION_DATEResult==null && other.getUPDATE_CREDIT_CARD_EXPIRATION_DATEResult()==null) || 
             (this.UPDATE_CREDIT_CARD_EXPIRATION_DATEResult!=null &&
              this.UPDATE_CREDIT_CARD_EXPIRATION_DATEResult.equals(other.getUPDATE_CREDIT_CARD_EXPIRATION_DATEResult())));
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
        if (getUPDATE_CREDIT_CARD_EXPIRATION_DATEResult() != null) {
            _hashCode += getUPDATE_CREDIT_CARD_EXPIRATION_DATEResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UPDATE_CREDIT_CARD_EXPIRATION_DATEResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "UPDATE_CREDIT_CARD_EXPIRATION_DATEResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">>UPDATE_CREDIT_CARD_EXPIRATION_DATEResponse>UPDATE_CREDIT_CARD_EXPIRATION_DATEResult"));
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
