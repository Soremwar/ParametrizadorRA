
package co.com.claro.service.soap.odi.consumer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OdiMasterCredentialType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OdiMasterCredentialType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="OdiUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OdiPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OdiMasterCredentialType", propOrder = {

})
public class OdiMasterCredentialType {

    @XmlElement(name = "OdiUser")
    protected String odiUser;
    @XmlElement(name = "OdiPassword")
    protected String odiPassword;

    /**
     * Gets the value of the odiUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdiUser() {
        return odiUser;
    }

    /**
     * Sets the value of the odiUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdiUser(String value) {
        this.odiUser = value;
    }

    /**
     * Gets the value of the odiPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdiPassword() {
        return odiPassword;
    }

    /**
     * Sets the value of the odiPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdiPassword(String value) {
        this.odiPassword = value;
    }

}
