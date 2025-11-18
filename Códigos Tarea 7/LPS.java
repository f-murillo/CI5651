public class LPS {
    public static String lps(String x) {
        int n = x.length();
        int[] b = new int[n + 1];
        b[0] = -1;
        int j = -1;

        for (int i = 1; i <= n; i++) {
            while (j >= 0 && x.charAt(i - 1) != x.charAt(j)) {
                j = b[j];
            }
            j++;
            b[i] = j;
        }

        int t = b[n];
        return x.substring(0, t);
    }   

    public static void main(String[] args) {
        System.out.println(lps("ABRACADABRA"));
        System.out.println(lps("AREPERA"));     
        System.out.println(lps("ALGORITMO"));
        System.out.println(lps("SEVENTYSEVEN")); 
    }
}