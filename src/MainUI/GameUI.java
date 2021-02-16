/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package MainUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;


/**
 * The main game frame
 */
public class GameUI extends JFrame {

    public GameUI() {
        setTitle("RPG");
        File WindowIcon = new File("Resources\\Sprites\\PackCharacterPixelArt09\\PackCharacterPixelArt09\\NPC01\\Idle\\NPC01_Idle_16.png");

        if (WindowIcon.exists()) {
            try {
                setIconImage(ImageIO.read(WindowIcon));
            } catch (IOException e) {
                System.out.println(e);
            }
        } else System.out.printf("GameUI - Failed To Load Window Icon, File [%s] Does not exist!\n", WindowIcon);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(new URPGPanel(1400, 800));
        setVisible(true);
        pack();

        //center the window on the screen
        setLocationRelativeTo(null);
    }
}
