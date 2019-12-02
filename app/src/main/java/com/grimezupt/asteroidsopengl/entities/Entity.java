package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Game;
import com.grimezupt.asteroidsopengl.utils.Timer;

public abstract class Entity {
    public static final String TAG = "Entity";
    public static Game _game = null;
//    public static Entity _world = null;

    public abstract void update(double dt);
    public abstract void render(float[] viewportMatrix);

    public Timer getTimer(){
        return _game._timer;
    }
}
