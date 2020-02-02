package io.posapps.woocom.utils

import io.posapps.woocom.model.posapps.Address
import io.posapps.woocom.model.posapps.Customer
import io.posapps.woocom.model.woocom.WooComCustomer
import org.springframework.stereotype.Component

@Component
class Translator {

  Customer getCustomer(WooComCustomer wooComCustomer) {
    return new Customer(
            firstName: wooComCustomer?.firstName,
            lastName: wooComCustomer?.lastName,
            emailAddress: wooComCustomer?.email,
            telephone: wooComCustomer?.phone,
            address: getAddress(wooComCustomer?.billingAddress) ?: getAddress(wooComCustomer?.shippingAddress),
            userName: wooComCustomer?.userName
    )
  }

  WooComCustomer getWooCustomer(Customer customer) {
    return new WooComCustomer(
            firstName: customer?.firstName,
            lastName: customer?.lastName,
            email: customer?.emailAddress)
  }

  Address getAddress(io.posapps.woocom.model.woocom.Address wooAddress) {
    return new Address(
            line1: wooAddress?.line1,
            line2: wooAddress?.line2,
            city: wooAddress?.city,
            postCode: wooAddress?.postcode
    )
  }

  io.posapps.woocom.model.woocom.Address getWooAddress(Address address) {
    return new io.posapps.woocom.model.woocom.Address(
            line1: address?.line1,
            line2: address?.line2,
            city: address?.city,
            postcode: address?.postCode
    )
  }
}
