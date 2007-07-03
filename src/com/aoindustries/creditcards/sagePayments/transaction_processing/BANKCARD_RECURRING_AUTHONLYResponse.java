/**
 * BANKCARD_RECURRING_AUTHONLYResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class BANKCARD_RECURRING_AUTHONLYResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RECURRING_AUTHONLYResponseBANKCARD_RECURRING_AUTHONLYResult BANKCARD_RECURRING_AUTHONLYResult;

    public BANKCARD_RECURRING_AUTHONLYResponse() {
    }

    public BANKCARD_RECURRING_AUTHONLYResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RECURRING_AUTHONLYResponseBANKCARD_RECURRING_AUTHONLYResult BANKCARD_RECURRING_AUTHONLYResult) {
           this.BANKCARD_RECURRING_AUTHONLYResult = BANKCARD_RECURRING_AUTHONLYResult;
    }


    /**
     * Gets the BANKCARD_RECURRING_AUTHONLYResult value for this BANKCARD_RECURRING_AUTHONLYResponse.
     * 
     * @return BANKCARD_RECURRING_AUTHONLYResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RECURRING_AUTHONLYResponseBANKCARD_RECURRING_AUTHONLYResult getBANKCARD_RECURRING_AUTHONLYResult() {
        return BANKCARD_RECURRING_AUTHONLYResult;
    }


    /**
     * Sets the BANKCARD_RECURRING_AUTHONLYResult value for this BANKCARD_RECURRING_AUTHONLYResponse.
     * 
     * @param BANKCARD_RECURRING_AUTHONLYResult
     */
    public void setBANKCARD_RECURRING_AUTHONLYResult(com.aoindustries.creditcards.sagePayments.transaction_processing.BANKCARD_RECURRING_AUTHONLYResponseBANKCARD_RECURRING_AUTHONLYResult BANKCARD_RECURRING_AUTHONLYResult) {
        this.BANKCARD_RECURRING_AUTHONLYResult = BANKCARD_RECURRING_AUTHONLYResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BANKCARD_RECURRING_AUTHONLYResponse)) return false;
        BANKCARD_RECURRING_AUTHONLYResponse other = (BANKCARD_RECURRING_AUTHONLYResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.BANKCARD_RECURRING_AUTHONLYResult==null && other.getBANKCARD_RECURRING_AUTHONLYResult()==null) || 
             (this.BANKCARD_RECURRING_AUTHONLYResult!=null &&
              this.BANKCARD_RECURRING_AUTHONLYResult.equals(other.getBANKCARD_RECURRING_AUTHONLYResult())));
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
        if (getBANKCARD_RECURRING_AUTHONLYResult() != null) {
            _hashCode += getBANKCARD_RECURRING_AUTHONLYResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BANKCARD_RECURRING_AUTHONLYResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">BANKCARD_RECURRING_AUTHONLYResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BANKCARD_RECURRING_AUTHONLYResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "BANKCARD_RECURRING_AUTHONLYResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>BANKCARD_RECURRING_AUTHONLYResponse>BANKCARD_RECURRING_AUTHONLYResult"));
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
