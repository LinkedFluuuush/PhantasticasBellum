package IA;

import java.util.List;

import Controleur.Partie;
import Model.Action;
import Model.Attaque;
import Model.Coup;
import Model.Joueur;
import Model.Personnage;
import Model.Sort;
import Personnages.Magicien;

/**
 * Created by Jean-Baptiste Louvet and Matthieu Biache on 11/03/15.
 */
public class Gaens extends AbstractIA{
	
	static int idGaens = 0;
	int profondeur=3;
	
    public Gaens(int _profondeur){
    	super("Gaens" + (idGaens==0?"":"_"+idGaens));
    	idGaens++;
    	profondeur=_profondeur;
    }

    public Gaens(){
    	super("Gaens" + (idGaens==0?"":"_"+idGaens));
    	idGaens++;
    }
    
    @Override
    public Coup getCoup(Partie p) {
    	memoriseCoup(null);
    	alphaBeta(-9999, 9999, p);
        return getCoupMemorise();
    }
    
	private void alphaBeta(int alpha, int beta, Partie p){
        int alphaTmp, valMax = 0;

        List<Coup> tousCoups = p.getTousCoups();

        for(Coup c : tousCoups) {
        	if(!c.getAuteur().isDejaJoue()) {
        		alphaTmp = getHeuristique(c);
                if (alphaTmp > valMax) {
                    valMax = alphaTmp;
                    memoriseCoup(c);
                }
        	}
        }

        for(Coup c : tousCoups) {
            alphaTmp = alphaBetaVal(c, p, profondeur, 0, 0, alpha, beta);

            if(alphaTmp > alpha){
                alpha = alphaTmp;
                memoriseCoup(c);
            }
        }
    }

     private int alphaBetaVal(Coup c, Partie p, int profMax, int profActuelle, int coutCumuleActuel, int alpha, int beta) {
        int val,alphaTemp,betaTemp;
        int profActuelleTemp = profActuelle + 1;
        int cout = coutCumuleActuel;
        Partie pClone = p.clone();
       
        pClone.appliquerCoup(c);
        pClone.tourSuivant();
        Joueur j = pClone.getJoueurActuel();
        if(j instanceof Gaens && j.getNom() == this.getNom()) { //Noeud Min : Si c'est Gaens le joueur actuel, alors le coup a été joué par l'adversaire (changement de tour)
            cout = cout - getHeuristique(c);

            if (pClone.estTerminee() || profActuelleTemp >= profMax) {
                return cout;
            }

            betaTemp = 9999;
            for (Coup nCoup : pClone.getTousCoups()) {
            	if(!nCoup.getAuteur().isDejaJoue()) {
            		val = alphaBetaVal(nCoup, pClone, profMax, profActuelleTemp, cout, alpha, Math.min(beta, betaTemp));

                    if (val <= alpha) {
                        return betaTemp;
                    } else {
                        betaTemp = Math.min(betaTemp, val);
                    }
            	} 
            }
            return betaTemp;
        } else { //Noeud Max : Si ce n'est pas Gaens le joueur actuel, alors le coup a été joué par Gaens (changement de tour)
            cout = cout + getHeuristique(c);

            if (pClone.estTerminee() || profActuelleTemp >= profMax) {
                return cout;
            }

            alphaTemp = -9999;
            for(Coup nCoup : pClone.getTousCoups()){
            	if(!nCoup.getAuteur().isDejaJoue()) {
            		val = alphaBetaVal(nCoup, pClone, profMax, profActuelleTemp, cout, Math.max(alpha, alphaTemp), beta);

                    if(val >= beta){
                        return alphaTemp;
                    } else {
                        alphaTemp = Math.max(alphaTemp, val);
                    }
            	}
                
            }
            return alphaTemp;
        }
    }
    
    public int getHeuristique(Coup c) {
		
		int valeur=0;
		List<Action> actions = c.getActions();
		Boolean flagAttaque=false;
		
		//Pour toutes les actions
		for(Action action : actions) {
			//Pour toutes les attaques
			if(action instanceof Attaque) {
				flagAttaque=true;
				Attaque a = (Attaque) action;
				
				Sort s = a.getSort();
				List<Personnage> cibles = a.getPersonnagesAttaques(); //getCibles retourne List<Personnage>
				
				for(Personnage cible : cibles) {
					if(cible.estVivant()) {
						
						Joueur nom = cible.getProprio();
						Joueur nomJoueur = c.getAuteur().getProprio();
						if(cible.getType() == s.getTypeCible()){
							int vTemp;
							//Si le personnage appartient au joueur sp�cifi�
							if(cible instanceof Magicien) {
								vTemp=10;
							} else {
								vTemp=s.getDegat();
							}
							
							if(nom==nomJoueur){
								valeur-=vTemp;
							} else {
								valeur+=vTemp;
							}
						}
					}
				}
			}
		}
		
		if(flagAttaque && valeur==0) {
			valeur=-9999;
		}
		
		return valeur;
	}
}
