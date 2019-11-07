#include <stdint.h>

typedef struct {
	uint32_t key[4];
} tea_key;

typedef struct {
	uint32_t block[2];
} tea_block;

extern void tea_encrypt (uint32_t* v, tea_key k);
extern void tea_decrypt (uint32_t* v, tea_key k);
