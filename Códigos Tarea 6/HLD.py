class SegmentTree:
    def __init__(self, n):
        self.N = n
        self.tree = [0] * (4 * n)
        self.lazy = [0] * (4 * n)

    def push(self, v, l, r):
        if self.lazy[v] == 0:
            return
        self.tree[v] = (r - l + 1) - self.tree[v]
        if l != r:
            self.lazy[2 * v] ^= 1
            self.lazy[2 * v + 1] ^= 1
        self.lazy[v] = 0

    def update(self, v, l, r, ql, qr):
        self.push(v, l, r)
        if qr < l or r < ql:
            return
        if ql <= l and r <= qr:
            self.lazy[v] ^= 1
            self.push(v, l, r)
            return
        m = (l + r) // 2
        self.update(2 * v, l, m, ql, qr)
        self.update(2 * v + 1, m + 1, r, ql, qr)
        self.tree[v] = self.tree[2 * v] + self.tree[2 * v + 1]

    def query(self, v, l, r, ql, qr):
        self.push(v, l, r)
        if qr < l or r < ql:
            return 0
        if ql <= l and r <= qr:
            return self.tree[v]
        m = (l + r) // 2
        return self.query(2 * v, l, m, ql, qr) + self.query(2 * v + 1, m + 1, r, ql, qr)

    def toggle(self, l, r):
        self.update(1, 0, self.N - 1, l, r)

    def exists(self, l, r):
        return self.query(1, 0, self.N - 1, l, r) > 0

    def forall(self, l, r):
        return self.query(1, 0, self.N - 1, l, r) == (r - l + 1)


class Tree:
    def __init__(self, n):
        self.N = n
        self.tree = [[] for _ in range(n)]
        self.parent = [-1] * n
        self.depth = [0] * n
        self.size = [0] * n
        self.heavy = [-1] * n
        self.head = [0] * n
        self.pos = [-1] * n
        self.time = 0
        self.seg = SegmentTree(n - 1)

    def add_edge(self, u, v):
        self.tree[u].append(v)
        self.tree[v].append(u)

    def dfs(self, u, p):
        self.parent[u] = p
        self.size[u] = 1
        max_size = 0
        for v in self.tree[u]:
            if v == p:
                continue
            self.depth[v] = self.depth[u] + 1
            self.dfs(v, u)
            self.size[u] += self.size[v]
            if self.size[v] > max_size:
                max_size = self.size[v]
                self.heavy[u] = v

    def decompose(self, u, h):
        self.head[u] = h
        if self.parent[u] != -1:
            self.pos[u] = self.time
            self.time += 1
        if self.heavy[u] != -1:
            self.decompose(self.heavy[u], h)
        for v in self.tree[u]:
            if v != self.parent[u] and v != self.heavy[u]:
                self.decompose(v, v)

    def build(self):
        self.dfs(0, -1)
        self.decompose(0, 0)

    def apply_to_path(self, u, v, op):
        while self.head[u] != self.head[v]:
            if self.depth[self.head[u]] < self.depth[self.head[v]]:
                u, v = v, u
            op(self.pos[self.head[u]], self.pos[u])
            u = self.parent[self.head[u]]
        if self.depth[u] > self.depth[v]:
            u, v = v, u
        if u != v:
            op(self.pos[v], self.pos[u + 1])

    def toggle(self, u, v):
        self.apply_to_path(u, v, self.seg.toggle)

    def exists(self, u, v):
        result = [False]
        def check(l, r):
            result[0] |= self.seg.exists(l, r)
        self.apply_to_path(u, v, check)
        return result[0]

    def forall(self, u, v):
        result = [True]
        def check(l, r):
            result[0] &= self.seg.forall(l, r)
        self.apply_to_path(u, v, check)
        return result[0]
    