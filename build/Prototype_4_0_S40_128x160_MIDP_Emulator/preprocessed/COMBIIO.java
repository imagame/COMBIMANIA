import java.io.*;

public class COMBIIO {
  public byte OBSw, OBSh; //
  public byte OBSdat[]; //Datos del obstáculo

  private int OBSnum;//Número de obstáculos
  private short OBScab[]; //offsets de los obstaculos contenidos en el fichero (offsets a partir de cabecera -OBSlenCab bytes-)
  private short OBSlenCab; //longitud en bytes de la cabecera en el fichero
  private short OBSid; //Id del obstáculo cargado, -1 si no hay ninguno cargado
  private InputStream OBSis;

  public COMBIIO() {
    OBSid = -1;
    OBS_CabLoad();
  }

private void OBS_Abrir(){
    OBSis = getClass().getResourceAsStream("/OBS.bin");
}


//----------------------------------------------------------------- OBS_CabLoad
// Desc: Carga los offsets de todos los obstaculos definidos en el fichero
//       en el array <OBScab> (offsets a partir de la cabecera del fichero)
//-----------------------------------------------------------------------------
  private void OBS_CabLoad(){
    try{
      OBS_Abrir();
      OBScab = new short[OBSnum = OBSis.read()];
      for(int i=0; i< OBSnum; i++)
        OBScab[i] = (short) (( (OBSis.read() & 0xff) << 8) | (OBSis.read() & 0xff));
      OBSlenCab = (short)(1 + OBSnum * 2);
      OBSis.close();
      OBSis = null;
    }
    catch(Exception ioex){ }
  }


// Desc: Carga datos del obstáculo <pidObs> a no ser que ya estén cargados
  public void OBS_DatLoad(short pidObs){
    if (pidObs != OBSid){
      try {
        //Leer datos de obstáculo
        OBS_Abrir();
        OBSis.skip(OBSlenCab + OBScab[pidObs]);
        //Leer tamaño de area ocupada por obstáculo
        OBSw = (byte) OBSis.read();
        OBSh = (byte) OBSis.read();
        OBSdat = new byte[OBSw * OBSh];
        OBSis.read(OBSdat);
        OBSis.close();
        OBSis = null;
        OBSid = pidObs; //registrar el obstaculo guardado (para futura reutiliz)
      }
      catch (IOException ioex) {
        System.out.println("OBS_DatLoad - Exc: " + ioex);
      }
      //catch(Exception ioex){ioex.printStackTrace();}
      //catch(Exception ioex){}
    }
  }


  public void OBS_Unload(){
    if(OBSid != -1){
      OBSdat = null;
      OBSid=-1;
    }
  }
}