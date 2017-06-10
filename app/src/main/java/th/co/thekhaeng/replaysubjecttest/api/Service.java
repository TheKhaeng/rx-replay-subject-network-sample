package th.co.thekhaeng.replaysubjecttest.api;


import th.co.thekhaeng.replaysubjecttest.api.base.BaseService;

/**
 * Created by TheKhaeng on 9/15/2016 AD.
 */

public class Service extends BaseService<ApiService>{

    public static Service newInstance( String baseUrl ){
        Service service = new Service();
        service.setBaseUrl( baseUrl );
        return service;
    }

    private Service(){
    }

    @Override
    protected Class<ApiService> getApiClassType(){
        return ApiService.class;
    }
}
