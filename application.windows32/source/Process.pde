/**
*
*  imports
*
**/

//import java.util.ArrayList;

/**
*
* creamos el objeto proceso
*
*/
public class Process{
  public String  name;           // el nombre del proceso
  public int     totalDuration;  // la duracion total en frames
  public int     progress;       // el progreso obtenido hasta ahora
  public color   ownColor;
  public char    state;          // estado del proceso R(running), W(waiting),P(Paused),F(Finished)
  public String  user;           // El nombre de usuario al que pertenece
  public int[]   intervals;      // los intervalos de ejecucion
  public int     intervalStep;   // el intervalo en el que esta ubicado actualmente  
  public int     midlleTime;     // el tiempo de espera entre intervalos
  public int     midlleProg;     // el progreso de espera entre intervalos
  public int     memoryTime;     // el tiempo que dura cargandose en memoria
  public int     memoryProg;     // el tiempo de progreso de garga en memoria   
  
  Process(String user){     //constructor
    this.name = "process"+(int)(Math.random() * (255 - 0 + 1) + 0);
    this.totalDuration = (int)(Math.random() * (70 - 20 + 1) + 20);
    this.ownColor = color((int)(Math.random() * (255 - 0 + 1) + 0),(int)(Math.random() * (255 - 0 + 1) + 0),(int)(Math.random() * (255 - 0 + 1) + 0)); //(int)(Math.random() * (255 - 0 + 1) + 0)
    this.user = user;
    this.progress = 0;
    this.state = 'W';
    this.intervals = calIntervals(totalDuration);
    this.midlleTime = (int)(Math.random() * (45 - 10 + 1) + 10);
    this.memoryTime = (int)(Math.random() * (45 - 10 + 1) + 10);
    this.midlleProg = midlleTime;
    this.memoryProg = memoryTime;
    this.intervalStep = 0;
  } 
  
  public int[] calIntervals(int maxCount){             //crea unos intervalos de ejecucion aleatorios
    int localmaxCount = maxCount;
    int min = 5;
    int max = 9;    
    int random_int = (int)(Math.random() * (max - min + 1) + min);
    int[] result = new int[random_int];
    for(int i= 0; i < random_int; i++){
       int random_interval = (int)(Math.random() * (localmaxCount - 0 + 1) + 0);
       if(i == random_int-1){
         result[i] = localmaxCount;
       }else{
         result[i] = random_interval;
         localmaxCount -= random_interval;
       }
    }
    println(maxCount);
    println(result);
    return result;
  }  
}

/**
*
* creamos el objeto de Cola de procesos
*
*/
public class ProcessQueue{
  
  public ArrayList<Process> queue ;
  
  ProcessQueue(){
    queue = new ArrayList<Process>();
  }
  
  public ArrayList<Process> getQueue(){
    return this.queue;
  }
  
  public void pushProcess(Process  in){
    this.queue.add(in);
  }
  
  public Process pullProcess(){
    Process out = queue.get(0);
    queue.remove(0);
    return out;
  }
  
  public void clearProcesses(){
    this.queue.clear();
  } 
} 

/**
*
* creamos el objeto Usuario
*
*/

public class User{
  
  public ProcessQueue userQueue;
  public String name;
  
  public User(String name){
    this.name = name;
    this.userQueue = new ProcessQueue();
  }
  
  public void addProcess(Process proceso){
    this.userQueue.pushProcess(proceso);
  }
  
}

/**
*
* creamos el objeto Maquina
*
*/

public class Machine{
  
  public char mode;       // designa el modo de ejecucion de programas S(serie) L(lotes monoprogramacion)  M(lotes multiprogramacion)
  public boolean state;   // 1 is on 0 is off designa si la maquina esta corriendo o no
  public User[] users;    // la lista de ususarios
  public float speed;     // la velocidad de ejecucion de la CPU
  
  
  public Machine(){
    this.mode = 'S';
    this.state = false;
    this.users = new User[2];
    this.users[0] = new User("User1");
    this.users[1] = new User("User2");
    this.speed = 1;
  }  
}

class ColorContainer {
  public color col1=color(0);
  ColorContainer(color c_) {
     col1=c_;
  }
} 


  
