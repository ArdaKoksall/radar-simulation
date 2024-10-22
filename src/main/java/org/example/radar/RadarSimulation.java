package org.example.radar;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import static org.example.radar.Functions.*;
import static org.example.radar.Graphics.*;

public class RadarSimulation extends Application {
    public static  double sweepAngle = 0; // Initial sweep angle
    private double sweepSpeed = 1; // Initial sweep speed
    public static  ArrayList<Target> targets = new ArrayList<>();
    public static  Random random = new Random();
    public static  final int VISIBLE_TIME_AFTER_DETECTION = 120; // Frames target stays visible after detection
    public static  Target lockedTarget = null; // The target currently locked
    public static  boolean isLocked = false; // Lock state
    public static  boolean wasInTWSMode = false; // Previous mode state
    public static  boolean isTWSMode = false; // TWS Mode toggle
    public static  int twsTargetIndex = 0; // Index of target being followed in TWS mode
    public static  boolean isLaunchMode = false; // Launch mode toggle
    public static  Target twsTarget = null; // Currently followed target in TWS mode
    public static  final int CANVAS_SIZE = 1000; // Adjust canvas size to 1000x1000
    public static  final int RADAR_CENTER = CANVAS_SIZE / 2; // Center of radar at (500, 500)
    public static  final int MAX_RADAR_RANGE = 450; // Max radar range for third circle

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        ComboBox<Integer> speedControl = new ComboBox<>();
        speedControl.getItems().addAll(1, 3, 5);
        speedControl.setValue(1);
        speedControl.setOnAction(_ -> sweepSpeed = speedControl.getValue());

        root.setTop(speedControl);
        root.setCenter(canvas);

        for (int i = 0; i < 5; i++) {
            double angle = random.nextDouble() * 360;
            double distance = 100 + random.nextDouble() * (MAX_RADAR_RANGE - 100);
            targets.add(new Target(angle, distance));
        }

        Scene scene = getScene(root);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawRadar(gc);
                if (!isLocked) {
                    sweepAngle += sweepSpeed;
                    if (sweepAngle >= 360) {
                        sweepAngle = 0;
                    }
                }
                detectTargets();
                updateTargets();
            }
        }.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Radar Simulation");
        primaryStage.show();
    }

    @NotNull
    private static Scene getScene(BorderPane root) {
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.X && !isLaunchMode) {
                toggleLock();
            }
            else if (event.getCode() == KeyCode.Z && !isLaunchMode) {
                if(!isLocked){
                    toggleTWSMode();
                }
            }
            else if (event.getCode() == KeyCode.A && isTWSMode && !isLaunchMode) {
                switchTWSTarget_plus();
            }
            else if (event.getCode() == KeyCode.D && isTWSMode && !isLaunchMode) {
                switchTWSTarget_minus();
            }
            else if (event.getCode() == KeyCode.C) {
                if(isLocked){
                    isLaunchMode = !isLaunchMode;
                    if(isLaunchMode){
                        playLaunchSound();
                    }
                }
            }
        });
        return scene;
    }

}