package com.grimezupt.asteroidsopengl.input;

public class InputManager {
    public float _verticalFactor = 0.0f;
    public float _horizontalFactor = 0.0f;
    public boolean _pressingA = false;
    public boolean _pressingB = false;
    public boolean _pressingA0 = false;
    public boolean _pressingB0 = false;
    public boolean _justPressedA = false;
    public boolean _justPressedB = false;
    public boolean _justReleasedA = false;
    public boolean _justReleasedB = false;

    public void onStart() {}

    public void onStop() {}

    public void onPause() {}

    public void onResume() {}
    
    public void updateTransients() {
        if (!_pressingA0 && _pressingA) {
            _justPressedA = true;
            _justReleasedA = false;
        } else if (_pressingA0 && !_pressingA) {
            _justReleasedA = true;
            _justPressedA = false;
        } else {
            _justPressedA = false;
            _justReleasedA = false;
        }
        if (!_pressingB0 && _pressingB) {
            _justPressedB = true;
            _justReleasedB = false;
        } else if (_pressingB0 && !_pressingB) {
            _justReleasedB = true;
            _justPressedB = false;
        } else {
            _justPressedB = false;
            _justReleasedB = false;
        }
        _pressingA0 = _pressingA;
        _pressingB0 = _pressingB;
    }
}
