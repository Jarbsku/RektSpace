package com.Ebbens.RektSpace;

import com.badlogic.gdx.Game;
import resources.ActionResolver;
import resources.Globals;
import resources.ScreenStack;

public class RektSpace extends Game {

    private ActionResolver ar;

    public RektSpace(ActionResolver ar){
        this.ar = ar;
    }
    public RektSpace(){    }

    @Override
    public void create() {
        ScreenStack.init(this);
    }

    public ActionResolver getActionResolver(){
        return this.ar;
    }
}
