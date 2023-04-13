package com.example;

import javafx.scene.image.Image;

public class ResourcesManager {

    public static Image getTitleIcon() {
        return new Image(ResourcesManager.class.getResourceAsStream("/asset/icon.png"));
    }

    public static void main(String[] args) {
        getTitleIcon();
    }
}
