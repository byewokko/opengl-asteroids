package com.grimezupt.asteroidsopengl;

import com.grimezupt.asteroidsopengl.entities.GLBorder;
import com.grimezupt.asteroidsopengl.entities.GLEntity;
import com.grimezupt.asteroidsopengl.entities.Player;
import com.grimezupt.asteroidsopengl.entities.Star;

import java.util.ArrayList;
import java.util.Collection;

public class World {
    private static final String TAG = "Game";
    public static Game _game = null;
    public static float WIDTH = 80f;
    public static float HEIGHT = 80f;
    private static final int STAR_COUNT = 50;

    public final ArrayList<GLEntity> _entities = new ArrayList<>();

    private Player _player = null;
    private GLBorder _border = null;


    public World() {
        build();
    }

    public void build() {
        _player = new Player(WIDTH /2f, HEIGHT /2f);
        addEntity(_player);
        _border = new GLBorder(0f, 0f, WIDTH, HEIGHT);
        addEntity(_border);
        for (int i = 0; i < STAR_COUNT; i++){
            addEntity(Star.random(WIDTH, HEIGHT));
        }
    }

    public void update(double dt){
        for (GLEntity e : _entities){
            e.update(dt);
        }
    }

    public void addEntity(GLEntity e){
        _entities.add(e);
    }

    public void clearEntities(){
        _entities.clear();
    }

    public void addEntities(final Collection<GLEntity> entityCollection){
        _entities.addAll(entityCollection);
    }
}
