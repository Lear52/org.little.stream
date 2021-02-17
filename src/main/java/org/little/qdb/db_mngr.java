/*
 * Created on 24.12.2020
 * Modification 24/12/2020
 *
 */
package org.little.qdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

//------------------------------------------------
public class db_mngr{
       final private static String CLASS_NAME="org.little.qdb.db_mngr";
       final private static int    CLASS_ID  =204;
             public  static String getClassName(){return CLASS_NAME;}
             public  static int    getClassId(){return CLASS_ID;}
             //private static Logger log=new Logger(CLASS_NAME);

       private String  name;
       private ArrayList<db_queue> list_queue;

       public db_mngr(){
              name=null;
              list_queue=new ArrayList<db_queue>(10);
       }




}

