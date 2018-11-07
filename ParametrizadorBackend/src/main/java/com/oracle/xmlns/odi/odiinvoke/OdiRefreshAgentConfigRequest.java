
package com.oracle.xmlns.odi.odiinvoke;

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
 *         &lt;element name="Credentials" type="{xmlns.oracle.com/odi/OdiInvoke/}OdiMasterCredentialType"/>
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
@XmlRootElement(name = "OdiRefreshAgentConfigRequest")
public class OdiRefreshAgentConfigRequest {

    @XmlElement(name = "Credentials", required = true)
    protected OdiMasterCredentialType credentials;

    /**
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link OdiMasterCredentialType }
     *     
     */
    public OdiMasterCredentialType getCredentials() {
        return credentials;
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link OdiMasterCredentialType }
     *     
     */
    public void setCredentials(OdiMasterCredentialType value) {
        this.credentials = value;
    }

}
