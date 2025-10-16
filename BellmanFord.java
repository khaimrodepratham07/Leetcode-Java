import java.util.Arrays;
import java.util.Scanner;

public class BellmanFord {

    public static void main(String[] args) {
        // Use a constant for infinity, which is safer than a magic number like 999.
        final int INFINITY = Integer.MAX_VALUE;

        // --- 1. Get User Input ---
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of vertices: ");
        int numVertices = scanner.nextInt();

        int[][] graph = new int[numVertices][numVertices];
        System.out.println("Enter the adjacency matrix (use 0 for no direct path):");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                int weight = scanner.nextInt();
                if (i == j) {
                    graph[i][j] = 0;
                } else if (weight == 0) {
                    graph[i][j] = INFINITY;
                } else {
                    graph[i][j] = weight;
                }
            }
        }

        System.out.print("Enter the source vertex (0 to " + (numVertices - 1) + "): ");
        int sourceVertex = scanner.nextInt();
        scanner.close();

        // --- 2. Initialize Distances Array ---
        int[] distances = new int[numVertices];
        Arrays.fill(distances, INFINITY);
        distances[sourceVertex] = 0;

        // --- 3. Relax Edges (V-1 Times) ---
        // This is the core of the algorithm. It iterates through all edges to find shorter paths.
        for (int i = 0; i < numVertices - 1; i++) {
            for (int u = 0; u < numVertices; u++) {
                for (int v = 0; v < numVertices; v++) {
                    if (graph[u][v] != INFINITY && distances[u] != INFINITY && distances[u] + graph[u][v] < distances[v]) {
                        distances[v] = distances[u] + graph[u][v];
                    }
                }
            }
        }

        // --- 4. Check for Negative-Weight Cycles ---
        // If a path can still be shortened, a negative cycle exists.
        /*for (int u = 0; u < numVertices; u++) {
            for (int v = 0; v < numVertices; v++) {
                if (graph[u][v] != INFINITY && distances[u] != INFINITY && distances[u] + graph[u][v] < distances[v]) {
                    System.out.println("\nError: The graph contains a negative-weight cycle!");
                    return; // Exit if a cycle is found
                }
            }
        }*/

        // --- 5. Print the Results ---
        System.out.println("\nShortest distances from source vertex " + sourceVertex + ":");
        for (int i = 0; i < numVertices; i++) {
            if (distances[i] == INFINITY) {
                System.out.println("Distance to vertex " + i + " is Unreachable");
            } else {
                System.out.println("Distance to vertex " + i + " is " + distances[i]);
            }
        }
    }
}


