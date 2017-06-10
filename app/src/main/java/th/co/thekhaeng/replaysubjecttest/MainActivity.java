package th.co.thekhaeng.replaysubjecttest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;

import java.util.ArrayList;
import java.util.List;

import th.co.thekhaeng.replaysubjecttest.api.ServiceManager;
import th.co.thekhaeng.replaysubjecttest.api.exception.ServerNotFoundException;
import th.co.thekhaeng.replaysubjecttest.api.result.ExampleResult;
import th.co.thekhaeng.replaysubjecttest.subscribe.SimpleRxBus;

public class MainActivity extends AppCompatActivity{
    private static final String TAG_AIS_DIALOG = "loading_dialog";

    private LoadingDialog dialog;
    private Button btnRequest;
    private int countRequest = 0;
    private List<TextView> tvList;
    private List<TextInputLayout> tvLayoutList;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        tvList = new ArrayList<>();
        tvList.add( (TextView) findViewById( R.id.tv_response_1 ) );
        tvList.add( (TextView) findViewById( R.id.tv_response_2 ) );
        tvList.add( (TextView) findViewById( R.id.tv_response_3 ) );
        tvList.add( (TextView) findViewById( R.id.tv_response_4 ) );

        tvLayoutList = new ArrayList<>();
        tvLayoutList.add( (TextInputLayout) findViewById( R.id.container_tv_1 ) );
        tvLayoutList.add( (TextInputLayout) findViewById( R.id.container_tv_2 ) );
        tvLayoutList.add( (TextInputLayout) findViewById( R.id.container_tv_3 ) );
        tvLayoutList.add( (TextInputLayout) findViewById( R.id.container_tv_4 ) );

        btnRequest = (Button) findViewById( R.id.btn_request );
        btnRequest.setOnClickListener( onClickRequest() );
    }

    @Override
    protected void onResume(){
        super.onResume();
//        RxBus.get().register( this );
        SimpleRxBus.register( this );
    }

    @Override
    protected void onPause(){
        super.onPause();
        SimpleRxBus.unregister( this );
    }

    @Subscribe
    public void onReceiveExampleResult( ExampleResult result ){
        if( countRequest <= 1 ){
            ServiceManager.getInstance().requestService();
            setText( result.getStatus() + "", countRequest );
        }
        countRequest += 1;
        if( countRequest > 1 ){
            dismissDialog();
        }
    }


    @Subscribe
    public void onExampleResultException( ServerNotFoundException exception ){
        dismissDialog();
        setText( exception.getMessage(), 0 );
        setText( exception.getMessage(), 1 );
        setText( exception.getMessage(), 2 );
        setText( exception.getMessage(), 3 );
    }

    public void setText( String str, int pos ){
        TransitionManager.beginDelayedTransition( tvLayoutList.get( pos ) );
        tvList.get( pos ).setText( str );
    }

    private void showLoadingDialog(){
        dismissDialog();
        try{
            dialog = new LoadingDialog.Builder()
                    .build();
            dialog.show( getSupportFragmentManager(), TAG_AIS_DIALOG );
        }catch( IllegalStateException e ){
            e.printStackTrace();
        }
    }

    private void dismissDialog(){
        try{
            if( dialog != null && dialog.isAdded() ){
                dialog.dismiss();
                dialog = null;
            }
        }catch( IllegalStateException e ){
            e.printStackTrace();
        }
    }

    @NonNull
    private View.OnClickListener onClickRequest(){
        return new View.OnClickListener(){
            @Override
            public void onClick( View v ){
                countRequest = 0;
                showLoadingDialog();
                ServiceManager.getInstance().requestService();
            }
        };
    }

}
