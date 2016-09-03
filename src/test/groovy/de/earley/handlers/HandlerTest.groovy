package de.earley

import spock.lang.Specification

import de.earley.handlers.*
import de.earley.events.Event
import de.earley.observers.Observer

class HandlerTest extends Specification {

	def cleanup() {
		Handler.handlers.clear()
	}

	class ExampleEvent implements Event {
		def data = true
	}

	class ExampleTransformer<ExampleEvent> extends Transformer {

		ExampleEvent handle(ExampleEvent payload) {
			payload.data = false
			return payload
		}

	}

	def "type of handler"() {

		given: "a handler of a certain type"
		def handler = Handler.get(type)

		expect: "handler is of type type"
		Handler.handlers.containsKey type

		where:
		type << [Event, ExampleEvent]
	}

	def "adding a consumer"() {
		given: "a consumer and event"
		Consumer c = Mock()
		Event e = Mock()

		when: "added to a handler and called"
		Handler.get(e.class).with {
			consumer c
			fire e
			doTasks()
		}

		then:
		1 * c.handle(e)
	}

	def "adding an observer"() {
		given: "an observer and event"
		Observer o = Mock()
		Event e = Mock()

		when: "added to a handler and called"
		Handler.get(e.class).with {
			observer o
			fire e
			doTasks()
		}

		then:
		1 * o.observe(e)
	}

	def "adding a transformer"() {
		given: "consumer and event"
		Consumer c = Mock()
		def e = new ExampleEvent()

		when: "everything is added and the event is fired"
		Handler.get(ExampleEvent).with {
			transformer new ExampleTransformer()
			consumer c
			fire e
			doTasks()
		}

		then:
		1 * c.handle({ !it.data })
	}

	def "all of the above as closures"() {
		given: "a event and handler chain"
		Event e = Mock()
		def runCounts = [observers: 0, transformers: 0, consumer: 0]
		Handler.get(e.class).with {
			observer {
				runCounts.observers++
			}
			transformer {
				runCounts.transformers++
				it
			}
			consumer {
				runCounts.consumer++
			}
			fire e
			doTasks()
		}

		expect: "all ran once"
		runCounts.every { it.value == 1 }

	}

	def "multiple observers and transformers"() {
		given: "the mocks"
		Event e1 = Mock()
		Event e2 = Mock()
		Transformer t = Mock()
		Consumer c = Mock()
		Observer o = Mock()

		when: "they are added and the event is fired twice"
		Handler.get(e1.class).with {
			observer o
			observer o
			transformer t
			transformer t
			consumer c
			fire e1
			fire e2
			doTasks()
		}

		then: "the methods were called in the correct order"
		2 * t.handle ( e1 ) >> e1
		then:
		2 * o.observe ( e1 )
		then:
		1 * c.handle ( e1 )
		then:
		2 * t.handle ( e2 ) >> e2
		then:
		2 * o.observe ( e2 )
		then:
		1 * c.handle ( e2 )
	}

}
