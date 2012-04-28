/*------------------------------------------------------------------------*/
/*  File: cluster.c                                                       */
/*  Version 1.0                                                           */
/*------------------------------------------------------------------------*/
/*  Written for Software Project Class.                                   */
/*  Based uopn qpex1.c                                                    */
/*  Permission is expressly granted to use this example in the            */
/*  course of developing applications that use ILOG products.             */
/*------------------------------------------------------------------------*/

/* cluster.c - Entering and optimizing k-clustering integer linear programming problem */

#include "cluster.h"

/* for coping with solution values 'close' to 1 (accuracy) */ 
#define IS_VALUE_1(X) ((1 - X) < 0.00001)  

/* This routine initializes the cplex enviorement, sets screen as an output for cplex errors and notifications, 
   and sets parameters for cplex. It calls for a mixed integer program solution and frees the environment.
   To Do:
   Declare the parameters for the problem and fill them accordingly. Fter creating the program thus copy it into cplex. 
   Define any integer or binary variables as needed, and change their type before the call to CPXmipopt to solve problem. 
   Read solution (both objective function value and variables assignment). Add out parameter to the calling function to 
   return the objective function max solution.
*/
int k_cluster(graph* grp, int K, double *objval, char* output_folder) {

   int i, j, V=grp->numVer, E=grp->numEdg;
   int T1=2*E*K, T2=E*K, T3=V, T4=K;  /* constraints types and sizes */
   int* next_ind=NULL;                

   /* Declare pointers for the variables and arrays that will contain
      the data which define the LP problem. */

   char* probname = NULL;  
   int numcols=K*(V+E);   /* number of variables */
   int numrows=T1+T2+T3+T4;   /* number of constraints */
   int objsen=CPX_MAX;     /* maximize in obj. function */
   double *obj = NULL;
   double *rhs = NULL;
   char *sense = NULL;
   int *matbeg = NULL;
   int *matcnt = NULL;
   int *matind = NULL;
   double *matval = NULL;
   double *lb = NULL;
   double *ub = NULL;

   /* for CPXchgctype */
   int cnt=0;
   int *indices = NULL;
   char *ctype = NULL;

   /* for CPXsolution */
   int solstat=0;
   double* x=NULL; 

   /* for CPXwriteprob */
   char *sol_file_path = NULL;  /* for <K>.lp */
   char tmp[FILE_NAME_LEN];
  

   /* Declare and allocate space for the variables and arrays where we
      will store the optimization results including the status, objective
      value and variable values. */

   CPXENVptr env              = NULL;
   CPXLPptr  lp               = NULL;
   int       status;
   
   /* Initialize the CPLEX environment */
   env = CPXopenCPLEX (&status);

   /* If an error occurs, the status value indicates the reason for
      failure.  A call to CPXgeterrorstring will produce the text of
      the error message. Note that CPXopenCPLEX produces no output,
      so the only way to see the cause of the error is to use
      CPXgeterrorstring. For other CPLEX routines, the errors will
      be seen if the CPX_PARAM_SCRIND indicator is set to CPX_ON.  */

   if ( env == NULL ) {
   char  errmsg[1024];
      fprintf (stderr, "Error: Could not open CPLEX environment.\n");
      CPXgeterrorstring (env, status, errmsg);
      fprintf (stderr, "%s", errmsg);
      goto TERMINATE;
   }

   /* Turn on output to the screen */
   status = CPXsetintparam (env, CPX_PARAM_SCRIND, CPX_ON);
   if ( status ) {
      fprintf (stderr, 
		  "Error: Failure to turn on screen indicator, error %d.\n", status);
      goto TERMINATE;
   }

   /* Create the problem. */
   probname=(char*)malloc((strlen("k-cluster")+1)*sizeof(char));
   if (probname==NULL) {
	   status=ERROR_MALLOC_FAILED; 
	   goto TERMINATE;
   }
   strcpy(probname,"k-cluster");  
 
   lp = CPXcreateprob (env, &status, probname);

   /* A returned pointer of NULL may mean that not enough memory
      was available or there was some other problem.  In the case of 
      failure, an error message will have been written to the error 
      channel from inside CPLEX. The setting of
      the parameter CPX_PARAM_SCRIND causes the error message to
      appear on stdout.  */

   if ( lp == NULL ) {
      fprintf (stderr, "Error: Failed to create problem.\n");
      goto TERMINATE;
   }


   /* filling in the data structures for the problem */

   lb = (double*)malloc(numcols*sizeof(double));
   ub = (double*)malloc(numcols*sizeof(double)); 
   /* lower and upper bounds for the variables */

   if ((lb==NULL) || (ub==NULL)) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   for (i=0; i<numcols; i++) {
	   lb[i]=0;
	   ub[i]=1; /* because they are binary variables */
   }


   obj = (double*)malloc(numcols*sizeof(double)); /* objective function coefficients */
   if (obj==NULL) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   for (i=0; i<K*V; i++) {
	   obj[i]=0;  /* coefficient of every x_i_p is 0 */
   }
   for (i=K*V; i<numcols; i++) {
	   obj[i]=(grp->edges+((i-K*V)%E))->weight; /* weights of edges, repeatedly */
   }


   rhs = (double*)malloc(numrows*sizeof(double)); /* right hand side values of the constraints */
   sense = (char*)malloc(numrows*sizeof(char));  /* inequality signs of the constraints */
   if ((rhs==NULL) || (sense==NULL)) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   /* numrows = T1+T2+T3+T4 - according to the 4 types of the constraints */

   for (i=0; i<T1; i++) {   /* type 1 constraints */
	   rhs[i]=0;
	   sense[i]='L';
   }
   for (i=T1; i<T1+T2; i++) {   /* type 2 constraints */
	   rhs[i]=-1;
	   sense[i]='G';
   }
   for (i=T1+T2; i<T1+T2+T3; i++) {   /* type 3 constraints */
	   rhs[i]=1;
	   sense[i]='E';
   }
   for (i=T1+T2+T3; i<numrows; i++) {   /* type 4 constraints */
	   rhs[i]=1;
	   sense[i]='G';
   }


   /* the mat### arrays */
   
   /* our varibales order: 
      x_0_1,...,x_(|V|-1)_1,...,x_0_k,...,x_(|V|-1)_k,
	  z_0_1,...,z_(|E|-1)_1,...,z_0_k,...,z_(|E|-1)_k */

   matbeg = (int*)calloc(numcols, sizeof(int));   /* as number of variables */
   matcnt = (int*)calloc(numcols, sizeof(int));
   if ((matbeg==NULL) || (matcnt==NULL)) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   for (i=K*V; i<numcols; i++) { 
   /* edges variables z_j_p, which come after nodes x_i_k variables */
	   matcnt[i]=3;    /* appears in exactly 3 constraints */
   }

   /* nodes x_i_k variables */
   for (i=0; i<E; i++) {  /* x_i_k appears in 2*(degree of vertex vi) + 2 constraints */
	   /* calculation for x_0_1 to x_(|V|-1)_1; for higher cluster number - the same */
	   matcnt[(grp->edges+i)->v1_id]+=2;
	   matcnt[(grp->edges+i)->v2_id]+=2;
	   /* calculates the 2*(degree of vertex vi) part */
   }

   for (i=0; i<V; i++) {  
	   /* adding the '+2' for x_0_1 to x_(|V|-1)_1 */ 
	   matcnt[i]+=2;   
   }

   for (i=V; i<K*V; i++) {   
	   /* filling for the rest of x_i_k variables, for cluster num. > 1, 
	      using already filled x_0_1 to x_(|V|-1)_1 */
	   matcnt[i]=matcnt[i%V];
   }


   matbeg[0]=0; /* assuming the graph isn't empty */
   for (i=0; i<numcols; i++) { /* filling matbeg with the help of matcnt */
	   matbeg[i]=matbeg[i-1]+matcnt[i-1];
   }

   /* our constraints order (first by type): 
      type1:
	  z_0_1-(clust. 1 var. of 1st vertex of e0)<=0
	  z_0_1-(clust. 1 var. of 2nd vertex of e0)<=0
	  ...
	  z_(|E|-1)_1-...<=0
	  z_(|E|-1)_1-...<=0
	  ...
	  z_0_k-...<=0
	  ...
	  z_(|E|-1)_k-...<=0
	  (total T1 const. of 2 varibales. 2*E variables per clust. num.)

      type2:
	  z_0_1-(clust. 1 var. of 1st vertex of e0)-(... 2nd vertex ...)=>-1
	  ...
	  z_(|E|-1)_1-(...)-(...)=>-1
	  ........
	  z_0_k-(...)-(...)=>-1
	  ...
	  z_(|E|-1)_k-(...)-(...)=>-1
      (total T2 const. of 3 varibales. E variables per clust. num.)

	  type3:
	  x_0_1+...+x_0_k=1
	  ...
	  x_(|V|-1)_1+...+x_(|V|-1)_k=1
      (total T3 const. of k varibales.)

	  type4:
	  x_0_1+...+x_(|V|-1)_1=>1
	  ...
	  x_0_k+...+x_(|V|-1)_k=>1
      (total T4 const. of V varibales.) */


   matind = (int*)calloc((2*T1+3*T2+K*T3+V*T4), sizeof(int));   
   /* as number of total instances of variables in the constraints */
   matval = (double*)calloc((2*T1+3*T2+K*T3+V*T4), sizeof(double));  

   next_ind = (int*)calloc(numcols, sizeof(int)); 
   /* will be initialized with values of matbeg. 
      for each variable, will hold and
      update the place in matind/matval for its next constraint index */

   if ((matind==NULL) || (matval==NULL) || (next_ind==NULL)) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   for (i=0; i<numcols; i++) { /* initializing next_ind with matbeg */
	    next_ind[i]=matbeg[i]; 
   }

   /* filling matind/matval for x_i_p varibales */
   
   /* filling indexes of constraints in type1 where they appear
      ((degree of vertex) constraints of each var.)
	  as type1 is the first, those are also overall indexes */

   for (i=0; i<E; i++) {  /* add comment */
	   for (j=1; j<=K; j++) {  /* change */
	   /* we run on edges and clusters. we look on edge's 2 vertices.
	      first, we go to the index of x_(ei end)_j in next_ind, skipping (j-1)*V vars.
	      of lower clusters. then, we put in matind/matval, according to next_int
		  'pointer', the correct constraint index:
		  we skip (j-1) cluster 'groups' of 2*E constraints of lower clusters,
		  and as each z_p_k has 2 constraints, we add 2*i.
		  (same for 2nd end of edge, but with 2*i+1) */
		     matind[next_ind[(j-1)*V+(grp->edges+i)->v1_id]]=(j-1)*2*E+2*i;
			 matind[next_ind[(j-1)*V+(grp->edges+i)->v2_id]]=(j-1)*2*E+2*i+1;
			 matval[next_ind[(j-1)*V+(grp->edges+i)->v1_id]]=-1;
			 matval[next_ind[(j-1)*V+(grp->edges+i)->v2_id]]=-1;
			 next_ind[(j-1)*V+(grp->edges+i)->v1_id]++;
			 next_ind[(j-1)*V+(grp->edges+i)->v2_id]++;
	   }
   }

  /* filling indexes of constraints in type2 where they appear 
     ((degree of vertex) constraints of each var.)
	 to get the overall index, we add T1 to the inner index in type2 */

   for (i=0; i<E; i++) {  
	   for (j=1; j<=K; j++) {  
	   /* same explanation as previously,
	      but here, to get to the correct constraint index:
		  we skip (j-1) cluster 'groups' of E, and add i
		  (both edge ends in same equation */
		     matind[next_ind[(j-1)*V+(grp->edges+i)->v1_id]]=T1+(j-1)*E+i;
			 matind[next_ind[(j-1)*V+(grp->edges+i)->v2_id]]=T1+(j-1)*E+i;
			 matval[next_ind[(j-1)*V+(grp->edges+i)->v1_id]]=-1;
			 matval[next_ind[(j-1)*V+(grp->edges+i)->v2_id]]=-1;
			 next_ind[(j-1)*V+(grp->edges+i)->v1_id]++;
			 next_ind[(j-1)*V+(grp->edges+i)->v2_id]++;
	   }
   }

   /* filling indexes of constraints in type3 where they appear
      (1 constraint of each var.)
	  to get the overall index, we add T1+T2 to the inner index in type3 */

   for (i=0; i<K*V; i++) {  
	   /* the correct constraint index is i%V, as we have all
	   cluster numbers in every equation */
	   matind[next_ind[i]]=T1+T2+i%V; 
	   matval[next_ind[i]]=1; 
	   next_ind[i]++;
   }


   /* filling indexes of constraints in type4 where they appear 
      (1 constraint of each var.)
	  to get the overall index, we add T1+T2+T3 to the inner index in type4 */

   for (i=0; i<K*V; i++) {
	   /* the correct constraint index is i/V, as we have all
	   vertices indexes in every equation */
	   matind[next_ind[i]]=T1+T2+T3+i/V; 
	   matval[next_ind[i]]=1; 
	   next_ind[i]++;
   }


   /* filling matind/matval for z_i_p varibales */

    /* filling indexes of constraints in type1 where they appear 
      (2 constraints of each var.)
	  as type1 is the first, those are also overall indexes */

   for (i=K*V; i<numcols; i++) {  
	   /* the correct constraint index is 2*(i-K*V), 
	      as (i-K*V) is a running index on z_j_p variables, 
		  and as each z_j_p has 2 constraints, we mult. by 2
		  to get its 1st index
		  (and then 2*(i-K*V)+1, for the next ind.) */
	   matind[next_ind[i]]=2*(i-K*V); 
	   matval[next_ind[i]]=1; 
	   next_ind[i]++;
	   matind[next_ind[i]]=2*(i-K*V)+1; 
	   matval[next_ind[i]]=1; 
	   next_ind[i]++;
   }

   /* filling indexes of constraints in type2 where they appear 
      (1 constraint of each var.)
	  to get the overall index, we add T1 to the inner index in type2 
	  (no z_j_p appearances in types 3 and 4) */

   for (i=K*V; i<numcols; i++) { 
	   /* here the correct constraint index is (i-K*V), 
	      as (i-K*V) is a running index on z_j_p variables */
	   matind[next_ind[i]]=T1+(i-K*V); 
	   matval[next_ind[i]]=1; 
	   next_ind[i]++;
   }
                  

   /* Use CPXcopylp to transfer the ILP part of the problem data into the cplex pointer lp */ 

   status = CPXcopylp (env, lp, numcols, numrows, objsen, obj, rhs, 
                       sense, matbeg, matcnt, matind, matval,
                       lb, ub, NULL);

   if ( status ) {
      fprintf (stderr, "Error: Failed to copy problem data.\n");
      goto TERMINATE;
   }

   
   /* filling ctype arrays */

   cnt=numcols; /* we'll define binary type for all variables */
   indices = (int*)malloc(cnt*sizeof(int));
   ctype = (char*)malloc(cnt*sizeof(char));
   if ((indices==NULL) || (ctype==NULL)) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   for (i=0; i<cnt; i++) {  /* binary type fill */
	   indices[i]=i; 
	   ctype[i]='B';
   }

   status = CPXchgctype (env, lp, cnt, indices, ctype);
   if ( status ) {
      fprintf (stderr, "Error: Failed to define variables types (as binary).\n");
      goto TERMINATE;
   }


   /* Optimize the problem. */
   status = CPXmipopt (env, lp);
   if ( status ) {
      fprintf (stderr, "Error: Failed to optimize problem.\n");
      goto TERMINATE;
   }


   /* obtaining the solution */
   x=(double*)malloc(numcols*sizeof(double)); /* will receive solution values of the variables */ 
   if (x==NULL) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   status = CPXsolution (env, lp, &solstat, objval, x, NULL, NULL, NULL);
   if ( status ) {
      fprintf (stderr, "Error: Failed to obtain solution.\n");
      goto TERMINATE;
   }
   

   /* Finally, write a copy of the problem to a file. */
   /* This command is commented out, but you can uncomment it and use it to save 
	  your lp file for your personal validation */

   sol_file_path=(char*)malloc((strlen(output_folder)+FILE_NAME_LEN)*sizeof(char)); /* for .lp file */  
    if (sol_file_path==NULL) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   strcpy(sol_file_path, output_folder);

   i=sprintf(tmp, "/%d.lp", K);
   if (i<0) {
	  status=ERROR_MALLOC_FAILED; 
	  goto TERMINATE;
   }

   strcat(sol_file_path, tmp);

   status = CPXwriteprob (env, lp, sol_file_path, NULL);
   if ( status ) {
      fprintf (stderr, "Error: Failed to write LP to disk.\n");
      goto TERMINATE;
   }



   /* filling the cluster numbers in vertices array, according to the CPLEX solution.
      that way, we return the solution to main, together with objval */
   for (i=0; i<V; i++) {
	   for (j=0; j<K; j++) {
		   if (IS_VALUE_1(x[j*V+i])) {  /* varibale x_i_j assigned to 1 */
			   (grp->vertices+i)->cluster=j+1;
			   break;
			   /* curr. vertex cluster field is assigned, and we're done with this vertex. */
		   }
	   }
   }
   
TERMINATE:

   /* Free up the problem as allocated by CPXcreateprob, if necessary */
   if ( lp != NULL ) {
      status = CPXfreeprob (env, &lp);
      if ( status ) {
         fprintf (stderr, "Error: CPXfreeprob failed, error code %d.\n", status);
      }
   }

   /* Free up the CPLEX environment, if necessary */
   if ( env != NULL ) {
      status = CPXcloseCPLEX (&env);

      /* Note that CPXcloseCPLEX produces no output,
         so the only way to see the cause of the error is to use
         CPXgeterrorstring.  For other CPLEX routines, the errors will
         be seen if the CPX_PARAM_SCRIND indicator is set to CPX_ON. */

      if ( status ) {
         char  errmsg[1024];
         fprintf (stderr, "Could not close CPLEX environment.\n");
         CPXgeterrorstring (env, status, errmsg);
         fprintf (stderr, "%s", errmsg);
      }
   }
     
   /* Free up the problem data arrays, if necessary. */

   free_and_null ((char **) &probname);
   free_and_null ((char **) &obj);
   free_and_null ((char **) &rhs);
   free_and_null ((char **) &sense);
   free_and_null ((char **) &matbeg);
   free_and_null ((char **) &matcnt);
   free_and_null ((char **) &matind);
   free_and_null ((char **) &matval);
   free_and_null ((char **) &lb);
   free_and_null ((char **) &ub);
   free_and_null ((char **) &indices);
   free_and_null ((char **) &ctype);
   free_and_null ((char **) &x);
   
   return (status);
}  


/* This simple routine frees up the pointer *ptr, and sets *ptr to NULL */
void free_and_null (char **ptr) {
   if ( *ptr != NULL ) {
      free (*ptr);
      *ptr = NULL;
   }
} 


