package Model;

import java.util.List;

import Controleur.Partie;

/**
 * Classe qui modelise un deplacement d'un Personnage
 * @author Gwenole Lecorve/Matthieu Biache
 */
public class Attaque2 implements Action {
    private Sort sort;
    private List<Personnage> cibles;
    
    /**
     * Constructeur
     * @param sort Sort a lancer
     * @param cibles Personnages vises
     */
    public Attaque2(Sort sort, List<Personnage> cibles) {
        this.sort = sort;
        this.cibles = cibles;
    }
    
    /**
     * Renvoie le sort de l'attaque
     * @return un sort
     */
    public Sort getSort() {
        return sort;
    }
    
    /**
     * Renvoie la liste de personnages vises
     * @return des personnages
     */
    public List<Personnage> getCibles() {
        return cibles;
    }
    
    @Override
    public String toString() {
        return getSort().toString() + " sur " + getCibles().toString();
    }

    @Override
    public void appliquer(Partie partie) {
    	for(Personnage cible : getCibles()) {
    		cible.appliquerSort(getSort());
    	}
    }
}
