package org.little.stream;


//import prj0.util.Logger;

public class def{
       final private static String CLASS_NAME="org.little.stream.def";
       final private static int    CLASS_ID  =1604;
	          public  static String getClassName(){return CLASS_NAME;}
             public  static int    getClassId(){return CLASS_ID;}
             //private static Logger log=new Logger(CLASS_NAME);

  public static final java.lang.String CNFGQUEUE                            = "SYSTEM.STREAM.CONFIG";
  public static final java.lang.String CMDQUEUE                             = "SYSTEM.STREAM.COMMAND";
  public static final int              CMDWAIT                              = 15000;
  public static final int              CFGWAIT                              = 1000;
  public static final int              CFGINTERVAL                          = 1000;
  public static final int              CMDINTERVAL                          = 1000;
  public static final int              MSGWAIT                              = 15000;
  public static final int              MSGINTERVAL                          = 1000;
  public static final java.lang.String CMDSTOP                              = "stop routing";
  //public static final int              BFRLNGTH_INIT                        = 1024;
  public static final int              BFRLNGTH_INIT                        = 4194304;
  public static final int              BFRLNGTH_MAX                         = 314572800;


  public static final java.lang.String LOG_FORMAT_CSV                       = "CSV";
  public static final java.lang.String LOG_FORMAT_PLAIN                     = "PLAIN";
  
  public static final java.lang.String ERR_LOG_PATH_KEY                     = "ru.factorts.stream.errlog.path";
  
  public static final int              RET_OK                               = org.little.stream.mq.def.RET_OK   ;
  public static final int              RET_WARN                             = org.little.stream.mq.def.RET_WARN ;
  public static final int              RET_ERROR                            = org.little.stream.mq.def.RET_ERROR;
  public static final int              RET_FATAL                            = org.little.stream.mq.def.RET_FATAL;

  public static final int              CMD_INIT                             = 0;
  public static final int              CMD_RUN                              = 1;
  public static final int              CMD_STOP                             = -1;

                                                    
}                                                   
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    
                                                    