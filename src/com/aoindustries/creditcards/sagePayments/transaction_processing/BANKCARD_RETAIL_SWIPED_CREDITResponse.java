/**
 * BANKCARD_RETAIL_SWIPED_CREDITResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class BANKCARD_RETAIL_SWIPED_CREDITResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RETAIL_SWIPED_CREDITResponseBANKCARD_RETAIL_SWIPED_CREDITResult BANKCARD_RETAIL_SWIPED_CREDITResult;

    public BANKCARD_RETAIL_SWIPED_CREDITResponse() {
    }

    public BANKCARD_RETAIL_SWIPED_CREDITResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RETAIL_SWIPED_CREDITResponseBANKCARD_RETAIL_SWIPED_CREDITResult BANKCARD_RETAIL_SWIPED_CREDITResult) {
           this.BANKCARD_RETAIL_SWIPED_CREDITResult = BANKCARD_RETAIL_SWIPED_CREDITResult;
    }


    /**
     * Gets the BANKCARD_RETAIL_SWIPED_CREDITResult value for this BANKCARD_RETAIL_SWIPED_CREDITResponse.
     * 
     * @return BANKCARD_RETAIL_SWIPED_CREDITResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RETAIL_SWIPED_CREDITResponseBANKCARD_RETAIL_SWIPED_CREDITResult getBANKCARD_RETAIL_SWIPED_CREDITResult() {
        return BANKCARD_RETAIL_SWIPED_CREDITResult;
    }


    /**
     * Sets the BANKCARD_RETAIL_SWIPED_CREDITResult value for this BANKCARD_RETAIL_SWIPED_CREDITResponse.
     * 
     * @param BANKCARD_RETAIL_SWIPED_CREDITResult
     */
    public void setBANKCARD_RETAIL_SWIPED_CREDITResult(com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RETAIL_SWIPED_CREDITResponseBANKCARD_RETAIL_SWIPED_CREDITResult BANKCARD_RETAIL_SWIPED_CREDITResult) {
        this.BANKCARD_RETAIL_SWIPED_CREDITResult = BANKCARD_RETAIL_SWIPED_CREDITResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BANKCARD_RETAIL_SWIPED_CREDITResponse)) return false;
        BANKCARD_RETAIL_SWIPED_CREDITResponse other = (BANKCARD_RETAIL_SWIPED_CREDITResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BANKCARD_RETAIL_SWIPED_CREDITResult==null && other.getBANKCARD_RETAIL_SWIPED_CREDITResult()==null) || 
             (this.BANKCARD_RETAIL_SWIPED_CREDITResult!=null &&
              this.BANKCARD_RETAIL_SWIPED_CREDITResult.equals(other.getBANKCARD_RETAIL_SWIPED_CREDITResult())));
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
        if (getBANKCARD_RETAIL_SWIPED_CREDITResult() != null) {
            _hashCode += getBANKCARD_RETAIL_SWIPED_CREDITResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BANKCARD_RETAIL_SWIPED_CREDITResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">BANKCARD_RETAIL_SWIPED_CREDITResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BANKCARD_RETAIL_SWIPED_CREDITResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "BANKCARD_RETAIL_SWIPED_CREDITResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>BANKCARD_RETAIL_SWIPED_CREDITResponse>BANKCARD_RETAIL_SWIPED_CREDITResult"));
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
