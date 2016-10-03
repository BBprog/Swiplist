package com.projectSwiplist;

/**    
 *
 * @author BBprog
 */
public class Node<T> {
    
    /**
     * Valeur de la clef du noeud.
     */
    private T value;
    
    /**
     * Liste de noeuds correspondant à la tour de pointeur.
     */
    private Node<T>[] tower;
    
    /**
     * Taille de la tour.
     */
    private int towerHeight;
   
    /**
     * Constructeur : assigne la valeur passé en paramètre au noeud et 
     * initialise la tour avec une taille dont la valeur est passé en paramètre
     * et initialise chaque élément à "null".
     *
     * @param value Valeur à assigner au noeud.
     * @param towerHeigth Taille de la tour.
     */
    public Node(T value, int towerHeigth) {
        this.value = value;
        if (towerHeight < 1) towerHeight = 1;
        this.towerHeight = towerHeigth;
        tower = new Node[towerHeigth];
    }

    /**
     * Assigne la valeur passé en paramètre à la clef du noeud.
     *
     * @param value Valeur à assigner au noeud.
     */
    public void setValue(T value) {
        this.value = value;
    }
    
    /**
     * Place un noeud dans la tour à la position passé en paramètre.
     *
     * @param index Position dans la tour à laquelle l'élément sera placé.
     * @param node Noeud à placer.
     */
    public void setNode(int index, Node<T> node) {
        if (index >= towerHeight) return;       
        this.tower[index] = node;
    }
    
    /**
     * Retourne la valeur du noeud.
     *
     * @return Valeur du noeud.
     */
    public T getValue() {
        return value;
    }
    
    /**
     * Renvoie le noeud de la tour présent à la position donnée en parmètre.
     *
     * @param index Position dans la tour.
     * @return Noeud à la position donnée en paramètre.
     */
    public Node<T> getNode(int index) {
        if (index >= towerHeight) return null;
        return tower[index];
    }
    
    /**
     * Renvoie la tour de pointeurs.
     *
     * @return la tour.
     */
    public Node<T>[] getTower() {
        return tower;
    }
    
    /**
     * Renvoie la taille de la tour.
     *
     * @return Taille de la tour.
     */
    public int getTowerHeight() {
        return towerHeight;
    }
    
    /**
     * Renvoie vraie si la tour est vide, faux sinon.
     *
     * @return Vraie si la tour est vide.
     */
    public boolean isEmptyTower() {
        return tower[0] == null;
    }
    
}
