package com.grimezupt.asteroidsopengl.utils;

public class Random {
    private final static java.util.Random RNG = new java.util.Random();
    private Random(){ super ();}

    public static boolean coinFlip(){
        return RNG.nextFloat() > 0.5 ;
    }

    public static float nextFloat(){
        return RNG.nextFloat();
    }

    public static int nextInt(final int max){
        return RNG.nextInt(max);
    }

    public static int between(final int min, final int max){
        return RNG.nextInt(max-min)+min;
    }

    public static float between(final float min, final float max){
        return min+RNG.nextFloat()*(max-min);
    }

    public static float norm(final float mean, final float sd){
        return (float) RNG.nextGaussian() * sd + mean;
    }

    public static float clampnorm(float mean, float sd, float clamp) {
        return Utils.clamp(norm(mean, sd), mean - clamp, mean + clamp);
    }
}
