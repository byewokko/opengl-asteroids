package com.grimezupt.asteroidsopengl.utils;

public class SignalWindow {

    private float [] _data;
    private int _pointer;
    public final int _size;

    public SignalWindow(int size) {
        Utils.require(size > 0);
        _size = size;
        _data = new float[size];
        _pointer = 0;
    }

    public void put(float f){
        _data[_pointer++] = f;
        _pointer %= _size;
    }

    public float readAverage(){
        float sum = 0f;
        for (float f : _data){
            sum += f;
        }
        return sum/_size;
    }
}
