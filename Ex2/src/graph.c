#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <malloc.h>
#include <ctype.h>
#include <time.h>

#include "graph.h"

#define group 20


/* memory allocation. */

int addMemory(graph *grp, int isVer) {
	/* will add 20 new memory cells to the required array: 1 - vert., 0 - edges. */
	vertex* tmp1=NULL;
	edge* tmp2=NULL;

	if (isVer==1) {  /* add to vertices array. */
		if ( (*grp).verLength == 0 ) {  /* wasn't allocated yet. */
			tmp1=(vertex*)calloc(group, sizeof(vertex));
			if (tmp1 == NULL) {  /* failed. */
				return ERROR_MALLOC;
			}
			(*grp).vertices = tmp1;
			tmp1=NULL;
			(*grp).verLength++;
			return 0;
		} else {      /* adding 20 cells, reallocation. */
			tmp1=(vertex*)realloc((*grp).vertices, (group*((*grp).verLength+1))*sizeof(vertex));
			if (tmp1 == NULL) {  /* failed. */
				return ERROR_MALLOC;
			}
			(*grp).vertices = tmp1;
			tmp1=NULL;
			(*grp).verLength++;
			return 0;
		}

	} else { /* add to edges array. */
		if ( (*grp).edgeLength == 0 ) {  /* wasn't allocated yet. */
			tmp2=(edge*)calloc(group, sizeof(edge));
			if (tmp2 == NULL) {  /* failed. */
				return ERROR_MALLOC;
			}
			(*grp).edges = tmp2;
			tmp2=NULL;
			(*grp).edgeLength++;
			return 0;
		} else {      /* adding 20 cells, reallocation. */
			tmp2=(edge*)realloc((*grp).edges, (group*((*grp).edgeLength+1))*sizeof(edge));
			if (tmp2 == NULL) {  /* failed. */
				return ERROR_MALLOC;
			}
			(*grp).edges = tmp2;
			tmp2=NULL;
			(*grp).edgeLength++;
			return 0;
		}
	}
}

/* end of memory allocation. */


/* help functions. */

bool degree0(graph *grp, int id) { /* returns true iff the degree of vertex at id is 0. */
	int i=0;

	for(i=0; i<=(*grp).lastEdgeIdx; i++) {
		if ((*((*grp).edges+i)).deleted == false) { /* existing edge. */
			if ( ((*((*grp).edges+i)).v1_id==id) || ((*((*grp).edges+i)).v2_id==id) ) {
				return false;
			}
		}
	}

	return true;
}

int isVertexNameExist(graph *grp, char* nam) {
	/* returns the first index of vertex 'name' if it exists, and -1 otherwise. */
	int i=0;

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {   /* existing vertex. */
			if (strcmp(nam, (*((*grp).vertices+i)).name) == 0) {  /* names are equal. */
				return i;
			}
		}
	}

	return -1;
}
                     
bool moreThanOnce(graph *grp, char* nam) {
	/* returns true iff vertex with 'name' appears more than once. */
	int i=0;
	int counter=0;

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {   /* existing vertex. */
			if (strcmp(nam, (*((*grp).vertices+i)).name) == 0) {  /* names are equal. */
				counter++;
				if (counter>1) {
					return true;
				}
			}
		}
	}

	return false;
}


int addVertexAtIndex(graph *grp, char* nam, int ind) {
	/* adds vertex 'name' at index ind, assuming it's free. */
    /* called from add_vertex to avoid code duplication. */
	char* tmp;	
	(*((*grp).vertices+ind)).deleted=false;
	(*((*grp).vertices+ind)).cluster=0;
	tmp=(char*)calloc(((int)strlen(nam)+1), sizeof(char));
	if (tmp==NULL) {  /* failed. */
		return ERROR_MALLOC;
	}

	(*((*grp).vertices+ind)).name=tmp;
	tmp=NULL;
	strcpy((*((*grp).vertices+ind)).name,nam);

	(*grp).numVertices++;
	return 0;
}

void addEdgeAtIndex(graph *grp, int ind, int v1, int v2, double w) {
	/* adds edge v1-v2-w at index ind, assuming it's free. */
	/* called from add_edge to avoid code duplication. */
	(*((*grp).edges+ind)).deleted=false;
	(*((*grp).edges+ind)).weight=w;
	(*((*grp).edges+ind)).v1_id=v1;
	(*((*grp).edges+ind)).v2_id=v2;

	(*grp).numEdges++;
}


/* end of help functions. */


int add_vertex(graph *grp, char *nam) {
	int i=0, check=0;
	int mem=0;

	if ( (*grp).numVertices == (*grp).lastVerIdx+1 ) {

		if ( (*grp).numVertices == ((*grp).verLength * group) ) {  /* array is full. */
			mem=addMemory(grp, 1);
			if (mem!=0) {   /* reallocation failed. */
				return mem;
			}
		}

		check=addVertexAtIndex(grp, nam, (*grp).lastVerIdx+1); /* adding at the very end. */

		if (check!=0) {  /* allocation of memory for name failed. */
			return check;
		}

		(*grp).lastVerIdx++;

		return 0;
	}
	
	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted ) {
			check=addVertexAtIndex(grp, nam, i); /* adding at index i. */

			if (check!=0) {  /* allocation of memory for name failed. */
				return check;
			} else {
				return 0;
			}
		}
	}
	return 0;
}
/* end of add_vertex. */


int remove_vertex_id(graph *grp, int id) {
	
	if (id > (*grp).lastVerIdx) {  /* out of range. */
		return ERROR_VER_NOT_EXIST;
	}

	if ( (*((*grp).vertices+id)).deleted ) { /* vertex doesn't exist. */
		return ERROR_VER_NOT_EXIST;
	}

	if (degree0(grp,id)==false) { /* edges attached to it. */
		return ERROR_VER_DEGREE_NOT_ZERO;
	}
	
	(*((*grp).vertices+id)).deleted=true;
	(*grp).numVertices--;

	free((*((*grp).vertices+id)).name);  /* memory free. */

	if (id==(*grp).lastVerIdx) {
		(*grp).lastVerIdx--;
	}

	return 0;
}
/* end of remove_vertex_id. */

int remove_vertex_name(graph *grp, char* nam) {

	int firstInd=isVertexNameExist(grp, nam);

	if (firstInd==-1) {   /* vertex doesn't exist. */
		return ERROR_VER_NOT_EXIST;
	}

	if (moreThanOnce(grp, nam)) {  /* 'name' appears more than once. */
		return ERROR_VER_SAME_NAME;
	}

	return remove_vertex_id(grp, firstInd); /* using prev. proc. */
}
/* end of remove_vertex_name. */


int add_edge_ids(graph* grp, int v1, int v2, double w) {
	int i=0, mem=0;

	if ( (v1 > (*grp).lastVerIdx) || (v2 > (*grp).lastVerIdx) ) {  /* out of range. */
		return ERROR_VER_NOT_FOUND;
	}

	if ( ((*((*grp).vertices+v1)).deleted) || ((*((*grp).vertices+v2)).deleted) ) {
		return ERROR_VER_NOT_FOUND;
	}

	if (v1==v2) {
		return ERROR_SELF_LOOPS;
	}

	for(i=0; i<=(*grp).lastEdgeIdx; i++) {
		if ((*((*grp).edges+i)).deleted == false) {   /* existing edge. */
			if ( ( ((*((*grp).edges+i)).v1_id==v1) && ((*((*grp).edges+i)).v2_id==v2) ) ||
				 ( ((*((*grp).edges+i)).v1_id==v2) && ((*((*grp).edges+i)).v2_id==v1) ) ) {
				 /* duplicate edge detected. */
				return ERROR_EDGE_DUPLICATE;
			}
		}
	}


	if ( (*grp).numEdges == (*grp).lastEdgeIdx+1 ) {

		if ( (*grp).numEdges == ((*grp).edgeLength * group) ) { /* array is full. */
			mem=addMemory(grp, 0);
			if (mem!=0) {   /* reallocation failed. */
				return mem;
			}
		}

		addEdgeAtIndex(grp, (*grp).lastEdgeIdx+1, v1, v2, w); /* at the very end. */

		(*grp).lastEdgeIdx++;

		return 0;
	}

	for(i=0; i<=(*grp).lastEdgeIdx; i++) {
		if ( (*((*grp).edges+i)).deleted ) {

			addEdgeAtIndex(grp, i, v1, v2, w); /* at index i. */
	
			return 0;
		}
	}
	return 0;
}
/* end of add_edge_ids. */


int add_edge_names(graph* grp, char* v1, char* v2, double w) {
	int v1exist=isVertexNameExist(grp, v1);
	int v2exist=isVertexNameExist(grp, v2);

	if ( (v1exist==-1) || (v2exist==-1) ) {
		return ERROR_VER_NOT_FOUND;
	}

	if ( (moreThanOnce(grp, v1)) || (moreThanOnce(grp, v2)) ) {
		return ERROR_VER_NOT_FOUND;
	}

	return add_edge_ids(grp, v1exist, v2exist, w); /* using prev. proc. */
}
/* end of add_edge_names. */


int remove_edge(graph* grp, int id) {
	
	if (id > (*grp).lastEdgeIdx) {  /* out of range. */
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
/* end of remove_edge. */


void print(graph *grp, bool clust) { /* cluster will call print with clust=1. */
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
/* end of print. */


void cluster(graph *grp, int k) {
	double score=0;
	int i=0;
	int v1=0, v2=0;  /* for edge ends. */

	srand((int)time(0));  /* init. random num. generator. */

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {  /* existing vertex. */
			(*((*grp).vertices+i)).cluster = (rand() % k) + 1;
			/* giving each vertex random clust. # from 1 to k. */
		}
	}


	for(i=0; i<=(*grp).lastEdgeIdx; i++) { /* calculating the score of the clustering. */
		if ( (*((*grp).edges+i)).deleted == false) {  /* existing edge. */
			v1=(*((*grp).edges+i)).v1_id;
			v2=(*((*grp).edges+i)).v2_id;

			if ( (*((*grp).vertices+v1)).cluster == (*((*grp).vertices+v2)).cluster ) {
				/* edge is in a cluster. */
				score=score+(*((*grp).edges+i)).weight;
			} else {   /* edge is between clusters. */
				score=score-(*((*grp).edges+i)).weight;
			}	
		}
	}

	print(grp, true);  /* printing. */

	printf("The random clustering score for %d clusters is %.3f\n", k, score);  
}  
/* end of cluster. */
