package IA;

import Controleur.Partie;
import Model.Coup;
import Model.Joueur;
import java.util.List;

/**
 *
 * @author Gwenole Lecorve
 */
public abstract class IA extends Joueur {

    /**
     * Constructeur
     */
    public IA(String nom) {
        super(nom);
    }

    /**
     * Recherche le coup a jouer
     * @param p Partie en cours
     * @return un coup
     */
     public abstract Coup getCoup(Partie p);
    
}
