package IA;

import Controleur.Partie;
import Model.Coup;

/**
 * Created by Jean-Baptiste Louvet on 11/03/15.
 */
public class Gaens extends AbstractIA{
    public Gaens(){
        super("Gaens");
    }

    @Override
    public Coup getCoup(Partie p) {
        return alphaBeta(-9999, 9999, p);
    }

    public int getHeuristique(Coup c) {
        return 0;
    }

    private Coup alphaBeta(int alpha, int beta, Partie p){
        int alphaTmp;
        for(Coup c : p.getTousCoups()){
            alphaTmp = alphaBetaVal(c, p, 1, 0, 0, alpha, beta);
            if(alphaTmp > alpha){
                alpha = alphaTmp;
                memoriseCoup(c);
            }
        }

        return getCoupMemorise();
    }

    private int alphaBetaVal(Coup c, Partie p, int profMax, int profActuelle, int coutCumuleActuel, int alpha, int beta) {
        int alphaTemp, betaTemp, val;
        int cout = getHeuristique(c) + coutCumuleActuel;
        Partie pClone = p.clone();
        pClone.appliquerCoup(c);

        if (pClone.estTerminee() || profActuelle == profMax) {
            return cout;
        } else {
            if(p.getJoueurActuel() instanceof Gaens) { //Noeud Max
                alphaTemp = -9999;
                for(Coup nCoup : p.getTousCoups()){
                    val = alphaBetaVal(nCoup, pClone, profMax, profActuelle++, cout, Math.max(alpha, alphaTemp), beta);
                    alphaTemp = Math.max(alphaTemp, val);

                    if(alphaTemp >= beta){
                        return alphaTemp;
                    }
                }
                return alphaTemp;
            } else { //Noeud Min
                betaTemp = 9999;
                for (Coup nCoup : p.getTousCoups()) {
                    val = alphaBetaVal(nCoup, pClone, profMax, profActuelle++, cout, alpha, Math.min(beta, betaTemp));
                    betaTemp = Math.min(betaTemp, val);

                    if(betaTemp <= alpha){
                        return betaTemp;
                    }
                }
                return betaTemp;
            }
        }
    }
}
