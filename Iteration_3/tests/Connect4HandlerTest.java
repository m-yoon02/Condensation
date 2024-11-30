import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.Connect4Handler;
import ca.ucalgary.seng300.p4.condensation.Backend.MoveData.MoveData;
import org.junit.Test;
import org.junit.Before;
import java.util.UUID;
import static org.junit.Assert.*;


public class Connect4HandlerTest {

    UUID firstPlayerID;
    UUID secondPlayerID;
    Connect4Handler connect4Handler;

    @Before
    public void setUp() {
        firstPlayerID = UUID.randomUUID();
        secondPlayerID = UUID.randomUUID();
        connect4Handler = new Connect4Handler(firstPlayerID, secondPlayerID);
    }

    @Test
    public void winInColumn() {
        for (int i = 0; i < 4; i++) {
            Vector2 movePosition = new Vector2(0, 0);
            MoveData moveData = new MoveData(firstPlayerID, movePosition);
            connect4Handler.makeMove(moveData);
        }

        // Verify that the game detects a win for the first player
        assertTrue(connect4Handler.winInColumn(0, connect4Handler.getPlayerColorFromUUID(firstPlayerID)));
    }

    @Test
    public void WinInRow() {
        for (int i = 0; i < 4; i++) {
            Vector2 movePosition = new Vector2(i, 0);
            MoveData moveData = new MoveData(firstPlayerID, movePosition);
            connect4Handler.makeMove(moveData);
        }

        // Verify that the game detects a win for the first player
        assertTrue(connect4Handler.winInRow(0, connect4Handler.getPlayerColorFromUUID(firstPlayerID)));
    }

}
