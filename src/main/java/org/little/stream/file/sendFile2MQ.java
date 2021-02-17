package org.little.stream.file;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import org.little.util.Except;
import org.little.util.Logger;

public class sendFile2MQ{
       final private static String CLASS_NAME="prj0.stream.file.sendFile2MQ";
       final private static int    CLASS_ID  =2105;
             public  static String getClassName(){return CLASS_NAME;}
             public  static int    getClassId(){return CLASS_ID;}
             private static Logger log=new Logger(CLASS_NAME);

       private connect2MQ  q;
       private boolean     is_read_file;
       private boolean     is_send_msg;
       private int         size_msg;
       final static private String id0="guid:sat-84004949.02000.6577402.3735650.1.ed701.xml-unicend";
       final static private String  empty_msg_1="<?xml version=\"1.0\" encoding=\"Windows-1251\"?>\n"
+"<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">\n"
+"  <env:Header>\n"
+"    <props:MessageInfo xmlns:props=\"urn:cbr-ru:msg:props:v1.3\">\n"
+"      <props:To>uic:451111111100</props:To>\n"
+"      <props:From>uic:452222222211</props:From>\n"
+"      <props:AppMessageID>";
//guid:sat-84004949.02000.6577402.3735650.1.ed701.xml-unicend
       final static private String  empty_msg_2="</props:AppMessageID>\n"
+"      <props:MessageID>";
//guid:sat-84004949.02000.6577402.3735650.1.ed701.xml-unicend
       final static private String  empty_msg_3="</props:MessageID>\n"
+"      <props:MessageType>1</props:MessageType>\n"
+"      <props:Priority>5</props:Priority>\n"
+"      <props:CreateTime>2018-08-10T05:07:01Z</props:CreateTime>\n"
+"      <props:AckRequest>true</props:AckRequest>\n"
+"    </props:MessageInfo>\n"
+"    <props:DocInfo xmlns:props=\"urn:cbr-ru:msg:props:v1.3\">\n"
+"      <props:DocFormat>1</props:DocFormat>\n"
+"      <props:DocType>ED701</props:DocType>\n"
+"      <props:EDRefID EDNo=\"300054000\" EDDate=\"2018-08-10\" EDAuthor=\"4522222222\"/>\n"
+"    </props:DocInfo>\n"
+"  </env:Header>\n"
+"  <env:Body>\n"
+"    <sen:SigEnvelope xmlns:sen=\"urn:cbr-ru:dsig:env:v1.1\">\n"
+"      <sen:SigContainer>\n"
+"        <dsig:MACValue xmlns:dsig=\"urn:cbr-ru:dsig:v1.1\">MIIBYAYJKoZIhvcNAQcCoIIBUTCCAU0CAQExDjAMBggqhQMHAQECAgUAMAsGCSqGSIb3DQEHATGCASkwggElAgEBMFcwQzELMAkGA1UEBhMCUlUxCzAJBgNVBAgTAjQ1MQwwCgYDVQQKEwNDQlIxDDAKBgNVBAsTA01DSTELMAkGA1UEAxMCQ0ECEEBQFMBruXkZhaYgSluWSYwwDAYIKoUDBwEBAgIFAKBpMBgGCSqGSIb3DQEJAzELBgkqhkiG9w0BBwEwHAYJKoZIhvcNAQkFMQ8XDTE5MDIwNzE0MTMxN1owLwYJKoZIhvcNAQkEMSIEINW6SvNhatZN6DTtNRCln5uqBUbKN3eiuwgOQngeshVhMAwGCCqFAwcBAQEBBQAEQNJooz0CY2R9aTW5OvGV4sAoXLTUmRXZZO3RCxjvtftMh6Kri2DRkQTlGLNo1KsWKRR6o8AW//5Gm+a95UUgNSA=</dsig:MACValue>\n"
+"      </sen:SigContainer>\n"
+"      <sen:Object>";

       final static private String  empty_msg_4="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";

       final static private String  empty_msg_5="</sen:Object>\n"
+"    </sen:SigEnvelope>\n"
+"  </env:Body>\n"
+"</env:Envelope>";

       final static private String test_buf=empty_msg_1+id0+empty_msg_2+id0+empty_msg_3+empty_msg_4+empty_msg_5;



       public sendFile2MQ() {
              clear();
       }
       public void clear() {
              q = new connect2MQ();
              if(System.getProperty("prj0.stream.file.is_read","true").equals("true"))is_read_file=true;
              else is_read_file=false;
              if(System.getProperty("prj0.stream.file.is_send","true").equals("true"))is_send_msg=true; 
              else is_send_msg=false;
              String s=System.getProperty("prj0.stream.file.msg_size");
              size_msg=0;
              if(s!=null){
                 try { size_msg=Integer.parseInt(s, 10);
                       log.trace("set prj0.stream.file.msg_size:"+size_msg);
                 } 
                 catch (Exception e) {
                       log.error("error parse prj0.stream.file.msg_size:"+s);
                       size_msg=0;
                 }
              }
              else log.trace("set prj0.stream.file.msg_size:0");
       }
       public void close(){
               q.close(); q=null; 
       }

       private byte [] getBuffer() {
              String id="guid:sat-"+System.currentTimeMillis()+"."+System.nanoTime()+".1.ed701.xml-unicend";

              String  msg="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"
                         +"=";

              if(size_msg>0){
                 StringBuilder buf0 = new StringBuilder(50000);
                 for(int i=0;i<size_msg;i++)buf0.append((char)((i%10)+60));
                 buf0.append("=");
                 msg=buf0.toString();
              }
              
              String str=empty_msg_1+id+empty_msg_2+id+empty_msg_3+msg+empty_msg_5;
              //log.error("BEFFER\n"+str);
              return str.getBytes();
       }


       private void run(String filename) {
              run(Paths.get(filename));
       }

       public void run(Path _file) {

              InputStream in=null;
              int         f_size=0;
              byte []     buf=null;

              if(is_read_file){
                 try{
                     f_size=(int)Files.size(_file);
                     in = Files.newInputStream(_file);
                 }
                 catch (Exception ex1){
                     log.error("open file:"+_file +" ex:"+Except.printException(ex1));
                     return;
                 }
                 buf=new byte[f_size];
                 try{
                     in.read(buf, 0, buf.length);
                 }
                 catch (Exception ex1){
                     log.error("read file:"+_file +" ex:"+Except.printException(ex1));
                     return;
                 }
              }
              else buf=getBuffer();

              if(is_send_msg)q.put(buf);

              if(is_read_file){
                 try{
                     in.close();
                 }
                 catch (Exception ex1){
                     log.error("close file:"+_file+" ex:"+Except.printException(ex1));
                 }
              }
              //-------------------------------------------------------------
              _file=null;
       }
       public int init(String _connect_qm_name,String _jms) {
              return q.init(_connect_qm_name,_jms);
       }
       public int init(String _connect_qm_name,String _send_q_name,String _send_qm_name) {
              return init(_connect_qm_name,_send_q_name,_send_qm_name);
       }
       public int init(String _connect_qm_name,String _host,int _port,String _channel,String _send_q_name,String _send_qm_name) {
              return init(_connect_qm_name,_host,_port,_channel,_send_q_name,_send_qm_name);
       }


       private static void work(String _connect_qm_name,String _send_q_name,String host,int port,String channel,String _send_qm_name,String filename){

              sendFile2MQ r=new sendFile2MQ();

              if(_connect_qm_name!=null || channel==null){
                 if(r.q.init(_connect_qm_name,_send_q_name,_send_qm_name)<0)return;
              }
              else{
                 if(r.q.init(_connect_qm_name,host,port,channel,_send_q_name,_send_qm_name)<0)return;
              }
              r.run(filename);
              r.q.close();
       }

       public static void help(String[] args) {
                 System.out.println("run java "+getClassName()+" manager_name queue_name||queue_name@manager_name [hostname port channel] filename");
       }
       public static void main(String[] args) {


              if((args.length<3) ||(args.length>=3 && args.length<6)){
                 help(args);
                 return;
              }
              String connect_qmanager_name=null;
              String send_qm_name=null;
              String send_q_name =null;
              String hostname    =null;
              String portname    =null;
              String channel     =null;
              String filename    =null;
              int    port=0;

              connect_qmanager_name=args[0];
              send_qm_name=connect_qmanager_name;
              send_q_name  =args[1];
              StringTokenizer parser_q;
              parser_q = new StringTokenizer(send_q_name, "@");
 
              if(parser_q.hasMoreTokens()) {
                 send_q_name=parser_q.nextToken();
                 if(parser_q.hasMoreTokens()) {
                    send_qm_name=parser_q.nextToken();
                 }
              }

              filename    =args[2];
              if(args.length>=6){
                 hostname    =args[2];
                 portname    =args[3];
                 channel     =args[4];
                 filename    =args[5];
              }
              if(portname!=null) try { port=Integer.parseInt(portname, 10);} catch (Exception e) {port=0;hostname=null;channel=null;}

              sendFile2MQ.work(connect_qmanager_name,send_q_name,hostname,port,channel,send_qm_name,filename);

       }

}
