package de.earley.events

/**
 *
 * An Observable thing
 *
 * Created 23/04/16
 * @author Timothy Earley
 */
class Subject {

	/**
	 * Each listener gets a list of broadcasts (any type)
	 */
	final List<Closure> listeners = []

	private broadcasts = []

	def allowDuplicates = true

	def broadcast(msg) {
		if (!allowDuplicates && broadcasts.contains(msg)) return
		broadcasts << msg
	}

	def process() {
		if (broadcasts.empty) return
		// clear the broadcasts beforehand, so that any newly added messages are not included in the current process
		final temp = broadcasts
		broadcasts = []
		listeners.each {
			it(temp)
		}
	}
}
