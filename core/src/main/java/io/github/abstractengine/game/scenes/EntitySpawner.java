package io.github.abstractengine.game.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.abstractengine.game.AlgorithmManager;
import io.github.abstractengine.game.entities.Circle;
import io.github.abstractengine.game.entities.SafeZone;
import io.github.abstractengine.game.entities.PowerUpPickup;
import io.github.abstractengine.game.entities.PowerUpType;
import io.github.abstractengine.game.entities.Square;
import io.github.abstractengine.game.entities.Triangle;
import io.github.abstractengine.managers.EntityManager;
import io.github.abstractengine.managers.MovementManager;
import io.github.abstractengine.entities.Entity;
import io.github.abstractengine.movement.RandomMovement;

/**
 * Handles spawning of enemies, answer squares, and power-up pickups.
 * Includes overlap protection to prevent entities from spawning
 * on top of each other or the player.
 * Extracted from StartScene to improve separation of concerns.
 */
public class EntitySpawner {

    private static final float ENEMY_BASE_SPEED = 150f;
    private static final float ENEMY_MOVE_PHASE_SECONDS = 2f;
    private static final float POWERUP_PICKUP_SIZE = 60f;

    private final Viewport viewport;
    private final EntityManager entityManager;
    private final MovementManager movementManager;
    private final Circle player;
    private final BitmapFont font;

    private SafeZone safeZone;

    private final List<Square> currentAnswerSquares = new ArrayList<>();
    private final List<PowerUpPickup> activePowerUpPickups = new ArrayList<>();
    private int prevStreakForPowerUps;

    public EntitySpawner(Viewport viewport, EntityManager entityManager,
                         MovementManager movementManager, Circle player, BitmapFont font) {
        this.viewport = viewport;
        this.entityManager = entityManager;
        this.movementManager = movementManager;
        this.player = player;
        this.font = font;
        this.prevStreakForPowerUps = 0;
        this.safeZone = null;
    }

    public void setSafeZone(SafeZone safeZone) {
        this.safeZone = safeZone;
    }

    // ---------------------------
    // ENEMY SPAWNING
    // ---------------------------

    public void spawnEnemy() {
        float tWidth = 70f;
        float tHeight = 70f;
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        float randomX;
        float randomY;
        boolean invalidPosition;
        int attempts = 0;

        do {
            invalidPosition = false;
            randomX = MathUtils.random(250f, worldW - tWidth - 20f);
            randomY = MathUtils.random(20f, worldH - 200f);

            if (safeZone != null && safeZone.overlapsRectangle(randomX, randomY, tWidth, tHeight, 0f)) {
                invalidPosition = true;
            }

            attempts++;
        } while (invalidPosition && attempts < 100);

        Triangle triangle = new Triangle(randomX, randomY, tWidth, tHeight);
        RandomMovement rm = new RandomMovement(
                ENEMY_BASE_SPEED,
                ENEMY_MOVE_PHASE_SECONDS,
                worldW,
                worldH,
                tWidth
        );
        rm.setSafeZone(safeZone);
        triangle.setMovementComponent(rm);

        entityManager.addEntity(triangle);
        movementManager.register(triangle);
    }

    // ---------------------------
    // QUESTION / ANSWER SPAWNING
    // ---------------------------

    /**
     * Spawns the next question with 3 answer squares.
     * Clears the previous answer squares first.
     *
     * @param algorithmManager selects the next question
     * @param fallbackQuestions fallback list if algorithm returns null
     * @return the spawned Question (for tracking by caller)
     */
    public QuestionBank.Question spawnNextQuestion(AlgorithmManager algorithmManager,
                                                    List<QuestionBank.Question> fallbackQuestions) {
        // Clear old squares
        for (Square s : currentAnswerSquares) {
            entityManager.removeEntity(s);
        }
        currentAnswerSquares.clear();

        QuestionBank.Question q = algorithmManager.getNextQuestion();
        if (q == null) {
            q = fallbackQuestions.get(MathUtils.random(0, fallbackQuestions.size() - 1));
        }

        // Spawn answers (1 correct + 2 decoys)
        String[] answers = { q.correct, q.decoy1, q.decoy2 };
        boolean[] correctness = { true, false, false };

        for (int i = 0; i < answers.length; i++) {
            spawnAnswerWithOverlapProtection(answers[i], correctness[i]);
        }

        return q;
    }

    private void spawnAnswerWithOverlapProtection(String text, boolean isCorrect) {
        GlyphLayout tempLayout = new GlyphLayout();
        tempLayout.setText(font, text);

        float padding = 20f;
        float sWidth = Math.max(tempLayout.width + padding * 2, 80f);
        float sHeight = Math.max(tempLayout.height + padding * 2, 50f);

        float spawnX = 0, spawnY = 0;
        boolean invalidPosition;
        int attempts = 0;
        float minDistanceFromPlayer = 150f;
        float safeZonePadding = 2f;

        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        do {
            invalidPosition = false;
            spawnX = MathUtils.random(250f, worldW - sWidth - 20f);
            spawnY = MathUtils.random(50f, worldH - 200f);

            float playerCenterX = player.getX() + player.getWidth() / 2f;
            float playerCenterY = player.getY() + player.getHeight() / 2f;
            float distToPlayer = Vector2.dst(
                    spawnX + sWidth / 2f, spawnY + sHeight / 2f,
                    playerCenterX, playerCenterY
            );

            if (distToPlayer < minDistanceFromPlayer) {
                invalidPosition = true;
            }

            if (!invalidPosition) {
                if (safeZone != null && safeZone.overlapsRectangle(spawnX, spawnY, sWidth, sHeight, safeZonePadding)) {
                    invalidPosition = true;
                }
            }

            if (!invalidPosition) {
                for (Square other : currentAnswerSquares) {
                    float buffer = 50f;
                    if (spawnX < other.getX() + other.getWidth() + buffer &&
                            spawnX + sWidth + buffer > other.getX() &&
                            spawnY < other.getY() + other.getHeight() + buffer &&
                            spawnY + sHeight + buffer > other.getY()) {
                        invalidPosition = true;
                        break;
                    }
                }
            }
            attempts++;
        } while (invalidPosition && attempts < 100);

        Square square = new Square(spawnX, spawnY, sWidth, sHeight, text, isCorrect);
        entityManager.addEntity(square);
        currentAnswerSquares.add(square);
    }

    // ---------------------------
    // POWER-UP SPAWNING
    // ---------------------------

    /**
     * Checks the current streak and spawns/clears power-up pickups
     * at the appropriate streak milestones.
     */
    public void handleStreakPowerUpSpawns(int streakNow) {
        if (streakNow < prevStreakForPowerUps) {
            clearPowerUpPickups();
        }
        if (streakNow > prevStreakForPowerUps) {
            int c = PowerUpType.CHERRY.streakRequired;
            int b = PowerUpType.BANANA.streakRequired;
            int w = PowerUpType.WATERMELON.streakRequired;
            if (prevStreakForPowerUps < c && streakNow >= c) {
                spawnPowerUpPickup(PowerUpType.CHERRY);
            }
            if (prevStreakForPowerUps < b && streakNow >= b) {
                spawnPowerUpPickup(PowerUpType.BANANA);
            }
            if (prevStreakForPowerUps < w && streakNow >= w) {
                spawnPowerUpPickup(PowerUpType.WATERMELON);
            }
        }
        prevStreakForPowerUps = streakNow;
    }

    public void clearPowerUpPickups() {
        for (PowerUpPickup p : new ArrayList<>(activePowerUpPickups)) {
            p.disposeTexture();
            entityManager.removeEntity(p);
        }
        activePowerUpPickups.clear();
    }

    public void removePowerUpPickup(PowerUpPickup pickup) {
        activePowerUpPickups.remove(pickup);
    }

    private void spawnPowerUpPickup(PowerUpType type) {
        float size = POWERUP_PICKUP_SIZE;
        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        float spawnX = 0f;
        float spawnY = 0f;
        boolean invalid;
        int attempts = 0;

        do {
            invalid = false;
            spawnX = MathUtils.random(250f, worldW - size - 20f);
            spawnY = MathUtils.random(50f, worldH - 200f);

            float pcx = player.getX() + player.getWidth() / 2f;
            float pcy = player.getY() + player.getHeight() / 2f;
            float cx = spawnX + size / 2f;
            float cy = spawnY + size / 2f;
            if (Vector2.dst(cx, cy, pcx, pcy) < 140f) {
                invalid = true;
            }

            if (!invalid) {
                for (Square s : currentAnswerSquares) {
                    if (rectsOverlap(spawnX, spawnY, size, size,
                            s.getX(), s.getY(), s.getWidth(), s.getHeight(), 35f)) {
                        invalid = true;
                        break;
                    }
                }
            }
            if (!invalid) {
                for (PowerUpPickup p : activePowerUpPickups) {
                    if (rectsOverlap(spawnX, spawnY, size, size,
                            p.getX(), p.getY(), p.getWidth(), p.getHeight(), 25f)) {
                        invalid = true;
                        break;
                    }
                }
            }
            attempts++;
        } while (invalid && attempts < 120);

        PowerUpPickup pickup = new PowerUpPickup(spawnX, spawnY, size, type);
        entityManager.addEntity(pickup);
        activePowerUpPickups.add(pickup);
    }

    // ---------------------------
    // UTILITY
    // ---------------------------

    private static boolean rectsOverlap(float ax, float ay, float aw, float ah,
                                      float bx, float by, float bw, float bh, float pad) {
        return ax < bx + bw + pad
                && ax + aw + pad > bx
                && ay < by + bh + pad
                && ay + ah + pad > by;
    }

    public List<Square> getCurrentAnswerSquares() {
        return currentAnswerSquares;
    }

    public List<PowerUpPickup> getActivePowerUpPickups() {
        return activePowerUpPickups;
    }

    /**
     * Enforces "no enemies inside safe zone" right after the safe zone teleports.
     * We reposition enemies rather than removing them to avoid messing with MovementManager.
     */
    public void enforceEnemiesOutsideSafeZone() {
        if (safeZone == null) return;

        float worldW = viewport.getWorldWidth();
        float worldH = viewport.getWorldHeight();

        for (Entity e : entityManager.getEntitiesSnapshot()) {
            if (!(e instanceof Triangle)) continue;
            Triangle t = (Triangle) e;

            float tW = t.getWidth();
            float tH = t.getHeight();

            if (!safeZone.overlapsRectangle(t.getX(), t.getY(), tW, tH, 0f)) continue;

            float scx = safeZone.getCenterX();
            float scy = safeZone.getCenterY();

            float tcx = t.getX() + tW / 2f;
            float tcy = t.getY() + tH / 2f;

            float dx = tcx - scx;
            float dy = tcy - scy;

            float len2 = dx * dx + dy * dy;
            if (len2 < 1e-4f) {
                dx = MathUtils.random(-1f, 1f);
                dy = MathUtils.random(-1f, 1f);
                len2 = dx * dx + dy * dy;
            }

            float invLen = (float) (1.0 / Math.sqrt(len2));
            dx *= invLen;
            dy *= invLen;

            float halfDiag = (float) (Math.sqrt(tW * tW + tH * tH) / 2f);
            float margin = safeZone.getRadius() + halfDiag + 5f;

            float newCenterX = scx + dx * margin;
            float newCenterY = scy + dy * margin;

            float newX = newCenterX - tW / 2f;
            float newY = newCenterY - tH / 2f;

            newX = MathUtils.clamp(newX, 0f, worldW - tW);
            newY = MathUtils.clamp(newY, 0f, worldH - tH);

            t.setPosition(newX, newY);

            if (t.getMovementComponent() instanceof RandomMovement) {
                ((RandomMovement) t.getMovementComponent()).onSafeZoneChanged();
            }
        }
    }
}