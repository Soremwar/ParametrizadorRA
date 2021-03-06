
package co.com.claro.service.soap.odi.consumer;

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
 *       &lt;all>
 *         &lt;element name="Credentials" type="{xmlns.oracle.com/odi/OdiInvoke/}OdiCredentialType"/>
 *         &lt;element name="Request" type="{xmlns.oracle.com/odi/OdiInvoke/}SessionRequestType"/>
 *         &lt;element name="Debug" type="{xmlns.oracle.com/odi/OdiInvoke/}DebugType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "OdiRestartSessWithCallbackRequest")
public class OdiRestartSessWithCallbackRequest {

    @XmlElement(name = "Credentials", required = true)
    protected OdiCredentialType credentials;
    @XmlElement(name = "Request", required = true)
    protected SessionRequestType request;
    @XmlElement(name = "Debug")
    protected DebugType debug;

    /**
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link OdiCredentialType }
     *     
     */
    public OdiCredentialType getCredentials() {
        return credentials;
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link OdiCredentialType }
     *     
     */
    public void setCredentials(OdiCredentialType value) {
        this.credentials = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link SessionRequestType }
     *     
     */
    public SessionRequestType getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link SessionRequestType }
     *     
     */
    public void setRequest(SessionRequestType value) {
        this.request = value;
    }

    /**
     * Gets the value of the debug property.
     * 
     * @return
     *     possible object is
     *     {@link DebugType }
     *     
     */
    public DebugType getDebug() {
        return debug;
    }

    /**
     * Sets the value of the debug property.
     * 
     * @param value
     *     allowed object is
     *     {@link DebugType }
     *     
     */
    public void setDebug(DebugType value) {
        this.debug = value;
    }

}
