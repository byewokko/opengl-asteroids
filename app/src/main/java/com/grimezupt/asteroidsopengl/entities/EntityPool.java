package com.grimezupt.asteroidsopengl.entities;

import android.util.Log;

import com.grimezupt.asteroidsopengl.entities.GLEntity;
import com.grimezupt.asteroidsopengl.entities.Suspendable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class EntityPool<E extends GLEntity & Suspendable> extends Entity {
    private static final String TAG = "EntityPool";
    private ArrayList<E> _suspendedEntities = new ArrayList<>();
    private ArrayList<E> _activeEntities = new ArrayList<>();
    private final Constructor<? extends E> _constructor = null;

    public EntityPool(int size) {
        init(size);
    }

    public EntityPool() {
    }

    public void init(int size) {
        for (int i = 0; i < size; i++) {
            _suspendedEntities.add(createNew());
        }
    }

    abstract E createNew();


    public E pull(){
        final E entity;
        if (_suspendedEntities.isEmpty()) {
            entity = createNew();
        } else {
            entity = _suspendedEntities.remove(0);
        }
        _activeEntities.add(entity);
        return entity;
    }

    public void suspend(E entity){
        _activeEntities.remove(entity);
        _suspendedEntities.add(entity);
    }

    @Override
    public void update(final double dt){
        for (Iterator<E> iterator = _activeEntities.iterator(); iterator.hasNext();){
            E e = iterator.next();
            e.update(dt);
            if (e.isSuspended()){
                _suspendedEntities.add(e);
                iterator.remove();
            }
        }
    }

    @Override
    public void render(float[] viewportMatrix) {
        for (E e : _activeEntities){
            e.render(viewportMatrix);
        }
    }

}
