package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.List;

/**
 *  PlayerID Instance associated to each player
 */
public enum PlayerID {
    PLAYER_1,
    PLAYER_2,
    PLAYER_3,
    PLAYER_4;
    
    public static List<PlayerID> rotationOrder(PlayerID p){
        int o = p==null?0:p.ordinal();
        int size = PlayerID.values().length;
        List<PlayerID> ret = new ArrayList<>(size);
        for (int i = 0; i<size; ++i){
            ret.add(PlayerID.values()[(i+o)%size]);
        }
        return ret;
    }
}
