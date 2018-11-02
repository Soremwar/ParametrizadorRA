
package co.com.claro.service.soap.odi.consumer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DebugType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DebugType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Debug" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="SuspendOnFirstStep" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DebugDescendants" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="BreakOnError" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DebugType", propOrder = {
    "debug",
    "suspendOnFirstStep",
    "debugDescendants",
    "breakOnError"
})
public class DebugType {

    @XmlElement(name = "Debug", defaultValue = "false")
    protected Boolean debug;
    @XmlElement(name = "SuspendOnFirstStep", defaultValue = "true")
    protected Boolean suspendOnFirstStep;
    @XmlElement(name = "DebugDescendants", defaultValue = "false")
    protected Boolean debugDescendants;
    @XmlElement(name = "BreakOnError", defaultValue = "false")
    protected Boolean breakOnError;

    /**
     * Gets the value of the debug property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDebug() {
        return debug;
    }

    /**
     * Sets the value of the debug property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDebug(Boolean value) {
        this.debug = value;
    }

    /**
     * Gets the value of the suspendOnFirstStep property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSuspendOnFirstStep() {
        return suspendOnFirstStep;
    }

    /**
     * Sets the value of the suspendOnFirstStep property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSuspendOnFirstStep(Boolean value) {
        this.suspendOnFirstStep = value;
    }

    /**
     * Gets the value of the debugDescendants property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDebugDescendants() {
        return debugDescendants;
    }

    /**
     * Sets the value of the debugDescendants property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDebugDescendants(Boolean value) {
        this.debugDescendants = value;
    }

    /**
     * Gets the value of the breakOnError property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBreakOnError() {
        return breakOnError;
    }

    /**
     * Sets the value of the breakOnError property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBreakOnError(Boolean value) {
        this.breakOnError = value;
    }

}
