package GUI.Vue3;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controleur.ControleurFenetre;
import Controleur.ControleurCombat;
import GUI.Vue3.Plateau.*;
import GUI.Vue3.Joueur.*;
import Model.Joueur;

/**
 * 
 * Classe interface qui etend VueJeu et qui va gerer l'affichage lors du combat entre les deux joueurs
 * @author Warlot/Gasquez
 */
public class VueJeuCombat extends VueJeu {
	private JPanel panelSud = null;
	
	/**
	 * Constructeur
	 * Il va instancier les deux parties joueurs a droite et gauche, au centre le plateau, et en bas la console
	 * @param controleurParent controleur parent
	 * @param longueur longueur du plateau
	 * @param largeur largeur du plateau
	 * @param listJoueurs liste de tous les joueurs
	 * @param joueurActuel joueur actuellement actif
	 */
	public VueJeuCombat(ControleurFenetre controleurParent, int longueur, int largeur, List<Joueur> listJoueurs, Joueur joueurActuel){
		super(controleurParent, listJoueurs, joueurActuel);
		
		this.panelJoueur1 = new VueJoueurCombat(getControleur(), listJoueurs.get(0));
		this.panelJoueur2 = new VueJoueurCombat(getControleur(), listJoueurs.get(1));
		this.panelPlateau = new VuePlateauCombat(getControleur(), joueurActuel, longueur, largeur);
		this.panelSud = new JPanel();
		
		panelSud.setLayout(new BoxLayout(panelSud, BoxLayout.Y_AXIS));
		
		panelSud.add(new JLabel("Console de jeu :"));
		
		getPanelPlateau().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				majDimension();
			}
		});

		panelOuestAdd(panelJoueur1);
		panelEstAdd(panelJoueur2);
		panelCenterAdd(panelPlateau);
		panelSudAdd(panelSud);
		
		getControleur().observePersonnages();
		cacherJoueur();
		initPanelJoueurActuel();
		majDimension();
		
		getControleur().afficherPersonnages();
	}

	/**
	 * Ajoute l'element monLabel dans la console dans la partie inferieur de l'application
	 * @param monLabel element JLabel a afficher
	 */
	public void majConsole(JLabel monLabel){
		getPanelSud().add(monLabel);
		getPanelSud().revalidate();
	}
	
	/**
	 * redimentionne le panel sud
	 */
	private void majDimension(){
		getPanelSud().setPreferredSize(new Dimension(
				(int) (getControleur().getControleurParent().getVue().getWidth() * 0.75),
				(int) (getControleur().getControleurParent().getVue().getHeight() * 0.20))
				);
	}
	
	/****************** GETTERS ******************/	
	@Override
	public ControleurCombat getControleur() {
		return (ControleurCombat) controleur;
	}
	public VueJeuCombat getMaVue(){
		return (VueJeuCombat) this;
	}

	@Override
	public VueJoueurCombat getPanelJoueur1() {
		return (VueJoueurCombat) panelJoueur1;
	}

	@Override
	public VueJoueurCombat getPanelJoueur2() {
		return (VueJoueurCombat) panelJoueur2;
	}

	@Override
	public VueJoueurCombat getPanelJoueurActuel() {
		return (VueJoueurCombat) panelJoueurActuel;
	}

	@Override
	public VuePlateauCombat getPanelPlateau() {
		return (VuePlateauCombat) panelPlateau;
	}
	public JPanel getPanelSud() {
		return panelSud;
	}
	
	/****************** SETTERS ******************/
	private void setPanelSud(JPanel panelSud) {
		this.panelSud = panelSud;
	}
	
	@Override
	public void setControleur(ControleurFenetre controleurParent){
		this.controleur = new ControleurCombat(getMaVue(), controleurParent);
	}
}
