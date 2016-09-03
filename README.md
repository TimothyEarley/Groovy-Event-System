# Groovy-Event-System

[![Build Status](https://travis-ci.org/TimothyEarley/Groovy-Event-System.svg?branch=master)](https://travis-ci.org/TimothyEarley/Groovy-Event-System)

Provides central event handling with handler chains.

### Example:

```groovy

// define an event

@Canonical
class DamageEvent implements Event {

	Map target
	int amount

	static {
		Handler.get(DamageEvent).with {

			// damage reduction
			transformer {
				it.amount -= it.target.armour
				if (it.amount < 0) it.amount = 0
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

```

This can then be used as follws

```groovy

	def target = [health: 10, armour: 5]

	Handler.fire new DamageEvent(target: target, amount: 10)
	Handler.doTasks()

	assert target.health == 5
}

```

### How this works

Each event type is associated with its own handle in the Handler class. On firing an event it is first stored in a task list to be later executed. This allows for having an event loop.
The handler then applies all transformers to the event, notifies the observers and finally lets the consumer handle it.

### TODO
- implement command pattern to add undo and replay functionality
