package de.earley

import de.earley.events.Event
import de.earley.handlers.*

class Main {

   class Common {
      static Transformer printer = {
         println it
         it
      }
   }

   //------------------------------

   class Damage {
      Map sender
      Map target
      int amount

      String toString() {
         "Damage: target: $target, $amount, sender: $sender"
      }

      static {
         Handler.get(Damage).with {
            transformers << Common.printer
            transformers << {
               if (it.target.props.armour) {
                  it.amount -= it.target.props.armour
                  if (it.amount < 0) it.amount = 0
                  println 'armour applied'
               }
               it
            }
            transformers << {
               Handler.fire new ArmourBoost(target: it.target, amount: 1)
               it
            }
            consumer = {
               it.target.props.health -= it.amount
               println "done with: $it"
            }
         }
      }
   }

   //------------------------------

   class ArmourBoost {
      Map target
      int amount

      String toString() {
         "armour boost: $target, $amount"
      }

      static {
         Handler.get(ArmourBoost).with {
            transformers << Common.printer
            consumer = {
               if (!it.target.props.armour) it.target.props.armour = 0
               it.target.props.armour += it.amount
               println "done with $it"
            }
         }
      }
   }

   //------------------------------

   static void run() {

      // can be dynamicalybloaded, but not on Android
      Handler.get(Damage).with {
         transformers << {
            if (it.sender && it.target.props.thorns) Handler.fire new Damage(target: it.sender, amount: it.amount / 2, sender: it.target)
            it
         }
      }

      def e = [name: 'e', health: 10, armour: 3]
      def f = [name: 'f', health: 10, thorns: true]

      Handler.fire new Damage(target: e, amount: 6, sender: f)

      Handler.fire new Damage(target: f, amount: 10, sender: e)

      Handler.doAll()

      Handler.fire new Damage(target: e, amount: 6)

      Handler.doAll()

      println 'done'
   }
}
