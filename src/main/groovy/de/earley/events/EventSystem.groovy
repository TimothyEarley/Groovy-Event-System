package de.earley.events

/**
 * Created 23/04/16
 * @author Timothy Earley
 */
class EventSystem {

	private List<Subject> subjects = []

	Subject createSubject(name) {
		def subject = new Subject()
		subjects << subject
		subject
	}

	def process() {
		subjects.each {
			it.process()
		}
	}

}
