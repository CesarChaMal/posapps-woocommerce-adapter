package io.posapps.woocom.endpoint.woocom

import com.google.gson.Gson
import groovy.util.logging.Log4j
import io.posapps.woocom.endpoint.Endpoint
import io.posapps.woocom.model.posapps.Request
import io.posapps.woocom.model.posapps.Response
import io.posapps.woocom.model.woocom.WooComCustomer
import io.posapps.woocom.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Log4j
@Component
class WooComCustomerEndpoint extends Endpoint {

  final CREATED = 'created'
  final DELETED = 'deleted'
  final UPDATED = 'updated'

  @Lazy
  @Autowired
  CustomerService customerService

  @Override
  boolean route(Request request) {
    return request.resourcePath() == '/woocustomer' && request.httpMethod() == POST
  }

  @Override
  Response respond(Request request) {
    def source = request.headers().get('X-WC-Webhook-Source')
    def domain = getDomainName(source)
    def event = request.headers().get('X-WC-Webhook-Event')
    def wooComCustomer = deserializeWooComCustomer(request.body())

    if (event == CREATED) customerService.createCustomer(wooComCustomer, domain)
    if (event == DELETED) customerService.deleteCustomer(wooComCustomer, domain)
    if (event == UPDATED) customerService.updateCustomer(wooComCustomer, domain)

    return Response.builder()
            .statusCode(200)
            .body('{"message":"OK"}')
            .build()
  }

  private getDomainName(String source) {
    def domainSections = source?.split('www.')
    if (domainSections) {
      domainSections.size() > 1 ? domainSections[1] : source?.split('://')[1]
    }
  }

  private WooComCustomer deserializeWooComCustomer(String json) {
    try {
      new Gson().fromJson(json, WooComCustomer)
    }
    catch (Exception ex) {
      log.debug("Not able to deserialize json to customer: $ex")
      return null
    }

  }
}
