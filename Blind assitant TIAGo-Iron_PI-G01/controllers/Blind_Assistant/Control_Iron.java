import com.cyberbotics.webots.controller.Robot;
import com.cyberbotics.webots.controller.Motor;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.Camera;

/**
 *
 * @author Dante Sterpin
 */
public class Control_Iron extends Robot
{
    private Camera camera;
    private DistanceSensor sensor[];
    private final int timeStep=32;
    private int height, width, image[];
    private String Path = "../../images/Sight.jpg";
    private double dS[],vB[],pH[];
    private AgenteDifuso agente;
    private Motor base[],head[];
    private pre_imagen observador;
    
    
    public Control_Iron()
    {
        super();
        
        // Inicializa los sensores
        camera = getCamera("camera");
        camera.enable(4*timeStep);
        height = camera.getHeight();
        width = camera.getWidth();
        
        sensor = new DistanceSensor[16];
        for(int i=0; i<16; i++)
        {
            sensor[i] = getDistanceSensor("sensor_"+i);
            sensor[i].enable(timeStep);
        }
        dS = new double[16];
        
        // Inicializa los motores
        base = new Motor[2];
        base[0] = getMotor("wheel_left_joint");
        base[1] = getMotor("wheel_right_joint");
        
        for(int j=0; j<2; j++)
        {
            base[j].setPosition(Double.POSITIVE_INFINITY);
        }
        vB = new double[2];
        
        head = new Motor[2];
        head[0] = getMotor("head_1_joint");
        head[1] = getMotor("head_2_joint");
        pH = new double[]{0.0, -0.2};
        
        for(int m=0; m<2; m++)
        {
            head[m].setVelocity(0.7);
            head[m].setPosition(pH[m]);
        }
        
        // Inicializa los agentes
        
        observador = new pre_imagen(Path);
        agente = new AgenteDifuso(observador.getVentana());
    }
    
    public void Ejecutar()
    {
        while (step(64) != -1)
        {
            // Lee los sensores
            image = camera.getImage();
            camera.saveImage(Path, 70);
            observador.ProcesarImagen();
            
            for(int i=0; i<16; i++)
            {
                dS[i] = sensor[i].getValue();
                //System.out.print("s_"+i+"="+dS[i]+" ");
            }
            //System.out.print("\n");
            
            for(int i=11; i<16; i++)
            {
                dS[i] = sensor[i].getValue();
                //System.out.print("s_"+i+"="+dS[i]+" ");
            }
            //System.out.print("\n");
            
            // Decide la actuación
            
            vB = agente.DecisionMaking(dS);
            //System.out.println(vB[0]+" "+vB[1]);
            
            // Mueve los motores
            for(int j=0; j<2; j++)
            {
                base[j].setVelocity(vB[j]);
                //System.out.print("motor_"+j+" "+vB[j]+" ");
            }//System.out.print("\n");
        };
        
        finalize();
    }
    
    public static void main(String[] args)
    {
        new Control_Iron().Ejecutar();
    }
}
