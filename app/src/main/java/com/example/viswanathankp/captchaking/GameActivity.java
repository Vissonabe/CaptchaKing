package com.example.viswanathankp.captchaking;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viswanathankp.captchaking.model.CaptchaModel;
import com.example.viswanathankp.captchaking.viewmodel.GameViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.example.viswanathankp.captchaking.utils.Constant.RESULT_CAPTCHA_BUNDLE;
import static com.example.viswanathankp.captchaking.viewmodel.GameViewModel.FINISHED;

public class GameActivity extends AppCompatActivity {

    private GameViewModel mViewModel;
    private TextView timerText;
    private ImageView captchaImg;
    private Button submitBtn;
    private EditText answerEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        timerText = findViewById(R.id.timer_txt);
        captchaImg = findViewById(R.id.img);
        submitBtn = findViewById(R.id.submit_btn);
        answerEditTxt = findViewById(R.id.edit_text);
        mViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        mViewModel.getTimerLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s != null & s.equals(FINISHED) && mViewModel.getCurrentGameLevel() == 1){
                    Toast.makeText(GameActivity.this, "GameFinished", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    timerText.setText(s);
                }
            }
        });

        mViewModel.getImageStringLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                int resourceImage = getResources().getIdentifier(s, "drawable", getPackageName());
                captchaImg.setImageResource(resourceImage);
            }
        });

        mViewModel.getPlayedCaptchaLiveData().observe(this, new Observer<ArrayList<CaptchaModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<CaptchaModel> captchaModels) {
                startResultActivity(captchaModels);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.submitClick(answerEditTxt.getText().toString());
            }
        });
    }

    private void startResultActivity(ArrayList<CaptchaModel> captchaModels){
        Intent intent = new Intent(this, ResultActivity.class);
//        Bundle extras = intent.getExtras();
//        extras.putParcelableArrayList(RESULT_CAPTCHA_BUNDLE, Parcels.wrap(captchaModels));
        startActivity(intent);
    }

}
