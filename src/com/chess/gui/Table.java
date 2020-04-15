package com.chess.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import org.jetbrains.annotations.NotNull;


public class Table {
    private final JFrame gameFrame;
    private final BoardPannel boardPannel;
    private final Board chessBoard;

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final Dimension TILE_PANEL_DIMENSION =new Dimension(10,10);
    private final static String defaultPieceImagesPath = "/Users/alinahib/Desktop/art/pieces/piece/";


    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    public Table(){
        this.gameFrame=new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar= createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPannel=new BoardPannel();
        this.gameFrame.add(this.boardPannel,BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.chessBoard=Board.createStandardBoard();
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar=new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu= new JMenu("File");
        final JMenuItem openPGN= new JMenuItem("Load PGN File");
        openPGN.addActionListener(e -> System.out.printf("Open up that PGN File!"));
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem= new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }
    //represent board
    private class BoardPannel extends JPanel{
        final List<TilePanel> boardTiles;

        BoardPannel(){
            super(new GridLayout(8,8));
            this.boardTiles= new ArrayList<>();
            for(int i=0;i< BoardUtils.NUM_TILES;i++){
                final TilePanel tilePanel=new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                this.add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }
    //represent tiles
    private class TilePanel extends JPanel{
        private final int tileId;

        TilePanel(final BoardPannel boardPannel,final int tileId){
            super(new GridBagLayout());
            this.tileId=tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColour();
            assignTilePieceIcon(chessBoard);
            validate();
        }
        private void assignTilePieceIcon(Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                try {
                    BufferedImage image=ImageIO.read(new File(defaultPieceImagesPath+chessBoard.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)+
                    board.getTile(this.tileId).getPiece().toString()+".gif"));

                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColour() {
            if (BoardUtils.EIGHTH_RANK[this.tileId]||BoardUtils.SIXTH_RANK[this.tileId]||BoardUtils.FOURTH_RANK[this.tileId]||BoardUtils.SECOND_RANK[this.tileId]){
                setBackground(this.tileId%2==0 ? lightTileColor:darkTileColor);
            }else if(BoardUtils.SEVENTH_RANK[this.tileId]||BoardUtils.FIFTH_RANK[this.tileId]||BoardUtils.THIRD_RANK[this.tileId]||BoardUtils.FIRST_RANK[this.tileId]){
                setBackground(this.tileId%2!=0 ? lightTileColor:darkTileColor);
            }
        }

        /*private void assignTileColour() {
            boolean isLight = ((tileId + tileId / 8) % 2 == 0);
            setBackground(isLight ? lightTileColor : darkTileColor);
        }*/
    }



}
