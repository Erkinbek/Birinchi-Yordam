package uz.itex.firstmedicalaid.events;

/**
 * Created by Bakhtiyorbek Begmatov on 23.05.2017
 */

public interface IEventSubscriber {

    void onEvent(Object event);
}
