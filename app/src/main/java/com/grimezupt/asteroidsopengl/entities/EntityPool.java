package com.grimezupt.asteroidsopengl.entities;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class EntityPool<E extends GLEntity & Suspendable> extends Entity {
    private static final String TAG = "EntityPool";
    public static final int FIXED_SIZE = 0;
    public static final int DYNAMIC_SIZE = 1;
    private final int _sizeType;
    private ArrayList<E> _suspendedEntities = new ArrayList<>();
    public ArrayList<E> _activeEntities = new ArrayList<>();

    public EntityPool(int sizeType) {
        _sizeType = sizeType;
    }

    public void init(int size) {
        for (int i = 0; i < size; i++) {
            _suspendedEntities.add(createNew());
        }
    }

    abstract E createNew();

    @Nullable
    public E pull(){
        final E entity;
        if (_suspendedEntities.isEmpty()) {
            if (_sizeType == 1) {
                entity = createNew();
            } else {
                return null;
            }
        } else {
            entity = _suspendedEntities.remove(0);
        }
        _activeEntities.add(entity);
        return entity;
    }

    public void removeSuspended(){
        for (Iterator<E> iterator = _activeEntities.iterator(); iterator.hasNext();){
            E e = iterator.next();
            if (e.isSuspended()){
                _suspendedEntities.add(e);
                iterator.remove();
            }
        }
    }

    @Override
    public void update(final double dt){
        for (E e : _activeEntities){
            e.update(dt);
        }
    }

    @Override
    public void render(float[] viewportMatrix) {
        for (E e : _activeEntities){
            e.render(viewportMatrix);
        }
    }

}
