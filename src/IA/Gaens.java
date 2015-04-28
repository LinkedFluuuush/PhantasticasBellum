package IA;

import Controleur.Partie;
import Evaluation.HeuristiqueCoup;
import Model.Coup;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    /*public int getHeuristique(Coup c) {
        return 0;
    }*/

    private Coup alphaBeta(int alpha, int beta, Partie p){
        int alphaTmp;
       
        for(Coup c : p.getTousCoups()){
            alphaTmp = alphaBetaVal(c, p, 3, 0, 0, alpha, beta);
            if(alphaTmp > alpha){
                alpha = alphaTmp;
                memoriseCoup(c);
            }
        }

        System.out.println(getCoupMemorise().getAuteur().getProprio().getNom() + ": " + "Coup choisi = " + getCoupMemorise().toString());
        System.out.println("Heuristique du coup : " + HeuristiqueCoup.getHeuristique(getCoupMemorise()));

        return getCoupMemorise();
    }

    private int alphaBetaVal(Coup c, Partie p, int profMax, int profActuelle, int coutCumuleActuel, int alpha, int beta) {
        int alphaTemp, betaTemp, val;
        int profActuelleTemp = profActuelle + 1;
        int cout = coutCumuleActuel;
        Partie pClone = p.clone();
       
        pClone.appliquerCoup(c);
        pClone.tourSuivant();

        if(pClone.getJoueurActuel() instanceof Gaens) { //Noeud Max
            cout = cout + HeuristiqueCoup.getHeuristique(c);

            if (pClone.estTerminee() || profActuelleTemp >= profMax) {
//                System.out.println("Cout de la branche : " + cout);
                return cout;
            }

            alphaTemp = -9999;
            for(Coup nCoup : pClone.getTousCoups()){
                val = alphaBetaVal(nCoup, pClone, profMax, profActuelleTemp, cout, Math.max(alpha, alphaTemp), beta);
                alphaTemp = Math.max(alphaTemp, val);

                if(alphaTemp >= beta){
                    return alphaTemp;
                }
            }
            return alphaTemp;
        } else { //Noeud Min
            cout = cout - HeuristiqueCoup.getHeuristique(c);

            if (pClone.estTerminee() || profActuelleTemp >= profMax) {
//                System.out.println("Cout de la branche : " + cout);
                return cout;
            }

            betaTemp = 9999;
            for (Coup nCoup : pClone.getTousCoups()) {
                val = alphaBetaVal(nCoup, pClone, profMax, profActuelleTemp, cout, alpha, Math.min(beta, betaTemp));
                betaTemp = Math.min(betaTemp, val);

                if (betaTemp <= alpha) {
                    return betaTemp;
                }
            }
            return betaTemp;
        }
    }
}
