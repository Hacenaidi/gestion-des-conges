package com.leavemng.utils;

import javafx.scene.Scene;

public final class UiTheme {
    private static final String STYLESHEET = "/com/leavemng/views/styles.css";

    private UiTheme() {
    }

    public static void apply(Scene scene) {
        if (scene == null) {
            return;
        }

        String stylesheet = UiTheme.class.getResource(STYLESHEET).toExternalForm();
        if (!scene.getStylesheets().contains(stylesheet)) {
            scene.getStylesheets().add(stylesheet);
        }
    }
}