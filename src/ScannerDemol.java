import java.math.BigInteger;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.Math;
//上面即  导包  过程
public class ScannerDemol {
    public static BigInteger fbnq(int n){
        BigInteger []dp=new BigInteger[100010];
        Arrays.fill(dp,BigInteger.ZERO);
     //用Arrays.fill(a,0);来实现数组整体赋值
        //实际上，new int[100010]写完以后，不用下面这句话也可以自动全部赋值0
         dp[0]= BigInteger.ONE;
         dp[1]=BigInteger.ONE;
         for(int i=2;i<=n;i++){
             dp[i]=dp[i-1].add(dp[i-2]);
         }
         return dp[n];
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        //建立对象，准备用其接收数据
        int i=sc.nextInt();  //整数读取

        System.out.println(fbnq(i));
        }
    }

