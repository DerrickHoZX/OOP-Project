package io.github.abstractengine.collision;

/**
 * Boundary represents the playable area boundaries.
 * Used for checking if entities hit screen edges and should bounce back.
 */
public class Boundary {
    
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;
    
    /**
     * Creates a boundary with specified limits
     * @param minX Left edge (usually 0)
     * @param maxX Right edge (usually screen width)
     * @param minY Bottom edge (usually 0)
     * @param maxY Top edge (usually screen height)
     */
    public Boundary(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
    
    // Getters
    public float getMinX() { return minX; }
    public float getMaxX() { return maxX; }
    public float getMinY() { return minY; }
    public float getMaxY() { return maxY; }
    
    public float getWidth() { return maxX - minX; }
    public float getHeight() { return maxY - minY; }
    
    // Setters
    public void setBounds(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
}