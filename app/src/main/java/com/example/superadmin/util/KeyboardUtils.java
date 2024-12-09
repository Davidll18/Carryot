package com.example.superadmin.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    // Método para ocultar el teclado cuando el usuario toca fuera de un campo
    public static boolean hideKeyboardOnTouch(Activity activity, MotionEvent event) {
        View view = activity.getCurrentFocus();
        if (view instanceof android.widget.EditText) {
            Rect outRect = new Rect();
            view.getGlobalVisibleRect(outRect);
            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                // Si el toque no está dentro del EditText, ocultamos el teclado
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return false;
    }
}
