#include <stdio.h>
#include <stdlib.h>

#include "otp-break.h"
#include "options.h"

main(int argc, char const *argv[])
{
	define_mode(argc, argv, &mode);
	define_execution(argv, mode);
	exit(0);
}
