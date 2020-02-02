package io.posapps.woocom.model.woocom

import com.google.gson.annotations.SerializedName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Entity
import javax.persistence.Id

@ToString
@EqualsAndHashCode
@Entity
class Address {
  @Id
  Long id
  @SerializedName('address_1')
  String line1
  @SerializedName('address_2')
  String line2
  String city
  String state
  String postcode
  String country
}
