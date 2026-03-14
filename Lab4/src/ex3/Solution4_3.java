package ex3;

//public class Solution4_3 {
//}


/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
//package week6.stacks;

/**
 * A basic singly linked list implementation.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class Solution4_3<E> implements Cloneable {
    //---------------- nested LinkedQueue class ----------------
    /**
     * LinkedQueue of a singly linked list, which stores a reference to its
     * element and to the subsequent LinkedQueue in the list (or null if this
     * is the last LinkedQueue).
     */
    private static class LinkedQueue<E> {

        /** The element stored at this LinkedQueue */
        private E element;            // reference to the element stored at this LinkedQueue

        /** A reference to the subsequent LinkedQueue in the list */
        private LinkedQueue<E> next;         // reference to the subsequent LinkedQueue in the list

        /**
         * Creates a LinkedQueue with the given element and next LinkedQueue.
         *
         * @param e  the element to be stored
         * @param n  reference to a LinkedQueue that should follow the new LinkedQueue
         */
        public LinkedQueue(E e, LinkedQueue<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods
        /**
         * Returns the element stored at the LinkedQueue.
         * @return the element stored at the LinkedQueue
         */
        public E getElement() { return element; }

        /**
         * Returns the LinkedQueue that follows this one (or null if no such LinkedQueue).
         * @return the following LinkedQueue
         */
        public LinkedQueue<E> getNext() { return next; }

        // Modifier methods
        /**
         * Sets the LinkedQueue's next reference to point to LinkedQueue n.
         * @param n    the LinkedQueue that should follow this one
         */
        public void setNext(LinkedQueue<E> n) { next = n; }
    } //----------- end of nested LinkedQueue class -----------

    // instance variables of the SinglyLinkedList
    /** The head LinkedQueue of the list */
    private LinkedQueue<E> head = null;               // head LinkedQueue of the list (or null if empty)

    /** The last LinkedQueue of the list */
    private LinkedQueue<E> tail = null;               // last LinkedQueue of the list (or null if empty)

    /** Number of LinkedQueues in the list */
    private int size = 0;                      // number of LinkedQueues in the list

    /** Constructs an initially empty list. */
    public Solution4_3() { }              // constructs an initially empty list

    // access methods
    /**
     * Returns the number of elements in the linked list.
     * @return number of elements in the linked list
     */
    public int size() { return size; }

    /**
     * Tests whether the linked list is empty.
     * @return true if the linked list is empty, false otherwise
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * Returns (but does not remove) the first element of the list
     * @return element at the front of the list (or null if empty)
     */
    public E first() {             // returns (but does not remove) the first element
        if (isEmpty()) return null;
        return head.getElement();
    }

    /**
     * Returns (but does not remove) the last element of the list.
     * @return element at the end of the list (or null if empty)
     */
    public E last() {              // returns (but does not remove) the last element
        if (isEmpty()) return null;
        return tail.getElement();
    }

    // update methods
    /**
     * Adds an element to the front of the list.
     * @param e  the new element to add
     */
    public void addFirst(E e) {                // adds element e to the front of the list
        head = new LinkedQueue<>(e, head);              // create and link a new LinkedQueue
        if (size == 0)
            tail = head;                           // special case: new LinkedQueue becomes tail also
        size++;
    }

    /**
     * Adds an element to the end of the list.
     * @param e  the new element to add
     */
    public void addLast(E e) {                 // adds element e to the end of the list
        LinkedQueue<E> newest = new LinkedQueue<>(e, null);    // LinkedQueue will eventually be the tail
        if (isEmpty())
            head = newest;                         // special case: previously empty list
        else
            tail.setNext(newest);                  // new LinkedQueue after existing tail
        tail = newest;                           // new LinkedQueue becomes the tail
        size++;
    }

    /**
     * Removes and returns the first element of the list.
     * @return the removed element (or null if empty)
     */
    public E removeFirst() {                   // removes and returns the first element
        if (isEmpty()) return null;              // nothing to remove
        E answer = head.getElement();
        head = head.getNext();                   // will become null if list had only one LinkedQueue
        size--;
        if (size == 0)
            tail = null;                           // special case as list is now empty
        return answer;
    }

    @SuppressWarnings({"unchecked"})
    public boolean equals(Object o) {
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Solution4_3 other = (Solution4_3) o;   // use nonparameterized type
        if (size != other.size) return false;
        LinkedQueue walkA = head;                               // traverse the primary list
        LinkedQueue walkB = other.head;                         // traverse the secondary list
        while (walkA != null) {
            if (!walkA.getElement().equals(walkB.getElement())) return false; //mismatch
            walkA = walkA.getNext();
            walkB = walkB.getNext();
        }
        return true;   // if we reach this, everything matched successfully
    }

    @SuppressWarnings({"unchecked"})
    public Solution4_3<E> clone() throws CloneNotSupportedException {
        // always use inherited Object.clone() to create the initial copy
        Solution4_3<E> other = (Solution4_3<E>) super.clone(); // safe cast
        if (size > 0) {                    // we need independent chain of LinkedQueues
            other.head = new LinkedQueue<>(head.getElement(), null);
            LinkedQueue<E> walk = head.getNext();      // walk through remainder of original list
            LinkedQueue<E> otherTail = other.head;     // remember most recently created LinkedQueue
            while (walk != null) {              // make a new LinkedQueue storing same element
                LinkedQueue<E> newest = new LinkedQueue<>(walk.getElement(), null);
                otherTail.setNext(newest);     // link previous LinkedQueue to this one
                otherTail = newest;
                walk = walk.getNext();
            }
        }
        return other;
    }

    public int hashCode() {
        int h = 0;
        for (LinkedQueue walk=head; walk != null; walk = walk.getNext()) {
            h ^= walk.getElement().hashCode();      // bitwise exclusive-or with element's code
            h = (h << 5) | (h >>> 27);              // 5-bit cyclic shift of composite code
        }
        return h;
    }

    public void concatenate(Solution4_3<E> Q2){

        if(Q2.isEmpty()) return;

        if(this.isEmpty()){
            this.head = Q2.head;
            this.tail = Q2.tail;
            this.size = Q2.size;
        } else {
            this.tail.setNext(Q2.head);
            this.tail = Q2.tail;
            this.size += Q2.size;
        }

        Q2.head = null;
        Q2.tail = null;
        Q2.size = 0;
    }

    /**
     * Produces a string representation of the contents of the list.
     * This exists for debugging purposes only.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        LinkedQueue<E> walk = head;
        while (walk != null) {
            sb.append(walk.getElement());
            if (walk != tail)
                sb.append(", ");
            walk = walk.getNext();
        }
        sb.append(")");
        return sb.toString();
    }

    public static void main(String[] args){
        Solution4_3<String> Q1 = new Solution4_3<String>();
        Q1.addFirst("Alan");
        Q1.addFirst("Bob");
        Q1.addFirst("Casper");

        Solution4_3<String> Q2 = new Solution4_3<String>();
        Q2.addFirst("Daisy");
        Q2.addFirst("Ellen");
        Q2.addFirst("Fazal");

        System.out.println("Before concatenation: ");
        System.out.println("Q1: " + Q1);
        System.out.println("Q2: " + Q2);

        Q1.concatenate(Q2);

        System.out.println("After concatenation: ");
        System.out.println("Q1: " + Q1);
        System.out.println("Q2: " + Q2);
    }
}
