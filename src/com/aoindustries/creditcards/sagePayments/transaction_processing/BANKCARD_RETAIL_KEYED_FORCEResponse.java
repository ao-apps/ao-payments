/**
 * BANKCARD_RETAIL_KEYED_FORCEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class BANKCARD_RETAIL_KEYED_FORCEResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RETAIL_KEYED_FORCEResponseBANKCARD_RETAIL_KEYED_FORCEResult BANKCARD_RETAIL_KEYED_FORCEResult;

    public BANKCARD_RETAIL_KEYED_FORCEResponse() {
    }

    public BANKCARD_RETAIL_KEYED_FORCEResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RETAIL_KEYED_FORCEResponseBANKCARD_RETAIL_KEYED_FORCEResult BANKCARD_RETAIL_KEYED_FORCEResult) {
           this.BANKCARD_RETAIL_KEYED_FORCEResult = BANKCARD_RETAIL_KEYED_FORCEResult;
    }


    /**
     * Gets the BANKCARD_RETAIL_KEYED_FORCEResult value for this BANKCARD_RETAIL_KEYED_FORCEResponse.
     * 
     * @return BANKCARD_RETAIL_KEYED_FORCEResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RETAIL_KEYED_FORCEResponseBANKCARD_RETAIL_KEYED_FORCEResult getBANKCARD_RETAIL_KEYED_FORCEResult() {
        return BANKCARD_RETAIL_KEYED_FORCEResult;
    }


    /**
     * Sets the BANKCARD_RETAIL_KEYED_FORCEResult value for this BANKCARD_RETAIL_KEYED_FORCEResponse.
     * 
     * @param BANKCARD_RETAIL_KEYED_FORCEResult
     */
    public void setBANKCARD_RETAIL_KEYED_FORCEResult(com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RETAIL_KEYED_FORCEResponseBANKCARD_RETAIL_KEYED_FORCEResult BANKCARD_RETAIL_KEYED_FORCEResult) {
        this.BANKCARD_RETAIL_KEYED_FORCEResult = BANKCARD_RETAIL_KEYED_FORCEResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BANKCARD_RETAIL_KEYED_FORCEResponse)) return false;
        BANKCARD_RETAIL_KEYED_FORCEResponse other = (BANKCARD_RETAIL_KEYED_FORCEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BANKCARD_RETAIL_KEYED_FORCEResult==null && other.getBANKCARD_RETAIL_KEYED_FORCEResult()==null) || 
             (this.BANKCARD_RETAIL_KEYED_FORCEResult!=null &&
              this.BANKCARD_RETAIL_KEYED_FORCEResult.equals(other.getBANKCARD_RETAIL_KEYED_FORCEResult())));
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
        if (getBANKCARD_RETAIL_KEYED_FORCEResult() != null) {
            _hashCode += getBANKCARD_RETAIL_KEYED_FORCEResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BANKCARD_RETAIL_KEYED_FORCEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">BANKCARD_RETAIL_KEYED_FORCEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BANKCARD_RETAIL_KEYED_FORCEResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "BANKCARD_RETAIL_KEYED_FORCEResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>BANKCARD_RETAIL_KEYED_FORCEResponse>BANKCARD_RETAIL_KEYED_FORCEResult"));
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
