public class Node<E> {
    private Node<E> prev;
    private final E data;
    private Node<E> next;

    public Node<E> getPrev() {
        return prev;
    }

    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }

    public E getData() {
        return data;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node(E data) {
        this.data = data;
    }
}
