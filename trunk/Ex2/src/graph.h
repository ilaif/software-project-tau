#ifndef GRAPH_H
#define GRAPH_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <malloc.h>
#include <ctype.h>
#include <time.h>
#include "types.h"

typedef struct grp {
	vertex *vertices;
	edge *edges;
	/* The last index of a non deleted vertex */
	int lastVerIdx;
	/* The last index of a non deleted edge */
	int lastEdgIdx;
	/* The number of groups */
	int verLen;
	/* The number of groups */
	int edgLen;
	/* The current number of vertices */
	int numVer;
	/* The current number of edges */
	int numEdg;
} graph;

int add_vertex(graph*, char*);

int remove_vertex_by_id(graph*, int);

int remove_vertex_by_name(graph*, char*);

int add_edge_by_names(graph*, char*, char*, double);

int add_edge_by_ids(graph*, int, int, double);

int remove_edge_by_id(graph*, int);

void print(graph*, bool);

void cluster(graph*, int);

#define ERROR_VERTEX_NAME_NOT_VALID 1
#define ERROR_VERTEX_ID_NOT_NUMBER 2
#define ERROR_VERTEX_NOT_EXIST 3
#define ERROR_VERTEX_NOT_FOUND 4
#define ERROR_VERTEX_CANNOT_BE_DELETED 5
#define ERROR_MULTIPLE_VERTICES_NAME 6

#define ERROR_EDGE_ID_NOT_NUMBER 7
#define ERROR_EDGE_NOT_EXIST 8
#define ERROR_DUPLICATED_EDGES 9
#define ERROR_WEIGHT_NOT_NUMBER 10
#define ERROR_WEIGHT_NOT_POSITIVE 11

#define ERROR_CMD_NOT_RECOGNIZED 12
#define ERROR_FORMAT_NOT_VALID 13

#define ERROR_MALLOC_FAILED 14
#define ERROR_CLUSTERS_NUMBER_NOT_VALID 15
#define ERROR_INVALID_LOOP 16



#endif
