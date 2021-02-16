/**
 * @BarroisMathias
 */
package PhysiX;

import InputHandler.InputKeyHandler;

public class UMovementComponent {

    //Movement Attributes We do not need more for now
    //Une fonction qui va appler toute les frames : tic, prend en para u n temps delta de type double, delta temps qui c'est ecouler depuis le dernier appel
    //Temps delta sert : mouvement constant malgré le fps
    //fonction tic update de temps réel les variables en dx et dy ( pos perso + delta )
    // en fonction de la direction changer les booléens
    //getteur bool et delta x et y, limité par une vitesse maximal
    //DIAGO priorité en haut a gauche
    /**
     * tells the current State of the movement
     */
    protected boolean IsGoingUp;
    protected boolean IsGoingRight;
    protected boolean IsGoingDown;
    protected boolean IsGoingLeft;

    /**
     * X and Y motion
     */
    protected float dx;
    protected float dy;

    /**
     * Acceleration physics
     */
    protected float Speed = 150;

    /**
     * @param DeltaTime
     * @return
     */

    public void tick(double DeltaTime) {
        //D=V*T, transformer T en seconde
        float Delta;
        /*
         * Delta Converstion
         *
         */
        Delta = (float) DeltaTime / 1000000000;

        /*
         *      D      V         *    T
         */
        if (IsGoingUp) {
            dy = -(Speed * Delta);
        }

        if (IsGoingDown) {
            dy = (Speed * Delta);
        }

        if (IsGoingLeft) {
            dx = -(Speed * Delta);
        }

        if (IsGoingRight) {
            dx = (Speed * Delta);
        }

    }

    /**
     * Demander Erwan pour le input
     */
    public void input(InputKeyHandler key) {

    }

    /**
     * Get dx
     *
     * @return IsGoing
     */
    public float getDx() {
        return dx;
    }

    /**
     * Get dy
     *
     * @return dy
     */
    public float getDy() {
        return dy;
    }

    /**
     * Get IsGoingUp
     *
     * @return IsGoingUp
     */
    public boolean isGoingUp() {
        return IsGoingUp;
    }

    /**
     * Get IsGoingRight
     *
     * @return IsGoingRight
     */
    public boolean isGoingRight() {
        return IsGoingRight;
    }

    /**
     * Get IsGoingDown
     *
     * @return IsGoingDown
     */
    public boolean isGoingDown() {
        return IsGoingDown;
    }

    /**
     * Get Is IsGoingLeft
     *
     * @return IsGoingLeft
     */
    public boolean isGoingLeft() {
        return IsGoingLeft;
    }

    /**
     * Set IsGoingUp
     *
     * @param bol
     */
    public void setIsGoingUp(boolean bol) {
        IsGoingUp = bol;
    }

    /**
     * Set IsGoingDown
     *
     * @param bol
     */
    public void setIsGoingDown(boolean bol) {
        IsGoingDown = bol;
    }

    /**
     * Set IsGoingLeft
     *
     * @param bol
     */
    public void setIsGoingLeft(boolean bol) {
        IsGoingLeft = bol;
    }

    /**
     * Set IsGoingRight
     *
     * @param bol
     */
    public void setIsGoingRight(boolean bol) {
        IsGoingRight = bol;
    }
}
