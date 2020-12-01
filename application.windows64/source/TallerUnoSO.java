import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TallerUnoSO extends PApplet {

Machine m = new Machine();        // el objeto maquina que tiene la informacion de los procesos
int btnsWidth = 200;              
int btnsHeigth = 40;
int PTSx = 700;
int PTy = 480;
int PTLx = PTSx + btnsWidth;
int PTMx = PTSx + (2*btnsWidth);
int SP0x = 700;
int SPy = 605;
int SP1x = SP0x + btnsWidth;
int SP2x = SP0x + (2*btnsWidth);
int UI0x = 700;
int UIy = 705;
int UI1x = SP0x + 150;
int UI2x = SP0x + (2*150);
int UI3x = SP0x + (3*150);

ArrayList<ColorContainer> cpuload = new ArrayList();

int switcher = 0;

public void setup(){
  
  frameRate(30);
  background(210);
}

public void draw(){
  background(210);  
  drawBase();  
  drawbuttonsPT();
  drawbuttonsSP();
  drawbuttonsUI();
  checkPTInput();
  checkSPInput();
  checkUInput();
  drawPU1();
  drawPU2();
  if(m.state){
    cpu();    
  }
  drawCpuline();
  stroke(0);
}

public void keyPressed() {
  if(keyCode == 67){
    reset();
  }
}

public void cpu(){
 switch(m.mode){
   case 'S':
     sCPU();
   break; 
   case 'L':
     lCPU();
   break; 
   case 'M':
     mCPU();
   break;
 }
}

public void drawCpuline(){
  int base = 692;  
   for (int i = cpuload.size()-1; i > 0; i--) {
      stroke(cpuload.get(i).col1);      
      line(base+Math.abs(i-cpuload.size()),200,base+Math.abs(i-cpuload.size()),260);
   }
}


public void sCPU(){// serial cpu processing
  for(Process item : m.users[0].userQueue.queue) {
   if(item.state == 'W' || item.state == 'R' || item.state == 'P'){
     switch(item.state){
       case 'W':
         item.state = 'R';
         if(item.memoryProg != 0){// aun esta cargando memoria
           item.memoryProg -= 1;
           println("loading memory");
           cpuload.add(new ColorContainer(color(150)));
         }else{// ya no esta cargando memoria
           if(item.progress == item.intervals[item.intervalStep]){
             item.midlleProg = item.midlleTime;
             item.state = 'P';// cambia a estado pausado P que es el blanco
             item.intervalStep += 1;// cambia al siguiente intervalo     
           }else {
              cpuload.add(new ColorContainer(item.ownColor));
              item.progress += 1;
              println("running");
           }           
         }
         break;       
       case 'R':
         if(item.progress == item.intervals[item.intervalStep]){
             item.midlleProg = item.midlleTime;
             item.state = 'P';// cambia a estado pausado P que es el blanco
             if(item.intervalStep <= item.intervals.length){
               item.intervalStep += 1;// cambia al siguiente intervalo 
             }            
           }else if(item.progress >= item.totalDuration){
             item.state = 'F';
           }else{
              cpuload.add(new ColorContainer(item.ownColor));
              item.progress += 1;              
           } 
       break;
       case 'P':
         if(item.midlleProg >= 0 && item.intervalStep == item.intervals.length-1 ){
           item.state = 'F';
         }else if(item.midlleProg == 0){
           item.midlleProg = item.midlleTime;
           item.state = 'R';// cambia a estado running
         }else if(item.progress >= item.totalDuration){
             item.state = 'F';
         }else{
           println("paused");
           item.midlleProg -= 1;
           cpuload.add(new ColorContainer(color(255)));
         }       
        break;
     }     
     break;
   }   
  }
}


public void lCPU(){// batch cpu procesing
 for(Process item : m.users[0].userQueue.queue) {
   if(item.state == 'W' || item.state == 'R' || item.state == 'P'){
     switch(item.state){
       case 'W':
         item.state = 'R';
         if(item.memoryProg != 0){// aun esta cargando memoria
           item.memoryProg -= 1;
           println("loading memory");
           cpuload.add(new ColorContainer(color(150)));
         }else{// ya no esta cargando memoria
           if(item.progress == item.intervals[item.intervalStep]){
             item.midlleProg = item.midlleTime;
             item.state = 'P';// cambia a estado pausado P que es el blanco
             item.intervalStep += 1;// cambia al siguiente intervalo     
           }else {
              cpuload.add(new ColorContainer(item.ownColor));
              item.progress += 1;
              println("running");
           }           
         }
         break;       
       case 'R':
         if(item.progress == item.intervals[item.intervalStep]){
             item.midlleProg = item.midlleTime;
             item.state = 'P';// cambia a estado pausado P que es el blanco
             if(item.intervalStep <= item.intervals.length){
               item.intervalStep += 1;// cambia al siguiente intervalo 
             }            
           }else if(item.progress >= item.totalDuration){
             item.state = 'F';
           }else{
              cpuload.add(new ColorContainer(item.ownColor));
              item.progress += 1;              
           } 
       break;
       case 'P':
         if(item.midlleProg >= 0 && item.intervalStep == item.intervals.length-1 ){
           item.state = 'F';
         }else if(item.midlleProg == 0){
           item.midlleProg = item.midlleTime;
           item.state = 'R';// cambia a estado running
         }else if(item.progress >= item.totalDuration){
             item.state = 'F';
         }else{
           println("paused");
           item.midlleProg -= 1;           
         }       
        break;
     }     
   }   
  }
}

public void mCPU(){ // mu batch procesing

for(Process item : m.users[switcher].userQueue.queue) {
   if(item.state == 'W' || item.state == 'R' || item.state == 'P'){
     switch(item.state){
       case 'W':
         item.state = 'R';
         if(item.memoryProg != 0){// aun esta cargando memoria
           item.memoryProg -= 1;
           println("loading memory");
           cpuload.add(new ColorContainer(color(150)));
         }else{// ya no esta cargando memoria
           if(item.progress == item.intervals[item.intervalStep]){
             item.midlleProg = item.midlleTime;
             item.state = 'P';// cambia a estado pausado P que es el blanco
             item.intervalStep += 1;// cambia al siguiente intervalo     
           }else {
              cpuload.add(new ColorContainer(item.ownColor));
              item.progress += 1;
              println("running");
           }           
         }
         break;       
       case 'R':
         if(item.progress == item.intervals[item.intervalStep]){
             item.midlleProg = item.midlleTime;
             item.state = 'P';// cambia a estado pausado P que es el blanco
             if(item.intervalStep <= item.intervals.length){
               item.intervalStep += 1;// cambia al siguiente intervalo 
             }            
           }else if(item.progress >= item.totalDuration){
             item.state = 'F';
           }else{
              cpuload.add(new ColorContainer(item.ownColor));
              item.progress += 1;              
           } 
       break;
       case 'P':
         if(item.midlleProg >= 0 && item.intervalStep == item.intervals.length-1 ){
           item.state = 'F';
         }else if(item.midlleProg == 0){
           item.midlleProg = item.midlleTime;
           item.state = 'R';// cambia a estado running
         }else if(item.progress >= item.totalDuration){
             item.state = 'F';
         }else{
           println("paused");
           item.midlleProg -= 1;           
         }       
        break;
     }     
   }   
  }
  if(switcher==0){
    switcher=1;
  }else if(switcher==1){
    switcher=0;
  }
}




public void drawBase(){ //  dibuja la base de la UI
  strokeWeight(2);
  fill(140);
  rect(0,50,300,50); // user1 label 
  rect(0,100,300,height); // user 1 queqe
  if(m.mode == 'M'){
    rect(300,50,300,50); // user2 label
    rect(300,100,300,height);// user 2 queqe
    fill(0);
    text("USER 2",410,80);
  }
  fill(0);
  rect(600,50,800,350);//execution black rectangle
  fill(140);
  rect(600,200,90,60); // cpu box 
  stroke(color(204,0,0));
  line(690,100,690,345);// red line
  stroke(0);
  fill(255);
  rect(740,58,30,30);// white box
  text("CPU doing nothing",790,80);
  fill(150);
  rect(1000,58,30,30);// green box
  text("MEMORY loading",1040,80);
  fill(0);
  textSize(30);
  text("CPU",620,240);
  textSize(20);
  text("PROCESS QUEUE",200,30);
  text("EXECUTION TIMELINE",850,30);
  text("PROCESSING MODE",900,450);
  text("SPEED",955,580);
  text("USER 1",110,80);
  
}


public void drawbuttonsPT(){ //dibuja los botones del procesing type  
  if(m.mode == 'S'){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(PTSx,PTy,btnsWidth,btnsHeigth);
  fill(0);
  text("SERIALLY",PTSx+70,PTy+25);
  if(m.mode == 'L'){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(PTLx,PTy,btnsWidth,btnsHeigth);
  fill(0);
  text("BATCH",PTLx+70,PTy+25);
  if(m.mode == 'M'){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(PTMx,PTy,btnsWidth,btnsHeigth);
  fill(0);
  text("BATCH MU",PTMx+50,PTy+25);
}

public void drawbuttonsSP(){ // dibuja los botones de la velocidad
  if(m.speed == 0.5f){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(SP0x,SPy,btnsWidth,btnsHeigth);
  fill(0);
  text("0.5",SP0x+70,SPy+25);
  if(m.speed == 1){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(SP1x,SPy,btnsWidth,btnsHeigth);
  fill(0);
  text("1.0",SP1x+70,SPy+25);
  if(m.speed == 1.5f){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(SP2x,SPy,btnsWidth,btnsHeigth);
  fill(0);
  text("1.5",SP2x+70,SPy+25);
}

public void drawbuttonsUI(){ // dibuja los botones de la UI inferior
  if(m.state){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(UI0x,UIy,150,60);
  fill(0);
  triangle(UI0x+60,UIy+15,UI0x+60,UIy+45,UI0x+90,UIy+30); // simbolo de play
  
  if(!m.state){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(UI1x,UIy,150,60);
  fill(0);
  rect(UI1x+60,UIy+15,10,30); //simbolo de pausa
  rect(UI1x+75,UIy+15,10,30);
  
  if(!m.state){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(230,0,0)); // amarillo
  }
  rect(UI2x,UIy,150,60);
  fill(0);
  text("+PU_1",UI2x+30,UIy+40);
  
  if(!m.state && m.mode == 'M'){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(230,0,0)); // rojo
  }
  rect(UI3x,UIy,150,60);
  fill(0);
  text("+PU_2",UI3x+30,UIy+40);
}

public void checkPTInput(){ // controller para los tipos de procesamiento
  if (mousePressed && (mouseButton == LEFT) && (mouseX > PTSx) && (mouseX < PTSx+btnsWidth) && (mouseY > PTy) && (mouseY < PTy+btnsHeigth) ) {
    m.mode = 'S';
  }else if(mousePressed && (mouseButton == LEFT) && (mouseX > PTLx) && (mouseX < PTLx+btnsWidth) && (mouseY > PTy) && (mouseY < PTy+btnsHeigth) ){
    m.mode = 'L';
  }else if(mousePressed && (mouseButton == LEFT) && (mouseX > PTMx) && (mouseX < PTMx+btnsWidth) && (mouseY > PTy) && (mouseY < PTy+btnsHeigth) ){
    m.mode = 'M';
  }
}

public void checkSPInput(){// controller para las velocidades
  if (mousePressed && (mouseButton == LEFT) && (mouseX > SP0x) && (mouseX < SP0x+btnsWidth) && (mouseY > SPy) && (mouseY < SPy+btnsHeigth) ) {
    m.speed = 0.5f;
    frameRate(15);
  }else if (mousePressed && (mouseButton == LEFT) && (mouseX > SP1x) && (mouseX < SP1x+btnsWidth) && (mouseY > SPy) && (mouseY < SPy+btnsHeigth) ) {
    m.speed = 1.0f;
    frameRate(30);
  }else if (mousePressed && (mouseButton == LEFT) && (mouseX > SP2x) && (mouseX < SP2x+btnsWidth) && (mouseY > SPy) && (mouseY < SPy+btnsHeigth) ) {
    m.speed = 1.5f;
    frameRate(45);
  }
}

public void checkUInput(){// controller para la UI inferior
  if (mousePressed && (mouseButton == LEFT) && (mouseX > UI0x) && (mouseX < UI0x+150) && (mouseY > UIy) && (mouseY < UIy+60) ) { //play button    
     if(!m.state){
       m.state = true;
     }
  }else if (mousePressed && (mouseButton == LEFT) && (mouseX > UI1x) && (mouseX < UI1x+150) && (mouseY > UIy) && (mouseY < UIy+60) ) {//pause button
     if(m.state){
       m.state = false;
     }
  }else if (mousePressed && (mouseButton == LEFT) && (mouseX > UI2x) && (mouseX < UI2x+150) && (mouseY > UIy) && (mouseY < UIy+60) && !m.state ) {//+pu_1
    m.users[0].addProcess(new Process("User1"));
  }
  else if (mousePressed && (mouseButton == LEFT) && (mouseX > UI3x) && (mouseX < UI3x+150) && (mouseY > UIy) && (mouseY < UIy+60) && !m.state && m.mode == 'M') {//+pu_2
    m.users[1].addProcess(new Process("User2"));
  }
}

public void drawPU1(){  
  int i = 100;
  for(Process item : m.users[0].userQueue.queue) {
    fill(item.ownColor);
    rect(2,i,296,100);
    fill(0);
    text(item.name,20,i+30);// nombre del proceso
    fill(255);
    text(getPTy(item.state),200, i+60);// estado del proceso
    rect(2,i+70,296,15);// fondo barra del progreso    
    fill(color(0,255,0));    
    rect(2,i+70,map(item.progress,0,item.totalDuration,0,296),15);// fondo barra del progreso 
    i+=100;
  } 
}

public void drawPU2(){  
  if(m.mode == 'M'){
    int i = 100;
    for(Process item : m.users[1].userQueue.queue) {
      fill(item.ownColor);
      rect(302,i,296,100);
      fill(0);
      text(item.name,320,i+30);
      fill(255);
      text(getPTy(item.state),500, i+60);// estado del proceso
      rect(302,i+70,296,15);// fondo barra del progreso    
      fill(color(0,255,0));     
      rect(302,i+70,map(item.progress,0,item.totalDuration,0,296),15);// fondo barra del progreso       
      i+=100;
    } 
  }
}


public String getPTy(char type){
  switch(type){
    case 'R':
      return "Running";    
    case 'W':
      return "Waiting";
    case 'P':
      return "Paused";
    case 'F':
      return "finished";
    default:
      return "NAN";
  }
}

public void reset(){
 m = new Machine();
 cpuload = new ArrayList();
}
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
  public int   ownColor;
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
  public int col1=color(0);
  ColorContainer(int c_) {
     col1=c_;
  }
} 


  
  public void settings() {  size(1400,800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "TallerUnoSO" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
