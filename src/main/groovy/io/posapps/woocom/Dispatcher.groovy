package io.posapps.woocom

import groovy.json.JsonOutput
import groovy.util.logging.Log4j
import io.posapps.woocom.model.posapps.Request
import io.posapps.woocom.model.posapps.Response
import io.posapps.woocom.endpoint.Endpoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Log4j
@Service
class Dispatcher {

  @Lazy
  @Autowired
  List<Endpoint> endpoints

  Response dispatch(Request request) {
    def endpoint = endpoints.find { it.route(request) }
    if (!endpoint) {
      log.info("No Endpoint found using the following rquest : ${JsonOutput.toJson(request)}")
      return Response.builder().statusCode(500).body('"error": "No endpoint for this request"').build()
    }

    log.info("Endpoint found: ${endpoint.class}")
    final def response = endpoint.respond(request)
    response.addHeader('X-Request-Id', request?.requestId())
    return response
  }

}
