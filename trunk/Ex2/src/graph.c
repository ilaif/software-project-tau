#include "graph.h"

#define GROUP 20

/* The function returns true if there are multiple vertices with the received name */
bool is_multiple_vertex_name(graph *grp, char* vertexName) {
	int i=0, counter=0;

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		/* vertex not deleted */
		if ( (*((*grp).vertices+i)).deleted == false ) {
			if (strcmp(vertexName, (*((*grp).vertices+i)).name) == 0) {
				counter++;
				if (counter>1) {
					return true;
				}
			}
		}
	}

	return false;
}

/* The function returns the first index of a vertex with the received name, if there is none returns -1 */
int is_vertex_name_exist(graph *grp, char* vertexName) {
	int i=0;

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		/* vertex not deleted */
		if ( (*((*grp).vertices+i)).deleted == false ) {
			if (strcmp(vertexName, (*((*grp).vertices+i)).name) == 0) {
				return i;
			}
		}
	}

	return -1;
}

/* The function returns true if the degree of the received vertex is 0, otherwise false */
bool is_vertex_degree_zero(graph *grp, int id) {
	int i=0;

	for(i=0; i<=(*grp).lastEdgIdx; i++) {
		/* edge not deleted */
		if ((*((*grp).edges+i)).deleted == false) {
			if ( ((*((*grp).edges+i)).v1_id==id) || ((*((*grp).edges+i)).v2_id==id) ) {
				return false;
			}
		}
	}

	return true;
}

/* The function insert a new vertex at idx */
int insert_vertex(graph *grp, char* vertexName, int idx) {
	char* tmp;	

	(*((*grp).vertices+idx)).deleted=false;
	(*((*grp).vertices+idx)).cluster=0;
	tmp=(char*)calloc(((int)strlen(vertexName)+1), sizeof(char));
	if (tmp==NULL) {  /* failed. */
		return ERROR_MALLOC_FAILED;
	}

	(*((*grp).vertices+idx)).name=tmp;
	tmp=NULL;
	strcpy((*((*grp).vertices+idx)).name,vertexName);

	(*grp).numOfVertices++;
	return 0;
}

/* The function adds memory to an array
 * 1: Vertices array
 * 0: Edges array */
int add_memory(graph *grp, int verticesOrEdges) {
	vertex* tmpVer=NULL;
	edge* tmpEdg=NULL;

	/* Vertices array */
	if (verticesOrEdges==1) {
		if ( (*grp).numOfVerticesGroups == 0 ) {
			tmpVer=(vertex*)calloc(GROUP, sizeof(vertex));
			if (tmpVer == NULL) {
				return ERROR_MALLOC_FAILED;
			}
			(*grp).vertices = tmpVer;
			tmpVer=NULL;
			(*grp).numOfVerticesGroups++;
			return 0;
		} else {
			tmpVer=(vertex*)realloc((*grp).vertices, (GROUP*((*grp).numOfVerticesGroups+1))*sizeof(vertex));
			if (tmpVer == NULL) {
				return ERROR_MALLOC_FAILED;
			}
			(*grp).vertices = tmpVer;
			tmpVer=NULL;
			(*grp).numOfVerticesGroups++;
			return 0;
		}

	/* Edges array */
	} else {
		if ( (*grp).numOfEdgesGroups == 0 ) {
			tmpEdg=(edge*)calloc(GROUP, sizeof(edge));
			if (tmpEdg == NULL) {
				return ERROR_MALLOC_FAILED;
			}
			(*grp).edges = tmpEdg;
			tmpEdg=NULL;
			(*grp).numOfEdgesGroups++;
			return 0;
		} else {
			tmpEdg=(edge*)realloc((*grp).edges, (GROUP*((*grp).numOfEdgesGroups+1))*sizeof(edge));
			if (tmpEdg == NULL) {
				return ERROR_MALLOC_FAILED;
			}
			(*grp).edges = tmpEdg;
			tmpEdg=NULL;
			(*grp).numOfEdgesGroups++;
			return 0;
		}
	}
}

/* The function insert a new edge at idx */
void insert_edge(graph *grp, int idx, int vertex1, int vertex2, double weight) {
	(*((*grp).edges+idx)).deleted=false;
	(*((*grp).edges+idx)).weight=weight;
	(*((*grp).edges+idx)).v1_id=vertex1;
	(*((*grp).edges+idx)).v2_id=vertex2;

	(*grp).numOfEdges++;
}

/* The function adds new edge using vertices ids */
int add_edge_by_ids(graph* grp, int vertex1, int vertex2, double weight) {
	int i=0;
	int mem=0;

	/* Check if ids are within range */
	if ((vertex1 > (*grp).lastVerIdx) || (vertex2 > (*grp).lastVerIdx)) {
		return ERROR_VERTEX_NOT_FOUND;
	}

	/* Check that vertices aren't deleted */
	if ( ((*((*grp).vertices+vertex1)).deleted) || ((*((*grp).vertices+vertex2)).deleted) ) {
		return ERROR_VERTEX_NOT_FOUND;
	}

	/* Check for self loops */
	if (vertex1==vertex2) {
		return ERROR_INVALID_LOOP;
	}

	for(i=0; i<=(*grp).lastEdgIdx; i++) {
		if ((*((*grp).edges+i)).deleted == false) {
			if ( ( ((*((*grp).edges+i)).v1_id==vertex1) && ((*((*grp).edges+i)).v2_id==vertex2) ) ||
				 ( ((*((*grp).edges+i)).v1_id==vertex2) && ((*((*grp).edges+i)).v2_id==vertex1) ) ) {
				return ERROR_DUPLICATED_EDGES;
			}
		}
	}


	if ( (*grp).numOfEdges == (*grp).lastEdgIdx+1 ) {

		if ( (*grp).numOfEdges == ((*grp).numOfEdgesGroups * GROUP) ) { /* array is full. */
			mem=add_memory(grp, 0);
			if (mem!=0) {   /* reallocation failed. */
				return mem;
			}
		}

		insert_edge(grp, (*grp).lastEdgIdx+1, vertex1, vertex2, weight); /* at the very end. */
		(*grp).lastEdgIdx++;
		return 0;
	}

	for(i=0; i<=(*grp).lastEdgIdx; i++) {
		if ( (*((*grp).edges+i)).deleted ) {
			insert_edge(grp, i, vertex1, vertex2, weight); /* at index i. */
			return 0;
		}
	}
	return 0;
}

/* The function adds a new edge using vertices names */
int add_edge_by_names(graph* grp, char* vertex1, char* vertex2, double weight) {
	int vertex1id=is_vertex_name_exist(grp, vertex1);
	int vertex2id=is_vertex_name_exist(grp, vertex2);

	if ( (vertex1id==-1) || (vertex2id==-1) ) {
		return ERROR_VERTEX_NOT_FOUND;
	}
	if ( (is_multiple_vertex_name(grp, vertex1)) || (is_multiple_vertex_name(grp, vertex2)) ) {
		return ERROR_VERTEX_NOT_FOUND;
	}

	return add_edge_by_ids(grp, vertex1id, vertex2id, weight);
}

/* The function removes an edge by its id */
int remove_edge_by_id(graph* grp, int edgeId) {
	if (edgeId > (*grp).lastEdgIdx) {
		return ERROR_EDGE_NOT_EXIST;
	}
	if ((*((*grp).edges+edgeId)).deleted) {
		return ERROR_EDGE_NOT_EXIST;
	}

	(*((*grp).edges+edgeId)).deleted=true;
	(*grp).numOfEdges--;

	if (edgeId==(*grp).lastEdgIdx) {
		(*grp).lastEdgIdx--;
	}

	return 0;
}

/* The funcation adds a new vertex to the graph */
int add_vertex(graph *grp, char *vertexName) {
	int i=0, check=0;
	int mem=0;

	if ( (*grp).numOfVertices == (*grp).lastVerIdx+1 ) {
		if ( (*grp).numOfVertices == ((*grp).numOfVerticesGroups * GROUP) ) {
			mem=add_memory(grp, 1);
			if (mem!=0) {
				return mem;
			}
		}

		check=insert_vertex(grp, vertexName, (*grp).lastVerIdx+1);
		if (check!=0) {
			return check;
		}

		(*grp).lastVerIdx++;
		return 0;
	}

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted ) {
			check=insert_vertex(grp, vertexName, i);
			if (check!=0) {
				return check;
			} else {
				return 0;
			}
		}
	}

	return 0;
}

/* The function removes a vertex by its id */
int remove_vertex_by_id(graph *grp, int vertexId) {

	/* Check if id is in range */
	if (vertexId > (*grp).lastVerIdx) {
		return ERROR_VERTEX_NOT_EXIST;
	}

	/* Check if vertex already deleted */
	if ( (*((*grp).vertices+vertexId)).deleted ) {
		return ERROR_VERTEX_NOT_EXIST;
	}

	/* Check vertex degree */
	if (is_vertex_degree_zero(grp,vertexId)==false) {
		return ERROR_VERTEX_CANNOT_BE_DELETED;
	}

	(*((*grp).vertices+vertexId)).deleted=true;
	(*grp).numOfVertices--;

	free((*((*grp).vertices+vertexId)).name);

	if (vertexId==(*grp).lastVerIdx) {
		(*grp).lastVerIdx--;
	}

	return 0;
}

/* The function removes a vertex by its name */
int remove_vertex_by_name(graph *grp, char* vertexName) {
	int firstInd=is_vertex_name_exist(grp, vertexName);

	if (firstInd==-1) {
		return ERROR_VERTEX_NOT_EXIST;
	}
	if (is_multiple_vertex_name(grp, vertexName)) {
		return ERROR_MULTIPLE_VERTICES_NAME;
	}

	return remove_vertex_by_id(grp, firstInd); /* using prev. proc. */
}

/* The function prints all the graph data */
void print(graph *grp, bool printVertexCluster) {
	int i=0;
	char* vertexName1;
	char* vertexName2;

	if ((*grp).numOfVertices > 0) {
		printf("%d vertices:\n", (*grp).numOfVertices);
	}

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {
			printf("%d: ", i);
			printf("%s", (*((*grp).vertices+i)).name);

			if (printVertexCluster) {
				printf(" %d", (*((*grp).vertices+i)).cluster);
			}

			printf("\n");
		}

	}

	if ((*grp).numOfEdges > 0) {
		printf("%d edges:\n", (*grp).numOfEdges);
	}

	for(i=0; i<=(*grp).lastEdgIdx; i++) {
		if ((*((*grp).edges+i)).deleted == false) {
			printf("%d: ", i);

			vertexName1=(*((*grp).vertices+(*((*grp).edges+i)).v1_id)).name;
			vertexName2=(*((*grp).vertices+(*((*grp).edges+i)).v2_id)).name;

			printf("%s-%s %.3f",vertexName1,vertexName2,(*((*grp).edges+i)).weight);
			printf("\n");
		}
	}
}  

/* The function generates random clusters */
void cluster(graph *grp, int numOfClusters) {
	int vertex1=0;
	int vertex2=0;
	double score=0;
	int i=0;

	/* initiate random seed */
	srand((int)time(0));

	for(i=0; i<=(*grp).lastVerIdx; i++) {
		if ( (*((*grp).vertices+i)).deleted == false ) {
			(*((*grp).vertices+i)).cluster = (rand() % numOfClusters) + 1;
		}
	}

	for(i=0; i<=(*grp).lastEdgIdx; i++) {
		if ( (*((*grp).edges+i)).deleted == false) {
			vertex1=(*((*grp).edges+i)).v1_id;
			vertex2=(*((*grp).edges+i)).v2_id;

			if ( (*((*grp).vertices+vertex1)).cluster == (*((*grp).vertices+vertex2)).cluster ) {
				score=score+(*((*grp).edges+i)).weight;
			} else {
				score=score-(*((*grp).edges+i)).weight;
			}	
		}
	}

	print(grp, true);
	printf("The random clustering score for %d clusters is %.3f\n", numOfClusters, score);
}
