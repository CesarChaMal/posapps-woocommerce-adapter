package io.posapps.woocom.auth

import com.google.gson.Gson
import io.posapps.woocom.model.posapps.SystemUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class Auth {

  static final WOO_COMMERCE = 'WooCommerce'
  static final DOMAIN = 'domain'

  @Value('${posapps.api.auth.endpoint}')
  String apiAuthEndpoint

  @Value('${posapps.auth.endpoint}')
  String authEndpoint

  @Value('${admin.user}')
  String adminUser

  @Value('${admin.password}')
  String adminPassword

  @Autowired
  RestTemplate restTemplate

  SystemUser authenticateByDomain(String domain) {
    def systemUser = getSystemUserByApi(buildBasicAuth(adminUser, adminPassword), domain)
    return subscriptionActive(systemUser) ? systemUser : null
  }

  SystemUser authenticateUser(String credentials) {
    def systemUser = getSystemUser(credentials)
    return subscriptionActive(systemUser) ? systemUser : null
  }

  String buildBasicAuth(String userName, String password) {
    return 'Basic ' + Base64.getEncoder().encodeToString((userName + ':' + password).getBytes())
  }

  private boolean subscriptionActive(SystemUser systemUser) {
    return systemUser.subscriptions?.name?.flatten()?.contains(WOO_COMMERCE)
  }

  private SystemUser getSystemUserByApi(String credentials, String domain) {
    HttpHeaders headers = new HttpHeaders()
    headers.add('Authorization', credentials)
    HttpEntity<String> request = new HttpEntity<String>(headers)
    ResponseEntity<String> response = restTemplate.exchange("$apiAuthEndpoint?domain=$domain", HttpMethod.GET, request, String)
    return new Gson().fromJson(response.body as String, SystemUser)
  }

  private SystemUser getSystemUser(String credentials) {
    HttpHeaders headers = new HttpHeaders()
    headers.add('Authorization', credentials)
    HttpEntity<String> request = new HttpEntity<String>(headers)
    ResponseEntity<String> response = restTemplate.exchange(authEndpoint, HttpMethod.GET, request, String)
    return new Gson().fromJson(response.body as String, SystemUser)
  }
}
