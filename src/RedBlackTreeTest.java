public class RedBlackTreeTest {
    public void runTests() {
        //  function
        testBasicOperations();

        // Performance
        testPerformance();
    }

    private void testBasicOperations() {
        System.out.println("Running basic functionality tests...");

        RedBlackTree tree = new RedBlackTree();

        // insertions
        for (int i : new int[]{10, 20, 5, 15, 25}) {
            tree.Insert(i);
        }

        // toString
        System.out.println("Tree contents: " + tree);

        // Find
        System.out.println("Finding 10: " + tree.Find(10));  // Should be true
        System.out.println("Finding 30: " + tree.Find(30));  // Should be false

        // Delete
        System.out.println("Deleting 20: " + tree.Delete(20));  // Should be true
        System.out.println("Deleting 30: " + tree.Delete(30));  // Should be false

        // Print
        System.out.println("Final tree contents: " + tree);
    }

    private void testPerformance() {
        System.out.println("\nRunning performance tests...");

        int[] sizes = {1000, 10000, 100000};

        for (int size : sizes) {
            RedBlackTree tree = new RedBlackTree();

            // insertion
            long startTime = System.nanoTime();
            for (int i = 0; i < size; i++) {
                tree.Insert((int)(Math.random() * size * 2));
            }
            long endTime = System.nanoTime();
            System.out.printf("Size %d - Insert time: %.3f ms%n",
                    size, (endTime - startTime) / 1_000_000.0);

            // search
            startTime = System.nanoTime();
            for (int i = 0; i < size; i++) {
                tree.Find((int)(Math.random() * size * 2));
            }
            endTime = System.nanoTime();
            System.out.printf("Size %d - Search time: %.3f ms%n",
                    size, (endTime - startTime) / 1_000_000.0);

            //  deletion
            startTime = System.nanoTime();
            for (int i = 0; i < size; i++) {
                tree.Delete((int)(Math.random() * size * 2));
            }
            endTime = System.nanoTime();
            System.out.printf("Size %d - Delete time: %.3f ms%n",
                    size, (endTime - startTime) / 1_000_000.0);
        }
    }
}




