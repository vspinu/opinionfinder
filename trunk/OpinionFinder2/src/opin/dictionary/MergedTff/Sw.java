//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.10 at 10:09:30 AM EDT 
//


package opin.dictionary.MergedTff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sw complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sw">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.cs.pitt.edu/mpqa/}sw">
 *       &lt;redefine>
 *         &lt;complexType name="sw">
 *           &lt;complexContent>
 *             &lt;extension base="{http://www.cs.pitt.edu/mpqa/}entryBaseType">
 *               &lt;sequence>
 *                 &lt;element name="entryDefinition" type="{http://www.cs.pitt.edu/mpqa/}entryDefinitionSwType"/>
 *               &lt;/sequence>
 *             &lt;/extension>
 *           &lt;/complexContent>
 *         &lt;/complexType>
 *       &lt;/redefine>
 *       &lt;sequence>
 *         &lt;element name="entryAttributes" type="{http://www.cs.pitt.edu/mpqa/}entryAttributesBaseType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sw", propOrder = {
    "entryAttributes"
})
public class Sw
    extends OriginalSw
{

    @XmlElement(required = true)
    protected EntryAttributesBaseType entryAttributes;

    /**
     * Gets the value of the entryAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link EntryAttributesBaseType }
     *     
     */
    public EntryAttributesBaseType getEntryAttributes() {
        return entryAttributes;
    }

    /**
     * Sets the value of the entryAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntryAttributesBaseType }
     *     
     */
    public void setEntryAttributes(EntryAttributesBaseType value) {
        this.entryAttributes = value;
    }

}
