/**
 * VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponseVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult;

    public VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse() {
    }

    public VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponseVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult) {
           this.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult = VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult;
    }


    /**
     * Gets the VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult value for this VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse.
     * 
     * @return VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponseVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult getVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult() {
        return VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult;
    }


    /**
     * Sets the VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult value for this VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse.
     * 
     * @param VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult
     */
    public void setVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult(com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponseVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult) {
        this.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult = VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse)) return false;
        VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse other = (VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult==null && other.getVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult()==null) || 
             (this.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult!=null &&
              this.VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult.equals(other.getVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult())));
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
        if (getVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult() != null) {
            _hashCode += getVIRTUAL_CHECK_CREDIT_BY_REFERENCEResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>VIRTUAL_CHECK_CREDIT_BY_REFERENCEResponse>VIRTUAL_CHECK_CREDIT_BY_REFERENCEResult"));
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
