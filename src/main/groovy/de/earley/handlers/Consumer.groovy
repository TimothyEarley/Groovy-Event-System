package de.earley.handlers


import groovy.transform.*
import de.earley.events.Event

/**
* The endpoint of an evnt handling chain
*/
@CompileStatic
@TypeChecked
interface Consumer<T extends Event> {
    void handle(T payload)
}
