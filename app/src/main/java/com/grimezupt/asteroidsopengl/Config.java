package com.grimezupt.asteroidsopengl;

public abstract class Config {
    public static abstract class Colors {
        public static final float[] BACKGROUND = {0.4f, 0.04f, 0.2f, 1f};
        public static final float[] FOREGROUND = {0.9f, 0.9f, 0.1f, 1f};
        public static final float[] HIGHLIGHT = {0.9f, 0.4f, 0.2f, 1f};
    }
    public static abstract class Physics {
        public static final float ASTEROID_VELOCITY = 1f;
        public static final float PLAYER_ACC = 1f;
        public static final float PLAYER_ROTATION_VELOCITY = 1f;
        public static final float PROJECTILE_VELOCITY = 1f;
    }
}
