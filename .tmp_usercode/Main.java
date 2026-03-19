import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        int[][] arr = new int[n][2];
        for (int i = 0; i < n; i++) {
            int left = sc.nextInt();
            int right = sc.nextInt();
            arr[i][0] = left;
            arr[i][1] = right;
        }
        Arrays.sort(arr, (x, y) -> x[1] - y[1]);
        int cnt = 1;
        int cur = arr[0][1];
        for (int i = 0; i < n; i++) {
            if (cur > arr[i][1] || cur < arr[i][0]) {
                cur = arr[i][1];
                cnt++;
            }
        }
        System.out.print(cnt);
    }
}
