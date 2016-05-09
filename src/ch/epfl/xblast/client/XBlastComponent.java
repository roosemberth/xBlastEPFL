package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

public final class XBlastComponent extends JComponent{
    private final static int PREFERRED_LENGTH = 960;
    private final static int PREFERRED_HEIGHT = 688;
    
    private GameState gs = null;
    private PlayerID player = null;

    @Override public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_LENGTH, PREFERRED_HEIGHT);
    }
    
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Nothing to do
        if (this.gs==null)
            return;
        
        List<Cell> spiralCells = Cell.SPIRAL_ORDER;
        spiralCells.stream().forEachOrdered(c -> {
            // Paint this cell
            Image block = gs.boardImages.get(spiralCells.indexOf(c));
            int xPos = block.getWidth(null)*c.x();
            int yPos = block.getHeight(null)*c.y();
            
            g.drawImage(block, xPos, yPos, null);
        });
        
        List<Cell> rowCells = Cell.ROW_MAJOR_ORDER;
        rowCells.stream().forEachOrdered(c -> {
            Image expl = gs.bombsExplImages.get(rowCells.indexOf(c));
            // Nothing to draw
            if (expl == null) 
                return;

            int xPos = expl.getWidth(null)*c.x();
            int yPos = expl.getHeight(null)*c.y();
            
            g.drawImage(expl, xPos, yPos, null);
        });
        
        for (int i = 0; i<gs.scoreImages.size(); ++i){
            Image scImg = gs.scoreImages.get(i);
            
            int xPos = i * scImg.getWidth(null);
            int yPos = Cell.ROWS * gs.boardImages.get(0).getHeight(null);
            
            g.drawImage(scImg, xPos, yPos, null);
        }

        for (int i = 0; i<gs.timeImages.size(); ++i){
            Image tImg = gs.timeImages.get(i);
            int xPos = i * tImg.getWidth(null);
            int yPos = Cell.ROWS * gs.boardImages.get(0).getHeight(null) + gs.scoreImages.get(0).getHeight(null);
            
            g.drawImage(tImg, xPos, yPos, null);
        }
        
        Comparator<Player> yPositionComparator = new Comparator<Player>() {
            @Override public int compare(Player o1, Player o2) {
                return Integer.compare(o1.position.y(), o2.position.y());
            }
        };

        Comparator<Player> roundRobinComparator = new Comparator<Player>() {
            @Override public int compare(Player o1, Player o2) {
                return Integer.compare(
                        PlayerID.rotationOrder(player).indexOf(o1.id),
                        PlayerID.rotationOrder(player).indexOf(o2.id)
                        );
            }
        };
        
        gs.players.stream().sorted(yPositionComparator.thenComparing(roundRobinComparator)).forEachOrdered(p -> {
            Image img = p.image;
            int xPos = p.position.x()*4-24;
            int yPos = p.position.y()*3-52;

            g.drawImage(img, xPos, yPos, null);
            
            switch (p.id){
            case PLAYER_1:
                xPos = 96; yPos = 659;
                break;
            case PLAYER_2:
                xPos = 240; yPos = 659;
                break;
            case PLAYER_3:
                xPos = 768; yPos = 659;
                break;
            case PLAYER_4:
                xPos = 912; yPos = 659;
                break;
            default:
                return;
            }
            Font font = new Font("Arial", Font.BOLD, 25);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(String.format("%1d", p.lives), xPos, yPos);
        });
    }
    
    public void setGameState(GameState gs, PlayerID pid){
        this.gs = gs;
        this.player = pid;
        this.repaint();
    }
}
