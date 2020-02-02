package io.posapps.woocom.service

import groovy.json.JsonOutput
import groovy.util.logging.Log4j
import io.posapps.woocom.auth.Auth
import io.posapps.woocom.model.woocom.WooComCustomer
import io.posapps.woocom.repository.WooComCustomerRepository
import io.posapps.woocom.utils.Translator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Log4j
@Service
class CustomerService {

  private static final DEVICE = 'device'
  private static final EMAIL = 'email'
  private static final WOO_COMMERCE = 'WooCommerce'

  @Value('${posapps.customer.endpoint}')
  String customerEndpoint

  @Lazy
  @Autowired
  RestTemplate restTemplate

  @Lazy
  @Autowired
  Translator translator

  @Lazy
  @Autowired
  Auth auth

  @Lazy
  @Autowired
  WooComCustomerRepository wooComCustomerRepository

  void createCustomer(WooComCustomer wooComCustomer, String domain) {
    def response = null
    def systemUser = auth.authenticateByDomain(domain)

    if (systemUser) {
      wooComCustomerRepository.save(wooComCustomer)

      HttpHeaders headers = new HttpHeaders()
      headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(systemUser.emailAddress, systemUser.password))
      HttpEntity<String> request = new HttpEntity<String>(JsonOutput.toJson(translator.getCustomer(wooComCustomer)), headers)

      log.info("Create customer RQ url: $customerEndpoint?$DEVICE=$WOO_COMMERCE request: ${JsonOutput.toJson(request)}")
      try {
        response = restTemplate.exchange("$customerEndpoint?$DEVICE=$WOO_COMMERCE", HttpMethod.POST, request, String)
      }
      catch (Exception ex) {
        log.error("Error with create request to customer api: $ex")
      }
      log.info("Create customer RS : ${JsonOutput.toJson(response)}")
    }
    else {
      log.info("No User found for domain : $domain")
    }

  }

  void updateCustomer(WooComCustomer wooComCustomer, String domain) {
    def systemUser = auth.authenticateByDomain(domain)

    wooComCustomerRepository.save(wooComCustomer)

    if (systemUser) {
      try {
        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(systemUser.emailAddress, systemUser.password))
        HttpEntity<String> request = new HttpEntity<String>(JsonOutput.toJson(translator.getCustomer(wooComCustomer)), headers)

        log.info("Update customer RQ url: $customerEndpoint?$DEVICE=$WOO_COMMERCE request: ${JsonOutput.toJson(request)}")
        def response = restTemplate.exchange("$customerEndpoint?$DEVICE=$WOO_COMMERCE", HttpMethod.PUT, request, String)
        log.info("Update customer RS : ${JsonOutput.toJson(response)}")
      }
      catch (Exception ex) {
        log.error("Error with update request to customer api: ${JsonOutput.toJson(ex)}")
      }
    }
  }

  void deleteCustomer(WooComCustomer wooComCustomer, String domain) {
    def wooCustomer = wooComCustomerRepository.findById(wooComCustomer.id)
    def systemUser = auth.authenticateByDomain(domain)

    wooComCustomerRepository.delete(wooComCustomer)

    if (systemUser && wooCustomer) {
      try {HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(systemUser.emailAddress, systemUser.password))
        HttpEntity<String> request = new HttpEntity<String>(JsonOutput.toJson(translator.getCustomer(wooComCustomer)), headers)

        log.info("Delete customer RQ url: $customerEndpoint?$DEVICE=$WOO_COMMERCE&$EMAIL=${wooCustomer?.email} request: ${JsonOutput.toJson(request)}")
        def response = restTemplate.exchange(
                "$customerEndpoint?$DEVICE=$WOO_COMMERCE&$EMAIL=${wooCustomer?.email}", HttpMethod.DELETE, request, String)
        log.info("Delete customer RS : ${JsonOutput.toJson(response)}")
      }
      catch (Exception ex) {
        log.error("Error with delete request to customer api: ${JsonOutput.toJson(ex)}")
      }
    }

  }
}
