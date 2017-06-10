package th.co.thekhaeng.replaysubjecttest.api;

import rx.Single;
import th.co.thekhaeng.replaysubjecttest.api.result.ExampleResult;
import th.co.thekhaeng.replaysubjecttest.subscribe.SchedulersIoThread;
import th.co.thekhaeng.replaysubjecttest.subscribe.SimpleSubscriber;

import static th.co.thekhaeng.replaysubjecttest.api.URL.BASE_URL;


public class ServiceManager{

    private static ServiceManager instance;
    private ApiService api;

    public static ServiceManager getInstance(){
        if( instance == null ){
            instance = new ServiceManager();
        }
        return instance;
    }

    private ServiceManager(){
    }

    public ApiService getServiceApi(){
        return api;
    }

    public void requestService(){
        requestServiceSingle()
                .subscribe( new SimpleSubscriber<>() );
    }

    private Single<ExampleResult> requestServiceSingle(){
        return Service.newInstance( BASE_URL )
                .getRxApi( getServiceApi() )
                .getRequestService()
                .compose(new SchedulersIoThread<ExampleResult>());
    }
}
