package io.posapps.woocom.model.posapps

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Customer {
  Long id
  Integer version
  Long systemUserId
  String firstName
  String lastName
  String emailAddress
  String telephone
  String mobile
  String userName
  Address address
  Set<Device> devices
}
