/**
 * BANKCARD_PRIOR_AUTH_SALE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class BANKCARD_PRIOR_AUTH_SALE  implements java.io.Serializable {
    private java.lang.String m_ID;

    private java.lang.String m_KEY;

    private java.lang.String t_AMT;

    private java.lang.String t_SHIPPING;

    private java.lang.String t_TAX;

    private java.lang.String t_AUTH;

    private java.lang.String t_REFERENCE;

    public BANKCARD_PRIOR_AUTH_SALE() {
    }

    public BANKCARD_PRIOR_AUTH_SALE(
           java.lang.String m_ID,
           java.lang.String m_KEY,
           java.lang.String t_AMT,
           java.lang.String t_SHIPPING,
           java.lang.String t_TAX,
           java.lang.String t_AUTH,
           java.lang.String t_REFERENCE) {
           this.m_ID = m_ID;
           this.m_KEY = m_KEY;
           this.t_AMT = t_AMT;
           this.t_SHIPPING = t_SHIPPING;
           this.t_TAX = t_TAX;
           this.t_AUTH = t_AUTH;
           this.t_REFERENCE = t_REFERENCE;
    }


    /**
     * Gets the m_ID value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @return m_ID
     */
    public java.lang.String getM_ID() {
        return m_ID;
    }


    /**
     * Sets the m_ID value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @param m_ID
     */
    public void setM_ID(java.lang.String m_ID) {
        this.m_ID = m_ID;
    }


    /**
     * Gets the m_KEY value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @return m_KEY
     */
    public java.lang.String getM_KEY() {
        return m_KEY;
    }


    /**
     * Sets the m_KEY value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @param m_KEY
     */
    public void setM_KEY(java.lang.String m_KEY) {
        this.m_KEY = m_KEY;
    }


    /**
     * Gets the t_AMT value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @return t_AMT
     */
    public java.lang.String getT_AMT() {
        return t_AMT;
    }


    /**
     * Sets the t_AMT value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @param t_AMT
     */
    public void setT_AMT(java.lang.String t_AMT) {
        this.t_AMT = t_AMT;
    }


    /**
     * Gets the t_SHIPPING value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @return t_SHIPPING
     */
    public java.lang.String getT_SHIPPING() {
        return t_SHIPPING;
    }


    /**
     * Sets the t_SHIPPING value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @param t_SHIPPING
     */
    public void setT_SHIPPING(java.lang.String t_SHIPPING) {
        this.t_SHIPPING = t_SHIPPING;
    }


    /**
     * Gets the t_TAX value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @return t_TAX
     */
    public java.lang.String getT_TAX() {
        return t_TAX;
    }


    /**
     * Sets the t_TAX value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @param t_TAX
     */
    public void setT_TAX(java.lang.String t_TAX) {
        this.t_TAX = t_TAX;
    }


    /**
     * Gets the t_AUTH value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @return t_AUTH
     */
    public java.lang.String getT_AUTH() {
        return t_AUTH;
    }


    /**
     * Sets the t_AUTH value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @param t_AUTH
     */
    public void setT_AUTH(java.lang.String t_AUTH) {
        this.t_AUTH = t_AUTH;
    }


    /**
     * Gets the t_REFERENCE value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @return t_REFERENCE
     */
    public java.lang.String getT_REFERENCE() {
        return t_REFERENCE;
    }


    /**
     * Sets the t_REFERENCE value for this BANKCARD_PRIOR_AUTH_SALE.
     * 
     * @param t_REFERENCE
     */
    public void setT_REFERENCE(java.lang.String t_REFERENCE) {
        this.t_REFERENCE = t_REFERENCE;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BANKCARD_PRIOR_AUTH_SALE)) return false;
        BANKCARD_PRIOR_AUTH_SALE other = (BANKCARD_PRIOR_AUTH_SALE) obj;
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
            ((this.t_SHIPPING==null && other.getT_SHIPPING()==null) || 
             (this.t_SHIPPING!=null &&
              this.t_SHIPPING.equals(other.getT_SHIPPING()))) &&
            ((this.t_TAX==null && other.getT_TAX()==null) || 
             (this.t_TAX!=null &&
              this.t_TAX.equals(other.getT_TAX()))) &&
            ((this.t_AUTH==null && other.getT_AUTH()==null) || 
             (this.t_AUTH!=null &&
              this.t_AUTH.equals(other.getT_AUTH()))) &&
            ((this.t_REFERENCE==null && other.getT_REFERENCE()==null) || 
             (this.t_REFERENCE!=null &&
              this.t_REFERENCE.equals(other.getT_REFERENCE())));
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
        if (getT_SHIPPING() != null) {
            _hashCode += getT_SHIPPING().hashCode();
        }
        if (getT_TAX() != null) {
            _hashCode += getT_TAX().hashCode();
        }
        if (getT_AUTH() != null) {
            _hashCode += getT_AUTH().hashCode();
        }
        if (getT_REFERENCE() != null) {
            _hashCode += getT_REFERENCE().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BANKCARD_PRIOR_AUTH_SALE.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">BANKCARD_PRIOR_AUTH_SALE"));
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
        elemField.setFieldName("t_SHIPPING");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "T_SHIPPING"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("t_TAX");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "T_TAX"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("t_AUTH");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "T_AUTH"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("t_REFERENCE");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "T_REFERENCE"));
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
