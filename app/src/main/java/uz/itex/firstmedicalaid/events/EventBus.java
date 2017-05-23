package uz.itex.firstmedicalaid.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bakhtiyorbek Begmatov on 23.05.2017
 */

public class EventBus {

    private static EventBus instance;

    private Map<Class, Set<IEventSubscriber>> subscribers;

    private EventBus() {
        subscribers = new HashMap<>();
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }

        return instance;
    }

    public void subscribe(IEventSubscriber subscriber, Class... eventClasses) {
        for (Class eventClass : eventClasses) {
            if (!subscribers.containsKey(eventClass)) {
                subscribers.put(eventClass, new HashSet<IEventSubscriber>());
            }

            subscribers.get(eventClass).add(subscriber);
        }
    }

    public void unsubscribe(IEventSubscriber subscriber) {
        for (Map.Entry<Class, Set<IEventSubscriber>> entry : subscribers.entrySet()) {
            if (entry.getValue().contains(subscriber)) {
                entry.getValue().remove(subscriber);
            }
        }
    }

    public void postEvent(Object event) {
        if (event == null) return;

        if (subscribers.containsKey(event.getClass())) {
            Set<IEventSubscriber> set = subscribers.get(event.getClass());

            for (IEventSubscriber subscriber : set) {
                subscriber.onEvent(event);
            }
        }
    }
}
