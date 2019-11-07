CFLAGS=-g -I.
INDENTFLAGS=-nuts -ts2 -orig

otp-break: otp-break.o decrypt.o options.o help.o
	$(CC) -o $@ $^

clean:
	$(RM) *.o
mostlyclean: clean
	$(RM) *~