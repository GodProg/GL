/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Map.Tiles;

import CoreUtils.FVector2;
import GraFX.USpriteSheet;
import Map.Tiles.Blocks.UMapBlock;
import Map.Tiles.Blocks.UPropsBlock;
import PhysiX.UCollisionHandler;

import java.awt.*;
import java.util.ArrayList;

public class UTileLayerPropsLayer extends UMapTileLayer {

    private int TileWidth;
    private int TileHeight;
    private int Height;
    /**
     * @deprecated old method
     */
    private ArrayList<UMapBlock> TileBlocks;

    /**
     * Our block
     */
    private UMapBlock[] TileBlocksOptimized;

    private UTileLayerPropsLayer() {
    }

    /**
     * Default constructor
     *
     * @param Visual      The Tileset
     * @param InBoundData The XML Data read To build our layer
     * @param W           The Width of the tileset
     * @param H           The Height of the tileset
     * @param Tilew       The Width of each tile
     * @param Tileh       The Height of each tile
     * @param Columns     The number of columns in this layer
     */
    public UTileLayerPropsLayer(USpriteSheet Visual, String InBoundData, int W, int H, int Tilew, int Tileh, int Columns) {

        TileWidth = Tilew;
        TileHeight = Tileh;
        Height = H;

        TileBlocksOptimized = new UMapBlock[W * H];

        //TileBlocks = new ArrayList<UMapBlock>();

        String[] BlockDataStringArray = InBoundData.split(",");

        for (int CurrentBlockIndex = 0; CurrentBlockIndex < (W * H); CurrentBlockIndex++) {
            /*
             * here we replace all \s by nothing, \s is a whitespace character,
             * we're basically removing all whitespaces.
             * @see https://www.javatpoint.com/java-regex
             */

            int CurrentBlockData = 705;
            try {
                CurrentBlockData = Integer.parseInt(BlockDataStringArray[CurrentBlockIndex].replaceAll("\\s", ""));
            } catch (NumberFormatException e) {
                System.out.println(String.format("Error reading tile indexation at tile (%d,%d)", CurrentBlockIndex % W, CurrentBlockIndex / H));

            }

            /*
             * 0 means that there is no block in at this index on this current layer
             *
             * anything else corresponds to the index on the image of our sprite tilemap
             *
             * the position (0,0) has an index of 1, (0,2) has an index of 2 and so on...
             */
            if (CurrentBlockData != 0) {
                //deprecated method
//                TileBlocks.add(new UPropsBlock(Tilew, Tileh, Visual.GetSubImage((int) ((CurrentBlockData - 1) % Columns),
//                               (int) ((CurrentBlockData - 1) / Columns)), new FVector2((int) (CurrentBlockIndex % W) * Tilew,
//                          (int) (CurrentBlockIndex / H) * Tileh)));

                TileBlocksOptimized[CurrentBlockIndex] = new UPropsBlock(Tilew, Tileh, Visual.GetSubImage((int) ((CurrentBlockData - 1) % Columns),
                        (int) ((CurrentBlockData - 1) / Columns)), new FVector2((int) (CurrentBlockIndex % W) * Tilew,
                        (int) (CurrentBlockIndex / H) * Tileh));
            }
        }

    }

    @Override
    public void RenderTile(Graphics2D g, UCollisionHandler CameraViewPort) {
        //deprecated method
//        for (UMapBlock tileBlock : TileBlocks) {
//            tileBlock.Render(g);
//        }


        /*
         * Define the X and Y coordinate of where to start drawing
         * for optimization purposes!
         *
         * We want to ignore all blocks outside the camera view!
         */
        int XStartPoint = (int) ((CameraViewPort.getPosition().XPos + FVector2.AbsoluteX) / TileWidth);
        int YStartPoint = (int) ((CameraViewPort.getPosition().YPos + FVector2.AbsoluteY) / TileHeight);

        for (int X = XStartPoint; X < XStartPoint + (CameraViewPort.getCollisionWidth() / TileWidth + 1); X++) {
            for (int Y = YStartPoint; Y < YStartPoint + (CameraViewPort.getCollisionHeight() / TileHeight + 1); Y++) {
                if (TileBlocksOptimized[X + (Y * Height)] != null) TileBlocksOptimized[X + (Y * Height)].Render(g);

            }
        }
    }
}
