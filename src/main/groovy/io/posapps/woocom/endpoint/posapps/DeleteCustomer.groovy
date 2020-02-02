package io.posapps.woocom.endpoint.posapps

import io.posapps.woocom.auth.Auth
import io.posapps.woocom.endpoint.Endpoint
import io.posapps.woocom.model.posapps.Request
import io.posapps.woocom.model.posapps.Response
import io.posapps.woocom.service.WooCustomerService
import io.posapps.woocom.utils.Translator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class DeleteCustomer extends Endpoint{

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
    return request.resourcePath() == '/customer' && request.httpMethod() == DELETE && request.queryString(EMAIL) && request.queryString(DOMAIN)
  }

  @Override
  Response respond(Request request) {
    def systemUser = auth.authenticateUser(request?.headers()?.Authorization)

    if (systemUser) {
      wooCustomerService.deleteWooCustomer(request.queryString(EMAIL), systemUser.apiConfigurations[0])

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
