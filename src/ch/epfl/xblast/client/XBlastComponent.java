package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

public final class XBlastComponent extends JComponent{
    
    private static final int ICON_WIDTH = 48;
    private static final int ICON_HEIGTH= 48;
    private static final int BLOCK_WIDTH = 64;
    private static final int BLOCK_HEIGHT = 48;
    private static final int LED_WIDTH = 16;
    
    private static final int COMPONENT_WIDTH = 960;
    private static final int COMPONENT_HEIGHT = 688;
    
    private GameState gs = null;
    private PlayerID id;
    
    public Dimension getPreferredSize(){
        return new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT);
    }
    
    public void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D)g0;
        if(gs != null){
            //DRAW BOARD
            List<Image> boardImages = gs.getBoardImages();
            List<Image> bombsExplImages = gs.getBombsExplImages();
            for(Cell c : Cell.ROW_MAJOR_ORDER){
                Image boardImage = boardImages.get(c.x()%Cell.COLUMNS + c.y()*Cell.COLUMNS);
                if(boardImage != null)
                g.drawImage(boardImage, c.x()*BLOCK_WIDTH, c.y()*BLOCK_HEIGHT, this);
                Image bombImage = bombsExplImages.get(c.x()%Cell.COLUMNS + c.y()*Cell.COLUMNS);
                if(bombImage != null)
                    g.drawImage(bombImage, c.x()*BLOCK_WIDTH, c.y()*BLOCK_HEIGHT, this);
                
            }
            //DRAW PLAYERS
            List<Player> players = gs.getPlayers();
            List<Player> sortedPlayers = sortPlayers(players, id);
            for(Player p :sortedPlayers){
                Image image = p.getImage();
                if(image != null)
                    g.drawImage(image, 4*p.getPosition().x()-24, 3*p.getPosition().y()-52, this);
            }
            //DRAW SCORE
            List<Image> scoreImages = gs.getScoreImages();
            for(int x = 0; x <  COMPONENT_WIDTH/ICON_WIDTH; x++){
               Image image = scoreImages.get(x);
               if(image!= null){
                   g.drawImage(image, x*ICON_WIDTH, Cell.ROWS*BLOCK_HEIGHT, this);
               }
            }
            
            //Draw lives
            Font font = new Font("Arial", Font.BOLD, 25);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(Integer.toString(players.get(0).getLives()), 96, 659);
            g.drawString(Integer.toString(players.get(1).getLives()), 240, 659);
            g.drawString(Integer.toString(players.get(2).getLives()), 768, 659);
            g.drawString(Integer.toString(players.get(3).getLives()), 912, 659);
            
            List<Image> timeImages = gs.getTimeImages();
            for(int x = 0; x < COMPONENT_WIDTH/LED_WIDTH; x++){
                Image image = timeImages.get(x);
                if(image!= null){
                    g.drawImage(image, x*LED_WIDTH, Cell.ROWS*BLOCK_HEIGHT+ICON_HEIGTH, this);
                }
             }
        }
    }
    private static List<Player> sortPlayers(List<Player> players, PlayerID id){
        List<Player> playerSorted = new ArrayList<>(players);
        Comparator<Player> positionComparator = (a,b) ->  Integer.compare(a.getPosition().y(), b.getPosition().y());
        Comparator<Player> finalComparator = positionComparator.thenComparing((a,b) -> a.getId() == id ? 1 : (b.getId() == id ? -1 : 0));
        Collections.sort(playerSorted, finalComparator);
        
        return playerSorted;
    }
    public void setGameState(GameState gs, PlayerID id){
        this.gs = gs;
        this.id = id;
        repaint();
    }
    
}
