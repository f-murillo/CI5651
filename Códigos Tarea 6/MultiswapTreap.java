import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiswapTreap{

    // Clase para representar los nodos del treap
    static class TreapNode{
        int value, priority, size;
        TreapNode left, right;

        TreapNode(int value){
            this.value = value;
            this.priority = new Random().nextInt();
            this.size = 1;
        }
    }

    // Clase para representar al treap 
    static class Treap{

        static int size(TreapNode node){
            return node == null ? 0 : node.size;
        }

        // para actualizar el size de los nodos luego de hacer split o merge
        static void update(TreapNode node){
            if(node != null)
                node.size = 1 + size(node.left) + size(node.right);
        }

        static TreapNode[] split(TreapNode node, int k){
            if(node == null) return new TreapNode[]{null, null};

            if(size(node.left) >= k){
                TreapNode[] splitLeft = split(node.left, k);
                node.left = splitLeft[1];
                update(node);
                return new TreapNode[]{splitLeft[0], node};

            } else{
                TreapNode[] splitRight = split(node.right, k - size(node.left) - 1);
                node.right = splitRight[0];
                update(node);
                return new TreapNode[]{node, splitRight[1]};
            }
        }

        static TreapNode merge(TreapNode a, TreapNode b){
            if(a == null || b == null) return a != null ? a : b;

            // hacemos merge en funcion de la prioridad
            if(a.priority > b.priority){
                a.right = merge(a.right, b);
                update(a);
                return a;
            } else{
                b.left = merge(a, b.left);
                update(b);
                return b;
            }
        }

        static TreapNode multiswap(TreapNode root, int a, int b){
            int L = b - a; // longitud de los bloques
            TreapNode[] t1 = split(root, a - 1);
            TreapNode[] t2 = split(t1[1], L);
            TreapNode[] t3 = split(t2[1], L);
            return merge(merge(t1[0], t3[0]), merge(t2[0], t3[1]));
        }

        // con esto obtendremos el arreglo final a partir del treap
        static void inorder(TreapNode node, List<Integer> res){
            if(node != null){
                inorder(node.left, res);
                res.add(node.value);
                inorder(node.right, res);
            }
        }

        // Ahora si, el algoritmo que resuelve el problema xd
        static void solver(int N, List<int[]>ops){
            TreapNode root = null;

            // construimos el treap
            for(int i = 1; i <= N; i++)
                root = Treap.merge(root, new TreapNode(i));

            // aplicamos los multiswaps
            for(int[] op : ops)
                root = Treap.multiswap(root, op[0], op[1]);

            // imprimimos el resultado
            List<Integer> res = new ArrayList<>();
            Treap.inorder(root, res);
            System.out.println(res);
        }
    }
}