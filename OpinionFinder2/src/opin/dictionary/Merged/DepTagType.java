//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.10 at 10:06:49 AM EDT 
//


package opin.dictionary.Merged;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for depTagType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="depTagType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="gov"/>
 *     &lt;enumeration value="dep"/>
 *     &lt;enumeration value="pred"/>
 *     &lt;enumeration value="aux"/>
 *     &lt;enumeration value="auxpass"/>
 *     &lt;enumeration value="cop"/>
 *     &lt;enumeration value="conj"/>
 *     &lt;enumeration value="cc"/>
 *     &lt;enumeration value="punct"/>
 *     &lt;enumeration value="arg"/>
 *     &lt;enumeration value="subj"/>
 *     &lt;enumeration value="nsubj"/>
 *     &lt;enumeration value="nsubjpass"/>
 *     &lt;enumeration value="csubj"/>
 *     &lt;enumeration value="csubjpass"/>
 *     &lt;enumeration value="comp"/>
 *     &lt;enumeration value="obj"/>
 *     &lt;enumeration value="dobj"/>
 *     &lt;enumeration value="iobj"/>
 *     &lt;enumeration value="pobj"/>
 *     &lt;enumeration value="pcomp"/>
 *     &lt;enumeration value="attr"/>
 *     &lt;enumeration value="ccomp"/>
 *     &lt;enumeration value="xcomp"/>
 *     &lt;enumeration value="complm"/>
 *     &lt;enumeration value="mark"/>
 *     &lt;enumeration value="rel"/>
 *     &lt;enumeration value="ref"/>
 *     &lt;enumeration value="expl"/>
 *     &lt;enumeration value="acomp"/>
 *     &lt;enumeration value="mod"/>
 *     &lt;enumeration value="advcl"/>
 *     &lt;enumeration value="purpcl"/>
 *     &lt;enumeration value="tmod"/>
 *     &lt;enumeration value="rcmod"/>
 *     &lt;enumeration value="amod"/>
 *     &lt;enumeration value="num"/>
 *     &lt;enumeration value="number"/>
 *     &lt;enumeration value="quantmod"/>
 *     &lt;enumeration value="nn"/>
 *     &lt;enumeration value="appos"/>
 *     &lt;enumeration value="abbrev"/>
 *     &lt;enumeration value="partmod"/>
 *     &lt;enumeration value="infmod"/>
 *     &lt;enumeration value="advmod"/>
 *     &lt;enumeration value="neg"/>
 *     &lt;enumeration value="measure"/>
 *     &lt;enumeration value="det"/>
 *     &lt;enumeration value="predet"/>
 *     &lt;enumeration value="preconj"/>
 *     &lt;enumeration value="poss"/>
 *     &lt;enumeration value="possessive"/>
 *     &lt;enumeration value="prep"/>
 *     &lt;enumeration value="prt"/>
 *     &lt;enumeration value="sdep"/>
 *     &lt;enumeration value="xsubj"/>
 *     &lt;enumeration value="agent"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum DepTagType {

    @XmlEnumValue("gov")
    GOV("gov"),
    @XmlEnumValue("dep")
    DEP("dep"),
    @XmlEnumValue("pred")
    PRED("pred"),
    @XmlEnumValue("aux")
    AUX("aux"),
    @XmlEnumValue("auxpass")
    AUXPASS("auxpass"),
    @XmlEnumValue("cop")
    COP("cop"),
    @XmlEnumValue("conj")
    CONJ("conj"),
    @XmlEnumValue("cc")
    CC("cc"),
    @XmlEnumValue("punct")
    PUNCT("punct"),
    @XmlEnumValue("arg")
    ARG("arg"),
    @XmlEnumValue("subj")
    SUBJ("subj"),
    @XmlEnumValue("nsubj")
    NSUBJ("nsubj"),
    @XmlEnumValue("nsubjpass")
    NSUBJPASS("nsubjpass"),
    @XmlEnumValue("csubj")
    CSUBJ("csubj"),
    @XmlEnumValue("csubjpass")
    CSUBJPASS("csubjpass"),
    @XmlEnumValue("comp")
    COMP("comp"),
    @XmlEnumValue("obj")
    OBJ("obj"),
    @XmlEnumValue("dobj")
    DOBJ("dobj"),
    @XmlEnumValue("iobj")
    IOBJ("iobj"),
    @XmlEnumValue("pobj")
    POBJ("pobj"),
    @XmlEnumValue("pcomp")
    PCOMP("pcomp"),
    @XmlEnumValue("attr")
    ATTR("attr"),
    @XmlEnumValue("ccomp")
    CCOMP("ccomp"),
    @XmlEnumValue("xcomp")
    XCOMP("xcomp"),
    @XmlEnumValue("complm")
    COMPLM("complm"),
    @XmlEnumValue("mark")
    MARK("mark"),
    @XmlEnumValue("rel")
    REL("rel"),
    @XmlEnumValue("ref")
    REF("ref"),
    @XmlEnumValue("expl")
    EXPL("expl"),
    @XmlEnumValue("acomp")
    ACOMP("acomp"),
    @XmlEnumValue("mod")
    MOD("mod"),
    @XmlEnumValue("advcl")
    ADVCL("advcl"),
    @XmlEnumValue("purpcl")
    PURPCL("purpcl"),
    @XmlEnumValue("tmod")
    TMOD("tmod"),
    @XmlEnumValue("rcmod")
    RCMOD("rcmod"),
    @XmlEnumValue("amod")
    AMOD("amod"),
    @XmlEnumValue("num")
    NUM("num"),
    @XmlEnumValue("number")
    NUMBER("number"),
    @XmlEnumValue("quantmod")
    QUANTMOD("quantmod"),
    @XmlEnumValue("nn")
    NN("nn"),
    @XmlEnumValue("appos")
    APPOS("appos"),
    @XmlEnumValue("abbrev")
    ABBREV("abbrev"),
    @XmlEnumValue("partmod")
    PARTMOD("partmod"),
    @XmlEnumValue("infmod")
    INFMOD("infmod"),
    @XmlEnumValue("advmod")
    ADVMOD("advmod"),
    @XmlEnumValue("neg")
    NEG("neg"),
    @XmlEnumValue("measure")
    MEASURE("measure"),
    @XmlEnumValue("det")
    DET("det"),
    @XmlEnumValue("predet")
    PREDET("predet"),
    @XmlEnumValue("preconj")
    PRECONJ("preconj"),
    @XmlEnumValue("poss")
    POSS("poss"),
    @XmlEnumValue("possessive")
    POSSESSIVE("possessive"),
    @XmlEnumValue("prep")
    PREP("prep"),
    @XmlEnumValue("prt")
    PRT("prt"),
    @XmlEnumValue("sdep")
    SDEP("sdep"),
    @XmlEnumValue("xsubj")
    XSUBJ("xsubj"),
    @XmlEnumValue("agent")
    AGENT("agent");
    private final String value;

    DepTagType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DepTagType fromValue(String v) {
        for (DepTagType c: DepTagType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}
