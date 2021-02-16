/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Map.Tiles.Blocks;

import CoreUtils.FVector2;
import PhysiX.UCollisionHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a block on our map
 */
public abstract class UMapBlock {
    /**
     * block wifth
     */
    protected int Width;
    /**
     * block height
     */
    protected int Height;
    /**
     * block image
     */
    protected BufferedImage BlockDisplay;
    /**
     * block world position
     */
    protected FVector2 BlockPosition;

    /**
     * constructor
     *
     * @param width         block width
     * @param height        block height
     * @param blockDisplay  block image
     * @param blockPosition block world position
     */
    public UMapBlock(int width, int height, BufferedImage blockDisplay, FVector2 blockPosition) {
        Width = width;
        Height = height;
        BlockDisplay = blockDisplay;
        BlockPosition = blockPosition;
    }

    /**
     * tells if its a collision block
     *
     * @param Other
     * @return true if its a collision block
     */
    public abstract boolean IsCollisionBlock(UCollisionHandler Other);

    public void Render(Graphics2D g) {

        g.drawImage(BlockDisplay, (int) BlockPosition.GetScreenPosition().XPos, (int) BlockPosition.GetScreenPosition().YPos, Width, Height, null);


        /*
         * only draw the blocks if they are within the camera view
         *
         * this little check optimized our game up to ~400%
         *
         * Tested with a target of 120fps:
         *      before this check the game ran in average ~30fps
         *      after this check the game runs in average ~118fps
         *
         * Optimization rate :  118/30 = ~3.93 = 393% optimization rate
         *
         * DEPRECATED ~Optimization has been done with a camera system
         */
//        @Deprecated
//        if (((BlockPosition.GetScreenPosition().XPos< URPGPanel.WIDTH && BlockPosition.GetScreenPosition().XPos>-Width)) &&((BlockPosition.GetScreenPosition().YPos< URPGPanel.HEIGHT && BlockPosition.GetScreenPosition().YPos>-Height))){
//            g.drawImage(BlockDisplay, (int) BlockPosition.GetScreenPosition().XPos, (int) BlockPosition.GetScreenPosition().YPos, Width, Height, null);
//        }
    }
}
