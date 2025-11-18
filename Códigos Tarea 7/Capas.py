import math

class Point:
    """Para representar los puntos del plano cartesiano"""
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __eq__(self, other): 
        return self.x == other.x and self.y == other.y
    
    def __hash__(self): 
        return hash((self.x, self.y)) # esto para que el set pueda comparar los puntos de forma correcta

def angle(base, p):
    return math.atan2(p.y - base.y, p.x - base.x)

def cross(a, b, c):
    dx1, dy1 = b.x - a.x, b.y - a.y
    dx2, dy2 = c.x - a.x, c.y - a.y
    return dx1 * dy2 - dy1 * dx2

def convexHull(points):
    if len(points) <= 1: return points[:]
    base = min(points, key=lambda p: (p.y, p.x))
    sorted_pts = sorted([p for p in points if p != base], key=lambda p: angle(base, p))

    stack = [base, sorted_pts[0]]
    for p in sorted_pts[1:]:
        while len(stack) >= 2 and cross(stack[-2], stack[-1], p) <= 0:
            stack.pop()
        stack.append(p)
    return stack

def count_layers(points):
    remaining = set(points)
    layers = 0
    while remaining:
        hull = convexHull(list(remaining))
        for p in hull:
            remaining.discard(p)
        layers += 1
    return layers
