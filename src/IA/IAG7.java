package IA;

import Controleur.Partie;
import Model.Coup;

public class IAG7 extends AbstractIA{
	int profondeur = 2;
	
	int cavalier = 1;
	int voleur = 2;
	int guerrier = 5;
	int sorcier = 10;

	public IAG7(String nom) {
		super(nom);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Coup getCoup(Partie p) {
		Etat etat = new Etat(p, null, 1, 1, 1);
		p.joueurSuivant();
		float heurActAdv = etat.heuristique();
		p.joueurSuivant();
		float heurActNous = etat.heuristique();
		System.out.println("<===================== HEURISTIQUE GENTIL INITIAL : " + heurActNous);
		System.out.println("<===================== HEURISTIQUE MECHANT INITIAL : " + heurActAdv);
		etat.alphabeta(profondeur, -9000, 9000, true, heurActAdv, heurActNous);
		System.out.println("<=========================== ALPHA : " + etat.alpha);
		System.out.println("<=========================== BETA : " + etat.beta);
		//System.out.println(etat.coupRetenu.toString());
		return etat.coupRetenu;
	}

}
