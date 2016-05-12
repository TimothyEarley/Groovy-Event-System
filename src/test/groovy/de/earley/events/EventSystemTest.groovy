package de.earley.events

import spock.lang.Specification

/**
 * Created 12/05/16
 * @author Timothy Earley
 */
class EventSystemTest extends Specification {


	def  "create subject"() {
		given: "a system"
		def eventSystem = new EventSystem()

		when: "a subject is added"
		def subject = eventSystem.createSubject()

		then: "the subject got created"
		subject
	}

	def "process"() {
		given: "a system and subject with a broadcast and listener"
		def system = new EventSystem()
		def subject = system.createSubject()
		subject.broadcast("Test")
		def done = false
		subject.listeners << { done = true}

		when: "the system gets processed, subject sends the message"
		system.process()

		then: "the message should have been received"
		done
	}

}
