import java.util.Scanner;

public class InicializacionVirtual {
    static int[] T, a, b;
    static int ctr = 0;

    public static void main(String[] args) {
        if(args.length != 1){
            System.err.println("Error, numero incorrecto de argumentos. Uso: java InicializacionVirtual <numero de elementos del arreglo>");
            return;
        }

        int n = Integer.parseInt(args[0]);
        T = new int[n];
        a = new int[n + 1]; 
        b = new int[n];     

        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("""
                               \nIngresa un comando:\n 
                               - ASIGNAR <posicion> <valor>
                               - CONSULTAR <posicion>
                               - LIMPIAR 
                               - SALIR\n""");
            System.out.print("> ");
            String[] tokens = sc.nextLine().trim().split("\\s+");
            System.out.println();

            if (tokens.length == 0) continue;

            String cmd = tokens[0].toUpperCase();

            switch (cmd){
                case "ASIGNAR" ->{
                    if(tokens.length != 3){
                        System.out.println("Error, numero incorrecto de argumentos. Uso: ASIGNAR <posicion> <valor>");
                        break;
                    }
                    int pos = Integer.parseInt(tokens[1]);
                    int val = Integer.parseInt(tokens[2]);
                    if(pos < 0 || pos >= n){
                        System.out.println("Error: posición inválida");
                        break;
                    }
                    ctr++;
                    a[ctr] = pos;
                    b[pos] = ctr;
                    T[pos] = val;
                    System.out.println("T[" + pos + "] inicializado");
                }

                case "CONSULTAR" ->{
                    if(tokens.length != 2){
                        System.out.println("Error, numero incorrecto de argumentos. Uso: CONSULTAR <posicion>");
                        break;
                    }
                    int pos = Integer.parseInt(tokens[1]);
                    if(pos < 0 || pos >= n){
                        System.out.println("Error: posición inválida");
                        break;
                    }
                    if(1 <= b[pos] && b[pos] <= ctr && a[b[pos]] == pos){
                        System.out.println("Inicializado: T[" + pos + "] = " +  T[pos]);
                    } else{
                        System.out.println("La posicion " + pos + " no esta inicializada");
                    }
                }

                case "LIMPIAR" ->{
                    ctr = 0;
                    System.out.println("Arreglo limpiado");
                    break;   
                }

                case "SALIR" ->{
                    System.out.println("Saliendo del programa...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Error, comando '" + cmd + "' no reconocido");
            }
        }
    }
}