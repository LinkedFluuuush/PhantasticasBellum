package IA;

import java.util.*;

import Controleur.Partie;
import Model.Action;
import Model.Attaque;
import Model.Coup;
import Model.Personnage;

/**
 * 
 * @author jnokaya
 * Classe Etat : classe modélisant un état de la partie à un instant t
 */
public class Etat {
	Partie partie;
	
	float alpha;
	float beta;
	float valeurUtile;
	
	int cavalier = 1;
	int voleur = 2;
	int guerrier = 5;
	int sorcier = 10;
	
	float[] resultat = new float[2];
	
	float coefPdvRestant, coefNbPersoRestant, coefNbPersoValeurRestant;
	Coup coupRetenuDefinitif;
	Coup coupRetenu;
	Coup coupJoue;
	private List<Personnage> mesPersosDispos;
	
	public Etat(Partie p, Coup coupJoue, float cPdv, float cNbPR, float cNbPVR) {
		this.partie = p;
		this.coupJoue = coupJoue;
		this.coefPdvRestant = cPdv;
		this.coefNbPersoRestant = cNbPR;
		this.coefNbPersoValeurRestant = cNbPVR;
		
	}
	/*
	public Coup getBestCoup(int profondeur, double alpha, double beta, boolean j1) {
		//initialisation des variables
		List<Coup> listeCoups = p.getTousCoups();
		Coup coup;
		Coup bestCoup;
		double alphaTmp;
		double betaTmp;
		
		if (listeCoups.isEmpty()) {
			coup = null;
		}
		else {
			coup = listeCoups.remove(0);
		}
		
		bestCoup = coup;
		
		//Si le noeud est de type max on cherche � maximiser alpha
		if(j1){
			while(coup != null && alpha<beta){
				//TODO cloner partie avant appliquer coup
				Partie pTmp = p.clone();
				pTmp.appliquerCoup(coup);
				alphaTmp = calcule_utilite(profondeur-1, alpha, beta, !j1);
				if (alpha < alphaTmp) {
					alpha = alphaTmp;
					bestCoup = coup;
				}
				if (listeCoups.isEmpty()) {
					coup = null;
				}
				else{
					coup = listeCoups.remove(0);
				}		
			}
		}
		//Si le noeud est de type min on va minimiser beta
		else if(!j1){
			while (coup != null && alpha<beta) {
				//TODO cloner partie avant appliquer coup
				Partie pTmp = p.clone();
				pTmp.appliquerCoup(coup);
				betaTmp = calcule_utilite(profondeur-1, alpha, beta, !j1);
				if (beta > betaTmp) {
					beta = betaTmp;
					bestCoup = coup;
				}	
				if (listeCoups.isEmpty() ){
					coup = null;
				}
				else {
					coup = listeCoups.remove(0);
				}
			}		
		}
		return bestCoup;
	}

	private double calcule_utilite(int profondeur, double alpha, double beta, boolean j1) {
		if(profondeur == 0 || profondeur == 4/*g�rer la profondeur max dynamiquement*) {
			/*
			 * TODO : return p.evaluer(); 
			 * En attendant, on retourne 0 
			 *
			return 0;
		} else {
			
			List<Coup> listeCoups = p.getTousCoups();
			Coup coup;
			
			if (listeCoups.isEmpty()){
				coup = null;
			}
			else {
				coup = listeCoups.remove(0);
			}
			
			if(j1) {
				while (coup != null && alpha<beta) {
					/*
					 * On clone la partie pour simuler le coup que l'on veut effectuer.
					 * En effet, on ne peut pas "revenir en arrière" après avoir effectué un coup.
					 *
					
					Partie partieTemp = p.clone();
					
					/*
					 * On applique le coup. Dans l'arbre alphabeta, on avance d'un cran en profondeur.
					 *
					
					partieTemp.appliquerCoup(coup);
					
					/*
					 * Appel récursif de l'algo, et comparaison avec le alpha actuel.
					 * On prend la valeur maximale.
					 *
					
					alpha = Math.max(alpha, calcule_utilite(profondeur-1, alpha, beta, !j1));
					
					if (listeCoups.isEmpty()) {
						coup = null;
					}
					else {
						coup = listeCoups.remove(0);
					}
				}
				return alpha;
			} else {
				while (coup != null && alpha<beta) {
					Partie partieTemp = p.clone();
					partieTemp.appliquerCoup(coup);
					beta = Math.min(beta, calcule_utilite(profondeur-1, alpha, beta, !j1));	
					
					if (listeCoups.isEmpty()) {
						coup = null;
					}
					else {
						coup = listeCoups.remove(0);
					}
				}
				return beta;				
			}
		}
	}*/
	/**
	 * @author jnokaya
	 * @param profondeur
	 * @param alpha
	 * @param beta
	 * @param monTour
	 * @return alpha
	 * Alphabeta version Jean-Michel Nokaya.
	 */
	public float alphabeta(int profondeur, float alpha, float beta, boolean monTour, float heurActAdv, float heurActNous){
		System.out.println("Alphabeta");
		Etat filsCourant;
		int i = 0;
		
		this.alpha = alpha;
		this.beta = beta;
		
		float alphaCourant;
		float betaCourant;
		
		resultat[0]=heurActNous;
		resultat[1]=heurActAdv;
		
		if(profondeur == 0){
			if(!monTour){
				System.out.println("=================MIN!==================");
				resultat[1] = heuristique();
			}
			else{
				System.out.println("=================MAX!==================");
				resultat[0] = heuristique();
			}
			System.out.println("<===================== HEURISTIQUE GENTIL : " + resultat[0]);
			System.out.println("<===================== HEURISTIQUE MECHANT : " + resultat[1]);
			System.out.println("Coup : " + coupJoue.toString());
			return resultat[1]-resultat[0];
		}else{
			boolean elagage = false;
			
			if(monTour){
				List<Coup> coupsDisponibles = this.partie.getTousCoups();
				List<Coup> attaques = new ArrayList<Coup>();	
		        for (Coup c : coupsDisponibles) {
		            if (contientAttaque(c)) {
		                attaques.add(c);
		            }
		        }
				
				while(!this.partie.estTerminee() && !attaques.isEmpty() && !elagage){
					i++;
					System.out.println("JE SUIS DANS LA BOUCLE ALPHA, tour : " + i);
					Partie partieSuivante = this.partie.clone();
					//Coup coupJoue = coupsDisponibles.remove(0);
					Coup coupJoue = attaques.remove(0);
					partieSuivante.appliquerCoup(coupJoue);
					partieSuivante.tourSuivant();
					filsCourant = new Etat(partieSuivante, coupJoue, this.coefPdvRestant, this.coefNbPersoRestant, this.coefNbPersoValeurRestant);
					
					float h_temp = filsCourant.heuristique();
					if(h_temp > this.heuristique()){
						coupRetenu = coupJoue;
					}
					alphaCourant = filsCourant.alphabeta(profondeur-1, alpha, beta, false, heurActAdv, heurActNous);
					
					if(this.alpha < alphaCourant){
						this.alpha = alphaCourant;
						System.out.println("Nouvel alpha : " + this.alpha);
						this.coupRetenu = filsCourant.coupJoue;
						System.out.println("Coup retenu : " + this.coupRetenu.toString());
					}
					
					if(this.beta <= this.alpha){
						elagage = true;
					}
					
					//this.valeurUtile = this.alpha;
				}
				
				return this.alpha;
			}else{
				List<Coup> coupsDisponibles = this.partie.getTousCoups();
				List<Coup> attaques = new ArrayList<Coup>();
		        for (Coup c : coupsDisponibles) {
		        	if (contientAttaque(c)) {
		                attaques.add(c);
		            }
		        }
				
				while(!this.partie.estTerminee() && !attaques.isEmpty() && !elagage){
					i++;
					System.out.println("JE SUIS DANS LA BOUCLE BETA, tour : " + i);
					Partie partieSuivante = this.partie.clone();
					//Coup coupJoue = coupsDisponibles.remove(0);
					Coup coupJoue = attaques.remove(0);
					partieSuivante.appliquerCoup(coupJoue);
					partieSuivante.tourSuivant();
					filsCourant = new Etat(partieSuivante, coupJoue, this.coefPdvRestant, this.coefNbPersoRestant, this.coefNbPersoValeurRestant);

					float h_temp = filsCourant.heuristique();
					if(h_temp < this.heuristique()){
						coupRetenu = coupJoue;
					}
					
					betaCourant = filsCourant.alphabeta(profondeur-1, alpha, beta, true, heurActAdv, heurActNous);
					
					if(this.beta > betaCourant){
						this.beta = betaCourant;
						System.out.println("Nouveau beta : " + this.beta);
						this.coupRetenu = filsCourant.coupJoue;
						System.out.println("Coup retenu : " + this.coupRetenu);
					}
					
					if(this.beta <= this.alpha){
						elagage = true;
					}
					
					//this.valeurUtile = this.alpha;
				}
				
				return this.beta;
			}
		}
	}
	
	public float heuristique(){
		return this.heuristique_pdvRestant() + 
				this.heuristique_nbPersoRestant() + 
				this.heuristique_nbPersoRestantValeur();
	}
	
	public float heuristique_pdvRestant(){
		float resultat = 0;
		List<Personnage> listePerso =  this.partie.listerEquipesAdverses();
		
		for(int i=0; i<listePerso.size(); i++){
			if(listePerso.get(i).getClasse().equals("Cavalier")){
				resultat += listePerso.get(i).getVie()*cavalier;
			}else if(listePerso.get(i).getClasse().equals("Guerrier")){
				resultat += listePerso.get(i).getVie()*guerrier;
			}else if(listePerso.get(i).getClasse().equals("Voleur")){
				resultat += listePerso.get(i).getVie()*voleur;
			}else if(listePerso.get(i).getClasse().equals("Magicien")){
				resultat += listePerso.get(i).getVie()*sorcier;
			}
		}
		
		return resultat;
	}
	
	public float heuristique_nbPersoRestant(){
		float resultat = 0;
		List<Personnage> listePerso =  this.partie.listerEquipesAdverses();
		
		for(int i=0; i<listePerso.size(); i++){
			if(listePerso.get(i).getClasse().equals("Cavalier")){
				resultat += (listePerso.get(i).getMaxVie()/2)*cavalier;
			}else if(listePerso.get(i).getClasse().equals("Guerrier")){
				resultat += (listePerso.get(i).getMaxVie()/2)*guerrier;
			}else if(listePerso.get(i).getClasse().equals("Voleur")){
				resultat += (listePerso.get(i).getMaxVie()/2)*voleur;
			}else if(listePerso.get(i).getClasse().equals("Magicien")){
				resultat += (listePerso.get(i).getMaxVie()/2)*sorcier;
			}
		}
		
		return resultat;
	}
	
	public float heuristique_nbPersoRestantValeur(){
		List<Personnage> listePerso;
		listePerso =  this.partie.listerEquipesAdverses();
		
		float resultat = 0;
		
		for(int i=0; i<listePerso.size(); i++){
			if(listePerso.get(i).getClasse().equals("Cavalier")){
				resultat += this.cavalier;
			}else if(listePerso.get(i).getClasse().equals("Guerrier")){
				resultat += this.guerrier;
			}else if(listePerso.get(i).getClasse().equals("Voleur")){
				resultat += this.voleur;
			}else if(listePerso.get(i).getClasse().equals("Magicien")){
				resultat += this.sorcier;
			}
		}
		
		return resultat;
	}
	
	protected boolean contientAttaque(Coup c) {
        for (Action a : c.getActions()) {
            if (a instanceof Attaque) {
                if (!((Attaque) a).getCible().getProprio().equals(partie.getJoueurActuel())) {
                    return true;
                }
            }
        }
        return false;
    }
}
