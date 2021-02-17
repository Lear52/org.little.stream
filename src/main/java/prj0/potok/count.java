package prj0.potok;

public  class count{
       final private static String CLASS_NAME="prj0.util.count";
       final private static int    CLASS_ID  =1028;
             public  static String getClassName(){return CLASS_NAME;}
             public  static int    getClassId(){return CLASS_ID;}
             //private static Logger log=new Logger(CLASS_NAME);

        static private Object lock = new Object();

        long   start;
        long   last;
        long   lcount;
        long   scount;
        long   slast;
        double tps;
        double stps;

        public count(){
               init();
        }

        public void init(){
               start=java.lang.System.currentTimeMillis();
               last=start;
               lcount=0;
               scount=0;
               slast=start;
               tps=0;
               stps=0;
        }

        public void add(){

               synchronized (lock) {
                             last=java.lang.System.currentTimeMillis();
                             lcount++;
                             long dt=last-start;
                             if(dt==0)dt=1;
                             tps=(double)lcount/(double)dt*1000.0;
                             dt=last-slast;if(dt==0)dt=1;
                             if(dt>1000){
                                stps=(double)(lcount-scount)/(double)dt*1000.0;
                                slast=last;
                                scount=lcount;
                             }
               }
        }

        public long   get(){return lcount;}
        public double getTPS(){return tps;}
        public double getCorrentTPS(){return stps;}

        public String toString(){
        return "count:"+get()+" tps:"+getTPS()+" current:"+getCorrentTPS();
        }

};
