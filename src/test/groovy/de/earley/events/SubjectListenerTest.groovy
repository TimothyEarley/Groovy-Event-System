package de.earley.events

import spock.lang.Specification
/**
 * Created 11/05/16
 * @author Timothy Earley
 */
class SubjectListenerTest extends Specification {

	def "add a listener"() {

		given: "a subject"
		def subject = new Subject()

		when: "a listener is added"
		subject.listeners << {}

		then: "the listener is registered"
		subject.listeners.size() == 1

	}

	def "add a message"() {

		given: "a subject, listener and bool"
		def subject = new Subject()
		def done = false
		subject.listeners << {
			done = true
		}

		when: "a message is add"
		subject.broadcast "Hello World"

		then: "the message is in the queue"
		!done
	}

	def "send a message"() {
		given: "a subject, listener and bool"
		def subject = new Subject()
		def done = false
		subject.listeners << {
			done = true
		}

		when: "a message is send"
		subject.broadcast "Hello World"
		subject.process()

		then: "the message is send"
		done

	}

	def "multiple listeners"() {
		given: "A subject and multiple listeners"
		def s = new Subject()
		def count = 0
		def lcount = 10
		lcount.times {
			s.listeners << { count++ }
		}

		when: "a message is sent"
		s.broadcast("test test test")
		s.process()

		then: "all listeners get the message"
		count == lcount
	}

	def "multiple messages"() {
		given: "a subject and listener"
		def subject = new Subject(allowDuplicates: dupes)
		def count = 0
		subject.listeners << { messages -> count += messages.size() }

		when: "all the messages are send"
		sendCount.times {
			subject.broadcast("bla bla")
		}
		subject.process()

		then: "the count matches"
		count == expected

		where:
		dupes   | sendCount || expected
		true    | 10        || 10
		true    | 0         || 0
		false   | 10        || 1
	}

}
