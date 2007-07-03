/**
 * BANKCARD_RETAIL_SWIPED_AUTHONLY.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class BANKCARD_RETAIL_SWIPED_AUTHONLY  implements java.io.Serializable {
    private java.lang.String m_ID;

    private java.lang.String m_KEY;

    private java.lang.String t_AMT;

    private java.lang.String t_TRACKDATA;

    public BANKCARD_RETAIL_SWIPED_AUTHONLY() {
    }

    public BANKCARD_RETAIL_SWIPED_AUTHONLY(
           java.lang.String m_ID,
           java.lang.String m_KEY,
           java.lang.String t_AMT,
           java.lang.String t_TRACKDATA) {
           this.m_ID = m_ID;
           this.m_KEY = m_KEY;
           this.t_AMT = t_AMT;
           this.t_TRACKDATA = t_TRACKDATA;
    }


    /**
     * Gets the m_ID value for this BANKCARD_RETAIL_SWIPED_AUTHONLY.
     * 
     * @return m_ID
     */
    public java.lang.String getM_ID() {
        return m_ID;
    }


    /**
     * Sets the m_ID value for this BANKCARD_RETAIL_SWIPED_AUTHONLY.
     * 
     * @param m_ID
     */
    public void setM_ID(java.lang.String m_ID) {
        this.m_ID = m_ID;
    }


    /**
     * Gets the m_KEY value for this BANKCARD_RETAIL_SWIPED_AUTHONLY.
     * 
     * @return m_KEY
     */
    public java.lang.String getM_KEY() {
        return m_KEY;
    }


    /**
     * Sets the m_KEY value for this BANKCARD_RETAIL_SWIPED_AUTHONLY.
     * 
     * @param m_KEY
     */
    public void setM_KEY(java.lang.String m_KEY) {
        this.m_KEY = m_KEY;
    }


    /**
     * Gets the t_AMT value for this BANKCARD_RETAIL_SWIPED_AUTHONLY.
     * 
     * @return t_AMT
     */
    public java.lang.String getT_AMT() {
        return t_AMT;
    }


    /**
     * Sets the t_AMT value for this BANKCARD_RETAIL_SWIPED_AUTHONLY.
     * 
     * @param t_AMT
     */
    public void setT_AMT(java.lang.String t_AMT) {
        this.t_AMT = t_AMT;
    }


    /**
     * Gets the t_TRACKDATA value for this BANKCARD_RETAIL_SWIPED_AUTHONLY.
     * 
     * @return t_TRACKDATA
     */
    public java.lang.String getT_TRACKDATA() {
        return t_TRACKDATA;
    }


    /**
     * Sets the t_TRACKDATA value for this BANKCARD_RETAIL_SWIPED_AUTHONLY.
     * 
     * @param t_TRACKDATA
     */
    public void setT_TRACKDATA(java.lang.String t_TRACKDATA) {
        this.t_TRACKDATA = t_TRACKDATA;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BANKCARD_RETAIL_SWIPED_AUTHONLY)) return false;
        BANKCARD_RETAIL_SWIPED_AUTHONLY other = (BANKCARD_RETAIL_SWIPED_AUTHONLY) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.m_ID==null && other.getM_ID()==null) || 
             (this.m_ID!=null &&
              this.m_ID.equals(other.getM_ID()))) &&
            ((this.m_KEY==null && other.getM_KEY()==null) || 
             (this.m_KEY!=null &&
              this.m_KEY.equals(other.getM_KEY()))) &&
            ((this.t_AMT==null && other.getT_AMT()==null) || 
             (this.t_AMT!=null &&
              this.t_AMT.equals(other.getT_AMT()))) &&
            ((this.t_TRACKDATA==null && other.getT_TRACKDATA()==null) || 
             (this.t_TRACKDATA!=null &&
              this.t_TRACKDATA.equals(other.getT_TRACKDATA())));
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
        if (getM_ID() != null) {
            _hashCode += getM_ID().hashCode();
        }
        if (getM_KEY() != null) {
            _hashCode += getM_KEY().hashCode();
        }
        if (getT_AMT() != null) {
            _hashCode += getT_AMT().hashCode();
        }
        if (getT_TRACKDATA() != null) {
            _hashCode += getT_TRACKDATA().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BANKCARD_RETAIL_SWIPED_AUTHONLY.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">BANKCARD_RETAIL_SWIPED_AUTHONLY"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("m_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "M_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("m_KEY");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "M_KEY"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("t_AMT");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "T_AMT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("t_TRACKDATA");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "T_TRACKDATA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
