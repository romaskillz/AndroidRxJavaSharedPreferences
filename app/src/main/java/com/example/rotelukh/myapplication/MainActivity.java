package com.example.rotelukh.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    private TextView tvValue1;
    private TextView tvValue2;

    private EditText eTinputValue;
    private Button b;

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_VALUE = "value";
    private SharedPreferences mSettings;
    private Observable<Integer> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            SharedPreferences.OnSharedPreferenceChangeListener listener;

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                listener = (sharedPreferences, key) -> {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(mSettings.getInt(APP_PREFERENCES_VALUE, 0));
                    }
                };
                mSettings.registerOnSharedPreferenceChangeListener(listener);
            }
        });

        observable.subscribe(value -> tvValue1.setText(value.toString()));
        observable.switchMap(integer -> Observable.just(integer * 2))
                .subscribe(value -> tvValue2.setText(value.toString()));


    }

    private void init() {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        eTinputValue = (EditText) findViewById(R.id.eTinputValue);
        b = (Button) findViewById(R.id.buttonSave);
        tvValue1 = (TextView) findViewById(R.id.tvValue1);
        tvValue2 = (TextView) findViewById(R.id.tvValue2);
    }

    public void onClick(View v) {
        int age = Integer.parseInt(eTinputValue.getText().toString());

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_VALUE, age);
        editor.apply();
    }
}
