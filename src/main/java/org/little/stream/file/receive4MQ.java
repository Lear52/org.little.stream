package org.little.stream.file;

import java.util.StringTokenizer;

import org.little.util.Logger;


public class receive4MQ implements cmdXML{
       final private static String CLASS_NAME="prj0.stream.file.receive4MQ";
       final private static int    CLASS_ID  =2105;
             public  static String getClassName(){return CLASS_NAME;}
             public  static int    getClassId(){return CLASS_ID;}
             private static Logger log=new Logger(CLASS_NAME);

       private connect2MQ  q;
       private boolean     is_run;


       public receive4MQ() {
              clear();
       }
       public void clear() {
              q=null;
              isRun(true);
       }
       public void open(){
              q = new connect2MQ();
              String _connect_qm_name=null;
              String _receive_q_name =null;
              String host            =null;
              int port               =0   ;
              String channel         =null;
              String _receive_qm_name=null;

              if(_connect_qm_name!=null || channel==null){
                 if(q.init(_connect_qm_name,_receive_q_name,_receive_qm_name)<0)return;
              }
              else{
                 if(q.init(_connect_qm_name,host,port,channel,_receive_q_name,_receive_qm_name)<0)return;
              }
       }
       public void close(){
               q.close(); q=null; 
       }

       public boolean isRun(){
              return is_run;
       }
       public void isRun(boolean r){
              is_run=r;
       }

       public long getCount(){return 0;}

       public void work() {

              while(isRun()){
                     byte [] buf=null;
                     buf=q.get();
                     if(buf!=null)log.trace("get msg size:"+buf.length);
              }
              isRun(false);
       }
       public int init(String _connect_qm_name,String _jms) {
              return q.init(_connect_qm_name,_jms);
       }
       public int init(String _connect_qm_name,String _receive_q_name,String _receive_qm_name) {
              return init(_connect_qm_name,_receive_q_name,_receive_qm_name);
       }
       public int init(String _connect_qm_name,String _host,int _port,String _channel,String _receive_q_name,String _receive_qm_name) {
              return init(_connect_qm_name,_host,_port,_channel,_receive_q_name,_receive_qm_name);
       }



       public static void help(String[] args) {
                 System.out.println("run java "+getClassName()+" manager_name queue_name||queue_name@manager_name [hostname port channel]");
       }
       public static void main(String[] args) {


              if((args.length<3) ||(args.length>=3 && args.length<6)){
                 help(args);
                 return;
              }
              String connect_qmanager_name=null;
              String receive_qm_name=null;
              String receive_q_name =null;
              String hostname    =null;
              String portname    =null;
              String channel     =null;
              int    port=0;

              connect_qmanager_name=args[0];
              receive_qm_name=connect_qmanager_name;
              receive_q_name  =args[1];
              StringTokenizer parser_q;
              parser_q = new StringTokenizer(receive_q_name, "@");
 
              if(parser_q.hasMoreTokens()) {
                 receive_q_name=parser_q.nextToken();
                 if(parser_q.hasMoreTokens()) {
                    receive_qm_name=parser_q.nextToken();
                 }
              }

              if(args.length>=5){
                 hostname    =args[2];
                 portname    =args[3];
                 channel     =args[4];
              }
              if(portname!=null) try { port=Integer.parseInt(portname, 10);} catch (Exception e) {port=0;hostname=null;channel=null;}
              receive4MQ r=new receive4MQ();
              r.open();
              r.work();
              r.close();

       }

}
