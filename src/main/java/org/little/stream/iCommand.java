package org.little.stream;

import org.little.stream.mq.mq_mngr;

public interface iCommand{


       public abstract void  clear();

       public abstract int   start(mq_mngr mngr_default);
       public abstract void  run();
       public abstract void  cmd_close();
       public abstract void  close();


}