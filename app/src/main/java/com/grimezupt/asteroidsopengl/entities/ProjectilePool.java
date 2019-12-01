package com.grimezupt.asteroidsopengl.entities;

public class ProjectilePool extends EntityPool<Projectile> {
    public ProjectilePool() {
        super(EntityPool.FIXED_SIZE);
    }

    @Override
    Projectile createNew() {
        return new Projectile();
    }
}
