/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Map.Tiles.Blocks;

import CoreUtils.FVector2;
import PhysiX.UCollisionHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Simple block that has no interaction
 * <p>
 * Visual only
 */
public class UPropsBlock extends UMapBlock {

    /**
     * constructor
     *
     * @param width         block width
     * @param height        block height
     * @param blockDisplay  block image
     * @param blockPosition block world position
     */
    public UPropsBlock(int width, int height, BufferedImage blockDisplay, FVector2 blockPosition) {
        super(width, height, blockDisplay, blockPosition);
    }

    @Override
    public boolean IsCollisionBlock(UCollisionHandler Other) {
        return false;
    }

    public void Render(Graphics2D g) {
        super.Render(g);
        //g.setColor(Color.green);
        //g.drawRect((int)BlockPosition.GetScreenPosition().XPos,(int)BlockPosition.GetScreenPosition().YPos,Width,Height);
    }
}
