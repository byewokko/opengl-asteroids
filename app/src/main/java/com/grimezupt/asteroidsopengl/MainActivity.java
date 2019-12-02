package com.grimezupt.asteroidsopengl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.grimezupt.asteroidsopengl.input.InputManager;
import com.grimezupt.asteroidsopengl.input.TouchController;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Game _game = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        _game = new Game(this);
        setContentView(R.layout.activity_main);
        InputManager controls = new TouchController(findViewById(R.id.gamepad));
        _game = findViewById(R.id.game);
        _game.setControls(controls);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            hideUI();
        }
    }

    private void hideUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}
