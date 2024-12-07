package ca.ucalgary.seng300.p4.condensation.Networking.Enums;

public enum GameTypes {
    CHECKERS,
    TICTACTOE,
    CONNECT4;

    @Override
    public String toString() {
        return this.name();
    }
}
