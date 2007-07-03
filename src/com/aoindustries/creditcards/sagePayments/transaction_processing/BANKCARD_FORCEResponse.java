/**
 * BANKCARD_FORCEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class BANKCARD_FORCEResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_FORCEResponseBANKCARD_FORCEResult BANKCARD_FORCEResult;

    public BANKCARD_FORCEResponse() {
    }

    public BANKCARD_FORCEResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_FORCEResponseBANKCARD_FORCEResult BANKCARD_FORCEResult) {
           this.BANKCARD_FORCEResult = BANKCARD_FORCEResult;
    }


    /**
     * Gets the BANKCARD_FORCEResult value for this BANKCARD_FORCEResponse.
     * 
     * @return BANKCARD_FORCEResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_FORCEResponseBANKCARD_FORCEResult getBANKCARD_FORCEResult() {
        return BANKCARD_FORCEResult;
    }


    /**
     * Sets the BANKCARD_FORCEResult value for this BANKCARD_FORCEResponse.
     * 
     * @param BANKCARD_FORCEResult
     */
    public void setBANKCARD_FORCEResult(com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_FORCEResponseBANKCARD_FORCEResult BANKCARD_FORCEResult) {
        this.BANKCARD_FORCEResult = BANKCARD_FORCEResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BANKCARD_FORCEResponse)) return false;
        BANKCARD_FORCEResponse other = (BANKCARD_FORCEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BANKCARD_FORCEResult==null && other.getBANKCARD_FORCEResult()==null) || 
             (this.BANKCARD_FORCEResult!=null &&
              this.BANKCARD_FORCEResult.equals(other.getBANKCARD_FORCEResult())));
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
        if (getBANKCARD_FORCEResult() != null) {
            _hashCode += getBANKCARD_FORCEResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BANKCARD_FORCEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">BANKCARD_FORCEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BANKCARD_FORCEResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "BANKCARD_FORCEResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>BANKCARD_FORCEResponse>BANKCARD_FORCEResult"));
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
