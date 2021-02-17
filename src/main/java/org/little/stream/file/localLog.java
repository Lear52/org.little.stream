package org.little.stream.file;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.little.util.Logger;


public class localLog{
       final private static String CLASS_NAME="prj0.stream.file.localLog";
       final private static int    CLASS_ID  =2102;
             public  static String getClassName(){return CLASS_NAME;}
             public  static int    getClassId(){return CLASS_ID;}
             private static Logger log=new Logger(CLASS_NAME);

       private FileWriter file;
       private String     filename;


       public localLog(String _filename) {
              open(_filename);
       }
       public localLog() {
              clear();
       }
       public void clear() {
              file=null;
              filename=null;
       }

       public void put(String msg) {
              if(file==null)return;
              try{
              SimpleDateFormat sfd =new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss_S");
              String msg_t        =sfd.format(new java.util.Date());

              file.write(java.lang.System.currentTimeMillis()+" "+msg_t+" "+msg+"\n");
              file.flush();

              } catch (IOException e) {}


       return ;
       }
       public boolean open(String _filename) {
              try{
                  filename=_filename;
                  file=new FileWriter(filename,true);
              } catch (IOException e) {log.error("open file:"+filename+" ex:"+e);clear();return false;}

              if(file!=null)log.info("open file:"+filename+" ret:ok");
              return true;
       }
       public void close() {
              if(file==null)return;
              try{
              file.close();
              } catch (IOException e) {log.error("close file:"+filename+" ex:"+e);}
              clear();
       return ;
       }

       public static void main(String[] args) {


       }

}
