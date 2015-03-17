package IA;

import Controleur.Partie;
import Model.Coup;

/**
 * Created by Jean-Baptiste Louvet on 11/03/15.
 */
public class IAAlphaBeta extends IA {
    public IAAlphaBeta(){
        super("IA Alpha Beta");
    }

    @Override
    public Coup getCoup(Partie p) {
        return alphaBeta(-9999, 9999, 1, 0, p);
    }

    private Coup alphaBeta(int alpha, int beta, int profMax, int profActuelle, Partie p){
        
        return null;
    }
}
