//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.10 at 10:03:40 AM EDT 
//


package opin.dictionary.dictDef;

import javax.xml.bind.annotation.XmlEnum;


/**
 * <p>Java class for majorClassTagType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="majorClassTagType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="V"/>
 *     &lt;enumeration value="J"/>
 *     &lt;enumeration value="N"/>
 *     &lt;enumeration value="R"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum MajorClassTagType {

    V,
    J,
    N,
    R;

    public String value() {
        return name();
    }

    public static MajorClassTagType fromValue(String v) {
        return valueOf(v);
    }

}
