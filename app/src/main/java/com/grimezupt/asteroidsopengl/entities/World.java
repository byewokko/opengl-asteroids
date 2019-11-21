package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Game;
import com.grimezupt.asteroidsopengl.InputManager;
import com.grimezupt.asteroidsopengl.entities.Asteroid;
import com.grimezupt.asteroidsopengl.entities.EntityPool;
import com.grimezupt.asteroidsopengl.entities.GLBorder;
import com.grimezupt.asteroidsopengl.entities.GLEntity;
import com.grimezupt.asteroidsopengl.entities.GLText;
import com.grimezupt.asteroidsopengl.entities.Player;
import com.grimezupt.asteroidsopengl.entities.Projectile;
import com.grimezupt.asteroidsopengl.entities.Star;
import com.grimezupt.asteroidsopengl.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class World extends Entity {
    private static final String TAG = "World";
    private static final int PROJECTILE_POOL_SIZE = 5;
    public static Game _game = null;
    public static float WIDTH = 160f;
    public static float HEIGHT = 90f;
    private static final int STAR_COUNT = 50;

    public final ArrayList<Entity> _entities = new ArrayList<>();
    public final EntityPool<Projectile> _projectilePool;

    private Player _player = null;
    private GLBorder _border = null;
    private double dt;


    public World() {
        _projectilePool = new EntityPool<Projectile>() {
            @Override
            Projectile createNew() {
                return new Projectile();
            }
        };
    }

    public void build() {
        for (int i = 0; i < STAR_COUNT; i++){
            addEntity(Star.random(WIDTH, HEIGHT));
        }
        _border = new GLBorder(0f, 0f, WIDTH, HEIGHT);
        addEntity(_border);
        for (int points = 3; points <= 9; points++){
            addEntity(new Asteroid(Random.between(0, WIDTH),
                    Random.between(0, HEIGHT),
                    points));
        }
        _projectilePool.init(PROJECTILE_POOL_SIZE);
        addEntity(_projectilePool);
        _player = new Player(_projectilePool, WIDTH /2f, HEIGHT /2f);
        addEntity(_player);
        addEntity(new GLText("HELLO world", 0, 0));
    }

    @Override
    public void update(double dt){
        for (Entity e : _entities){
            e.update(dt);
        }
    }

    @Override
    public void render(float[] viewportMatrix) {
        for (Entity e : _entities){
            e.render(viewportMatrix);
        }
    }

    public void addEntity(Entity e){
        _entities.add(e);
    }

    public void clearEntities(){
        _entities.clear();
    }

    public void addEntities(final Collection<GLEntity> entityCollection){
        _entities.addAll(entityCollection);
    }

    public void input(InputManager inputs) {
        _player.input(inputs);
    }
}
