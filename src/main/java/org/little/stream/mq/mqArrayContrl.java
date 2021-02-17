package org.little.stream.mq;

import java.util.ArrayList;

import org.little.util.Logger;


public class mqArrayContrl{
       final private static String CLASS_NAME="prj0.stream.mq.mqArrayContrl";
       final private static int    CLASS_ID  =1702;
             public  static String getClassName(){return CLASS_NAME;}
             public  static int    getClassId(){return CLASS_ID;}
             private static Logger log=new Logger(CLASS_NAME);

       private ArrayList<mqContrl> list_cntrl;

       public mqArrayContrl(){
              clear();
       }

       public mqContrl  open(String jms_connection)  throws  mqExcept{
              mqContrl  cntrl=null;
              for(int i=0;i<list_cntrl.size();i++){
                  cntrl=list_cntrl.get(i);
                  if(cntrl.isURL(jms_connection)&&(!cntrl.isUse())){
                     //log.trace("util connect to mq string connection:"+jms_connection);
                     break;
                  }
              }
              if(cntrl==null){
                 cntrl=new mqContrl();
                 cntrl.init(jms_connection);
                 cntrl.open();
                 //log.trace("open new connect to mq string connection:"+jms_connection);
              }
              cntrl.isUse(true);
              return cntrl;
       }
       public void close(mqContrl cntrl){
              if(cntrl!=null) cntrl.isUse(false);
       }
       public void closeAll(){
              for(int i=0;i<list_cntrl.size();i++){
                  try{
                  list_cntrl.get(i).close();
                  }
                  catch(mqExcept e){
                        log.error("close mqControl ex:"+e);
                  }
              }

       }

       public void clear() {
              list_cntrl=new ArrayList<mqContrl>(10);
       }
 

}


