package IA;

import Controleur.Partie;
import Model.Coup;
import Model.Joueur;

/**
 *
 * @author Gwenole Lecorve
 */
public abstract class IA extends Joueur {

    /**
     * Computation time in milliseconds
     */
    public static final int DELAY = 4000;

    private Coup coupMemorise;

    /**
     * Constructeur
     * @param nom Nom du joueur artificiel
     */
    public IA(String nom) {
        super(nom);
        coupMemorise = null;
    }

    /**
     * Renvoie le dernier coup memorise
     *
     * @return un coup
     */
    public final Coup getCoupMemorise() {
        return coupMemorise;
    }

    /**
     * Memorise un coup
     *
     * @param coup Le coup a memoriser
     */
    public final void memoriseCoup(Coup coup) {
        coupMemorise = coup;
    }

    /**
     * Recherche le coup a jouer
     *
     * @param p Partie en cours
     * @return un coup
     */
    public abstract Coup getCoup(Partie p);

}
