package com.grimezupt.asteroidsopengl.entities;

import com.grimezupt.asteroidsopengl.Config;
import com.grimezupt.asteroidsopengl.mesh.Triangle;
import com.grimezupt.asteroidsopengl.utils.Utils;

public class JetFlame extends GLEntity {
    private static final float SIZE = 2.5f;

    public JetFlame() {
        setColors(Config.Colors.HIGHLIGHT);
        setScale(SIZE);
        _mesh = new Triangle();
        _mesh.applyAspectRatio();
        _mesh.setOrigin(0f, 4f, 0f);
    }

    @Override
    public void update(double dt) {
        setScale((float) (SIZE - Utils.sawtoothWave(getTimer().getClock(), 6f)));
    }
}
