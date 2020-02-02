package io.posapps.woocom.repository

import io.posapps.woocom.model.woocom.WooComCustomer
import org.springframework.data.repository.CrudRepository

interface WooComCustomerRepository extends CrudRepository<WooComCustomer, Long> {
  WooComCustomer findById(Long id)
  WooComCustomer findByEmail(String email)
}
