package org.little.stream.file;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.little.util.Logger;

public class sendDir2MQ implements cmdXML{
       final private static String CLASS_NAME="prj0.stream.file.sendDir2MQ";
       final private static int    CLASS_ID  =2104;
             public  static String getClassName(){return CLASS_NAME;}
             public  static int    getClassId(){return CLASS_ID;}
             private static Logger log=new Logger(CLASS_NAME);

       private String                 connect_qm_name;
       private String                 send_q_name;
       private String                 host;
       private int                    port;
       private String                 channel;
       private String                 send_qm_name;
       private String                 global_dirname;
       private String                 local_dirname;
       private long                   time_range;
       private int                    tps;      
       private String                 topic;
       private String                 jms;
       private localLog               _log;
       private sendFile2MQ            send;
       private ArrayList<pathElement> list;
       private long                   count;
       private boolean                is_run;

       private static Hashtable<String,ArrayList<pathElement>> h_list = new Hashtable<String,ArrayList<pathElement>>();

       public sendDir2MQ() {
              clear();
       }
       public sendDir2MQ(String _connect_qm_name,String _jms,String _global_dirname,String _local_dirname,long _time_range,String _topic){
              clear();
              connect_qm_name=_connect_qm_name;
              global_dirname =_global_dirname ;
              local_dirname  =_local_dirname  ;
              time_range     =_time_range     ;
              topic          =_topic          ;
              jms            =_jms            ;

       }

       public sendDir2MQ(String _connect_qm_name, String _send_q_name,String _host,int _port,String _channel,String _send_qm_name,String _global_dirname,String _local_dirname,long _time_range,String _topic) {
              clear();
              connect_qm_name=_connect_qm_name;
              send_q_name    =_send_q_name    ;
              host           =_host           ;
              port           =_port           ;
              channel        =_channel        ;
              send_qm_name   =_send_qm_name   ;
              global_dirname =_global_dirname ;
              local_dirname  =_local_dirname  ;
              time_range     =_time_range     ;
              topic          =_topic          ;
       }

       public void addCount() {
                   count++;
       }
       public long getCount() {
              long c;
                   c=count;
              return c;
       }

       public boolean isRun(){
              return is_run;
       }
       public void isRun(boolean r){
              is_run=r;
       }

       public void clear() {
              connect_qm_name=null;
              send_q_name    =null;
              host           =null;
              port           =0;
              channel        =null;
              send_qm_name   =null;
              global_dirname =null;
              local_dirname  =null;
              time_range     =0;
              tps            =0;
              topic          =null;
              jms            =null;
              _log           =null;
              send           =null;
              list           =null;
              count          =1;
              isRun(true);
       }


       public void open(){
              send=new sendFile2MQ();

              synchronized (h_list){
                       String key=global_dirname+"/"+local_dirname;
                       log.trace("search dir_list:"+key);
                       list=h_list.get(key);

                        if(list==null){
                           log.trace("begin create new dir_list:"+key);
                           list=pathElement.getDir(global_dirname,local_dirname,time_range);
                           if(list==null){
                              log.trace("dir_list:"+key+" is_empty");
                              clear();
                              return;
                           }
                           log.trace("end create new dir_list:"+key);
                       
                           h_list.put(key,list);
                        }
                        else{
                           log.trace("get dir_list:"+key);
                        }
              }

              //------------------------------------------------
              if(jms==null){
                 if(send.init(connect_qm_name,host,port,channel,send_q_name,send_qm_name)<0){
                    log.error("connect:"+connect_qm_name);
                    clear();
                    return;
                 }
              }
              else{
                 if(send.init(connect_qm_name,jms)<0){
                    log.error("connect:"+connect_qm_name +" jms:"+jms);
                    clear();
                    return;
                 }
              }

              _log=new localLog(topic+".txt");
       }
       public void work(){
              if(list==null || send==null)return;
              //------------------------------------------------
              long st=System.currentTimeMillis();

              count=1;

              while(list.size()>0 && isRun()){
                  pathElement o=null;
                  long        t=0;

                  synchronized(list){
                     o=list.get(0);
                     t=System.currentTimeMillis()-(o.getStart()+st);
                     if(t>=0){
                        list.remove(0);
                     }
                     else o=null;
                  }

                  if(o!=null){
                     // посылаем сообщение из файла с именем (Path) полученного из o.get();
                     send.run(o.get());

                     long tt=System.currentTimeMillis()-st;
                     double tps;
                     if(tt==0) tps=0;
                     else      tps=((double)getCount())/((double)tt)*1000.0;

                     _log.put(" "+getCount()+" "+o.getFullName()+" time:"+tt+" pts:"+tps + " dt:"+t);

                     addCount();

                     o.clear();
                  }
                  else{
                     if(t<0)try{Thread.sleep(-t);}catch(Exception e){}
                  }
                  
              }
              isRun(false);
              //------------------------------------------------
              close();
       }
       public void close(){
              if(_log!=null)_log.close();
              if(send!=null)send.close();
              clear();
       }
       public static void help(String[] args) {
              System.out.println("run java "+getClassName()+"manager_name_for_connect queue_name_for_send[@manager_name_for_send] [hostname port channel] global_dir_name time_range_sek [local_dir_name]");
       }
       public static void main(String[] args) {


              if((args.length<4) || (args.length>=4 && args.length<7)){
                 help(args);
                 return;
              }
              if(args.length==3)System.out.println("run java "+getClassName()+"["+args.length+"]"+args[0]+" "+args[1]+" "+args[2]+" "+args[2]);
              else              System.out.println("run java "+getClassName()+"["+args.length+"]"+args[0]+" "+args[1]+" "+args[2]+" "+args[3]+" "+args[4]+" "+args[5]+" "+args[6]);

              String con_manager_name =null;
              String send_qm_name     =null;
              String send_q_name      =null;
              String hostname         =null;
              String portname         =null;
              String channel          =null;
              String global_dirname   =null;
              String local_dirname    =null;
              String _range_time      =null;
              long   range_time       =0;
              int    port=0;

              con_manager_name     =args[0];
              send_qm_name         =args[0];
              send_q_name          =args[1];
              global_dirname       =args[2];
              _range_time          =args[3];
              if(args.length>=4)   local_dirname=args[4];
              
              StringTokenizer parser_q;
              parser_q = new StringTokenizer(send_q_name, "@");
 
              if(parser_q.hasMoreTokens()) {
                 send_q_name=parser_q.nextToken();
                 if(parser_q.hasMoreTokens()) {
                    con_manager_name=parser_q.nextToken();
                 }
              }
              
              if(args.length>=6){
                 hostname    =args[2];
                 portname    =args[3];
                 channel     =args[4];
                 global_dirname     =args[5];
                 _range_time        =args[6];
                 if(args.length>=7)local_dirname     =args[7];
              }
              if(portname   !=null) try { port=Integer.parseInt(portname, 10);} catch (Exception e) {port=0;hostname=null;channel=null;}
              if(_range_time!=null) try { range_time=Long.parseLong(_range_time, 10);} catch (Exception e) {range_time=0;}

              range_time*=1000;

              sendDir2MQ s=new sendDir2MQ(con_manager_name,send_q_name,hostname,port,channel,send_qm_name,global_dirname,local_dirname,range_time,"stat_file");
              s.open();
              s.work();
              s.close();
       }

}
