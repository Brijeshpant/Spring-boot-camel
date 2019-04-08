package com.brij;

import com.brijeshpant.soap.gen.CreateEmployeeRequest;
import com.brijeshpant.soap.gen.CreateEmployeeResponse;
import com.brijeshpant.soap.gen.Employee;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;

@Component
public class PersonRoute extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json);

        rest("/person")
             .post("/").description("create person").type(Person.class)
                .param().name("body").type(body).description("The user to update").endParam()
                .to("direct:decide");


        from("direct:soap")
                .process(exchange -> {
                    Person p = exchange.getIn().getBody(Person.class);
                    Employee dto = PersonMapper.INSTANCE.personToPersonDTO(p);
                    CreateEmployeeRequest request=  new CreateEmployeeRequest();
                    request.setEmployee(dto);
                    exchange.getOut().setBody(request);
                })
                .setHeader(CxfConstants.OPERATION_NAME,constant("createEmployee"))
                .to("cxf:bean:cxfEmployeeService?synchronous=true")
                .process(exchange -> {
                    CreateEmployeeResponse p = exchange.getIn().getBody(CreateEmployeeResponse.class);
                    exchange.getOut().setBody(p);
                });

        from("direct:decide").routeId("decide-routes")

                .log("Making route decisions")
                .choice()
                    .when(exchange -> {
                        Person p = exchange.getIn().getBody(Person.class);
                        return p.getId() == 0;
                    })
                        .log(LoggingLevel.DEBUG,"Calling soap service", body().toString())
                        .to("direct:soap")
                    .otherwise()
                        .log(LoggingLevel.DEBUG,"No call to any service", body().toString())
                        .process(exchange -> {
                            exchange.getOut().setBody("No call to any service");
                        })
                .end();
    }
}
