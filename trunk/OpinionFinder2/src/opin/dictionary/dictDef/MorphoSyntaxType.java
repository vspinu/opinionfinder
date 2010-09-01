//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.10 at 10:03:40 AM EDT 
//


package opin.dictionary.dictDef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for morphoSyntaxType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="morphoSyntaxType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lemma" type="{http://www.cs.pitt.edu/mpqa/}lemmaType" minOccurs="0"/>
 *         &lt;element name="word" type="{http://www.cs.pitt.edu/mpqa/}wordType" minOccurs="0"/>
 *         &lt;element name="mC" type="{http://www.cs.pitt.edu/mpqa/}majorClassType" minOccurs="0"/>
 *         &lt;element name="pos" type="{http://www.cs.pitt.edu/mpqa/}posType" minOccurs="0"/>
 *         &lt;element name="sense" type="{http://www.cs.pitt.edu/mpqa/}senseBaseType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "morphoSyntaxType", propOrder = {
    "lemma",
    "word",
    "mc",
    "pos",
    "sense"
})
public class MorphoSyntaxType {

    protected LemmaType lemma;
    protected WordType word;
    @XmlElement(name = "mC")
    protected MajorClassType mc;
    protected PosType pos;
    protected SenseBaseType sense;

    /**
     * Gets the value of the lemma property.
     * 
     * @return
     *     possible object is
     *     {@link LemmaType }
     *     
     */
    public LemmaType getLemma() {
        return lemma;
    }

    /**
     * Sets the value of the lemma property.
     * 
     * @param value
     *     allowed object is
     *     {@link LemmaType }
     *     
     */
    public void setLemma(LemmaType value) {
        this.lemma = value;
    }

    /**
     * Gets the value of the word property.
     * 
     * @return
     *     possible object is
     *     {@link WordType }
     *     
     */
    public WordType getWord() {
        return word;
    }

    /**
     * Sets the value of the word property.
     * 
     * @param value
     *     allowed object is
     *     {@link WordType }
     *     
     */
    public void setWord(WordType value) {
        this.word = value;
    }

    /**
     * Gets the value of the mc property.
     * 
     * @return
     *     possible object is
     *     {@link MajorClassType }
     *     
     */
    public MajorClassType getMC() {
        return mc;
    }

    /**
     * Sets the value of the mc property.
     * 
     * @param value
     *     allowed object is
     *     {@link MajorClassType }
     *     
     */
    public void setMC(MajorClassType value) {
        this.mc = value;
    }

    /**
     * Gets the value of the pos property.
     * 
     * @return
     *     possible object is
     *     {@link PosType }
     *     
     */
    public PosType getPos() {
        return pos;
    }

    /**
     * Sets the value of the pos property.
     * 
     * @param value
     *     allowed object is
     *     {@link PosType }
     *     
     */
    public void setPos(PosType value) {
        this.pos = value;
    }

    /**
     * Gets the value of the sense property.
     * 
     * @return
     *     possible object is
     *     {@link SenseBaseType }
     *     
     */
    public SenseBaseType getSense() {
        return sense;
    }

    /**
     * Sets the value of the sense property.
     * 
     * @param value
     *     allowed object is
     *     {@link SenseBaseType }
     *     
     */
    public void setSense(SenseBaseType value) {
        this.sense = value;
    }

}
