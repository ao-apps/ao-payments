/**
 * VERIFY_BANKCARD_SERVICEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class VERIFY_BANKCARD_SERVICEResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.VERIFY_BANKCARD_SERVICEResponseVERIFY_BANKCARD_SERVICEResult VERIFY_BANKCARD_SERVICEResult;

    public VERIFY_BANKCARD_SERVICEResponse() {
    }

    public VERIFY_BANKCARD_SERVICEResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.VERIFY_BANKCARD_SERVICEResponseVERIFY_BANKCARD_SERVICEResult VERIFY_BANKCARD_SERVICEResult) {
           this.VERIFY_BANKCARD_SERVICEResult = VERIFY_BANKCARD_SERVICEResult;
    }


    /**
     * Gets the VERIFY_BANKCARD_SERVICEResult value for this VERIFY_BANKCARD_SERVICEResponse.
     * 
     * @return VERIFY_BANKCARD_SERVICEResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.VERIFY_BANKCARD_SERVICEResponseVERIFY_BANKCARD_SERVICEResult getVERIFY_BANKCARD_SERVICEResult() {
        return VERIFY_BANKCARD_SERVICEResult;
    }


    /**
     * Sets the VERIFY_BANKCARD_SERVICEResult value for this VERIFY_BANKCARD_SERVICEResponse.
     * 
     * @param VERIFY_BANKCARD_SERVICEResult
     */
    public void setVERIFY_BANKCARD_SERVICEResult(com.aoindustries.creditcards.sagePayments.transaction_processing.VERIFY_BANKCARD_SERVICEResponseVERIFY_BANKCARD_SERVICEResult VERIFY_BANKCARD_SERVICEResult) {
        this.VERIFY_BANKCARD_SERVICEResult = VERIFY_BANKCARD_SERVICEResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VERIFY_BANKCARD_SERVICEResponse)) return false;
        VERIFY_BANKCARD_SERVICEResponse other = (VERIFY_BANKCARD_SERVICEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VERIFY_BANKCARD_SERVICEResult==null && other.getVERIFY_BANKCARD_SERVICEResult()==null) || 
             (this.VERIFY_BANKCARD_SERVICEResult!=null &&
              this.VERIFY_BANKCARD_SERVICEResult.equals(other.getVERIFY_BANKCARD_SERVICEResult())));
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
        if (getVERIFY_BANKCARD_SERVICEResult() != null) {
            _hashCode += getVERIFY_BANKCARD_SERVICEResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VERIFY_BANKCARD_SERVICEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">VERIFY_BANKCARD_SERVICEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VERIFY_BANKCARD_SERVICEResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "VERIFY_BANKCARD_SERVICEResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>VERIFY_BANKCARD_SERVICEResponse>VERIFY_BANKCARD_SERVICEResult"));
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
