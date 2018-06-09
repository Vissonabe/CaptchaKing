package com.example.viswanathankp.captchaking.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import com.example.viswanathankp.captchaking.model.CaptchaModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.viswanathankp.captchaking.utils.Constant.LEVEL_1_TIMER;
import static com.example.viswanathankp.captchaking.utils.Constant.LEVEL_2_TIMER;
import static com.example.viswanathankp.captchaking.utils.Constant.LEVEL_3_TIMER;
import static com.example.viswanathankp.captchaking.utils.Constant.LEVEL_4_TIMER;
import static com.example.viswanathankp.captchaking.utils.Constant.LEVEL_5_TIMER;

public class GameViewModel extends AndroidViewModel {

    public static String FINISHED = "finished";

    private final int MAX_GAME_CAN_BE_PLAYED = 5;

    private int totalGamePlayed = 0;

    private int current_game_level = 3;

    private int time=30;

    private ArrayList<CaptchaModel> playedCaptchaList;

    private ArrayList<CaptchaModel> captchaArrayList;

    private CaptchaModel currentModel;

    private MutableLiveData<String> timerLive = new MutableLiveData<>();

    private MutableLiveData<String> imageStringLive = new MutableLiveData<>();

    private MutableLiveData<ArrayList<CaptchaModel>> captchaLive = new MutableLiveData<>();

    public GameViewModel(@NonNull Application application) {
        super(application);
        playedCaptchaList = new ArrayList<>();
        captchaArrayList = loadJSONFromAsset();
        startGame(current_game_level);
    }

    private void startGame(int level){
        totalGamePlayed++;
        startTimer(getTimerFromLevel(level));
        ArrayList<CaptchaModel> levelModel = getLevelViceModels(level);
        if(levelModel.size() > 0) {
            currentModel = levelModel.get(0);
            playedCaptchaList.add(currentModel);
            imageStringLive.postValue(currentModel.name);
        }
    }

    public int getCurrentGameLevel(){
        return current_game_level;
    }

    public void submitClick(String answer){
        if(currentModel.answer.equalsIgnoreCase(answer)){
            currentModel.isCorrectAnswer = true;
            incrementGameLevel();
        } else {
            currentModel.isCorrectAnswer = false;
            decrementGameLevel();
        }
        startGame(current_game_level);
    }

    private void incrementGameLevel(){
        if(totalGamePlayed == MAX_GAME_CAN_BE_PLAYED && current_game_level == 5){
            showResultActivity();
        } else {
            current_game_level++;
        }
    }

    private void showResultActivity(){
        captchaLive.postValue(playedCaptchaList);
    }

    private void decrementGameLevel(){
            current_game_level--;
    }

    private int getTimerFromLevel(int level){
        switch (level){
            case 1 : return LEVEL_1_TIMER;
            case 2 : return LEVEL_2_TIMER;
            case 3 : return LEVEL_3_TIMER;
            case 4 : return LEVEL_4_TIMER;
            case 5 : return LEVEL_5_TIMER;
        }
        return LEVEL_1_TIMER;
    }

    private ArrayList<CaptchaModel> getLevelViceModels(int level){
        ArrayList<CaptchaModel> list = new ArrayList<>();
        for(CaptchaModel model : captchaArrayList){
            if(model.difficulty == level){
                list.add(model);
            }
        }
        return list;
    }

    public LiveData<String> getTimerLiveData(){
        return timerLive;
    }

    public LiveData<String> getImageStringLiveData(){
        return imageStringLive;
    }

    public LiveData<ArrayList<CaptchaModel>> getPlayedCaptchaLiveData(){
        return captchaLive;
    }

    private void startTimer(int seconds){
        time = seconds;

        new CountDownTimer(seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerLive.postValue("0:"+checkDigit(time));
                time--;
            }

            public void onFinish() {
                timerLive.postValue(FINISHED);
                decrementGameLevel();
            }

        }.start();
    }

    private String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private ArrayList<CaptchaModel> loadJSONFromAsset() {
        ArrayList<CaptchaModel> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getApplication().getApplicationContext().getAssets().open("captcha.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("captcha");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                CaptchaModel location = new CaptchaModel();
                location.id = jo_inside.getInt("id");
                location.name = ( jo_inside.getString("name"));
                location.difficulty = (jo_inside.getInt("difficulty"));
                location.answer = (jo_inside.getString("answer"));

                //Add your values in your `ArrayList` as below:
                locList.add(location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locList;
    }
}
