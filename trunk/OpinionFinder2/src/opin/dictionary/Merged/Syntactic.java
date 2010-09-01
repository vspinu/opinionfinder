//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.10 at 10:06:49 AM EDT 
//


package opin.dictionary.Merged;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for syntactic complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="syntactic">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.cs.pitt.edu/mpqa/}relationBaseType">
 *       &lt;attribute name="dep" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="force" type="{http://www.cs.pitt.edu/mpqa/}forceType" default="positive" />
 *       &lt;attribute name="head" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="type" type="{http://www.cs.pitt.edu/mpqa/}depTagType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "syntactic")
public class Syntactic
    extends RelationBaseType
{

    @XmlAttribute(required = true)
    protected int dep;
    @XmlAttribute
    protected ForceType force;
    @XmlAttribute(required = true)
    protected int head;
    @XmlAttribute
    protected DepTagType type;

    /**
     * Gets the value of the dep property.
     * 
     */
    public int getDep() {
        return dep;
    }

    /**
     * Sets the value of the dep property.
     * 
     */
    public void setDep(int value) {
        this.dep = value;
    }

    /**
     * Gets the value of the force property.
     * 
     * @return
     *     possible object is
     *     {@link ForceType }
     *     
     */
    public ForceType getForce() {
        if (force == null) {
            return ForceType.POSITIVE;
        } else {
            return force;
        }
    }

    /**
     * Sets the value of the force property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForceType }
     *     
     */
    public void setForce(ForceType value) {
        this.force = value;
    }

    /**
     * Gets the value of the head property.
     * 
     */
    public int getHead() {
        return head;
    }

    /**
     * Sets the value of the head property.
     * 
     */
    public void setHead(int value) {
        this.head = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link DepTagType }
     *     
     */
    public DepTagType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link DepTagType }
     *     
     */
    public void setType(DepTagType value) {
        this.type = value;
    }

}
