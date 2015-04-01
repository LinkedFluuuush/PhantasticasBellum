package Evaluation;

import java.util.List;

import Model.Action;
import Model.Attaque2;
import Model.Coup;
import Model.Personnage;
import Model.Sort;

public class HeuristiqueCoup {

	public int getHeuristique(Coup c, String nomJoueur) {
		int valeur=0;
		List<Action> actions = c.getActions();
		
		//Pour toutes les actions
		for(Action action : actions) {
			//Pour toutes les attaques
			if(action instanceof Attaque2) {
				Attaque2 a = (Attaque2) action;
				
				Sort s = a.getSort();
				List<Personnage> cibles = a.getCibles(); //getCibles retourne List<Personnage>
				
				//Pour chaque personnage ciblé par l'attaqye
				for(Personnage cible : cibles) {
					String nom = cible.getProprio().getNom();
					
					//Si le personnage appartient au joueur spécifié
					if(nom==nomJoueur){
						valeur-=s.getDegat();
					} else {
						valeur+=s.getDegat();
					}
				}
			}
		}
		
		return valeur;
	}
	
}
