package se.hig.oodp.b9;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.hig.oodp.b9.client.ClientGame;
import se.hig.oodp.b9.client.ClientNetworkerSocket;
import se.hig.oodp.b9.client.GameWindow;
import se.hig.oodp.b9.server.ServerGame;
import se.hig.oodp.b9.server.ServerNetworkerSocket;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        test();
    }

    public static void test() throws IOException, InterruptedException
    {
        ServerGame serverGame;
        List<ClientGame> clientGame;

        int port = 59440;

        serverGame = new ServerGame(new ServerNetworkerSocket(port));

        clientGame = new ArrayList<ClientGame>();
        for (int i = 0; i < 4; i++)
        {
            clientGame.add(new ClientGame(new Player("Player " + i), new ClientNetworkerSocket("127.0.0.1", port)).sendGreeting());
            serverGame.playerAdded.waitFor();
        }

        Thread.sleep(1000);

        serverGame.rules = new Rules();
        serverGame.newGame();

        GameWindow.start(clientGame.get(0));

        for (ClientGame game : clientGame)
        {
            if (game.getTable() == null)
                game.getNetworker().onTable.waitFor();
        }

        Thread.sleep(1000);

        for (int turn = 0; turn < 4; turn++)
        {
            for (ClientGame game : clientGame)
            {
                game.makeMoveAndWait(new Move(game.getMyHand().getFirstCard()));
            }
        }

        System.out.println("Client hand size: " + clientGame.get(0).getMyHand().size());

    }

    public static void windows()
    {
        int port = 59440;

        // Server setup

        ServerNetworkerSocket server = null;

        try
        {
            server = new ServerNetworkerSocket(port);
        }
        catch (IOException e)
        {
            System.out.println("Can't create server!\n\t" + e.getMessage());
            System.exit(1);
        }

        ServerGame serverGame = new ServerGame(server);

        // Client setup

        for (int i = 0; i < 4; i++)
        {
            Player me = new Player("Player " + (i + 1));

            ClientNetworkerSocket clientNetworker = null;

            try
            {
                clientNetworker = new ClientNetworkerSocket("127.0.0.1", port);
                clientNetworker.onMessage.add(msg ->
                {
                    System.out.println("Client: " + msg.getMessage() + (msg.getSource() != null ? (" (from: " + msg.getSource() + ")") : ""));
                });
            }
            catch (IOException e)
            {
                System.out.println("Can't listen to server!\n\t" + e.getMessage());
                System.exit(1);
            }

            GameWindow.start(new ClientGame(me, clientNetworker));
        }

        for (int i = 0; i < 4; i++)
            serverGame.playerAdded.waitFor();

        serverGame.rules = new Rules();
        serverGame.newGame();
    }

    public static void pseudos()
    {
        int port = 59440;

        // Server setup

        ServerNetworkerSocket server = null;

        try
        {
            server = new ServerNetworkerSocket(port);
        }
        catch (IOException e)
        {
            System.out.println("Can't create server!\n\t" + e.getMessage());
            System.exit(1);
        }

        ServerGame serverGame = new ServerGame(server);

        // Client setup

        Player me = new Player("MrGNU");

        ClientNetworkerSocket clientNetworker = null;

        try
        {
            clientNetworker = new ClientNetworkerSocket("127.0.0.1", port);
            clientNetworker.onMessage.add(msg ->
            {
                System.out.println("Client: " + msg.getMessage() + (msg.getSource() != null ? (" (from: " + msg.getSource() + ")") : ""));
            });
        }
        catch (IOException e)
        {
            System.out.println("Can't listen to server!\n\t" + e.getMessage());
            System.exit(1);
        }

        GameWindow.start(new ClientGame(me, clientNetworker));

        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e1)
        {
        }

        for (int i = 0; i < 3; i++)
            try
            {
                ClientNetworkerSocket psuedoClientNetworker = new ClientNetworkerSocket("127.0.0.1", port);
                psuedoClientNetworker.sendGreeting(new Player("Pseudo" + i));
            }
            catch (IOException e)
            {
                System.out.println("Can't listen to server!\n\t" + e.getMessage());
                System.exit(1);
            }

        serverGame.rules = new Rules();
        serverGame.newGame();
    }
}
