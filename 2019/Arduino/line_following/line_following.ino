#include <Pixy2.h>
#include <Wire.h>
#include <math.h>
#define PI 3.1415926535897932384626433832795
Pixy2 pixy;
int angleList[5];
int index = 0;
// 0 is false 1 is true;
int isDataAvailable = 0;
// John Marangola
// Line following script for Pixy2 / arduino

void setup(){ //setting stuff up
  pixy.init();
  pixy.changeProg("line");
  configureCamera();
  Wire.begin(4);
  Wire.onRequest(receiveProtocol);
  Wire.onReceive(receiveProtocol);
  Serial.begin(115200);
  Serial.print("Initializing processes...\n");
  isDataAvailable = false;
}

void configureCamera(){
  // just a dump of default settings so we can change them all in the same place
  pixy.line.setMode(LINE_MODE_WHITE_LINE);
  pixy.setLamp(10, 0);
  pixy.setCameraBrightness(60);
  pixy.getResolution();
}

void receiveProtocol(){// successful communications if this works at all lol
  Serial.print("Receiving information from Master device...");
} 

void loop(){
  int i = 0;
  int x = 0;
  int sum = 0;
  pixy.line.getMainFeatures();  
  //Serial.println(burstAverage(1));
  // my buffer function :: 
  if (isDataAvailable == 1){
    angleList[0] = getAngle(pixy.line.vectors[pixy.line.numVectors-1]);
    for (int i = 0; i<sizeof(angleList); i++){
      angleList[i + 1] = angleList[i];
    }
    for (int x = 0; x<sizeof(angleList); x++){
      sum += angleList[x];
    }
    return (sum/sizeof(angleList));
  }
  else {
    angleList[index] = getAngle(pixy.line.vectors[pixy.line.numVectors-1]);
    index++;
    if (index == 5){
       isDataAvailable = 1;
       index = 0;
    }
  }
}

double getAngle(Vector Line){
  // IK this is technically a waste of memory and we have a limited capacity but this improves readibility a LOT!
  double tailX = Line.m_x0;
  double tailY = Line.m_y0;
  double headX = Line.m_x1;
  double headY = Line.m_x1;
  double yLen = abs(tailY - headY);
  double xLen = abs(tailX - headX);
  double angle = atan2(yLen, xLen);
  //right rotation is MY positive convention
  //leftward rotation is MY negative convention
  // if leftward facing right triangle yee : 
  if (angleFacing(Line) == "left"){
    angle = angle * (180 / PI);
    angle = -1*(90 - angle);
    //Serial.println(angle);
    return angle;
  }
  // if rightward facing right triangle:
  else if (angleFacing(Line) == "right"){
    angle = angle * (180 / PI);
    angle = 90 - angle;
    //Serial.println(angle);
    return angle;
  }
  else { //error out of range return value to check for --nothing should be below -90 degrees
    return -10000000;
  }
}

String angleFacing(Vector Line){
  if (Line.m_x1 >= Line.m_x0){
    return "right";
  }
  else if (Line.m_x1 < Line.m_x0)
  {
    return "left";
  }
  else{
    return "error";
  }
 
}
double burstAverage(int nVectors){
  double angleList[nVectors];
  int8_t i; 
  double sum = 0;
  double average;
  for (i = 0; i < nVectors; i++){
    sum = sum + getAngle(pixy.line.vectors[pixy.line.numVectors -1]);
  }
  return (sum/nVectors);
 
}
// not currently used, could be used to optimize in future
double df(double x1, double y1, double x2, double y2){
  double x_diff = x1-x2;
  double y_diff = y1-y2;
  return sqrt(pow(2, (x_diff)) + pow(2, (y_diff)));
}

//testing function: 
void printAllDetectedLines(){
  int8_t i;
  char buf[128];
  for (i=0; i<pixy.line.numVectors; i++){
    sprintf(buf, "line %d: ",i);
    Serial.print(buf);
    pixy.line.vectors[i].print();
  
  }
}
boolean isValidAngle(boolean angle){return (angle < 90 && angle > -90);}
