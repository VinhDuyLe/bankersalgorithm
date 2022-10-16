//Student: Vinh Le
//Class: COEN283
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class BankersAlgorithm {
    int n, m;
    int[][] allocation;
    int[][] max;
    int[] available;
    int[][] need;
    int[] safeSequence;
    BufferedWriter bw;

    public void createFile() throws IOException {
        bw = new BufferedWriter(new FileWriter("bankersalgorithm.txt"));
    }

    public void writeFile(BufferedWriter bw, String title, int[][] matrix, int[] array) throws IOException {
        bw.write(title);
        bw.newLine();
        for (int j = 0; j < m; j++) {
            bw.write("\tR" + (j+1) + '\t');
        }
        bw.newLine();
        if (array == null) {
            for (int i = 0; i < n; i++) {
                bw.write("P" + (i + 1) + "\t");
                for (int j = 0; j < m; j++) {
                    bw.write(matrix[i][j] + "\t\t");
                }
                bw.newLine();
            }
        } else {
            for (int j = 0; j < m; j++) {
                bw.write("\t" + array[j] + '\t');
            }
            bw.newLine();
        }
        bw.flush();
    }

    public void initializeValue() {
        Scanner sc = null;
        try {
            sc = new Scanner(System.in);
            System.out.print("Enter number of Process: ");
            n = sc.nextInt();
            System.out.print("Enter number of Resources: ");
            m = sc.nextInt();

            System.out.println("------Allocation Matrix------");
            allocation = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j< m; j++) {
                    System.out.printf("\tProcess %d Resource %d: ", i+1, j+1);
                    allocation[i][j] = sc.nextInt();
                }
            }
            System.out.println("------Max Request Matrix------");
            max = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j< m; j++) {
                    System.out.printf("\tProcess %d Resource %d: ", i+1, j+1);
                    max[i][j] = sc.nextInt();
                }
            }
            System.out.println("------Available instance of-------");
            available = new int[m];
            for (int j = 0; j< m; j++) {
                System.out.printf("\tResource %d: ", j+1);
                available[j] = sc.nextInt();
            }

            writeFile(bw, "ALLOCATION", allocation, null);
            writeFile(bw, "MAX REQUEST", max, null);
            writeFile(bw, "AVAILABLE",null, available);

        } catch (Exception e){
        }
    }

    public void calculateNeed() {
        need = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j< m; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
        try {
            writeFile(bw, "NEED", need, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkSafeState() {
        safeSequence = new int[n];
        int count = 0;
        boolean[] visited = new boolean[n];
        Arrays.fill(visited, false);

        int[] tempAvail = new int[m];
        for (int j = 0; j < m; j++) {
            tempAvail[j] = available[j];
        }

        while (count < n) {
            boolean flag = false;
            for (int i = 0; i < n; i++) {
                if (visited[i] == false) {
                    int j;
                    for (j = 0; j < m; j++) {
                        if (need[i][j] > tempAvail[j]) break;
                    }
                    if (j == m) {
                        safeSequence[count++] = i;
                        visited[i] = true;
                        flag = true;
                        for (j = 0; j < m; j++) {
                            tempAvail[j] += allocation[i][j];
                        }
                    }
                }
            }
            if (flag == false) break;
        }
        try {
            if (count < n) {
                System.out.println("System is in Unsafe state");
                bw.write("System is in Unsafe state");
                bw.newLine();
            } else {
                System.out.print("System is in Safe state, the safe sequence: ");
                bw.write("System is in Safe state, the safe sequence: ");
                for (int i = 0; i < n; i++) {
                    System.out.print("P" + (safeSequence[i] + 1) + ((i == n-1)? "" : " -> "));
                    bw.write("P" + (safeSequence[i] + 1) + ((i == n-1)? "" : " -> "));
                }
            }
            bw.flush();
        } catch (IOException e) {}
    }

    public static void main(String[] args) throws IOException {
        BankersAlgorithm bankersAlgorithm = new BankersAlgorithm();
        bankersAlgorithm.createFile();
        bankersAlgorithm.initializeValue();
        bankersAlgorithm.calculateNeed();
        bankersAlgorithm.checkSafeState();
    }
}
