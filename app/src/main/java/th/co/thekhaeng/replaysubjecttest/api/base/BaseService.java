package th.co.thekhaeng.replaysubjecttest.api.base;


import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.thekhaeng.replaysubjecttest.BuildConfig;
import th.co.thekhaeng.replaysubjecttest.api.interceptor.HttpLogger;


/**
 * Created by thekhaeng on 3/20/2017 AD.
 */
public abstract class BaseService<T>{
    protected String baseUrl;
    private boolean logger = false;
    private T api;

    //every network service class must inherit this class and set the class type, too
    protected abstract Class<T> getApiClassType();

    public String getBaseUrl(){
        return baseUrl;
    }

    public void setBaseUrl( String baseUrl ){
        this.baseUrl = baseUrl;
    }

    private boolean isLogger(){
        logger = BuildConfig.DEBUG;
        return logger;
    }


    public void setApi( T api ){
        this.api = api;
    }

    public T getRxApi( T api ){
        setApi( api );
        if( this.api == null ){
            this.api = createApi();
        }
        return this.api;
    }

    protected Retrofit.Builder getBaseRetrofitBuilder(){
        if( addConverter() == null ){
            return new Retrofit.Builder()
                    .baseUrl( getBaseUrl() )
                    .client( getClient() );
        }
        return new Retrofit.Builder()
                .baseUrl( getBaseUrl() )
                .addConverterFactory( addConverter() )
                .client( getClient() );
    }


    /**
     * return "null" for not use Converter in retrofit.
     */
    protected Converter.Factory addConverter(){
        return GsonConverterFactory.create( new GsonBuilder().setPrettyPrinting().create() );
    }


    protected long getDefaultTimeout(){
        return 60000;
    }

    protected CertificatePinner getDefaultCertificatePinner(){
        return new CertificatePinner.Builder().build();
    }

    protected HttpLoggingInterceptor getDefaultHttpLogging( boolean showLog ){
        if( showLog ){
            return new HttpLoggingInterceptor( new HttpLogger() ).setLevel( HttpLoggingInterceptor.Level.BODY );
        }else{
            return new HttpLoggingInterceptor().setLevel( HttpLoggingInterceptor.Level.NONE );
        }
    }

    /********************/
    /** Private method **/
    /********************/
    //<editor-fold desc="Private function folding">
    private OkHttpClient getClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder
                .addNetworkInterceptor( getDefaultHttpLogging( isLogger() ) )
                .certificatePinner( getDefaultCertificatePinner() )
                .readTimeout( getDefaultTimeout(), TimeUnit.MILLISECONDS )
                .writeTimeout( getDefaultTimeout(), TimeUnit.MILLISECONDS )
                .connectTimeout( getDefaultTimeout(), TimeUnit.MILLISECONDS )
                .build();
    }

    private T createApi(){
        return getBaseRetrofitBuilder()
                .addCallAdapterFactory( RxJavaCallAdapterFactory.create() )
                .build()
                .create( getApiClassType() );
    }

    //</editor-fold>

}
