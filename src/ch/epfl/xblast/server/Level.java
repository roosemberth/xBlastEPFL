package ch.epfl.xblast.server;

import java.util.Objects;

public final class Level {
	
	public final static SOMETHIGN DEFAULT_LEVEL //BOARD?
	private final BoardPainter bp;
	private final GameState gs;
	
	public Level(BoardPainter bp, GameState gs){
		this.bp = Objects.requireNonNull(bp);
		this.gs = Objects.requireNonNull(gs);
	}
	
	public BoardPainter getBp(){
		return bp;
	}
	
	public GameState getGs(){
		return gs;
	}
}
