package com.Ebbens.RektSpace.android;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.Ebbens.RektSpace.RektSpace;

import com.google.example.games.basegameutils.GameHelper;
import com.google.android.gms.games.Games;

import resources.ActionResolver;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, ActionResolver {

    private GameHelper gh;
    private boolean setupDone = false;

    @Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        if (gh == null) {
            gh = new GameHelper(this, GameHelper.CLIENT_GAMES);
        }


        initialize(new RektSpace(this), config);
	}

    @TargetApi(19)
    private void hideVirtualButtons(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                hideVirtualButtons();
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(gh.isSignedIn()) {
            gh.onStart(this);
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        if(gh.isSignedIn()) {
            gh.onStop();
        }
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        gh.onActivityResult(request, response, data);
    }

    @Override
    public boolean getSignedInGPGS() {
        return gh.isSignedIn();
    }

    @Override
    public void loginGPGS() {
        if(!setupDone) {
            gh.setup(this);
            setupDone = true;
        }

        try {
            runOnUiThread(new Runnable(){
                public void run() {
                    gh.beginUserInitiatedSignIn();
                }
            });
        } catch (final Exception ex) {
        }
    }

    @Override
    public void submitScoreGPGS(int score) {
        if (gh.isSignedIn()) {
            Games.Leaderboards.submitScore(gh.getApiClient(), "CgkI5c7UovcREAIQAQ", score);
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gh.getApiClient(), "CgkI5c7UovcREAIQAQ"), 100);
        }
        else if (!gh.isConnecting()) {
            loginGPGS();
        }
    }

    @Override
    public void unlockAchievementGPGS(String achievementId) {

    }

    @Override
    public void getLeaderboardGPGS() {
        if(gh.isSignedIn()){
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gh.getApiClient(), "CgkI5c7UovcREAIQAQ"), 100);
        }else if(!gh.isConnecting()){
            loginGPGS();
        }
    }

    @Override
    public void getAchievementsGPGS() {

    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }
}
