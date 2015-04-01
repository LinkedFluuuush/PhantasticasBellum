package Evaluation;

import java.util.List;

import Model.Action;
import Model.Attaque;
import Model.Coup;
import Model.Personnage;
import Model.Sort;

public class HeuristiqueCoup {

	public int getHeuristique(Coup c) {
		int valeur=0;
		List<Action> actions = c.getActions();
		
		//Pour toutes les actions
		for(Action action : actions) {
			//Pour toutes les attaques
			if(action instanceof Attaque) {
				Attaque a = (Attaque) action;
				
				Sort s = a.getSort();
				Personnage cible = a.getCible(); //getCibles retourne List<Personnage>
				

				String nom = cible.getProprio().getNom();
				String nomJoueur = c.getAuteur().getProprio().getNom();
				
				//Si le personnage appartient au joueur sp�cifi�
				if(nom==c.getAuteur().getProprio().getNom()){
					valeur-=s.getDegat();
				} else {
					valeur+=s.getDegat();
				}
			}
		}
		
		return valeur;
	}
	
}
