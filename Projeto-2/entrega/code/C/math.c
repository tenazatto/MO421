int gcd(int x, int y) {
    return (y == 0) ? x : gcd(y, x % y);
}

int coprimality(int x, int y) {
    return (gcd(x, y) == 1);
}

int mod_inverse(int a, int m) {
    int x;
    a %= m;
    for(x = 1; x < m; x++) {
        if((a*x) % m == 1) return x;
    }

    return -1;
}
