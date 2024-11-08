package ca.ucalgary.seng300.p4.condensation.condensation.Backend;

public class Vector2 {
    private final int x;
    private final int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds this Vector in R2 with another Vector in R2
     * @param other the other Vector to add with
     * @return the new Vector as a result of the addition
     */
    public Vector2 add(Vector2 other){
        return new Vector2(x + other.x, y + other.y);
    }

    /**
     * Subtracts this Vector in R2 with another Vector in R2 (this - other)
     * @param other the other Vector in R2
     * @return the new Vector as a result of the subtraction
     */
    public Vector2 sub(Vector2 other){
        return new Vector2(x - other.x, y - other.y);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
