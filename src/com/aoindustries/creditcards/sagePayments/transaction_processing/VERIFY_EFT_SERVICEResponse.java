/**
 * VERIFY_EFT_SERVICEResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class VERIFY_EFT_SERVICEResponse  implements java.io.Serializable {
    private com.aoindustries.creditcards.sagePayments.transaction_processing.VERIFY_EFT_SERVICEResponseVERIFY_EFT_SERVICEResult VERIFY_EFT_SERVICEResult;

    public VERIFY_EFT_SERVICEResponse() {
    }

    public VERIFY_EFT_SERVICEResponse(
           com.aoindustries.creditcards.sagePayments.transaction_processing.VERIFY_EFT_SERVICEResponseVERIFY_EFT_SERVICEResult VERIFY_EFT_SERVICEResult) {
           this.VERIFY_EFT_SERVICEResult = VERIFY_EFT_SERVICEResult;
    }


    /**
     * Gets the VERIFY_EFT_SERVICEResult value for this VERIFY_EFT_SERVICEResponse.
     * 
     * @return VERIFY_EFT_SERVICEResult
     */
    public com.aoindustries.creditcards.sagePayments.transaction_processing.VERIFY_EFT_SERVICEResponseVERIFY_EFT_SERVICEResult getVERIFY_EFT_SERVICEResult() {
        return VERIFY_EFT_SERVICEResult;
    }


    /**
     * Sets the VERIFY_EFT_SERVICEResult value for this VERIFY_EFT_SERVICEResponse.
     * 
     * @param VERIFY_EFT_SERVICEResult
     */
    public void setVERIFY_EFT_SERVICEResult(com.aoindustries.creditcards.sagePayments.transaction_processing.VERIFY_EFT_SERVICEResponseVERIFY_EFT_SERVICEResult VERIFY_EFT_SERVICEResult) {
        this.VERIFY_EFT_SERVICEResult = VERIFY_EFT_SERVICEResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VERIFY_EFT_SERVICEResponse)) return false;
        VERIFY_EFT_SERVICEResponse other = (VERIFY_EFT_SERVICEResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VERIFY_EFT_SERVICEResult==null && other.getVERIFY_EFT_SERVICEResult()==null) || 
             (this.VERIFY_EFT_SERVICEResult!=null &&
              this.VERIFY_EFT_SERVICEResult.equals(other.getVERIFY_EFT_SERVICEResult())));
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
        if (getVERIFY_EFT_SERVICEResult() != null) {
            _hashCode += getVERIFY_EFT_SERVICEResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VERIFY_EFT_SERVICEResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">VERIFY_EFT_SERVICEResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VERIFY_EFT_SERVICEResult");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "VERIFY_EFT_SERVICEResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">>VERIFY_EFT_SERVICEResponse>VERIFY_EFT_SERVICEResult"));
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
