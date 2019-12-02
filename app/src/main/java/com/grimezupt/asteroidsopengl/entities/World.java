package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Game;
import com.grimezupt.asteroidsopengl.input.InputManager;
import com.grimezupt.asteroidsopengl.utils.Random;
import com.grimezupt.asteroidsopengl.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;

public class World extends Entity {
    private static final String TAG = "World";
    private static final int PROJECTILE_POOL_SIZE = 3;
    private static final int ASTEROID_COUNT = 4;
    public static Game _game = null;
    public static float WIDTH = 160f;
    public static float HEIGHT = 90f;
    private static final int STAR_COUNT = 50;

    public final ArrayList<Entity> _entities = new ArrayList<>();
    public final AsteroidPool _asteroidPool;
    public final ProjectilePool _projectilePool;
    public final ExplosionPool _explosionPool;

    private Player _player = null;
    private GLBorder _border = null;


    public World() {
        _projectilePool = new ProjectilePool();
        _asteroidPool = new AsteroidPool();
        _explosionPool = new ExplosionPool();
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
        for (int i = 0; i < ASTEROID_COUNT; i++){
            Asteroid a = _asteroidPool.pull();
            if (a != null) {
                a.setRandomVelocity();
                a.activate(Random.between(0, WIDTH),
                        Random.between(0, HEIGHT),
                        Random.between(5, 10),
                        Asteroid.DEFAULT_SIZE);
            }
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
        _asteroidPool.cleanup();
        _projectilePool.cleanup();
    }

    private void collisionDetection() {
        // projectiles vs. asteroids
        for (Projectile p : _projectilePool._activeEntities){
            for (Asteroid a : _asteroidPool._activeEntities){
                if (p.isColliding(a)){
                    p.onCollision(a);
                    a.onCollision(p);
                    if (a.isActive()){
                        // asteroid destroyed!
                        _explosionPool.makeExplosion(p, a, ExplosionPool.BIG_EXPLOSION);
                    } else {
                        _explosionPool.makeExplosion(p, a, ExplosionPool.SMALL_EXPLOSION);
                    }
                    break; // bullet can damage only one asteroid
                }
            }
        }
        // player vs. asteroids
        for (Asteroid a : _asteroidPool._activeEntities){
            if (_player.isColliding(a)){
                GLEntity.qdImpactVelocity(_player, a);
                _player.onCollision(a);
                Utils.negateVector(GLEntity.impactUnit);
                a.onCollision(_player);
                if (a.isActive()){
                    // asteroid destroyed!
                    _explosionPool.makeExplosion(_player, a, ExplosionPool.BIG_EXPLOSION);
                } else {
                    _explosionPool.makeExplosion(_player, a, ExplosionPool.SMALL_EXPLOSION);
                }
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
