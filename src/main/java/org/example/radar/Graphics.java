package org.example.radar;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static org.example.radar.RadarSimulation.*;

public class Graphics {

    // Draw radar screen
    public static void drawRadar(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);

        for (int i = 0; i < 3; i++) {
            gc.setStroke(Color.DARKGREEN);
            gc.setLineWidth(2);
            int radius = 150 * (i + 1);
            gc.strokeOval(RADAR_CENTER - radius, RADAR_CENTER - radius, 2 * radius, 2 * radius);
        }

        drawModeText(gc);

        if (isTWSMode) {
            drawTWSCursor(gc);
        } else if (isLocked && lockedTarget != null) {
            if(isLaunchMode){
                drawLaunchCursor(gc);
            }
            else{
                drawLockedCursor(gc);
            }
        } else {
            drawNormalCursor(gc);
        }

        for (Target target : targets) {
            boolean forceVisible = (isLocked && target == lockedTarget) || isTWSMode;
            target.draw(gc, forceVisible);
        }

        if (isLocked && lockedTarget != null) {
            if(isLaunchMode){
                drawLockHighlight(gc, lockedTarget, Color.RED);
            }
            else {
                drawLockHighlight(gc, lockedTarget, Color.BLUE);
            }

        } else if (isTWSMode && twsTarget != null) {
            drawLockHighlight(gc, twsTarget, Color.YELLOW);
        }

        if (isTWSMode) {
            gc.setFill(Color.YELLOW);
            gc.fillText("TWS Mode: " + targets.size() + " targets, Target " + (twsTargetIndex + 1), 10, 50);
        }
    }

    // Draw launch cursor
    public static void drawLaunchCursor(GraphicsContext gc) {
        if (lockedTarget != null) {
            double angleInRadians = Math.toRadians(lockedTarget.getAngle());
            double x = RADAR_CENTER + lockedTarget.getDistance() * Math.cos(angleInRadians);
            double y = RADAR_CENTER - lockedTarget.getDistance() * Math.sin(angleInRadians);
            gc.setStroke(Color.RED);
            gc.strokeLine(RADAR_CENTER, RADAR_CENTER, x, y);
        }
    }

    // Draw mode text
    public static void drawModeText(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(new javafx.scene.text.Font(20));

        if (isTWSMode) {
            gc.setFill(Color.YELLOW);
            gc.fillText("TWS Mode", 10, 30);
        } else {
            gc.setFill(Color.WHITE);
            gc.fillText("Normal Mode", 10, 30);
        }

        if (isLocked) {
            if(isLaunchMode){
                gc.setFill(Color.RED);
                gc.fillText("Ready", CANVAS_SIZE - 100, 30);
            }
            else{
                gc.setFill(Color.BLUE);
                gc.fillText("Locked", CANVAS_SIZE - 100, 30);
            }
        } else {
            gc.setFill(Color.LIMEGREEN);
            gc.fillText("Searching", CANVAS_SIZE - 120, 30);
        }
    }

    // Draw normal cursor
    public static void drawNormalCursor(GraphicsContext gc) {
        double angleInRadians = Math.toRadians(sweepAngle);
        double x = RADAR_CENTER + MAX_RADAR_RANGE * Math.cos(angleInRadians);
        double y = RADAR_CENTER - MAX_RADAR_RANGE * Math.sin(angleInRadians);
        gc.setStroke(Color.LIMEGREEN);
        gc.strokeLine(RADAR_CENTER, RADAR_CENTER, x, y);
    }

    // Draw TWS cursor
    public static void drawTWSCursor(GraphicsContext gc) {
        if (twsTarget != null) {
            double angleInRadians = Math.toRadians(twsTarget.getAngle());
            double x = RADAR_CENTER + twsTarget.getDistance() * Math.cos(angleInRadians);
            double y = RADAR_CENTER - twsTarget.getDistance() * Math.sin(angleInRadians);
            gc.setStroke(Color.YELLOW); // Yellow line for TWS mode
            gc.strokeLine(RADAR_CENTER, RADAR_CENTER, x, y);
        }
    }

    // Draw locked cursor
    public static void drawLockedCursor(GraphicsContext gc) {
        if (lockedTarget != null) {
            double angleInRadians = Math.toRadians(lockedTarget.getAngle());
            double x = RADAR_CENTER + lockedTarget.getDistance() * Math.cos(angleInRadians);
            double y = RADAR_CENTER - lockedTarget.getDistance() * Math.sin(angleInRadians);
            gc.setStroke(Color.BLUE);
            gc.strokeLine(RADAR_CENTER, RADAR_CENTER, x, y);
        }
    }

    // Draw lock highlight
    public static void drawLockHighlight(GraphicsContext gc, Target target, Color color) {
        double angleInRadians = Math.toRadians(target.getAngle());
        double x = RADAR_CENTER + target.getDistance() * Math.cos(angleInRadians);
        double y = RADAR_CENTER - target.getDistance() * Math.sin(angleInRadians);

        gc.setStroke(color);
        gc.setLineWidth(3);
        if (color == Color.BLUE) {
            gc.strokeRect(x - 10, y - 10, 20, 20);
        } else if (color == Color.YELLOW) {
            gc.strokePolygon(new double[]{x, x - 10, x + 10}, new double[]{y - 10, y + 10, y + 10}, 3);
        }
        else if(color == Color.RED){
            gc.strokeRect(x - 10, y - 10, 20, 20);
        }
    }

}
