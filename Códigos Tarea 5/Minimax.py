import time
import copy

def siguiente_turno(turno):
    return "|" if turno == "-" else "-"

def hay_tres_plus(tablero):
    lineas = []

    for i in range(3):
        lineas.append([tablero[i][j] for j in range(3)])  # fila
        lineas.append([tablero[j][i] for j in range(3)])  # columna

    lineas.append([tablero[i][i] for i in range(3)])      # diagonal principal
    lineas.append([tablero[i][2 - i] for i in range(3)])  # diagonal secundaria

    for linea in lineas:
        if all(celda == "+" for celda in linea):
            return True
    return False

def casillas_validas(tablero, casilla_previa, turno):
    validas = []
    for i in range(3):
        for j in range(3):
            if (i, j) == casilla_previa:
                continue
            celda = tablero[i][j]
            if celda == " " or celda == siguiente_turno(turno):
                validas.append((i, j))
    return validas

def aplicar_jugada(tablero, turno, casilla):
    i, j = casilla
    nuevo_tablero = copy.deepcopy(tablero)
    actual = nuevo_tablero[i][j]

    if actual == " ":
        nuevo_tablero[i][j] = turno
    elif actual == siguiente_turno(turno):
        nuevo_tablero[i][j] = "+"
    return nuevo_tablero

def minimax_con_ruta(tablero, turno, casilla_previa, profundidad_max=None, tiempo_limite=None, inicio=None):
    if tiempo_limite and time.time() - inicio > tiempo_limite:
        return 0, []  # Corte por tiempo, se asume empate

    # Si alguien gano (estado terminal)
    if hay_tres_plus(tablero):
        return (1 if turno == "|" else -1), []

    # Limite de profundidad (estado no terminal)
    if profundidad_max is not None and profundidad_max == 0:
        return 0, []  # Estado neutro (empate)

    # Empate (estado terminal)
    # Obtenemos las movidas antes del bucle
    movidas_posibles = casillas_validas(tablero, casilla_previa, turno)
    if not movidas_posibles:
        return 0, []

    mejor_valor = -float('inf') if turno == "-" else float('inf')
    mejor_ruta = []
    mejor_casilla = None # Para rastrear si encontramos al menos una jugada

    for casilla in movidas_posibles:
        nuevo_tablero = aplicar_jugada(tablero, turno, casilla)
        nuevo_max = None
        if profundidad_max is not None:
            nuevo_max = profundidad_max - 1
            
        valor, subruta = minimax_con_ruta(
            nuevo_tablero,
            siguiente_turno(turno),
            casilla,
            nuevo_max,
            tiempo_limite,
            inicio
        )

        if turno == "-" and valor > mejor_valor:
            mejor_valor = valor
            mejor_casilla = casilla
            mejor_ruta = subruta
        elif turno == "|" and valor < mejor_valor:
            mejor_valor = valor
            mejor_casilla = casilla
            mejor_ruta = subruta

    return mejor_valor, [(turno, mejor_casilla)] + mejor_ruta

def mejor_jugada_y_ruta(tablero, turno, profundidad_max=None, tiempo_limite=None):
    inicio = time.time() if tiempo_limite else None
    mejor_valor = -float('inf') if turno == "-" else float('inf')
    mejor_jugada = None
    mejor_ruta = []

    # Aseguramos que casilla_previa sea None para la primera jugada
    for casilla in casillas_validas(tablero, None, turno):
        nuevo_tablero = aplicar_jugada(tablero, turno, casilla)
        
        nuevo_max = None
        if profundidad_max is not None:
            # La primera llamada gasta 1 de profundidad
            nuevo_max = profundidad_max - 1 
            
        valor, ruta = minimax_con_ruta(
            nuevo_tablero,
            siguiente_turno(turno),
            casilla,
            nuevo_max, 
            tiempo_limite,
            inicio
        )

        if turno == "-" and valor > mejor_valor:
            mejor_valor = valor
            mejor_jugada = casilla
            mejor_ruta = [(turno, casilla)] + ruta
        elif turno == "|" and valor < mejor_valor:
            mejor_valor = valor
            mejor_jugada = casilla
            mejor_ruta = [(turno, casilla)] + ruta

    return mejor_jugada, mejor_valor, mejor_ruta

tablero_inicial = [[" "]*3 for _ in range(3)]

jugada, valor, ruta = mejor_jugada_y_ruta(tablero_inicial, "-", 
                                          profundidad_max=16, 
                                          tiempo_limite=600) 

print("--- Análisis Completo (o hasta tiempo límite) ---")
print(f"Mejor jugada inicial: {jugada}")
print(f"Valor esperado: {valor}")
print("Ruta óptima:")
for t, c in ruta:
    print(f"{t} → {c}")
