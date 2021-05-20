import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

public class Grafico extends Canvas
{
    private String Texto = "Variacion del error";
    private int derivada,error;
    private int xant=15,yant=200,xsig=20,ysig=0,yant_error=200,ysig_error=0;
    List<Integer> ejex= new ArrayList<>();
    List<Integer> ejey= new ArrayList<>();
    List<Integer> ejeyerror= new ArrayList<>();
   

    public Grafico(double der, double error) {
      
      this.derivada=(int)der;
      this.error=(int)error;
    	
    }
    
    public Grafico() {
    
    }
    
    
    @Override
    public void paint(Graphics g)
    {
        // Marco y Ejes Cartesianos (opcional)
          
        g.drawRect(0, 0, 1000, 400);
        this.setBackground(Color.WHITE);
        g.setColor(Color.blue);
        g.drawLine(10, 200, 995, 200);
        g.drawLine(15, 15, 15, 390); // Agregar acÃ¡ los elementos propios de la GrÃ¡fica
        ysig_error=200-(this.error/10);

       if(ejex.size()>2)
       {         
        for(int x=1; x<ejex.size(); x++)
        {
          g.setColor(Color.red);
          g.drawLine(ejex.get(x-1),ejey.get(x-1),ejex.get(x),ejey.get(x));
          g.drawLine(ejex.get(x-1),ejey.get(x-1)-1,ejex.get(x),ejey.get(x)-1);
          g.setColor(Color.green);
          g.drawLine(ejex.get(x-1),ejeyerror.get(x-1),ejex.get(x),ejeyerror.get(x));
          g.drawLine(ejex.get(x-1),ejeyerror.get(x-1)-1,ejex.get(x),ejeyerror.get(x)-1);
          
        }
       }
        g.setColor(Color.red);
        g.drawLine(xant,yant,xsig,ysig);
        g.drawLine(xant,yant-1,xsig,ysig-1);
        g.drawLine(xant,yant-2,xsig,ysig-2);
        
        g.setColor(Color.green);
        g.drawLine(xant,yant_error,xsig,ysig_error);
        g.drawLine(xant,yant_error-1,xsig,ysig_error-1);
        g.drawLine(xant,yant_error-2,xsig,ysig_error-2);
        
        ejex.add(xant);ejex.add(xsig);
        ejey.add(yant);ejey.add(ysig);
        ejeyerror.add(yant_error);ejeyerror.add(ysig_error);

        g.setColor(Color.blue);
        g.drawString(Texto, 230, 80);
    }
    
    public void Linea(double derivada, double error)
    {	
            yant-=this.derivada/10;
            yant_error-=this.error/10;
            this.derivada=(int)(derivada);
            this.error=(int)(error);
            xant=xsig;
            xsig+=5;
            ysig=yant-(this.derivada/10);
            //ysig_error=200-(this.error/10);
            
            

            if(xsig>1000)
            {
              xant=15;
              xsig=20;
              ejex.clear();
              ejey.clear();
              ejeyerror.clear();
              repaint();
              
            }

    	repaint();
    	
    }
    
    public void setTexto(String Texto)
    {
        this.Texto = Texto;
    }
}