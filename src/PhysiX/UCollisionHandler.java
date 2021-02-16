/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package PhysiX;

import CoreUtils.FVector2;
import Map.Tiles.UTileLayerCollisionLayer;


/**
 * Collision Class
 * <p>
 * This Collision is of type AABB (Axis-aligned bounding box -> https://en.wikipedia.org/wiki/AABB)
 * <p>
 * This collision can behave as a simple box or a circle
 */
public class UCollisionHandler {
    /**
     * Where this Collision is located
     * <p>
     * This vector can be attached to another object's vector
     */
    private FVector2 Position;

    /**
     * Local Offset on the X Axis
     */
    private float CollisionXOffset;
    /**
     * Local Offset on the Y Axis
     */
    private float CollisionYOffset;

    /**
     * Collision Width
     */
    private float CollisionWidth;
    /**
     * Collision height
     */
    private float CollisionHeight;

    /**
     * Collision Size
     */
    private float CollisionSize;

    /**
     * Circle collision Radius
     */
    private float CircleCollisionRadius = 0;

    /**
     * Make Box Collision
     *
     * @param position        Collision Spawn Position
     * @param collisionWidth  Collision Width
     * @param collisionHeight Collision height
     */
    public UCollisionHandler(FVector2 position, float collisionWidth, float collisionHeight) {
        Position = position;
        CollisionWidth = collisionWidth;
        CollisionHeight = collisionHeight;

        CollisionSize = Math.max(collisionHeight, collisionWidth);

    }

    /**
     * Make circle collision
     *
     * @param position Collision Position
     * @param Radius   Circle Collision Radius
     */
    public UCollisionHandler(FVector2 position, float Radius) {
        Position = position;
        CircleCollisionRadius = Radius;
    }


    /**
     * @param NewPosition The New Position
     * @param NewWidth    The New Width
     * @param NewHeight   the New height
     * @deprecated Unused method on the current version
     */
    @Deprecated
    public void SetCollision(FVector2 NewPosition, float NewWidth, float NewHeight) {
        Position = NewPosition;
        CollisionWidth = NewWidth;
        CollisionHeight = NewHeight;

        CollisionSize = Math.max(NewWidth, NewHeight);
    }


    /**
     * check if 2 boxes collide
     *
     * @param Other the other box
     * @return true is they're colliding by the rules of AABB
     * @see UCollisionHandler
     */
    public boolean IsColliding(UCollisionHandler Other) {
        float SourceXCenter = ((getPosition().GetScreenPosition().XPos + CollisionXOffset) + (CollisionWidth / 2));
        float SourceYCenter = ((getPosition().GetScreenPosition().YPos + CollisionYOffset) + (CollisionHeight / 2));

        /*
         * Other Collision center pont
         */
        float TargetXCenter = ((Other.getPosition().GetScreenPosition().XPos + Other.CollisionXOffset) + (Other.getCollisionWidth() / 2));
        float TargetYCenter = ((Other.getPosition().GetScreenPosition().YPos + Other.CollisionYOffset) + (Other.getCollisionHeight() / 2));


        return (Math.abs(SourceXCenter - TargetXCenter) < (CollisionWidth / 2) + (Other.CollisionWidth / 2) &&
                Math.abs(SourceYCenter - TargetYCenter) < (CollisionHeight / 2) + (Other.CollisionHeight / 2));

    }

    /**
     * Checks if this collision BOX is about to collide with a blocking object
     *
     * @param XPosition the current owner X position
     * @param YPosition the current owner Y position
     * @return true if its about to collide
     */
    public boolean IsCollidingWithWorld(float XPosition, float YPosition) {

        //loop through each corner of this collision box
        for (int CollisionCorner = 0; CollisionCorner < 4; CollisionCorner++) {

            // here we're simply checking in which tile the object is about to be in
            int CenterX = (int) (((Position.XPos + XPosition) + (CollisionCorner % 2) * CollisionWidth + CollisionXOffset) / 64);
            int CenterY = (int) (((Position.YPos + YPosition) + ((int) CollisionCorner / 2) * CollisionHeight + CollisionYOffset) / 64);

            //check if that tile is a collision tile
            if (UTileLayerCollisionLayer.CollisionLayerOptimized[CenterX + (CenterY * UTileLayerCollisionLayer.Height)] != null) {
                return UTileLayerCollisionLayer.CollisionLayerOptimized[CenterX + (CenterY * UTileLayerCollisionLayer.Height)].IsCollisionBlock(this);
            }

        }

        return false;

    }

    /**
     * Checks is this collision's circle is colliding with another entity that has a rectangle
     *
     * @param Other the other collision
     * @return true if the other's rectangle collision is colliding with this circle
     */
    public boolean IsCircleColliding(UCollisionHandler Other) {

        //based on the formula of  http://www.jeffreythompson.org/collision-detection/circle-rect.php

        float ClosestXPoint = Math.max(Other.getPosition().GetScreenPosition().XPos + Other.getCollisionXOffset(),
                Math.min(Position.GetScreenPosition().XPos + (CircleCollisionRadius / 2),
                        Other.getPosition().GetScreenPosition().XPos + Other.getCollisionXOffset() + Other.getCollisionWidth()));

        float ClosestYPoint = Math.max(Other.getPosition().GetScreenPosition().YPos + Other.getCollisionYOffset(),
                Math.min(Position.GetScreenPosition().YPos + (CircleCollisionRadius / 2),
                        Other.getPosition().GetScreenPosition().YPos + Other.getCollisionYOffset() + Other.getCollisionHeight()));


        ClosestXPoint = Position.GetScreenPosition().XPos + (CircleCollisionRadius / 2) - ClosestXPoint;
        ClosestYPoint = Position.GetScreenPosition().YPos + (CircleCollisionRadius / 2) - ClosestYPoint;

        if (Math.sqrt(ClosestXPoint * ClosestXPoint + ClosestYPoint * ClosestYPoint) < CircleCollisionRadius / 2) {
            return true;
        }
        return false;

    }

    public float getCollisionXOffset() {
        return CollisionXOffset;
    }

    public float getCollisionYOffset() {
        return CollisionYOffset;
    }

    public void setCircleCollisionRadius(float circleCollisionRadius) {
        CircleCollisionRadius = circleCollisionRadius;
    }

    public float getCircleCollisionRadius() {
        return CircleCollisionRadius;
    }

    public FVector2 getPosition() {
        return Position;
    }

    public float getCollisionWidth() {
        return CollisionWidth;
    }

    public float getCollisionHeight() {
        return CollisionHeight;
    }

    public float getCollisionSize() {
        return CollisionSize;
    }

    public void setPosition(FVector2 position) {
        Position = position;
    }

    public void setCollisionXOffset(float collisionXOffset) {
        CollisionXOffset = collisionXOffset;
    }

    public void setCollisionYOffset(float collisionYOffset) {
        CollisionYOffset = collisionYOffset;
    }

    public void setCollisionWidth(float collisionWidth) {
        CollisionWidth = collisionWidth;
    }

    public void setCollisionHeight(float collisionHeight) {
        CollisionHeight = collisionHeight;
    }
}
