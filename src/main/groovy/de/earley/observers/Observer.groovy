package de.earley.observers

import groovy.transform.*
import de.earley.events.Event

@CompileStatic
@TypeChecked
interface Observer<T extends Event> {

	void observe(T payload)

}
