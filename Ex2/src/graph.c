#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <malloc.h>
#include <ctype.h>
#include <time.h>

#include "graph.h"

#define group 20

/* The function adds a new vertex to the graph */
int add_vertex(graph *grp, char *verName) {
	int i=0, check=0;
	int mem=0;

	if ( (*grp).numVertices == (*grp).lastVerIdx+1 ) {

		 /* Check if the array is full */
		if ( (*grp).numVertices == ((*grp).verLength * group) ) {
			mem=memory_allocation(grp, 1);
			if (mem!=0) {
				return mem;
			}
		}

		check=add_vertex_in_index(grp, verName, (*grp).lastVerIdx+1);

		/* Check if memory allocation failed */
		if (check!=0) {
			return check;
		}

		(*grp).lastVerIdx++;

		return 0;
	}
	
	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted ) {
			check=add_vertex_in_index(grp, verName, i);

			/* Check if memory allocation failed */
			if (check!=0) {
				return check;
			} else {
				return 0;
			}
		}
	}
	return 0;
}

/* The function removes a vertex with the received id */
int remove_vertex_by_id(graph *grp, int id) {
	if (id > (*grp).lastVerIdx) {
		return ERROR_VER_NOT_EXIST;
	}
	if ( (*((*grp).vertices+id)).deleted ) {
		return ERROR_VER_NOT_EXIST;
	}
	if (is_degree_zero(grp,id)==false) {
		return ERROR_VER_DEGREE_NOT_ZERO;
	}
	
	(*((*grp).vertices+id)).deleted=true;
	(*grp).numVertices--;

	free((*((*grp).vertices+id)).name);

	if (id==(*grp).lastVerIdx) {
		(*grp).lastVerIdx--;
	}

	return 0;
}

/* The function removes a vertex with the received name */
int remove_vertex_by_name(graph *grp, char* verName) {

	int firstInd=get_vertex_index(grp, verName);

	if (firstInd==-1) {
		return ERROR_VER_NOT_EXIST;
	}
	if (is_muliple_vertex_name(grp, verName)) {
		return ERROR_VER_SAME_NAME;
	}

	return remove_vertex_by_id(grp, firstInd);
}

/* The function adds an edge between v1 and v2 with weight w */
int add_edge_by_ids(graph* grp, int v1, int v2, double w) {
	int i=0, mem=0;

	if ( (v1 > (*grp).lastVerIdx) || (v2 > (*grp).lastVerIdx) ) {
		return ERROR_VER_NOT_FOUND;
	}
	if ( ((*((*grp).vertices+v1)).deleted) || ((*((*grp).vertices+v2)).deleted) ) {
		return ERROR_VER_NOT_FOUND;
	}
	if (v1==v2) {
		return ERROR_SELF_LOOPS;
	}

	for(i=0; i<=(*grp).lastEdgeIdx; i++) {
		if ((*((*grp).edges+i)).deleted == false) {
			if ( ( ((*((*grp).edges+i)).v1_id==v1) && ((*((*grp).edges+i)).v2_id==v2) ) ||
				 ( ((*((*grp).edges+i)).v1_id==v2) && ((*((*grp).edges+i)).v2_id==v1) ) ) {
				return ERROR_EDGE_DUPLICATE;
			}
		}
	}

	if ( (*grp).numEdges == (*grp).lastEdgeIdx+1 ) {

		if ( (*grp).numEdges == ((*grp).edgeLength * group) ) {
			mem=memory_allocation(grp, 0);
			if (mem!=0) {
				return mem;
			}
		}

		add_edge_in_index(grp, (*grp).lastEdgeIdx+1, v1, v2, w);

		(*grp).lastEdgeIdx++;

		return 0;
	}

	for(i=0; i<=(*grp).lastEdgeIdx; i++) {
		if ( (*((*grp).edges+i)).deleted ) {

			add_edge_in_index(grp, i, v1, v2, w);
	
			return 0;
		}
	}
	return 0;
}

/* The function adds an edge between v1 and v2 with weight w */
int add_edge_by_names(graph* grp, char* v1, char* v2, double w) {
	int v1exist=get_vertex_index(grp, v1);
	int v2exist=get_vertex_index(grp, v2);

	if ( (v1exist==-1) || (v2exist==-1) ) {
		return ERROR_VER_NOT_FOUND;
	}
	if ( (is_muliple_vertex_name(grp, v1)) || (is_muliple_vertex_name(grp, v2)) ) {
		return ERROR_VER_NOT_FOUND;
	}

	return add_edge_by_ids(grp, v1exist, v2exist, w);
}

/* The function removes an edge with the received id */
int remove_edge_by_id(graph* grp, int id) {
	
	if (id > (*grp).lastEdgeIdx) {
		return ERROR_EDGE_NOT_EXIST;
	}
	if ((*((*grp).edges+id)).deleted) {
		return ERROR_EDGE_NOT_EXIST;
	}

	(*((*grp).edges+id)).deleted=true;
	(*grp).numEdges--;

	if (id==(*grp).lastEdgeIdx) {
		(*grp).lastEdgeIdx--;
	}

	return 0;
}

/* Prints the graph */
void print(graph *grp, bool clust) {
	int i=0;
	char* name1;
	char* name2;

	if ((*grp).numVertices > 0) {
		printf("%d vertices:\n", (*grp).numVertices);
	}

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {
			printf("%d: ", i);
			printf("%s", (*((*grp).vertices+i)).name);
			if (clust) {
				printf(" %d", (*((*grp).vertices+i)).cluster);
			}
			printf("\n");
		}

	}

	if ((*grp).numEdges > 0) {
		printf("%d edges:\n", (*grp).numEdges);
	}

	for(i=0; i<=(*grp).lastEdgeIdx; i++) {
		if ((*((*grp).edges+i)).deleted == false) {
			printf("%d: ", i);
			name1=(*((*grp).vertices+(*((*grp).edges+i)).v1_id)).name;
			name2=(*((*grp).vertices+(*((*grp).edges+i)).v2_id)).name;
			printf("%s-%s %.3f",name1,name2,(*((*grp).edges+i)).weight);
			printf("\n");
		}
	}
}  

/* Creates a random cluster */
void cluster(graph *grp, int k) {
	double score=0;
	int i=0;
	int v1=0, v2=0;

	/* initiate random seed */
	srand((int)time(0));

	/* Giving each vertex a cluster number */
	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {
			(*((*grp).vertices+i)).cluster = (rand() % k) + 1;
		}
	}

	/* Calculate the sum */
	for(i=0; i<=(*grp).lastEdgeIdx; i++) {
		if ( (*((*grp).edges+i)).deleted == false) {
			v1=(*((*grp).edges+i)).v1_id;
			v2=(*((*grp).edges+i)).v2_id;

			if ( (*((*grp).vertices+v1)).cluster == (*((*grp).vertices+v2)).cluster ) {
				score=score+(*((*grp).edges+i)).weight;
			} else {
				score=score-(*((*grp).edges+i)).weight;
			}	
		}
	}

	print(grp, true);

	printf("The random clustering score for %d clusters is %.3f\n", k, score);  
}  

/* The function allocates 20 new memory cells */
/* Vertices array: 1 */
/* Edges array: 0 */
int memory_allocation(graph *grp, int isVer) {
	vertex* tmpVer=NULL;
	edge* tmpEdg=NULL;

	/* Add to vertices array */
	if (isVer==1) {
		if ( (*grp).verLength == 0 ) {
			tmpVer=(vertex*)calloc(group, sizeof(vertex));
			if (tmpVer == NULL) {
				return ERROR_MALLOC;
			}
			(*grp).vertices = tmpVer;
			tmpVer=NULL;
			(*grp).verLength++;
			return 0;
		} else {
			tmpVer=(vertex*)realloc((*grp).vertices, (group*((*grp).verLength+1))*sizeof(vertex));
			if (tmpVer == NULL) {
				return ERROR_MALLOC;
			}
			(*grp).vertices = tmpVer;
			tmpVer=NULL;
			(*grp).verLength++;
			return 0;
		}
	/* Add to edges array */
	} else {
		if ( (*grp).edgeLength == 0 ) {
			tmpEdg=(edge*)calloc(group, sizeof(edge));
			if (tmpEdg == NULL) {
				return ERROR_MALLOC;
			}
			(*grp).edges = tmpEdg;
			tmpEdg=NULL;
			(*grp).edgeLength++;
			return 0;
		} else {
			tmpEdg=(edge*)realloc((*grp).edges, (group*((*grp).edgeLength+1))*sizeof(edge));
			if (tmpEdg == NULL) {
				return ERROR_MALLOC;
			}
			(*grp).edges = tmpEdg;
			tmpEdg=NULL;
			(*grp).edgeLength++;
			return 0;
		}
	}
}

/* Tools */

/* The function returns true if the degree of vertex with the received id is 0, otherwise false */
bool is_degree_zero(graph *grp, int id) {
	int i=0;

	for(i=0; i<=(*grp).lastEdgeIdx; i++) {
		if ((*((*grp).edges+i)).deleted == false) {
			if ( ((*((*grp).edges+i)).v1_id==id) || ((*((*grp).edges+i)).v2_id==id) ) {
				return false;
			}
		}
	}

	return true;
}

/* The function returns the first index of vertex with the received name, if it does not exist returns -1 */
int get_vertex_index(graph *grp, char* verName) {
	int i=0;

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {
			/* Check equlity between names */
			if (strcmp(verName, (*((*grp).vertices+i)).name) == 0) {
				return i;
			}
		}
	}

	return -1;
}

/* The function returns true if there 2 or more vertices with received name */
bool is_muliple_vertex_name(graph *grp, char* verName) {
	int i=0;
	int counter=0;

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {
			/* Check equlity between names */
			if (strcmp(verName, (*((*grp).vertices+i)).name) == 0) {
				counter++;
				if (counter>1) {
					return true;
				}
			}
		}
	}

	return false;
}

/* The function adds a vertex with the received name at idx */
int add_vertex_in_index(graph *grp, char* verName, int idx) {
	char* tmp;
	(*((*grp).vertices+idx)).deleted=false;
	(*((*grp).vertices+idx)).cluster=0;
	tmp=(char*)calloc(((int)strlen(verName)+1), sizeof(char));
	if (tmp==NULL) {
		return ERROR_MALLOC;
	}

	(*((*grp).vertices+idx)).name=tmp;
	tmp=NULL;
	strcpy((*((*grp).vertices+idx)).name,verName);

	(*grp).numVertices++;
	return 0;
}

/* The function adds an edge (v1, v2) with weight w at idx */
void add_edge_in_index(graph *grp, int ind, int v1, int v2, double w) {
	(*((*grp).edges+ind)).deleted=false;
	(*((*grp).edges+ind)).weight=w;
	(*((*grp).edges+ind)).v1_id=v1;
	(*((*grp).edges+ind)).v2_id=v2;

	(*grp).numEdges++;
}
