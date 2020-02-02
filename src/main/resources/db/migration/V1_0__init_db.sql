CREATE TABLE Address (
  id bigint(20) NOT NULL,
  city varchar(255) DEFAULT NULL,
  country varchar(255) DEFAULT NULL,
  line1 varchar(255) DEFAULT NULL,
  line2 varchar(255) DEFAULT NULL,
  postcode varchar(255) DEFAULT NULL,
  state varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE WooComCustomer (
  id bigint(20) NOT NULL,
  email varchar(255) DEFAULT NULL,
  firstName varchar(255) DEFAULT NULL,
  lastName varchar(255) DEFAULT NULL,
  userName varchar(255) DEFAULT NULL,
  billingAddress_id bigint(20) DEFAULT NULL,
  shippingAddress_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKegkj5khuvwa26vv7ust4d80sa (billingAddress_id),
  KEY FKljja3v2j628gec52h1x8sa610 (shippingAddress_id),
  CONSTRAINT FKegkj5khuvwa26vv7ust4d80sa FOREIGN KEY (billingAddress_id) REFERENCES Address (id),
  CONSTRAINT FKljja3v2j628gec52h1x8sa610 FOREIGN KEY (shippingAddress_id) REFERENCES Address (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
