/*
 * ppm: subroutines for reading PPM picture files
 *
 * Adapted from the work of Paul Heckbert and Michael Garland.
 *
 */

typedef struct {
	int nx, ny;
	unsigned char *pix;
} pic;

extern int ppm_read(FILE *fp, pic *p);
extern int ppm_write(FILE *fp, pic *pic);

