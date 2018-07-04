package processing

import events.Event
import events.EventListener
import utility.ReflectionTools
import java.lang.reflect.ParameterizedType
import java.util.*


object EventManager {
    private val listenerMap = HashMap<Class<*>, ArrayList<EventListener<*>>>()

    init {
        registerListeners()
    }

    private fun registerListeners() {
        ReflectionTools.getAllEventListeners().map {
            registerListener(it.newInstance())
        }
    }

    fun <E : Event> registerListener(listener: EventListener<E>) {
//        println("Registering $listener")
        val listenerClass = getListenedForClass(listener)
        if (!listenerMap.containsKey(listenerClass)) {
            listenerMap[listenerClass] = ArrayList()
        }
        listenerMap[listenerClass]?.add(listener)
        listenerMap[listenerClass] = ArrayList(listenerMap[listenerClass]?.sortedWith(compareBy { it.getPriority() }))
    }

    fun <E : Event> unRegisterListener(listener: EventListener<E>) {
        val listenerClass = getListenedForClass(listener)
        if (listenerMap.containsKey(listenerClass)) {
            listenerMap[listenerClass]?.remove(listener)
        }
    }

    fun <E : Event> postEvent(event: E) {
        val eventClass = event.javaClass
        if (listenerMap.containsKey(eventClass)) {
            listenerMap[eventClass]?.forEach { (it as EventListener<E>).handle(event) }
        }
    }

    private fun getListenedForClass(listener: EventListener<*>): Class<*> {
        return (listener.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
    }

}
