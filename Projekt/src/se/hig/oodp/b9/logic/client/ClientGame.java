package se.hig.oodp.b9.logic.client;

import java.util.HashMap;

import se.hig.oodp.b9.communication.Move;
import se.hig.oodp.b9.logic.CardCollection;
import se.hig.oodp.b9.logic.Event;
import se.hig.oodp.b9.logic.Table;
import se.hig.oodp.b9.logic.Two;
import se.hig.oodp.b9.model.Player;

/**
 * The client-side game component
 */
public class ClientGame
{
    /**
     * The client's player
     */
    Player me;

    /**
     * The table
     */
    Table table;

    /**
     * The client's networker
     */
    ClientNetworker networker;

    /**
     * Event invoked on a change
     */
    public final Event<Boolean> onChange = new Event<Boolean>();

    /**
     * Event invoked on change in turn status
     */
    public final Event<Boolean> onTurnStatus = new Event<Boolean>();

    /**
     * Event invoked on the end of the game
     */
    public final Event<Two<HashMap<Player, Integer>, HashMap<Player, Integer>>> onEndGame = new Event<Two<HashMap<Player, Integer>, HashMap<Player, Integer>>>();

    /**
     * Is it this player's turn?
     */
    boolean myTurn = false;

    /**
     * Create new game
     * 
     * @param me
     *            the client's player
     * @param networker
     *            the networker handling the communication
     */
    public ClientGame(Player me, ClientNetworker networker)
    {
        this.me = me;
        this.networker = networker;

        networker.onMove.add(move ->
        {
            table.moveCard(table.getCard(move.getCardId()), table.getCardCollection(move.getCardCollcetionId()));
            onChange.invoke(true);
        });

        networker.onCardInfo.add(two ->
        {
            onChange.invoke(true);
        });

        networker.onTable.add(table ->
        {
            this.table = table;
            onChange.invoke(true);
        });

        networker.onCards.add(cards ->
        {
            table.changeDeck(cards);
        });

        networker.onPlayerTurn.add((player) ->
        {
            onTurnStatus.invoke(myTurn = me.equals(player));
        });

        networker.onMoveResult.add(result ->
        {
            onTurnStatus.invoke(myTurn = !result);
        });

        networker.onEndGame.add(result ->
        {
            onEndGame.invoke(result);
        });
    }

    /**
     * Get the networker
     * 
     * @return the networker
     */
    public ClientNetworker getNetworker()
    {
        return networker;
    }

    /**
     * Get the client's player's hand
     * 
     * @return the hand
     */
    public CardCollection getMyHand()
    {
        return table.getPlayerHand(me);
    }

    /**
     * Get the client's player's points (cards that the player have taken)
     * 
     * @return the client's player's points (cards that the player have taken)
     */
    public CardCollection getMyPoints()
    {
        return table.getPlayerPoints(me);
    }

    /**
     * Check if it is this client's player's turn
     * 
     * @return is it?
     */
    public boolean isMyTurn()
    {
        return myTurn;
    }

    /**
     * Set my turn
     * 
     * @param myTurn
     *            is it?
     */
    public void setMyTurn(boolean myTurn)
    {
        this.myTurn = myTurn;
    }

    /**
     * Get the client's player
     * 
     * @return the player
     */
    public Player getMe()
    {
        return me;
    }

    /**
     * Get the table
     * 
     * @return the table
     */
    public Table getTable()
    {
        return table;
    }

    /**
     * Have we sent a greeting to the server?
     */
    boolean greetingSent = false;

    /**
     * Send greeting to the server
     * 
     * @return this game (for chaining)
     */
    public ClientGame sendGreeting()
    {
        if (greetingSent)
            return this;
        greetingSent = true;

        networker.sendGreeting(me);
        return this;
    }

    /**
     * End this game
     * 
     * @param reason
     *            the reason
     */
    public void end(String reason)
    {
        networker.close(reason);
    }

    /**
     * End this game
     */
    public void end()
    {
        networker.close();
    }

    /**
     * Make a move (send it to the server)
     * 
     * @param move
     *            the move to make
     * @return this game (for chaining)
     */
    public ClientGame makeMove(Move move)
    {
        networker.sendMove(move);
        return this;
    }

    /**
     * Make a move (send it to the server) and wait for validation
     * 
     * @param move
     *            the move to make
     * @return if the server allows the move or not
     */
    public boolean makeMoveAndWait(Move move)
    {
        networker.sendMove(move);
        return networker.onMoveResult.waitFor();
    }
}
