package org.example.radar;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Random;

public class Target {
    private double angle;  // Angle of the target from radar center
    private double distance; // Distance from the radar center
    private double dx, dy;   // Movement velocity
    private int visibleTime = 0;  // Time the target remains visible after detection
    private Random random = new Random();

    private static final int RADAR_CENTER = 500; // Updated radar center (500, 500)
    private static final int MAX_RADAR_RANGE = 450; // Updated radar max range
    private static final int MIN_RADAR_RANGE = 100; // Minimum radar range

    public Target(double angle, double distance) {
        this.angle = angle;
        this.distance = distance;
        this.dx = (random.nextDouble() - 0.5) * 2;
        this.dy = (random.nextDouble() - 0.5) * 2;
    }

    public double getAngle() {
        return angle;
    }

    public double getDistance() {
        return distance;
    }

    public void setVisibleTime(int time) {
        this.visibleTime = time;
    }

    public boolean isVisible() {
        return visibleTime > 0;
    }

    public void decrementVisibleTime() {
        if (visibleTime > 0) {
            visibleTime--;
        }
    }

    public void updatePosition() {
        double speed = 0.5;
        double angleInRadians = Math.toRadians(angle);

        double newX = distance * Math.cos(angleInRadians) + dx * speed;
        double newY = distance * Math.sin(angleInRadians) + dy * speed;

        distance = Math.sqrt(newX * newX + newY * newY);
        angle = Math.toDegrees(Math.atan2(newY, newX));

        if (distance > MAX_RADAR_RANGE || distance < MIN_RADAR_RANGE) {
            dx = -dx;
            dy = -dy;
        }
    }

    public void draw(GraphicsContext gc, boolean forceVisible) {
        if (isVisible() || forceVisible) {
            double angleInRadians = Math.toRadians(angle);
            double x = RADAR_CENTER + distance * Math.cos(angleInRadians);
            double y = RADAR_CENTER - distance * Math.sin(angleInRadians);

            gc.setFill(Color.RED);
            gc.fillOval(x - 5, y - 5, 10, 10);
        }
    }
}