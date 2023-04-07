package com.example.ostunes;

import java.util.NoSuchElementException;

public class DoublyCircularLinkedList<T> {

    private Node head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    public DoublyCircularLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public Song getHeadData() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (Song)head.data;
    }

    public Song getNext(Node a){
        return (Song)a.next.data;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            newNode.prev = tail;
            newNode.next = head;
        } else {
            newNode.next = head;
            head.prev = newNode;
            newNode.prev = tail;
            tail.next = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            newNode.prev = tail;
            newNode.next = head;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
            tail = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T data = (T) head.data;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head.next.prev = tail;
            tail.next = head.next;
            head = head.next;
        }
        size--;
        return data;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T data = tail.data;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail.prev.next = head;
            head.prev = tail.prev;
            tail = tail.prev;
        }
        size--;
        return data;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("List is empty");
        } else {
            Node<T> current = head;
            do {
                System.out.print(current.data + " ");
                current = current.next;
            } while (current != head);
            System.out.println();
        }
    }

    public void addNode(Song song) {
        Node newNode = new Node(song);

        if (head == null) {
            head = newNode;
            head.next = head;
            head.prev = head;
        } else {
            Node lastNode = head.prev;
            newNode.next = head;
            head.prev = newNode;
            newNode.prev = lastNode;
            lastNode.next =newNode;
        }
    }


    public void removeNode(Node node) {
        if (node == head) {
            head = node.next;
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

}

