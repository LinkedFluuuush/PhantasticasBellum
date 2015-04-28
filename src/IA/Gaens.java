package IA;

import java.util.List;

import Controleur.Partie;
import Model.Action;
import Model.Attaque;
import Model.Coup;
import Model.Joueur;
import Model.Personnage;
import Model.Sort;

/**
 * Created by Jean-Baptiste Louvet on 11/03/15.
 */
public class Gaens extends AbstractIA{
	
	static int idGaens = 0;
	
    public Gaens(){
    	super("Gaens" + (idGaens==0?"":"_"+idGaens));
    	idGaens++;
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

        List<Coup> tousCoups = p.getTousCoups();
        memoriseCoup(tousCoups.get(0));
       
        for(Coup c : p.getTousCoups()){
            alphaTmp = alphaBetaVal(c, p, 2, 0, 0, alpha, beta);
            if(alphaTmp > alpha){
                alpha = alphaTmp;
                memoriseCoup(c);
            }
        }

        System.out.println(getCoupMemorise().getAuteur().getProprio().getNom() + ": " + "Coup choisi = " + getCoupMemorise().toString());
        System.out.println("Heuristique du coup : " + alpha);

        return getCoupMemorise();
    }

    private int alphaBetaVal(Coup c, Partie p, int profMax, int profActuelle, int coutCumuleActuel, int alpha, int beta) {
        int alphaTemp, betaTemp, val;
        int profActuelleTemp = profActuelle + 1;
        int cout = coutCumuleActuel;
        Partie pClone = p.clone();
       
        pClone.appliquerCoup(c);
        pClone.tourSuivant();
        Joueur j = pClone.getJoueurActuel();
        if(j instanceof Gaens && j.getNom() == this.getNom()) { //Noeud Max
            cout = cout + getHeuristique(c);

            if (pClone.estTerminee() || profActuelleTemp >= profMax) {
                System.out.println("Cout de la branche : " + cout);
                return cout;
            }

            alphaTemp = -9999;
            for(Coup nCoup : pClone.getTousCoups()){
                val = alphaBetaVal(nCoup, pClone, profMax, profActuelleTemp, cout, Math.max(alpha, alphaTemp), beta);
               
                if(val >= beta){
                    return alphaTemp;
                } else {
                	alphaTemp = Math.max(alphaTemp, val);
                }
            }
            return alphaTemp;
        } else { //Noeud Min
            cout = cout - getHeuristique(c);

            if (pClone.estTerminee() || profActuelleTemp >= profMax) {
//                System.out.println("Cout de la branche : " + cout);
                return cout;
            }

            betaTemp = 9999;
            for (Coup nCoup : pClone.getTousCoups()) {
                val = alphaBetaVal(nCoup, pClone, profMax, profActuelleTemp, cout, alpha, Math.min(beta, betaTemp));
                
                if (val <= alpha) {
                    return betaTemp;
                } else {
                	betaTemp = Math.min(betaTemp, val);
                }
            }
            return betaTemp;
        }
    }
    
    public int getHeuristique(Coup c) {
		
		int valeur=0;
		List<Action> actions = c.getActions();
		
		//Pour toutes les actions
		for(Action action : actions) {
			//Pour toutes les attaques
			if(action instanceof Attaque) {
				Attaque a = (Attaque) action;
				
				Sort s = a.getSort();
				List<Personnage> cibles = a.getPersonnagesAttaques(); //getCibles retourne List<Personnage>
				
				for(Personnage cible : cibles) {
					Joueur nom = cible.getProprio();
					Joueur nomJoueur = c.getAuteur().getProprio();
					
					if(cible.getType() == s.getTypeCible()){
						//Si le personnage appartient au joueur spécifié
						if(nom==nomJoueur){
							valeur-=s.getDegat();
						} else {
							valeur+=s.getDegat();
						}
					}
				}
			}
		}

		//System.out.println("v: "+valeur );
		return valeur;
	}
}
