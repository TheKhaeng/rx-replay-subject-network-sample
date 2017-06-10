package th.co.thekhaeng.replaysubjecttest.subscribe;

import rx.SingleSubscriber;
import th.co.thekhaeng.replaysubjecttest.api.exception.ServerNotFoundException;

/**
 * Created by trusttanapruk on 8/10/2016.
 */
public class SimpleSubscriber<T> extends SingleSubscriber<T>{

    @Override
    public void onSuccess( T value ){
//        RxBus.get().post( value );
        SimpleReplaySubject.getInstance().post( value );
        this.unsubscribe();
    }

    @Override
    public void onError( Throwable error ){
        error.printStackTrace();
        sendModifiedErrorWithServiceName( error.getMessage() );
        this.unsubscribe();
    }

    private void sendModifiedErrorWithServiceName( String errorMessage ){
        ServerNotFoundException serverNotFoundException = new ServerNotFoundException( errorMessage );
//        RxBus.get().post( serverNotFoundException );
        SimpleReplaySubject.getInstance().post( serverNotFoundException );
    }
}
