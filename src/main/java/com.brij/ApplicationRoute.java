package com.brij;

import com.brijeshpant.soap.gen.CreateEmployeeRequest;
import com.brijeshpant.soap.gen.CreateEmployeeResponse;
import com.brijeshpant.soap.gen.Employee;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;

@Component("restbuilder")
public class ApplicationRoute extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json);
        onException(ValidationException.class)
                .handled(true)
                .log("exception")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(401))
                .bean("restbuilder","errorResponse(4000,'Invalid Header')")
                 .process(exchange -> {
                         exchange.getOut().setBody(new ApiResponse(2,"Authentication failure"));
        });


        rest("/transactions")
                .post("/").description("create transaction").type(Transaction.class)
                .param().name("body").type(body).description("The transaction to update").endParam()
                .responseMessage().code(401).responseModel(ApiResponse.class).message("Unexpected body").endResponseMessage() //Wrong input
                .to("direct:validate");

        rest("/employees")
                .get("/").description("create transaction")
                .to("direct:restcall");

        from("direct:restcall")
                .removeHeader("CamelHttp*")
//                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("http://dummy.restapiexample.com/api/v1/?bridgeEndpoint=true")
                .process(exchange -> {
                    Object obj = exchange.getIn();
                    System.out.println(obj);
                })
              .log("responded");

        from("direct:validate")
                 .process(new HeaderProcessor())
//                .validate(header("auth").isNotNull())
                .log(body().toString())
                .log(LoggingLevel.INFO, "validating request object")
                .to("direct:findRoute")
 ;


        from("direct:findRoute").routeId("decide-routes")
                .log("Making route decisions")
                .bean(RouteFinder.class, "findRoute")
                .recipientList().exchangeProperty("route");


        from("direct:makeRequest1")
                .log("Calling 1 soap service")
                .process(exchange -> {
                    CreateEmployeeRequest request = transformRequest(exchange);
                    exchange.getOut().setBody(request);
                })
                .setHeader(CxfConstants.OPERATION_NAME, constant("createEmployee"))
                .to("cxf:bean:cxfEmployeeService?synchronous=true")
                .process(exchange -> {
                    CreateEmployeeResponse p = exchange.getIn().getBody(CreateEmployeeResponse.class);
                    exchange.getOut().setBody(p);
                });

        from("direct:makeRequest2")
                .log("Calling request2 soap service")
                .process(exchange -> {
                    CreateEmployeeRequest request = transformRequest(exchange);
                    exchange.getOut().setBody(request);
                })
                .setHeader(CxfConstants.OPERATION_NAME, constant("createEmployee"))
                .to("cxf:bean:cxfEmployeeService?synchronous=true")
                .process(exchange -> {
                    CreateEmployeeResponse p = exchange.getIn().getBody(CreateEmployeeResponse.class);
                    exchange.getOut().setBody(p);
                });

        from("direct:makeRequest3")
                .log("Calling 3 soap service")
                .process(exchange -> {
                    CreateEmployeeRequest request = transformRequest(exchange);
                    exchange.getOut().setBody(request);
                })
                .setHeader(CxfConstants.OPERATION_NAME, constant("createEmployee"))
                .to("cxf:bean:cxfEmployeeService?synchronous=true")
                .process(exchange -> {
                    CreateEmployeeResponse p = exchange.getIn().getBody(CreateEmployeeResponse.class);
                    exchange.getOut().setBody(p);
                });
    }

    private CreateEmployeeRequest transformRequest(Exchange exchange) {
        Transaction p = exchange.getIn().getBody(Transaction.class);
        Employee dto = PersonMapper.INSTANCE.personToPersonDTO(p);
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmployee(dto);
        return request;
    }
    public static ApiResponse errorResponse(int code, String message){
        return new ApiResponse(code, message);
    }

}
