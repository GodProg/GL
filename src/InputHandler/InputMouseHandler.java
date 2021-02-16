package InputHandler;


import MainUI.URPGPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Note to the team member doing this class
 * The interface MouseListener and MouseMotionListener may be what we're look for
 *
 * @see java.awt.event.MouseListener
 * @see java.awt.event.MouseMotionListener
 * @see java.awt.event.MouseEvent
 * <p>
 * What we need:
 * Mouse X and Y Position
 * Mouse Button Pressed (RMB or LMB or MMB)[maybe an int?]
 * ...[add more as needed]
 * Methode of MouseListener :
 * public abstract void mouseClicked(MouseEvent e);
 * public abstract void mouseEntered(MouseEvent e);
 * public abstract void mouseExited(MouseEvent e);
 * public abstract void mousePressed(MouseEvent e);
 * public abstract void mouseReleased(MouseEvent e);
 */
public class InputMouseHandler implements MouseListener {

    int x, y;

    private int MouseButton;

    private boolean MouseEnter;

    public InputMouseHandler(URPGPanel GameInstance) {
        GameInstance.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        MouseButton = e.getButton();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        MouseButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        MouseButton = e.getButton();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        MouseEnter = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        x = e.getX();
        y = e.getY();

    }

    /**
     * Get MouseButton
     *
     * @return MouseButton
     */
    public int GetMouseButton() {
        return MouseButton;
    }

    /**
     * Get MouseEnter
     *
     * @return MouseEnter
     */
    public boolean GetMouseEnter() {
        return MouseEnter;
    }
}
