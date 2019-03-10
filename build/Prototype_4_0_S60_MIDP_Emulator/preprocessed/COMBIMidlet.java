/* This file was created by Nokia Developer's Suite for J2ME(TM) */
import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;


public class COMBIMidlet extends MIDlet
{
  public static COMBIMidlet M = null;
  public static RecordStore RS;
  public byte MENnivOpen[] = {19,  0,-1, 0,0,0,0};//Lista de IDs del mayor nivel abierto en cada fase de los distintos modos de juego
  public long MENtimRecord[] = new long[60];
  public int MENscoreRecord;
  public boolean MENbMus, MENbSnd; //opciones de activación de música y sonido

        private Display display;
        private COMBICanvas canvas;


        public COMBIMidlet()
        {
          M = this;
       }

        protected void startApp(  )
        {
          System.gc();

          //Comprobar si dispositivo soporta MMAPI
          /*
          if(packageExist("javax.microedition.media.Manager")) {
                System.out.println("Mobile Media API (JSR 135): " +
                        System.getProperty("microedition.media.version") + "-" +
                        System.getProperty("supports.video.capture"));
          }
*/

          //Gestión de datos de configuración guardados en RecordStore
          CNF_Load();
          if(canvas == null)
            canvas = new COMBICanvas(this);
          display = Display.getDisplay(this);
          display.setCurrent(canvas);
        }

        protected void destroyApp( boolean p1 ) //throws MIDletStateChangeException
        {
          //Liberar memoria
          canvas.GAM_Unload();
        }


        public static void salir(){
        //Parar musica si está activa
        //  if(c.ez != null)
        //    c.ez.stop();
        M.destroyApp(true);
        M.notifyDestroyed();
        M = null;
       }


        protected void pauseApp(  )
        {
          //notifyPaused();
        }


//Comprobar existencia de paquete
        boolean packageExist(String clazz) {
            try {
              Class check = Class.forName(clazz);
              check = null;
            } catch(Exception ign) {
              return false;
            }
            return true;
        }



//-------------------------------------------------------------------- CNF_Save
// Desc: Crea o graba (en funcion de pnew) los datos de config del midlet
//----------------------------------------------------------------------------
        public void CNF_Save(boolean pnew){
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          DataOutputStream dos = new DataOutputStream(baos);
          try
          {
              for(byte i=0; i < 7; i++)
                      dos.writeByte(MENnivOpen[i]);
              dos.writeBoolean(MENbMus);
              dos.writeBoolean(MENbSnd);
              for(byte i=0; i <60 ; i++)
                dos.writeLong(MENtimRecord[i]);
              dos.writeInt(MENscoreRecord);
              dos.close();

              byte abyte0[] = baos.toByteArray();
              RS = RecordStore.openRecordStore("Config", true);
              if(pnew)
                  RS.addRecord(abyte0, 0, abyte0.length);
              else
                  RS.setRecord(1, abyte0, 0, abyte0.length);
              RS.closeRecordStore();
          }
          catch(Exception exception) { }
        }


//-------------------------------------------------------------------- CNF_Init
// Desc: Inicializa o carga los datos de config del recordstore
//----------------------------------------------------------------------------
        private void CNF_Load(){
          try{
            RS = RecordStore.openRecordStore("Config", true);
            if(RS.getNumRecords()==0){ //Si fichero config no existe
              //inicializar a cero los records y activadas las opciones de sonido.
              MENbMus = MENbSnd = true;
              for(byte i=0; i<60; i++)
                MENtimRecord[i] = 0;
              MENscoreRecord=0;
              RS.closeRecordStore();
              CNF_Save(true); //Crear recordstore
            }else{
              byte aRs[] = RS.getRecord(1);
              DataInputStream dis = new DataInputStream(new ByteArrayInputStream(aRs));
              //Leer máximo nivel desbloqueado en cada una de las fases
              for(byte i=0; i < 7; i++)
                      MENnivOpen[i] = dis.readByte();
              MENbMus = dis.readBoolean();
              MENbSnd = dis.readBoolean();
              for(byte i=0; i<60; i++)
                MENtimRecord[i] = dis.readLong();
              MENscoreRecord = dis.readInt();
              dis.close();
              RS.closeRecordStore();
            }

          } catch(Exception ex) {
            System.out.println("CNF_Init - Exc: " + ex);
          }
        }



        static
        {
            System.gc();
        }
}