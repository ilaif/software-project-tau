//Full Name 1: Or Segal; Id No 1: 203993118; User Name 1: orsegal
//Full Name 2: Aviv Mor; Id No 2: 201254059; User Name 2: avivmor
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "graph.h"

vertex *ver; //vertices array
edge *ed; //edges array
int ver_len = 0, ed_len = 0, ver_count = 0, ed_count = 0, ver_last = -1, ed_last = -1;

void add_vertex(const char* name) {
	vertex *ver_tmp = NULL;
	//printf("length:%d	000%s000\n", strlen(name), name);//check
	//char* name_tmp = malloc((strlen(name)+1)*sizeof(char));
	//printf("length:%d	999%s999\n", strlen(name), name);//check
	//if (name_tmp == NULL){
	//	printf("Error: Memory allocation failed\n");
	//	return;
	//}
	//printf("111%s111\n", name);//check
	//strcpy(name_tmp,name);
	//printf("222%s222\n",name_tmp);//check
	int next = next_ver();
	//printf("%d 111\n%d 222\n", strlen(name), strlen(name_tmp));//check
	if (next == ver_len) {
		ver_tmp = realloc(ver,sizeof(vertex)*(ver_len + 20));
		if (ver_tmp == NULL) {
			printf("Error: Memory allocation failed\n");
			return;
		}
		ver = ver_tmp;
		ver_len += 20;
	}
	//printf("333%s333\n",name_tmp);//check
	//printf("%d 333\n%d 444\n", strlen(name), strlen(name_tmp));//check
	ver[next].name = malloc((strlen(name)+1)*sizeof(char));
	//printf("string %s\nlength %d\n", name_tmp, strlen(name_tmp));//check
	if (ver[next].name == NULL){
		printf("Error: Memory allocation failed\n");
		return;
	}
	strcpy(ver[next].name,name);
	ver[next].deleted = false;
	ver_count++;
	if (next > ver_last) {
		ver_last = next;
	}
	//free(name_tmp);
	//printf("%s\n", ver[next].name);//check
}

void remove_vertex_by_id(int id) {
	if (ver_exist(id) == false) {
		printf("Error: Requested vertex does not exist\n");
		return;
	}
	if (edges_attached(id) == true) {
		printf("Error: Cannot delete vertex %s since there are edges connected to it\n", ver[id].name);
		return;
	}
	free(ver[id].name);
	ver[id].deleted = true;
	ver_count--;
}

void remove_vertex_by_name(char* name) {
	int search = search_ver(name);
	if (search == -1) {
		printf("Error: Requested vertex does not exist\n");
		return;
	}
	if (search == -2) {
		printf("Error: More than one vertex with the requested name exists, please remove using id\n");
		return;
	}
	if (edges_attached(search) == true) {
		printf("Error: Cannot delete vertex %s since there are edges connected to it\n", ver[search].name);
		return;
	}
	free(ver[search].name);
	ver[search].deleted = true;
	ver_count--;
}

void add_edge_by_name(char* first_vertex_name, char* second_vertex_name, double weight) {
	int search_first = search_ver(first_vertex_name);
	int search_second = search_ver(second_vertex_name);
	if (search_first == -1 || search_second == -1) {
		printf("Error: First/second vertex was not found\n");
		return;
	}
	if (search_first == -2 || search_second == -2) {
		printf("Error: More than one vertex with the requested name exists, please add edge using vertices ids\n");
		return;
	}
	add_edge_by_id(search_first, search_second, weight);
}

void add_edge_by_id(int head_vertex_id, int tail_vertex_id, double weight) {
	edge *ed_tmp = NULL;
	if (ver_exist(head_vertex_id) == false || ver_exist(tail_vertex_id) == false) {
		printf("Error: First/second vertex was not found\n");
		return;
	}
	if(head_vertex_id == tail_vertex_id) {
		printf("Error: No self loops are allowed in the network\n");
		return;
	}
	if (edge_duplication(head_vertex_id, tail_vertex_id) == true) {
		printf("Error: No duplicated edges are allowed in the network\n");
		return;
	}
	int next = next_ed();
	if (next == ed_len) {
		ed_tmp = realloc(ed,sizeof(edge)*(ed_len + 20));
		if (ed_tmp == NULL) {
			printf("Error: Memory allocation failed\n");
			return;
		}
		ed = ed_tmp;
		ed_len += 20;
	}
	ed[next].v1_id = head_vertex_id;
	ed[next].v2_id = tail_vertex_id;
	ed[next].weight = weight;
	ed[next].deleted = false;
	ed_count++;
	if (next > ed_last) {
		ed_last = next;
	}
}

void remove_edge(int id) {
	if (ed_exist(id) == false) {
		printf("Error: Requested edge does not exist\n");
		return;
	}
	ed[id].deleted = true;
	ed_count--;
}

void print() {
	if (ver_count == 0) {
		return;
	}
	int i;
	printf("%d vertices:\n", ver_count);
	for (i = 0; i <= ver_last; ++i) {
		if (ver_exist(i) == true) {
			printf("%d: %s\n", i, ver[i].name);
		}
	}
	if (ed_count == 0) {
		return;
	}
	printf("%d edges: \n", ed_count);
	for (i = 0; i <= ed_last; ++i) {
		if (ed_exist(i) == true) {
			printf("%d: %s-%s %.3f\n", i, ver[ed[i].v1_id].name, ver[ed[i].v2_id].name, ed[i].weight);
		}
	}
}

void cluster(int num_of_clusters) {
	int i;
	double score = 0.0;
	srand(time(NULL));
	printf("%d vertices:\n", ver_count);
	for (i = 0; i <= ver_last; ++i) {
		if (ver_exist(i) == true) {
			ver[i].cluster = 1 + (rand() % num_of_clusters);
			printf("%d: %s %d\n", i, ver[i].name, ver[i].cluster);
		}
	}
	printf("%d edges: \n", ed_count);
	for (i = 0; i <= ed_last; ++i) {
		if (ed_exist(i) == true) {
			if (ver[ed[i].v1_id].cluster == ver[ed[i].v2_id].cluster) {
				score += ed[i].weight;
			}
			else {
				score -= ed[i].weight;
			}
			printf("%d: %s-%s %.3f\n", i, ver[ed[i].v1_id].name, ver[ed[i].v2_id].name, ed[i].weight);
		}
	}
	printf("The random clustering score for %d clusters is %.3f\n", num_of_clusters, score);
}

// this function prints an error message
void print_error(char* error) {
	printf("Error: %s \n", error);
}

void free_and_quit() {
	int i;
	free(ed);
	for (i = 0; i <= ver_last; ++i) {
		if (ver[i].deleted == false) {
			free(ver[i].name);
		}
	}
	free(ver);
}

int next_ver() {
	int i;
	for (i = 0; i <= ver_last; ++i) {
		if (ver[i].deleted == true){
			return i;
		}
	}
	return i;
}

int next_ed() {
	int i;
	for (i = 0; i <= ed_last; ++i) {
		if (ed[i].deleted == true){
			return i;
		}
	}
	return i;
}

//the function returns -1 if the name does not exist, -2 if there is more than one match,
//otherwise it returns the id with that name.
int search_ver(char* name) {
	int i, p = -1;
	for (i = 0; i <= ver_last; ++i) {
		if (ver[i].deleted == false && strcmp(ver[i].name,name) == 0) {
			if (p == -1) {
				p = i;
			}
			else {
				return -2;//
			}
		}
	}
	return p;
}

bool edge_duplication(int id1, int id2) {
	int i;
	for (i = 0; i < ed_len; ++i) {
		if (ed[i].deleted == false && ((ed[i].v1_id == id1 && ed[i].v2_id == id2) || (ed[i].v1_id == id2 && ed[i].v2_id == id1))) {
			return true;
		}
	}
	return false;
}

bool ver_exist(int id) {
	if(id > ver_last || id < 0 || ver[id].deleted == true) {
		return false;
	}
	return true;
}

bool ed_exist(int id) {
	if (id > ed_last || id < 0 || ed[id].deleted == true) {
		return false;
	}
	return true;
}

bool edges_attached(int id) {
	int i;
	for (i = 0; i <= ed_last; ++i) {
		if (ed_exist(i) == true && (ed[i].v1_id == id || ed[i].v2_id == id)) {
			return true;
		}
	}
	return false;
}
