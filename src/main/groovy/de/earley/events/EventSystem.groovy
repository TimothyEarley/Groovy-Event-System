package de.earley.events

/**
 *
 * Holds a List of Subject that can be processed with one call
 *
 * Created 23/04/16
 * @author Timothy Earley
 */
class EventSystem {

	private List<Subject> subjects = []

	def addSubject(Subject subject) {
		subjects << subject
	}

	Subject createSubject() {
		def s = new Subject()
		addSubject(s)
		s
	}

	def process() {
		subjects.each {
			it.process()
		}
	}

}
