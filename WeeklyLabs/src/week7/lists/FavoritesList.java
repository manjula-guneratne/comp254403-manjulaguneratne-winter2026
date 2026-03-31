package week7.lists;

import java.util.*;

// ---------- Position Interface ----------
interface Position<E> {
  E getElement();
}

// ---------- PositionalList Interface ----------
interface PositionalList<E> extends Iterable<E> {
  int size();
  boolean isEmpty();
  Position<E> first();
  Position<E> last();
  Position<E> before(Position<E> p);
  Position<E> after(Position<E> p);
  Position<E> addFirst(E e);
  Position<E> addLast(E e);
  Position<E> addBefore(Position<E> p, E e);
  E remove(Position<E> p);
}

// ---------- LinkedPositionalList ----------
class LinkedPositionalList<E> implements PositionalList<E> {

  private static class Node<E> implements Position<E> {
    E element;
    Node<E> prev, next;

    Node(E e, Node<E> p, Node<E> n) {
      element = e;
      prev = p;
      next = n;
    }

    public E getElement() { return element; }
  }

  private Node<E> header, trailer;
  private int size = 0;

  public LinkedPositionalList() {
    header = new Node<>(null, null, null);
    trailer = new Node<>(null, header, null);
    header.next = trailer;
  }

  private Node<E> validate(Position<E> p) {
    return (Node<E>) p;
  }

  private Position<E> position(Node<E> node) {
    if (node == header || node == trailer) return null;
    return node;
  }

  public int size() { return size; }
  public boolean isEmpty() { return size == 0; }

  public Position<E> first() { return position(header.next); }
  public Position<E> last() { return position(trailer.prev); }

  public Position<E> before(Position<E> p) {
    Node<E> node = validate(p);
    return position(node.prev);
  }

  public Position<E> after(Position<E> p) {
    Node<E> node = validate(p);
    return position(node.next);
  }

  private Position<E> addBetween(E e, Node<E> pred, Node<E> succ) {
    Node<E> newest = new Node<>(e, pred, succ);
    pred.next = newest;
    succ.prev = newest;
    size++;
    return newest;
  }

  public Position<E> addFirst(E e) {
    return addBetween(e, header, header.next);
  }

  public Position<E> addLast(E e) {
    return addBetween(e, trailer.prev, trailer);
  }

  public Position<E> addBefore(Position<E> p, E e) {
    Node<E> node = validate(p);
    return addBetween(e, node.prev, node);
  }

  public E remove(Position<E> p) {
    Node<E> node = validate(p);
    Node<E> pred = node.prev;
    Node<E> succ = node.next;
    pred.next = succ;
    succ.prev = pred;
    size--;
    return node.element;
  }

  public Iterator<E> iterator() {
    return new Iterator<E>() {
      Node<E> cursor = header.next;

      public boolean hasNext() {
        return cursor != trailer;
      }

      public E next() {
        E val = cursor.element;
        cursor = cursor.next;
        return val;
      }
    };
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (E e : this)
      sb.append(e).append(", ");
    sb.append("]");
    return sb.toString();
  }
}

// ---------- FavoritesList ----------
public class FavoritesList<E> {

  protected static class Item<E> {
    private E value;
    private int count = 0;

    public Item(E val) { value = val; }
    public int getCount() { return count; }
    public E getValue() { return value; }
    public void increment() { count++; }

    public String toString() {
      return "(" + value + ":" + count + ")";
    }
  }

  PositionalList<Item<E>> list = new LinkedPositionalList<>();

  protected E value(Position<Item<E>> p) {
    return p.getElement().getValue();
  }

  protected int count(Position<Item<E>> p) {
    return p.getElement().getCount();
  }

  protected Position<Item<E>> findPosition(E e) {
    Position<Item<E>> walk = list.first();
    while (walk != null && !e.equals(value(walk)))
      walk = list.after(walk);
    return walk;
  }

  protected void moveUp(Position<Item<E>> p) {
    int cnt = count(p);
    Position<Item<E>> walk = p;
    while (walk != list.first() && count(list.before(walk)) < cnt)
      walk = list.before(walk);

    if (walk != p)
      list.addBefore(walk, list.remove(p));
  }

  public int size() { return list.size(); }
  public boolean isEmpty() { return list.isEmpty(); }

  public void access(E e) {
    Position<Item<E>> p = findPosition(e);
    if (p == null)
      p = list.addLast(new Item<>(e));
    p.getElement().increment();
    moveUp(p);
  }

  public void remove(E e) {
    Position<Item<E>> p = findPosition(e);
    if (p != null)
      list.remove(p);
  }

  public Iterable<E> getFavorites(int k) {
    if (k < 0 || k > size())
      throw new IllegalArgumentException("Invalid k");

    PositionalList<E> result = new LinkedPositionalList<>();
    Iterator<Item<E>> iter = list.iterator();

    for (int j = 0; j < k; j++)
      result.addLast(iter.next().getValue());

    return result;
  }

  public String toString() {
    return list.toString();
  }

  public static void test(FavoritesList<Character> fav) {
    char[] sample = "hello this is a test".toCharArray();

    for (char c : sample) {
      fav.access(c);
      int k = Math.min(5, fav.size());

      System.out.println("Entire list: " + fav);
      System.out.println("Top " + k + ": " + fav.getFavorites(k));
      System.out.println();
    }
  }

  public static void main(String[] args) {
    test(new FavoritesList<>());
  }
}