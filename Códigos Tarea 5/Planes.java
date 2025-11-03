import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Planes{
    // Consideracion: para la implementacion se asumio que el grafo tiene como nodos enteros
    public class Grafo{
        int V; // |V|
        List<List<Integer>> adj; // lista de adyacencias
        int[] prenum;
        int[] highest;
        boolean[] visitado;
        boolean hayPunto = false;
        int num = 0;

        public Grafo(int V){
            this.V = V;
            adj = new ArrayList<>();
            for(int i = 0; i < V; i++){
                adj.add(new ArrayList<>());
            }
        }

        public void agregarArista(int u, int v){
            adj.get(u).add(v);
            adj.get(v).add(u); // G es no dirigido
        }

        public boolean existePlan(){
            prenum = new int[V];
            highest = new int[V];
            visitado = new boolean[V];
            Arrays.fill(prenum, -1);

            dfsRec(0, -1); // asumamos que el nodo inicial es 0

            for(boolean v : visitado){
                if(!v) return false; // el grafo no es conexo
            }

            return !hayPunto;
        }

        private void dfsRec(int u, int padre){
            num++;
            prenum[u] = highest[u] = num;
            visitado[u] = true;
            int hijos = 0;

            for(int v : adj.get(u)){
                if(prenum[v] == -1){
                    hijos++;
                    dfsRec(v, u);
                    highest[u] = Math.min(highest[u], highest[v]);

                    if(padre != -1 && highest[v] >= prenum[u]){
                        hayPunto = true;
                        return;
                    }
                } else if(v != padre){
                    highest[u] = Math.min(highest[u], prenum[v]);
                }
            }

            if(padre == -1 && hijos > 1) 
                hayPunto = true; 
        }
    }
}