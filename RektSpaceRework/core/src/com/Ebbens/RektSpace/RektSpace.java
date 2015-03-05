package com.Ebbens.RektSpace;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import resources.ScreenStack;

public class RektSpace extends Game {

    @Override
    public void create() {

        ScreenStack.init(this);
    }
}
