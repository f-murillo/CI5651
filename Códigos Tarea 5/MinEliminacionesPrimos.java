import java.util.*;

public class MinEliminacionesPrimos{

    static boolean esPrimo(int n){
        if(n < 2) return false;
        for(int i = 2; i * i <= n; i++){
            if(n % i == 0) 
                return false;
        }
        return true;
    }

    static int hopcroftKarp(List<Integer> A, List<Integer> B){
        Map<Integer, Integer> pairU = new HashMap<>();
        Map<Integer, Integer> pairV = new HashMap<>();
        Map<Integer, Integer> dist = new HashMap<>();

        for(int u : A) pairU.put(u, null);
        for(int v : B) pairV.put(v, null);

        int matching = 0;
        while(bfs(A, B, pairU, pairV, dist)){
            for(int u : A){
                if(pairU.get(u) == null && dfs(u, A, B, pairU, pairV, dist)){
                    matching++;
                }
            }
        }
        return matching;
    }

    static boolean bfs(List<Integer> A, List<Integer> B,
                       Map<Integer, Integer> pairU, Map<Integer, Integer> pairV,
                       Map<Integer, Integer> dist){
        Queue<Integer> queue = new LinkedList<>();
        for(int u : A){
            if(pairU.get(u) == null){
                dist.put(u, 0);
                queue.add(u);
            } else{
                dist.put(u, Integer.MAX_VALUE);
            }
        }
        dist.put(null, Integer.MAX_VALUE);

        while(!queue.isEmpty()){
            int u = queue.poll();
            if(dist.get(u) < dist.get(null)){
                for(int v : B){
                    if(esPrimo(u + v)){
                        Integer pu = pairV.get(v);
                        if(pu == null){
                            dist.put(null, dist.get(u) + 1);
                        } else if(dist.get(pu) == Integer.MAX_VALUE){
                            dist.put(pu, dist.get(u) + 1);
                            queue.add(pu);
                        }
                    }
                }
            }
        }
        return dist.get(null) != Integer.MAX_VALUE;
    }

    static boolean dfs(int u, List<Integer> A, List<Integer> B,
                       Map<Integer, Integer> pairU, Map<Integer, Integer> pairV,
                       Map<Integer, Integer> dist){
        if(u != 0){
            for(int v : B){
                if(esPrimo(u + v)){
                    Integer pu = pairV.get(v);
                    if(pu == null || (dist.get(pu) == dist.get(u) + 1 && dfs(pu, A, B, pairU, pairV, dist))){
                        pairU.put(u, v);
                        pairV.put(v, u);
                        return true;
                    }
                }
            }
            dist.put(u, Integer.MAX_VALUE);
            return false;
        }
        return true;
    }

    public static int minEliminaciones(List<Integer> C){
        List<Integer> A = new ArrayList<>();
        List<Integer> B = new ArrayList<>();
        for(int i = 0; i < C.size(); i++){
            if(i % 2 == 0) 
                A.add(C.get(i));
            else 
                B.add(C.get(i));
        }
        return hopcroftKarp(A, B); // por Konig, este es el minimo numero de elementos a eliminar
    }

    public static void main(String[] args){
        List<Integer> C = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        int resultado = minEliminaciones(C);
        System.out.println("Mínimos elementos a eliminar: " + resultado);
    }
}