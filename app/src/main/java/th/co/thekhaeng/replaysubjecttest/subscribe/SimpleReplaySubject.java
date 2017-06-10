package th.co.thekhaeng.replaysubjecttest.subscribe;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by thekhaeng on 3/20/2017 AD.
 */

public class SimpleReplaySubject<T>{
    public static final int DEFAULT_CACHE_SIZE = 5;
    private static SimpleReplaySubject mInstance;

    //SerializedSubject is very important - we want to be able to post/subscribe items
    //on different threads - http://reactivex.io/RxJava/javadoc/rx/subjects/SerializedSubject.html
    private final Subject<Object, Object> rxBus =
            new SerializedSubject<>( ReplaySubject.createWithSize( DEFAULT_CACHE_SIZE ) );

    private SimpleReplaySubject(){
    }

    static public SimpleReplaySubject getInstance(){
        if( mInstance == null ){
            mInstance = new SimpleReplaySubject();
        }

        return mInstance;
    }

    /**
     * General method for publishing of events
     *
     * @param obj
     */
    public void post( Object obj ){
        rxBus.onNext( obj );
    }


    /**
     * General method for observing on all events
     *
     * @return
     */
    public Observable<Object> observe(){
        return rxBus;
    }


    public Observable<Object> subscribe(){
        return rxBus;
    }

    /**
     * Method to observe on certain Event class
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T extends Object> Observable<T> observe( final Class<T> eventType ){
        return rxBus.filter( new Func1<Object, Boolean>(){
            @Override
            public Boolean call( Object object ){
                return eventType.isInstance( object );
            }
        } ).cast( eventType );
    }


    /**
     * @param eventType
     * @param <T>
     * @return Return true if subject has any event of certain type, otherwise return false
     */
    public <T extends Object> boolean hasEventsOfType( final Class<T> eventType ){
        Object[] events = ( (ReplaySubject) rxBus ).getValues();

        int count = 0;
        for( Object e : events ){
            if( eventType.isInstance( e ) ){
                count++;
            }
        }

        return ( count > 0 );
    }
}
