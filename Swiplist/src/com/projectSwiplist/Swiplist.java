package com.projectSwiplist;

import java.util.ArrayList;

/**
 *
 * @author BBprog
 */
public class Swiplist<T extends Comparable<? super T>> {
    
    private final int MAX = 20;
    
    /**
     * Tableau des têtes de liste. 
     * Chaque tête correspond à un étage de la liste.
     */
    private Node<T> head;
    
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
        head = null;
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
     * 
     * (A REFAIRE)
     *
     * @param prevNodes Noeuds précédents le noeud à trouver.
     * @param value Valeur du noeud à trouver.
     * @return Le noeud trouvé.
     */
    private Node<T> findNodeToAdd(Node<T>[] prevNodes, T value) {
        if (isEmpty()) return head;
        
        Node<T> prevNode = null;
        Node<T> currentNode = head; 
        
        for ( ; currentNode != null && currentNode.getValue().compareTo(value) < 0; )
        {
            prevNode = currentNode;
            currentNode = currentNode.getNode(currentNode.getTowerHeight() - 1);
        }
        
        if (prevNode == null) return head;
        
        int index = prevNode.getTowerHeight() - 1;
        prevNodes[index] = prevNode;
        
        for ( ; index >= 0; --index)        
        {
            currentNode = prevNode.getNode(index);
            for ( ; currentNode != null && currentNode.getValue().compareTo(value) < 0; )
            {
                prevNode = currentNode;
                currentNode = currentNode.getNode(index);
            }
            prevNodes[index] = prevNode;
        }
        
        return prevNode;
    }
    
    /**
     * Cherche la valeur dans la liste et renvoie le noeud trouvé.
     * Parcours la liste depuis l'étage le plus haut et remplie les deux 
     * tableaux passés en paramètres avec les noeuds précédents le noeud à 
     * trouver.
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
                currentNode = head;
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
     * (A REFAIRE)
     * 
     * @param value Valeur du noeud à trouver.
     * @return Le noeud trouvé ou "null" si non trouvé.
     */
    private Node<T> findNodeByValue(T value) {
        Node<T> prevNode = null;
        Node<T> currentNode = null;
        
        for (int index = getNumberOfStairs() - 1; index >= 0; --index) {
            if (prevNode == null) 
                currentNode = head;
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
     *
     * @param value Valeur du noeud à ajouter à la liste.
     */
    public void add(T value) {    	
    	Node<T> node = generateNode(value);
        Node<T>[] prevNodes = new Node[node.getTowerHeight()];
        
        //findNodeToAdd(prevNodes, value);
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
        if (prevTower[0] == null)
            insertNodeFirst(node);
        else
            for (int index = 0; index < node.getTowerHeight(); ++index) {
                node.setNode(index, prevTower[index].getNode(index));
                prevTower[index].setNode(index, node);
            }
    }
    
    /**
     * Insère un noeud au début de la liste.
     * 
     * @param node Noeud à insérer en début de liste.
     */
    private void insertNodeFirst(Node<T> node) {
        Node<T> next = head;
        for (int index = 0; index < node.getTowerHeight(); ++index)
        {
            while(next != null && index == next.getTowerHeight())
                next = next.getNode(next.getTowerHeight() - 1);
            node.setNode(index, next);
        }

        node.setNode(0, head);
        head = node;
    }
    
    /**
     * Cherche une valeur dans la liste et supprime le noeud correspondant.
     *
     * @param value Valeur du noeud à ajouter à la liste.
     */
    public void remove(T value) {
        Node<T>[] prevNodes = new Node[getNumberOfStairs()];

        Node<T> node = findNodeToAdd(prevNodes, value);
        
        if (node != null) {
            deleteNode(prevNodes, node);
            values.remove(node.getValue());
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
        if (prevTower[0] == null)
            head = node.getNode(0);
        else
        {
            for (int index = 0; index < node.getTowerHeight(); ++index)
                prevTower[index].setNode(index, node.getNode(index));
        }
        node = null;
    }
    
    /**
     * Affiche les valeurs de chaque noeud de la liste.
     */
    public void print() {
        System.out.print("(h:" + getNumberOfStairs() + ")");    
        System.out.print("[ ");
        Node n = head;
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
     * Parcours la liste en montant au plus haut des tours de chaque noeud 
     * rencontré jusqu'à un pointeur null.
     * 
     * @return Nombre d'étage.
     */
    public int getNumberOfStairs() {
        if (isEmpty()) return 0;
        
        Node n = head;
        for ( ; n.getNode(n.getTowerHeight() - 1) != null; )
            n = n.getNode(n.getTowerHeight() - 1);
            
        return n.getTowerHeight();
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
        return (head == null);
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
