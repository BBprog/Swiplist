package com.projectSwiplist;

import java.util.ArrayList;

public class Skiplist<T extends Comparable<? super T>> {
    /**
     * Nombre maximum d'étages dans la liste.
     */
    private final int MAX = 10;
    
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
     * Initialise le tableau de tête à la taille maximum.
     */
    public Skiplist () {
        this.values = new ArrayList<T> ();
        this.heads = new Node[MAX];
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
        int level = 1;
        while (Math.random() < 0.5) {
            ++level;
        }
        return (level < MAX) ? level : MAX;
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

        return (prevNode == null) ? heads[0] : prevNode.getNode(0);
    }
    
    /**
     * Ajoute un noeud dans la liste.
     * Génère un noeud avec la valeur passé en paramètre, cherche la position 
     * où le noeud doit être inséré puis l'ajoute à la liste.
     *
     * @param value Valeur du noeud à ajouter à la liste.
     */
    public void add(T value) {  
        System.out.println("add " + value);
    	Node<T> node = generateNode(value);        
        
        Node<T>[] prevNodes = new Node[getNumberOfStairs()];
        
        Node nodeFound = findNode(prevNodes, value);
        if (nodeFound == null || nodeFound.getValue() != value) {
            insertNode(prevNodes, node);
        }
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
        System.out.println("remove " + value);
        Node<T>[] prevNodes = new Node[getNumberOfStairs()];

        Node<T> nodeFound = findNode(prevNodes, value);
        
        if (nodeFound != null && nodeFound.getValue().equals(value)) {
            deleteNode(prevNodes, nodeFound);
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
     * @param prevTower Liste des noeuds précédents le noeud à supprimer.
     * @param node Noeud à supprimer de la liste.
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
     * @return String La liste sous forme de chaîne de caractères.
     */
    @Override
    public String toString() { 
        if (isEmpty()) {
            return "[]";
        }
        
        String listToString = ("[H]\t");
        for (int index = 0; index < getNumberOfStairs(); ++index) {
            String str = (heads[index] == null) ? "X"
                            : heads[index].getValue().toString();
            listToString += ("[" + str + "] ");
        }
        listToString += ("\n"); 
        
        Node n = heads[0];
        for ( ; n != null; n = n.getNode(0)) {
            listToString += ("[" + n.getValue() + "]\t");
            for (int index = 0; index < n.getTowerHeight(); ++index) {
                String str = (n.getNode(index) == null) ? "X"
                                : n.getNode(index).getValue().toString();
                listToString += ("[" + str + "] ");
            }
            listToString += ("\n");    
        }
        
        listToString += ("values = [ ");
        for (int index = 0; index < values.size(); ++index) {
            listToString += ("" + values.get(index) + " ");
        }
        listToString += ("]\n");
        
        return listToString;
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
        Skiplist list = new Skiplist();

        System.out.println("-> adding éléments");
        list.add(3);
        System.out.println(list.toString());
        list.add(5);
        System.out.println(list.toString());
        list.add(10);
        System.out.println(list.toString());
        list.add(8);
        System.out.println(list.toString());
        list.add(1);
        System.out.println(list.toString());
        
        System.out.println("-> remove 8");
        list.remove(8);
        System.out.println(list.toString());
        System.out.println("-> remove 2");
        list.remove(2);
        System.out.println(list.toString());
          
        System.out.println("-> adding éléments");
        list.add(9);
        list.add(12);
        list.add(1);
        System.out.println(list.toString());
        
        System.out.println("-> remove 3 random");
        list.removeRandomNodes(3);
        System.out.println(list.toString());
    }    
}
