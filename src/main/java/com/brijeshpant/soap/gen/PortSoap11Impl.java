
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.brijeshpant.soap.gen;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.16
 * 2019-04-04T00:11:23.989+05:30
 * Generated source version: 3.1.16
 * 
 */

@javax.jws.WebService(
                      serviceName = "portService",
                      portName = "portSoap11",
                      targetNamespace = "http://brijeshpant.com/soap/gen",
                      wsdlLocation = "file:/Users/bpant/work/poc/spring-boot-camel/src/main/resources/wsdl/employees.wsdl",
                      endpointInterface = "com.brijeshpant.soap.gen.Port")
                      
public class PortSoap11Impl implements Port {

    private static final Logger LOG = Logger.getLogger(PortSoap11Impl.class.getName());

    /* (non-Javadoc)
     * @see com.brijeshpant.soap.gen.Port#getEmployee(com.brijeshpant.soap.gen.GetEmployeeRequest getEmployeeRequest)*
     */
    public com.brijeshpant.soap.gen.GetEmployeeResponse getEmployee(GetEmployeeRequest getEmployeeRequest) { 
        LOG.info("Executing operation getEmployee");
        System.out.println(getEmployeeRequest);
        try {
            com.brijeshpant.soap.gen.GetEmployeeResponse _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see com.brijeshpant.soap.gen.Port#createEmployee(com.brijeshpant.soap.gen.CreateEmployeeRequest createEmployeeRequest)*
     */
    public com.brijeshpant.soap.gen.CreateEmployeeResponse createEmployee(CreateEmployeeRequest createEmployeeRequest) { 
        LOG.info("Executing operation createEmployee");
        System.out.println(createEmployeeRequest);
        try {
            com.brijeshpant.soap.gen.CreateEmployeeResponse _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
