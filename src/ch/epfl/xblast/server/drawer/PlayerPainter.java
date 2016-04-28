package ch.epfl.xblast.server.drawer;

import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState.State;

public class PlayerPainter {
    public static final byte byteForPlayer(Player p, int tick){
        int ret = 0;
        ret += p.id().ordinal() * 20;
        if (p.lifeState().state() == State.DEAD) return (byte) (ret + 14);
        if (p.lifeState().state() == State.DYING) return (byte) (ret + 12 + p.lives()>1?0:1);

        // Offset to player number
        ret += p.direction().ordinal() * 3;

        if (p.lifeState().state() == State.INVULNERABLE)
            if ((tick % 2) == 1)
                ret = 80; // White blinking

        int position = p.direction().isHorizontal()?p.position().x():p.position().y();
        switch (position%4){
        case 0:
        case 2:
            break;
        case 1:
            ret += 1;
            break;
        case 3:
            ret += 3;
            break;
        default:
            System.err.println("Processing invalid value for Player " + p + " on tick " + tick);
        }
        return (byte) ret;
    }
}
