#include "xgmml.h"

/* The function comapares between clusters by their size */
int compare_clusters_size(const void* cluster1, const void* cluster2) {
	if (((clusterProperties*)cluster1)->numOfVertices > ((clusterProperties*)cluster2)->numOfVertices) {
		return -1;
	} else {
		if (((clusterProperties*)cluster1)->numOfVertices < ((clusterProperties*)cluster2)->numOfVertices)  {
			return 1;
		} else {
			return 0;
		}
	}
}

/* The function compares between clusters by their id */
int compare_clusters_id(const  void* cluster1, const void* cluster2)  {
	if (((clusterProperties*)cluster1)->idNum > ((clusterProperties*)cluster2)->idNum) {
		return 1;
	} else {
		if (((clusterProperties*)cluster1)->idNum < ((clusterProperties*)cluster2)->idNum) {
			return -1;
		} else {
			return 0;
		}
	}
}

/* The function calculates the clusters sizes */
void calculate_clusters_sizes(graph* grp, clusterProperties* clusters, int numOfClusters) {

	int i;
	int numOfVertices;
	vertex ver;
	vertex* vertices;

	vertices = grp->vertices;
	numOfVertices = grp->numOfVertices;

	/* Initiate all the clusters properties */
	for (i=0;i<numOfClusters;i++) {
		clusters[i].idNum = i+1;
		clusters[i].orderNum = 0;
		clusters[i].numOfVertices = 0;
	}

	for (i=0;i<numOfVertices;i++) {	
		ver = vertices[i]; 
		clusters[ver.cluster-1].numOfVertices += 1;
	}
}

void xml_unlink_free(xmlNodePtr pNode) {
	xmlUnlinkNode(pNode);
	xmlFreeNode(pNode);
}

/* The function creates the xml document */
xmlDocPtr create_xml(graph *grp) {
	char str[LONGEST_STR]; 
	char tmp[LONGEST_NUM]; 
	
	int i;
	int numOfVertices,numOfEdges;
	
	vertex vertex1,vertex2;	
	edge edge1;
	vertex* vertices;
	edge* edges;
	
	xmlNodePtr pNode,pRoot;
	xmlDocPtr pXMLDom;

	/* Init xgmml document */
	pXMLDom = xmlNewDoc(BAD_CAST "1.0");
	pRoot = xmlNewNode(NULL, BAD_CAST "graph");
	
	xmlNewProp(pRoot, BAD_CAST "directed", BAD_CAST "0");
	xmlNewProp(pRoot, BAD_CAST "xmlns", BAD_CAST "http://www.cs.rpi.edu/XGMML");
	xmlNewProp(pRoot, BAD_CAST "xmlns:xlink", BAD_CAST "http://www.w3.org/1999/xlink");
	xmlNewProp(pRoot, BAD_CAST "xmlns:rdf", BAD_CAST "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
	xmlNewProp(pRoot, BAD_CAST "xmlns:dc", BAD_CAST "http://purl.org/dc/elements/1.1/");
	xmlNewProp(pRoot, BAD_CAST "label", NULL);
	xmlNewProp(pRoot, BAD_CAST "xmlns:cy", BAD_CAST "http://www.cytoscape.org");

	xmlDocSetRootElement(pXMLDom,pRoot);

	pRoot = xmlDocGetRootElement(pXMLDom);
	vertices = grp->vertices;
	edges = grp->edges;

	numOfVertices = grp->numOfVertices;
	numOfEdges = grp->numOfEdges;
	
	/* add all vertices */
	for (i=0; i < numOfVertices; i++) {
		vertex1 = vertices[i];
		pNode=xmlNewChild(pRoot, NULL, BAD_CAST "node", NULL);

		xmlNewProp(pNode, BAD_CAST "label", BAD_CAST vertex1.name);
		sprintf(tmp, "%d", i+1);
		xmlNewProp(pNode, BAD_CAST "id", BAD_CAST tmp);

		pNode=xmlNewChild(pNode, NULL, BAD_CAST "graphics", NULL);
		xmlNewProp(pNode, BAD_CAST "type", BAD_CAST GRAPHICS_TYPE);
		xmlNewProp(pNode, BAD_CAST "fill", NULL);
		xmlNewProp(pNode, BAD_CAST "width", BAD_CAST GRAPHICS_WIDTH);
		xmlNewProp(pNode, BAD_CAST "cy:nodeLabel", BAD_CAST vertex1.name);
		xmlNewProp(pNode, BAD_CAST "cy:borderLineType", BAD_CAST GRAPHICS_BORDER);
	}

	/* add all edges */
	for (i=0; i < numOfEdges; i++) {
		edge1 = edges[i];
		vertex1 = vertices[edge1.v1_id];
		vertex2 = vertices[edge1.v2_id];
		pNode=xmlNewChild(pRoot, NULL, BAD_CAST "edge", NULL);

		sprintf(str, "%s (pp) %s", vertex1.name, vertex2.name);
		xmlNewProp(pNode, BAD_CAST "label", BAD_CAST str);
		sprintf(tmp,"%d", edge1.v1_id+1);
		xmlNewProp(pNode, BAD_CAST "source", BAD_CAST tmp);
		sprintf(tmp,"%d", edge1.v2_id+1);
		xmlNewProp(pNode, BAD_CAST "target", BAD_CAST tmp);

		pNode=xmlNewChild(pNode, NULL, BAD_CAST "graphics", NULL);
		sprintf(tmp, "weight=%.3f",edge1.weight);
		xmlNewProp(pNode, BAD_CAST "cy:edgeLabel", BAD_CAST tmp);
	}

	return pXMLDom;
}

/* The function colors the nodes in xml according to clusters size */
void color_xml_nodes(xmlDocPtr pXMLDom, graph *grp, clusterProperties *clusters, int numOfClusters) {
	int i;
	int numOfVertices;

	vertex v;
	xmlNodePtr pRoot,pNode,pGraphics;
	vertex *vertices;
	char tmp[LONGEST_STR];
	
	vertices = grp->vertices;
	numOfVertices = grp->numOfVertices;

	calculate_clusters_sizes(grp,clusters,numOfClusters);

	/* descending order sort by size. */
	qsort(clusters, numOfClusters, sizeof(clusterProperties), compare_clusters_size);

	pRoot = xmlDocGetRootElement(pXMLDom);
	pNode = xmlFirstElementChild(pRoot);

	/* changes the label of graph's root element */
	sprintf(tmp,"%d_clustering_solution",numOfClusters);
	xmlSetProp(pRoot, BAD_CAST "label", BAD_CAST tmp);

	for (i=0; i < numOfVertices; i++) {
		v = vertices[i];
		pGraphics = xmlFirstElementChild(pNode);
	
		/* painting node according to size of cluster it belongs to. */

		if (v.cluster==clusters[0].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#00FFFF");

		} else if (v.cluster==clusters[1].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#0000FF");

		} else if (v.cluster==clusters[2].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#8A2BE2");

		} else if (v.cluster==clusters[3].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#A52A2A");

		} else if (v.cluster==clusters[4].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#7FFF00");

		} else if (v.cluster==clusters[5].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#006400");

		} else if (v.cluster==clusters[6].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FFD700");

		} else if (v.cluster==clusters[7].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FF69B4");

		} else if (v.cluster==clusters[8].idNum) {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FF4500");

		} else {
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#C0C0C0");
		}

		pNode = xmlNextElementSibling(pNode);
	}

}

/* The function removes small clusters from the xml */
void remove_xml_small_clusters(xmlDocPtr pXMLDom, graph *grp, clusterProperties *clusters, int numClustersUpperBound) {
	int i;
	int numOfVertices,numOfEdges;

	vertex vertex1,vertex2;	
	edge edge1;
	vertex* vertices;
	edge* edges;

	xmlNodePtr ptrRoot;
	xmlNodePtr ptrNode;
	xmlNodePtr ptrTmpNode=NULL;

	numOfVertices = grp->numOfVertices;
	numOfEdges = grp->numOfEdges;
	vertices = grp->vertices;
	edges = grp->edges;

	ptrRoot = xmlDocGetRootElement(pXMLDom);

	xmlSetProp(ptrRoot, BAD_CAST "label", BAD_CAST "best_clusters");

	if (numClustersUpperBound <= THRESHOLD) {
		return;
	}
	
	calculate_clusters_sizes(grp,clusters,numClustersUpperBound);

	/* sort clusters by size */
	qsort(clusters,numClustersUpperBound,sizeof(clusterProperties),compare_clusters_size);

	/* saving order data */
	for(i=0; i < numClustersUpperBound; i++) {
		clusters[i].orderNum=i;			
	}

	/* sort clusters by id */
	qsort(clusters, numClustersUpperBound, sizeof(clusterProperties), compare_clusters_id);

	ptrNode = xmlFirstElementChild(ptrRoot);

	/* free nodes of vertices */
	for(i=0; i < numOfVertices; i++) {
		vertex1 = vertices[i];
		if (clusters[vertex1.cluster-1].orderNum >= THRESHOLD) {
			ptrTmpNode=xmlNextElementSibling(ptrNode);
			xml_unlink_free(ptrNode);
			ptrNode=ptrTmpNode;
		} else {
			ptrNode = xmlNextElementSibling(ptrNode);
		}
	}

	/* free nodes of edges */
	for(i=0;i<numOfEdges;i++) {
		edge1 = edges[i];
		vertex1 = vertices[edge1.v1_id];
		vertex2 = vertices[edge1.v2_id];
		if ((clusters[vertex1.cluster-1].orderNum >= THRESHOLD) || (clusters[vertex2.cluster-1].orderNum >= THRESHOLD)) {
			ptrTmpNode=xmlNextElementSibling(ptrNode);
			xml_unlink_free(ptrNode);
			ptrNode=ptrTmpNode;
		} else {
			ptrNode = xmlNextElementSibling(ptrNode);
		}
	}
}

/* The function frees the xml document */
void free_xml_doc(xmlDocPtr pXMLDom) {
	xmlFreeDoc(pXMLDom);
}

/* The function saves the xml document */
void save_xml_doc(xmlDocPtr pXMLDom, const char* filename, const char* encoding) {
	xmlSaveFileEnc(filename, pXMLDom, encoding);
}
