#include <stdint.h>
#include "tea.h"

uint32_t delta=0x9e3779b9;

void tea_encrypt (uint32_t* v, tea_key k) {
    uint32_t v0=v[0], v1=v[1], sum=0, n=32;
    uint32_t k0=k.key[0], k1=k.key[1], k2=k.key[2], k3=k.key[3];
    while (n-- > 0) {
        sum += delta;
        v0 += ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);
        v1 += ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);
    }
    v[0]=v0; v[1]=v1;
}

void tea_decrypt (uint32_t* v, tea_key k) {
    uint32_t v0=v[0], v1=v[1], sum=delta<<5, n=32;
    uint32_t k0=k.key[0], k1=k.key[1], k2=k.key[2], k3=k.key[3];
    while (n-- > 0) {
        v1 -= ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);
        v0 -= ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);
        sum -= delta;
    }
    v[0]=v0; v[1]=v1;
}
