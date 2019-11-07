#include "math.h"
#include "affine.h"

unsigned char affine_encrypt(affine_key key, unsigned char buf, int m) {
	return (key.a * buf + key.b) % m;
}

unsigned char affine_decrypt(affine_key key, unsigned char buf, int m) {
	return (mod_inverse(key.a, m) * (buf - key.b)) % m;
}
