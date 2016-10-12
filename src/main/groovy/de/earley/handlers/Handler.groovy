package de.earley.handlers

import groovy.transform.*
import de.earley.events.Event
import de.earley.observers.Observer

@CompileStatic
@TypeChecked
class Handler<T extends Event> {

   static Map<Class, Handler> handlers = [:]

   static List<Event> tasks = []

   static <S extends Event>  Handler<S> get(Class<S> type) {
      def p = handlers[type]
      if (!p) {
         p = new Handler<S>()
         handlers[type] = p
      }
      return p
   }

   static void fire(Event task) {
      tasks << task
   }

   /*
   * returns true if tasks have been closed
   */
   static boolean doTasks() {

      if (!tasks) return false

      // allow for adding tasks during loop
      def doNow = tasks
      tasks = []

      doNow.each {
         get(it.class).handle it
      }

      return true
   }

   private List<Transformer<T>> transformers = []
   private List<Observer<T>> observers = []
   private Consumer<T> consumer

   void transformer (Transformer<T> transformer) {
      transformers << transformer
   }

   void observer (Observer<T> observer) {
      observers << observer
   }

   void consumer (Consumer<T> consumer) {
      this.consumer = consumer
   }

   private void handle(T payload) {

      payload = transformers.inject (payload) {
            p, Transformer i -> i.handle p
      }

      // can be parallel
      observers.each {
         it.observe payload
      }

      consumer?.handle payload
   }

}
