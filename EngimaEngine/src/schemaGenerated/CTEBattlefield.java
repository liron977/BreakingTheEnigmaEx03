//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.09.25 at 07:13:05 PM IDT 
//


package schemaGenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="level" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Easy"/>
 *             &lt;enumeration value="Medium"/>
 *             &lt;enumeration value="Hard"/>
 *             &lt;enumeration value="Insane"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="battle-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="allies" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "CTE-Battlefield")
public class CTEBattlefield {

    @XmlAttribute(name = "level", required = true)
    protected String level;
    @XmlAttribute(name = "battle-name", required = true)
    protected String battleName;
    @XmlAttribute(name = "allies", required = true)
    protected int allies;

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel(String value) {
        this.level = value;
    }

    /**
     * Gets the value of the battleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBattleName() {
        return battleName;
    }

    /**
     * Sets the value of the battleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBattleName(String value) {
        this.battleName = value;
    }

    /**
     * Gets the value of the allies property.
     * 
     */
    public int getAllies() {
        return allies;
    }

    /**
     * Sets the value of the allies property.
     * 
     */
    public void setAllies(int value) {
        this.allies = value;
    }

}
