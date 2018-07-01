import events.Event
import events.EventListener
import java.lang.reflect.ParameterizedType
import java.util.*


object EventManager {
    private val listenerMap = HashMap<Class<*>, ArrayList<EventListener<*>>>()

    fun <E : Event> registerListener(listener: EventListener<E>) {
        val listenerClass = getListenedForClass(listener)
        if (!listenerMap.containsKey(listenerClass)) {
            listenerMap[listenerClass] = ArrayList()
        }
        listenerMap[listenerClass]?.add(listener)
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
            listenerMap[eventClass]?.forEach { (it as EventListener<E>).handle(event)  }
        }
    }

    private fun getListenedForClass(listener: EventListener<*>): Class<*> {
        return (listener.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>
    }

}