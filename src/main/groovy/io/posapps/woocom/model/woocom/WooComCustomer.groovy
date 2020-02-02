package io.posapps.woocom.model.woocom

import com.google.gson.annotations.SerializedName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Transient

@ToString
@EqualsAndHashCode
@Entity
class WooComCustomer{
  @Id
  Long id
  String email
  @Transient
  String phone
  @Transient
  String password
  @SerializedName("first_name")
  String firstName
  @SerializedName("last_name")
  String lastName
  @SerializedName("username")
  String userName
  @SerializedName("billing_address")
  @OneToOne
  Address billingAddress
  @SerializedName("shipping_address")
  @OneToOne
  Address shippingAddress
}
