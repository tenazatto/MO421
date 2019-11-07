typedef struct {
	unsigned int a, b;
} affine_key;

extern unsigned char affine_encrypt(affine_key key, unsigned char buf, int m);
extern unsigned char affine_decrypt(affine_key key, unsigned char buf, int m);
