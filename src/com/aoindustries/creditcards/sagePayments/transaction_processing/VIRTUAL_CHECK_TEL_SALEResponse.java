/**
 * VIRTUAL_CHECK_TEL_SALEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class VIRTUAL_CHECK_TEL_SALEResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_TEL_SALEResponseVIRTUAL_CHECK_TEL_SALEResult VIRTUAL_CHECK_TEL_SALEResult;

    public VIRTUAL_CHECK_TEL_SALEResponse() {
    }

    public VIRTUAL_CHECK_TEL_SALEResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_TEL_SALEResponseVIRTUAL_CHECK_TEL_SALEResult VIRTUAL_CHECK_TEL_SALEResult) {
           this.VIRTUAL_CHECK_TEL_SALEResult = VIRTUAL_CHECK_TEL_SALEResult;
    }


    /**
     * Gets the VIRTUAL_CHECK_TEL_SALEResult value for this VIRTUAL_CHECK_TEL_SALEResponse.
     * 
     * @return VIRTUAL_CHECK_TEL_SALEResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_TEL_SALEResponseVIRTUAL_CHECK_TEL_SALEResult getVIRTUAL_CHECK_TEL_SALEResult() {
        return VIRTUAL_CHECK_TEL_SALEResult;
    }


    /**
     * Sets the VIRTUAL_CHECK_TEL_SALEResult value for this VIRTUAL_CHECK_TEL_SALEResponse.
     * 
     * @param VIRTUAL_CHECK_TEL_SALEResult
     */
    public void setVIRTUAL_CHECK_TEL_SALEResult(com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_TEL_SALEResponseVIRTUAL_CHECK_TEL_SALEResult VIRTUAL_CHECK_TEL_SALEResult) {
        this.VIRTUAL_CHECK_TEL_SALEResult = VIRTUAL_CHECK_TEL_SALEResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VIRTUAL_CHECK_TEL_SALEResponse)) return false;
        VIRTUAL_CHECK_TEL_SALEResponse other = (VIRTUAL_CHECK_TEL_SALEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VIRTUAL_CHECK_TEL_SALEResult==null && other.getVIRTUAL_CHECK_TEL_SALEResult()==null) || 
             (this.VIRTUAL_CHECK_TEL_SALEResult!=null &&
              this.VIRTUAL_CHECK_TEL_SALEResult.equals(other.getVIRTUAL_CHECK_TEL_SALEResult())));
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
        if (getVIRTUAL_CHECK_TEL_SALEResult() != null) {
            _hashCode += getVIRTUAL_CHECK_TEL_SALEResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VIRTUAL_CHECK_TEL_SALEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">VIRTUAL_CHECK_TEL_SALEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VIRTUAL_CHECK_TEL_SALEResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "VIRTUAL_CHECK_TEL_SALEResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>VIRTUAL_CHECK_TEL_SALEResponse>VIRTUAL_CHECK_TEL_SALEResult"));
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
