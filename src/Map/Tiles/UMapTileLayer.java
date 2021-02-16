/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package Map.Tiles;

import PhysiX.UCollisionHandler;

import java.awt.*;

/**
 * Abstract Layer
 */
public abstract class UMapTileLayer {

    /**
     * Renders the whole layer
     *
     * @param g              the graphics to render on
     * @param CameraViewport the world camera viewport
     */
    public abstract void RenderTile(Graphics2D g, UCollisionHandler CameraViewport);
}
