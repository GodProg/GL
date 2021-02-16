/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Map.Tiles.Blocks;

import CoreUtils.FVector2;
import PhysiX.UCollisionHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A block on the map that blocks entities movement
 */
public class UCollisionBlock extends UMapBlock {

    /**
     * constructor
     *
     * @param width         block width
     * @param height        block height
     * @param blockDisplay  block image
     * @param blockPosition block world position
     */
    public UCollisionBlock(int width, int height, BufferedImage blockDisplay, FVector2 blockPosition) {
        super(width, height, blockDisplay, blockPosition);
    }

    @Override
    public boolean IsCollisionBlock(UCollisionHandler Other) {
        return true;
    }

    public void Render(Graphics2D g) {
        super.Render(g);
        //g.setColor(Color.cyan);
        //g.drawRect((int)BlockPosition.GetScreenPosition().XPos,(int)BlockPosition.GetScreenPosition().YPos,Width,Height);
    }
}
