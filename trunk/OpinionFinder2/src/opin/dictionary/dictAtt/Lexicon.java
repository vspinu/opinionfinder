//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.10 at 10:05:25 AM EDT 
//


package opin.dictionary.dictAtt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entryAttributes" type="{http://www.cs.pitt.edu/mpqa/}entryAttributesBaseType" maxOccurs="unbounded"/>
 *         &lt;element name="lexiconMetadata" type="{http://www.cs.pitt.edu/mpqa/}lexiconMetadataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entryAttributes",
    "lexiconMetadata"
})
@XmlRootElement(name = "lexicon")
public class Lexicon {

    @XmlElement(required = true)
    protected List<EntryAttributesBaseType> entryAttributes;
    @XmlElement(required = true)
    protected LexiconMetadataType lexiconMetadata;

    /**
     * Gets the value of the entryAttributes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entryAttributes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntryAttributes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntryAttributesBaseType }
     * 
     * 
     */
    public List<EntryAttributesBaseType> getEntryAttributes() {
        if (entryAttributes == null) {
            entryAttributes = new ArrayList<EntryAttributesBaseType>();
        }
        return this.entryAttributes;
    }

    /**
     * Gets the value of the lexiconMetadata property.
     * 
     * @return
     *     possible object is
     *     {@link LexiconMetadataType }
     *     
     */
    public LexiconMetadataType getLexiconMetadata() {
        return lexiconMetadata;
    }

    /**
     * Sets the value of the lexiconMetadata property.
     * 
     * @param value
     *     allowed object is
     *     {@link LexiconMetadataType }
     *     
     */
    public void setLexiconMetadata(LexiconMetadataType value) {
        this.lexiconMetadata = value;
    }

}
