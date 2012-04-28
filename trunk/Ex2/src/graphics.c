#include "graphics.h"

/* obtains clusters sizes. 
time complexity is linear in graph size. */

void sizeClusters(graph* grp, Cluster* clusters, int K) {

	int i,V;
	vertex v;
	vertex* vertices;

	vertices = grp->vertices;
	V = grp->numVer;

	for (i=0;i<K;i++) {
		clusters[i].id = i+1;
		clusters[i].order = 0; /* init. */
		clusters[i].size = 0;
	}
	for (i=0;i<V;i++) {	
		v = vertices[i]; 
		clusters[v.cluster-1].size += 1;
	}
}

/* compares clusters by their size (for later sorting). */

int compare_clusters_size(const void* c1, const void* c2) {
	if (((Cluster*)c1)->size > ((Cluster*)c2)->size) {
		return -1; /* for descending order sort. */
	} else { 
		if (((Cluster*)c1)->size < ((Cluster*)c2)->size)  {
			return 1;
		} else {
			return 0;
		}
	}
}

/* compares clusters by their id (for later sorting). */

int compare_clusters_id(const  void* c1, const void* c2)  {
	if (((Cluster*)c1)->id > ((Cluster*)c2)->id) {
		return 1; 
	} else {  
		if (((Cluster*)c1)->id < ((Cluster*)c2)->id) {
			return -1;
		} else {
			return 0;
		}
	}
}

void xmlSaveDoc(xmlDocPtr pXMLDom, const char* filename, const char* encoding) {
	xmlSaveFileEnc(filename, pXMLDom, encoding);
}

void xmlFreeDocument(xmlDocPtr pXMLDom) {
	xmlFreeDoc(pXMLDom);
}

void xmlUnlinkAndFree(xmlNodePtr pNode) {
	xmlUnlinkNode(pNode);
	xmlFreeNode(pNode);
}


/* generates xml doc. for graph, without cluster painting (for reuse). */

xmlDocPtr generateXML(graph *grp) {
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
	V = grp->numVer;
	E = grp->numEdg;
	
	/* create vertex nodes. */
	for (i=0;i<V;i++) {
		v = vertices[i];
		pNode=xmlNewChild(pRoot,NULL,BAD_CAST "node",NULL);

		xmlNewProp(pNode,BAD_CAST "label",BAD_CAST v.name);
		sprintf(tmp,"%d",i+1); /* tmp gets vertex id. */
		xmlNewProp(pNode,BAD_CAST "id",BAD_CAST tmp);

		pNode=xmlNewChild(pNode,NULL,BAD_CAST "graphics",NULL); /* graphics child. */
		xmlNewProp(pNode,BAD_CAST "type",BAD_CAST TYPE);
		xmlNewProp(pNode,BAD_CAST "fill",NULL);
		xmlNewProp(pNode,BAD_CAST "width",BAD_CAST WIDTH);
		xmlNewProp(pNode,BAD_CAST "cy:nodeLabel",BAD_CAST v.name);
		xmlNewProp(pNode,BAD_CAST "cy:borderLineType",BAD_CAST LINE);
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

void paintXML(xmlDocPtr pXMLDom, graph *grp, Cluster *clusters, int k) {
	int i,V;
	vertex v;
	xmlNodePtr pRoot,pNode,pGraphics;
	vertex *vertices;
	char tmp[LONGEST_STR];
	
	vertices = grp->vertices;
	V = grp->numVer;

	sizeClusters(grp,clusters,k);

	/* descending order sort by size. */
	qsort(clusters,k,sizeof(Cluster),compare_clusters_size);

	pRoot = xmlDocGetRootElement(pXMLDom);
	pNode = xmlFirstElementChild(pRoot);

	/* changes the label of graph's root element */
	sprintf(tmp,"%d_clustering_solution",k);
	xmlSetProp(pRoot, BAD_CAST "label", BAD_CAST tmp);

	for (i=0;i<V;i++) {
		v = vertices[i];
		pGraphics = xmlFirstElementChild(pNode);
	
		/* painting node according to size of cluster it belongs to. */

		if (v.cluster==clusters[0].id) /* biggest cluster. */
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#00FFFF");
		else if (v.cluster==clusters[1].id)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#0000FF");
		else if (v.cluster==clusters[2].id)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#8A2BE2");
		else if (v.cluster==clusters[3].id)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#A52A2A");
		else if (v.cluster==clusters[4].id)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#7FFF00");
		else if (v.cluster==clusters[5].id)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#006400");
		else if (v.cluster==clusters[6].id)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FFD700");
		else if (v.cluster==clusters[7].id)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FF69B4");
		else if (v.cluster==clusters[8].id)
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#FF4500");
		else /* 10th sized cluster and above. */
			xmlSetProp(pGraphics,BAD_CAST "fill",BAD_CAST "#C0C0C0");

		pNode = xmlNextElementSibling(pNode);
	}

}

/* cleans the xml tree, to leave only 'big clusters'. */

void trimXML(xmlDocPtr pXMLDom, graph *grp, Cluster *clusters, int U) {
	int i,V,E;
	vertex v,u;	
	edge e;
	vertex* vertices;
	edge* edges;
	xmlNodePtr pRoot,pNode;

	V = grp->numVer;
	E = grp->numEdg;
	vertices = grp->vertices;
	edges = grp->edges;

	pRoot = xmlDocGetRootElement(pXMLDom);

	/* changes the label of graph's root element */
	xmlSetProp(pRoot, BAD_CAST "label", BAD_CAST "best_clusters");

	if (U<=THRESHOLD) { /* we don't need to clean-up the tree in that case */
		return;
	}
	
	sizeClusters(grp,clusters,U);

	/* sorting by cluster size descending order. */
	qsort(clusters,U,sizeof(Cluster),compare_clusters_size);

	/* assigning ascending order, to filter in case of equal cluster sizes. */
	for(i=0; i<U;i++) {
		clusters[i].order=i;			
	}

	/* sorting by cluster id ascending order. */
	qsort(clusters,U,sizeof(Cluster),compare_clusters_id);

	pNode = xmlFirstElementChild(pRoot);

	/* free not-needed vertex nodes. */
	for(i=0;i<V;i++) {
		v = vertices[i];
		if (clusters[v.cluster-1].order >= THRESHOLD) {
			xmlUnlinkAndFree(pNode); /* outside 5 biggest clusters. */
		}
		pNode = xmlNextElementSibling(pNode);
	}

	/* free not-needed edge nodes. */
	for(i=0;i<E;i++) {
		e = edges[i];
		v = vertices[e.v1_id];
		u = vertices[e.v2_id];
		if ((clusters[v.cluster-1].order >= THRESHOLD) || (clusters[u.cluster-1].order >= THRESHOLD)) {
			xmlUnlinkAndFree(pNode); /* one of v,u outside 5 biggest clusters. */
		}
		pNode = xmlNextElementSibling(pNode);
	}
}
