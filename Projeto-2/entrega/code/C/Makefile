CFLAGS=-g -I.
INDENTFLAGS=-nuts -ts2 -orig

image-cipher: encrypt_image.o tea.o affine.o math.o ppm.o
	$(CC) -o $@ $^

clean:
	$(RM) *.o
mostlyclean: clean
	$(RM) *~
