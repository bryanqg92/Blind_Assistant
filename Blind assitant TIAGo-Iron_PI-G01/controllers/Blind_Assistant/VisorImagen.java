import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class VisorImagen extends Canvas
{

   private static final long serialVersionUID = 1L;
   public int x,y;
	
   private BufferedImage Imagen,Imagen2,Imagen3;
    
    public VisorImagen(BufferedImage Imagen,BufferedImage Imagen2,BufferedImage Imagen3)
    {
        this.Imagen = Imagen;
        this.Imagen2 = Imagen2;
        this.Imagen3 = Imagen3;
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(Imagen, 0, 0, Imagen.getWidth(), Imagen.getHeight(), null);
        g.drawImage(Imagen2, 400, 0, Imagen.getWidth(), Imagen.getHeight(), null);
        g.drawImage(Imagen3, 800, 0, Imagen.getWidth(), Imagen.getHeight(), null);

    }
    
    public void setImagen(BufferedImage Imagen,BufferedImage Imagen2,BufferedImage Imagen3)
    {
        this.Imagen = Imagen;
        this.Imagen2 = Imagen2;
        this.Imagen3 = Imagen3;
    }
    
    public void latas(int x, int y)
    {
    	this.x=x;
    	this.y=y;
    
    }
}