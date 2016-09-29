package com.projectSwiplist;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Bprog
 */
public class Swiplist<T extends Comparable<? super T>> {
    
    private final int MAX = 20;
    
    /**
     * Tableau des têtes de liste. 
     * Chaque tête correspond à un étage de la liste.
     */
    private ArrayList<Node<T>> heads;
    
    /**
     * Tableau des valeurs contenues dans la liste. 
     */
    private ArrayList<T> values;
    
    /**
     * Constructeur par defaut. 
     * Initialise la première tête de la liste à "null".
     */
    public Swiplist () {
        values = new ArrayList<T> ();
        heads = new ArrayList<Node<T>>();
        heads.add(null);
    }
    
    /**
     * Etend la liste des têtes jusqu'à la taille de la tour du noeud passé en 
     * paramètre.
     * 
     * @param newSize .
     */
    private void extendHeadsSize(int newSize) {
        for ( ; getNumberOfStairs() < newSize; )
            heads.add(null);
    }
    
    /**
     * Réduit la liste des têtes en enlevant les dernière cases de valeur nulle.
     * 
     * @param node Noeud.
     */
    private void reduceHeadsSize() {
        for ( ; getNumberOfStairs() > 1 && 
                heads.get(getNumberOfStairs() - 1) == null; )
            heads.remove(getNumberOfStairs() - 1);
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
     * Cherche la valeur dans la liste et renvoie le noeud trouvé.
     * Parcours la liste depuis l'étage le plus haut et remplie les deux 
     * tableaux passés en paramètres avec les noeuds précédent et les noeuds
     * suivants le noeud à trouver.
     * 
     * (A REFAIRE)
     *
     * @param prevNodes Noeuds précédents le noeud à trouver.
     * @param value Valeur du noeud à trouver.
     * @return Le noeud trouvé.
     */
    private Node<T> findNodeToAdd(Node<T>[] prevNodes, T value) {
        Node<T> prevNode = null;
        Node<T> currentNode = null;
        
        for (int index = getNumberOfStairs() - 1; index >= 0; --index) {
            if (prevNode == null) 
                currentNode = heads.get(index);
            else
                currentNode = prevNode.getNode(index);

            while (currentNode != null && 
                   currentNode.getValue().compareTo(value) < 0) {
            	if (currentNode.getValue() != value)
            		prevNode = currentNode;
                currentNode = currentNode.getNode(index);
                
            }
            prevNodes[index] = prevNode;
        }
        
        return currentNode;
    }
    
    /**
     * Cherche la valeur dans la liste et renvoie le noeud trouvé.
     * Parcours la liste depuis l'étage le plus haut et remplie les deux 
     * tableaux passés en paramètres avec les noeuds précédent et les noeuds
     * suivants le noeud à trouver.
     * 
     * (A REFAIRE)
     *
     * @param prevNodes Noeuds précédents le noeud à trouver.
     * @param succNodes Noeuds suivants le noeud à trouver.
     * @param value Valeur du noeud à trouver.
     * @return Le noeud trouvé.
     */
    private Node<T> findNodeToRemove(Node<T>[] prevNodes, Node<T>[] succNodes, T value) {
        Node<T> prevNode = null;
        Node<T> currentNode = null;
        
        for (int index = getNumberOfStairs() - 1; index >= 0; --index) {
            if (prevNode == null) 
                currentNode = heads.get(index);
            else
                currentNode = prevNode.getNode(index);

            while (currentNode != null && 
                   currentNode.getValue().compareTo(value) < 0) {
                prevNode = currentNode;
                currentNode = currentNode.getNode(index);
            }
            
            if (currentNode != null && currentNode.getValue() == value) 
            	currentNode = currentNode.getNode(index);
            
            prevNodes[index] = prevNode;
            succNodes[index] = currentNode;
        }
        
        return (prevNodes[0] == null) ? null : prevNodes[0].getNode(0);
    }
    
    /**
     * Renvoie le noeud contenant la valeur passée en paramètre si il est
     * présent dans la liste, renvoie "null" sinon.
     *
     * @param value Valeur du noeud à trouver.
     * @return Le noeud trouvé ou "null" si non trouvé.
     */
    private Node<T> findNodeByValue(T value) {
        Node<T> prevNode = null;
        Node<T> currentNode = null;
        
        for (int index = getNumberOfStairs() - 1; index >= 0; --index) {
            if (prevNode == null) 
                currentNode = heads.get(index);
            else
                currentNode = prevNode.getNode(index);

            while (currentNode != null && 
                   currentNode.getValue().compareTo(value) < 0) {
                currentNode = currentNode.getNode(index);
            }
            
            if (currentNode != null && 
                currentNode.getValue() == value) 
                return currentNode;
        }
        
        return null;
    }
    
    /**
     * Ajoute un noeud dans la liste.
     * Génère un noeud avec la valeur passé en paramètre, cherche la position 
     * où le noeud doit être inséré puis l'ajoute à la liste.
     * Enfin, si le noeud possède une tour de taille supérieure à la taille de 
     * la tour de têtes, la tour de têtes est étendue. 
     *
     * @param value Valeur du noeud à ajouter à la liste.
     */
    public void add(T value) {    	
    	Node<T> node = generateNode(value);
        
        if (node.getTowerHeight() > getNumberOfStairs())
            extendHeadsSize(node.getTowerHeight());
        
        Node<T>[] prevNodes = new Node[getNumberOfStairs()];

        findNodeToAdd(prevNodes, value);
        
        insertNode(prevNodes, node);
    	values.add(node.getValue());
    }
    
    /**
     * Insère un noeud dans la liste.
     *
     * @param prevTower Noeuds précédents le nouveau noeud à insérer.
     * @param node Noeud à insérer dans la liste.
     */
    private void insertNode(Node[] prevTower,
                            Node<T> node) {        
        for (int index = 0; index < getNumberOfStairs(); ++index) {
            if (prevTower[index] == null)
            {
                node.setNode(index, heads.get(index));
                heads.set(index, node);
            }
            else
            {
            	node.setNode(index, prevTower[index].getNode(index));
                prevTower[index].setNode(index, node);
            }
        }
    }
    
    /**
     * Cherche une valeur dans la liste et supprime le noeud correspondant.
     *
     * @param value Valeur du noeud à ajouter à la liste.
     */
    public void remove(T value) {
        Node<T>[] prevNodes = new Node[getNumberOfStairs()];
        Node<T>[] succNodes = new Node[getNumberOfStairs()];

        Node<T> node = findNodeToRemove(prevNodes, succNodes, value);
        
        if (node != null) {
            deleteNode(prevNodes, succNodes, node);
            values.remove(node.getValue());
        }
        
        reduceHeadsSize();
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
     * @param succTower Noeuds suivants le noeud à supprimer.
     * @param node Noeud à supprimer la liste.
     */
    private void deleteNode(Node[] prevTower, 
                            Node[] succTower, 
                            Node node) {
        for (int index = 0; index < getNumberOfStairs(); ++index) {
            if (prevTower[index] == null) 
                heads.set(index, succTower[index]);
            else
                prevTower[index].setNode(index, succTower[index]);
        }
        node = null;
    }
    
    /**
     * Affiche les valeurs de chaque noeud de la liste.
     */
    public void print() {
        System.out.print("(h:" + getNumberOfStairs() + ")");
        
        System.out.print("[ ");
        Node n = heads.get(0);
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
        return heads.size();
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
        return heads.isEmpty();
    }
    
    /**
     * Renvoie vrai si la valeur passée en paamètre est dans la liste.
     * 
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
        list.add("comment");
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
