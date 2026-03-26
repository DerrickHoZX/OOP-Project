package io.github.abstractengine.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.abstractengine.interfaces.Movable;
import io.github.abstractengine.game.movement.AvoidanceZone;
import io.github.abstractengine.game.movement.EnemyMovementModifiable;
import io.github.abstractengine.game.movement.SafeZoneChangeListener;
import io.github.abstractengine.game.movement.TelegraphState;

import java.util.Random;

public class RandomMovement extends MovementComponent implements TelegraphState, SafeZoneChangeListener, EnemyMovementModifiable {

    private final float baseSpeed;
    private float speed;
    /** Seconds of movement in the current direction before the pre-turn pause. */
    private final float movePhaseDuration;

    private float screenWidth;
    private float screenHeight;
    private float entitySize;

    private final Random random;
    private boolean frozen;

    // Optional obstacle-avoidance.
    private AvoidanceZone safeZone;
    private boolean avoidSafeZone;
    private float safeZonePadding = 0f;

    // How long each "warning" sprite should show during pre-turn pause.
    private static final float TELEGRAPH_STEP_SECONDS = 0.1f;

    /** Seconds to stand still before picking a new direction (telegraph; scaled by difficulty from scene). */
    private float preTurnPauseSeconds;

    private float moveTimer;
    private boolean inPauseBeforeTurn;
    private float pauseTimer;

    public RandomMovement(float speed, float movePhaseDuration, float screenW, float screenH, float entitySize) {
        super();
        this.baseSpeed = speed;
        this.speed = speed;
        this.movePhaseDuration = movePhaseDuration;
        this.screenWidth = screenW;
        this.screenHeight = screenH;
        this.entitySize = entitySize;

        this.random = new Random();
        this.frozen = false;
        this.preTurnPauseSeconds = 1f;

        this.moveTimer = 0f;
        this.inPauseBeforeTurn = false;
        this.pauseTimer = 0f;

        this.safeZone = null;
        this.avoidSafeZone = false;
        pickRandomDirection();
    }

    public void setSafeZone(AvoidanceZone safeZone) {
        this.safeZone = safeZone;
        this.avoidSafeZone = (safeZone != null);
    }

    /**
     * Call when the safe zone moves (teleports) so enemies immediately react.
     */
    @Override
    public void onSafeZoneChanged() {
        if (!avoidSafeZone) return;
        // Reset timers so we don't get stuck waiting out the current phase.
        inPauseBeforeTurn = false;
        pauseTimer = 0f;
        moveTimer = 0f;
        pickRandomDirection();
    }

    /**
     * @return true while the entity is paused before switching direction.
     */
    @Override
    public boolean isInPauseBeforeTurn() {
        return inPauseBeforeTurn;
    }

    /**
     * @return how many seconds we've been in the pause-before-turn state.
     */
    public float getPauseTimerSeconds() {
        return inPauseBeforeTurn ? pauseTimer : 0f;
    }

    /**
     * Telegraphed sprite selection for the pre-turn pause.
     *  - 1 => Enemy1.png
     *  - 2 => Enemy2.png
     *  - 0 => normal Enemy.png
     */
    @Override
    public int getTelegraphSpriteIndex() {
        if (!inPauseBeforeTurn) return 0;
        int step = (int) (pauseTimer / TELEGRAPH_STEP_SECONDS);
        // Alternate: 0 -> Enemy1, 1 -> Enemy2, 2 -> Enemy1, ...
        return (step % 2 == 0) ? 1 : 2;
    }

    @Override
    public void setPreTurnPauseSeconds(float seconds) {
        this.preTurnPauseSeconds = Math.max(0f, seconds);
    }

    /**
     * Applies streak power-up modifiers. Call each frame before {@link #update(Movable, float)}.
     * When {@code frozen} is true, the entity does not move; unfreezing restores or picks a direction.
     */
    @Override
    public void setEnemyModifiers(float speedMultiplier, boolean frozen) {
        boolean wasFrozen = this.frozen;
        this.frozen = frozen;
        this.speed = baseSpeed * speedMultiplier;

        if (frozen) {
            return;
        }

        if (wasFrozen) {
            inPauseBeforeTurn = false;
            pauseTimer = 0f;
            moveTimer = 0f;
            pickRandomDirection();
            return;
        }

        if (inPauseBeforeTurn) {
            return;
        }

        if (velocity.len2() < 1e-4f) {
            pickRandomDirection();
        } else {
            velocity.nor().scl(speed);
        }
    }

    @Override
    public void update(Movable entity, float dt) {
        if (frozen) {
            return;
        }

        if (inPauseBeforeTurn) {
            pauseTimer += dt;
            velocity.setZero();
            if (pauseTimer >= preTurnPauseSeconds) {
                inPauseBeforeTurn = false;
                pauseTimer = 0f;
                pickRandomDirection();
            }
            return;
        }

        moveTimer += dt;

        Vector2 currentPos = entity.getPosition();
        float newX = currentPos.x + velocity.x * dt;
        float newY = currentPos.y + velocity.y * dt;

        if (newX < 0) {
            newX = 0;
            velocity.x = -velocity.x;
        } else if (newX > screenWidth - entitySize) {
            newX = screenWidth - entitySize;
            velocity.x = -velocity.x;
        }

        if (newY < 0) {
            newY = 0;
            velocity.y = -velocity.y;
        } else if (newY > screenHeight - entitySize) {
            newY = screenHeight - entitySize;
            velocity.y = -velocity.y;
        }

        entity.setPosition(newX, newY);

        if (moveTimer >= movePhaseDuration) {
            moveTimer = 0f;
            inPauseBeforeTurn = true;
            pauseTimer = 0f;
            velocity.setZero();
        }

        // Safe-zone avoidance happens after movement/boundary clamping.
        if (avoidSafeZone && safeZone != null) {
            // Use rectangle overlap checks so partial overlap also counts as "inside".
            boolean overlaps = safeZone.overlapsRectangle(
                    entity.getPosition().x,
                    entity.getPosition().y,
                    entitySize,
                    entitySize,
                    safeZonePadding
            );

            if (overlaps) {
                // Reject this step: keep the previous position and change direction.
                // Note: we intentionally do not push the enemy out here;
                // StartScene also enforces "no enemies inside" immediately after relocation.
                entity.setPosition(currentPos.x, currentPos.y);
                moveTimer = 0f;
                inPauseBeforeTurn = false;
                pauseTimer = 0f;
                pickRandomDirection();
            }
        }
    }

    private void pickRandomDirection() {
        float angle = random.nextFloat() * (float) (2 * Math.PI);
        float dirX = (float) Math.cos(angle);
        float dirY = (float) Math.sin(angle);
        velocity.set(dirX, dirY).nor().scl(speed);
    }
}
