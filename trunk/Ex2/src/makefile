all: network

clean:
	-rm main.o graph.o network

network: main.o graph.o
	gcc -ansi -pedantic-errors -g -lm -o network main.o graph.o

main.o: main.c graph.o
	gcc -ansi -pedantic-errors -c -Wall -g main.c

graph.o: graph.h types.h graph.c
	gcc -ansi -pedantic-errors -c -Wall -g graph.c
