package com.projectSwiplist;

import java.util.ArrayList;

/**
 * IMPORTANT : REMETTRE UN TABLEAU DE TETE Node<T>[] heads
 * @author BBprog
 */
public class Swiplist<T extends Comparable<? super T>> {
    
    private final int MAX = 20;
    
    /**
     * Tableau des têtes de liste. 
     * Chaque tête correspond à un étage de la liste.
     */
    private Node<T>[] heads;
    
    /**
     * Tableau des valeurs contenues dans la liste. 
     */
    private ArrayList<T> values;
    
    /**
     * Constructeur par defaut. 
     * Initialise la première tête de la liste à "null".
     */
    public Swiplist () {
        this.values = new ArrayList<T> ();
        this.heads = new Node[1];
        this.heads[0] = null;
    }
    
    /**
     * Agrandi le tableau de têtes à la taille passée en paramètre.
     * 
     * @param newSize Nouvelle taille du tableau.
     */
    private void extendHeads(int newSize) {
        if (newSize <= getNumberOfStairs()) return;
        
        Node<T>[] nodes = new Node[newSize];
        System.arraycopy(heads, 0, nodes, 0, getNumberOfStairs());
        heads = nodes;
    }
    
    /**
     * Supprime les pointeurs null en fin du tableau de tête.
     */
    private void reduceHeads() {
        if (getNumberOfStairs() < 2) return;
        
        int index = 0;
        while (index < getNumberOfStairs() && heads[index] != null)
            ++index;
        
        Node<T>[] nodes = new Node[index];
        System.arraycopy(heads, 0, nodes, 0, index);
        heads = nodes;
    }

    /**
     * Retourne un noeud contenant la valeur passé en paramètre et avec une 
     * tour de taille aléatoire.
     * 
     * @param value Valeur du noeud.
     * @return Le noeud généré.
     */
    private Node<T> generateNode(T value) {
        return new Node<T>(value, generateRandomTowerSize());
    }
    
    /**
     * Retourne une valeur aléatoire comprise entre 1 et le nombre maximum 
     * d'étage.
     * 
     * @return Une valeur aléatoire entre 1 et MAX.
     */
    private int generateRandomTowerSize() {
        return (int) Math.floor((Math.random() * (MAX)) + 1);
    }
    
    /**
     * Retourne une valeur aléatoire présente dans la liste.
     * 
     * @return Une valeur contenue dans la liste.
     */
    private T getRandomValue() {
        return values.get(
                (int) Math.floor((Math.random() * (values.size() - 1)) + 1));
    }
    
    /**
     * Renvoie le noeud contenant la valeur passée en paramètre si il est
     * présent dans la liste, renvoie "null" sinon.
     *
     * (A REFAIRE)
     * 
     * @param value Valeur du noeud à trouver.
     * @return Le noeud trouvé ou "null" si non trouvé.
     */
    private Node<T> findNodeByValue(T value) {
        Node<T> prevNode = null;
        Node<T> currentNode = null;
        
        return null;
    }
    
    /**
     * Cherche la valeur dans la liste et renvoie le noeud trouvé.
     * 
     * (A REFAIRE)
     *
     * @param prevNodes Noeuds précédents le noeud à trouver.
     * @param value Valeur du noeud à trouver.
     * @return Le noeud trouvé.
     */
    private Node<T> findNode(Node<T>[] prevNodes, T value) {
        if (isEmpty()) return null;
        
        Node<T> prevNode = null;
        Node<T> currentNode = null; 
        
        for (int index = getNumberOfStairs() - 1; index >= 0; --index) {
            if (prevNode == null) 
                currentNode = heads[index];
            else
                currentNode = prevNode.getNode(index);

            while (currentNode != null && 
                   currentNode.getValue().compareTo(value) <= 0) {
                if (currentNode.getValue() != value)
                    prevNode = currentNode;
                currentNode = currentNode.getNode(index);
            }
            prevNodes[index] = prevNode;
        }

        return (prevNode == null) ? null : prevNode.getNode(0);
    }
    
    /**
     * Ajoute un noeud dans la liste.
     * Génère un noeud avec la valeur passé en paramètre, cherche la position 
     * où le noeud doit être inséré puis l'ajoute à la liste.
     *
     * @param value Valeur du noeud à ajouter à la liste.
     */
    public void add(T value) {    	
    	Node<T> node = generateNode(value);
        
        extendHeads(node.getTowerHeight());
        
        Node<T>[] prevNodes = new Node[getNumberOfStairs()];
        
        Node nodeFound = findNode(prevNodes, value);
        if (nodeFound == null || nodeFound.getValue() != value)
            insertNode(prevNodes, node);
    }
    
    /**
     * Insère un noeud dans la liste.
     * 
     * @param prevTower Noeuds précédents le nouveau noeud à insérer.
     * @param node Noeud à insérer dans la liste.
     */
    private void insertNode(Node[] prevTower,
                            Node<T> node) {
        for (int index = 0; index < node.getTowerHeight(); ++index) {
            if (prevTower[index] == null) {
                node.setNode(index, heads[index]); 
                heads[index] = node;  
            }
            else {
                node.setNode(index, prevTower[index].getNode(index));
                prevTower[index].setNode(index, node); 
            }
        }
        values.add(node.getValue());
    }
    
    /**
     * Cherche une valeur dans la liste et supprime le noeud correspondant.
     *
     * @param value Valeur du noeud à ajouter à la liste.
     */
    public void remove(T value) {
        Node<T>[] prevNodes = new Node[getNumberOfStairs()];

        Node<T> nodeFound = findNode(prevNodes, value);
        
        if (nodeFound != null && nodeFound.getValue().equals(value)) {
            deleteNode(prevNodes, nodeFound);
            reduceHeads();
        }
    }
    
    /**
     * Supprime aléatoirement un nombre passé en paramètre de noeuds.
     *
     * @param count Nombre de noeuds à supprimer.
     */
    public void removeRandomNodes(int count) {
        if (count >= size()) return;
        for (int index = 0; index < count; ++index)
            remove(getRandomValue());
    }
    
    /**
     * Supprime un noeud de la liste.
     *
     * @param prevTower Noeuds précédents le noeud à supprimer.
     * @param node Noeud à supprimer la liste.
     */
    private void deleteNode(Node[] prevTower,
                            Node node) {
        for (int index = 0; index < node.getTowerHeight(); ++index)
        {
            if (prevTower[index] == null)
                heads[index] = node.getNode(index);
            else
                prevTower[index].setNode(index, node.getNode(index));
        }
        values.remove(node.getValue());
        node = null;
    }
    
    /**
     * Affiche les valeurs de chaque noeud de la liste.
     */
    public void print() {
        System.out.print("(h:" + getNumberOfStairs() + ")");    
        System.out.print("[ ");
        Node n = heads[0];
        for (; n != null; n = n.getNode(0)) {
            System.out.print(n.getValue() + ":" + n.getTowerHeight() + " ");
        }
        System.out.print("]\n");
        
        System.out.print("values = [ ");
        for (int index = 0; index < values.size(); ++index) {
            System.out.print("" + values.get(index) + " ");
        }
        System.out.print("]\n");
    }
    
    /**
     * Renvoie le nombre d'étage de la liste.
     * 
     * @return Nombre d'étage.
     */
    public int getNumberOfStairs() {
        return heads.length;
    }
    
    /**
     * Renvoie le nombre d'éléments de la liste.
     * 
     * @return Nombre de noeuds.
     */
    public int size() {
        return values.size();
    }
    
    /**
     * Renvoie vrai si la liste est vide.
     * 
     * @return Vrai si la liste est vide.
     */
    public boolean isEmpty() {
        return (heads[0] == null);
    }
    
    /**
     * Renvoie vrai si la valeur passée en paamètre est dans la liste.
     * 
     * @param value Valeur à chercher dans la liste.
     * @return Vrai si la valeur est dans la liste.
     */
    public boolean isValueInList(T value) {
        return values.contains(value);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Swiplist list = new Swiplist();
        
        System.out.println("-> adding éléments");
        list.add("cc");
        list.print();
        list.add("comment");
        list.print();
        list.add("va");
        list.add("tu");

        list.print();
        
        list = new Swiplist();
        System.out.println("-> adding éléments");
        list.add(3);
        list.add(5);
        list.add(10);
        list.add(8);
        list.print();
        list.add(1);
        list.print();
        
        System.out.println("-> remove 8");
        list.remove(8);
        list.print();
        System.out.println("-> remove 2");
        list.remove(2);
        list.print();
          
        System.out.println("-> adding éléments");
        list.add(9);
        list.add(12);
        list.add(1);
        list.print();
        
        System.out.println("-> remove 3 random");
        list.removeRandomNodes(3);
        list.print();
    }    

}
