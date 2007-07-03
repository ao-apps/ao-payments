/**
 * BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.transaction_processing;

public class BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS  implements java.io.Serializable {
    private java.lang.String m_ID;

    private java.lang.String m_KEY;

    private java.lang.String t_AMT;

    private java.lang.String c_CARDNUMBER;

    private java.lang.String c_EXP;

    private java.lang.String c_ADDRESS;

    private java.lang.String c_ZIP;

    public BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS() {
    }

    public BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS(
           java.lang.String m_ID,
           java.lang.String m_KEY,
           java.lang.String t_AMT,
           java.lang.String c_CARDNUMBER,
           java.lang.String c_EXP,
           java.lang.String c_ADDRESS,
           java.lang.String c_ZIP) {
           this.m_ID = m_ID;
           this.m_KEY = m_KEY;
           this.t_AMT = t_AMT;
           this.c_CARDNUMBER = c_CARDNUMBER;
           this.c_EXP = c_EXP;
           this.c_ADDRESS = c_ADDRESS;
           this.c_ZIP = c_ZIP;
    }


    /**
     * Gets the m_ID value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @return m_ID
     */
    public java.lang.String getM_ID() {
        return m_ID;
    }


    /**
     * Sets the m_ID value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @param m_ID
     */
    public void setM_ID(java.lang.String m_ID) {
        this.m_ID = m_ID;
    }


    /**
     * Gets the m_KEY value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @return m_KEY
     */
    public java.lang.String getM_KEY() {
        return m_KEY;
    }


    /**
     * Sets the m_KEY value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @param m_KEY
     */
    public void setM_KEY(java.lang.String m_KEY) {
        this.m_KEY = m_KEY;
    }


    /**
     * Gets the t_AMT value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @return t_AMT
     */
    public java.lang.String getT_AMT() {
        return t_AMT;
    }


    /**
     * Sets the t_AMT value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @param t_AMT
     */
    public void setT_AMT(java.lang.String t_AMT) {
        this.t_AMT = t_AMT;
    }


    /**
     * Gets the c_CARDNUMBER value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @return c_CARDNUMBER
     */
    public java.lang.String getC_CARDNUMBER() {
        return c_CARDNUMBER;
    }


    /**
     * Sets the c_CARDNUMBER value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @param c_CARDNUMBER
     */
    public void setC_CARDNUMBER(java.lang.String c_CARDNUMBER) {
        this.c_CARDNUMBER = c_CARDNUMBER;
    }


    /**
     * Gets the c_EXP value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @return c_EXP
     */
    public java.lang.String getC_EXP() {
        return c_EXP;
    }


    /**
     * Sets the c_EXP value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @param c_EXP
     */
    public void setC_EXP(java.lang.String c_EXP) {
        this.c_EXP = c_EXP;
    }


    /**
     * Gets the c_ADDRESS value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @return c_ADDRESS
     */
    public java.lang.String getC_ADDRESS() {
        return c_ADDRESS;
    }


    /**
     * Sets the c_ADDRESS value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @param c_ADDRESS
     */
    public void setC_ADDRESS(java.lang.String c_ADDRESS) {
        this.c_ADDRESS = c_ADDRESS;
    }


    /**
     * Gets the c_ZIP value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @return c_ZIP
     */
    public java.lang.String getC_ZIP() {
        return c_ZIP;
    }


    /**
     * Sets the c_ZIP value for this BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.
     * 
     * @param c_ZIP
     */
    public void setC_ZIP(java.lang.String c_ZIP) {
        this.c_ZIP = c_ZIP;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS)) return false;
        BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS other = (BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS) obj;
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
            ((this.c_CARDNUMBER==null && other.getC_CARDNUMBER()==null) || 
             (this.c_CARDNUMBER!=null &&
              this.c_CARDNUMBER.equals(other.getC_CARDNUMBER()))) &&
            ((this.c_EXP==null && other.getC_EXP()==null) || 
             (this.c_EXP!=null &&
              this.c_EXP.equals(other.getC_EXP()))) &&
            ((this.c_ADDRESS==null && other.getC_ADDRESS()==null) || 
             (this.c_ADDRESS!=null &&
              this.c_ADDRESS.equals(other.getC_ADDRESS()))) &&
            ((this.c_ZIP==null && other.getC_ZIP()==null) || 
             (this.c_ZIP!=null &&
              this.c_ZIP.equals(other.getC_ZIP())));
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
        if (getC_CARDNUMBER() != null) {
            _hashCode += getC_CARDNUMBER().hashCode();
        }
        if (getC_EXP() != null) {
            _hashCode += getC_EXP().hashCode();
        }
        if (getC_ADDRESS() != null) {
            _hashCode += getC_ADDRESS().hashCode();
        }
        if (getC_ZIP() != null) {
            _hashCode += getC_ZIP().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", ">BANKCARD_RETAIL_KEYED_AUTHONLY_WITH_AVS"));
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
        elemField.setFieldName("c_CARDNUMBER");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "C_CARDNUMBER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("c_EXP");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "C_EXP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("c_ADDRESS");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "C_ADDRESS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("c_ZIP");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/vterm_extensions/TRANSACTION_PROCESSING", "C_ZIP"));
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
