package ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs;

public record Vector2(int x, int y) {

    /**
     * Adds this Vector in R2 with another Vector in R2
     *
     * @param other the other Vector to add with
     * @return the new Vector as a result of the addition
     */
    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    /**
     * Subtracts this Vector in R2 with another Vector in R2 (this - other)
     *
     * @param other the other Vector in R2
     * @return the new Vector as a result of the subtraction
     */
    public Vector2 sub(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }
}
