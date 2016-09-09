package trees;

/**
 * Created by Никита on 09.09.2016.
 */
public class BinaryTree<T1 extends Comparable<T1>, T2> {

    public Node<T1, T2> root = null;

    static class Node<T1, T2> {

        T1 key;
        T2 value;
        Node<T1, T2> left, right, parent;

        Node(T1 key, T2 value) {
            this.key = key;
            this.value = value;
        }
    }

    public Node<T1, T2> searchNode(T1 key) {
        Node<T1, T2> x = root;
        while (x != null) {
            if (key.compareTo(x.key) == 0) {
                return x;
            } else if (key.compareTo(x.key) > 0) {
                x = x.right;
            } else {
                x = x.left;
            }
        }
        return null;
    }

    public Node<T1, T2> nodeWithMinimumKey(Node<T1, T2> x) {
        Node<T1, T2> y = null;
        while (x != null) {
            y = x;
            x = x.left;
        }
        return y;
    }

    public Node<T1, T2> nodeWithMaximumKey(Node<T1, T2> x) {
        Node<T1, T2> y = null;
        while (x != null) {
            y = x;
            x = x.right;
        }
        return y;
    }

    public Node<T1, T2> successorNode(Node<T1, T2> x) {
        if (x == null) {
            return null;
        }
        if (x.right != null) {
            return nodeWithMinimumKey(x.right);
        }
        Node<T1, T2> y = x.parent;
        while (y != null && x == y.right) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    public Node<T1, T2> predecessorNode(Node<T1, T2> x) {
        if (x == null) {
            return null;
        }
        if (x.left != null) {
            return nodeWithMaximumKey(x.left);
        }
        Node<T1, T2> y = x.parent;
        while (y != null && x == y.left) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    public void insertNode(T1 key, T2 value) {
        Node<T1, T2> y = searchNode(key), z = null;
        if (y != null) {
            y.value = value;
            return;
        }
        Node<T1, T2> x = new Node<>(key, value);
        if (root == null) {
            root = x;
            return;
        }
        y = root;
        while (y != null) {
            z = y;
            if (key.compareTo(y.key) > 0) {
                y = y.right;
            } else {
                y = y.left;
            }
        }
        if (key.compareTo(z.key) > 0) {
            z.right = x;
            x.parent = z;
        } else {
            z.left = x;
            x.parent = z;
        }
    }

    public void transplant(Node<T1, T2> u, Node<T1, T2> v) {
        if (u.parent == null) {
            root = v;
            return;
        }
        if (u.parent.left == u) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    public void removeNode(T1 key) {
        Node<T1, T2> y = searchNode(key);
        if (y == null) {
            return;
        }
        if (y.left == null) {
            transplant(y, y.right);
        } else if (y.right == null) {
            transplant(y, y.left);
        } else {
            Node<T1, T2> x = nodeWithMaximumKey(y.right);
            if (x.parent != y) {
                transplant(x, x.right);
                x.right = y.right;
                x.right.parent = x;
            }
            transplant(y, x);
            x.left = y.left;
            y.left.parent = y;
        }
    }

    public void printBinaryTreeInorderWalk() {
        printBinaryTreeInorderWalk(root);
        System.out.println();
    }

    private void printBinaryTreeInorderWalk(Node<T1, T2> x) {
        if (x != null) {
            printBinaryTreeInorderWalk(x.left);
            System.out.print(x.key + " ");
            printBinaryTreeInorderWalk(x.right);
        }
    }
}

