CPLEX=cplex121
CONCERT=concert29
CPARCH=x86-64_debian4.0_4.1

CFLAGS+= -ansi -pedantic-errors -Wall -g
CFLAGS+= -I/usr/local/stow/${CPLEX}/lib/${CPLEX}/include
CFLAGS+= -I/usr/local/stow/${CONCERT}/lib/${CONCERT}/include/
CFLAGS+= -I/usr/include/libxml2

LDFLAGS+= -L/usr/local/stow/${CPLEX}/lib/${CPLEX}/lib/${CPARCH}/static_pic -lilocplex -lcplex 
LDFLAGS+= -lm -lpthread -lgraph
LDFLAGS+= -lxml2

all: kcluster

clean:
	rm main.o graph.o cluster.o graphics.o kcluster

kcluster: graph.o cluster.o main.o graphics.o
	gcc -o kcluster main.o graph.o cluster.o graphics.o $(CFLAGS) $(LDFLAGS)

main.o: main.c graph.o cluster.o graphics.o
	gcc -c main.c $(CFLAGS)

graph.o: graph.c graph.h types.h
	gcc -c graph.c $(CFLAGS)

cluster.o: cluster.c cluster.h graph.o
	gcc -c cluster.c $(CFLAGS)
	
graphics.o: graphics.c graphics.h graph.o
	gcc -c graphics.c $(CFLAGS)