/**
 * VIRTUAL_CHECK_CCD_CREDITResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class VIRTUAL_CHECK_CCD_CREDITResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_CCD_CREDITResponseVIRTUAL_CHECK_CCD_CREDITResult VIRTUAL_CHECK_CCD_CREDITResult;

    public VIRTUAL_CHECK_CCD_CREDITResponse() {
    }

    public VIRTUAL_CHECK_CCD_CREDITResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_CCD_CREDITResponseVIRTUAL_CHECK_CCD_CREDITResult VIRTUAL_CHECK_CCD_CREDITResult) {
           this.VIRTUAL_CHECK_CCD_CREDITResult = VIRTUAL_CHECK_CCD_CREDITResult;
    }


    /**
     * Gets the VIRTUAL_CHECK_CCD_CREDITResult value for this VIRTUAL_CHECK_CCD_CREDITResponse.
     * 
     * @return VIRTUAL_CHECK_CCD_CREDITResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_CCD_CREDITResponseVIRTUAL_CHECK_CCD_CREDITResult getVIRTUAL_CHECK_CCD_CREDITResult() {
        return VIRTUAL_CHECK_CCD_CREDITResult;
    }


    /**
     * Sets the VIRTUAL_CHECK_CCD_CREDITResult value for this VIRTUAL_CHECK_CCD_CREDITResponse.
     * 
     * @param VIRTUAL_CHECK_CCD_CREDITResult
     */
    public void setVIRTUAL_CHECK_CCD_CREDITResult(com.aoindustries.creditcards.sagePayments.transaction_processing.VIRTUAL_CHECK_CCD_CREDITResponseVIRTUAL_CHECK_CCD_CREDITResult VIRTUAL_CHECK_CCD_CREDITResult) {
        this.VIRTUAL_CHECK_CCD_CREDITResult = VIRTUAL_CHECK_CCD_CREDITResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VIRTUAL_CHECK_CCD_CREDITResponse)) return false;
        VIRTUAL_CHECK_CCD_CREDITResponse other = (VIRTUAL_CHECK_CCD_CREDITResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VIRTUAL_CHECK_CCD_CREDITResult==null && other.getVIRTUAL_CHECK_CCD_CREDITResult()==null) || 
             (this.VIRTUAL_CHECK_CCD_CREDITResult!=null &&
              this.VIRTUAL_CHECK_CCD_CREDITResult.equals(other.getVIRTUAL_CHECK_CCD_CREDITResult())));
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
        if (getVIRTUAL_CHECK_CCD_CREDITResult() != null) {
            _hashCode += getVIRTUAL_CHECK_CCD_CREDITResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VIRTUAL_CHECK_CCD_CREDITResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">VIRTUAL_CHECK_CCD_CREDITResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VIRTUAL_CHECK_CCD_CREDITResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "VIRTUAL_CHECK_CCD_CREDITResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>VIRTUAL_CHECK_CCD_CREDITResponse>VIRTUAL_CHECK_CCD_CREDITResult"));
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
