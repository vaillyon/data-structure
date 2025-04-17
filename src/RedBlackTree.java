public class RedBlackTree {
    private Node root;

    // Constructor tree w empty root
    public RedBlackTree() {
        root = null;
    }

    // Node class w each node in tree
    private static class Node {
        int data;
        Node left, right, parent; // Point to left, right, parent
        boolean isRed; //  node either red or black

        // Node constructor
        public Node(int data) {
            this.data = data;
            this.isRed = true; // New nodes are red
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    // Left rotate
    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;

        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.parent = node.parent;

        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }

        rightChild.left = node;
        node.parent = rightChild;
    }

    // Right rotation
    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;

        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.parent = node.parent;

        // Update root
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }

        leftChild.right = node;
        node.parent = leftChild;
    }

    // Fix tree after insert
    private void fixInsert(Node node) {
        Node uncle;
        // Continue until root or tree is done
        while (node != root && node.parent.isRed) {
            if (node.parent == node.parent.parent.right) {
                uncle = node.parent.parent.left;
                // Parent and branch is red
                if (uncle != null && uncle.isRed) {
                    uncle.isRed = false;
                    node.parent.isRed = false;
                    node.parent.parent.isRed = true;
                    node = node.parent.parent;
                } else {
                    // Node is left child
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    // Node is right child
                    node.parent.isRed = false;
                    node.parent.parent.isRed = true;
                    rotateLeft(node.parent.parent);
                }
            } else {
                uncle = node.parent.parent.right;
                if (uncle != null && uncle.isRed) {
                    uncle.isRed = false;
                    node.parent.isRed = false;
                    node.parent.parent.isRed = true;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.isRed = false;
                    node.parent.parent.isRed = true;
                    rotateRight(node.parent.parent);
                }
            }
        }
        root.isRed = false; // Black root
    }

    // Insert new value into the Red-Black Tree
    public void Insert(int value) {
        Node node = new Node(value);
        Node parent = null;
        Node current = root;

        // find spot to insert
        while (current != null) {
            parent = current;
            if (value < current.data) {
                current = current.left;
            } else if (value > current.data) {
                current = current.right;
            } else {
                return; // Value exists
            }
        }

        node.parent = parent;
        if (parent == null) {
            root = node; // Insert as root if empty tree
        } else if (value < parent.data) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        // Fix tree
        fixInsert(node);
    }

    // Find value in Tree
    public boolean Find(int value) {
        Node current = root;
        while (current != null) {
            if (value == current.data) {
                return true; // found
            }
            current = value < current.data ? current.left : current.right;
        }
        return false; // not found
    }

    // traversal to produce a sorted output
    private void inorderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inorderTraversal(node.left, sb);
            sb.append(node.data).append(" ");
            inorderTraversal(node.right, sb);
        }
    }

    // Override toString rtn tree in sorted order
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        inorderTraversal(root, sb);
        return sb.toString().trim();
    }

    // Delete
    public boolean Delete(int value) {
        Node node = root;
        Node z = null;
        Node x, y;

        // Find node to delete
        while (node != null) {
            if (node.data == value) {
                z = node;
                break;
            }
            node = value < node.data ? node.left : node.right;
        }

        if (z == null) {
            return false; // if cant find
        }

        y = z;
        boolean yOriginalColor = y.isRed;

        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.isRed;
            x = y.right;

            if (y.parent == z) {
                if (x != null) {
                    x.parent = y;
                }
            } else {
                transplant(y, y.right);
                y.right = z.right;
                if (y.right != null) {  // null
                    y.right.parent = y;
                }
            }

            transplant(z, y);
            y.left = z.left;
            if (y.left != null) {  //  null
                y.left.parent = y;
            }
            y.isRed = z.isRed;
        }

        if (!yOriginalColor) {
            fixDelete(x); // Fix tree
        }

        return true;
    }

    // Helper method to replace a subtree with another
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    // node with the minimum in subtree
    private Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Fix after deletion
    private void fixDelete(Node x) {
        Node w;

        // Handle the case where x is null
        if (x == null) return;

        while (x != root && !x.isRed) {
            if (x == x.parent.left) {
                w = x.parent.right;
                if (w == null) break;

                if (w.isRed) {
                    w.isRed = false;
                    x.parent.isRed = true;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }

                if (w == null) break;

                boolean leftBlack = (w.left == null || !w.left.isRed);
                boolean rightBlack = (w.right == null || !w.right.isRed);

                if (leftBlack && rightBlack) {
                    w.isRed = true;
                    x = x.parent;
                } else {
                    if (rightBlack) {
                        w.left.isRed = false;
                        w.isRed = true;
                        rotateRight(w);
                        w = x.parent.right;
                    }

                    if (w != null) {  // null check
                        w.isRed = x.parent.isRed;
                        x.parent.isRed = false;
                        if (w.right != null) {
                            w.right.isRed = false;
                        }
                        rotateLeft(x.parent);
                    }
                    x = root;
                }
            } else {
                w = x.parent.left;
                if (w == null) break;

                if (w.isRed) {
                    w.isRed = false;
                    x.parent.isRed = true;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }

                if (w == null) break;

                boolean rightBlack = (w.right == null || !w.right.isRed);
                boolean leftBlack = (w.left == null || !w.left.isRed);

                if (rightBlack && leftBlack) {
                    w.isRed = true;
                    x = x.parent;
                } else {
                    if (leftBlack) {
                        w.right.isRed = false;
                        w.isRed = true;
                        rotateLeft(w);
                        w = x.parent.left;
                    }

                    if (w != null) {
                        w.isRed = x.parent.isRed;
                        x.parent.isRed = false;
                        if (w.left != null) {
                            w.left.isRed = false;
                        }
                        rotateRight(x.parent);
                    }
                    x = root;
                }
            }
        }

        if (x != null) {  // Additional null check
            x.isRed = false;
        }
    }
}
