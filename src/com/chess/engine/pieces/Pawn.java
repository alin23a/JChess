package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.chess.engine.board.Move.*;
import com.google.common.collect.ImmutableList;


public class Pawn extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES={8,16,7,9};
    public Pawn(final Alliance pieceAlliance, int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves= new ArrayList<>();

        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate= this.piecePosition+ (this.pieceAlliance.getDirection()*currentCandidateOffset);

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }

            if(currentCandidateOffset==8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                //TODO more work on later.(deal with promotion)
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }else if(currentCandidateOffset==16&&this.isFirstMove()&&(BoardUtils.SEVENTH_RANK[this.piecePosition]&&this.getPieceAlliance().isBlack())||
                    (BoardUtils.SECOND_RANK[this.piecePosition]&&this.getPieceAlliance().isWhite())){

                final int behindCandidateDestination=this.piecePosition+(this.pieceAlliance.getDirection()*8);
                if(!board.getTile(behindCandidateDestination).isTileOccupied()&&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    //TODO more work on later.
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
            }else if(currentCandidateOffset==7&&
                    !(BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.getPieceAlliance().isWhite()||
                    BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.getPieceAlliance().isBlack())){

                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceonCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance!=pieceonCandidate.getPieceAlliance()){
                        //add attack move
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            }else if(currentCandidateOffset==9&&!(BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.getPieceAlliance().isWhite()||
                    BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.getPieceAlliance().isBlack())){

                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceonCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance!=pieceonCandidate.getPieceAlliance()){
                        //en puessaince
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
}
