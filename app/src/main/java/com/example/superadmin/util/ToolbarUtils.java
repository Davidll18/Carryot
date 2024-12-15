package com.example.superadmin.util;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;

public class ToolbarUtils {

    /**
     * Ajusta el padding superior del Toolbar para manejar dispositivos con notch o barras de estado.
     * @param toolbar El Toolbar al que se le ajustará el padding superior.
     */
    public static void adjustToolbarPadding(Toolbar toolbar) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            int insetTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            toolbar.setPadding(toolbar.getPaddingLeft(), insetTop, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
            return insets;
        });
    }
}
