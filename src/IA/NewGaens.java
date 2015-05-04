package IA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Controleur.Partie;
import Model.Action;
import Model.Attaque;
import Model.Coup;
import Model.Deplacement;
import Model.Joueur;
import Model.Personnage;
import Model.Position;
import Model.Sort;
import Personnages.Magicien;

/**
 * Created by Jean-Baptiste Louvet and Matthieu Biache on 11/03/15.
 */
public class NewGaens extends AbstractIA{
	
	static int idGaens = 0;
	int profondeur;
	
    public NewGaens(int _profondeur){
    	super("NewGaens" + (idGaens==0?"":"_"+idGaens));
    	idGaens++;
    	profondeur=_profondeur;
    }

    @Override
    public Coup getCoup(Partie p) {
    	memoriseCoup(null);
    	alphaBeta(-9999, 9999, p);
        return getCoupMemorise();
    }

    public synchronized List<Coup> getTousCoups(Partie partie) {
        List<Coup> tousCoups = new ArrayList<Coup>();
        
        // Calculer toutes les cases libres
        List<Position> casesLibres = partie.getToutesPositions();
        for (Personnage perso : partie.listerEquipes()) {
            casesLibres.remove(perso.getPosition());
        }
        
        List<Personnage> tousPersonnages = partie.listerEquipes();
       for (Personnage pf : partie.getJoueurActuel().listerEquipe()) {
           if (pf.estVivant() && !pf.isDejaJoue()) {
               tousCoups.addAll(getTousCoupsPersonnage(partie,pf, casesLibres, tousPersonnages));
            }
        }
       return tousCoups;
    }
    
    private Collection<? extends Coup> getTousCoupsPersonnage(Partie partie,
			Personnage pf, List<Position> casesLibres,
			List<Personnage> tousPersonnages) {
    	List<Coup> coups = new ArrayList<Coup>();
    	
    	if(!pf.isDejaJoue()) {
            // Coup nul
            coups.add(new Coup(pf, new ArrayList<Action>()));
            
            // Deplacements seuls
            List<Deplacement> deplacementsTheoriques = pf.getDeplacements();
            for (Deplacement d : deplacementsTheoriques) {
            	if (partie.isCaseValide(d.getDestination()) && casesLibres.contains(d.getDestination())) {
                    coups.add(new Coup(pf, d));
                }
            }
            
            // Attaques seules
            for (Sort sort : pf.getAttaques()) {
                // Tester si chaque personnage est atteignable
                for (Personnage cible : tousPersonnages) {
                    // Si le personnage est atteignable et en vie
                    if (sort.peutAtteindre(pf.getPosition(), cible.getPosition()) && cible.estVivant() && (cible.getType() == sort.getTypeCible())) {

                        // Construire la liste des cases ciblees avec le personnage cible comme centre
                        List<Position> casesCiblees = sort.getZone().getCasesAccessibles(cible.getPosition());
                        casesCiblees.removeAll(casesLibres);
                        
                        // Chercher les personnages sur ces cases
                        List<Personnage> cibles = new ArrayList<Personnage>();
                        cibles.add(cible);
                        
                        for (Personnage cibleCollaterale : tousPersonnages) {
                            if (cibleCollaterale != cible  && cibleCollaterale.estVivant() && (cibleCollaterale.getType() == sort.getTypeCible())) {
                                if (casesCiblees.contains(cibleCollaterale.getPosition())) {
                                   cibles.add(cibleCollaterale);
                                }
                            }
                        }
                        
                        // Construire le coup correcpondant
                        coups.add(new Coup(pf, new Attaque(sort, cibles)));
                   }
                }
            }
    	}
    	
		return coups;
	}

	private void alphaBeta(int alpha, int beta, Partie p){
        int alphaTmp, valMax = 0;

        List<Coup> tousCoups = getTousCoups(p);

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
//                System.out.println("Cout de la branche : " + cout);
                return cout;
            }

            betaTemp = 9999;
            for (Coup nCoup : getTousCoups(pClone)) {
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
                //System.out.println("Cout de la branche : " + cout);
                return cout;
            }

            alphaTemp = -9999;
            for(Coup nCoup : getTousCoups(pClone)){
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
								vTemp=20;
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
