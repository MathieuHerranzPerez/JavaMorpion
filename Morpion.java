import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * classe principale
 *
 */
public class Morpion {
	/**
	 * liste des cases vides
	 */
	List<Point> dispo;
	
    Scanner scan = new Scanner(System.in);
    
    /**
     * grille du morpion
     */
    ValMorpion[][] grille;
    
    /**
     *valeurs à mettre dans les cases
     */
    enum ValMorpion {
        cercle,
        croix,
        vide
    }
    
    /**
     * associe à chaque point le score potentiel
     */
    List<PointsEtScores> racineEnfant;

    public Morpion() {}
    
    /**
     * initialise le morpion avec des cases vides
     * @see ValMorpion
     */
    public void initialiserGrille() {
    	grille = new ValMorpion[][] {{ValMorpion.vide, ValMorpion.vide, ValMorpion.vide},
    									{ValMorpion.vide, ValMorpion.vide, ValMorpion.vide},
    									{ValMorpion.vide, ValMorpion.vide, ValMorpion.vide}};
    }

    /**
     * verifie si quelqu'un a gagné ou si il y a égalité
     * @return vrai si la partie est finie
     */
    public boolean verifierGain() {
        return (ordiGagne() || humainGagne() || etatDisponible().isEmpty());
    }

    /**
     * verifie si l'ordi a gagné
     * @return
     */
    public boolean ordiGagne() {
    	//diagonales
        if ((grille[0][0].equals(grille[1][1]) && grille[0][0].equals(grille[2][2]) && grille[0][0].equals(ValMorpion.croix)) 
        	|| (grille[0][2].equals(grille[1][1]) && grille[0][2].equals(grille[2][0]) && grille[0][2].equals(ValMorpion.croix))) {
            return true;
        }
        //lignes et colonnes
        for (int i = 0; i < 3; ++i) {
            if ((grille[i][0].equals(grille[i][1]) && grille[i][0].equals(grille[i][2]) && grille[i][0].equals(ValMorpion.croix))
                || (grille[0][i].equals(grille[1][i]) && grille[0][i].equals(grille[2][i]) && grille[0][i].equals(ValMorpion.croix))) {
                return true;
            }
        }
        return false;
    }

    /**
     * verifie si l'humain a gagné
     * @return
     */
    public boolean humainGagne() {
    	//diagonales
        if ((grille[0][0].equals(grille[1][1]) && grille[0][0].equals(grille[2][2]) && grille[0][0].equals(ValMorpion.cercle)) 
        		|| (grille[0][2].equals(grille[1][1]) && grille[0][2].equals(grille[2][0]) && grille[0][2].equals(ValMorpion.cercle))) {
            return true;
        }
        //lignes et colonnes
        for (int i = 0; i < 3; ++i) {
            if ((grille[i][0].equals(grille[i][1]) && grille[i][0].equals(grille[i][2]) && grille[i][0].equals(ValMorpion.cercle))
            		|| (grille[0][i].equals(grille[1][i]) && grille[0][i].equals(grille[2][i]) && grille[0][i].equals(ValMorpion.cercle))) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * ajoute dans une liste les coordonnées des cases vides
     * @return
     * @see Point
     */
    public List<Point> etatDisponible() {
        dispo = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (grille[i][j].equals(ValMorpion.vide)) {
                    dispo.add(new Point(i, j));
                }
            }
        }
        return dispo;
    }

    /**
     * On place soit une croix soit un cercle dans la case correspondante
     * @param point
     * @param valmorpion
     */
    public void placerVal(Point point, ValMorpion valmorpion) {
        grille[point.x][point.y] = valmorpion;
    }

    /**
     * renvoie le point le plus "strategique" pour ne pas perdre
     * @return
     */
    public Point meilleurPoint() {
        int MAX = -100000;
        int meilleur = -1;

        for (int i = 0; i < racineEnfant.size(); ++i) { 
            if (MAX < racineEnfant.get(i).score) {
                MAX = racineEnfant.get(i).score;
                meilleur = i;
            }
        }

        return racineEnfant.get(meilleur).point;
    }

    /**
     * invite l'humain à saisir des coordonnées au clavier, et place un cercle au bon endroit
     */
    public void saisirClavier() {
    	for(;;) {
	        System.out.println("ligne puis colonne:  ");
	        int x = scan.nextInt();
	        int y = scan.nextInt();
	        if(grille[x][y].equals(ValMorpion.vide)) {
		        Point point = new Point(x, y);
		        placerVal(point, ValMorpion.cercle);
		        break;
	        }
	        System.out.println("Case pleine ! tricheur !");
    	}
    }

    /**
     * afficher l'etat du morpion
     */
    public void afficherGrille() {
    	for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j)
                System.out.print(grille[i][j] + " "); 
            System.out.println("");
        }
    }

    /**
     * On cherche ce que l'humain va jouer (quel serait le score le plus interessant pour lui)
     * @param liste
     * @return
     */
    public int min(List<Integer> liste) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < liste.size(); ++i) {
            if (liste.get(i) < min) {
                min = liste.get(i);
                index = i;
            }
        }
        return liste.get(index);
    }

    /**
     * On cherche ce que doit jouer l'IA (quel serait le score le plus interessant pour elle)
     * @param liste
     * @return
     */
    public int max(List<Integer> liste) {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < liste.size(); ++i) {
            if (liste.get(i) > max) {
                max = liste.get(i);
                index = i;
            }
        }
        return liste.get(index);
    }
 
    /**
     * On lance le minimax avec la profondeur désiérée (pour un jeu d'echec vaut mieux s'arreter avec la fin, sinon on
     * passerai 10 ans par déplacement)
     * @see minimax
     * @param profondeur
     * @param tour
     */
    public void minMax(int profondeur, int tour){
        racineEnfant = new ArrayList<>();
        minimax(profondeur, tour);
    }
    
    /**
     * appel recursif de minimax pour parcourir l'arbre
     * @param pronfondeur
     * @param tour
     * @return le max de l'IA u le min de l'humain
     */
    public int minimax(int pronfondeur, int tour) {

        if (ordiGagne()) return +1;
        if (humainGagne()) return -1;

        List<Point> pointsPossibles = etatDisponible();
        if (pointsPossibles.isEmpty()) return 0; 

        List<Integer> scores = new ArrayList<>(); 

        for (int i = 0; i < pointsPossibles.size(); ++i) {
            Point point = pointsPossibles.get(i);  

            if (tour == 1) {
                placerVal(point, ValMorpion.croix); 
                int score = minimax(pronfondeur + 1, 2);
                scores.add(score);

                if (pronfondeur == 0) 
                    racineEnfant.add(new PointsEtScores(score, point));
                
            } else if (tour == 2) {
                placerVal(point, ValMorpion.cercle); 
                scores.add(minimax(pronfondeur + 1, 1));
            }
            grille[point.x][point.y] = ValMorpion.vide; //vide la case
        }
        return tour == 1 ? max(scores) : min(scores);
    }
    
    /**
     * le main()
     */
    static public void testMorpion() {
    	Morpion morpion = new Morpion();
        Random rand = new Random();
        morpion.initialiserGrille();
        
        morpion.afficherGrille();
        
        while (!morpion.verifierGain()) {
        	morpion.saisirClavier();
            morpion.afficherGrille();
            if (morpion.verifierGain())
                break;
            morpion.minMax(0, 1);
            morpion.placerVal(morpion.meilleurPoint(), ValMorpion.croix);
            morpion.afficherGrille();
        }
        if (morpion.ordiGagne()) {
            System.out.println("L'ordi a gagné ! :p");
        } 
        else if (morpion.humainGagne()) {
            System.out.println("Vous avez gagné (c'est impossible)");
        } 
        else {
            System.out.println("Egalité... Il faut vous réjouir, c'est le mieux que vous pouviez faire");
        }
    }
    
    /**
     * le vrai main() ...
     * @param args
     */
    public static void main(String[] args) {
        testMorpion();
    }
    
}
