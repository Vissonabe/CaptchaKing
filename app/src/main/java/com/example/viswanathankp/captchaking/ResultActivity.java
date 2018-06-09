package com.example.viswanathankp.captchaking;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.viswanathankp.captchaking.viewmodel.ResultViewModel;

public class ResultActivity extends AppCompatActivity {

    private ResultViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
    }
}
