package Projects.Jobs;

/**
 * Created by Jesper Lundin on 16 Nov 2016. 
 * Write a program the reverses the order of a linked list.
 */
public class ReverseLinkedList {


    public static void main(String[] args) {
        testCase();
        System.out.println();
        testCase2();
    }

    public static void testCase() {
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(5);

        a.setNext(b);
        b.setNext(c);
        c.setNext(d);
        Node newNode = reverseLinkedList(a);

        printNodeList(newNode);
    }

    public static void testCase2() {
        Node newNode = reverseLinkedList(null);
        printNodeList(newNode);
    }

    public static Node reverseLinkedList(Node firstNode) {
        if (firstNode == null) {
            return null;
        }

        Node first = firstNode;
        Node middle = firstNode.next;
        if (middle == null) {
            return firstNode;
        }
        first.next = null;

        while (true) {
            Node last = middle.next;
            middle.next = first;

            if (last == null) {
                break;
            }
            first = middle;
            middle = last;
        }

        return middle;
    }

    public static void printNodeList(Node start) {

        while (start != null) {
            System.out.println(start.value + " ");
            start = start.next;
        }
    }

    public static class Node {
        private int value;
        private Node next;

        public Node(int a) {
            value = a;
        }

        public void setNext(Node next) {
            this.next = next;
        }
        public Node getNext() {
            return next;
        }
    }


}