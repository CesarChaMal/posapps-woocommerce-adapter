package io.posapps.woocom.service

import com.google.gson.Gson
import groovy.json.JsonOutput
import groovy.util.logging.Log4j
import io.posapps.woocom.auth.Auth
import io.posapps.woocom.model.posapps.ApiConfiguration
import io.posapps.woocom.model.woocom.WooComCustomer
import io.posapps.woocom.repository.WooComCustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Log4j
@Component
class WooCustomerService {

  @Lazy
  @Autowired
  RestTemplate restTemplate

  @Lazy
  @Autowired
  Auth auth

  @Lazy
  @Autowired
  WooComCustomerRepository wooComCustomerRepository

  void createWooCustomer(WooComCustomer wooComCustomer, ApiConfiguration apiConfiguration) {

    def existingWooCustomer = wooComCustomerRepository.findByEmail(wooComCustomer.email)

    if (!existingWooCustomer) {
      try {
        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(apiConfiguration.apiKey, apiConfiguration.apiSecret))
        headers.add('Content-Type', 'application/json')
        HttpEntity<String> request = new HttpEntity<String>(new Gson().toJson(wooComCustomer), headers)

        log.info("Create woocom customer url: ${apiConfiguration.apiEndPoint}/customers request: ${JsonOutput.toJson(request)}")
        def response = restTemplate.exchange(
                "${apiConfiguration.apiEndPoint}/customers", HttpMethod.POST, request, String)
        log.info("Create woocom customer response: ${JsonOutput.toJson(response)}")
      }
      catch (Exception ex) {
        log.error("Error with create request to WooCommerce API: $ex")
      }
    }
  }

  void updateWooCustomer(WooComCustomer wooComCustomer, ApiConfiguration apiConfiguration) {

    def existingWooCustomer = wooComCustomerRepository.findByEmail(wooComCustomer.email)

    if (existingWooCustomer) {
      try {
        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(apiConfiguration.apiKey, apiConfiguration.apiSecret))
        headers.add('Content-Type', 'application/json')
        HttpEntity<String> request = new HttpEntity<String>(new Gson().toJson(wooComCustomer), headers)

        log.info("Update woocom customer url:${apiConfiguration.apiEndPoint}/customers/${existingWooCustomer.id} request: ${JsonOutput.toJson(request)}")
        def response = restTemplate.exchange(
                "${apiConfiguration.apiEndPoint}/customers/${existingWooCustomer.id}", HttpMethod.PUT, request, String)
        log.info("Update woocom customer response: ${JsonOutput.toJson(response)}")
      }
      catch (Exception ex) {
        log.error("Error with update request to WooCommerce API: $ex")
      }
    }
  }

  void deleteWooCustomer(String email, ApiConfiguration apiConfiguration) {

    def existingWooCustomer = wooComCustomerRepository.findByEmail(email)

    if (existingWooCustomer) {

      try {
        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(apiConfiguration.apiKey, apiConfiguration.apiSecret))
        headers.add('Content-Type', 'application/json')
        HttpEntity<String> request = new HttpEntity<String>(null, headers)

        log.info("Delete woocom customer url: ${apiConfiguration.apiEndPoint}/customers/${existingWooCustomer.id}?force=true request: ${JsonOutput.toJson(request)}")
        def response = restTemplate.exchange(
                "${apiConfiguration.apiEndPoint}/customers/${existingWooCustomer.id}?force=true", HttpMethod.DELETE, request, String)
        log.info("Delete woocom customer response: ${JsonOutput.toJson(response)}")

      }
      catch (Exception ex) {
        log.error("Error with delete request to WooCommerce API: $ex}")
      }
    }
  }

}
