public class Decomp{
    // Clase para los numeros complejos (para el FFT)
    static class Complex{
        double re, im;
        Complex(double r, double i){
            re = r;
            im = i;
        }

        Complex add(Complex o){
            return new Complex(re + o.re, im + o.im);
        }
        Complex sub(Complex o){
            return new Complex(re - o.re, im - o.im);
        }
        Complex mul(Complex o){
            return new Complex(re*o.re - im*o.im, re*o.im + im*o.re);
        }
    }

    // FFT
    static Complex[] fft(Complex[] a, boolean invert){
        int n = a.length;
        if(n == 1) return new Complex[]{ a[0] };

        Complex[] aEven = new Complex[n/2];
        Complex[] aOdd  = new Complex[n/2];
        for(int i = 0; i < n/2; i++){
            aEven[i] = a[2*i];
            aOdd[i]  = a[2*i+1];
        }

        Complex[] s = fft(aEven, invert);
        Complex[] t = fft(aOdd, invert);

        Complex[] r = new Complex[n];
        double ang = 2 * Math.PI / n * (invert ? -1 : 1);
        Complex wn = new Complex(Math.cos(ang), Math.sin(ang));
        Complex w = new Complex(1, 0);

        for(int j = 0; j < n/2; j++){
            Complex u = s[j];
            Complex v = t[j].mul(w);
            r[j] = u.add(v);
            r[j+n/2] = u.sub(v);
            w = w.mul(wn);
        }

        return r;
    }

    // Convolucion con el FFT
    static int[] convolution(int[] f){
        int n = 1;
        while(n < 2*f.length) n <<= 1;

        Complex[] a = new Complex[n];
        for(int i = 0; i < n; i++){
            a[i] = new Complex(i < f.length ? f[i] : 0, 0);
        }

        Complex[] A = fft(a, false);
        for(int i = 0; i < n; i++){
            A[i] = A[i].mul(A[i]); // convolucion consigo misma
        }
        Complex[] convC = fft(A, true);

        int[] conv = new int[n];
        for(int i = 0; i < n; i++){
            conv[i] = (int)Math.round(convC[i].re / n);
        }
        return conv;
    }

    // Preprocesamiento: criba modificada para el numero de divisores
    static int[] F(int N){
        int[] f = new int[N+1];
        for(int d = 1; d <= N; d++){
            for(int m = d; m <= N; m += d) {
                f[m]++;
            }
        }
        return f;
    }

    // Funcion final que resuelve el problema xd
    static int decomp(int N){
        int[] f = F(N);
        int[] conv = convolution(f);

        int max = 0;
        for(int X = 1; X <= N; X++){
            if(conv[X] > max) max = conv[X];
        }

        return max;
    }
}
