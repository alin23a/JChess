package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.concurrent.CancellationException;

public class King extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES={-9,-8,-7,-1, 1, 7, 8, 9};
    public King(final Alliance pieceAlliance,final int piecePosition) {
        super(PieceType.KING, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves= new ArrayList<>();

        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordiante=this.piecePosition+currentCandidateOffset;
            //edge cases

            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
                    ||isEightColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }

            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordiante)){
                final Tile candidateDestinationTile= board.getTile(candidateDestinationCoordiante);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordiante));
                }else{

                    final Piece pieceAtDestination= candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance= pieceAtDestination.getPieceAlliance();

                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordiante, pieceAtDestination));
                    }
                }
            }



        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    @Override
    public King movePiece(final Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    public static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet){
        return BoardUtils.FIRST_COLUMN[currentPosition] &&(candidateOffSet==-9 || candidateOffSet==-1
                || candidateOffSet==7);
    }

    public static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffSet){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] &&(candidateOffSet==-7 || candidateOffSet==1
                ||candidateOffSet==9);
    }
}
