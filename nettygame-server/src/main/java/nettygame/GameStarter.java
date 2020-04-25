package nettygame;

import nettygame.server.GameServer;

public class GameStarter {
    public static void main(String[] args){
        GameServer gameServer=new GameServer();
        gameServer.bind(9999);
    }
}
