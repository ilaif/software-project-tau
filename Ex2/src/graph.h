#ifndef GRAPH_H
#define GRAPH_H

#include "types.h"

typedef struct graphStruct {
	vertex *vertices;
	edge *edges;
	/* The last index of a vertex (not deleted) */
	int lastVerIdx;
	/* The last index of an edge (not deleted) */
	int lastEdgeIdx;
	/* The number of groups of 20 vertices */
	int verLength;
	/* The number of groups of 20 edges */
	int edgeLength;
	/* The current number of vertices in the array */
	int numVertices;
	/* The current number of edges in the array */
	int numEdges;
} graph;

int add_vertex(graph*, char*);
int remove_vertex_by_id(graph*, int);
int remove_vertex_by_name(graph*, char*);
int add_edge_by_names(graph*, char*, char*, double);
int add_edge_by_ids(graph*, int, int, double);
int remove_edge_by_id(graph*, int);
void print(graph*, bool);
void cluster(graph*, int);

#define ERROR_CMD_NAME 1
#define ERROR_CMD_FORMAT 2
#define ERROR_WEIGHT_NUMBER 3
#define ERROR_WEIGHT_POSITIVE 4
#define ERROR_VER_ONE_LETTER 5
#define ERROR_EDGE_ID_NUMBER 6
#define ERROR_VER_ID_NUMBER 7
#define ERROR_NUMBER_OF_CLUSTERS 8
#define ERROR_MALLOC 9
#define ERROR_VER_NOT_EXIST 10
#define ERROR_VER_SAME_NAME 11
#define ERROR_VER_NOT_FOUND 12
#define ERROR_SELF_LOOPS 13
#define ERROR_EDGE_DUPLICATE 14
#define ERROR_EDGE_NOT_EXIST 15
#define ERROR_VER_DEGREE_NOT_ZERO 16

#endif
