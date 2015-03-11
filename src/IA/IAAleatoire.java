package IA;

import Controleur.Partie;
import Model.Coup;

import java.util.List;

/**
 * Created by Jean-Baptiste Louvet on 11/03/15.
 */
public class IAAleatoire extends IA {
    /**
     * Constructeur
     */
    public IAAleatoire() {
        super("IA aleatoire");
    }

    /**
     * Recherche le coup a jouer
     * @param p Partie en cours
     * @return un coup
     */
    @Override
    public Coup getCoup(Partie p) {
        List<Coup> coups = p.getTousCoups();
        return coups.get((int) (Math.random()*(coups.size()-1)));
    }
}
