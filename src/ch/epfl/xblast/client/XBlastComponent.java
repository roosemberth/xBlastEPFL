/*
 * XBlast, CS108 Programming Project
 *
 * (C) 2016 - 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 *          - 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 * Released under CC BY-NC-SA License: https://creativecommons.org/licenses/
 */


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

/**
 * XBlastComponent
 *
 * This class is shown to the user a GUI.
 * @author 247128 - Roosembert Palacios <roosembert.palacios@epfl.ch>
 * @author 246452 - Pedro Miguel Candeias <pedro.candeiasmartins@epfl.ch>
 */
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

    /**
     * @return this window's preferred size
     */
    @Override public Dimension getPreferredSize(){
        return new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT);
    }

    /**
     * Paint the Graphical Component.
     * @param g0    Graphics Object
     */
    @Override public void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D)g0;
        if(gs != null){
            //DRAW BOARD
            List<Image> boardImages = gs.getBoardImages();
            List<Image> bombsExplImages = gs.getBombsExplImages();
            for(Cell c : Cell.ROW_MAJOR_ORDER){
                Image boardImage = boardImages.get(c.rowMajorIndex());
                if(boardImage != null)
                g.drawImage(boardImage, c.x()*BLOCK_WIDTH, c.y()*BLOCK_HEIGHT, this);
                Image bombImage = bombsExplImages.get(c.rowMajorIndex());
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

    /**
     * Sorts players for drawing:
     *  - First, the players are sorted according to their y-coordinate (from the smallest to the largest).
     *  - Second, when two players have the swame y-coordinate, they are sorted in order of rotation of
     *      players up last player for whom the display is made.
     * @param players   List of players to sort.
     * @param id        ID Of the player to whom the display is made.
     * @return          Sorted list of players for drawing.
     */
    private static List<Player> sortPlayers(List<Player> players, PlayerID id){
        List<Player> playerSorted = new ArrayList<>(players);
        Comparator<Player> positionComparator = (a,b) ->  Integer.compare(a.getPosition().y(), b.getPosition().y());
        Comparator<Player> finalComparator = positionComparator.thenComparing((a,b) -> a.getId() == id ? 1 : (b.getId() == id ? -1 : 0));
        Collections.sort(playerSorted, finalComparator);

        return playerSorted;
    }

    /**
     * Sets gamestate to the specified state and update the GUI.
     * @param gs    Client Gamestate to set.
     * @param id    Client player id
     */
    public void setGameState(GameState gs, PlayerID id){
        this.gs = gs;
        this.id = id;
        repaint();
    }

}
