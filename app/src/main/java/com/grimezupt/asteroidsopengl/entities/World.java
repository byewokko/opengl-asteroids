package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Game;
import com.grimezupt.asteroidsopengl.InputManager;
import com.grimezupt.asteroidsopengl.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class World extends Entity {
    private static final String TAG = "World";
    private static final int PROJECTILE_POOL_SIZE = 3;
    private static final int ASTEROID_COUNT = 8;
    public static Game _game = null;
    public static float WIDTH = 160f;
    public static float HEIGHT = 90f;
    private static final int STAR_COUNT = 50;

    public final ArrayList<Entity> _entities = new ArrayList<>();
    public final EntityPool<Asteroid> _asteroidPool;
    public final EntityPool<Projectile> _projectilePool;

    private Player _player = null;
    private GLBorder _border = null;


    public World() {
        _projectilePool = new EntityPool<Projectile>(EntityPool.FIXED_SIZE) {
            @Override
            Projectile createNew() {
                return new Projectile();
            }
        };
        _asteroidPool = new EntityPool<Asteroid>(EntityPool.DYNAMIC_SIZE) {
            @Override
            Asteroid createNew() {
                Asteroid a = new Asteroid();
                a.setPool(_asteroidPool);
                return a;
            }
        };
    }

    public void build() {
        for (int i = 0; i < STAR_COUNT; i++){
            addEntity(Star.random(WIDTH, HEIGHT));
        }
        _player = new Player(_projectilePool, WIDTH /2f, HEIGHT /2f);
        addEntity(_player);
        _border = new GLBorder(WIDTH/2f, HEIGHT/2f, WIDTH, HEIGHT);
        addEntity(_border);
//        for (int points = 3; points <= 9; points++){
//            addEntity(new Asteroid(Random.between(0, WIDTH),
//                    Random.between(0, HEIGHT),
//                    points));
//        }
        _asteroidPool.init(ASTEROID_COUNT*3);
        for (int points = 3; points <= 9; points++){
            Asteroid a = _asteroidPool.pull();
            Objects.requireNonNull(a).activate(Random.between(0, WIDTH),
                    Random.between(0, HEIGHT),
                    points);
        }
        addEntity(_asteroidPool);
        _projectilePool.init(PROJECTILE_POOL_SIZE);
        addEntity(_projectilePool);
    }

    @Override
    public void update(double dt){
        for (Entity e : _entities){
            e.update(dt);
        }
        collisionDetection();
        _asteroidPool.removeSuspended();
        _projectilePool.removeSuspended();
    }

    private void collisionDetection() {
        // projectiles vs. asteroids
        for (Projectile p : _projectilePool._activeEntities){
            for (Asteroid a : _asteroidPool._activeEntities){
                if (p.isColliding(a)){
                    p.onCollision(a);
                    a.onCollision(p);
                    break; // bullet can damage only one asteroid
                }
            }
        }
        // player vs. asteroids
        for (Asteroid a : _asteroidPool._activeEntities){
            if (_player.isColliding(a)){
                _player.onCollision(a);
                a.onCollision(_player);
                break;
            }
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
