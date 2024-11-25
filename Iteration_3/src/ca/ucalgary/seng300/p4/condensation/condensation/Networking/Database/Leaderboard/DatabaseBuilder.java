package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Database.Leaderboard;

public class DatabaseBuilder {
    public static void main(String[] args) {
        Leaderboard leaderboard = new Leaderboard();

        // This should print the top 6 players
        System.out.println(leaderboard.fetchTop5Players().toString());

        // Basically this class is so that we can test the leaderboard stuff

    }
}
