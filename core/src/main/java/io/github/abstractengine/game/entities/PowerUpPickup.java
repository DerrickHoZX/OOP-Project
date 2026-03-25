package io.github.abstractengine.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.abstractengine.entities.CollidableEntity;

/**
 * Collectible streak reward; drawn from a sprite when the asset exists, otherwise a colored disc.
 */
public class PowerUpPickup extends CollidableEntity {

    private final PowerUpType powerUpType;
    private Texture texture;

    public PowerUpPickup(float x, float y, float size, PowerUpType type) {
        super(x, y, size, size);
        this.powerUpType = type;
        this.texture = loadTexture(type, (int) Math.ceil(size));
    }

    private static String texturePathFor(PowerUpType type) {
        switch (type) {
            case CHERRY:
                return "Cherry.png";
            case BANANA:
                return "Banana.png";
            case WATERMELON:
                return "Watermelon.png";
            default:
                throw new IllegalArgumentException("Unknown PowerUpType: " + type);
        }
    }

    private static Texture loadTexture(PowerUpType type, int pixelSize) {
        String path = texturePathFor(type);
        if (Gdx.files.internal(path).exists()) {
            Texture t = new Texture(path);
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            return t;
        }
        return createDiscTexture(type, pixelSize);
    }

    private static Texture createDiscTexture(PowerUpType type, int pixelSize) {
        int n = Math.max(8, pixelSize);
        Pixmap pm = new Pixmap(n, n, Pixmap.Format.RGBA8888);
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fill();

        Color core;
        Color rim;
        switch (type) {
            case CHERRY:
                core = new Color(0.95f, 0.15f, 0.22f, 1f);
                rim = new Color(0.45f, 0.05f, 0.1f, 1f);
                break;
            case BANANA:
                core = new Color(1f, 0.92f, 0.2f, 1f);
                rim = new Color(0.65f, 0.45f, 0.1f, 1f);
                break;
            case WATERMELON:
                core = new Color(0.2f, 0.85f, 0.35f, 1f);
                rim = new Color(0.1f, 0.35f, 0.12f, 1f);
                break;
            default:
                throw new IllegalArgumentException("Unknown PowerUpType: " + type);
        }

        float cx = (n - 1) / 2f;
        float cy = (n - 1) / 2f;
        float r = n / 2f - 1f;

        for (int iy = 0; iy < n; iy++) {
            for (int ix = 0; ix < n; ix++) {
                float dx = ix - cx;
                float dy = iy - cy;
                float d = (float) Math.sqrt(dx * dx + dy * dy);
                if (d <= r) {
                    float t = d / r;
                    float rr = core.r + (rim.r - core.r) * t;
                    float gg = core.g + (rim.g - core.g) * t;
                    float bb = core.b + (rim.b - core.b) * t;
                    pm.setColor(rr, gg, bb, 1f);
                    pm.drawPixel(ix, iy);
                }
            }
        }

        Texture t = new Texture(pm);
        pm.dispose();
        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return t;
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (texture != null) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public void disposeTexture() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
