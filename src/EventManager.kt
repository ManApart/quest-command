import events.Event
import events.EventListener
import java.lang.reflect.ParameterizedType
import java.util.HashMap
import java.util.ArrayList
import kotlin.reflect.KClass


object EventManager {
    private val listenerMap = HashMap<KClass<*>, ArrayList<EventListener<*>>>()

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
        val eventClass = event::class as KClass<*>
        if (listenerMap.containsKey(eventClass)) {
            val listenerList = listenerMap[eventClass]
            for (i in listenerList?.indices!!) {
                val listener = listenerList[i] as EventListener<E>
                listener.handle(event)
            }
        }
    }

    private fun getListenedForClass(listener: EventListener<*>): KClass<*> {
        return (listener.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as KClass<*>
    }

}