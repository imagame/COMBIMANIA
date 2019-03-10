import java.io.*;
import javax.microedition.lcdui.*;
import com.nokia.mid.ui.*;

public class COMBILVL {
           //#if CFGtamanyoGfx == 2
//#            public static final byte NUMPIXELS_TILEX = 12; //Número de pixels en un tile horizontal
//#            public static final byte NUMPIXELS_TILEY = 12;
           //#else
           public static final byte NUMPIXELS_TILEX = 8; //Número de pixels en un tile horizontal
           public static final byte NUMPIXELS_TILEY = 8;
           //#endif
    
  private static final short NUMPIXELS_TILE_XY = NUMPIXELS_TILEX * NUMPIXELS_TILEY;
  private static final byte NUMTILES_SCRX = 14; //Número de tiles horizontales en una pantalla
  private static final byte NUMTILES_SCRY = 14;
  private static final byte HORZ = 0;
  private static final byte VERT = 1;
  private static final byte ARR = 1;
  private static final byte ABA = 2;
  private static final byte IZQ = 3;
  private static final byte DER = 4;
  private static final byte NUMTIPPIE = 8;
  private static final byte NUMCOLORES = 4;

  //General
  public static byte idLvl; //identificador unico del nivel
  public static byte idModo; //id del modo de juego (0: tutorial, 1: arcade, 2: reflexión)
  public static byte idFase; //id de la fase (0..F-1)
  public static byte idNiv; //id del nivel dentro de la fase (0..N-1)
  public static byte confFaseMod[] = {1,2,4}; //Número de fases por modo
  public static byte confFaseNivMod[][] = { //Número de niveles por fase para cada modo
      {20, 0, 0, 0}, {10, 10, 0, 0}, {20, 20, 10, 10}
  }; //Importante: La configuración de este arrays afecta a <MODconfNiv>

  //Gráficos
  public static byte idBorPixlim;
  public static int colbase;
  public static short pix[];    // Tiles graficos en formato short del nivel en curso (64 * NUMPIXELS_TILEXY)
  public static byte idxStrTit; //Indice sobre GAMstr que indica título del Nivel

  //Texto
  public static boolean bTut; //Hay tutorial en el nivel en curso
  public static byte sitPag; //Situacion en pantalla de la página de msgs. Valores: 0:HUD off y pag abajo, 1: HUD on y pag en centro, 2: HUD om y pag arriba
  public static byte idPag;  //id de la página de texto de intro correspondiente al nivel (-1 si no tiene texto)
  public static String TXTstr[]; //Líneas de texto de las páginas del nivel
  public static String HUDstr; //KKTXT

  //Mapa
  public static byte numTilesW, numTilesH; //Ancho y Alto en tiles del mapa contenido en NIVmap
  public static byte numTotBor; //Número de bordes totales (externos + internos) del nivel
  public static byte NIVmap[]; //Mapa de tiles del nivel
  public static byte NIVzon[]; //Número de bordes en cada zona del escenario
  public static byte NIVbor[][];  //IMPORTANTE: Ordenado de izq a der y de arr a aba (por 1,3,4)
  //[0]: id de zona: 0..N
  //[1]: tipo: VERT/HORZ
  //[2]: sentido: ARR/ABA/DER/IZQ
  //[3]: Posición fija (X si VERT / Y si HORZ). Posiciones de 0 a max-1
  //[4]: Posición desde (Y si VERT / X si HORZ)
  //[5]: Posición hasta (Y si VERT / X si HORZ)
  //[6]: Número de orden en la zona (en sentido horario)
  public static byte NIVobs[]; //lista de ids de obstáculos en el mapa, para el nivel
  //[i]: id de obstaculo (para identificación dentro del fichero OBS.bin)
  //[i+1]: Pos X en mapa del nivel
  //[i+2]: Pos Y en mapa del nivel

  //Elementos de juego
  public static byte NIVtipPie[]; //Tipos de piezas permitidas en el modo de aparición aleatoria
  public static byte NIVcolPie[]; //Colores de piezas permitidos en el modo de aparición aleatoria
  public static byte NIVtipPieProb[];//Probabilidad aparición de cada tipo de pieza (0..100)(todo el vector suma 100)
  public static byte NIVtipPieProbAcu[]; //Probabilida acumulada de cada tipo de pieza.
  public static byte NIVcolPieProb[];//Probabilidad aparición de cada color (0..100)(todo el vector suma 100)
  public static byte NIVcolPieProbAcu[]; //Probabilida acumulada de cada tipo de pieza.
  public static byte PIEtipSec; //0:sin secuencia, >0 número de piezas en la secuencia
  public static byte PIEnumRepSec; //secuencia repetitiva: -1 repetición ilimitada, >0 nº de repeticiones de la secuencia (antes de pasar a modo aleatorio)
  public static byte PIEsec[]; //Secuencia de aparición de piezas (tip, ori, col)
  public static int PIEdur; //Límite de tiempo de colocación de piezas (en ms),// 0 si no hay límite
  public static int PUNbase; //Puntuación base del combi (1x1) en el nivel actual

  //Cursor
  public static byte CURcontInit[]; //Contador inicial de uso de cada modo de Cursor
  public static byte CURmodoDef; //Modo de cursor inicial
  public static byte CURxDef, CURyDef; //Posición inicial del cursor
  public static byte PROidBorDef;

  //Piezas/Combis por defecto
  public static byte PIEnumDef, COMnumDef;
  public static byte PIEdef[]; //Array de piezas por defecto: (posX, posY, tip, ori, col)
  public static byte COMdef[]; //Array de combis por defecto: (posX, posY, w, h, col)
                             //[Pte]: incluir <nivel> en comDef si se desean mostrar combis compuestos

//Reglas cumplimiento de Objetivos
  public static byte OBJnumRul;
  public static byte OBJrul[][];
//----------------------------------------------- Descripción campos OBJrul[][]
//[0]: Tipo de objetivo
//   - 0: GENERAR Combi
//   - 1: OCUPAR (aplica 1,2, 3(indica si 1es 4,5,6,7(Area),15
//   - 2: CAMINO (reservado para uso futuro)
//   - 3..6: POSPIE, POSPROP, BORPROP, INFO
//[1]: Para tipo 0: Número de combis a generar
//     Para tipo 1: a) Num mínimo de combis que deben ocupar el área de juego, si [3]=1
//                  b) -1: núm libre de combis para ocupar la superficie, si [3]=-1
//     Para tipo 6: valor 0
//[2]: Color => (-2: cualquier color, -1: monocolor, 0: multicolor, 1..N: color determinado)
// Opciones (1 regla con -2 // 1 regla con -1 // 2 reglas: -1 y 0 // N reglas con 0..N)
//[3]: Para tipo 0: Número mínimo de colores (sólo aplica si [2]==0, -1: n/a)
//     Para tipo 1: (1) ocupación num combis, (-1)ocupación superficie
//[4]: Posición X (de inicio) => (-1: n/a)   // Tambien valor asociado a [15]
//[5]: Posición Y (de inicio) => (-1: n/a)
//[6]: Dimensión W (ancho) mínima => (-1: n/a)
//[7]: Dimensión H (alto) mínima => (-1: n/a)
//[8]: Área mínima => (-1: n/a)
//[9]: Forma requerida: (0: rectangular, 1: cuadrada) => (-1: n/a)
//[10]: Cantidad de piezas => (-1: n/a, 0:mínimo, 1: exacto)
//[11]: Número de piezas => -1 si [10] es -1
//[12]: Nivel mínimo del combi (-1: n/a, 0: nivel exacto 1(combi simple), >=1: nivel mínimo que debe tener el combi)
//[13]: Composición a base de piezas (-1: n/a, 0: sin piezas sueltas, 1: con piezas sueltas)
//[14]: Composición a base de combis (-1: n/a, 0: igual tamaño de combis hijo (sin importar nº), N: formado por N combis hijo de nivel superior)
//[15]: Tipo de pieza (-1:n/a, N>0: combi con exactamente N tipos de pieza diferentes, 0: combi que contiene la pieza indicada en [4]))

//PAra tipo [0] = 3: POSPIE, 4: POSPRO, 5: BORPRO, 6:INFO
//[1]: valor 1 para todos, excepto [6] que tendrá valor 0 ????????
//[2]: color
//[3]: idx gráfico (sobre HUDpix)
//[4][5]: X,Y   (de pieza, de x en borde, o de flecha info)
//[6]: Tip de pieza, id de borde
//[7]: Ori (de pieza y borde)
//[8..14]: valor 0 (sin uso)


  public static boolean OBJbEliGen; //booleano que indica si reglas eliminación coinciden con reglas Gen
  public static boolean OBJbEliDefPie;// Booelano que indica si deben regenerarse las piezas por defectos q hayan sido eliminadas (por Combi o cursor)
  public static byte OBJnumRulEli;
  public static byte OBJrulEli[][]; //Reglas de Eliminación de combis para el nivel en curso
//[0]: tipo. Valores:
//  tipo 0.- (INCLUSIÓN)Eliminar Combi si se genera encima/dentro de contorno objetivo de Combi
//   (le aplica el valor 1 como tipo solap: 1 solap, 2 dentro)
//  tipo 1.- (CONJUNCIÓN o TIPOLOGÍA) Eliminar Combis compuestos
//         color,w,h, compuestos de combis igual tamaño
//  tipo 2.- (POR DEFECTO) Eliminación de combi si no cumple ningún objetivo de Generación
//[1]: color (aplica a tipo 1), o posición relativa con sup obj (aplica a tipo 0)
//  Para tipo 1: Color => (-2: cualquier color, -1: monocolor, 0: multicolor, 1..N: color determinado)
//               Opciones (1 regla con -2 // 1 regla con -1 // 2 reglas: -1 y 0 // N reglas con 0..N)
//  Para tipo 0: Posición relativa permitida =>
//            1: permite solapado e internos(borra solo externos)
//            2: permite sólo internos (borrar externos y solapados)
//[2]: w (aplica a tipo 1 si [4]!=-1)
//[3]: h (aplica a tipo 1 si [4]!=-1)
//[4]: tipDim (-1: n/a, 0: dim w y h mínimas, 1: dim w y h máximas(w,h commutativa), 2:dim w o h máximas , [PTE]: 3 dim exactas) (aplica a tipo 1)
//[5]: forma (pte uso futuro)
//[6]: Num pie -Exacto- (pte uso futuro)
//[7]: nivel (0: simple -borra si combi solo contiene piezas--, i>1: borrar si combi en nivel i de jerarquía de contención combis)(pte uso futuro)
//[8]: Comp pieza (pte uso futuro)
//[9]: Composición de combis hijo de igual tamaño (-1: n/a, 0: igual tamaño de combis hijo sin importar nº, N: igual tamaño y formado por N combis) (pte uso futuro)
//[10] Tip pieza (pte uso futuro)


  public static byte OBJincBonus; // Si 0 es que no hay bonus de utilización de los modos cursor por creación de combis en el nivel en curso
  public static byte OBJrulBonus[]; //24 pos
  //dimMono[4], areaMono[4], bonusMono[4],
  //dimMulti[4], areaMulti[4], bonusMulti[4]
  //bonus (suma binaria):
  //1: inc desp com
  //2: inc desp pie
  //4: inc eli
  //8: inc col
  //16: inc lanz

  //----------------------------------------------------- Propiedades privadas
  //IMPORTANTE: cada pos del sig array debe sumar confFaseNivMod_i
  private byte MODconfNiv[] = {20,20,60}; //Número de niveles total por modo
  private static short TXTconfPag[]; //Offset en fichero TXT.bin de cada página de texto
  // de 0..7: texto genérico a cada fase (Cada fase una página)
  // de 8..N: texto especifico de cada página (cada nivel debe asignarse a una página, excepto si idPag=-1)
  private static byte TXTconfLin[]; //Número de líneas de texto para cada página

  private byte NIVconfPixTema[] = {28, 15, 28}; //Primer tile de cada tema que delimita borde gráfico con tile de tablero (utilizable para colocar pieza/combi)
  private int NIVconfColTema[] = {0xFF969696, 0xFFBEA8A6, 0xFF88BB99};
  private String NIVconfFilTema[] = {"/T0.png","/T1.png","/T2.png"}; //Nombre de ficheros gráficos de cada tema

  private byte idTema; //Tema gráfico correspondiente al nivel en curso
  private byte idMap; //Mapa correspondiente al nivel en curso
  private InputStream LVLis;




//-------------------------------------------------------------------- COMBILVL
public COMBILVL(){
    idFase = idNiv = idLvl = idTema = -1;
    NIVmap = new byte[NUMTILES_SCRX*2*NUMTILES_SCRY*2]; //784 bytes máximo de mapeado
    pix = new short[64 * NUMPIXELS_TILE_XY];    //64 tiles graficos como máximo en cada tema
    NIVtipPie = new byte[NUMTIPPIE];
    NIVtipPieProb = new byte[NUMTIPPIE];
    NIVtipPieProbAcu =  new byte[NUMTIPPIE];
    NIVcolPie = new byte[NUMCOLORES];
    NIVcolPieProb = new byte[NUMCOLORES];
    NIVcolPieProbAcu =  new byte[NUMCOLORES];
    CURcontInit = new byte[5];
    OBJrulBonus = new byte[24];
}


public void CargarNivel() {
  byte aux;
  //Si fase y nivel ya están cargados (y mapa no ha sido updated con obstaculos??)
  if(ObtIdLvl()){ //Si el nivel ha cambiado
    NIVzon = null;
    NIVbor = null;
    NIVobs = null;
    PIEsec = null;
    PIEdef = null;
    COMdef = null;
    TXTstr = null;

    OBJrul = null;
    OBJrulEli = null;
    ObtIdStrLvl();

    System.gc();
    try{
      LVL_Abrir(); //abre fichero de la fase actual y se posiciona en inicio de nivel actual
      //Acceder a la posición exacta del Nivel en el fichero
      int salto=2*ObtNumPrevNiv();
      for (int i = 0; i < salto; i += LVLis.skip(salto - i)); //Salta definición de offsets de cabecera hasta situarnos en el del nivel en curso

      int offset = ((LVLis.read() & 0xff) << 8) | (LVLis.read() & 0xff);
      offset +=2*MODconfNiv[idModo]-salto-2;
      for (int i = 0; i < offset; i += LVLis.skip(offset - i)); //Salta: Offset desde la cabecera + resto de cabecera = (num_niv*2bytes - 2+

      if ( (aux = (byte) LVLis.read()) != idTema) { //Cargar Tema nuevo sólo si el nuevo nivel no usa el tema actual
        idTema = aux;
        idBorPixlim = NIVconfPixTema[idTema];
        colbase = NIVconfColTema[idTema];
        LoadPixTema();
      }

      //Cargar variables de control de juego
      //Tipos de Piezas permitidas en el nivel
      ObtenerNIVtipPie(LVLis.read());
      for (int i = 0; i < NUMTIPPIE; i += LVLis.read(NIVtipPieProb, i, NUMTIPPIE - i));
      NIVtipPieProbAcu[0] = NIVtipPieProb[0];
      for (int i = 1; i < NUMTIPPIE; i++)
        NIVtipPieProbAcu[i] = (byte)(NIVtipPieProbAcu[i-1]+NIVtipPieProb[i]);

      //Colores permitidos en el nivel
      ObtenerNIVcolPie(LVLis.read());
      for (int i = 0; i < NUMCOLORES; i += LVLis.read(NIVcolPieProb, i, NUMCOLORES - i));
      NIVcolPieProbAcu[0] = NIVcolPieProb[0];
      for (int i = 1; i < NUMCOLORES; i++)
        NIVcolPieProbAcu[i] = (byte)(NIVcolPieProbAcu[i-1]+NIVcolPieProb[i]);

      //Sistema de aparición de piezas (secuencia vs aleatorio)
      if((PIEtipSec = (byte) LVLis.read()) > 0){
        PIEsec = new byte[PIEtipSec*3];
        for (int i = 0; i < PIEsec.length; i += LVLis.read(PIEsec, i, PIEsec.length - i));
      }
      PIEnumRepSec = (byte) LVLis.read();
      PIEdur =  ((LVLis.read() & 0xff) << 24) | ((LVLis.read() & 0xff) << 16) |
                 ((LVLis.read() & 0xff) << 8) | (LVLis.read() & 0xff);

      //Contadores modo cursor
      for (int i = 0; i < CURcontInit.length; i += LVLis.read(CURcontInit, i, CURcontInit.length - i));
      CURmodoDef = (byte)LVLis.read();
      CURxDef = (byte)LVLis.read();
      CURyDef = (byte)LVLis.read();
      PROidBorDef = (byte)LVLis.read();

      //Variables sistema puntuación
      PUNbase = ((LVLis.read() & 0xff) << 8) | (LVLis.read() & 0xff);

      //Piezas por defecto en el nivel
      if((PIEnumDef = (byte) LVLis.read()) > 0){
        PIEdef = new byte[PIEnumDef*5];
        for (int i = 0; i < PIEdef.length; i += LVLis.read(PIEdef, i, PIEdef.length - i));
      }

      //Combis por defecto en el nivel
      if((COMnumDef = (byte) LVLis.read()) > 0){
        COMdef = new byte[COMnumDef*5];
        for (int i = 0; i < COMdef.length; i += LVLis.read(COMdef, i, COMdef.length - i));
      }



      //Cargar Datos identificativos del Mapa de juego y Páginas de introducción al mapa
      idMap = (byte)LVLis.read();
      idPag = (byte) LVLis.read();
      sitPag = (byte) LVLis.read();
      bTut =  LVLis.read()==1?true:false;

      //Leer info de obstáculos para el nivel
      if((aux = (byte) LVLis.read()) > 0){
        NIVobs = new byte[aux * 3];
        for (int i = 0; i < aux * 3; i += LVLis.read(NIVobs, i, aux*3 - i));
      }

      //Leer objetivos del Nivel
      //OBJ_Cargar();
      OBJnumRul = (byte)LVLis.read();
      OBJrul = new byte[OBJnumRul][16];
      for(int i=0; i<OBJnumRul; i++)
        for(int j=0; j<16; j += LVLis.read(OBJrul[i], j, 16 - j));


      //Leer Reglas de Eliminación para el nivel
      OBJbEliGen= LVLis.read()==1?true:false;
      OBJbEliDefPie = LVLis.read()==1?true:false; //dejar cambiar piezas por def
      if((OBJnumRulEli = (byte) LVLis.read()) > 0){
        OBJrulEli = new byte[OBJnumRulEli][11];
        for(int i=0; i<OBJnumRulEli; i++)
          for(int j=0; j<11; j += LVLis.read(OBJrulEli[i], j, 11 - j));
      }

     //Leer info de Bonus para el nivel
     if((OBJincBonus = (byte) LVLis.read()) > 0)
       for(int i=0; i<24; i += LVLis.read(OBJrulBonus, i, 24 - i));
       //OBJ_RulBonusCargar();


      LVLis.close();
      LVLis = null;
    }
    catch(Exception ioex){
        ioex.printStackTrace();
    }

    //Cargar Mapa e información de Zonas y Bordes
    NIVmap_Cargar();
    TXT_Cargar(); //Carga texto de la página del nivel
  }
}

//---------------------------------------------------------------- OBTIdStrLvl
private void ObtIdStrLvl(){
  int idx = 0;
  for(int i=0; i<idModo; i++)
    idx+=confFaseMod[i];
  idxStrTit = (byte)(idx + idFase + 7); //7: idx del primer titulo en GAMstr
}



private void ObtenerNIVtipPie(int pval){
  for(int i=0,j=1;i<NUMTIPPIE;i++, j*=2)
    NIVtipPie[i] = (byte)((pval & j)==0?0:1);
}
private void ObtenerNIVcolPie(int pval){
  for(int i=0,j=1;i<NUMCOLORES;i++, j*=2)
    NIVcolPie[i] = (byte)((pval & j)==0?0:1);
}


private void LVL_Abrir(){
    LVLis = getClass().getResourceAsStream("/MOD"+Integer.toString(idModo)+".bin");
}


//------------------------------------------------------------------- ObtSigLvl
// Desc: obtiene el siguiente nivel de juego al actual, manteniendo el modo
//       de juego
// Return: 1: Hay siguiente nivel (en fase en curso, o sig fase--solo arcade--)
//         2: Fin última fase de modo arcade (vuelta completada)
//         -1:Fin fase modo Reflexión o Tutorial
//-----------------------------------------------------------------------------

public byte IncLvl(){
    short nivlim;
    idNiv++; //Incrementamos el nivel actual

    if(idNiv == confFaseNivMod[idModo][idFase]) //Si hemos llegado al último nivel de la fase
      if(idModo==1){ //En modo Arcade las fases son cíclicas. (no hay final)
        idNiv=0;
        if (idFase == confFaseMod[1]-1){
          idFase = 0;
          return 2;
        }else{
          idFase++;
          return 1;
        }
      }else
        return -1; //fin fase (= fin partida)
    else
      return 1; //sig nivel en fase
  }


//------------------------------------------------------------------- ObtIdLvl
// Desc: Establece identificador único del nivel en base a modo, fase y nivel
//       dentro de la fase.
// Return: Verdadero si el nivel a cargar es diferente al actual
//----------------------------------------------------------------------------
private boolean ObtIdLvl(){
  byte lvl=0;

   //sumar los niveles de modos previos
    if (idModo==1)
      lvl = MODconfNiv[0];
    else if (idModo==2){
      lvl = MODconfNiv[0];
      lvl += MODconfNiv[1];
    }
    lvl += ObtNumPrevNiv(); //sumar los niveles previos del modo actual

    if(idLvl != lvl){
      idLvl = lvl;
      return true;
    }
    return false;
}

//Obtiene el número de niveles anteriores al nivel actual que existen en el
// modo en curso.
private short ObtNumPrevNiv(){
    byte lvl=0;
    for(int i=0; i<idFase; i++)
      lvl+=confFaseNivMod[idModo][i];
    lvl+=idNiv;
  return lvl;
}

//----------------------------------------------------------------- NIV_LoadPix
// Desc: Carga de tiles gráficos de fondo para los niveles con tema <pTema>
//-----------------------------------------------------------------------------
private void LoadPixTema(){
    Image imgPix=null;
      try {
       imgPix = Image.createImage(NIVconfFilTema[idTema]);
      }
      catch (IOException e) {
       System.out.println("EX - NIV_LoadPix");
      }
      Graphics g = DirectUtils.createImage(NUMPIXELS_TILEX,NUMPIXELS_TILEY, 0xFFFF).getGraphics();
      DirectGraphics dg = DirectUtils.getDirectGraphics(g);
      short pixels_aux[] = new short[NUMPIXELS_TILE_XY];
      for(int j=0; j<8; j++)
          for (int i = 0; i < 8; i++) {
              g.drawImage(imgPix, -NUMPIXELS_TILEX * i, -NUMPIXELS_TILEY * j, Graphics.TOP | Graphics.LEFT);
              dg.getPixels(pixels_aux, 0, NUMPIXELS_TILEX, 0, 0, NUMPIXELS_TILEX, NUMPIXELS_TILEY, 4444);
              System.arraycopy(pixels_aux, 0, pix, (j * 8 + i) * NUMPIXELS_TILE_XY, NUMPIXELS_TILE_XY);
          }
      pixels_aux = null;
      imgPix = null;
      g = null;
      dg = null;
      System.gc();
  }

//--------------------------------------------------------------- NIVmap_Cargar
// Desc: Carga mapa de juego en NIVmap
//       No borra el contenido existente en NIVmap, machaca sólo la parte que
//       vaya a utilizar.
//----------------------------------------------------------------------------
private void NIVmap_Cargar(){
    try {
      InputStream is = getClass().getResourceAsStream("/MAP.bin");

      int numMaps = is.read();


      for (int i = 0; i < (idMap<<1); i += is.skip((idMap<<1) - i)); //situarse en def del offset del nivel en cabecera
      int offset = ((is.read() & 0xff) << 8) | (is.read() & 0xff);
      offset += (numMaps - idMap - 1)*2;
      for (int i = 0; i < offset; i += is.skip(offset - i)); //Saltar offset desde punto en el que estamos

      numTilesW = (byte) is.read();
      numTilesH = (byte) is.read();
      int maplen = numTilesW * numTilesH;
      for (int i = 0; i < maplen; i += is.read(NIVmap, i, maplen - i));

//Leer número de zonas y número de bordes en cada Zonas
      numTotBor = 0;
      NIVzon = new byte[is.read()];
      for(int i=0; i<NIVzon.length; i++){
        NIVzon[i] = (byte) is.read();
        numTotBor += NIVzon[i];
      }
//Leer info de bordes
      NIVbor = new byte[numTotBor][7];
      for(int i=0; i<numTotBor; i++)
        for(int j=0; j<7; j += is.read(NIVbor[i], j, 7 - j));


      is.close();
      is = null;

    }
    catch (Exception ex) {
       System.out.println("EX - NIVmap_Cargar");
     }
}

//------------------------------------------------------------------ TXT_Cargar
// Desc: Carga las líneas de texto correspondientes a la página de texto del
// nivel <idPag>.
//----------------------------------------------------------------------------
 private void TXT_Cargar(){
   int idxFas=0;
   short idxNiv = 0;

   TXT_CargarConf();
   if(idModo!=0 || (idModo==0 && idNiv==0)){ //En modo tutorial sólo aplica al primer nivel
     try {
       DataInputStream dis = new DataInputStream(getClass().getResourceAsStream("/TXT.bin"));

       if (idModo == 0)
         idxFas = idFase;
       else if (idModo == 1)
         idxFas = confFaseMod[0] + idFase;
       else
         idxFas = confFaseMod[0] + confFaseMod[1] + idFase;
       idxNiv = TXTconfLin[idxFas];
       TXTstr = new String[idxNiv + (idPag!=-1?TXTconfLin[idPag]:0)];

       //se situa en la página inicial de texto del modo
       for (int i = 0; i < TXTconfPag[idxFas];i += dis.skipBytes(TXTconfPag[idxFas] - i));
       //Lee líneas de texto
       for (int i = 0; i < idxNiv; i++)
         TXTstr[i] = dis.readUTF();
       dis.close();

     }
     catch (Exception ex) {
       System.out.println("EX - TXT_Cargar(fase)");
     }
   }

   //Cargar texto específico del nivel (si hay)
   if(idPag !=-1){
     try {
       DataInputStream dis = new DataInputStream(getClass().
                                                 getResourceAsStream(
           "/TXT.bin"));
       //se situa en la página inicial del nivel
       for (int i = 0; i < TXTconfPag[idPag];
            i += dis.skipBytes(TXTconfPag[idPag] - i));
       //Lee líneas de texto
       if(idModo==0 && idNiv>0)
         TXTstr = new String[TXTconfLin[idPag]];
       for (int i = 0; i < TXTconfLin[idPag]; i++)
         TXTstr[idxNiv + i] = dis.readUTF();
       dis.close();
     }
     catch (Exception ex) {
       System.out.println("EX - TXT_Cargar(nivel)");
     }
   }
   
   
   //HUDstr = "Sigue detenidamente este tutorial para conocer todo acerca de los Combis.\nSerá una referencia imprescindible para poder completar los objetivos propuestos tanto en los niveles del modo Arcade como en los del modo Reflexión. Además deberás conseguir que este string sea más largo de lo que originalmente tenía pensado que iba a ser. Así puedo probar el texto.";
    HUDstr = "Sigue detenidamente este tutorial para conocer todo acerca de los Combis.";   
 }



//-------------------------------------------------------------- TXT_CargarConf
// Desc: Carga configuración de offsets por páginas de texto.
//----------------------------------------------------------------------------
 /*
   TXTconfPag= { //Offset en fichero TXT.bin de cada página de texto
       0x0000, 0x00fe, 0x00fe, 0x025c, 0x0430, 0x05f2, 0x0744, //0..6
       0x087b, 0x08c8, 0x0929, 0x0968, 0x09bc, 0x0a49, 0x0ab8, 0x0b16, 0x0b8e, 0x0bed, //8..17  Arcade-F0
       0x0c3f, 0x0c92, 0x0cfe, 0x0d7a, 0x0df6, 0x0e6d, 0x0ecc, 0x0f3f, 0x0fb1, 0x1022, //18..27 Arcade-F1

   };
   */
  /*
  = TXTconfLin{
      8,12,12,15,15,11,10, //0..6
      3,3,2,3,5,4,3,4,3,3, //7..16  Arcade-F0
      3,4,4,4,4,4,4,4,4,4 //17..26 Arcade-F1
  }; //Para cada página: número de líneas
  */

private void TXT_CargarConf(){
 if(TXTconfPag==null){
   TXTconfPag = new short[76];
   TXTconfLin = new byte[76];
   try{
     DataInputStream dis = new DataInputStream(getClass().getResourceAsStream("/config.bin"));
     for (int i = 0; i < 76; i ++)
       TXTconfPag[i] = dis.readShort();
     for (int i = 0; i < 76; i ++)
       TXTconfLin[i] = dis.readByte();
     dis.close();
   }
   catch (Exception ex) {
     System.out.println("EX - TXT_CargarConf");
   }
 }

}



}