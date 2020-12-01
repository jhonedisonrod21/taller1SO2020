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

void setup(){
  size(1400,800);
  frameRate(30);
  background(210);
}

void draw(){
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

void keyPressed() {
  if(keyCode == 67){
    reset();
  }
}

void cpu(){
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

void drawCpuline(){
  int base = 692;  
   for (int i = cpuload.size()-1; i > 0; i--) {
      stroke(cpuload.get(i).col1);      
      line(base+Math.abs(i-cpuload.size()),200,base+Math.abs(i-cpuload.size()),260);
   }
}


void sCPU(){// serial cpu processing
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


void lCPU(){// batch cpu procesing
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

void mCPU(){ // mu batch procesing

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




void drawBase(){ //  dibuja la base de la UI
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


void drawbuttonsPT(){ //dibuja los botones del procesing type  
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

void drawbuttonsSP(){ // dibuja los botones de la velocidad
  if(m.speed == 0.5){
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
  if(m.speed == 1.5){
    fill(color(0,240,0));  // verde
  }else{
    fill(color(255,255,0)); // amarillo
  }
  rect(SP2x,SPy,btnsWidth,btnsHeigth);
  fill(0);
  text("1.5",SP2x+70,SPy+25);
}

void drawbuttonsUI(){ // dibuja los botones de la UI inferior
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

void checkPTInput(){ // controller para los tipos de procesamiento
  if (mousePressed && (mouseButton == LEFT) && (mouseX > PTSx) && (mouseX < PTSx+btnsWidth) && (mouseY > PTy) && (mouseY < PTy+btnsHeigth) ) {
    m.mode = 'S';
  }else if(mousePressed && (mouseButton == LEFT) && (mouseX > PTLx) && (mouseX < PTLx+btnsWidth) && (mouseY > PTy) && (mouseY < PTy+btnsHeigth) ){
    m.mode = 'L';
  }else if(mousePressed && (mouseButton == LEFT) && (mouseX > PTMx) && (mouseX < PTMx+btnsWidth) && (mouseY > PTy) && (mouseY < PTy+btnsHeigth) ){
    m.mode = 'M';
  }
}

void checkSPInput(){// controller para las velocidades
  if (mousePressed && (mouseButton == LEFT) && (mouseX > SP0x) && (mouseX < SP0x+btnsWidth) && (mouseY > SPy) && (mouseY < SPy+btnsHeigth) ) {
    m.speed = 0.5;
    frameRate(15);
  }else if (mousePressed && (mouseButton == LEFT) && (mouseX > SP1x) && (mouseX < SP1x+btnsWidth) && (mouseY > SPy) && (mouseY < SPy+btnsHeigth) ) {
    m.speed = 1.0;
    frameRate(30);
  }else if (mousePressed && (mouseButton == LEFT) && (mouseX > SP2x) && (mouseX < SP2x+btnsWidth) && (mouseY > SPy) && (mouseY < SPy+btnsHeigth) ) {
    m.speed = 1.5;
    frameRate(45);
  }
}

void checkUInput(){// controller para la UI inferior
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

void drawPU1(){  
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

void drawPU2(){  
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


String getPTy(char type){
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

void reset(){
 m = new Machine();
 cpuload = new ArrayList();
}
