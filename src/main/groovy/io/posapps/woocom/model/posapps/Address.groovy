package io.posapps.woocom.model.posapps

import groovy.transform.EqualsAndHashCode
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

import javax.persistence.*
import java.sql.Timestamp

@EqualsAndHashCode
class Address {
  Long id
  Timestamp created
  Timestamp updated
  Integer version
  String name
  String line1
  String line2
  String city
  String postCode
}
