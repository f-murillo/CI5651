public class Familiares {

    public static int maxFamiliares(int[] A){
        int n = A.length;
        int[][] familiares = new int[n][n];
        int maxLen = 0;

        for (int i = n - 1; i >= 0; i--){
            for (int j = n - 1; j >= 0; j--){
                if (mcd(A[i], A[j]) == 1){
                    
                    if(i + 1 < n && j + 1 < n){
                        familiares[i][j] = 1 + familiares[i + 1][j + 1];
                    } 
                    
                    else{
                        familiares[i][j] = 1;
                    }

                    if((i + familiares[i][j] - 1) < j || (j + familiares[i][j] - 1) < i){
                        maxLen = Math.max(maxLen, familiares[i][j]);
                    }

                } else{
                    familiares[i][j] = 0;
                }
            }
        }
        return maxLen;
    }

    public static int mcd(int a, int b) {
        while (b != 0) {
            int tmp = b;
            b = a % b;
            a = tmp;
        }
        return a;
    }

    public static void main(String[] args) {
        int[] A = {3, 10, 1, 8, 4, 5, 3, 6, 9, 2};
        int d = maxFamiliares(A);
        System.out.println("La máxima longitud de subarreglos familiares es " + d);
    }
}

