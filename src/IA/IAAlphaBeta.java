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
        return alphaBeta(-9999, 9999, p);
    }

    @Override
    public int getHeuristique(Coup c) {
        return 0;
    }

    private Coup alphaBeta(int alpha, int beta, Partie p){
        int alphaTmp;
        for(Coup c : p.getTousCoups()){
            alphaTmp = alphaBetaMin(c, p, 1, 0, 0, alpha, beta);
            if(alphaTmp > alpha){
                alpha = alphaTmp;
                memoriseCoup(c);
            }
        }


        return getCoupMemorise();
    }

    private int alphaBetaMin(Coup c, Partie p, int profMax, int profActuelle, int coutCumuleActuel, int alpha, int beta){
        int vMin = 0, vFils;
        int cout = getHeuristique(c) + coutCumuleActuel;
        Partie pClone = p.clone();
        pClone.appliquerCoup(c);

        if(pClone.estTerminee()){
            return cout;
        } else {
            for(Coup cTmp : p.getTousCoups()){
                vFils = alphaBetaMax(cTmp, p, profMax, profActuelle++, cout, alpha, beta);
                vMin = Math.min(vFils, vMin);
            }
        }

        return vMin;
    }

    private int alphaBetaMax(Coup c, Partie p, int profMax, int profActuelle, int coutCumuleActuel, int alpha, int beta){
        int vMax = 0, vFils;
        int cout = getHeuristique(c) + coutCumuleActuel;
        Partie pClone = p.clone();
        pClone.appliquerCoup(c);

        if(pClone.estTerminee()){
            return cout;
        } else {
            for(Coup cTmp : p.getTousCoups()){
                vFils = alphaBetaMin(cTmp, p, profMax, profActuelle++, cout, alpha, beta);
                vMax = Math.max(vFils, vMax);
            }
        }
        return vMax;
    }
}
