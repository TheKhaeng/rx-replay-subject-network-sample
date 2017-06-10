package th.co.thekhaeng.replaysubjecttest.subscribe;

import android.support.annotation.NonNull;

import com.hwangjr.rxbus.RxBus;

import rx.Subscriber;

/**
 * Created by thekhaeng on 3/21/2017 AD.
 */

public class SimpleRxBus{
    private static Subscriber subscriber;

    public static void post( Object object ){
        RxBus.get().post( object );
    }

    public static void register( Object object ){
        RxBus.get().register( object );
        subscriber = newSubscriber();
        SimpleReplaySubject.getInstance().observe().subscribe( subscriber );
    }

    public static void unregister( Object object ){
        RxBus.get().unregister( object );
        subscriber.unsubscribe();
    }

    @NonNull
    private static Subscriber newSubscriber(){
        return new Subscriber(){
            @Override
            public void onCompleted(){
            }

            @Override
            public void onError( Throwable e ){
                RxBus.get().post( e );
            }

            @Override
            public void onNext( Object o ){
                RxBus.get().post( o );
            }
        };
    }
}
