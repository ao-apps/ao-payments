/**
 * UPDATE_CREDIT_CARD_DATA.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aoindustries.creditcards.sagePayments.wsVault;

public class UPDATE_CREDIT_CARD_DATA  implements java.io.Serializable {
    private java.lang.String m_ID;

    private java.lang.String m_KEY;

    private java.lang.String GUID;

    private java.lang.String CARDNUMBER;

    private java.lang.String EXPIRATION_DATE;

    public UPDATE_CREDIT_CARD_DATA() {
    }

    public UPDATE_CREDIT_CARD_DATA(
           java.lang.String m_ID,
           java.lang.String m_KEY,
           java.lang.String GUID,
           java.lang.String CARDNUMBER,
           java.lang.String EXPIRATION_DATE) {
           this.m_ID = m_ID;
           this.m_KEY = m_KEY;
           this.GUID = GUID;
           this.CARDNUMBER = CARDNUMBER;
           this.EXPIRATION_DATE = EXPIRATION_DATE;
    }


    /**
     * Gets the m_ID value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @return m_ID
     */
    public java.lang.String getM_ID() {
        return m_ID;
    }


    /**
     * Sets the m_ID value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @param m_ID
     */
    public void setM_ID(java.lang.String m_ID) {
        this.m_ID = m_ID;
    }


    /**
     * Gets the m_KEY value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @return m_KEY
     */
    public java.lang.String getM_KEY() {
        return m_KEY;
    }


    /**
     * Sets the m_KEY value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @param m_KEY
     */
    public void setM_KEY(java.lang.String m_KEY) {
        this.m_KEY = m_KEY;
    }


    /**
     * Gets the GUID value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @return GUID
     */
    public java.lang.String getGUID() {
        return GUID;
    }


    /**
     * Sets the GUID value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @param GUID
     */
    public void setGUID(java.lang.String GUID) {
        this.GUID = GUID;
    }


    /**
     * Gets the CARDNUMBER value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @return CARDNUMBER
     */
    public java.lang.String getCARDNUMBER() {
        return CARDNUMBER;
    }


    /**
     * Sets the CARDNUMBER value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @param CARDNUMBER
     */
    public void setCARDNUMBER(java.lang.String CARDNUMBER) {
        this.CARDNUMBER = CARDNUMBER;
    }


    /**
     * Gets the EXPIRATION_DATE value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @return EXPIRATION_DATE
     */
    public java.lang.String getEXPIRATION_DATE() {
        return EXPIRATION_DATE;
    }


    /**
     * Sets the EXPIRATION_DATE value for this UPDATE_CREDIT_CARD_DATA.
     * 
     * @param EXPIRATION_DATE
     */
    public void setEXPIRATION_DATE(java.lang.String EXPIRATION_DATE) {
        this.EXPIRATION_DATE = EXPIRATION_DATE;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UPDATE_CREDIT_CARD_DATA)) return false;
        UPDATE_CREDIT_CARD_DATA other = (UPDATE_CREDIT_CARD_DATA) obj;
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
            ((this.GUID==null && other.getGUID()==null) || 
             (this.GUID!=null &&
              this.GUID.equals(other.getGUID()))) &&
            ((this.CARDNUMBER==null && other.getCARDNUMBER()==null) || 
             (this.CARDNUMBER!=null &&
              this.CARDNUMBER.equals(other.getCARDNUMBER()))) &&
            ((this.EXPIRATION_DATE==null && other.getEXPIRATION_DATE()==null) || 
             (this.EXPIRATION_DATE!=null &&
              this.EXPIRATION_DATE.equals(other.getEXPIRATION_DATE())));
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
        if (getGUID() != null) {
            _hashCode += getGUID().hashCode();
        }
        if (getCARDNUMBER() != null) {
            _hashCode += getCARDNUMBER().hashCode();
        }
        if (getEXPIRATION_DATE() != null) {
            _hashCode += getEXPIRATION_DATE().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UPDATE_CREDIT_CARD_DATA.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", ">UPDATE_CREDIT_CARD_DATA"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("m_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("m_KEY");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "M_KEY"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("GUID");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "GUID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CARDNUMBER");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "CARDNUMBER"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EXPIRATION_DATE");
        elemField.setXmlName(new javax.xml.namespace.QName("https://www.sagepayments.net/web_services/wsVault/wsVault", "EXPIRATION_DATE"));
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
