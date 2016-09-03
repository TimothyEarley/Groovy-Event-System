package de.earley.handlers

import groovy.transform.*

@CompileStatic
@TypeChecked
interface Transformer<T> {
    T handle(T payload)
}
