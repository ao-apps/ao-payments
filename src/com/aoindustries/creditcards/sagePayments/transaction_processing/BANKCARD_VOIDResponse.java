/**
 * BANKCARD_VOIDResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class BANKCARD_VOIDResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_VOIDResponseBANKCARD_VOIDResult BANKCARD_VOIDResult;

    public BANKCARD_VOIDResponse() {
    }

    public BANKCARD_VOIDResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_VOIDResponseBANKCARD_VOIDResult BANKCARD_VOIDResult) {
           this.BANKCARD_VOIDResult = BANKCARD_VOIDResult;
    }


    /**
     * Gets the BANKCARD_VOIDResult value for this BANKCARD_VOIDResponse.
     * 
     * @return BANKCARD_VOIDResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_VOIDResponseBANKCARD_VOIDResult getBANKCARD_VOIDResult() {
        return BANKCARD_VOIDResult;
    }


    /**
     * Sets the BANKCARD_VOIDResult value for this BANKCARD_VOIDResponse.
     * 
     * @param BANKCARD_VOIDResult
     */
    public void setBANKCARD_VOIDResult(com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_VOIDResponseBANKCARD_VOIDResult BANKCARD_VOIDResult) {
        this.BANKCARD_VOIDResult = BANKCARD_VOIDResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BANKCARD_VOIDResponse)) return false;
        BANKCARD_VOIDResponse other = (BANKCARD_VOIDResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BANKCARD_VOIDResult==null && other.getBANKCARD_VOIDResult()==null) || 
             (this.BANKCARD_VOIDResult!=null &&
              this.BANKCARD_VOIDResult.equals(other.getBANKCARD_VOIDResult())));
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
        if (getBANKCARD_VOIDResult() != null) {
            _hashCode += getBANKCARD_VOIDResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BANKCARD_VOIDResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">BANKCARD_VOIDResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BANKCARD_VOIDResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "BANKCARD_VOIDResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>BANKCARD_VOIDResponse>BANKCARD_VOIDResult"));
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
