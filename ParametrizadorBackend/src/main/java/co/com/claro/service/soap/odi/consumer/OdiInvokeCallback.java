
package co.com.claro.service.soap.odi.consumer;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "OdiInvokeCallback", targetNamespace = "xmlns.oracle.com/odi/OdiInvoke/", wsdlLocation = "http://localhost:8088/mockInvokeRequestSOAP11Binding?WSDL")
public class OdiInvokeCallback
    extends Service
{

    private final static URL ODIINVOKECALLBACK_WSDL_LOCATION;
    private final static WebServiceException ODIINVOKECALLBACK_EXCEPTION;
    private final static QName ODIINVOKECALLBACK_QNAME = new QName("xmlns.oracle.com/odi/OdiInvoke/", "OdiInvokeCallback");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8088/mockInvokeRequestSOAP11Binding?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        ODIINVOKECALLBACK_WSDL_LOCATION = url;
        ODIINVOKECALLBACK_EXCEPTION = e;
    }

    public OdiInvokeCallback() {
        super(__getWsdlLocation(), ODIINVOKECALLBACK_QNAME);
    }

    public OdiInvokeCallback(WebServiceFeature... features) {
        super(__getWsdlLocation(), ODIINVOKECALLBACK_QNAME, features);
    }

    public OdiInvokeCallback(URL wsdlLocation) {
        super(wsdlLocation, ODIINVOKECALLBACK_QNAME);
    }

    public OdiInvokeCallback(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, ODIINVOKECALLBACK_QNAME, features);
    }

    public OdiInvokeCallback(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public OdiInvokeCallback(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns RequestPortTypeCallback
     */
    @WebEndpoint(name = "OdiInvokeCallbackSOAP11Port")
    public RequestPortTypeCallback getOdiInvokeCallbackSOAP11Port() {
        return super.getPort(new QName("xmlns.oracle.com/odi/OdiInvoke/", "OdiInvokeCallbackSOAP11Port"), RequestPortTypeCallback.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RequestPortTypeCallback
     */
    @WebEndpoint(name = "OdiInvokeCallbackSOAP11Port")
    public RequestPortTypeCallback getOdiInvokeCallbackSOAP11Port(WebServiceFeature... features) {
        return super.getPort(new QName("xmlns.oracle.com/odi/OdiInvoke/", "OdiInvokeCallbackSOAP11Port"), RequestPortTypeCallback.class, features);
    }

    private static URL __getWsdlLocation() {
        if (ODIINVOKECALLBACK_EXCEPTION!= null) {
            throw ODIINVOKECALLBACK_EXCEPTION;
        }
        return ODIINVOKECALLBACK_WSDL_LOCATION;
    }

}