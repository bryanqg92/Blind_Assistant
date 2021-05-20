

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class pre_imagen {
  private BufferedImage Imagen;
  private BufferedImage Imagen2;
  private BufferedImage Imagen3;
  private BufferedImage Imagen4;
  private File file, output;
  private Color color,color2;
  private int a,Rn,Gn,Bn,RGB,mitad,Rn2,Gn2,Bn2,RGB2;
  private int Ri,Gi,Bi;
  private InterfazGrafica ventana;
  private double [][] smooth,borde_m,average,borde_color,sua5x5;
  private AgenteDifuso controlador;
  
 
	
	
  public pre_imagen(String Path) 
  {
    // TODO Auto-generated constructor stub
    file = new File(Path);
    output = new File("../../images/Camera_Bordes.jpg"); 
    LeerImagen();
    mitad=0;
    ventana = new InterfazGrafica(Imagen,Imagen2,Imagen3);
    
    //controlador = new AgenteDifuso();

   

//************PARA SUAVIZADO EN GRISES****************************************    
    smooth = new double[][]
                            {	
                              {1,4,7,4,1},
                              {4,16,26,16,4},  //factor 275
                              {7,26,41,26,7},
                              {4,16,26,16,4},
                              {1,4,7,4,1}
                              
                              
                            };
    borde_m = new double[][]
                            {	
                              {0,0,1,0,0}, 
                              {0,1,2,1,0},
                              {1,2,-16,2,1},
                              {0,1,2,1,0},
                              {0,0,1,0,0}
                            };
//******************************************************************************
//*********************SUAVIZADO Y BORDE EN COLOR*****************************
   average= new double[][]
                            {	
                              {1,1,1},
                              {1,1,1}, //factor 9
                              {1,1,1}
                            };
                          
   sua5x5 = new double[][]
                            {	
                              {1,1,1,1,1},
                              {1,1,1,1,1}, //factor 25
                              {1,1,1,1,1},
                              {1,1,1,1,1},
                              {1,1,1,1,1}
                            };
              
   borde_color = new double[][]
                            {	
                              {0,1,0},
                              {1,-4,1}, 
                              {0,1,0}
                            };
//*********************************************************************************
  }
	
 //////////*****************************************************************************************
 
  public void /*BufferedImage*/ ProcesarImagen()
    {
        LeerImagen();
        rgb2gray();
        //Imagen=convolucion(Imagen,sua5x5,25);
        Imagen2=convolucion(Imagen,average,9);
        Imagen3=borde(Imagen2,borde_m);
        Imagen4=im2bw(Imagen2);
        visualizar(Imagen2,Imagen3,Imagen4);
        //guardar(Imagen2,output);
        //return Imagen2;   
    }
  //*****************************************************************************************************
 /////////////////***************************************************************************************   
  public void rgb2gray()
  {
  
    for(int h=0;h<Imagen.getHeight();h++)
        {
            for(int w=0;w<Imagen.getWidth();w++)
            {
                color=new Color(Imagen.getRGB(w, h)); 
                a=color.getAlpha();
                Ri=color.getRed(); 
                Gi=color.getGreen(); 
                Bi=color.getBlue();
                
                Rn=(int)((Ri*0.299) + (Gi*0.587) + (Bi*0.114));
                RGB=(a << 24) | (Rn << 16) | (Rn << 8) | Rn;
                Imagen.setRGB(w, h, RGB);
            }  
        }

  }
  
//**********************************************************************************************************
//**********************************************************************************************************  
  public BufferedImage convolucion (BufferedImage captura, double [][] filtro, double factor) 
  {			
    mitad= filtro.length/2;
    //this.Imagen=Imagen;
    BufferedImage captura2 = new BufferedImage(Imagen.getWidth()-mitad, Imagen.getHeight()-mitad, BufferedImage.TYPE_INT_ARGB);

    
    for(int h=mitad;h<captura.getHeight()-mitad;h++)
    {
      for(int w=mitad;w<captura.getWidth()-mitad;w++)
      {
        Rn=0;
        Gn=0;
        Bn=0;
        RGB=0;
        for(int h2=0;h2<filtro.length;h2++)
        {
          for(int w2=0;w2<filtro.length;w2++)
          {
            int locx=(w-mitad)+w2;
            int locy=(h-mitad)+h2;
            color = new Color(captura.getRGB(locx,locy));
            
            Rn=(int) (Rn +(color.getRed()*((filtro[h2][w2])/factor)));     
          }
        }
	     
        RGB=(a << 24) | (Rn << 16) | (Rn << 8) | Rn;	        	 
        captura2.setRGB(w, h, RGB);
	        	
       }
    }
    return captura2;
 }
 //**************************************************************************************************************
 //**************************************************************************************************************
    
  public BufferedImage borde (BufferedImage captura, double[][] filtro) 
  {	
	
    mitad= filtro.length/2;
    BufferedImage captura2 = new BufferedImage(Imagen.getWidth()-mitad, Imagen.getHeight()-mitad, BufferedImage.TYPE_INT_ARGB);

    for(int h=mitad;h<captura.getHeight()-mitad;h++)
    {
      for(int w=mitad;w<captura.getWidth()-mitad;w++)
      {
        Rn=0;
        Gn=0;
        Bn=0;
        RGB=0;
        for(int h2=0;h2<filtro.length;h2++)
        {
          for(int w2=0;w2<filtro.length;w2++)
          {
            int locx=(w-mitad)+w2;
            int locy=(h-mitad)+h2;
            color = new Color(captura.getRGB(locx,locy));
            Rn=(int) (Rn +(color.getRed()*(borde_m[h2][w2]))); 
    
          }
        }
        if(Rn<0) {Rn=0;}if(Rn>255) {Rn=255;} 
        RGB=(a << 24) | (Rn << 16) | (Rn << 8) | Rn;	        	 
        captura2.setRGB(w, h, RGB);
	        	
       }
    }
    
       return captura2;	
  }
//*******************************************************************************************************************
//******************************************************************************************************************
  
  public BufferedImage im2bw (BufferedImage captura) 
  {	
    BufferedImage captura2 = new BufferedImage(captura.getWidth(), captura.getHeight(), BufferedImage.TYPE_INT_ARGB);

    for(int h = captura.getHeight()-1; h > 0; h--)
    {
      for(int w= 0; w < captura.getWidth(); w++)
      {
          
        color = new Color(captura.getRGB(w,h));
	        	 
		if (color.getRed()>70)
		{
	    	  Rn=255;
	
	            }
		
		else 
		{
	    	  Rn=0;
	    	  
		}
	
        RGB=(a << 24) | (Rn << 16) | (Rn << 8) | Rn;	        	 
        captura2.setRGB(w, h, RGB);
	        	
       }
    }
    
       return captura2;	
  }
  
  
  //*****************************************************************************************************************
  //*****************************************************************************************************************
  
  
  public void visualizar(BufferedImage resul,BufferedImage resul2,BufferedImage resul3)
  {
    
    ventana.VerImagen(resul,resul2,resul3);
    ventana.repaint();
  }
  
  public InterfazGrafica getVentana()
  {
    return ventana;
  }
  
 
	
    public void LeerImagen()
    {
        try
        {
            Imagen = ImageIO.read(file);
        }
        catch (IOException ex)
        {
            System.out.println("Error al Leer la Imagen en la Interfaz: "+ex.getMessage());
        }
    }
    
    public void guardar(BufferedImage guardado, File lugar)
    {
        try
        {
            ImageIO.write(guardado,"jpg",lugar);
        }
        catch (IOException ex)
        {
            System.out.println("Error al guardar la Imagen en la Interfaz: "+ex.getMessage());
        }
    }
}

