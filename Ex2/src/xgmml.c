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

/* obtains clusters sizes. 
time complexity is linear in graph size. */
void calculate_clusters_sizes(graph* grp, clusterProperties* clusters, int K) {

	int i;
	int numOfVertices;
	vertex ver;
	vertex* vertices;

	vertices = grp->vertices;
	numOfVertices = grp->numOfVertices;

	/* Init the clusters properties */
	for (i=0;i<K;i++) {
		clusters[i].idNum = i+1;
		clusters[i].orderNum = 0;
		clusters[i].numOfVertices = 0;
	}

	for (i=0;i<numOfVertices;i++) {	
		ver = vertices[i]; 
		clusters[ver.cluster-1].numOfVertices += 1;
	}
}

void xml_save_doc(xmlDocPtr pXMLDom, const char* filename, const char* encoding) {
	xmlSaveFileEnc(filename, pXMLDom, encoding);
}

void xml_free_doc(xmlDocPtr pXMLDom) {
	xmlFreeDoc(pXMLDom);
}

void xmlUnlinkAndFree(xmlNodePtr pNode) {
	xmlUnlinkNode(pNode);
	xmlFreeNode(pNode);
}


/* generates xml doc. for graph, without cluster painting (for reuse). */

xmlDocPtr generate_xml(graph *grp) {
	char str[LONGEST_STR]; 
	char tmp[LONGEST_NUM]; 
	int i,V,E;
	vertex v,u;	
	edge e;
	vertex* vertices;
	edge* edges;
	xmlNodePtr pNode,pRoot;
	xmlDocPtr pXMLDom;

	/* create (and initialize) the XGMML document. */
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
	V = grp->numOfVertices;
	E = grp->numOfEdges;
	
	/* create vertex nodes. */
	for (i=0;i<V;i++) {
		v = vertices[i];
		pNode=xmlNewChild(pRoot,NULL,BAD_CAST "node",NULL);

		xmlNewProp(pNode,BAD_CAST "label",BAD_CAST v.name);
		sprintf(tmp,"%d",i+1); /* tmp gets vertex id. */
		xmlNewProp(pNode,BAD_CAST "id",BAD_CAST tmp);

		pNode=xmlNewChild(pNode,NULL,BAD_CAST "graphics",NULL); /* graphics child. */
		xmlNewProp(pNode,BAD_CAST "type",BAD_CAST GRAPHICS_TYPE);
		xmlNewProp(pNode,BAD_CAST "fill",NULL);
		xmlNewProp(pNode,BAD_CAST "width",BAD_CAST GRAPHICS_WIDTH);
		xmlNewProp(pNode,BAD_CAST "cy:nodeLabel",BAD_CAST v.name);
		xmlNewProp(pNode,BAD_CAST "cy:borderLineType",BAD_CAST GRAPHICS_BORDER);
	}

	/* create edge nodes. */
	for (i=0;i<E;i++) {
		e = edges[i];
		v = vertices[e.v1_id];
		u = vertices[e.v2_id];
		pNode=xmlNewChild(pRoot,NULL,BAD_CAST "edge",NULL);	

		sprintf(str,"%s (pp) %s",v.name,u.name);    
		xmlNewProp(pNode,BAD_CAST "label",BAD_CAST str);
		sprintf(tmp,"%d",e.v1_id+1); /* tmp gets vertex v1_id. */
		xmlNewProp(pNode,BAD_CAST "source",BAD_CAST tmp);
		sprintf(tmp,"%d",e.v2_id+1); /* tmp gets vertex v2_id. */
		xmlNewProp(pNode,BAD_CAST "target",BAD_CAST tmp);

		pNode=xmlNewChild(pNode,NULL,BAD_CAST "graphics",NULL); /* graphics child. */
		sprintf(tmp,"weight=%.3f",e.weight);
		xmlNewProp(pNode,BAD_CAST "cy:edgeLabel",BAD_CAST tmp);
	}

	return pXMLDom;
}

/* paints nodes according to clustered graph. */

void paint_xml(xmlDocPtr pXMLDom, graph *grp, clusterProperties *clusters, int k) {
	int i,V;
	vertex v;
	xmlNodePtr pRoot,pNode,pGraphics;
	vertex *vertices;
	char tmp[LONGEST_STR];
	
	vertices = grp->vertices;
	V = grp->numOfVertices;

	calculate_clusters_sizes(grp,clusters,k);

	/* descending order sort by size. */
	qsort(clusters,k,sizeof(clusterProperties),compare_clusters_size);

	pRoot = xmlDocGetRootElement(pXMLDom);
	pNode = xmlFirstElementChild(pRoot);

	/* changes the label of graph's root element */
	sprintf(tmp,"%d_clustering_solution",k);
	xmlSetProp(pRoot, BAD_CAST "label", BAD_CAST tmp);

	for (i=0;i<V;i++) {
		v = vertices[i];
		pGraphics = xmlFirstElementChild(pNode);
	
		/* painting node according to size of cluster it belongs to. */

		if (v.cluster==clusters[0].idNum) /* biggest cluster. */
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#00FFFF");
		else if (v.cluster==clusters[1].idNum)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#0000FF");
		else if (v.cluster==clusters[2].idNum)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#8A2BE2");
		else if (v.cluster==clusters[3].idNum)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#A52A2A");
		else if (v.cluster==clusters[4].idNum)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#7FFF00");
		else if (v.cluster==clusters[5].idNum)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#006400");
		else if (v.cluster==clusters[6].idNum)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FFD700");
		else if (v.cluster==clusters[7].idNum)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FF69B4");
		else if (v.cluster==clusters[8].idNum)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FF4500");
		else /* 10th sized cluster and above. */
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#C0C0C0");

		pNode = xmlNextElementSibling(pNode);
	}

}

/* cleans the xml tree, to leave only 'big clusters'. */

void trim_xml(xmlDocPtr pXMLDom, graph *grp, clusterProperties *clusters, int numClustersUpperBound) {
	int i;
	int numOfVertices,numOfEdges;
	vertex v,u;	
	edge e;
	vertex* vertices;
	edge* edges;
	xmlNodePtr pRoot,pNode;

	numOfVertices = grp->numOfVertices;
	numOfEdges = grp->numOfEdges;
	vertices = grp->vertices;
	edges = grp->edges;

	pRoot = xmlDocGetRootElement(pXMLDom);

	/* changes the label of graph's root element */
	xmlSetProp(pRoot, BAD_CAST "label", BAD_CAST "best_clusters");

	if (numClustersUpperBound<=THRESHOLD) { /* we don't need to clean-up the tree in that case */
		return;
	}
	
	calculate_clusters_sizes(grp,clusters,numClustersUpperBound);

	/* sorting by cluster size descending order. */
	qsort(clusters,numClustersUpperBound,sizeof(clusterProperties),compare_clusters_size);

	/* assigning ascending order, to filter in case of equal cluster sizes. */
	for(i=0; i<numClustersUpperBound;i++) {
		clusters[i].orderNum=i;			
	}

	/* sorting by cluster id ascending order. */
	qsort(clusters,numClustersUpperBound,sizeof(clusterProperties),compare_clusters_id);

	pNode = xmlFirstElementChild(pRoot);

	/* free not-needed vertex nodes. */
	for(i=0;i<numOfVertices;i++) {
		v = vertices[i];
		if (clusters[v.cluster-1].orderNum >= THRESHOLD) {
			xmlUnlinkAndFree(pNode); /* outside 5 biggest clusters. */
		}
		pNode = xmlNextElementSibling(pNode);
	}

	/* free not-needed edge nodes. */
	for(i=0;i<numOfEdges;i++) {
		e = edges[i];
		v = vertices[e.v1_id];
		u = vertices[e.v2_id];
		if ((clusters[v.cluster-1].orderNum >= THRESHOLD) || (clusters[u.cluster-1].orderNum >= THRESHOLD)) {
			xmlUnlinkAndFree(pNode); /* one of v,u outside 5 biggest clusters. */
		}
		pNode = xmlNextElementSibling(pNode);
	}
}
