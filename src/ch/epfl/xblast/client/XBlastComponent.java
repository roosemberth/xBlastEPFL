package ch.epfl.xblast.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

public final class XBlastComponent extends JComponent{
    private GameState gs = null;
    private PlayerID id;
    
    public Dimension getPreferredSize(){
        return new Dimension(960, 688);
    }
    
    public void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D)g0;
        if(gs != null){
            //DRAW BOARD
            List<Image> boardImages = gs.getBoardImages();
            List<Image> bombsExplImages = gs.getBombsExplImages();
            for(int y = 0; y < Cell.ROWS; y++){
                for(int x = 0; x < Cell.COLUMNS; x++){
                    Image boardImage = boardImages.get(x%Cell.COLUMNS + y*Cell.COLUMNS);
                    if(boardImage != null)
                    g.drawImage(boardImage, x*64, y*48, this);
                    Image bombImage = bombsExplImages.get(x%Cell.COLUMNS + y*Cell.COLUMNS);
                    if(bombImage != null)
                        g.drawImage(bombImage, x*64, y*48, this);
                }
            }
            //DRAW PLAYERS
            List<Player> players = gs.getPlayers();
            for(int i = 0; i < 4; i++){
                Image image = players.get(i).image;
                if(image != null)
                    g.drawImage(image, 4*players.get(i).position.x()-24, 3*players.get(i).position.y()-52, this);
            }
        }
    }
    
    public void setGameState(GameState gs, PlayerID id){
        this.gs = gs;
        this.id = id;
        repaint();
    }
    
}
