import java.util.Arrays;

public class Equipajes{

    static class Punto{
        int x, y;
        Punto(int x, int y){
            this.x = x; 
            this.y = y; 
        }
    }

    static int n;
    static Punto[] maletas;
    static int[] dp;

    public static int distanciaCuadrada(Punto a, Punto b){
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    public static int resolver(){
        int N = 1 << n; // 2^n
        dp = new int[N];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for(int mask = 0; mask < N; mask++){
            if(dp[mask] == Integer.MAX_VALUE) continue;

            // Buscamos la primera maleta no recogida
            for(int i = 0; i < n; i++){
                if ((mask & (1 << i)) != 0) continue;

                // Opcion 1, recoger solo la maleta i
                int nueva = mask | (1 << i);
                int costo = distanciaCuadrada(new Punto(0, 0), maletas[i]) + distanciaCuadrada(maletas[i], new Punto(0, 0));
                dp[nueva] = Math.min(dp[nueva], dp[mask] + costo);

                // Opcion 2, recoger la maleta i y otra maleta j
                for(int j = i + 1; j < n; j++){
                    if ((mask & (1 << j)) != 0) continue;
                    nueva = mask | (1 << i) | (1 << j);
                    costo = distanciaCuadrada(new Punto(0, 0), maletas[i]) +
                            distanciaCuadrada(maletas[i], maletas[j]) +
                            distanciaCuadrada(maletas[j], new Punto(0, 0));
                    dp[nueva] = Math.min(dp[nueva], dp[mask] + costo);
                }

                break; // solo necesitamos el primer i no recogido
            }
        }

        return dp[N - 1]; 
    }
}