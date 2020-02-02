package io.posapps.woocom.endpoint

import io.posapps.woocom.model.posapps.Request
import io.posapps.woocom.model.posapps.Response

abstract class Endpoint {
    public static final DEVICE  = 'device'
    public static final EMAIL   = 'email'
    public static final CREATED = 'created'
    public static final DELETED = 'deleted'
    public static final UPDATED = 'updated'
    public static final DOMAIN  = 'domain'

    public static final POST    = 'POST'
    public static final GET     = 'GET'
    public static final DELETE  = 'DELETE'
    public static final PUT     = 'PUT'

    abstract boolean route(Request request)
    abstract Response respond(Request request)
}
