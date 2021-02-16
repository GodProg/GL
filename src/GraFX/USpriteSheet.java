/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package GraFX;

import CoreUtils.FVector2;
import CoreUtils.UMasterFunctionLibrary;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;


/**
 * This class represents the sheet of an entity in our game
 * <p>
 * it will store an image containing all the entity's animations
 * and split them into different arrays for indexation purposes
 * <p>
 * This class reads the sprite sheets from left to right and top to bottom
 */
public class USpriteSheet {

    /**
     * The imagine containing all the images
     */
    private BufferedImage SpriteSheet = null;

    /**
     * All the animations Split into arrays
     */
    private BufferedImage[][] Sprite2DArray;

    /**
     * Default tile size of each sub-picture of our sprite sheet
     */
    private final int DefaultTileSize = 64;

    /**
     * The Sub-picture width
     */
    public int Width;
    /**
     * The Sub-picture height
     */
    public int Height;

    /**
     * How many sprites do we have from left to right at most
     */
    private int XNumberOfSprites;
    /**
     * How many sprites do we have from top to bottom at most
     */
    private int YNumberOfSprites;

    /**
     * We Assume that our sprite sheet is completely filled and does not have blank spaces on the right side
     */
    private boolean IsFilledSheet = true;

    public USpriteSheet(String FilePath) {
        Width = DefaultTileSize;
        Height = DefaultTileSize;


        System.out.printf("Loading Sprite Sheet with location : %s\n", FilePath);

        SpriteSheet = LoadSheet(FilePath);
        XNumberOfSprites = SpriteSheet.getWidth() / Width;
        YNumberOfSprites = SpriteSheet.getHeight() / Height;
        LoadSheetArray();


    }

    public USpriteSheet(String FilePath, int width, int height, boolean isFilledSheet) {
        Width = width;
        Height = height;
        IsFilledSheet = isFilledSheet;

        System.out.printf("Loading Sprite Sheet with location : %s\n", FilePath);

        SpriteSheet = LoadSheet(FilePath);
        XNumberOfSprites = SpriteSheet.getWidth() / Width;
        YNumberOfSprites = SpriteSheet.getHeight() / Height;
        LoadSheetArray();
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    /**
     * Load a sprite sheet
     *
     * @param Path the sprite path
     * @return the loaded sheet
     */
    private BufferedImage LoadSheet(String Path) {
        BufferedImage sheet = null;

        try {
            sheet = ImageIO.read(new FileInputStream(Path));
        } catch (Exception e) {
            System.out.printf("Error Loading SpriteSheet!\nTrace: %s\nInput Path: %s\n", e.getMessage(), Path);
            e.printStackTrace();
        }

        return sheet;
    }

    /**
     * Split all the sheet animations into different arrays
     */
    private void LoadSheetArray() {
        Sprite2DArray = new BufferedImage[YNumberOfSprites][XNumberOfSprites];
        for (int y = 0; y < YNumberOfSprites; y++) {
            for (int x = 0; x < XNumberOfSprites; x++) {
                if (!IsFilledSheet) {
                    if (!IsFrameValid(GetSubImage(x, y))) {
                        Sprite2DArray[y][x] = null;
                        break;
                    }
                }
                Sprite2DArray[y][x] = GetSubImage(x, y);
            }
        }
    }

    /**
     * Check if the current frame is valid
     *
     * @param img the frame to check
     * @return true if the image is not completely transparent
     */
    private boolean IsFrameValid(BufferedImage img) {
        return !VerifyFrameIntegrity(img);
    }

    /**
     * Check if the current frame is valid
     *
     * @param img the frame to check
     * @return true if the image is completely transparent
     */
    private boolean VerifyFrameIntegrity(BufferedImage img) {
        BufferedImage blank = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int height = img.getHeight();
        int width = img.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (blank.getRGB(x, y) != img.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Get the sheet
     *
     * @return The loaded sprite sheet
     */
    public BufferedImage GetSpriteSheet() {
        return SpriteSheet;
    }

    /**
     * Get a full animation sequence from an index
     *
     * @param SequenceIndex the animation sequence index
     * @return the entire sequence of the given index
     */
    public BufferedImage[] GetSpriteSequence(int SequenceIndex) {
        return Sprite2DArray[SequenceIndex];
    }

    /**
     * Get the whole data
     *
     * @return the whole sprite data
     */
    public BufferedImage[][] GetSprite2DArray() {
        return Sprite2DArray;
    }


    /**
     * get a sub-image from the sprite-sheet
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @return the sub-picture
     */
    public BufferedImage GetSubImage(int x, int y) {
        return SpriteSheet.getSubimage(x * Width, y * Height, Width, Height);
    }

    /**
     * Helper function to draw a whole sequence at once
     *
     * @param g        the graphics
     * @param sequence the sequence to draw
     * @param position the position to draw
     * @param width    the width
     * @param height   the height
     * @param XOffset  the space in the X axis between each picture
     * @param YOffset  the space in the Y axis between each picture
     * @deprecated this function serves no purpose unless its used to draw text form a sprite sheet
     */
    @Deprecated
    public static void DrawSequence(Graphics2D g, ArrayList<BufferedImage> sequence, FVector2 position, int width, int height, int XOffset, int YOffset) {
        float XPos = position.XPos;
        float YPos = position.YPos;

        for (BufferedImage CurrentFrame : sequence) {
            if (CurrentFrame != null) {
                g.drawImage(CurrentFrame, (int) XPos, (int) YPos, width, height, null);
            }
            XPos += XOffset;
            YPos += YOffset;
        }
    }


    @Override
    public String toString() {
        String out = "USpriteSheet{" +
                "SpriteSheet=" + SpriteSheet +
                ", DefaultTileSize=" + DefaultTileSize +
                ", Width=" + Width +
                ", Height=" + Height +
                ", XNumberOfSprites=" + XNumberOfSprites +
                ", YNumberOfSprites=" + YNumberOfSprites +
                ", IsFilledSheet=" + IsFilledSheet + "\n\tData{\n";


        for (int y = 0; y < Sprite2DArray.length; y++) {
            out += String.format("\t\tElement [%d] has [%d] frames \n", y, UMasterFunctionLibrary.CountValidArrayElements(Sprite2DArray[y]));
        }
        out += "\t}\n}\n";
        return out;
    }
}
