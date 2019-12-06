package com.grimezupt.asteroidsopengl.entities;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class EntityPool<E extends GLEntity & Poolable> extends Entity {
    private static final String TAG = "EntityPool";
    public static final int FIXED_SIZE = 0;
    public static final int DYNAMIC_SIZE = 1;
    private final int _sizeType;
    private ArrayList<E> _suspendedEntities = new ArrayList<>();
    ArrayList<E> _activeEntities = new ArrayList<>();
    ArrayList<E> _entitiesToAdd = new ArrayList<>();

    public EntityPool(int sizeType) {
        _sizeType = sizeType;
    }

    public void init(int size) {
        for (int i = 0; i < size; i++) {
            E e = createNew();
            e.setPool(this);
            _suspendedEntities.add(e);
        }
    }

    abstract E createNew();

    @Nullable
    public E pull(){
        final E entity;
        if (_suspendedEntities.isEmpty()) {
            if (_sizeType == 1) {
                entity = createNew();
                entity.setPool(this);
            } else {
                return null;
            }
        } else {
            entity = _suspendedEntities.remove(0);
        }
        _entitiesToAdd.add(entity);
        return entity;
    }

    public void suspendInactive(){
        for (Iterator<E> iterator = _activeEntities.iterator(); iterator.hasNext();){
            E e = iterator.next();
            if (!e.isActive()){
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

    public void cleanup(){
        suspendInactive();
        _activeEntities.addAll(_entitiesToAdd);
        _suspendedEntities.removeAll(_entitiesToAdd);
        _entitiesToAdd.clear();
    }

    public boolean isAllSuspended() {
        return (_activeEntities.isEmpty() && _entitiesToAdd.isEmpty());
    }
}
