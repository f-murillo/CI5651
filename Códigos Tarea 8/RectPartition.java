import java.util.*;

public class RectPartition{

    // Clase para representar un rectangulo
    static class Rect{
        int h, w;
        Rect(int h, int w){
            this.h = h;
            this.w = w;
        }
    }

    // Para eliminar rectangulos dominados (redundantes)
    static List<Rect> filter(List<Rect> rects){
        // Ordenamos por alto ascendente, y si empatan, por ancho descendente
        rects.sort((a,b) -> a.h != b.h ? a.h - b.h : b.w - a.w);

        List<Rect> filtered = new ArrayList<>();
        int maxW = -1;
        for(Rect r : rects){
            if(r.w > maxW){
                filtered.add(r);
                maxW = r.w;
            }
        }
        return filtered;
    }

    // Clase para las lineas que usara la optimizacion convex hull
    static class Line{
        long m, b;
        Line(long m, long b){
            this.m = m;
            this.b = b;
        }
        long eval(long x){
            return m*x + b;
        }
    }

    // Clase para aplicar la optimización de convex hull
    static class Hull{
        LinkedList<Line> dq = new LinkedList<>();

        // Para verificar si l2 es innecesaria
        boolean bad(Line l1, Line l2, Line l3){
            return (double)(l3.b - l1.b)/(l1.m - l3.m)
                 <= (double)(l2.b - l1.b)/(l1.m - l2.m);
        }

        void add(Line l){
            while(dq.size() >= 2){
                Line l2 = dq.removeLast();
                Line l1 = dq.peekLast();
                if(bad(l1, l2, l)){
                    // l2 es redundante
                    continue;
                } else{
                    dq.addLast(l2);
                    break;
                }
            }
            dq.addLast(l);
        }

        long query(long x){
            while(dq.size() >= 2 && dq.get(0).eval(x) >= dq.get(1).eval(x)){
                dq.removeFirst();
            }
            return dq.get(0).eval(x);
        }
    }

    // Funcion que resuelve el problema 
    static long solver(List<Rect> rects){
        List<Rect> filtered = filter(rects);
        int n = filtered.size();

        long[] dp = new long[n+1];
        dp[0] = 0;

        Hull hull = new Hull();

        hull.add(new Line(filtered.get(0).w, dp[0]));

        for(int i = 1; i <= n; i++){
            long h = filtered.get(i-1).h;
            dp[i] = hull.query(h);
            if(i < n){
                hull.add(new Line(filtered.get(i).w, dp[i]));
            }
        }

        return dp[n];
    }
}
