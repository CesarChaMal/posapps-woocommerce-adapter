package io.posapps.woocom

import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import io.posapps.woocom.model.posapps.ApiConfiguration
import io.posapps.woocom.model.posapps.Customer
import io.posapps.woocom.model.posapps.Status
import io.posapps.woocom.model.posapps.Subscription
import io.posapps.woocom.model.posapps.SystemUser
import io.posapps.woocom.model.woocom.WooComCustomer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.springframework.http.HttpStatus

import static com.github.tomakehurst.wiremock.client.WireMock.*

class ApplicationTest {

  def application

  @Rule
  public WireMockRule wireMockRule = new WireMockRule()

  @Before
  void setup() {
    application = new Application()
    initWireMock()
  }

  @Test
  void "should confirm web-hook added"() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/woocom-webhook-added.json').text) as Map
    def response = application.handleRequest(input, null)
    assert response.statusCode == 200
  }

  @Test
  void "should get the status response"() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/get-status-input.json').text) as Map
    def response = application.handleRequest(input, null)
    assert response.statusCode == 200
    assert new JsonSlurper().parseText(response.body) as Status
  }

  @Test
  void "create, update and delete a customer issued by the posapps api"() {
    /*
    Create the customer first
     */
    def input = new JsonSlurper().parseText(getClass().getResource('/input/posapps-create-customer.json').text) as Map
    def response = application.handleRequest(input, null)
    assert response.statusCode == 200

    /*
    Update the customer
     */
    def updateInput = new JsonSlurper().parseText(getClass().getResource('/input/posapps-update-customer.json').text) as Map
    def updateResponse = application.handleRequest(updateInput, null)
    assert updateResponse.statusCode == 200

    /*
    Delete the customer
     */
    def deleteInput = new JsonSlurper().parseText(getClass().getResource('/input/posapps-delete-customer.json').text) as Map
    def deleteResponse = application.handleRequest(deleteInput, null)
    assert deleteResponse.statusCode == 200

  }

  @Test
  void "create a customer issued by the woocom api"() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/woocom-create-customer.json').text) as Map
    def response = application.handleRequest(input, null)
    assert response.statusCode == 200
  }

  @Test
  void "delete a customer issued by the woocom api"() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/woocom-delete-customer.json').text) as Map
    def response = application.handleRequest(input, null)
    assert response.statusCode == 200
  }

  @Test
  void "update a customer issued by the woocom api"() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/woocom-update-customer.json').text) as Map
    def response = application.handleRequest(input, null)
    assert response.statusCode == 200
  }

  private static void initWireMock() {
    /*
    posapps mock endpoints
     */
    stubFor(get(urlPathEqualTo('/admin/user'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody(JsonOutput.toJson(
            new SystemUser(
                    apiConfigurations: [new ApiConfiguration(apiEndPoint: 'http://localhost:8080/woocom')],
                    subscriptions: [new Subscription(name: 'WooCommerce')])))))

    stubFor(get(urlPathEqualTo('/admin/authenticate'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody(JsonOutput.toJson(
            new SystemUser(
                    apiConfigurations: [new ApiConfiguration(apiEndPoint: 'http://localhost:8080/woocom')],
                    subscriptions: [new Subscription(name: 'WooCommerce')])))))

    stubFor(post(urlPathEqualTo('/customer'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody(JsonOutput.toJson(new Customer()))))

    stubFor(put(urlPathEqualTo('/customer'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody(JsonOutput.toJson(new Customer()))))

    stubFor(delete(urlPathEqualTo('/customer'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody()))

    /*
    WooCommerce mock endpoints
     */
    stubFor(post(urlPathEqualTo('/woocom/customers'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.CREATED.value())
            .withBody(JsonOutput.toJson(new WooComCustomer(
            id: 1, firstName: 'Hugh', lastName: 'Stevenson', email: 'hugh.net@mac.com')))))

    stubFor(put(urlPathEqualTo('/woocom/customers/1'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody(JsonOutput.toJson(new WooComCustomer(
            id: 1, firstName: 'Hue', lastName: 'Stephenson', email: 'hugh.net@mac.com')))))

    stubFor(delete(urlPathEqualTo('/woocom/customers/1'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody(JsonOutput.toJson(new WooComCustomer()))))

  }
}
