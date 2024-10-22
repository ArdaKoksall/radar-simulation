package org.example.radar;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;

import static org.example.radar.RadarSimulation.*;

public class Functions {

    private static Clip clip;

    // Detect targets within 3 degrees of sweep angle
    public static void detectTargets() {
        if (!isLocked && !isTWSMode) {
            for (Target target : targets) {
                if (Math.abs(sweepAngle - target.getAngle()) < 3) {
                    target.setVisibleTime(VISIBLE_TIME_AFTER_DETECTION);
                    playRadarBeep();
                }
            }
        }
    }

    // Update target positions and decrement visible time
    public static void updateTargets() {
        for (Target target : targets) {
            target.updatePosition();
            target.decrementVisibleTime();
        }
    }

    // Toggle TWS mode
    public static void toggleTWSMode() {
        if (!isTWSMode) {
            isTWSMode = true;
            twsTargetIndex = random.nextInt(targets.size());
            twsTarget = targets.get(twsTargetIndex);
        } else {
            isTWSMode = false;
            twsTarget = null;
        }
    }

    // Switch TWS target+
    public static void switchTWSTarget_plus() {
        if (isTWSMode) {
            twsTargetIndex = (twsTargetIndex + 1) % targets.size();
            twsTarget = targets.get(twsTargetIndex);
            playTWSTargetChange();
        }
    }

    // Switch TWS target-
    public static void switchTWSTarget_minus() {
        if (isTWSMode) {
            twsTargetIndex = (twsTargetIndex - 1 + targets.size()) % targets.size();
            twsTarget = targets.get(twsTargetIndex);
            playTWSTargetChange();
        }
    }

    // Toggle lock
    public static void toggleLock() {
        if (isLocked) {
            isLocked = false;
            lockedTarget = null;
            isTWSMode = wasInTWSMode;
        } else if (isTWSMode && twsTarget != null) {
            lockedTarget = twsTarget;
            isLocked = true;
            wasInTWSMode = true;
            isTWSMode = false;
            playLockSound();
        } else {
            for (Target target : targets) {
                if (target.isVisible()) {
                    lockedTarget = target;
                    isLocked = true;
                    wasInTWSMode = false;
                    playLockSound();
                    break;
                }
            }
        }
    }

    // Toggle launch mode
    private static void playLockSound() {
        String lockSound = "src/main/resources/lock.wav";
        loadSound(lockSound);
    }

    // Play launch sound
    public static void playLaunchSound() {
        String launchSound = "src/main/resources/ready-for-launch.wav";
        loadSound(launchSound);
    }

    // Play radar beep sound
    private static void playRadarBeep() {
        String radarBeep = "src/main/resources/radarbeep.wav";
        loadSound(radarBeep);
    }

    // Play TWS target change sound
    private static void playTWSTargetChange() {
        String twsTargetChange = "src/main/resources/tws-target-change.wav";
        loadSound(twsTargetChange);
    }

    // Load sound file
    private static void loadSound(String filePath) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(0);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException _) {}
    }

}
