/**
 * INSERT_BULK_DATAResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVault;

public class INSERT_BULK_DATAResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[] INSERT_BULK_DATAResult;

    public INSERT_BULK_DATAResponse() {
    }

    public INSERT_BULK_DATAResponse(
           com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[] INSERT_BULK_DATAResult) {
           this.INSERT_BULK_DATAResult = INSERT_BULK_DATAResult;
    }


    /**
     * Gets the INSERT_BULK_DATAResult value for this INSERT_BULK_DATAResponse.
     * 
     * @return INSERT_BULK_DATAResult
     */
    public com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[] getINSERT_BULK_DATAResult() {
        return INSERT_BULK_DATAResult;
    }


    /**
     * Sets the INSERT_BULK_DATAResult value for this INSERT_BULK_DATAResponse.
     * 
     * @param INSERT_BULK_DATAResult
     */
    public void setINSERT_BULK_DATAResult(com.aoindustries.creditcards.sagePayments.wsVault.BULK_RESULT[] INSERT_BULK_DATAResult) {
        this.INSERT_BULK_DATAResult = INSERT_BULK_DATAResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof INSERT_BULK_DATAResponse)) return false;
        INSERT_BULK_DATAResponse other = (INSERT_BULK_DATAResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.INSERT_BULK_DATAResult==null && other.getINSERT_BULK_DATAResult()==null) || 
             (this.INSERT_BULK_DATAResult!=null &&
              java.util.Arrays.equals(this.INSERT_BULK_DATAResult, other.getINSERT_BULK_DATAResult())));
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
        if (getINSERT_BULK_DATAResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getINSERT_BULK_DATAResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getINSERT_BULK_DATAResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(INSERT_BULK_DATAResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">INSERT_BULK_DATAResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("INSERT_BULK_DATAResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "INSERT_BULK_DATAResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "BULK_RESULT"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "BULK_RESULT"));
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
