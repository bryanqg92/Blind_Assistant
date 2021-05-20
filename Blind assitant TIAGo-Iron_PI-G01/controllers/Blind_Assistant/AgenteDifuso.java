
public class AgenteDifuso
{


    private double[] vB,salida;
    private double[][] cons_difusas_obs,cons_difusas_error,cons_difusas_der;
    public double [] fuzzy_obs,fuzzy_error,fuzzy_der;
    public double [][][] relacion_AND, centros_der,centros_izq, aux_defuzzyficacion, aux_defuzzyficacion2;
    public double numerador,numerador2,denominador,error,obstaculo,der,deseado=0,past_error;
    private Grafico grafico;
    private InterfazGrafica ventana;
    
    
    private double vel_max = 10;

    
    public AgenteDifuso(InterfazGrafica ventana)
    {
    	
       cons_difusas_obs = new double[][]
        		        {	           {-1026,-1025,-300,-200},	//cerca izquierda	
            			{-300,-200,-100,0},			//izquierda
            			{-100,0,0,100},				//libre
            			{0,100,200,300},				//derecha
            			{200,300,1025,1026}		//cerca derecha				
              		};
       cons_difusas_error = new double[][]
                     {	
                       {-2,-1,0,50},			//lejos	
                    	{0,50,100,150},			//medio cerca
                    	{100,150,200,250},			//cerca
                    	{200,250,1025,1026}			//muy cerca			
                     };
       cons_difusas_der = new double[][]
                     {	{-1026,-1025,-30,-10},		//rapido izq
                        {-30,-10,-10,0},			//despacio izq
                        {-10,0,1,2}				//centrado
                        //{0,50,70,80},				//despacio der
                        //{70,80,1001,1002}			//rapido der	
                     };
              		
      centros_der= new double [][][] {
                         {  
                         {-20,0,40,40,40},
                         {-40,-20,20,60,60},
                         {-60,-40,0,80,80},
                         {-80,-60,-20,100,100}
                         //{0, 0, 0, 0, 0} 
                         },
                         {
                         {0,20,40,60,60},
                         {-20,0,40,60,60},
                         {-40,-20,20,80,80},
                         {-60,-40,0,80,80}
                         //{0, 0, 0, 0, 0}
                         },
                         
                         {
                         {20,40,40,80,80},
                         {0,20,40,80,80},
                         {-20,0,20,100,100},
                         {-40,-20,20,100,100}
                         //{0, 0, 0, 0, 0}
                         },
                         
                       /*  {
                         {0, 0, 75, 75, 0},
                         {0, 0, 75, 75, 75},
                         {0, 0, 75, 100, 75},
                         {0, 0, 75, 75, 0},
                         {0, 0, 0, 0, 0}
                         },

                         {
                         {0, 0, 0, 0, 0},
                         {0, 0, 0, 75, 75},
                         {0, 0, 0, 75, 75},
                         {0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0}
                         },*/
                       };
    centros_izq= new double [][][] {
                           {  
                           {40,40,40,0,-20},
                           {60,60,20,-20,-40},
                           {80,80,0,-40,-60},
                           {100,100,-20,-60,-80}
                           //{0, 0, 0, 0, 0} 
                           },
                           
                           {
                           {60,60,40,20,0},
                           {60,60,40,2,-20},
                           {80,80,20,-20,-40},
                           {80,80,0,-40,-60}
                           //{0, 0, 0, 0, 0}
                           },
                           
                           {
                           {80,80,40,40,20},
                           {80,80,40,20,0},
                           {100,100,40,0,-20},
                           {100,100,20,-20,-40},
                           //{0, 0, 0, 0, 0}
                           },
                           
                         /*  {
                           {0, 0, 75, 75, 0},
                           {0, 0, 75, 75, 75},
                           {0, 0, 75, 100, 75},
                           {0, 0, 75, 75, 0},
                           {0, 0, 0, 0, 0}
                           },

                           {
                           {0, 0, 0, 0, 0},
                           {0, 0, 0, 75, 75},
                           {0, 0, 0, 75, 75},
                           {0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0}
                           },*/
                         };
                                                 
         fuzzy_obs = new double [cons_difusas_obs.length];
         fuzzy_error = new double [cons_difusas_error.length];
         fuzzy_der = new double [cons_difusas_der.length];
         relacion_AND = new double[fuzzy_der.length][fuzzy_error.length][fuzzy_obs.length]; 
         aux_defuzzyficacion = new double[fuzzy_der.length][fuzzy_error.length][fuzzy_obs.length];
         aux_defuzzyficacion2 = new double[fuzzy_der.length][fuzzy_error.length][fuzzy_obs.length];
         vB = new double[2];
         salida = new double[2];
         //grafico=new Grafico(der);
         this.ventana=ventana;

     }
    
  /**********************************************************************************| 
  |* valor deseado  0 indica que ambos sensores frontales no detectan nada de frente |
  |* s0 - s15 	= error 															 | 
  |* s1 : s5 	= sensores derecha													 |
  |* s11 : s14 	= sensores izquierda												 |
  |* 																				 |
  |**********************************************************************************/
     
  public double[] DecisionMaking(double dS[]) 
  {
	  past_error = 	error;
	  error =	 	Math.max(dS[0],Math.max(dS[1],Math.max(dS[14],dS[15])))-deseado;
  	  der =	           past_error - error;
  	  
  	  System.out.println(error);
  	  ventana.getGrafico().Linea(der,error);
	  obstaculo = 	Math.max(dS[1],Math.max(dS[2],dS[0]))- Math.max(dS[15],Math.max(dS[14],dS[13]));
	  fuzzy_error =	fuzzy(cons_difusas_error,error);
	  fuzzy_obs =	fuzzy(cons_difusas_obs,obstaculo);
	  fuzzy_der =	fuzzy(cons_difusas_der,der);
	  razonamiento_difuso();
	  defuzzificacion();
	  salida();
	  
	  for(int x=0;x<2;x++)
	  {
	    vB[x]=Lin_error(salida[x],-100,100,vel_max*-1,vel_max);
	  }
	  //grafico = new Grafico(der);
	  return vB;
  }
  
  public double Lin_error(double error, double oldmin, double oldmax, double newmin, double newmax)
    {
      double mean= ((((error-(oldmin))*(newmax-(newmin)))/(oldmax-(oldmin)))+(newmin));             
      return mean;
    } 
    
 public double[] fuzzy(double [][] constantes, double entradas)
                
    {	
	 	double [] resultado = new double[constantes.length];
	
	     for(int fila_cons=0; fila_cons<resultado.length; fila_cons++) 
	     {
					  
		   if(entradas<=constantes[fila_cons][0]) 
		   {
			   resultado[fila_cons]=0;
		   }
		   
		   else if(constantes[fila_cons][0] < entradas && entradas < constantes[fila_cons][1] ) 
		   {
			   resultado[fila_cons]=(entradas-constantes[fila_cons][0])/(constantes[fila_cons][1]-constantes[fila_cons][0]);   
		   }
		   
		   else if(constantes[fila_cons][1] <= entradas && entradas <= constantes[fila_cons][2]) 
		   {
			   resultado[fila_cons]=1;
		   }
		   
		   else if(constantes[fila_cons][2] < entradas && entradas < constantes[fila_cons][3] ) 
		   {
			   resultado[fila_cons]=(constantes[fila_cons][3]-entradas)/(constantes[fila_cons][3]-constantes[fila_cons][2]);
		   }
		   
		   else if(entradas >= constantes[fila_cons][3]) 
		   {
			   resultado[fila_cons]=0;
		   }
		   
	}
              
       return resultado;
   }
       

    
 public void razonamiento_difuso() 
 {

       for(int z2=0; z2 < fuzzy_der.length ; z2++)
       {
    	   for(int fila2=0; fila2 < fuzzy_error.length; fila2++) 
    	   {
    		   for(int col2=0; col2 < fuzzy_obs.length; col2++)
    		   {
    			   relacion_AND[z2][fila2][col2]=Math.min(Math.min(fuzzy_obs[col2], fuzzy_error[fila2]),fuzzy_der[z2]);
    		   }
    	   }
       }        	   
       
 }
      
public void defuzzificacion()
{
	for(int z3=0; z3<fuzzy_der.length; z3++)
	{
		for(int fila3=0; fila3<fuzzy_error.length; fila3++)
		{
			for(int col3=0; col3<fuzzy_obs.length; col3++)
			{
				aux_defuzzyficacion [z3][fila3][col3] = relacion_AND[z3][fila3][col3]  * centros_der[z3][fila3][col3];
			    aux_defuzzyficacion2[z3][fila3][col3] = relacion_AND[z3][fila3][col3]  * centros_izq[z3][fila3][col3];
			}
			
		}
	}
    
}

               
public void salida()
 {
     numerador=numerador2=0;
     vB[0]=0;
     vB[1]=0;
     denominador=0;
     
     for(int z2=0; z2<fuzzy_der.length; z2++)
     {
    	 for(int fila2=0; fila2<fuzzy_error.length; fila2++)
    	 {
    		 for(int col2=0; col2<fuzzy_obs.length; col2++)
    		 {
                 denominador=denominador+relacion_AND[z2][fila2][col2];
    		     numerador=numerador+aux_defuzzyficacion[z2][fila2][col2];
    		     numerador2=numerador2+aux_defuzzyficacion2[z2][fila2][col2];
    		 }
    		 
    	 }
    	 
     }

     salida[1]=(numerador/denominador);
     salida[0]=(numerador2/denominador);           

}
      
      


}

