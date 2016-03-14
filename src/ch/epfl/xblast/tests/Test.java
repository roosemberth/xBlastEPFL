package ch.epfl.xblast.tests;

import ch.epfl.cs108.Sq;

public class Test {

    public static void main(String[] args) {

//        Block __ = Block.FREE;
//        Block XX = Block.INDESTRUCTIBLE_WALL;
//        Block xx = Block.DESTRUCTIBLE_WALL;
//        Block yy = null;
//        Board board = Board.ofQuadrantNWBlocksWalled(
//                Arrays.asList(
//                        Arrays.asList(__, __, __, __, __, xx, __),
//                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
//                        Arrays.asList(__, xx, __, __, __, xx, __),
//                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
//                        Arrays.asList(__, xx, __, xx, __, __, __),
//                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
//        List<Player> lP = new ArrayList<Player>();
//        for(int i = 0; i < 4; i++)
//            lP.add(new Player(PlayerID.values()[i], 1,new Cell(1+(i%2 == 1 ? 12 : 0),1+(i>1 ? 10: 0)), 0,0));
//        GameState gs = new GameState(board, lP);
//        GameStatePrinter.printGameState(gs);
        Sq<Integer> s = Sq.iterate(0, a->a+1).limit(3);
        System.out.println(s.head());
        s = s.tail();
        System.out.println(s.head());
        s = s.tail();
        System.out.println(s.head());
        s = s.tail();
        if(s.isEmpty()) System.out.println("hey");
    }

}
