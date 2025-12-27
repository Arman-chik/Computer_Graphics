package org.rendering_app;

import javafx.application.Application;

public class Main {
        public static void main(String[] args) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism",
                String.valueOf(Runtime.getRuntime().availableProcessors()));
        Application.launch(MainWindow.class, args);
    }
}
