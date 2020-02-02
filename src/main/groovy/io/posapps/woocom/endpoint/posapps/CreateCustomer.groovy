package io.posapps.woocom.endpoint.posapps

import com.google.gson.Gson
import io.posapps.woocom.auth.Auth
import io.posapps.woocom.endpoint.Endpoint
import io.posapps.woocom.model.posapps.Customer
import io.posapps.woocom.model.posapps.Request
import io.posapps.woocom.model.posapps.Response
import io.posapps.woocom.service.WooCustomerService
import io.posapps.woocom.utils.Translator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CreateCustomer extends Endpoint{

  @Lazy
  @Autowired
  WooCustomerService wooCustomerService

  @Lazy
  @Autowired
  Translator translator

  @Lazy
  @Autowired
  Auth auth

  @Override
  boolean route(Request request) {
    return request.resourcePath() == '/customer' && request.httpMethod() == POST && request.queryString(DOMAIN)
  }

  @Override
  Response respond(Request request) {
    def systemUser = auth.authenticateUser(request?.headers()?.Authorization)

    if (systemUser) {
      def customerJson = request.body()
      def customer = new Gson().fromJson(customerJson, Customer)

      def wooComCustomer = translator.getWooCustomer(customer)
      wooComCustomer.password = 'Test123'
      wooCustomerService.createWooCustomer(wooComCustomer, systemUser.apiConfigurations[0])

      return Response.builder()
              .statusCode(HttpStatus.OK.value())
              .body('{"message":"OK"}')
              .build()
    }

    return Response.builder()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body('{"message":"Not authorized"}')
            .build()
  }
}
