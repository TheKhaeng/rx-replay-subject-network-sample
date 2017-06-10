package th.co.thekhaeng.replaysubjecttest.api;


import retrofit2.http.GET;
import rx.Single;
import th.co.thekhaeng.replaysubjecttest.api.result.ExampleResult;

import static th.co.thekhaeng.replaysubjecttest.api.URL.URL_REQUEST_SERVICE;

/**
 * Created by TheKhaeng on 9/14/2016 AD.
 */

public interface ApiService{

    @GET( URL_REQUEST_SERVICE )
    Single<ExampleResult> getRequestService();
}
