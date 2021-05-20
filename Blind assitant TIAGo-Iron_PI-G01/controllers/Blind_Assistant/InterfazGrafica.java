import javax.swing.JFrame;
import javax.swing.JLabel;



import java.awt.image.BufferedImage;


public class InterfazGrafica extends JFrame
{
    private VisorImagen visorImagen;
    private Grafico grafico;
    private static final long serialVersionUID = 1L;


    
    public InterfazGrafica(BufferedImage Imagen,BufferedImage Imagen2,BufferedImage Imagen3)
    {
       
        setSize(1250,750);
        setResizable(false);
        this.setLocation(800,10);
        setTitle("PI-2020B DSP - CONTROL DIFUSO SISTEMAS DE COMUNICACIÓN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        visorImagen = new VisorImagen(Imagen,Imagen2,Imagen3);
        grafico=new Grafico();
 
        
        visorImagen.setBounds(10, 10, 1250, Imagen.getHeight());
        add(visorImagen);
        
        grafico.setBounds(10, 250, 1000, 400);
        add(grafico);
        
       

        
        setVisible(true);
    }
    
    public Grafico getGrafico()
    {
       return grafico;
    }


    
    public void VerImagen(BufferedImage Imagen,BufferedImage Imagen2,BufferedImage Imagen3)
    {
        visorImagen.setImagen(Imagen,Imagen2,Imagen3);
        visorImagen.repaint();
    }

    }