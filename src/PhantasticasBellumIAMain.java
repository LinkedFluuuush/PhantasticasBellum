
import Controleur.ControleurPlacement;
import Controleur.GestionnairePartie;
import Exception.ExceptionPersonnage;
import Exception.ExceptionParamJeu;
import GUI.Fenetre;
import GUI.Vue3.VueJeuCombat;
import IA.IA;
import Model.Joueur;
import Model.Personnage;
import Model.Position;
import static java.lang.Math.random;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Classe de lancement d'une partie pour le module d'intelligence artificielle
 *
 * @author Gw�nol� Lecorv�
 */
public class PhantasticasBellumIAMain {

    /**
     * Fixe la composition et la position de l'�quipe d'un joueur
     *
     * @param j Le joueur � initialiser
     * @param p La partie qui va �tre jou�e
     * @param cote C�t� du plateau o� d�butera le joueur
     */
    public static void initPersonnagesJoueur(Joueur j, GestionnairePartie p, ControleurPlacement.coteJeu cote) throws ExceptionPersonnage {
        List<Personnage> all_characters = p.getPersonnagesDisponibles();
        int n = all_characters.size();

        int colonne = 0;
        if (cote == ControleurPlacement.coteJeu.DROIT) {
            colonne = p.getPlateauLargeur()-1;
        }

        for (Personnage pf : all_characters) {
            Personnage copie_pf = (Personnage) pf.clone();
            j.ajouterMembre(copie_pf);
            j.setPersonnageActif(copie_pf);
            copie_pf.setNouveauNom();

            // Set random position
            boolean place = false;
            while (!place) {
                int ligne = (int) (random() * p.getPlateauHauteur());
                Position pos = new Position(colonne, ligne);
                place = p.setPositionPersonnage(pos);
            }
        }

        j.personnagesTousPlaces();
    }

    public static void main(String[] args) {
        // Initialise chaque joueur
        Joueur j1 = new Joueur("Joueur 1");
        Joueur j2 = new IA();
        
        Fenetre fenetre = new Fenetre();
        GestionnairePartie maPartie = new GestionnairePartie(false, j1, j2);
        fenetre.setPartie(maPartie);

        try {
            // Set number of characters and board size
            maPartie.setTailleEquipePlateau(4, 6, 6);

            ControleurPlacement.coteJeu coteJ1;
            ControleurPlacement.coteJeu coteJ2;
//            if (random() < 0.5) {
                coteJ1 = ControleurPlacement.coteJeu.GAUCHE;
                coteJ2 = ControleurPlacement.coteJeu.DROIT;
//            } else {
//                coteJ1 = ControleurPlacement.coteJeu.DROIT;
//                coteJ2 = ControleurPlacement.coteJeu.GAUCHE;
//            }

            initPersonnagesJoueur(j1, maPartie, coteJ1);
            maPartie.joueurSuivant();
            initPersonnagesJoueur(j2, maPartie, coteJ2);
            maPartie.joueurSuivant();

            fenetre.getControleur().continueGeneralJeu();
            fenetre.getControleur().continueGeneralJeu();

        } catch (ExceptionParamJeu ex) {
            Logger.getLogger(PhantasticasBellumIAMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExceptionPersonnage ex) {
            Logger.getLogger(PhantasticasBellumIAMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
