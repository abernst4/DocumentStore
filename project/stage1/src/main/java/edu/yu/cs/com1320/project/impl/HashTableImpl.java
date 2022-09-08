package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {
    private class Node<Key, Value> {
        private Key key;
        private Value data;
        private Node next;

        private Node(Key k, Value v) { // made change here
            this.key = k;
            this.data = v;
        }
    }

    private Node[] nodes;

    public HashTableImpl() {
        nodes = new Node[5];
    }

    public Value put(Key k, Value v) {
        if (firstSlotEmpty(k, v)) {
            return null;
        }
        if (isAppendable(nodes[makeIndex(k)], new Node(k, v))) {
            appendNode(nodes[makeIndex(k)], new Node(k, v));
            return null;
        }
        return (Value) replaceNode(nodes[makeIndex(k)], new Node(k, v), k);
    }

    private boolean isAppendable(Node<?, ?> current, Node<?, ?> newNode) {
        if (!isDifferentKey(current, newNode)) {
            return false;
        }
        while (current.next != null && isDifferentKey(current, newNode)) {
            current = current.next;
        }
        if (current.next == null && isDifferentKey(current, newNode)) { //this is the change that I made
            return true;
        }
        return false;
    }

    private void appendNode(Node<?, ?> current, Node<?, ?> node) {
        while (current.next != null) {
            current = current.next;
        }
        current.next = node;
    }

    private boolean firstSlotEmpty(Key k, Value v) {
        int index = makeIndex(k);
        if (nodes[index] == null) {
            nodes[index] = new Node(k, v);
            return true;
        }
        return false;
    }

    private int makeIndex(Key k) { 
        return (k.hashCode() & 0x7fffffff) % this.nodes.length;
    }

    private boolean isDifferentKey(Node<?, ?> current, Node<?, ?> node) {
        if (current.key.equals(node.key)) {
            return false;
        }
        return true;
    }

    private boolean isDifferentKey(Node<?, ?> current, Key k) {
        if (current.key.equals(k)) {
            return false;
        }
        return true;
    }

    private Value replaceNode(Node<?, ?> current, Node<?, ?> newNode, Key k) {
        if (!isDifferentKey(current, newNode)) {
            newNode.next = current.next;
            nodes[makeIndex(k)] = newNode;
            return (Value) current.data;
        }
        while (isDifferentKey(current.next, newNode)) {
            current = current.next;
        }
        Node<?, ?> oldNode = current.next;
        newNode.next = current.next.next;
        current.next = newNode;
        return (Value) oldNode.data;
    }

    public Value get(Key k) {
        Node<?, ?> current = nodes[makeIndex(k)];
        if (nodes[makeIndex(k)] == null) {
            return null;
        }
        while (isDifferentKey(current, k)) {
            current = current.next;
            if(current == null){
                return null;
            }
        }
        return (Value) current.data;
    }
}
