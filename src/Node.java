public class Node<E> {
    private final E data;
    private Node<E> prev;
    private Node<E> next;

    public Node(E data) {
        this.data = data;
    }

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
}
