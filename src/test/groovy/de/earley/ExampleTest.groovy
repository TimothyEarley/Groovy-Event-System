package de.earley

import spock.lang.Specification

import groovy.transform.*

import de.earley.handlers.*
import de.earley.events.Event
import de.earley.observers.Observer

class HExampleTest extends Specification {

	@Canonical
	class DamageEvent implements Event {

		Map target
		int amount

		static {
			Handler.get(DamageEvent).with {

				// damage reduction
				transformer {
					if (it.target.armour) {
						it.amount -= it.target.armour
						if (it.amount < 0) it.amount = 0
					}
					return it
				}

				// print the damage taking place
				observer {
					println it
				}

				// apply the damage
				consumer {
					if (it.target.health) it.target.health -= it.amount
				}
			}
		}
	}

	def "example"() {

		given: "a target"
		def target = [health: 10, armour: 5]

		when: "damage is dealt"
		Handler.fire new DamageEvent(target, 10)
		Handler.doTasks()

		then: "the damage was taken"
		target.health == 5
	}
}
