import java.util.*;
public class Main2 {
  public static void main(String[] args) {
    int idx=0;
    int n=Integer.parseInt(args[idx++]);
    int[][] arr=new int[n][2];
    for(int i=0;i<n;i++){
      arr[i][0]=Integer.parseInt(args[idx++]);
      arr[i][1]=Integer.parseInt(args[idx++]);
    }
    Arrays.sort(arr, Comparator.comparingInt(a->a[1]));
    int cnt=1;
    int cur=arr[0][1];
    for(int i=1;i<n;i++){
      if(arr[i][0] > cur){
        cnt++;
        cur=arr[i][1];
      }
    }
    System.out.print(cnt);
  }
}
