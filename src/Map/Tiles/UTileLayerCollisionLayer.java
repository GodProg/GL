/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Map.Tiles;

import CoreUtils.FVector2;
import GraFX.USpriteSheet;
import Map.Tiles.Blocks.UCollisionBlock;
import Map.Tiles.Blocks.UMapBlock;
import PhysiX.UCollisionHandler;

import java.awt.*;
import java.util.HashMap;

public class UTileLayerCollisionLayer extends UMapTileLayer {

    /**
     * We should only have one collision layer
     * we also want to make it accessible anywhere
     *
     * @deprecated this method is very slow and the blocks order may be totally randomized
     * <p>
     * this variable will remain here for progress showcase purposes
     * we had this idea and we've changed our mind
     */
    public static HashMap<String, UMapBlock> CollisionLayer;

    /**
     * our new optimized way of handling a layer
     */
    public static UMapBlock[] CollisionLayerOptimized;

    /**
     * The Width of each tile
     */
    private int TileWidth;
    /**
     * The Height of each tile
     */
    private int TileHeight;

    /**
     * The Image tileset Height
     */
    public static int Height;

    private UTileLayerCollisionLayer() {
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
    public UTileLayerCollisionLayer(USpriteSheet Visual, String InBoundData, int W, int H, int Tilew, int Tileh, int Columns) {

        TileWidth = Tilew;
        TileHeight = Tileh;
        Height = H;

        CollisionLayerOptimized = new UMapBlock[W * H];


        //CollisionLayer = new HashMap<String, UMapBlock>();

        String[] BlockData = InBoundData.split(",");

        for (int BlockDataIndex = 0; BlockDataIndex < (W * H); BlockDataIndex++) {
            // \s are white spaces
            int temp = Integer.parseInt(BlockData[BlockDataIndex].replaceAll("\\s+", ""));

            //if the current data on the array is 0 it means there is no block at this position
            if (temp != 0) {
                FVector2 blockPosition = new FVector2((int) (BlockDataIndex % W) * Tilew, (int) (BlockDataIndex / H) * Tileh);
                UMapBlock CurrentBlock = new UCollisionBlock(Tilew, Tileh, Visual.GetSubImage((int) ((temp - 1) % Columns), (int) ((temp - 1) / Columns)), blockPosition);

                /*
                 * Deprecated Usage
                 */
                //CollisionLayer.put(String.valueOf((int) (BlockDataIndex % W) + "," + String.valueOf((int) (BlockDataIndex / H))), CurrentBlock);

                CollisionLayerOptimized[BlockDataIndex] = CurrentBlock;
            }
        }

    }


    @Override
    public void RenderTile(Graphics2D g, UCollisionHandler CameraViewPort) {
        //deprecated
//        for (UMapBlock tileBlock : CollisionLayer.values()) {
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
                if (CollisionLayerOptimized[X + (Y * Height)] != null)
                    CollisionLayerOptimized[X + (Y * Height)].Render(g);

            }
        }


    }

}
