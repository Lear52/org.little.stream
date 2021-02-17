package org.little.stream.file;



import org.little.util.tfork;

public class delayXML implements cmdXML{

       private int    timeout;
       private boolean  is_run;

       public delayXML() {
              clear();
       }
       public delayXML(int _timeout){
              timeout=_timeout;
       }

       public void clear() {
              timeout=0;
              isRun(true);
       }


       public void open(){
       }
       public void work(){


              if(timeout!=0)tfork.delayMs(timeout);
              isRun(false);
              
       }

       public void close(){
       }

       public long getCount(){return 0;}

       public boolean isRun(){
              return true;
       }
       public void isRun(boolean r){
              is_run=r;
       }

}
