package IA;

import Controleur.Partie;
import Model.Coup;
import java.util.List;

/**
 * Joueur artificiel qui choisit chaque coup au hasard
 * @author Gwenole Lecorve
 */
public class IAAleatoire extends IA {

    /**
     * Constructeur par defaut
     */
    public IAAleatoire() {
        super("IA aleatoire");
    }
    
    /**
     * Constructeur
     * @param nom Nom du joueur artificiel
     */
    public IAAleatoire(String nom) {
        super(nom);
    }
    
    @Override
    public Coup getCoup(Partie p) {
        System.out.println(Thread.currentThread().getName()+": "+"-------- getCoup ---------");
        List<Coup> coups = p.getTousCoups();
        if (coups.size() == 0) {
            coups = p.getTousCoups();
            System.out.println(Thread.currentThread().getName()+": "+"Recomputed all strokes");
            for (Coup c : coups) {
                System.out.println(Thread.currentThread().getName()+": "+"+ Coup possible : " + c.toString());
            }
            System.exit(1);
        }
        for (Coup c : coups) {
            System.out.println(Thread.currentThread().getName()+": "+"Coup possible : " + c.toString());
        }
        Coup resultat = coups.get((int) (Math.random() * (coups.size() - 1)));
        System.out.println(Thread.currentThread().getName()+": "+"Coup calcule = " + resultat.toString());
        return resultat;
    }

}
