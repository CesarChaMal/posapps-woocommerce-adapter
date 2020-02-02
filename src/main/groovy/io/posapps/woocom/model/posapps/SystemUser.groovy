package io.posapps.woocom.model.posapps

class SystemUser {
  Long id
  List<Subscription> subscriptions
  String emailAddress
  String password
  List<ApiConfiguration> apiConfigurations
}
