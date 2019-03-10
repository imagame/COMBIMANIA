/* This file was created by Nokia Developer's Suite for J2ME(TM) */

// import com.nokia.mid.sound.Sound;
 import com.nokia.mid.ui.*;
 import java.io.*;
 import java.util.Random;
 import javax.microedition.lcdui.*;
 //#if CFGtipoSnd == 1
//#  import com.nokia.mid.sound.*;
 //#endif
         


 
 public class COMBICanvas extends FullCanvas  implements Runnable
 {
   //#if CFGtipoSnd == 2
   public static COMBIMusic MUS;
   //#endif
   private COMBILVL LVL;
   private COMBIIO CIO;


//	********************************************** Variables del dispositivo (DEV)
           private int DEVscrPxlH, DEVscrPxlW;
           public static int DEVntvFmt;
           public static short GAMscrPatchLE, HUD_RI;


//	*************************************************** Variables del juego (GAM)                   
         // Constantes de escenario (aplicable a todos los mapas de juego)
           
           
           
           
           //#if CFGtamanyoGfx == 2
//#            public static final byte NUMPIXELS_TILEX = 12; //Número de pixels en un tile horizontal
//#            public static final byte NUMPIXELS_TILEY = 12;
           //#else
           public static final byte NUMPIXELS_TILEX = 8; //Número de pixels en un tile horizontal
           public static final byte NUMPIXELS_TILEY = 8;
           //#endif
           public static final short NUMPIXELS_TILE_XY = NUMPIXELS_TILEX * NUMPIXELS_TILEY;
           public static final byte NUMTILES_SCRX = 14; //Número de tiles horizontales en una pantalla
           public static final byte NUMTILES_SCRY = 14;          
           public static final short GAMscrPxlW = NUMTILES_SCRX * NUMPIXELS_TILEX;
           public static final short GAMscrPxlH = NUMTILES_SCRY * NUMPIXELS_TILEY;                      
           public static short GAMscrPxlLE = 0;
           public static short GAMscrPxlRI = 0;
           
           ///#if CFGfnt != 0
                //#if CFGtamanyoGfx == 2
//#            public static final int FNTH = 14; //Alto fuente
//#            public static final int FNTW = 8; //Ancho fuente
                //#else
           public static final int FNTH = 8; //Alto fuente
           public static final int FNTW = 6; //Ancho fuente
                //#endif
           ///#endif


            //#if CFGtamanyoGfx == 2
//#             public static final short MENscrPxlUP = 64;
//#             public static final short MENscrPxlDO = 16;
//#             public static final short MENscrPxlOp = 18;
            //#else            
            public static final short MENscrPxlUP = 28;
            public static final short MENscrPxlDO = 12;
            public static final short MENscrPxlOp = 12;                       
            //#endif
           
           public static final byte NUMFX = 8;
           
           
         

         //Constantes de estado del juego
         public static final byte VGSTS_MENU = 0;
         public static final byte VGSTS_GAME = 1;
         public static final byte VGSTS_LOAD = 2;
         //Constantes de estados de la partida
         public static final byte PLAY_IN_INIT = 0;
         public static final byte PLAY_IN_CURSOR = 1;
         public static final byte PLAY_IN_PROPCHGBOR = 2;
         public static final byte PLAY_IN_PROPLAN = 3;
         public static final byte PLAY_IN_COMBI = 4;
         public static final byte PLAY_IN_CHKNIV = 5;
         public static final byte PLAY_OUT_PAUSA = 6;
         public static final byte PLAY_OUT_CHGNIV = 7;
         public static final byte PLAY_OUT_ENDGAM = 8;


         //Constantes movimiento y situación ACTor
           public static final byte STOP = 0;
           public static final byte ARR = 1;
           public static final byte ABA = 2;
           public static final byte IZQ = 3;
           public static final byte DER = 4;
           public static final byte FIRE = 5;
           public static final byte FIRE2 = 6;
           public static final byte AUX1 = 7;
           public static final byte AUX2 = 8;
           public static final byte AUX3 = 9;
           public static final byte AUX4 = 10;
           public static final byte AUX5 = 11;
           public static final byte SOFTLEFT = 12;
           public static final byte SOFTRIGHT = 13;

           public static final byte HORZ = 0;
           public static final byte VERT = 1;
           public static final byte NUMLINTXT = 4;

           //Constantes Estado de Pieza en Tablero
           public static final byte OCUPADO = 1;
           public static final byte LIBRE = 0;
           public static final byte IN_FX = -1;


           // Variables control juego
           private byte VGsts; //Estado del juego
           private byte GAMsts; //Estado de la partida (dentro de VGsts)
           private byte GAMsbSts; //Subestado de la partida

           //Variables Menú de juego
           private byte MENid; //id menu actual (0:main, 1:M0,2:M1,3:M2,4:Set,5:Cre,6:exit)
           private byte MENop; //Opción del menú main seleccionada: 0..5
           private byte MENsubop; //Subopción del menú 1,2,3 seleccionada: 0,1
           private byte MENval1, MENval2; //Valores de las subopciones MENsubop
           private byte MENmsg; //Id del mensaje a mostrar en el pie durante el menú.
           private byte MENkeys[] = //ids de strs de Softkeys (izq & der) para cada pantalla con softkeys
               {17,18, 19,20, 19,20, 19,20,  23,20, -1,20, 21,22, //0..6 (Menus)
                18,20, 17,-1, -1,-1, }; //7..
           private byte MENcheatSts = -1; //Estado de activación del truco de desactivación de niveles bloqueados
           private byte MENcheatCode[] = {IZQ,DER,ARR,IZQ,ARR,DER,AUX2};
           private String MENcheatPwd = "ImaGame";

         //Variables Menú pausa
           private byte MENpauCnf[] = {10,0,0,46,  2,20,30,47, 0,20,30,48}; //valores configuración pantalla pausa para todas las secciones de la misma
           //[i]:número items en pantalla <MENid>=i
           //[i+1]: offset inicio items de la sección dentro de array <MENpauPos>
           //[i+2]: offset inicio items de la sección dentro de array <MENpauTxt>
           //[i+3]: id str del título de sección
           private short MENpauPos[] = { //posición x y ancho de cada item en todas las secciones de Pausa
               1,21, 27,25, 54,12, 68,12, 86,12, 100,12,  114,12,  128,12, 142,12, 158,16, //Seccion 0
               1,21, 27,62 //seccion 1
           };
           private String MENpauTxt[] = { //cadenas de texto descriptivas de cada item de las secciones de la pantalla Pausa
           "Color del Combi", "Multicolor + [nº mín. colores]", "monocolor, o color determinado",
           "Dimensiones del Combi","Ancho x Alto", "(dimensiones mínimas)",
           "Área del Combi","Unidades de ocupación", "Ancho por alto (mínimo)",
           "Forma del Combi","C: Cuadrada", "R: Rectangular",
           "Total piezas en el Combi","[>]: mínimo requerido", "[=]: nº exacto requerido",
           "Nivel del Combi","[S]: Simple, contiene sólo piezas","[nº]: combis anidados",
           "Formado por Piezas", "S: Debe contener piezas sueltas", "N: Solo debe contener combis",
           "Formado por Combis","[=]: de mismas dimensiones", "[nº]: por un nº fijo de combis",
           "Tipo de piezas del combi", "[nº]: tipos diferentes de piezas", "[-]: tipo de pieza requerido ",
           "Estado del objetivo","[nº/] Combis conseguidos", "[/nº] Total combis a conseguir",
           "Color del Combi", "Multicolor + [nº mín. colores]", "monocolor, o color determinado",
           "Dimensiones del Combi","Ancho x Alto", "(mínimos o máximos)",
             };
           int MENsit[] = {
               10 * NUMPIXELS_TILEY-4 , 16 * NUMPIXELS_TILEY-2, //sit 0
               4 * NUMPIXELS_TILEY - 2, 10 * NUMPIXELS_TILEY, //sit 1
               2, 8 * NUMPIXELS_TILEY+4 //sit 2
           }; //Posiciones iniY,finY para posicionar ventana de  texto introductorio a inicio de nivel

           //Variables graficos
           private Image imgHud;
           //#if CFGtamanyoGfx == 2
//#            private Image imgCab;                      
           //#endif          
           //#if CFGfnt != 0
//#            private Image imgFnt;  
           //#if CFGtamanyoGfx==2
//#            private int FNTancho[] = {
//#                    6,3,5,7,7,7,8,3, 5,5,7,7,4,6,3,5,
//#                    7,5,7,7,7,7,7,7, 7,7,3,4,6,6,6,7,
//#                    8,7,7,7,7,6,6,7, 7,3,6,7,6,7,7,7,
//#                    7,7,7,7,7,7,7,7, 7,7,7,4,5,4,5,7,
//#                    4,7,7,6,7,7,6,7, 7,3,4,7,3,7,7,7,
//#                    7,7,6,6,6,7,7,7, 7,7,6,7,7,7,7,6,
//#                    6,6,4,4,5,7,7,7, 7,7,7,7,7,7,7,7,
//#                    6,7,7,7,4,4,5,7, 7,7,7,7,7,7,0,0
//#            };
           //#else
//#             private int FNTancho[] = {
//#                    6,3,5,7,7,7,8,3, 5,5,7,7,4,6,3,5,
//#                    7,5,7,7,7,7,7,7, 7,7,3,4,6,6,6,7,
//#                    8,7,7,7,7,6,6,7, 7,3,6,7,6,7,7,7,
//#                    7,7,7,7,7,7,7,7, 7,7,7,4,5,4,5,7,
//#                    4,7,7,6,7,7,6,7, 7,3,4,7,3,7,7,7,
//#                    7,7,6,6,6,7,7,7, 7,7,6,7,7,7,7,6,
//#                    6,6,4,4,5,7,7,7, 7,7,7,7,7,7,7,7,
//#                    6,7,7,7,4,4,5,7, 7,7,7,7,7,7,0,0
//#            };           
           //#endif
           //#endif
            
            
           private Image GAMspr[] = new Image[7];
           private short GAMpiePix[];// = new short[14 * 4 * NUMPIXELS_TILEXY];
           private int GAMmanip; //manipulación del grafico actual a dibujar
           private boolean GAMbsplash; //Indica si hay que dibujar logo presentación

           //Variables aparición piezas
           private byte GAMpie[][] = new byte[3][3]; //Piezas pendientes de colocar //byte[3][3]
           private byte GAMpieLastId; //Indicador de la posición de GAMpie donde está la próxima pieza que debe ser colocada
           private byte GAMpieNumRepSec; //Número de repeticiones que quedan de la secuencia predefinida de aparición de piezas.
           private byte GAMpieSigValSec; //Proximo valor de GAMpieSec a leer (0..longitud secuencia-1 si en_secuencia, -1 si No_en_secuencia
           Random rand = new Random(System.currentTimeMillis());
           private boolean GAMbPieSig; //Indica si se ha sacado la siguiente pieza en el tick en curso


         //Variables objetivos y reglas eliminación
         public static final byte TIPOBJ_GENCOM = 0;
         public static final byte TIPOBJ_OCUCOM = 1;
         public static final byte TIPOBJ_POSPIE = 3;
         public static final byte TIPOBJ_POSPROP = 4;
         public static final byte TIPOBJ_BORPROP = 5;
         public static final byte TIPOBJ_INFO = 6;

         private byte GAMobjSts[]; //Estado de los objetivos del nivel
         //[i]: num veces a conseguir el objetivo <i>
         //[i+1]: num veces conseguido el objetivo <i>.

         short GAMnumRulObj; //Número de reglas objetivos establecidos en el nivel (Como mínimo debe haber 1)
         short GAMnumTotObj; //Número total de objetivos a cumplir correspondientes a las reglas objetivo

         private boolean GAMbHayComRulEli; //Existe Combi que cumple las reglas de eliminación
         private byte GAMidComRulEli; //Id del Combi que cumple las reglas de eliminación
         private byte GAMidRul; //Id de la regla objetivo que cumple el Combi eliminado.
         private byte GAMnumComRulGen; //Número de Combis generados en secuencia que cumplen las reglas de Generación
         private byte GAMaIdRul[] = new byte[10]; //Array de ids de combis que cumplen objetivo y se han generado de forma consecutiva tras la colocación de una misma pieza
         private boolean GAMbPuntComRulGen; //Indica si ha habido algún cumplimiento de regla objetivo para puntuar combi en modo Reflexión

         private boolean GAMbChkFinNiv; //Indicas si necesario chequear fin nivel en el tick en curso
         private byte GAMobjProg; // Porcentaje progreso en cumplimiento de objetivos
         private int GAMscore; //Puntuación global de la partida
         private int GAMround; //Número de vuelta que se ha jugado a los niveles (0..N)
         private boolean GAMbRecord; //Indicador de record batido (De puntos o tiempo) tras el nivel en curso
         private byte OBJgfxSts=0; //Control dibujado de graficos asociados a OBjetivos del Nivel

//--- Datos Textos
           private boolean GAMbNivIntro; //Indica si se está mostrando el texto introductorio al nivel
          
           private boolean TXTsig, TXTant; //Indicadores de opción de navegación en tutorial
           ///#if CFGfnt==0
           private byte TXTidLinAct; //Id actual de las líneas de texto del nivel
           ///#else
           private int TXTx, TXTy; //KKTXT Posiciones globales x,y donde dibujar carácter actual
           private int TXTiniPos; //Posición inicial del string dibujado en pantalla
           private int TXTnumCars; //Número de caracteres del string dibujados en pantalla
           ///#endif
           
            private boolean TIMbPie; //Indica si está activado el contador de tiempo de colocación de pieza en el nivel en curso
            private boolean TIMbGam; //Indica si esta activado el contador de tiempo del nivel en la partida
            private long TIMpieIni, TIMgamIni; //Instante inicial aparición de pieza (inicio/restaurar partida);
            private long TIMpieAcu, TIMgamAcu; //Tiempo acumulado previo a las pausas trancurrido desde la aparicion de la ultima pieza (inicio partida)
            private long TIMpieDur; //Tiempo límite para colocar pieza en el modo Arcade

           // Variables auxiliares (controlador del game loop y hud)
           private Font fTit, fSbt, fCnt;
           public long currTime, lastTime, elapsedTime, frameTime=0, fps, tickTime = 0;

           private short PIXnum;
           private int xPoints[]; //coordenadas X para dibujo de polígonos
           private int yPoints[]; //coordenadas Y para dibujo de polígonos

           public short numFrames, currFrameNumber = 0;
           //Array de literales del VG
           //- Comandos de softkeys
           //- Títulos de niveles de modo Tutorial
           //- Títulos de Fases
           //#if CFGtamanyoGfx == 2
//#            public String GAMstr[] = {
//#                "Tutorial","Modo Arcade","Modo Reflexión","Opciones","Créditos","Salir","Menu",
//#                "COMBIpedia", "Fase básica", "Fase avanzada", //7
//#                "COMBIficio", "COMBIlatría", "COMBInología", "COMBIcracia", //10
//#                "Fin partida", "<Desbloqueado>","! NUEVO RECORD !", //14-16
//#                "OK", "Salir", "Jugar", "Volver", "Sí", "No", "Cambiar", //17->23
//#                "Primera Fase/Nivel", "Última Fase/Nivel", "Nivel Bloqueado", //24-26
//#                "Selecciona Fase y Nivel", "Cambia las opciones de juego","www.imagame.com","¿Salir de COMBImanía?","¿Abandonar partida?", //->31
//#                "Nivel ","Música: ON", "Música: OFF", "Sonido: ON", "Sonido: OFF", //32-36
//#                "Cargando", "Pulsa una tecla", "Imposible. Combi por defecto", "Avance Objetivo:",  //37-40
//#                "Nivel superado !!", "Fase finalizada !!", "Modo Cursor agotado", "Siguiente nivel","Imposible. Pieza por defecto", //41-45
//#                "Objetivos del Nivel", "Reglas Eliminación Combis", "Descripción del Nivel", //46-48
//#                "Idea, Diseño y Código", "Diseño de niveles y Testeo", //49-50
//#                "Combis que cumplan alguna","Combis que no cumplan ninguna","condición objetivo del nivel.", //51-53
//#                "Combis generados en el exterior","Combis generados sobre el","contorno, o en el exterior,","de la superficie objetivo.", //54-57
//#                "No hay eliminación de combis.","Lección aprendida !!", //58-59
//#                "Colocar correctamente las piezas", "en las posiciones indicadas.", //60
//#                "No se requiere cumplir ningún","objetivo en este nivel.", //62
//#                "Ocupar el área de juego con los","siguientes tipos de combis:",//64
//#                "Ocupar el área objetivo con tipos", "de combis: que no se eliminen.", //66
//#                "Generar los siguientes tipos","de combis:",//68
//#                "Se eliminan automáticamente los" //70
//#            };
           //#else
           public String GAMstr[] = {
               "Tutorial","Modo Arcade","Modo Reflexión","Opciones","Créditos","Salir","Menu",
               "COMBIpedia", "Fase básica", "Fase avanzada", //7
               "COMBIficio", "COMBIlatría", "COMBInología", "COMBIcracia", //10
               "Fin partida", "<Desbloqueado>","! NUEVO RECORD !", //14-16
               "OK", "Salir", "Jugar", "Volver", "Sí", "No", "Cambiar", //17->23
               "Primer Nivel", "Último Nivel", "Bloqueado", //24-26
               "Selecciona Fase y Nivel", "Cambiar opciones de juego","www.imagame.com","¿Salir de COMBImanía?","¿Abandonar partida?", //->31
               "Nivel ","Música: ON", "Música: OFF", "Sonido: ON", "Sonido: OFF", //32-36
               "Cargando", "Pulsa una tecla", "Imposible. Combi por defecto", "Avance Objetivo:",  //37-40
               "Nivel superado !!", "Fase finalizada !!", "Modo Cursor agotado", "Siguiente nivel","Imposible. Pieza por defecto", //41-45
               "Objetivos del Nivel", "Reglas Eliminación Combis", "Descripción del Nivel", //46-48
               "Idea, Diseño y Código", "Diseño de niveles y Testeo", //49-50
               "Combis que cumplan alguna","Combis que no cumplan ninguna","condición objetivo del nivel.", //51-53
               "Combis generados en el exterior","Combis generados sobre el","contorno, o en el exterior,","de la superficie objetivo.", //54-57
               "No hay eliminación de combis.","Lección aprendida !!", //58-59
               "Colocar correctamente las piezas", "en las posiciones indicadas.", //60
               "No se requiere cumplir ningún","objetivo en este nivel.", //62
               "Ocupar el área de juego con los","siguientes tipos de combis:",//64
               "Ocupar el área objetivo con tipos", "de combis: que no se eliminen.", //66
               "Generar los siguientes tipos","de combis:",//68
               "Se eliminan automáticamente los" //70
           };
           //#endif

//	**************************************** Variables Marcador (HUD)
           private String sTime = new String("123456");
           private short HUDtimAct, HUDtimMax;
           private long HUDtimgamAct; //Tiempo acumulado de partida en el nivel en curso (en ms)
           private boolean HUDbIni, HUDbScore, HUDbProg, HUDbCurCont, HUDbPie;
           private boolean GAMbHUDon;
           private short HUDlocpie[] = {4,39,22,43,40,43}; //posX,idPix de las piezas sig.
           private int HUDcol = 0xFFECE9D8; //color crema
//	**************************************** Variables Sistema Efectos Especiales (FX)

           private byte FXtip[] = { //Tipos de gfx
               1, 0,  8, 0,  2,//0: Combi GEN
               1, 0,  7,16, -1, //1: Combi ELI
               0, 8, 16, 0, -1,//2: Combi PUNTOS
               0, 8, 16, 0, -1,//3: Cursor BONUS
               0, 0, 10, 0,  3,//4: Cursor CONTADOR
               0, 0, 40, 0, -1,//5: Mensaje asíncrono
               1, 40, 50, 0,-1, //6: Mensaje síncrono
               0, 0, 6, 14, -1, //7: Pie ELI
               1, 0, 5, 0,  -1, //8: Apertura Diálogo
               1, 0, 10, 0, -1  //9: Transición pantallas
           };
         //[idx]: tipo  (0: CREAR COMBI, 1: )
         //[0]: clase (0: asíncrono, 1: síncrono)
         //[1]: tick indicador de ejecución (-1: desactivado, 0: ejecutar mas .. N:ticks restantes hasta inicio ejecución)
         //[2]: duración en ticks (0: borrar, .. N:ticks restantes de ejecución)
         //[3]: valor auxiliar para dibujado gfx
         //[4] SND-FX asociado (-1 si no tiene)


           private short FXdat[][] = new short[NUMFX][12];
           //[0]: tipo  (0: CREAR COMBI, 1: )
           //[1]: clase (0: asíncrono, 1: síncrono)
           //[2]: tick indicador de ejecución (-1: desactivado, 0: ejecutar mas .. N:ticks restantes hasta inicio ejecución)
           //[3]: duración en ticks (0: borrar, .. N:ticks restantes de ejecución)
           //[4]: valor trabajo para dibujado gfx
           //[5]: Valor id (si != -1 entonces gfx asociado a combi. Automaticamente se calculan 5,6,7,8,9)
           //[6][7]: x,y
           //[8][9]: w,h
           //[10]: color
           //[11]: valor libre

//	**************************************** Variables Sistema VISualización (VIS)
           private short VISAreaScrollLimXizq, VISAreaScrollLimXder; //coordenadas x,y iniciales del área de scroll dentro de NIV (cte para todos los Niveles)
           private short VISAreaScrollLimYarr, VISAreaScrollLimYaba; //coordenadas x,y finales del área de scroll dentro de NIV (depediente de extensión del Nivel)
           private boolean VISbUpd; //Indica si es necesario refrescar el fondo gráfico

//	*************************************************** Variables BackGround (BGR)
           private short BGRnivTilX, BGRnivTilY; //Coordenadas en tiles del NIV en el que se inicia el BGR (-1..BGRnivTilXmax)
           private short BGRnivTilXmax, BGRnivTilYmax; //límite superior derecho de BGRnivTilX y BGRnivTilY;
           private byte BGRscroll; //indicador de la dirección del scroll: 1(der), -1(izq), 2(arr), -2(aba)

           public static DirectGraphics SCRdg;
           private Image BGRimg1, BGRimg2;
           private Graphics BGRg1, BGRg2;
           private DirectGraphics BGRdg1,BGRdg2;
           private boolean BGRprin;

           //#if CFGtamanyoGfx == 2
//#            private static final byte IMGdat[] ={
//#                 0, 0, 8,8, //num 0
//#                 8, 0, 8,8, //num 1
//#                16, 0, 8,8, //num 2
//#                24, 0, 8,8, //num 3
//#                32, 0, 8,8, //num 4
//#                40, 0, 8,8, //num 5
//#                48, 0, 8,8, //num 6
//#                56, 0, 8,8, //num 7
//#                64, 0, 8,8, //num 8
//#                72, 0, 8,8, //num 9
//#                80, 0, 8,8, //%
//#                88, 0, 8,8, //-
//# 
//#                 0, 8,12,12, // iconos modo cursor on y off(#12)
//#                12, 8,12,12,
//#                24, 8,12,12,
//#                36, 8,12,12,
//#                48, 8,12,12,
//#                60, 8,12,12,
//#                72, 8,12,12,
//#                84, 8,12,12,
//# 
//#                 0,20, 8,7, // tiempo (#20)
//#                 8,20, 8,7, //nivel
//#                16,20, 8,7, //progreso
//#                24,20, 7,7, //combi
//#                31,20, 7,7, //pieza
//#                38,20, 6,7, // < apagado
//#                44,20, 6,7, // > apagado
//#                50,20, 6,7, // < encendido
//#                56,20, 6,7, // > encendido
//#                62,20, 8,7, // ^ encendido
//#                70,20, 8,7, // V encendido
//# 
//#                0,27, 8,8, // marco sup-izq  (#31)
//#                8,27, 8,8, // marco sup-izq
//#               16,27, 8,8, // marco sup-izq
//#               24,27, 8,8, // marco sup-izq
//#               32,27, 2,8, // marco izq
//#               34,27, 2,8, // marco der
//#                2,27, 8,2, // marco sup
//#               22,33, 8,2, // marco inf
//# 
//#               36,27, 4,4, // marco pieza on sup-izq (#39)
//#               40,27, 4,4, // marco pieza on sup-der
//#               36,31, 4,4, // marco pieza on inf-izq
//#               40,31, 4,4, // marco pieza on inf-izq
//#               44,27, 4,4, // marco pieza off sup-izq
//#               48,27, 4,4, // marco pieza off sup-der
//#               44,31, 4,4, // marco pieza off inf-izq
//#               48,31, 4,4, // marco pieza off inf-izq
//# 
//#               52,28,5,5, //pieza roja  (#47)
//#               57,28,5,5, //pieza verde
//#               62,28,5,5, //pieza azul
//#               67,28,5,5, //pieza amarilla
//# 
//#               0,35, 9,12, // punt 0  (#51)
//#               9,35, 6,12, // punt 1
//#              15,35, 9,12, // punt 2
//#              24,35, 9,12, // punt 3
//#              33,35, 9,12, // punt 4
//#              42,35, 9,12, // punt 5
//#              51,35, 9,12, // punt 6
//#              60,35, 9,12, // punt 7
//#              69,35, 9,12, // punt 8
//#              78,35, 9,12, // punt 9
//#              87,35, 9,12, // punt +
//# 
//#              0,47, 12,12, //Ind pieza (#62)
//#              12,47,12,12,
//#              24,47,12,12, //Ind flecha (#64)
//#              36,47,12,12,
//#              48,47,12,12, //Ind cruz (#66)
//#              60,47,12,12,
//# 
//#              72,47, 6,12, // punt 1 azul  (#68)
//#              78,47, 9,12, // punt 2 azul
//#              87,47, 9,12, // punt 3 azul
//# 
//# 
//#              84,20,12,12, //combi menu(#71)
//#               9,59,9,9, //combi mono
//#               0,59,9,9, //combi multi
//#              18,59,9,9, //rojo
//#              27,59,9,9, //verde
//#              36,59,9,9, //azul
//#              45,59,9,9, //amarillo
//#              54,59,7,7, //icono combi nivel
//#              61,59,7,7, //icono combi compuesto
//#              68,59,8,7, //^ apagado
//#              76,59,8,7,  //V apagado
//# 
//#              84,59,6,8, //Pieza - (#82)
//#              90,59,6,8, //Pieza V
//#               0,68,6,8, //Pieza Z
//#               6,68,6,8, //Pieza L
//#              12,68,6,8, //Pieza T
//#              18,68,6,8,  //Pieza I
//# 
//#              72,27,8,7,  //icono num piezas  (#88)
//#              79,27,4,8,   //signo :
//#              54,59,4,4    //Signo punto
//# 
//#            };
            //#else
               private static final byte IMGdat[] ={
                0, 0, 5,7, //num 0
                5, 0, 5,7, //num 1
               10, 0, 5,7, //num 2
               15, 0, 5,7, //num 3
               20, 0, 5,7, //num 4
               25, 0, 5,7, //num 5
               30, 0, 5,7, //num 6
               35, 0, 5,7, //num 7
               40, 0, 5,7, //num 8
               45, 0, 5,7, //num 9
               50, 0, 5,7, //%
               55, 0, 5,7, //-

                0, 7,8,8, // iconos modo cursor on y off(#12)
                8, 7,8,8,
               16, 7,8,8,
               24, 7,8,8,
               32, 7,8,8,
               40, 7,8,8,
               48, 7,8,8,
               56, 7,8,8,

                0,15, 5,6, // tiempo (#20)
                5,15, 5,6, //nivel
               10,15, 5,6, //progreso
               15,15, 6,6, //combi
               21,15, 5,6, //pieza
               26,16, 5,5, // < apagado
               31,16, 5,5, // > apagado
               36,16, 5,5, // < encendido
               41,16, 5,5, // > encendido
               46,15, 5,6, // ^ encendido
               51,16, 5,6, // V encendido

               0, 0, 0,0, // marco sup-izq  (#31)
               0, 0, 0,0, // marco sup-izq
               0, 0, 0,0, // marco sup-izq
               0, 0, 0,0, // marco sup-izq
               0, 0, 0,0, // marco izq
               0, 0, 0,0, // marco der
               0, 0, 0,0, // marco sup
               0, 0, 0,0, // marco inf

               0, 0, 0,0, // marco pieza on sup-izq (#39)
               0, 0, 0,0, // marco pieza on sup-der
               0, 0, 0,0, // marco pieza on inf-izq
               0, 0, 0,0, // marco pieza on inf-izq
               0, 0, 0,0, // marco pieza off sup-izq
               0, 0, 0,0, // marco pieza off sup-der
               0, 0, 0,0, // marco pieza off inf-izq
               0, 0, 0,0, // marco pieza off inf-izq

              15,45,4,4, //pieza roja  (#47)
              19,45,4,4, //pieza verde
              23,45,4,4, //pieza azul
              27,45,4,4, //pieza amarilla

              0,23, 6,8, // punt 0  (#51)
              6,23, 4,8, // punt 1
             10,23, 6,8, // punt 2
             16,23, 6,8, // punt 3
             22,23, 6,8, // punt 4
             28,23, 6,8, // punt 5
             34,23, 6,8, // punt 6
             40,23, 6,8, // punt 7
             46,23, 6,8, // punt 8
             52,23, 6,8, // punt 9
             58,23, 6,8, // punt +

              0,31,  8,8, //Ind pieza (#62)
              8,31, 8,8,
             16,31, 8,8, //Ind flecha (#64)
             24,31, 8,8,
             32,31, 8,8, //Ind cruz (#66)
             40,31, 8,8,

             48,31, 4,8, // punt 1 azul  (#68)
             52,31, 6,8, // punt 2 azul
             58,31, 6,8, // punt 3 azul


             56,15,8,8, //combi menu(#71)
              6,39,6,6, //combi mono
              0,39,6,6, //combi multi
             12,39,6,6, //rojo
             18,39,6,6, //verde
             24,39,6,6, //azul
             30,39,6,6, //amarillo
             36,39,5,5, //icono combi nivel
             41,39,5,5, //icono combi compuesto
             46,39,5,6, //^ apagado
             51,39,5,6,  //V apagado

             56,39,4,5, //Pieza - (#82)
             60,39,4,5, //Pieza V
              0,45,4,5, //Pieza Z
              4,45,4,5, //Pieza L
              8,45,4,5, //Pieza T
             12,45,4,5,  //Pieza I

             32,45,5,5,  //icono num piezas  (#88)
             37,44,3,6,   //signo :
             36,39,3,3    //Signo punto

           };
           //#endif

           //*************************************************** Tipos de Pieza (TIPPIE)
           public static final byte NUMTIPPIE = 8;
           public static final byte NUMCOLORES = 4;
           private int COLval[] = {0xFFCCCCCC, 0xFFFF3300, 0xFF66CC00, 0xFF3366FF, 0xFFFFFF00};
           //Bounding box de cada de tipo de pieza [(x,y) de cada orientación]
           private static final byte[][]TIPPIEbb =
           {
                           {1,2, 2,1, 1,2, 2,1}, //Tipo 1
                           {2,2, 2,2, 2,2, 2,2}, //Tipo 2
                           {3,2, 2,3, 3,2, 2,3}, //Tipo 3
                           {3,2, 2,3, 3,2, 2,3}, //Tipo 4
                           {2,3, 3,2, 2,3, 3,2}, //Tipo 5
                           {2,3, 3,2, 2,3, 3,2}, //Tipo 6
                           {3,2, 2,3, 3,2, 2,3},  //Tipo 7
                           {1,3, 3,1, 1,3, 3,1}  //Tipo 8
           };
           //Definición de las partes de cada tipo de pieza en cada una de las 4 orientaciones: N,E,S,O
           private static final byte[] TIPPIEval =
                   {
                   //Tipo 1
                          1, 0, 0, 2, 0, 0, 0, 0, 0,
                          3, 4, 0, 0, 0, 0, 0, 0, 0,
                          1, 0, 0, 2, 0, 0, 0, 0, 0,
                          3, 4, 0, 0, 0, 0, 0, 0, 0,
                   //Tipo 2
                          1, 0, 0, 7, 4, 0, 0, 0, 0,
                          5, 4, 0, 2, 0, 0, 0, 0, 0,
                          3, 6, 0, 0, 2, 0, 0, 0, 0,
                          0, 1, 0, 3, 8, 0, 0, 0, 0,
                   // Tipo 3
                          3, 6, 0, 0, 7, 4, 0, 0, 0,
                          0, 1, 0, 5, 8, 0, 2, 0, 0,
                          3, 6, 0, 0, 7, 4, 0, 0, 0,
                          0, 1, 0, 5, 8, 0, 2, 0, 0,
                   // Tipo 4
                          0, 5, 4, 3, 8, 0, 0, 0, 0,
                          1, 0, 0, 7, 6, 0, 0, 2, 0,
                          0, 5, 4, 3, 8, 0, 0, 0, 0,
                          1, 0, 0, 7, 6, 0, 0, 2, 0,
                   // Tipo 5
                          1, 0, 0, 9, 0, 0, 7, 4, 0,
                          5,10, 4, 2, 0, 0, 0, 0, 0,
                          3, 6, 0, 0, 9, 0, 0, 2, 0,
                          0, 0, 1, 3,10, 8, 0, 0, 0,
                   // Tipo 6
                          0, 1, 0, 0, 9, 0, 3, 8, 0,
                          1, 0, 0, 7,10, 4, 0, 0, 0,
                          5, 4, 0, 9, 0, 0, 2, 0, 0,
                          3,10, 6, 0, 0, 2, 0, 0, 0,
                   // Tipo 7
                          0, 1, 0, 3,12, 4, 0, 0, 0,
                          1, 0, 0,13, 4, 0, 2, 0, 0,
                          3,11, 4, 0, 2, 0, 0, 0, 0,
                          0, 1, 0, 3,14, 0, 0, 2, 0,
                    // Tipo 8
                    1, 0, 0, 9, 0, 0, 2, 0, 0,
                    3,10, 4, 0, 0, 0, 0, 0, 0,
                    1, 0, 0, 9, 0, 0, 2, 0, 0,
                    3,10, 4, 0, 0, 0, 0, 0, 0
                };

       private static final  byte[] TIPPIEcontval =
               {
//Tipo 1
       1, 0, 0, 2, 0, 0, 0, 0, 0,
       1, 2, 0, 0, 0, 0, 0, 0, 0,
       1, 0, 0, 2, 0, 0, 0, 0, 0,
       1, 2, 0, 0, 0, 0, 0, 0, 0,

//Tipo 2
       2, 0, 0, 3, 2, 0, 0, 0, 0,
       3, 2, 0, 2, 0, 0, 0, 0, 0,
       2, 3, 0, 0, 2, 0, 0, 0, 0,
       0, 2, 0, 2, 3, 0, 0, 0, 0,
// Tipo 3
       2, 7, 0, 0, 7, 2, 0, 0, 0,
       0, 2, 0, 6, 6, 0, 2, 0, 0,
       2, 7, 0, 0, 7, 2, 0, 0, 0,
       0, 2, 0, 6, 6, 0, 2, 0, 0,
// Tipo 4
       0, 7, 2, 2,7, 0, 0, 0, 0,
       2, 0, 0, 6, 6, 0, 0, 2, 0,
       0, 7, 2, 2, 7, 0, 0, 0, 0,
       2, 0, 0, 6, 6, 0, 0, 2, 0,
// Tipo 5
       2, 0, 0, 4, 0, 0, 3, 2, 0,
       3, 4, 2, 2, 0, 0, 0, 0, 0,
       2, 3, 0, 0, 4, 0, 0, 2, 0,
       0, 0, 2, 2, 4, 3, 0, 0, 0,
// Tipo 6
       0, 2, 0, 0, 4, 0, 2, 3, 0,
       2, 0, 0, 3, 4, 2, 0, 0, 0,
       3, 2, 0, 4, 0, 0, 2, 0, 0,
       2, 4, 3, 0, 0, 2, 0, 0, 0,
// Tipo 7
       0, 2, 0, 2, 5, 2, 0, 0, 0,
       2, 0, 0, 5, 2, 0, 2, 0, 0,
       2, 5, 2, 0, 2, 0, 0, 0, 0,
       0, 2, 0, 2, 5, 0, 0, 2, 0,
// Tipo 8
       1, 0, 0, 4, 0, 0, 2, 0, 0,
       1, 4, 2, 0, 0, 0, 0, 0, 0,
       1, 0, 0, 4, 0, 0, 2, 0, 0,
       1, 4, 2, 0, 0, 0, 0, 0, 0,
              };

              private int[] TIPPIEcontman = {
                  //Tipo 1
                   0, 0, 0, 0x4000, 0, 0, 0, 0, 0,
                   90, 270, 0, 0, 0, 0, 0, 0, 0,
                   0, 0, 0, 0x4000, 0, 0, 0, 0, 0,
                   90, 270, 0, 0, 0, 0, 0, 0, 0,
                   //Tipo 2
                   0, 0, 0, 90, 270, 0, 0, 0, 0,
                   0, 270, 0, 180, 0, 0, 0, 0, 0,
                   90, 0x2000, 0, 0, 180, 0, 0, 0, 0,
                   0, 0, 0, 90, 180, 0, 0, 0, 0,
                   // Tipo 3
                   90, 0x2000, 0, 0, 0x4000, 270, 0, 0, 0,
                   0, 0, 0, 0, 0x6000, 0, 180, 0, 0,
                   90, 0x2000, 0, 0, 0x4000, 270, 0, 0, 0,
                   0, 0, 0, 0, 0x6000, 0, 180, 0, 0,
                   // Tipo 4
                   0, 0, 270, 90, 0x6000, 0, 0, 0, 0,
                   0, 0, 0, 0x4000, 8192, 0, 0, 180, 0,
                   0, 0, 270, 90, 0x6000, 0, 0, 0, 0,
                   0, 0, 0, 0x4000, 8192, 0, 0, 180, 0,
                   // Tipo 5
                   0, 0, 0, 0, 0, 0, 90, 270, 0,
                   0, 270, 270, 180, 0, 0, 0, 0, 0,
                   90, 0x2000, 0, 0, 180, 0, 0, 180, 0,
                   0, 0, 0, 90, 90, 180, 0, 0, 0,
                   // Tipo 6
                   0, 0, 0, 0, 0, 0, 90, 180, 0,
                   0, 0, 0, 90, 270, 270, 0, 0, 0,
                   0, 270, 0, 180, 0, 0, 180, 0, 0,
                   90, 90, 0x2000, 0, 0, 180, 0, 0, 0,
                   // Tipo 7
                   0, 0, 0, 90, 180, 270, 0, 0, 0,
                   0, 0, 0, 90, 270, 0, 180, 0, 0,
                   90, 0, 270, 0, 180, 0, 0, 0, 0,
                   0, 0, 0, 90, 270, 0, 0, 180, 0,
                   // Tipo 8
                   0, 0, 0, 0x4000, 0, 0, 0x4000, 0, 0,
                   90, 270, 270, 0, 0, 0, 0, 0, 0,
                   0, 0, 0, 0x4000, 0, 0, 0x4000, 0, 0,
                   90, 270, 270, 0, 0, 0, 0, 0, 0

               };
        private static byte TIPPIErefval[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};



// *************************************************** Variables del Nivel (NIV)

       private short NIVnumTilW, NIVnumTilH; //Número de Tiles (posiciones del tablero) del nivel
       static short NIVidPieza[]; // Valores: -2,-1,..999 (-2: borde, -1: sin pieza, otro: id de pieza)
       static byte NIVnumPart[];  //= new byte[contPosX , contPosY];número de partes de las piezas (0..14)

//	 *************************************************** Variables de Piezas (PIE)
         //Constantes Estado de Pieza temporal
           public static final byte INACTIVA = 0;
           public static final byte ACTIVA = 1;
         //Constantes Situación de Pieza temporal
           public static final byte PIE_EN_CUR = 0;
           public static final byte PIE_EN_TAB = 1;
           public static final byte PIE_EN_MOV = 2;
           public static final byte COM_EN_TAB = 3;
           public static final byte COM_EN_MOV = 4;
         //Constantes estados de FSM PIETMP
           public static final byte STSPIE_EN_CURTAB = 0;
           public static final byte STSPIE_EN_MOV = 1;
           public static final byte STSPIE_EN_HUD = 2;

           private byte PIETMPsts,PIETMPsbSts; //Estado y subestado para la FSM PIETMP
           private short PIElastId; //Id de la última pieza colocada en el área de juego (-1..999)
           private byte PIEdat[][];  //0..PIElastId-1;
           //[0]: Estado (0: libre, 1: ocupado)
           //[1]: Tipo (1..NUMTIPPIE)
           //[2]: Orientación (0:Norte, 1:Este, 2:Sur, 3:Oeste)
           //[3]: Color (1..NUMCOLORES)
           //[4]: Id del combi (-1: si no forma parte de combi, 0..99: id del combi SIMPLE del que forma parte)

           private short PIEtmp[] = new short[10];
           //[0] Estado: 0 (inactiva) / 1 (activa)
           //[1] Situación:
           //    0: (pieza asociada [guiada] por el cursor)
           //    1: (pieza fija colocada en tablero)
           //    2: (pieza en movimiento, desplazándose)
           //    3: (combi fijo colocado en tablero)
           //    4: (combi en movimiento, desplazándose)
           //[2][3]: PosX, PosY
           //[4][5][6]: Tipo, Orientación, Color
           //[7] Dir: ARR / ABA / DER / IZQ / STOP
           //[9]: idPieza, o idCombi (en función de situación [1]: para PIE_EN_TAB o COM_EN_TAB)

           private byte COMlastId; //Id del último combi generado (-1..99)
           private byte COMdat[][];
           //[0]: Estado (0: libre, 1: ocupado)
           //[1]: Nivel (1: simple, N: compuesto,contiene jerarquía de N-1 combis)
           //[2]: Padre (id del combi padre si contenido en combi compuesto, 0 si no contenido en ningún combi)
           //[3][4]: PosX, PosY
           //[5][6]: Ancho, Alto
           //[7]: Tipo (0: cuadrado: 1 rectángulo)
           //[8]: Color (multicolor, o id del color si monocolor)
           private short COMtmp[] = new short[8];
           //[0]: Id del combi generado
           //[1]: Nivel (1: simple, N: compuesto,contiene jerarquía de N-1 combis)
           //[2][3]: PosX, PosY
           //[4][5]: Ancho, Alto
           //[6]: Color
           //[7]: multicolor debido a contener combi multicolor?? (0: no, 1: sí)

           //Variables para optimización detección de combis
           //boolean COMbOptAreaConex, COMbOptSupConex, COMbOptSupConexHorz, COMbOptSupConexVert;
           short COMsuplimXini, COMsuplimXfin, COMsuplimYini, COMsuplimYfin;

//	 *************************************************** Variables del Scroll (ACT)
           private short ACTposX, ACTposY; // Coordenadas en tiles en el mapa del nivel (de [0,0] a NUMTILESSCRX-1)
           private short ACTacuIncX, ACTacuIncY; //incremento acumulado a aplicar en el Tick actual

           private short ACTincX, ACTincY; //incrementos automáticos fijados para un tick
           private short ACTdstX, ACTdstY; //Posiciones destino para desplazamiento automático
           //private short ACTsrcX, ACTsrcY; //Posiciones origen para desplazamiento automático

           private byte actorAccion, last_actorAccion, prev_actorAccion; // 0..4 (0: stop, 1:right, 2:left, 3:up, 4:down
           private byte disc_actorAccion;

//	 *************************************************** Variables del Cursor (CUR)
           //Constantes modos de Cursorl
           public static final byte LANZAR = 0;
           public static final byte COLOCAR = 1;
           public static final byte ELIMINAR = 2;
           public static final byte DESPLAZAR = 3;
           public static final byte VISUALIZAR = 4;

           private byte CURmodo; //Modo del cursor. Valores posibles
           //0: Lanzar (Modo propulsor con pieza encima del mismo)
           //1: Colocar (Modo flecha con pieza flotante)
           //2: Eliminar (Modo flecha X, con highlight de pieza a la que apunte)
           //3: Desplazar (Modo flecha con área rectangular de selección e indicación de la dirección de desplazamiento)
           //4: Visualizar
           private short CURposX, CURposY; //posición sup-izq x,y en el grid (de 0 a N-1)
           private boolean CURbHayPieza;    //True si pieza manejada por cursor (en función del modo)
           private boolean CURbHayCombi;    //True si selección del cursor es un Combi
           //Modo Lanzar: True si pieza pegada a propulsor (todavía sin lanzar)
           //Modo Colocar: Siempre True (una vez pieza colocada el modo cambia a Lanzar)
           //Modo Eliminar: True si flecha apunta a pieza
           //Modo Desplazar: True si flecha apunta a pieza o a Combi
           //Modo Libre: Siempre True (aunque no afecta)
           private Image CURpix[] = new Image[16];    // Tiles graficos en formato Image del propulsor
           private byte CURcont[] = new byte[5]; //Contador de opciones de uso de cada modo de Cursor
           //[0]: Modo Lanzar (-1: ilimitado)
           //[1]: Modo Colocar (-1: ilimitado)
           //[2]: Modo Eliminar (-1: ilimitado)
           //[3]: Modo Desplazar Pie (-1: ilimitado)
           //[4]: Modo Desplazar Com (-1: ilimitado)
           private byte CURcontBon; //Contador (vector de flags binarios) de bonus de opciones de uso de cada modo Cursor
           //bit 0: Modo Colocar
           //bit 1: Modo Eliminar
           //bit 2: Modo Desplazar Pie
           //bit 3: Modo Desplazar Com
           //bit 4: Modo Lanzar

//     *************************************************** Variables del Propulsor (PRO)

    private byte PROmodo; //Modo del propulsor. Valores: HORZ / VERT
    private byte PROori;    //Orientación (a donde mira el propulsor). Valores: ARR / ABA / IZQ / DER
    private short PROposX, PROposY;    //Posición sup-izq del propulsor (en la que se empieza a dibujar).Valor: de 0 a numtiles-1
    private short PROidbor;  //Borde en el que está situado el propulsor.Valor: Índice sobre NIVbor (0..NIVnumBor-1)
    private short PROlen; //Ancho/alto del propulsor en función del modo HORZ/VERT

   //52, 53, 54
    private byte PROval[] = {  //Definición de tiles gráficos del propulsor
        0,-1,0,-1,-1,0,2,0,-1,-1, //HORZ - ARR - ancho1
        0,-1,-1,0,-1,0,2,2,0,-1, //HORZ - ARR - ancho2
        0,-1,-1,-1,0,0,2,2,2,0, //HORZ - ARR - ancho3
        0,2,0,-1,-1,0,-1,0,-1,-1, //HORZ - ABA - ancho1
        0,2,2,0,-1,0,-1,-1,0,-1, //HORZ - ABA - ancho2
        0,2,2,2,0,0,-1,-1,-1,0, //HORZ - ABA - ancho3

        1,1,2,-1,1,1,-1,-1,-1,-1, //VERT - DER - alto1
        1,1,2,-1,2,-1,1,1,-1,-1, //VERT - DER - alto2
        1,1,2,-1,2,-1,2,-1,1,1, //VERT - DER - alto3
        1,1,-1,2,1,1,-1,-1,-1,-1, //VERT - IZQ - alto1
        1,1,-1,2,-1,2,1,1,-1,-1, //VERT - IZQ - alto2
        1,1,-1,2,-1,2,-1,2,1,1  //VERT - IZQ - alto3
    };

    private int PROman[] = {
        0, 0,0x2000,     0,     0,0x4000, 0,0x6000,     0,     0, //HORZ - ARR - ancho1
        0, 0,     0,0x2000,     0,0x4000, 0,     0,0x6000,     0, //HORZ - ARR - ancho2
        0, 0,     0,     0,0x2000,0x4000, 0,     0,     0,0x6000, //HORZ - ARR - ancho3
        0, 0,0x2000,     0,     0,0x4000, 0,0x6000,     0,     0, //HORZ - ABA - ancho1
        0, 0,     0,0x2000,     0,0x4000, 0,     0,0x6000,     0, //HORZ - ABA - ancho2
        0, 0,     0,     0,0x2000,0x4000, 0,     0,     0,0x6000, //HORZ - ABA - ancho3
        0,0x2000, 90, 0,0x4000,0x6000,     0,     0,     0,     0, //VERT - DER - alto1
        0,0x2000, 90, 0,    90,     0,0x4000,0x6000,     0,     0, //VERT - DER - alto2
        0,0x2000, 90, 0,    90,     0,    90,     0,0x4000,0x6000, //VERT - DER - alto3
        0,0x2000, 0 ,90,0x4000,0x6000,     0,     0,     0,     0, //VERT - DER - alto1
        0,0x2000, 0 ,90,     0,    90,0x4000,0x6000,     0,     0, //VERT - DER - alto2
        0,0x2000, 0 ,90,     0,    90,     0,    90,0x4000,0x6000 //VERT - DER - alto3
    };


//s40 : SND version .ott (sin MMAPI)
//    private static int SNDlen[] = {132};
//#if CFGtipoSnd == 1
//#     private static int SNDlen[] = {81,41};
//#     private static Sound SNDdat[];
//#endif

//*****************************************************************************
//************************************************************************* GAM
//*****************************************************************************

   private void GAM_CargarGraficos(){
       Image img=null;
       Graphics g;
       DirectGraphics dg;

//       System.out.println("GAM_CargarGraficos");

       try {
         if (PIXnum == 5)
           img = Image.createImage("/PIE.png");
         else if (PIXnum == 4)
           img = Image.createImage("/CUR.png");
         else if (PIXnum == 3)
           img = Image.createImage("/SPR.png");
         else if (PIXnum == 2){        
               imgHud = Image.createImage("/HUD.png");
               //#if CFGfnt != 0
//#                imgFnt = Image.createImage("/fnt.png");                
               //#endif
         }
         //#if CFGtamanyoGfx == 2
//#          else if (PIXnum == 1)
//#            imgCab = Image.createImage("/cab.png");
         //#endif
       }
       catch (IOException e) {
//         System.out.println("EX - GAM_CargarGraficos");
         e.printStackTrace();
       }

//--------------- Carga de Graficos SIN transparencia
//	  Carga de Graficos PIEZAS
       if(PIXnum==5){
         GAMpiePix = new short[14 * 4 * (NUMPIXELS_TILE_XY)];
         g = DirectUtils.createImage(NUMPIXELS_TILEX, NUMPIXELS_TILEY,
                                     0xFFFFFFFF).getGraphics();
         dg = DirectUtils.getDirectGraphics(g);
         short[] pixels_aux0 = new short[NUMPIXELS_TILE_XY];
         for (int j = 0; j < 4; j++)
           for (int i = 0; i < 14; i++) {
             //GAMpiePix[j*14+i] = Image.createImage(12,12);
             //GAMpiePix[j*14+i].getGraphics().drawImage(imgPie, -12 * i, -12 * j, Graphics.TOP | Graphics.LEFT);
             g.drawImage(img, -NUMPIXELS_TILEX * i, -NUMPIXELS_TILEY * j,
                         Graphics.TOP | Graphics.LEFT);
             dg.getPixels(pixels_aux0, 0, NUMPIXELS_TILEX, 0, 0,
                          NUMPIXELS_TILEX, NUMPIXELS_TILEY, DEVntvFmt);
             System.arraycopy(pixels_aux0, 0, GAMpiePix,
                              (j * 14 + i) * NUMPIXELS_TILE_XY,
                              NUMPIXELS_TILE_XY);
           }
         pixels_aux0 = null;
       }
       else
//--------------------- Carga de Graficos CON transparencia
// Carga de Graficos Cursores (matriz de 7x7 tiles)
       if(PIXnum==4){
         for (int j = 0; j < 2; j++) {
           for (int i = 0; i < 8; i++) {
             //#if CFGtamanyoGfx == 2   
//#              CURpix[j * 8 + i] = DirectUtils.createImage(NUMPIXELS_TILEX + 8, NUMPIXELS_TILEY + 8, 0x00000000);
             //#else
             CURpix[j * 8 + i] = DirectUtils.createImage(NUMPIXELS_TILEX + 6, NUMPIXELS_TILEY + 6, 0x00000000);
             //#endif
             dg = DirectUtils.getDirectGraphics(CURpix[j * 8 + i].getGraphics());
     
             //#if CFGtamanyoGfx == 2   
//#              dg.drawImage(img, - (NUMPIXELS_TILEY + 8) * i, - (NUMPIXELS_TILEY + 8) * j, Graphics.TOP | Graphics.LEFT, 0);
             //#else
             dg.drawImage(img, - (NUMPIXELS_TILEY + 6) * i, - (NUMPIXELS_TILEY + 6) * j, Graphics.TOP | Graphics.LEFT, 0);
            //#endif
           }
         }
       }
       else
       //Carga bordes combis y partes propulsor
       if(PIXnum==3){
         for (int i = 0; i < 7; ++i) {
           GAMspr[i] = DirectUtils.createImage(NUMPIXELS_TILEX, NUMPIXELS_TILEY, 0x00000000);
           dg = DirectUtils.getDirectGraphics(GAMspr[i].getGraphics());
           dg.drawImage(img, -NUMPIXELS_TILEX * i, 0, Graphics.TOP | Graphics.LEFT, 0);           
         }
       }

       img = null;
       g = null;
       dg = null;
       System.gc();

//     System.out.println("Fin GAM_CargarGraficos");
   }



//-------------------------------------------------------------------- GAM_Tick
// Desc: Gestión del FSM del juego
//-----------------------------------------------------------------------------
 private void GAM_Tick(){
   int i,val;
   ACTacuIncX = ACTacuIncY = 0;

   switch(GAMsts){
     case PLAY_IN_INIT:
       if(GAMsbSts==0){
         FX_Add( (short) 10, (short) -1, (short) 1); //System.out.println("FX =>> Transición de nivel");
         GAMsbSts++;
       }else
         if(FX_In(10)==-1){
           GAMsts = PLAY_IN_CURSOR;
         }
       break;

//Control del juego por el usuario
     case PLAY_IN_CURSOR:
       ACT_Teclado();
       break;

//Control autónomo del Propulsor para cambio a contorno opuesto
     case PLAY_IN_PROPCHGBOR:
       if(ACT_FinMov(false)){ //se ha llegado a posición final
         if(! PRO_CambiarFin())
           GAMsts = PLAY_IN_CURSOR;
       }
       else{
         ACTacuIncX = ACTincX;    //Incrementos fijos establecidos en PRO_CambiarIni();
         ACTacuIncY = ACTincY;
       }
       break;

//Control autónomo del Propulsor para lanzamiento automático de pieza tras mov a derechas.
     case PLAY_IN_PROPLAN:
       if(ACT_RetMov()) //si se ha retornado a la posición original de salida (se ha dado una vuelta completa en sentido CW al borde zona 0)
         PIETMPsbSts++; //No es posible lanzar pieza. Pasar a subestado colocar pieza
       else{
         if(PRO_LanzarPie()) //Se intenta lanzar, y si no hay colisión (es posible el lanzamiento)
           GAMsts = PLAY_IN_CURSOR;
         else{ //si hay colisión se avanza PROP en sentido CW
           ACTacuIncX = ACTincX;
           ACTacuIncY = ACTincY;
         }
       }
       break;

//Control de Detección/Creación/Eliminación de Combis
     case PLAY_IN_COMBI:
       switch(GAMsbSts){

         case 0: //Detección de Combi a partir de pieza (indicada en PIEtmp)
           GAMbHayComRulEli = false; //inicialmente todavia no hay combi que cumpla regla de eliminación
           if (COM_DetectarCombiPie(PIEtmp[2], PIEtmp[3], (byte) PIEtmp[4],(byte) PIEtmp[5]))
             GAMsbSts = 2;
           else
             GAMsts = PLAY_IN_CHKNIV;
           break;

         case 1: //Detección de Combi a partir de Combi (indicado en COMtmp)
           if (COM_DetectarCombiCom(COMtmp[0])){
             if(LVL.OBJbEliGen) //Si reglas ELI son iguales a reglas GEN => Priorida GEN sobre ELI
               GAMsbSts = 2;
             else{ //Preferencia ELI sobre GEN
               if (GAMbHayComRulEli)
                 GAMsbSts = 3;
               else
                 GAMsbSts = 2;
             }
           }else {
             //Tratar consecución de objetivo/s
             if(GAMnumComRulGen >0){
               GAM_IncObjSts(GAMnumComRulGen); //Incrementa contador de objetivos cumplidos
               GAMnumComRulGen = 0;
             }
             if (GAMbHayComRulEli)
               GAMsbSts = 5;
             else{
               GAMsts = PLAY_IN_CHKNIV;
               GAMsbSts = 0;
             }
           }
           break;

         case 2: //Chequear Reglas Objetivo que se cumplan por Generación de Combis
           if(COM_ChkRulObj())
             GAMaIdRul[GAMnumComRulGen++] = GAMidRul;
           GAMsbSts = 3;
           break;

         case 3: //Chequear Eliminación de Combi
           if (COM_ChkRulEli()) {
             GAMbHayComRulEli = true; //Pte asignar id elim al crear combi
             GAMsbSts = 4;
           }
           else{
             //Tratar consecución de objetivo
             if (GAMnumComRulGen > 0) {
               GAM_IncObjSts(GAMnumComRulGen); //Incrementa contador de objetivos cumplidos
               GAMnumComRulGen = 0;
               GAMbPuntComRulGen = true;
             }

             if (GAMbHayComRulEli)
               GAMsbSts = 5;
             else
               GAMsbSts = 4;
           }
           break;

         case 4: //Creación de Combi
           switch(FX_In(1)){ //si retorna 1(in progress) no hay que hacer nada
             case 0: //antes del FX
               COM_Crear();
               //Crear FX síncrono de generación de combi
               i = FX_Add((short)1, COMtmp[0], (short)0);
               COMdat[COMtmp[0]][0] = IN_FX; //indicar que esta en estado de proceso de FX. permite retrasar las acciones del juego que afectan a ese elemento => Dependencia directa con el proc./metodo de dicho elemento.
              //Incrementar puntuación y Crear FX asíncrono para puntos flotando
              if(LVL.idModo==1 || (LVL.idModo==2 && GAMbPuntComRulGen)){
                 i = FX_Add( (short) 3, COMtmp[0], COM_CalcPunGen(COMtmp[0]));
                 GAMscore += FXdat[i][11];
                 HUDbScore = true;
                 GAMbPuntComRulGen = false;
               }

               //Crear FX asíncrono para bonus (en caso de que existan bonus para el nivel y se hayan obtenido por la creación del combi)
               if((LVL.OBJincBonus>0) && (val=COM_CalcCurBonus(COMtmp[0]))>0){
                 CUR_IncCont((byte)val);
                 i = FX_Add( (short) 4, COMtmp[0], (short)val);
               }
               break;
             case -1: //despues del FX
               COMdat[COMtmp[0]][0] = OCUPADO;
               if(GAMbHayComRulEli)
                 GAMidComRulEli = (byte)COMtmp[0];
               VISbUpd=true; //Actualiza dibujado de Combi
               GAMbChkFinNiv=true; //Chequear fin nivel en el tick en curso
               GAMsbSts = 1;
           }
           break;

         case 5: //Eliminación de Combi
             switch(FX_In(2)){ //si retorna 1(in progress) no hay que hacer nada
               case 0: //antes del FX
                 i = FX_Add((short)2, GAMidComRulEli, (short)0);
                 COM_Eliminar(GAMidComRulEli);
                 break;
               case -1: //despues del FX
                 GAMbHayComRulEli = false;
                 //Comprueba si se ha eliminado alguna pieza por defecto, y si hay q volver a regenerarla
                 if(LVL.OBJbEliDefPie){
                   PIE_LoadDef();
                   COM_LoadDef();
                 }
                 GAMbChkFinNiv=true; //Chequear fin nivel en el tick en curso
                 GAMsts = PLAY_IN_CHKNIV;
                 GAMsbSts = 0;
             }
             break;
       }
       break;//Fin: PLAY_IN_COMBI


//Control Finalización nivel después de creación/eliminación de combi.
     case PLAY_IN_CHKNIV:
       if(GAMsbSts == 0){
         //Comprobar si se cumplen los objetivos de finalización del nivel
         if (GAMbChkFinNiv && GAM_ChkFinNiv()) {
           FX_Add( (short) 7, (short) - 1, (short) (LVL.idModo==0?59:41)); //System.out.println("FX =>> Nivel finalizado por cumplimiento de objetivos");
           TIMbGam = false; //Parar timer nivel (y que siga el fx con el reloj parado)
           //#if CFGtipoSnd == 2
           if(COMBIMidlet.M.MENbSnd)
             MUS.TocaMusica(4);
           //#endif
           GAMsbSts++;
         }
         else{
           GAMbChkFinNiv = false;
           GAMsts = PLAY_IN_CURSOR;
         }
       }else
          if (FX_In(7)==-1){
            GAMsts = PLAY_OUT_CHGNIV;
            GAMsbSts = 0;
          }
        break;

//Control pantallas modo pausa
    case PLAY_OUT_PAUSA:
         actorAccion = disc_actorAccion;
         disc_actorAccion = STOP;
         if(actorAccion == DER && (MENid==0 || MENid==1)){ //[PTE: Parche MENid0
           MENop = (byte) ((MENop+1) % MENpauCnf[(MENid>2?2:MENid)<<2]);
        }else if(actorAccion == IZQ && (MENid==0 || MENid==1)){//[PTE: Parche MENid0
           if (--MENop < 0)
             MENop = (byte)(MENpauCnf[(MENid>2?2:MENid)<<2]-1);
         }else if(actorAccion == ARR){
           if(MENid>0){
             MENid--;
             MENop=0;
           }
         }else if(actorAccion == ABA){
           if(MENid<(short)(LVL.TXTstr.length/10 +1+ (LVL.TXTstr.length%10>0?1:0))){ //2 //PTE: TXTstr.length/10
             MENid++;
             MENop=0;
           }
         }else if(actorAccion == SOFTLEFT){
           if(MENid==-1)//confirmar abandono de partida
             GAM_FinNiv();
           else
             MENid=-1;
         }
         else if(actorAccion == SOFTRIGHT){
           if(MENid==-1) //descartar abandono
            MENid=0;
           else{ //volver a partida
             GAMsts = PLAY_IN_CURSOR;
             HUDbIni = true;
             TIM_FinPausa(); //reinicia timer partida y pieza
           }
         }

        break;



      case PLAY_OUT_CHGNIV: //--------------------------------- AVANCE DE NIVEL
        //---------------------------------------------------- Subestado 0
        if(GAMsbSts==0){
          //Comprobar si hay que registrar sig nivel como el mayor nivel desbloqueado de la fase en curso
          if (LVL.idNiv < LVL.confFaseNivMod[LVL.idModo][LVL.idFase] - 1) { //Si no es la ultima fase del nivel
            byte idx = MEN_ObtIdxLvl(LVL.idModo, LVL.idFase);
            if(COMBIMidlet.M.MENnivOpen[idx] == LVL.idNiv){ //si el maximo nivel desbloqueado es el actual
              COMBIMidlet.M.MENnivOpen[idx]++; //desbloquear el siguiente nivel
              COMBIMidlet.M.CNF_Save(false);
            }
          }else{
            if(LVL.idModo==1 && LVL.idFase <1){ //Avance de fase en modo ARcade. Desbloquear nivel 1 de siguiente fase
              byte idx = MEN_ObtIdxLvl((byte)1, (byte)(LVL.idFase+1));
              COMBIMidlet.M.MENnivOpen[idx]++;
              COMBIMidlet.M.CNF_Save(false);
            }
          }
          //Comprobaciones records
          GAM_ChkRecord(true, true);

          //Activar flag dibujado piezas de dentro del combi
          for (i = 0; i <= COMlastId; i++)
            if (COMdat[i][0] == OCUPADO)
              COMdat[i][0] = IN_FX;
          VISbUpd = true;
          //Preparar modo visualización
          GAMbHUDon = false;
          CURbHayPieza = false;
          CURmodo = VISUALIZAR;
          if(!LVL.bTut){
            FX_Add( (short) 9, (short)-1,(short) ( (LVL.idModo == 1 ? 90 : 40) + (GAMbRecord ? 16 : 0))); //Apertura ventana: nuevo record
            GAMsbSts++;
          }else
            GAMsbSts=2;
        }
        //---------------------------------------------------- Subestado 1
        else if (GAMsbSts==1){
          if(FX_In(9)==-1)
            GAMsbSts++;
        }
        //---------------------------------------------------- Subestado 2
        else if (GAMsbSts == 2) {
          //Entra en modo visualizar y permite desplazarse por pantalla para ver el combi resultado
          actorAccion = last_actorAccion;
          if (actorAccion == DER || actorAccion == IZQ || actorAccion == ARR || actorAccion == ABA)
            CUR_Mover(actorAccion);

          actorAccion = disc_actorAccion;
          disc_actorAccion = STOP;
          if (actorAccion == SOFTLEFT) {
            byte sig = LVL.IncLvl();
            if (sig > 0) { //si hay siguiente nivel (no es fin de fase)
              if (sig == 2) //Si final de última fase de modo Arcade, incrementar nº de vueltas para comenzar desde 1er nivel
                GAMround++;
              FX_Add( (short) 10, (short) - 1, (short) 0); //System.out.println("FX =>> Transición de nivel");
              GAMsbSts=4;
            }
            else {
              FX_Add( (short) 7, (short) - 1, (short) 42); //System.out.println("FX =>> Fase finalizada ");
              GAMsbSts++;
            }
          }
        }
        //---------------------------------------------------- Subestado 3
        else if (GAMsbSts == 3) {
          if (FX_In(7) == -1)
            GAM_FinNiv();
        }
        //---------------------------------------------------- Subestado 4
        else if(GAMsbSts == 4){ //GAMsbSts = 4
          if (FX_In(10) == 1)
            GAMsbSts++;
        }
        else
          MEN_InitGame(false); //Cargar e iniciar siguiente nivel
        break;


    case PLAY_OUT_ENDGAM:  //---------------------- FINAL PARTIDA por derrota
        if(GAMsbSts == 0) {
          FX_Add( (short) 7, (short) - 1, (short) 14); //System.out.println("FX =>> Partida finalizada por derrota");
          TIMbGam = false; //Parar timer nivel (y que siga el fx con el reloj parado)
          GAM_ChkRecord(true, false);
          GAMsbSts++;
        }else if (GAMsbSts==1){
          if (FX_In(7)==-1){
            GAMbHUDon = false;
            CURbHayPieza = false;
            CURmodo = VISUALIZAR;
            GAMsbSts++;
          }
        }else{
          //Espera a pulsación tecla
          actorAccion = disc_actorAccion;
          disc_actorAccion = STOP;
          if (actorAccion == SOFTLEFT)
            GAM_FinNiv();
        }
   }

   //Actualiza variables de posición final después del tick
   ACTposX += ACTacuIncX;
   ACTposY += ACTacuIncY;
 }


//----------------------------------------------------------------- GAM_FinNiv
// Desc: Acciones de finalización a la salida del nivel.
private void GAM_FinNiv(){
   VGsts = VGSTS_MENU;
   MENid = MENop = 0;
   MENmsg = -1;
   //Iniciar música menú si está activada en opciones
   //#if CFGtamanyoGfx == 2
//#    if(COMBIMidlet.M.MENbMus)
//#      MUS.TocaMusica(-1);
   //#endif
}


//-------------------------------------------------------------- GAM_ChkRecord
// Desc: Comprueba si existe record de puntos/tiempo en función del modo juego
// Update: Actualiza booleano GAMbRecord si existe nuevo record, y lo registra
//         en RecordStore
//-----------------------------------------------------------------------------
private void GAM_ChkRecord(boolean pPun, boolean pTim){
   //Comprobraciones de nuevo record (puntos o tiempo)
   GAMbRecord=false;
   if(pPun && LVL.idModo == 1){
     if(GAMscore > COMBIMidlet.M.MENscoreRecord){
       COMBIMidlet.M.MENscoreRecord = GAMscore;
       COMBIMidlet.M.CNF_Save(false);
       GAMbRecord=true;
     }
   }
   else if(pTim && LVL.idModo == 2){
     short idx = LVL.idNiv;
     for(byte j=0; j<LVL.idFase; j++)
       idx += LVL.confFaseNivMod[2][j];
     if(HUDtimgamAct < COMBIMidlet.M.MENtimRecord[idx] || COMBIMidlet.M.MENtimRecord[idx]==0){
       COMBIMidlet.M.MENtimRecord[idx] = HUDtimgamAct;
       COMBIMidlet.M.CNF_Save(false);
       GAMbRecord=true;
     }
   }
}





//--------------------------------------------------------------- GAM_IncObjSts
// Desc: Incrementa contador de cumplimiento de objetivo
//-----------------------------------------------------------------------------
 private void GAM_IncObjSts(byte pnumIdRul){
   short pidRul, numobjs=0;

   //Chequea que objetivos de los conseguidos no se habian cumplido ya en su totalidad
   for(byte z=0; z<pnumIdRul; z++){
     pidRul = GAMaIdRul[z];
     if (GAMobjSts[pidRul * 2 + 1] < GAMobjSts[pidRul * 2]) {
       GAMobjSts[pidRul * 2 + 1]++;
       numobjs++;
     }
   }

   //Crea FX de avance de progreso de los nuevos objetivos cumplidos
   if(numobjs > 0){
     int inc =  (100 / GAMnumTotObj)*numobjs;
     if (GAMobjProg + inc >= 95 && inc != 100)
       inc = 100 - GAMobjProg;

     //Crea gfx con mensaje de cumplimiento de objetivo y nuevo %
     int i;
     if ( (i = FX_Add( (short) 6, (short) - 1, (short)40)) != -1) {
       FXdat[i][8] = numobjs;
       FXdat[i][9] = GAMobjProg; // % actual avance progreso
       FXdat[i][10] = (short)inc; //% a incrementar
     }
     //Crea Snd Fx
     //if(COMBIMidlet.M.MENbMus)
     //  MUS.TocaMusica(4);
     //Actualiza contador progreso objetivos del nivel
     GAMobjProg += inc;
     HUDbProg = true;
   }


 }



//--------------------------------------------------------------- GAM_ChkFinGam
// Desc: Comprueba que el nivel actual NO ha sido superado, por quedarse sin
//       espacio el tablero para continuar el juego.
//       (Se asume que existe pieza temporal recien cogida por cursor)
//-----------------------------------------------------------------------------
private boolean GAM_ChkFinGam(){
    //Comprobar si quedan mas opciones de eliminacion de piezas
    if(CURcont[2] != 0) //valor -1(ilimitado) o >0
        return false;

    //Comprobar si queda espacio para colocar la pieza actual (si no hay colision al colocar una pieza en alguna parte del tablero)
    for(short i=0;i<NIVnumTilW; i++)
        for(short j=0;j<NIVnumTilH; j++){
          if (!NIV_ChkColPie(i, j, (byte) PIEtmp[4], (byte) PIEtmp[5]))
            return false;
        }
   return true;
}


//-------------------------------------------------------------- GAM_ChkFinNiv
// Desc: Comprueba que el nivel actual ha sido superado por cumplir las reglas
//       de finalización específicas del nivel en curso
//-----------------------------------------------------------------------------
private boolean GAM_ChkFinNiv(){

   //Comprobar si estamos en un nivel de objetivo de OCUPACIÓN
   if (LVL.OBJrul[0][0] == TIPOBJ_OCUCOM){ //(basta chequear el 1er objetivo pues todos seran del mismo tipo)
     if (LVL.OBJrul[0][3] == 1)
       HUDbProg = GAM_ChkOcuNumCombis();
     else //==-1
       HUDbProg = GAM_ChkOcuSupCombis();
      if(HUDbProg)
        GAMobjProg = 100;
      return HUDbProg;
   }
   else{ //Objetivos de tipo GENERACION o resto
     //Comprueba si todos los objetivos se han cumplido
     boolean bHayObjs=false;
     for (int i = 0; i < GAMnumRulObj; i++)
       if(GAMobjSts[i * 2] != 0){ //no chequeamos los objetivos informativos
         bHayObjs = true;
         if (GAMobjSts[i * 2] != GAMobjSts[i * 2 + 1]) //Si valor conseguido[2] no ha llegado al valor objetivo[1]
           return false;
       }
     if(bHayObjs){
       GAMobjProg = 100;
       HUDbProg = true;
       return true;
     }else
       return false;
   }
}

//------------------------------------------------------------ GAM_ChkOcuSupCombis
// Desc: Calcula el % de ocupación de la superficie objetivo de ocupación
//       para cada uno de los objetivos.
// Return: TRUE si la superficie de ocupación está rellena cumpliendo todos los
//         objetivos de ocupación.
//-----------------------------------------------------------------------------
private boolean GAM_ChkOcuSupCombis(){
   //1.- Comprobar que el area está completa (que no hay huecos) y todas las piezas
   // pertenecen a un combi, y están totalmente incluidas en el área.
   if(COM_ExisteCombiEnSup((short)LVL.OBJrul[0][4],
                           (short)LVL.OBJrul[0][5],
                           (short)(LVL.OBJrul[0][4]+LVL.OBJrul[0][6]-1),
                           (short)(LVL.OBJrul[0][5]+LVL.OBJrul[0][7]-1)))
      //2.- Comprobar que todas las piezas del área estan incluidas en combi
      if(! COMsup_ExistePie(LVL.OBJrul[0][4],LVL.OBJrul[0][5],LVL.OBJrul[0][6],LVL.OBJrul[0][7]))
        return true;
    //3.-Que no hay medios combis (poco probable: se descarta incluir esta comprobación)
    //4.-[PTE: Nivel que lo use] que el combi cumple las condiciones de la regla objetivo.

   return false;
}


//--------------------------------------------------------- GAM_ChkOcuNumCombis
// Desc: Calcula el Número de combis de cada color (según las reglas de
//       ocupación) que ocupan el área de juego.
//-----------------------------------------------------------------------------
private boolean GAM_ChkOcuNumCombis(){

//[PTE]: Informar del avance/retroceso en el cumplimiento del objetivo (% ocupación total)
//: Recordar progreso actual
//: Calcular nuevo progreso
//: FX Msg informando del avance/retroceso experimentado (en caso de que éste haya variado).

   //Versión implementada:  Las reglas de Eliminación no son contrapuestas a las de Ocupación
   //=> Necesario evaluar cada combi para ver si cumple las reglas de ocupación
   // Objetivo: Contabilizar sólo los combis que cumplen las reglas
   for(int i=0; i<GAMnumRulObj; i++)
     GAMobjSts[i * 2 + 1] = 0; //Reseteo del progreso actual

   for(int i=0; i<100; i++)
     if (COMdat[i][0] == OCUPADO && COMdat[i][2] == -1){
       //Cargar COMtmp con datos del combi para evaluar contra reglas obj
       COMtmp[1] = COMdat[i][1];
       COMtmp[2] = COMdat[i][3];
       COMtmp[3] = COMdat[i][4];
       COMtmp[4] = COMdat[i][5];
       COMtmp[5] = COMdat[i][6];
       for (int j = 0; j < GAMnumRulObj; j++){
         //Comprobar cumplimiento de color
         if (COMdat[i][8] == LVL.OBJrul[j][2] || //si mismo color
             (LVL.OBJrul[j][2] == -1 && COMdat[i][8] != 0) || //si monocolor
             LVL.OBJrul[j][2] == -2) //si color indiferente

           //Comprobar cumplimiento resto de regla
           if (COM_ChkRulGen(j))
             GAMobjSts[j * 2 + 1]++;
       }
   }

   //Comprobar cumplimiento global del nivel
   for(int i=0; i<GAMnumRulObj;i++)
     if(GAMobjSts[i*2+1]<GAMobjSts[i*2])
       return false;

   return true;
}



// Desc: Chequeo de las Reglas-Objetivo asociadas a la pieza pasada por param.
 private void PIE_ChkRulObj(short pidPie, byte ptip, byte pori, byte pcol, short px, short py){
   //establece orientación unívoca para piezas simétricas.
   if((ptip==1 || ptip==3 || ptip==4 || ptip==8)&& pori >1)
     pori -=2;
   for(int i=0; i<GAMnumRulObj; i++)
     if (LVL.OBJrul[i][0] == TIPOBJ_POSPIE && GAMobjSts[i*2+1]==0)  ///Tipo objetivo PIE y todavia no cumplido
         if(LVL.OBJrul[i][4]==px && LVL.OBJrul[i][5]==py && LVL.OBJrul[i][6]==ptip && LVL.OBJrul[i][7]==pori) //si cumple pos, tipo y ori
           if(LVL.OBJrul[i][2]==0 || LVL.OBJrul[i][2]==pcol){ // si cumple color
             GAMaIdRul[0] = (byte)i;
             GAM_IncObjSts((byte)1); //Incrementa contador objetivos del nivel
             GAMbChkFinNiv=true; //Chequear fin nivel en el tick en curso
             GAMsts = PLAY_IN_CHKNIV;
             GAMsbSts=0;
           }
 }

// Desc: Chequeo de las Reglas-Objetivo asociadas a la pieza pasada por param.
// Return: Booleano que indica si se ha cumplido regla de posición del propulsor
 private boolean PRO_ChkRulObj(short ptip){
     for(int i=0; i<GAMnumRulObj; i++){
       if (LVL.OBJrul[i][0] == TIPOBJ_BORPROP && GAMobjSts[i*2+1]==0 && ptip == TIPOBJ_BORPROP) //Tipo objetivo PIE y todavia no cumplido
         if (LVL.OBJrul[i][6] == PROidbor) {
           GAMaIdRul[0] = (byte)i;
           GAM_IncObjSts((byte)1); //Incrementa contador objetivos del nivel
           GAMbChkFinNiv=true; //Chequear fin nivel en el tick en curso
           GAMsts = PLAY_IN_CHKNIV;
           GAMsbSts=0;
           return true;
         }

       if(LVL.OBJrul[i][0] == TIPOBJ_POSPROP && GAMobjSts[i*2+1]==0  && ptip == TIPOBJ_POSPROP){ //TIPOBJ_POSPROP
         int x = LVL.OBJrul[i][4];
         int y = LVL.OBJrul[i][5];
         if (LVL.OBJrul[i][7] == ARR)
           y--;
         else if (LVL.OBJrul[i][7] == IZQ)
           x--;
         if(PROposX == x && PROposY ==y){
           GAMaIdRul[0] = (byte)i;
           GAM_IncObjSts((byte)1); //Incrementa contador objetivos del nivel
           GAMbChkFinNiv=true; //Chequear fin nivel en el tick en curso
           GAMsts = PLAY_IN_CHKNIV;
           GAMsbSts=0;
           return true;
         }
       }
     }
     return false;
 }



int rnd(int interval)
{
    return (rand.nextInt() & 0x07FFFFFFF) % interval;
}


//--------------------------------------------------------------- GAM_GetSigPie
// Desc: Obtiene nuevo TIPO/ORI/COLOR de pieza a incluir en la cola de piezas
//       del nivel de juego.
//-----------------------------------------------------------------------------
private void GAM_GetSigPie(){
    byte tip=0, ori=0, col=0, idx=0;
    boolean bAleatorio = false;

   //Tratamiento obtención de pieza predefinida en secuencia
   if(GAMpieNumRepSec > 0 || GAMpieNumRepSec==-1){ //Si estado es secuencia predefinida
        idx = GAMpieSigValSec; //apunta al proximo valor de GAMpieSec a leer
        if (idx == ((LVL.PIEsec.length/3)-1)){ //si hemos llegado a final de secuencia
          GAMpieSigValSec = 0; //reinicio del puntero, para la próxima pieza
          if(GAMpieNumRepSec != -1) //si NO está activada la repetición de secuencia
            if(--GAMpieNumRepSec == 0) //si ya no quedan más repeticiones pasamos a modo aleatorio
              bAleatorio = true;
        }
        else
            GAMpieSigValSec++; //dejar preparado puntero para proxima pieza
    }
    else
        bAleatorio = true;

    //Tratamiento generación de pieza aleatoria
    if(bAleatorio){
//Obtiene de forma aleatoria tipo de pieza según probabilidades especificadas en NIVtiPieProb
        byte i, prob;
        do{
          prob = (byte) ((rand.nextInt() & 0x07FFFFFFF)% 100);
          for (i = 0; i < 8; i++)
            if (prob <= LVL.NIVtipPieProbAcu[i])
              break;
        }while(LVL.NIVtipPie[i] == 0);
        tip = (byte)(i+1);

//Idem para colores
        do{
          prob = (byte) ((rand.nextInt() & 0x07FFFFFFF)% 100);
          for (i = 0; i < 8; i++)
            if (prob <= LVL.NIVcolPieProbAcu[i])
              break;
        }while(LVL.NIVcolPie[i] == 0);
        col = (byte)(i+1);
        ori = (byte) ((rand.nextInt() & 0x07FFFFFFF)% 4);

    }
    //Tratamiento obtener pieza de secuencia predefinida
    else{
        idx = (byte)(idx*3);
        tip = LVL.PIEsec[idx];
        ori = LVL.PIEsec[idx+1];
        col = LVL.PIEsec[idx+2];
    }

   //Añadir a array de GAMpie en la pos indicada por el puntero
   //GAMpieLastId apunta a la posición de la pieza que ya ha sido colocada, y por tanto ha dejado la pos vacía
    GAMpie[GAMpieLastId][0] = tip;
    GAMpie[GAMpieLastId][1] = ori;
    GAMpie[GAMpieLastId][2] = col;
    HUDbPie = true;
    //Actualizar puntero para indicar la próxima pieza a ser colocada
    GAMpieLastId = (byte)((GAMpieLastId + 1) % 3);
}




//*****************************************************************************
//************************************************************************* ACT
//*****************************************************************************




//----------------------------------------------------------------- FGR_Dibujar
// Desc; Dibuja el cursor/propulsor y las piezas/combis asociadas al mismo o
//       la que esté en movimiento en el Foreground de pantalla
//----------------------------------------------------------------------------
  private void FGR_Dibujar(Graphics g) {
    short tilX = 0, tilY = 0, locPieX, locPieY;

//	Determinar posición local a pantalla FGR dónde dibujar el ACTor
     switch (VIS_InAreaScrollX(ACTposX)) {
          case 0:
            tilX = VISAreaScrollLimXizq;
            break;
          case -1:
            tilX = ACTposX;
            break;
          case 1:
            tilX = (short) (ACTposX - VISAreaScrollLimXder +  VISAreaScrollLimXizq);
     }

     switch (VIS_InAreaScrollY(ACTposY)) {
          case 0:
            tilY = VISAreaScrollLimYarr;
            break;
          case -1:
            tilY = (byte) ACTposY;
            break;
          case 1:
            tilY = (short) (ACTposY - VISAreaScrollLimYaba +  VISAreaScrollLimYarr);
     }

     //Obtener pos local de PIEtmp
     locPieX = VIS_ConvNivX(PIEtmp[2]);
     locPieY = VIS_ConvNivY(PIEtmp[3]);

     //Dibujar graficos asociados a OBjetivos del Nivel
     int j= OBJgfxSts>10?1:0;
     if(++OBJgfxSts >15)
       OBJgfxSts=0;
     for (int i = 0; i < GAMnumRulObj; i++)
       //IMPORTANTE: Los Objs que tengan asociado GFX deben tener un id superior a 2 ([PTE]XXXXX
       if (LVL.OBJrul[i][0] > 2 && GAMobjSts[i*2+1]==0) { //si tiene grafico objetivo asociado y el objetivo está sin cumplir
         if (VIS_SupEnFgr(LVL.OBJrul[i][4], LVL.OBJrul[i][5], LVL.OBJrul[i][4] + 2,
                          LVL.OBJrul[i][5] + 2)) { //si es visible en pantalla
           if (LVL.OBJrul[i][0] == TIPOBJ_POSPIE) //Tipo marca posición Pieza
             FGR_DibujarOBjPie(g, VIS_ConvNivX(LVL.OBJrul[i][4]),
                               VIS_ConvNivY(LVL.OBJrul[i][5]), LVL.OBJrul[i][6],
                               LVL.OBJrul[i][7], (short) (LVL.OBJrul[i][3] + j));
           else if (LVL.OBJrul[i][0] == TIPOBJ_POSPROP || LVL.OBJrul[i][0] ==TIPOBJ_BORPROP)//Tipo marca X
             HUD_DibImg(g, VIS_ConvNivX(LVL.OBJrul[i][4]) * NUMPIXELS_TILEX,
                            VIS_ConvNivY(LVL.OBJrul[i][5]) * NUMPIXELS_TILEY,
                            (short) (LVL.OBJrul[i][3] + j));
         }
         //Tipo FLECHA: Siempre visible en punto estatico en pantalla
         if (LVL.OBJrul[i][0] == TIPOBJ_INFO)
           HUD_DibImg(g, LVL.OBJrul[i][4] * NUMPIXELS_TILEX,
                        LVL.OBJrul[i][5] * NUMPIXELS_TILEY,
                        (short) (LVL.OBJrul[i][3] + j));
        }






     g.setClip(0, 0, DEVscrPxlW, DEVscrPxlH);

    //[PTE]: Decidir si camara va a quedarse en CURSOR o en pieza lanzada.
     //AHORA: camara fijada en cursor (ie, ACTnivX,Y siguen al cursor, no a la pieza)
     //Dibujar Pieza si está en movimiento (desplazándose)
     if(PIEtmp[0] == ACTIVA && PIEtmp[1] == PIE_EN_MOV){
       byte tip = (byte) PIEtmp[4];
       byte ori = (byte) PIEtmp[5];
       byte pieW = TIPPIEbb[tip - 1][ori * 2];
       byte pieH = TIPPIEbb[tip - 1][ori * 2 + 1];
       if (VIS_SupEnFgr(PIEtmp[2],PIEtmp[3],PIEtmp[2]+pieW-1,PIEtmp[3]+pieH-1)){
         CUR_DibujarPieImg(locPieX, locPieY);
       }
     }


     //Dibujar Cursor (en función del modo) y pieza asociada a cursor (si procede)
     switch(CURmodo){
        case LANZAR:    //Modo Propulsor
          PRO_Dibujar(tilX, tilY); //Dibujar propulsor
          if (CURbHayPieza) { //Dibujar pieza (si está pegada a propulsor)
            CUR_DibujarPieImg(locPieX, locPieY);
            CUR_DibujarPieCont(locPieX, locPieY);
          }
          break;
        case COLOCAR: //Modo Colocar
          if (CURbHayPieza) { //en inicio de partida no hay pieza en este modo
            CUR_DibujarPieImg(locPieX, locPieY);
            CUR_DibujarPieCont(locPieX, locPieY);
          }
          CUR_DibujarFlechaCol(tilX,tilY);
          break;
        case ELIMINAR: //Modo Eliminar
             if(CURbHayPieza) //Dibujar pieza highlighted si hay pieza apuntada
                 CUR_DibujarPieCont(locPieX, locPieY);
             CUR_DibujarFlechaEli(tilX,tilY); //Dibujar flechaX (siempre)
            break;
        case DESPLAZAR:    //Modo Desplazar
          System.out.println("Desp X:"+tilX+" Y:"+tilY+ " ACTX:"+ACTposX+" ACTY:"+ACTposY);
            if(CURbHayPieza || CURbHayCombi)  //Dibujar cursor selección si hay pieza/combi apuntada
                CUR_DibujarCurSel(locPieX, locPieY);
            CUR_DibujarFlechaDes(tilX,tilY);
            break;
        case VISUALIZAR:
          //Dibujar indicadores despl scroll pantalla (activados sólo si ACT dentro de zona de scroll).
          //CUR_DibujarFlechaCol(tilX,tilY);
          break;
     }
     if(ACTposY == NIVnumTilH-1 || locPieY>=11) //Pasada la posY 11 la pieza puede montarse en el hud y hay forzar su dibujado.
       HUDbIni = true;

  }


private void FGR_DibujarOBjPie(Graphics g, short ptilX, short ptilY, byte ptip, byte pori, short pidpix){
    int nivX, nivY;
    byte idPartPie;
    int man[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    TIPPIE_ObtenerContVal(ptip, pori);
    ptilX = (short)(ptilX*NUMPIXELS_TILEX);
    ptilY = (short)(ptilY*NUMPIXELS_TILEY);

    //El contorno de la pieza se dibuja 3 pixels a la izquierda y por encima de
    // cada tile que conforma cada parte de la pieza
    for (int j = 0; j < 3; j++) {
      nivY = j * NUMPIXELS_TILEY;
      for (int i = 0; i < 3; i++) {
        nivX = i * NUMPIXELS_TILEX;
        idPartPie = TIPPIErefval[j * 3 + i];
        if (idPartPie > 0)
          HUD_DibImg(g, ptilX + nivX, ptilY + nivY, pidpix);
      }
    }
 }




//----------------------------------------------------------------- ACT_Teclado
// Update:
// - ACTacuIncX,Y
//-----------------------------------------------------------------------------
 private void ACT_Teclado(){

   actorAccion = last_actorAccion;
   switch (actorAccion) {
     case DER: // Derecha
     case IZQ: // Izquierda
     case ARR: //Arriba
     case ABA: //Abajo
       if(CURmodo == LANZAR)
         PRO_Mover_Cur(actorAccion);
       else
         CUR_Mover(actorAccion);
        break;

     case STOP: //stop
       // Si se acaba de coger pieza chequear nueva posición del propulsor para ajustarla adecuadamente
        if(GAMbPieSig){
          if(CURmodo == LANZAR)
            PRO_AjustarAct();
          GAMbPieSig=false;
        }
        if(prev_actorAccion != STOP)
           prev_actorAccion = STOP;
        break;
   }


   //Gestión de Acciones discretas (Al dejar de pulsar teclas)
     actorAccion = disc_actorAccion;
     switch (actorAccion) {
       case FIRE:
         switch(CURmodo){
           case LANZAR: //Propulsor
             if (CURbHayPieza)
               PRO_LanzarPie();
             break;
           case COLOCAR: //Colocar
             if (CURbHayPieza)
               CUR_ColocarPie();
             break;
           case ELIMINAR: //Eliminar
             if (CURbHayPieza)
               CUR_EliminarPie();
             break;
           case DESPLAZAR: //Desplazar
             if (CURbHayPieza)
               CUR_DesplazarPie();
             else if (CURbHayCombi)
               CUR_DesplazarCom();
             break;
         }
         break;

       case AUX1: //Rotación izquierda y derecha
       case AUX2:
           if(CURmodo == LANZAR || CURmodo == COLOCAR)
             CUR_RotarPie(actorAccion);
           else if (CURmodo == DESPLAZAR)
             CUR_RotarCURsel(actorAccion);
           if(CURmodo==LANZAR)
             PRO_AjustarAct();
         break;

// Opciones sólo para testing&tweaking

       case AUX4:
         CUR_CambiarTipPie();
         break;
       case AUX3:
         CUR_CambiarCol();
         break;
       case AUX5:
         GAMsts = PLAY_OUT_CHGNIV;
         GAMsbSts = 0;
         break;

       case FIRE2: //Cambio de modo de cursor (tecla [0])
         if(PIEtmp[0] == INACTIVA || (PIEtmp[0] == ACTIVA && PIEtmp[1] != PIE_EN_MOV && PIEtmp[1] != COM_EN_MOV))
           CUR_CambiarSigMod();
         break;
      case SOFTLEFT:
          HUD_SoftLeft();
          break;
      case SOFTRIGHT:
          HUD_SoftRight();
          break;
     }
     disc_actorAccion = STOP;
 }



private boolean ACT_FinMov(boolean pand){
   if(pand){
     if (ACTposX == ACTdstX && ACTposY == ACTdstY)
       return true;
   }
   else if (ACTposX == ACTdstX || ACTposY == ACTdstY)
     return true;
   return false;
 }

private boolean ACT_RetMov(){
   if ((ACTposX + ACTincX) == ACTdstX && (ACTposY + ACTincY )== ACTdstY)
     return true;
   else
     return false;

 }

//*****************************************************************************
//************************************************************************* CUR
//*****************************************************************************

 private void CUR_Tick() {
     //Actualizar posición actual del cursor
    CURposX = ACTposX;
    CURposY = ACTposY;

    //Actualiza posición de display de la pieza en función del modo de cursor
    switch (CURmodo) {
      case LANZAR: //Modo Lanzar
        PROposX = CURposX; //Actualizar posición actual de propulsor
        PROposY = CURposY;

        PRO_CalcLen();
        if(CURbHayPieza){
            //PRO_AjustarBor(); //Ajusta PROposX e Y en caso de que hayan salido fuera de borde
            //ACTposX = PROposX;
            //ACTposY = PROposY;
          PIETMP_CalcPosModLan(); //Calcula la posicion de la pieza temporal asociada al propulsor
        }

        break;
      case COLOCAR: //Modo Colocar
      case VISUALIZAR: //MOdo scroll visualización
        PIETMP_CalcPosModCol();
        break;
      case ELIMINAR: //Modo Eliminar
        if(!PIETMP_CalcPosModEli())  //Si cursor no apunta a pieza 'eliminable'
            CURbHayPieza = false;
        break;
      case DESPLAZAR: //Modo Desplazar
        if(!PIETMP_CalcPosModDes()){  //Si cursor no apunta a pieza o combi 'desplazable'
            CURbHayPieza = false;
            CURbHayCombi = false;
        }
        break;
    }
 }



//----------------------------------------------------------------- CUR_Mover

private void CUR_Mover(byte dir){
   if(dir==DER){
     if ( ACTposX < NIVnumTilW-1 )
        ACTacuIncX = (byte) 1;
   }else if (dir==IZQ){
     if ( ACTposX > 1 )
        ACTacuIncX = (byte) -1;
   }else if (dir==ARR){
     if ( ACTposY > 1 )
        ACTacuIncY = (byte) -1;
   }else if (dir==ABA){
     if ( ACTposY < NIVnumTilH-1 )
       ACTacuIncY = (byte) 1;
   }
}




//----------------------------------------------------------- CUR_DibujarPieImg
// Desc: Dibuja la pieza temporal en las posiciones locales de pantalla pasadas
//       por parámetro.
//-----------------------------------------------------------------------------
 private void CUR_DibujarPieImg(short ptilX, short ptilY){
   int nivX, nivY;
   int offset = (PIEtmp[6] - 1) * 14 * NUMPIXELS_TILE_XY; //offset sobre GAMpiePix: 14 gráficos * color (1 gráfico = 12*12=144, 144*14= 2016)
   byte idPartPie;

   TIPPIE_ObtenerVal((byte) (PIEtmp[4]), (byte) (PIEtmp[5]));
   ptilX = (short)(ptilX * NUMPIXELS_TILEX);
   ptilY = (short)(ptilY * NUMPIXELS_TILEY);

   for (int j = 0; j < 3; j++) {
     nivY = j * NUMPIXELS_TILEY;
     if((ptilY + nivY)>=NUMTILES_SCRY*NUMPIXELS_TILEY)
       break;
     for (int i = 0; i < 3; i++) {
       nivX = i * NUMPIXELS_TILEX;
       idPartPie = TIPPIErefval[j * 3 + i];
       if (idPartPie > 0)
         SCRdg.drawPixels(GAMpiePix,
                       true, //transparencia
                       offset + (idPartPie - 1) * NUMPIXELS_TILE_XY, //offset inicial + id_parte_pieza * logitud_parte_pieza
                       NUMPIXELS_TILEX, //scanlength
                       ptilX + nivX, ptilY + nivY, //posX, posY locales
                       NUMPIXELS_TILEX, NUMPIXELS_TILEY, //ancho, alto
                       0, DEVntvFmt);
     }
   }

 }


//--------------------------------------------------------- CUR_DibujarPieCont
// Desc: Dibuja el contorno de la pieza temporal en las posiciones locales de
//       pantalla pasadas por parámetro.
//-----------------------------------------------------------------------------

 private void CUR_DibujarPieCont(short ptilX, short ptilY){
   int nivX, nivY;
   byte idPartPie;
   int man[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

   TIPPIE_ObtenerContVal((byte)PIEtmp[4], (byte)PIEtmp[5]);
   TIPPIE_ObtenerContMan(man, (byte)PIEtmp[4], (byte)PIEtmp[5]);
   ptilX = (short)(ptilX*NUMPIXELS_TILEX);
   ptilY = (short)(ptilY*NUMPIXELS_TILEY);

   //El contorno de la pieza se dibuja 3 pixels a la izquierda y por encima de
   // cada tile que conforma cada parte de la pieza
   for (int j = 0; j < 3; j++) {
     //#if CFGtamanyoGfx == 2   
//#      nivY = j * NUMPIXELS_TILEY - 4;
     //#else
      nivY = j * NUMPIXELS_TILEY - 3;
      //#endif
     for (int i = 0; i < 3; i++) {
       //#if CFGtamanyoGfx == 2   
//#        nivX = i * NUMPIXELS_TILEX - 4;
       //#else
       nivX = i * NUMPIXELS_TILEX - 3;
       //#endif
       idPartPie = TIPPIErefval[j * 3 + i];
       if (idPartPie > 0)
         SCRdg.drawImage(CURpix[idPartPie-1], ptilX + nivX, ptilY + nivY, Graphics.TOP | Graphics.LEFT, man[j * 3 + i]);
     }
   }
}


private void CUR_DibujarFlechaCol(short ptilX, short ptilY){
   SCRdg.drawImage(CURpix[13], ptilX*NUMPIXELS_TILEX, ptilY*NUMPIXELS_TILEY, Graphics.TOP | Graphics.LEFT, 0);
}


private void CUR_DibujarFlechaEli(short ptilX, short ptilY){
   SCRdg.drawImage(CURpix[14], ptilX*NUMPIXELS_TILEX, ptilY*NUMPIXELS_TILEY, Graphics.TOP | Graphics.LEFT, 0);
}


private void CUR_DibujarFlechaDes(short ptilX, short ptilY){
   SCRdg.drawImage(CURpix[15], ptilX*NUMPIXELS_TILEX, ptilY*NUMPIXELS_TILEY, Graphics.TOP | Graphics.LEFT, 0);
}

//--------------------------------------------------------- CUR_DibujarCurSel
// Desc: Dibuja el cuadrante que rodea a la pieza temporal en las posiciones
//       locales de pantalla pasadas por parámetro.
//-----------------------------------------------------------------------------

private void CUR_DibujarCurSel(short ptilX, short ptilY){
// Dibujar cuadrante
   int i, j, nivX, nivY, nivXfin, nivYfin, medX, medY, oriDes;
   byte idPartPie, tip, ori, pieW, pieH;

   if(CURbHayPieza){
     tip = (byte) PIEtmp[4];
     ori = (byte) PIEtmp[5];
     pieW = TIPPIEbb[tip - 1][ori * 2];
     pieH = TIPPIEbb[tip - 1][ori * 2 + 1];
   }
   else{
     pieW = COMdat[PIEtmp[8]][5];
     pieH = COMdat[PIEtmp[8]][6];
   }

   ptilX = (short)(ptilX*NUMPIXELS_TILEX);
   ptilY = (short)(ptilY*NUMPIXELS_TILEY);
   nivXfin = ptilX + ( (pieW - 1) * NUMPIXELS_TILEX - 4);
   nivYfin = ptilY + ( (pieH - 1) * NUMPIXELS_TILEY - 4);


   if(pieW==1){
     SCRdg.drawImage(CURpix[8], ptilX -4, ptilY -4,Graphics.TOP | Graphics.LEFT, 0);
     SCRdg.drawImage(CURpix[8], nivXfin, nivYfin,Graphics.TOP | Graphics.LEFT, DirectGraphics.FLIP_VERTICAL);
   }
   else if(pieH==1){
     SCRdg.drawImage(CURpix[8], ptilX -4, ptilY -4,Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_90);
     SCRdg.drawImage(CURpix[8], nivXfin, nivYfin,Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_270);
   }
   else{
     SCRdg.drawImage(CURpix[7], ptilX - 4, ptilY - 4,
                  Graphics.TOP | Graphics.LEFT, 0);
     SCRdg.drawImage(CURpix[7], ptilX - 4, nivYfin, Graphics.TOP | Graphics.LEFT,
                  DirectGraphics.FLIP_VERTICAL);
     SCRdg.drawImage(CURpix[7], nivXfin, ptilY - 4, Graphics.TOP | Graphics.LEFT,
                  DirectGraphics.FLIP_HORIZONTAL);
     SCRdg.drawImage(CURpix[7], nivXfin, nivYfin, Graphics.TOP | Graphics.LEFT,
                  DirectGraphics.ROTATE_180);
   }


   //Displayar indicadores de dirección de desplazamiento
   short idPixDes[] = {11,11,11,11}; //IMPORTANTE: mantener mismo orden de iconos con Ctes ARR-ABA-IZQ-DER (1,2,3 y 4)
   oriDes = PIEtmp[7];
   idPixDes[oriDes-1]++; //selección del grafico de la orientación de desplazamiento seleccionada
   medX = (short) ((nivXfin + NUMPIXELS_TILEX + 4 - ptilX)/2 - 10);
   medY = (short) ((nivYfin + NUMPIXELS_TILEY + 4 - ptilY)/2 - 10);
   SCRdg.drawImage(CURpix[idPixDes[0]], ptilX + medX, ptilY-NUMPIXELS_TILEY-8, Graphics.TOP | Graphics.LEFT,0);
   SCRdg.drawImage(CURpix[idPixDes[1]], ptilX + medX, nivYfin + NUMPIXELS_TILEY + 4, Graphics.TOP | Graphics.LEFT,DirectGraphics.FLIP_VERTICAL);
   SCRdg.drawImage(CURpix[idPixDes[2]], ptilX - NUMPIXELS_TILEX - 8, ptilY + medY, Graphics.TOP | Graphics.LEFT,DirectGraphics.ROTATE_90);
   SCRdg.drawImage(CURpix[idPixDes[3]], nivXfin + NUMPIXELS_TILEX + 4, ptilY + medY, Graphics.TOP | Graphics.LEFT,DirectGraphics.ROTATE_270);
   HUDbIni = true; //Ya que los indicadores de despl. se pueden dibujar sobre el HUD
}



//---------------------------------------------------------------- CUR_CogerPie
// Desc: Toma primera pieza de las tres existentes en cola <GAMpie> y la asigna
//       al propulsor.
//-----------------------------------------------------------------------------
 private void CUR_CogerPie(){
   CURbHayPieza = true;

   PIEtmp[0] = ACTIVA; //Pieza activa
   PIEtmp[1] = PIE_EN_CUR; //Pieza asociada al cursor
   PIEtmp[4] = GAMpie[GAMpieLastId][0]; //Tipo
   PIEtmp[5] = GAMpie[GAMpieLastId][1]; //Orientación
   PIEtmp[6] = GAMpie[GAMpieLastId][2]; //Color
//Generar la pos de la pieza en función del tipo de la misma (su bb) y
//la localización del propulsor.
   PIETMP_CalcPosModLan();
   PRO_CalcLen();

 }

//-------------------------------------------------------------- CUR_SoltarPie
// Desc: Fuerza a soltar la pieza en curso por finalización de tiempo.
//       a) Lanza la pieza si modo LANZAR
//       b) Suelta la pieza si modo COLOCAR
//       c) Elimina la pieza si modo ELIMINAR y pieza seleccionada
//       d) Desplaza la pieza/combi si modo DESPLAZAR y pieza/combi seleccionada
//       e) Lanza la pieza si modo ELIMINAR o DESPLAZAR y no hay selección, o
//          modo ELIMINAR y hay combi seleccionado
// Return: booleano que indica si es necesario lanzar la pieza o no
//------------------------------------------------------------------------------
private boolean CUR_SoltarPie(){
    boolean bLan= false;

    //Decidir acción a realizar en función de modo
    switch(CURmodo){
        case LANZAR:
            bLan = true;
            break;
        case COLOCAR:
            if(!CUR_ColocarPie())
                bLan = true;
            break;
        case ELIMINAR:
            if(CURbHayPieza){
                if(!CUR_EliminarPie())
                    bLan = true;
            }else
                bLan = true;
            break;
        case DESPLAZAR:
            if(CURbHayPieza){
                if(CUR_DesplazarPie())
                    break;
                else
                    bLan = true;
            }
            else if (CURbHayCombi){
              if (CUR_DesplazarCom())
                break;
              else
                bLan = true;
            }
            else
                bLan = true;
            break;
    }
    return bLan;
}

// Desc: Cambio forzado a de modo cursor
private boolean CUR_LanzarPie(){
    if(CURmodo != LANZAR)
      CUR_CambiarMod(LANZAR);
    return PRO_LanzarPie();
}

// Desc: Colocar pieza en primer hueco del tablero.
private boolean CUR_DejarPie(){
    //buscar hueco
    for(short i=0;i<NIVnumTilW; i++)
        for(short j=0;j<NIVnumTilH; j++)
            if(! NIV_ChkColPie(i, j, (byte)PIEtmp[4], (byte)PIEtmp[5])){
               PIEtmp[2] = i;
               PIEtmp[3] = j;
               return CUR_ColocarPie();
            }
    return false;
}



private void CUR_RotarPie(short pacc){
    if(pacc==AUX3)
      PIEtmp[5] = (byte) ((PIEtmp[5] + 1)% 4);
    else
      PIEtmp[5] = (byte)(PIEtmp[5]==0?3:(PIEtmp[5] - 1) % 4);
}


private void CUR_CambiarTipPie(){
    do{
         PIEtmp[4]++;
         if(PIEtmp[4] > NUMTIPPIE)
                 PIEtmp[4] = 1;
    }while(LVL.NIVtipPie[PIEtmp[4]-1] == 0);
}


private void CUR_CambiarCol(){
    do{
         PIEtmp[6]++;
         if(PIEtmp[6] > NUMCOLORES)
                 PIEtmp[6] = 1;
    }while(LVL.NIVcolPie[PIEtmp[6]-1] == 0);
}

//[PTE]: cambiar la secuencia de constantes a ARR,DER,ABA,IZQ
private void CUR_RotarCURsel(short pAcc){
    byte sec[]={1,4,2,3};
    int i;
    for(i=0;PIEtmp[7]!=sec[i];i++);

    if(pAcc== AUX2) //CW
      if(i==3)
        PIEtmp[7]=sec[0];
      else
        PIEtmp[7]=sec[i+1];
    else
      if(i==0)
        PIEtmp[7]=sec[3];
      else
        PIEtmp[7]=sec[i-1];
}



//-------------------------------------------------------------- CUR_CambiarMod
// Desc: Cambiar el modo de cursor (Cambio cíclico) al siguiente modo que
//       tenga contador de uso suficiente (indeterminado o >0), o al siguiente
//       modo independientemente del contador si estamos en modo con tiempo de
//       pieza.
//-----------------------------------------------------------------------------
 private void CUR_CambiarSigMod(){
    byte sigmod=CURmodo;
    if(TIMbPie)
      sigmod = (byte) ( (CURmodo + 1) % 4);
    else
      do {
        sigmod = (byte) ( (sigmod + 1) % 4);
      }
      while (CURcont[sigmod] == 0);

    if(sigmod != CURmodo){
      CUR_CambiarMod(sigmod);
      HUDbIni = true;
      HUDbCurCont = true;
    }
}

//-------------------------------------------------------------- CUR_CambiarMod
// Desc: cambiar del modo actual al nuevo modo <pmod>
//       Se asume que el modo actual es diferente al nuevo modo
//-----------------------------------------------------------------------------
private void CUR_CambiarMod(byte pmod){

    //[PTE]: [FX]: Dibujar en tick actual gráfico intermedio de desaparición/desintegración de PIE (FX) ((si hay pieza asociada al cursor))

    //[PTE]: si salimos de modo LANZ a otro modo colocar el cursor cerca del prop, pero no encima de bordes.
    // Basta chequear que CURposX, CURposY no están ni fuera de tablero ni en bordes.
   if(CURmodo == LANZAR){ //calcular posición de la flecha cursor
        if(PROmodo == HORZ){
            CURposX = (short)(PROposX + 2);
            if(LVL.NIVbor[PROidbor][2] == ABA)
                CURposY = (short)(PROposY +2);
            else
                CURposY = PROposY;
        }
        else{
            CURposY = (short)(PROposY + 2);
            if(LVL.NIVbor[PROidbor][2] == DER)
                CURposX = (short)(PROposX +2);
            else
                CURposX = PROposX;
        }
    }


    //Si el nuevo modo es LANZAR habrá que situar el propulsor en el borde más cercano a la posición actual del cursor
    if(pmod == LANZAR){
      PROidbor = PRO_ObtenerCerBor((short)0, CURposX, CURposY);
      PROori = LVL.NIVbor[PROidbor][2];
      if((PROmodo = LVL.NIVbor[PROidbor][1]) == HORZ){ //se mantiene la posX
          PROposX = (short)(CURposX-2);
          if(PROori == ABA)
              CURposY = PROposY = LVL.NIVbor[PROidbor][3];
          else
              CURposY = PROposY = (short)(LVL.NIVbor[PROidbor][3]-1);
      }
      else{
         PROposY = (short)(CURposY-2);
          if(PROori == DER)
              CURposX = PROposX = LVL.NIVbor[PROidbor][3];
          else
              CURposX = PROposX = (short)(LVL.NIVbor[PROidbor][3]-1);

      }
      CUR_CogerPie();
      PRO_CalcLen();
      PRO_AjustarBor();
      CURposX = PROposX;
      CURposY = PROposY;
    }

    if(pmod == COLOCAR){
      CUR_CogerPie();
    }

    ACTposX = CURposX;
    ACTposY = CURposY;
    CURmodo = pmod;
    VISbUpd = true;
}

// Desc: decrementa contador de modo cursor
private void CUR_DecCont(int pmod){
    if(CURcont[pmod] != -1){
      CURcont[pmod]--;

      //Activa gfx de descontar modo cursor
      FX_Add((short)5, (short)-1, (short)pmod);

      HUDbCurCont = true;
    }
    //#if CFGtipoSnd == 2
    else //SND FX colocación/eliminación/desplazamiento en caso de que no descuente
    if(COMBIMidlet.M.MENbSnd)
      MUS.TocaMusica(pmod==1?0:5);
    //#endif

}

//---------------------------------------------------------------- CUR_IncCont
// Desc: Incrementa los contadores de modos de cursor indicados en el vector
//       binario <CURcontBon> en la cantidad <pInc>
//----------------------------------------------------------------------------
private void CUR_IncCont(byte pInc){
    for(int i=1, idx=0; i<=16; i*=2, idx++)
      if((CURcontBon & i)!= 0)  //AND lógico para detectar si el modo cursor (4-idx) tiene q ser incrementado
        CURcont[4-idx] += pInc;
    HUDbCurCont = true;
}


//-------------------------------------------------------------- CUR_ColocarPie
// Desc: Comprueba que es posible colocar en el tablero la pieza asociada al
//       cursor. En caso afirmativo llama a NIVel para que se encargue de su
//       colocación.
//-----------------------------------------------------------------------------
 private boolean CUR_ColocarPie(){
   byte tip, ori;
   short posX, posY;

   posX = PIEtmp[2];
   posY = PIEtmp[3];
   tip = (byte)PIEtmp[4];
   ori = (byte)PIEtmp[5];

   if(CURcont[1] !=0){
       if (!NIV_ChkColPie(posX, posY, tip, ori)){
         NIV_ColocarPie(posX, posY); //Coloca la pieza
         //Contabiliza
         CUR_DecCont(1);
         //Cambio estados
         GAMsts = PLAY_IN_COMBI; //siguiente subestado GAM: detectar formación Combi y crearlo
         GAMsbSts = 0;
         PIETMPsts = STSPIE_EN_HUD; //siguiente subestado PIE: Generar y coger siguiente pieza, e init time.
         PIETMPsbSts = 0;  //Generar y coger
         CURbHayPieza = false;  //[PTE: se puede quitar esto??? el CUR pasara a PRO, no?

         return true;
       }
       //else
       //  System.out.println("FX =>> No es posible colocar pieza. Hay colisión");
    }
    else
      FX_Add( (short) 6, (short) -1, (short)43); //System.out.println("FX =>> Contador de cursor Colocación a 0.");
    return false;
 }


//------------------------------------------------------------- CUR_EliminarPie
// Desc: Elimina la pieza seleccionada por el cursor
//       (Se asume que hay pieza seleccionada y cumple las condiciones de eliminación)
//-----------------------------------------------------------------------------
 private boolean CUR_EliminarPie(){
   short pIdPie;

   if(CURcont[2] !=0){
       pIdPie = PIEtmp[8];

       //Comprueba si se intenta eliminar alguna pieza por defecto estando prohibido en el nivel
       if(!LVL.OBJbEliDefPie || !PIE_EsDef(PIEtmp[2], PIEtmp[3], (byte)PIEtmp[4], (byte)PIEtmp[5])){
         //Añade FX eliminación pieza
         FX_Add( (short) 8, (short) 0, (short)0);
         NIV_ResetMapPie(PIEtmp[2], PIEtmp[3], (byte)PIEtmp[4], (byte)PIEtmp[5]);

         //Contabiliza
         CUR_DecCont(2);
         //Cambiar el estado de la pieza y desactivar la pieza temporal
         PIEdat[pIdPie][0] = LIBRE;
         PIEtmp[0] = INACTIVA;
         CURbHayPieza = false;

         PIETMPsts = STSPIE_EN_HUD; //Cambiar estado del FSM de pieza
         PIETMPsbSts = 2; //No generar una nueva pieza. Solo init tiempo

         return true;
       }
       else
         FX_Add( (short) 6, (short) -1, (short)45); //System.out.println("FX =>> NO es posible borrar pieza por defecto");
   }
   else
      FX_Add( (short) 6, (short) -1, (short)43); //System.out.println("FX =>> Contador de cursor Eliminación a 0.");
    return false;
 }

//------------------------------------------------------------ CUR_DesplazarPie
// Desc: Comprueba que es posible desplazar la pieza del tablero, seleccionada
//       por el cursor, en la dirección de desplazamiento actual.
//       (Se asume que hay pieza seleccionada)
//-----------------------------------------------------------------------------

 //[PTE: Cambiar. La pieza seleccionada para desplazar cumple las condiciones de desplazamiento
 // El sentido de desplazamiento queda indicado en PIEtmp[7]

 private boolean CUR_DesplazarPie(){
    //Comprobar que el contador de opciones de Desplazamiento de Piezas es > 0
   if(CURcont[3] !=0){
     //Comprueba si se intenta desplazar alguna pieza por defecto estando prohibido en el nivel
     if(!LVL.OBJbEliDefPie || !PIE_EsDef(PIEtmp[2], PIEtmp[3], (byte)PIEtmp[4], (byte)PIEtmp[5])){

       //Detectar posición siguiente en el sentido del desplazamiento (PIEtmp[7])
       short newPosX = PIEtmp[2];
       short newPosY = PIEtmp[3];
       //PIE_MoverTmp(newPosX, newPosY, true)
       switch (PIEtmp[7]) {
         case ARR:
           newPosY--;
           break;
         case ABA:
           newPosY++;
           break;
         case IZQ:
           newPosX--;
           break;
         case DER:
           newPosX++;
           break;
       }

       //Comprobar si hay obstáculo en el sentido del desplazamiento.
       //(sin tener en cuenta colisión con la propia pieza)
       short idPie = NIVidPieza[CURposY * NIVnumTilW + CURposX];
       if (!NIV_ChkColPie(idPie, newPosX, newPosY)) {
         //System.out.println("FX =>> Desplazamiento de pieza");
         NIV_DesplazarPie(idPie, PIEtmp[2], PIEtmp[3], newPosX, newPosY);
         //Contabiliza
         CUR_DecCont(3);
         CURbHayPieza = false;
         return true;
       }
//       else
//         System.out.println("FX =>> No es posible desplazar pieza. Hay colisión");
     }else
       FX_Add( (short) 6, (short) -1, (short)45); //System.out.println("FX =>> No se permite desplazar pieza por defecto.");
    }else
      FX_Add( (short) 6, (short) -1, (short)43); //System.out.println("FX =>> Contador de cursor Desplazamiento de pieza a 0.");

    return false;
 }



//------------------------------------------------------------ CUR_DesplazarCom
// Desc: Desplaza combi en el sentido de desplazamiento establecido en el c
//       cursor de selección <PIEtmp[7]>
//       (Se asume que el Combi seleccionado es de mayor nivel -sin padre-)
//----------------------------------------------------------------------------
private boolean CUR_DesplazarCom(){
    //Comprobar que el contador de opciones de Desplazamiento de Combis es > 0
   if(CURcont[4] !=0){
     //Comprueba si se intenta desplazar alguna pieza por defecto estando prohibido en el nivel
     if (!LVL.OBJbEliDefPie || !COM_EsDef(PIEtmp[8])) {
       short newPosX = PIEtmp[2];
       short newPosY = PIEtmp[3];
       switch (PIEtmp[7]) {
         case ARR:
           newPosY--;
           break;
         case ABA:
           newPosY++;
           break;
         case IZQ:
           newPosX--;
           break;
         case DER:
           newPosX++;
       }

       //Comprobar si hay obstáculo en el sentido del desplazamiento.
       if (!NIV_ChkColCom(newPosX, newPosY, COMdat[PIEtmp[8]][5],
                          COMdat[PIEtmp[8]][6], (byte) PIEtmp[7])) {
 //        System.out.println("FX =>> Inicio Desplazamiento de Combi");
         NIV_DesplazarCom();
         //Contabiliza
         CUR_DecCont(4);
         CURbHayCombi = false;
         return true;
       }
//       else
//         System.out.println("FX =>> No es posible desplazar Combi. Hay colisión");
     }
     else
       FX_Add( (short) 6, (short) -1, (short)39); //System.out.println("FX =>> No se permite desplazar combi por defecto.");
    }else
      FX_Add( (short) 6, (short) -1, (short)43); //System.out.println("FX =>> Contador de cursor Desplazamiento de Combis a 0.");
    return false;
}


//---------------------------------------------------------------- PRO_Avanzar
// Desc: Avance automático del propulsor en el borde en el que esté situado
//-----------------------------------------------------------------------------
private void PRO_Avanzar(){
    byte dir;
    if(PROori==ARR)
      dir = LVL.NIVbor[PROidbor][0]==0?IZQ:DER;
    else if (PROori==ABA)
      dir = LVL.NIVbor[PROidbor][0]==0?DER:IZQ;
    else if (PROori==DER)
      dir = LVL.NIVbor[PROidbor][0]==0?ARR:ABA;
    else
      dir = LVL.NIVbor[PROidbor][0]==0?ABA:ARR;
    PRO_Mover_Cur(dir);
 }


//--------------------------------------------------------------- PRO_Mover_Cur
// Desc: Mover el propulsor en el sentido indicado por <dir>. (en sentido de
//       Si el sentido es opuesto al borde en el que está el propulsor se
//       produce un cambio al borde opuesto más próximo.
//----------------------------------------------------------------------------
private void PRO_Mover_Cur(byte dir){
short ajuste, newX, newY, newIdbor, sit, zona;
short incX=0, incY = 0;

    zona =LVL.NIVbor[PROidbor][0];
    if(PROmodo == HORZ){
      if (dir == DER)
        incX = 1;
      else if (dir == IZQ)
        incX = (short) -1;
      else if (dir == PROori && PIETMPsts != STSPIE_EN_MOV){ //ARR || ABA
        PRO_CambiarIni(); //cambiar de borde horizontal
        return;
      }
      //calcular sentido CW o CCW del posible paso a siguiente borde
      if((zona == 0 && PROori == ARR) ||(zona != 0 && PROori == ABA)){
        if (dir == DER)
          dir = IZQ;
        else
          dir = DER;
      }
    }

    else{ // modo VERT
      if (dir == ABA)
        incY =1;
      else if (dir == ARR)
        incY = (short) -1;
      else if (dir == PROori && PIETMPsts != STSPIE_EN_MOV){ //DER || IZQ
        PRO_CambiarIni(); //cambiar de borde vertical
        return;
      }
      //calcular sentido CW o CCW del posible paso a siguiente borde
      if((zona == 0 && PROori == DER) || (zona != 0 && PROori == IZQ)){
        if (dir == ARR)
          dir = DER;
        else
          dir = IZQ;
      }else{
        if (dir == ARR)
          dir = IZQ;
        else
          dir = DER;
      }
    }

  //La nueva posición tras sumar el incremento puede hacer salir el prop del borde
    newX = (short) (PROposX + incX);
    newY = (short) (PROposY + incY);

    // Si no se ha sobrepasado el límite del borde actual
    if((sit = PRO_InBor(PROidbor, PROlen, newX, newY)) == 0){
      ACTacuIncX = incX;
      ACTacuIncY = incY;
    }else{ //borde sobrepasado. Identificar siguiente/anterior borde dentro de la misma zona
      newIdbor = PRO_ObtenerSigBor(PROidbor, dir);
      //cambiar el propulsor al nuevo borde
      PROmodo = LVL.NIVbor[newIdbor][1];
      PROori = LVL.NIVbor[newIdbor][2];
      PRO_CalcLen(); //El cambio de modo produce cambio de longitud (Ojo: la pieza no rota al girar el propulsor)
      PROposX = PRO_CalcPosXBor(newIdbor, dir, zona); //calcula primera posición X del borde para colocar el propulsor al inicio/fin del borde en función del sentido por el que se viene
      PROposY = PRO_CalcPosYBor(newIdbor, dir, zona);
      PROidbor = newIdbor;
      //los incrementos a sumar a ACTniv deben dar como resultado que ACTniv coincida con la posición del propulsor
      ACTacuIncX = (short)(PROposX - ACTposX);
      ACTacuIncY = (short)(PROposY - ACTposY);
      //la posición de PIEtmp quedará actualizada en CUR_Tick
      VISbUpd=true;
      //Chequeo de las Reglas-Objetivo asociadas al borde del Propulsor
      PRO_ChkRulObj(TIPOBJ_BORPROP);
    }

    //Chequeo de las Reglas-Objetivo asociadas a la posición del Propulsor
    PRO_ChkRulObj(TIPOBJ_POSPROP);

}



//------------------------------------------------------------------- PRO_InBor
// Desc: Chequea si un Propulsor con longitud <plen> y situado en las
//       coordenadas <ptilX>,<ptilY> cae dentro o fuera del borde <pidBor>
//-----------------------------------------------------------------------------
private short PRO_InBor(short pidBor, short plen, short ptilX, short ptilY){
  short nivIni;

  if(LVL.NIVbor[pidBor][1] == HORZ)
    nivIni = ptilX;
  else
    nivIni = ptilY;

  if(nivIni < (LVL.NIVbor[pidBor][4] -1)) // desde - 1
    return -1;
  else if ((nivIni + plen) > LVL.NIVbor[pidBor][5]) //hasta
    return 1;
  else
    return 0;
}
//----------------------------------------------------------- PRO_ObtenerSigBor
// Desc: Obtiene el siguiente borde a <pidBor> en el sentido indicado por
//       <pdir> (Derecha -CW-  o Izquierda -CCW-), dentro de la misma zona.
// Return: id del siguiente/anterior borde dentro de la misma zona.
//-----------------------------------------------------------------------------
private short PRO_ObtenerSigBor(short pidBor, short pdir){
  short dst;
  if(pdir == DER) //obtener siguiente
    dst = (short) ((LVL.NIVbor[pidBor][6]+1)%LVL.NIVzon[LVL.NIVbor[pidBor][0]]);
  else{
    dst = (short) (LVL.NIVbor[pidBor][6] - 1);
    if(dst < 0)
      dst = (short)(LVL.NIVzon[LVL.NIVbor[pidBor][0]]-1);
  }

  for(short i=0; i<LVL.numTotBor; i++)
    if(LVL.NIVbor[i][0] == LVL.NIVbor[pidBor][0] && LVL.NIVbor[i][6] == dst)
      return i;
  return -1;
}

//------------------------------------------------------------- PRO_CalcPosXBor
// Desc: Retorna pos X donde colocar el propulsor que acaba de entrar al
//       borde <pidBor> en el sentido <pdir>.
// Se retorna la primera/ultima pos X para inicio/fin del borde en función del sentido por el que se viene
private short PRO_CalcPosXBor(short pidBor, short pdir, short pzon){
  short oriBor = LVL.NIVbor[pidBor][2];


  if (pdir == DER) //entrada al borde en sentido de las agujas del reloj
    switch(oriBor){
      case ABA:
        if(pzon == 0)
          return (short)(LVL.NIVbor[pidBor][4]-1); //x_desde-1
        else
          return (short)(LVL.NIVbor[pidBor][5] - PROlen); //x_hasta - longitudPro
      case ARR:
        if(pzon == 0)
          return (short)(LVL.NIVbor[pidBor][5] - PROlen); //x_hasta - longitudPro
        else
          return (short)(LVL.NIVbor[pidBor][4]-1); //x_desde-1
      case IZQ:
        return (short)(LVL.NIVbor[pidBor][3]-1); //x_fija-1
      case DER:
        return LVL.NIVbor[pidBor][3]; //x_fija
    }
  else //entrada al borde en sentido inverso a las agujas del reloj
    switch(oriBor){
       case ABA:
         if (pzon == 0)
           return (short) (LVL.NIVbor[pidBor][5] - PROlen); //x_hasta - longitudPro
         else
           return (short) (LVL.NIVbor[pidBor][4] - 1); //x_desde-1
       case ARR:
         if (pzon == 0)
           return (short) (LVL.NIVbor[pidBor][4] - 1); //x_desde-1
         else
           return (short) (LVL.NIVbor[pidBor][5] - PROlen); //x_hasta - longitudPro
       case IZQ:
         return (short)(LVL.NIVbor[pidBor][3]-1); //x_fija-1
       case DER:
         return LVL.NIVbor[pidBor][3];//x_fija
     }
   return -1;
}

//------------------------------------------------------------- PRO_CalcPosYBor
private short PRO_CalcPosYBor(short pidBor, short pdir, short pzon){
   short oriBor = LVL.NIVbor[pidBor][2];

   if(pzon != 0 && (oriBor == IZQ || oriBor == DER))
     if(pdir == DER)
       pdir = IZQ;
     else
       pdir = DER;

   if (pdir == DER) //entrada al borde en sentido de las agujas del reloj
     switch(oriBor){
       case ABA:
         return LVL.NIVbor[pidBor][3]; //y_fija
       case ARR:
         return (short)(LVL.NIVbor[pidBor][3]-1); //y_fija-1
       case IZQ:
         return (short)(LVL.NIVbor[pidBor][4]-1); //y_desde-1
       case DER:
         return (short)(LVL.NIVbor[pidBor][5] - PROlen); //y_hasta - longitudPro
     }
   else //entrada al borde en sentido inverso a las agujas del reloj
    switch(oriBor){
       case ABA:
         return LVL.NIVbor[pidBor][3]; //y_fija
       case ARR:
         return (short)(LVL.NIVbor[pidBor][3]-1); //y_fija-1
       case IZQ:
         return (short)(LVL.NIVbor[pidBor][5] - PROlen); //y_hasta - longitudPro
       case DER:
         return (short)(LVL.NIVbor[pidBor][4]-1); //y_desde-1
     }
   return -1;
}


// Desc: Encuentra el borde de la zona <pidZon> más cercano a la posición
private short PRO_ObtenerCerBor(short pidZon, short posX, short posY){
    short j, maxVert=28, idVert=0, maxHorz=28, idHorz=0;

    for(short i=0; i< LVL.numTotBor; i++)
        if(LVL.NIVbor[i][0] == pidZon){
            if(LVL.NIVbor[i][1] == VERT && ((j=(short)Math.abs(LVL.NIVbor[i][3] - posX)) < maxVert) &&
              posY+2 >= LVL.NIVbor[i][4] && posY <= LVL.NIVbor[i][5]){
                maxVert = j;
                idVert = i;
            }
            if(LVL.NIVbor[i][1] == HORZ && ((j=(short)Math.abs(LVL.NIVbor[i][3] - posY)) < maxHorz) &&
               posX+2 >= LVL.NIVbor[i][4] && posX <= LVL.NIVbor[i][5]){
                maxHorz = j;
                idHorz = i;
            }
        }

    if(maxVert > maxHorz)
        return idHorz;
    else
        return idVert;
}


//------------------------------------------------------------------------ PRO_Cambiar
// Desc: Busca el borde opuesto al borde pasado por parámetro
//       la misma si no existe zona interior intermedia.
//----------------------------------------------------------------------------------------
private short PRO_ObtenerOpuBor(short pidBor, short posX, short posY, short len){
    short ini, fin;
    //Buscar borde con las siguientes condiciones:
    // a) Mismo modo
    // b) distinta orientación
    // c) con la menor pos fija más cercana a la fija actual
        // Más cercana:
        // ori ARR: mayor f < f actual
        // ori ABA: menor f > f actual
        // ori IZQ: mayor f < f actual
        // ori DER: menor f > f actual
    // d) con las posiciones variables que se solapen (total o parcialmente) con la longitud del Propulsor

    short modo = LVL.NIVbor[pidBor][1];
    short ori = LVL.NIVbor[pidBor][2];
    short fact = LVL.NIVbor[pidBor][3];

    if(modo == HORZ){
        ini = posX;
        fin = (short) (posX + len + 1);
    }
    else{
        ini = posY;
        fin = (short) (posY + len + 1);
    }

    if (ori == ABA || ori == DER){//buscar la menor f: recorrer el array en orden directo
         for(short i=0; i<LVL.numTotBor; i++)
            if(LVL.NIVbor[i][1] == modo && LVL.NIVbor[i][2] != ori)
                if(LVL.NIVbor[i][3] > fact)
                    if(LVL.NIVbor[i][4] <= fin && LVL.NIVbor[i][5] >= ini)
                        return i;
      return -1;
    }
    else{ //buscar la mayor f: recorrer el array en orden inverso
          for(short i=(short)(LVL.numTotBor-1); i>=0; i--)
            if(LVL.NIVbor[i][1] == modo && LVL.NIVbor[i][2] != ori)
                if(LVL.NIVbor[i][3] < fact)
                    if(LVL.NIVbor[i][4] <= fin && LVL.NIVbor[i][5] >= ini)
                        return i;
      return -1;

    }

}


//-------------------------------------------------------------- PRO_CambiarIni
// Desc: Inicia el cambio del propulsor al borde opuesto al que está actualmente
//-----------------------------------------------------------------------------
private void PRO_CambiarIni(){
    short newIdbor = PRO_ObtenerOpuBor(PROidbor, PROposX, PROposY, PROlen);
    //cambiar el propulsor al nuevo borde (y también su orientación)
    PROmodo = LVL.NIVbor[newIdbor][1];
    PROori = LVL.NIVbor[newIdbor][2];

    //Establecer la posición destino y el incremento automático para el
    //desplazamiento desde el borde actual hasta el nuevo borde
    ACTincX = ACTincY = 0;
    ACTdstX = ACTdstY = -1;
    if(PROmodo == HORZ){ //desplazar el propulsor verticalmente al nuevo borde
      if(LVL.NIVbor[newIdbor][3] > PROposY)
        ACTincY = 1;
      else
        ACTincY = -1;
      if(PROori == ABA)
        PROposY = LVL.NIVbor[newIdbor][3];
      else
        PROposY = (short)(LVL.NIVbor[newIdbor][3]-1);
      ACTdstY = PROposY;
    }
    else{ //desplazarlo horizontalmente al nuevo borde
      if(LVL.NIVbor[newIdbor][3] > PROposX)
        ACTincX = 1;
      else
        ACTincX = -1;
      if(PROori == IZQ)
        PROposX = (short)(LVL.NIVbor[newIdbor][3]-1);
      else
        PROposX = LVL.NIVbor[newIdbor][3];
      ACTdstX = PROposX;
    }
    PROidbor = newIdbor;

    GAMsts = PLAY_IN_PROPCHGBOR;  //Cambia subestado del juego a modo de control automático
}



// Desc: Ajusta el propulsor al borde y propaga sus efectos al sistema de
//       control del scroll.
private void PRO_AjustarAct(){
    PRO_CalcLen();
    PROposX = ACTposX;
    PROposY = ACTposY;
    PRO_AjustarBor();
    ACTacuIncX = (short)(PROposX - ACTposX);
    ACTacuIncY = (short)(PROposY - ACTposY);
}

private void PRO_AjustarBor(){
    short sit, dir;

    //Comprobar si el propulsor queda parcialmente fuera del borde. En ese caso ajustarlo al límite más cercano.
    if ((sit = PRO_InBor(PROidbor, PROlen, PROposX, PROposY)) != 0){
        short ori = LVL.NIVbor[PROidbor][2];
        if(ori==ABA || ori == IZQ){
          if (sit == 1)
            dir = DER; //si zona 0 IZQ sino DER
          else
            dir = IZQ; //si zona 0 DER sino IZQ
        }else{
          if (sit == 1)
            dir = IZQ;
          else
            dir = DER;
        }
        PROposX = PRO_CalcPosXBor(PROidbor, dir, (short)1); //calcula primera posición X del borde para colocar el propulsor al inicio/fin del borde en función del sentido por el que se viene
        PROposY = PRO_CalcPosYBor(PROidbor, dir, (short)1);
    }

}



//-------------------------------------------------------------- PRO_CambiarFin
// Desc: Finaliza el cambio del propulsor al borde opuesto ajustándolo al
//       límite del mismo en el caso de que quede parcialmente fuera del borde.
// Return: Booleano que indica si se ha cumplido regla de pos en borde del prop.
//-----------------------------------------------------------------------------
private boolean PRO_CambiarFin(){
    PRO_AjustarBor();
    //los incrementos a sumar a ACTniv deben dar como resultado que ACTniv coincida con la posición del propulsor
    ACTacuIncX = (short)(PROposX - ACTposX); //[PTE]: Ajustar este incremento en función de la relación final entre PROpos y ACTniv
    ACTacuIncY = (short)(PROposY - ACTposY);
    HUDbIni = true; //evita que el cambio pegado al HUD deje rastro
    //la posición de PIEtmp quedará actualizada en CUR_Tick

    //Chequeo de las Reglas-Objetivo asociadas al borde del Propulsor
    return PRO_ChkRulObj(TIPOBJ_BORPROP);
}


//--------------------------------------------------------------- PRO_LanzarPie
// Desc: Iniciar lanzamiento de pieza desde el Propulsor
//       (Se asume pieza asociada a propulsor)
// Update:
// - PIEtmp: Datos de la pieza
// - PROposY: Posición Y (sup) del propulsor
// Return_
// - bColi: Booleano que indica si es posible lanzar pieza o si no lo es por colisión de pieza con piezas colocadas en tablero
//----------------------------------------------------------------------------------------
 private boolean PRO_LanzarPie(){
    if(CURcont[0] !=0){
        //Chequear si puede ser lanzado
        if(! NIV_ChkColPie(PIEtmp[2], PIEtmp[3], (byte)PIEtmp[4], (byte)PIEtmp[5])){
          CURbHayPieza = false;
          //Actualizar datos de pieza lanzada
          PIEtmp[1] = PIE_EN_MOV; // Pieza en movimiento (desplazándose)
          PIEtmp[7] = PROori;
          //Contabiliza
          CUR_DecCont(0);
          //Activar siguiente estado de la FSM PIETMP
          PIETMPsts = STSPIE_EN_MOV;
//#if CFGtipoSnd == 2
          if(COMBIMidlet.M.MENbSnd)
            MUS.TocaMusica(0);
//#endif          
          return true;
        }
        //#if CFGtipoSnd == 2
        else
        if (COMBIMidlet.M.MENbSnd)
          MUS.TocaMusica(1); //System.out.println("FX =>> No es posible lanzar pieza. Hay colisión");
        //#endif
    }
    else
      FX_Add( (short) 6, (short) -1, (short)43); //System.out.println("FX =>> Contador de cursor Lanzamiento a 0.");
    return false;
}


//---------------------------------------------------------------------------- PRO_CalcLen
// Desc: Calcula longitud del propulsor para albergar la pieza temporal actual o
//       le asigna la longitud mínima si no hay pieza asociada.
//----------------------------------------------------------------------------------------

private void PRO_CalcLen(){
    if(CURbHayPieza){
        if (PROmodo == HORZ)
            PROlen = TIPPIEbb[PIEtmp[4]-1][PIEtmp[5]*2];
        else
            PROlen = TIPPIEbb[PIEtmp[4]-1][PIEtmp[5]*2+1];
    }
    else
        PROlen = 1;
}

//---------------------------------------------------------------------------- PRO_Dibujar
// Desc: Dibujar propulsor en las coordenadas locales de pantalla pasadas por parámetro.
//----------------------------------------------------------------------------------------

private void PRO_Dibujar(short pcX, short pcY){
    short x,y, idx=0,pieW, pieH, proW, proH, numimg;

    //Determinar ancho y alto máximo de propulsor e indice sobre array de graficos del Propulsor
    if (PROmodo == HORZ){
      proW = 5;
      proH = 2;
      idx = 0;
    }
    else{
      proW = 2;
      proH = 5;
      idx = 60;
    }
    if (PROori == ABA || PROori == IZQ)
        idx += 30;
    idx += (PROlen-1)*10;


    //Dibujar desde [pcX,pcY] hasta [pcX+proW*anchoTile, pcY+proH*altoTile]
    pcX = (short) (pcX * NUMPIXELS_TILEX);
    y = (short)(pcY * NUMPIXELS_TILEY);
    for (int j = 0; j < proH; j++) {
      x = pcX;
      for (int i = 0; i < proW; i++) {
        numimg =  PROval[idx];
        if(numimg != -1)
          SCRdg.drawImage(GAMspr[numimg], x, y, Graphics.TOP | Graphics.LEFT, PROman[idx]);
        idx++;
        x += NUMPIXELS_TILEX;
      }
      y += NUMPIXELS_TILEY;
    }
}

//*****************************************************************************
//************************************************************************* NIV
//*****************************************************************************


//-------------------------------------------------------------------- NIV_Init
// Desc: Inicializa datos del nivel después de estar cargados en memoria los
//       datos del mismo. LLamado al iniciar nivel dentro de una partida.
//-----------------------------------------------------------------------------
 private void NIV_Init(boolean pstart){
//  Inicialización datos de mapa de juego
    NIVnumTilW = LVL.numTilesW;
    NIVnumTilH = LVL.numTilesH;
    NIV_UpdMapObs(); //Mapea datos de Obstáculos sobre el nivel de juego
    NIV_InitMap(); //Iniciliza estructura de datos de piezas y combis por defecto

//  Reset sistema de FX
    for(int i=0;i<NUMFX;i++)
      FXdat[i][2] = -1; //FX desactivados

//	Datos del CURsor
    CURmodo = LVL.CURmodoDef;
    PROposX = CURposX = ACTposX = LVL.CURxDef;
    PROposY = CURposY = ACTposY = LVL.CURyDef;
    PROidbor = LVL.PROidBorDef;
    PROmodo = LVL.NIVbor[PROidbor][1];
    PROori = LVL.NIVbor[PROidbor][2];

    if(pstart)
      GAMscore = GAMround = 0;
    //Carga de valore en contadores de modo cursor (Acumulación en modo arcade, asignación en resto modos))
    for (int i = 0; i < LVL.CURcontInit.length; i++)
      if(pstart || LVL.idModo!=1 || LVL.CURcontInit[i]==-1)
        CURcont[i] = LVL.CURcontInit[i];
      else
        CURcont[i] += LVL.CURcontInit[i];

//  Preparación de secuencia de aparición de piezas
    GAMpieNumRepSec = LVL.PIEnumRepSec;
    TIMbPie = LVL.PIEdur==0?false:true; //Tiempo de colocación de pieza
    if (TIMbPie) {
      if (GAMround == 0) //Si es la 1ra vuelta que vamos a jugar al modo arcade
        TIMpieDur = LVL.PIEdur;
      else if (GAMround == 1) //2a vuelta
        TIMpieDur -= 10000;
      else
        TIMpieDur = 10000; //resto vueltas
    }

    CURbHayPieza = false;
    CURbHayCombi = false;
    GAMpieLastId = 0;
    GAM_GetSigPie(); //genera 1a pieza
    GAM_GetSigPie(); //genera 2a pieza
    GAM_GetSigPie(); //genera 3a pieza


//Inicializar estados de cumplimiento de objetivos
    GAMnumTotObj = 0;
    GAMobjSts = null;
    if((GAMnumRulObj=LVL.OBJnumRul)>0){
      GAMobjSts = new byte[GAMnumRulObj * 2];
      for (int i = 0; i < GAMnumRulObj; i++) {
        GAMnumTotObj += GAMobjSts[i * 2] = LVL.OBJrul[i][1]==0?(byte)0:(byte)(LVL.OBJrul[i][1]+GAMround*2); //Copiar valor objetivo e incrementarlos segun vueltas de jueg
        GAMobjSts[i*2+1] = 0; //Se inicializa a 0 veces cumplido cada objetivo
      }
    }
    GAMobjProg = 0;


//Datos HUD y pantalla texto intro
    GAMbHUDon = LVL.sitPag == 0?false:true;
    GAMbNivIntro = true; //Arrancamos pantalla de introduccion al nivel
//    //#if CFGfnt == 0 
    //TXTidLinAct = 0; //KKTXT
    //TXT_LinUpd(0); //KKTXT
//    //#else
    TXTiniPos = 0;
    TXTnumCars = TXT_ObtLongTextoFwd(LVL.HUDstr, 0, 56, DEVscrPxlW-12);
    TXTsig = TXTnumCars == LVL.HUDstr.length()? false: true;
    TXTant = false;
//    //#endif
    HUDbIni = true;
    HUDbScore = HUDbProg = HUDbCurCont = HUDbPie = false;

//Inicializa estados de juego
    GAMbChkFinNiv = true;
    actorAccion = last_actorAccion = disc_actorAccion = STOP;
    PIETMPsts = STSPIE_EN_HUD;
    PIETMPsbSts = 1; //La pieza actual ya ha sido generada
    PIEtmp[0] = INACTIVA;
    lastTime = 0;

}




//--------------------------------------------------------------- NIV_ChkColPie
// Desc: Comprueba si existe colisión al colocar una nueva pieza del tipo,
//       orientación y posición pasados por parámetro.
// Param:
// - pPosX: posición X izq donde se va a intentar colocar la pieza
// - pPosY: posición Y sup donde se va a intentar colocar la pieza
// - pTip: tipo de pieza a colocar
// - pOri: Orientación de la pieza a colocar
// Return:
// - bColi: booleano que indica si existe colisión
//-----------------------------------------------------------------------------
 private boolean NIV_ChkColPie(short pPosX, short pPosY, byte pTip, byte pOri){
    short pieW, pieH, i, j;

    TIPPIE_ObtenerVal(pTip, pOri);
    pieW = TIPPIEbb[pTip-1][pOri*2];
    pieH = TIPPIEbb[pTip-1][pOri*2+1];

    if(pPosY+pieH-1>=NIVnumTilH || pPosX+pieW-1>= NIVnumTilW)
      return true;
    for(i=0; i<pieW; i++)
        for(j=0;j<pieH; j++)
          if (TIPPIErefval[j * 3 + i] != 0 &&  NIVidPieza[ (pPosY + j) * NIVnumTilW + pPosX + i] != -1)
                return true;
    return false;
 }

//--------------------------------------------------------------- NIV_ChkColPie
// Desc: Comprueba si existe colisión al cambiar de posición una pieza
//       (Lógicamente la pieza a mover no genera colisión consigo misma)
// Param:
// - pIdPie: Identificador de la pieza a mover
// - pPosX, pPosy: posición destino a la que se quiere mover la pieza
// Return:
// - Booleano que indica si hay colisión con otra pieza del tablero
//-----------------------------------------------------------------------------
 private boolean NIV_ChkColPie(short pIdPie, short pPosX, short pPosY){
    byte tip = PIEdat[pIdPie][1];
    byte ori = PIEdat[pIdPie][2];
    TIPPIE_ObtenerVal(tip, ori);
    short pieW = TIPPIEbb[tip-1][ori*2];
    short pieH = TIPPIEbb[tip-1][ori*2+1];

    for (byte i = 0; i < pieW; i++)
      for (byte j = 0; j < pieH; j++)
        if (TIPPIErefval[j * 3 + i] != 0) {
          short idx = (short)((pPosY + j) * NIVnumTilW + pPosX + i);
          if (NIVidPieza[idx] != -1 && NIVidPieza[idx] != pIdPie)
            return true;
        }
    return false;
 }


//--------------------------------------------------------------- NIV_ChkColCom
// Desc: Comprueba si existe colisión al desplazar combi de ancho/alto <pW,pH>
//       a la posición <pPosX, pPosY> en el sentido <pOri>
//-----------------------------------------------------------------------------
 private boolean NIV_ChkColCom(short pPosX, short pPosY, byte pW, byte pH, byte pOri){
    short i, j, offset;

    switch(pOri){
      case ARR:
        offset = (short)(pPosY * NIVnumTilW);
        for(i=0;i<pW;i++)
          if(NIVidPieza[offset + pPosX + i] != -1)
            return true;
        break;
      case ABA:
        offset = (short)((pPosY + pH - 1)* NIVnumTilW);
        for(i=0;i<pW;i++)
          if(NIVidPieza[offset + pPosX + i] != -1)
            return true;
        break;
      case IZQ:
        offset = (short) (pPosY * NIVnumTilW + pPosX);
        for(j=0;j<pH;j++)
          if(NIVidPieza[offset + j * NIVnumTilW] != -1)
            return true;
        break;
      case DER:
        offset = (short) (pPosY * NIVnumTilW + pPosX + pW - 1);
        for(j=0;j<pH;j++)
          if(NIVidPieza[offset + j * NIVnumTilW] != -1)
            return true;
    }
    return false;
 }

//--------------------------------------------------------------- NIV_ChkColSup
// Desc: Comprueba si existe colisión de la superficie pasada por parámetro
//       con los bordes de todas las zonas del escenario
//-----------------------------------------------------------------------------

private boolean NIV_ChkColSup(short supiniX, short supiniY, short supfinX, short supfinY){
    for(short z=0; z<LVL.NIVzon.length; z++)
        for(short i=0; i<LVL.numTotBor; i++)
            if(LVL.NIVbor[i][0] == z){
                if (LVL.NIVbor[i][1] == HORZ){
                    if(LVL.NIVbor[i][3] >= supiniY && LVL.NIVbor[i][3] <= supfinY) //  col vert
                       if((supiniX < LVL.NIVbor[i][4] && supfinX >= LVL.NIVbor[i][4]) ||
                          (supiniX <= LVL.NIVbor[i][5] && supfinX >= LVL.NIVbor[i][5]) ||
                          (supiniX >= LVL.NIVbor[i][4] && supfinX <= LVL.NIVbor[i][5]))
                               return true;
                }
                else
                    if(LVL.NIVbor[i][3] >= supiniX && LVL.NIVbor[i][3] <= supfinX) //  col horz
                      if((supiniY < LVL.NIVbor[i][4] && supfinY >= LVL.NIVbor[i][4]) ||
                         (supiniY <= LVL.NIVbor[i][5] && supfinY >= LVL.NIVbor[i][5]) ||
                         (supiniY >= LVL.NIVbor[i][4] && supfinY <= LVL.NIVbor[i][5]))
                           return true;
            }
    return false;
}




//-------------------------------------------------------------- NIV_ColocarPie
// Desc: Coloca la pieza temporal PIEtmp en las posiciones del área de juego
//       pasadas por parámetro y comprueba si se ha generado combi
//       (Asume que no hay colisión)
// Param:
// - pPosX, pPosY: posición sup-izq del BB de la pieza a colocar.
// Update:
// - PIEdat (crea nueva pieza una vez colocada en tablero)
// - NIVidPieza, NIVnumPart (actualiza mapa de piezas del nivel)
// - PIEtmp (desactiva la pieza temporal colocada)
//-----------------------------------------------------------------------------
 private void NIV_ColocarPie(short pPosX, short pPosY){
   short idPie, pieW, pieH;
   byte tip, ori, col;
   boolean bCombi;

   tip = (byte)PIEtmp[4];
   ori = (byte)PIEtmp[5];
   col = (byte)PIEtmp[6];
   //Crear la pieza en la lista de piezas del tablero
   idPie = PIE_GenerarPie(tip, ori, col);
   NIV_UpdMapPie(idPie, pPosX, pPosY, tip, ori); //Actualizar datos de piezas y sus partes del NIVel

   //Chequeo de las Reglas-Objetivo asociadas a la colocación de una pieza
   PIE_ChkRulObj(idPie, tip, ori, col, pPosX, pPosY);

   //Desactivar pieza temporal
   PIEtmp[0] = INACTIVA;
   //Chequear generación de combis tras la colocación de pieza
   COMtmp[6] = col; //PTE?????? guarda el color de la pieza para poder calcular color del combi a detectar
 }

//--------------------------------------------------------------- NIV_UpdMapObs
// Desc: Comprueba si hay obstáculos a aplicar al nivel actual
//-----------------------------------------------------------------------------
private void NIV_UpdMapObs(){
   if(LVL.NIVobs != null){
     for (int i = 0; i < LVL.NIVobs.length/3; i++) //Para cada obstáculo de la página del nivel
         NIV_UpdMapObs(LVL.NIVobs[i*3], LVL.NIVobs[i*3+1], LVL.NIVobs[i*3+2]);
     CIO.OBS_Unload();
   }
}

// Desc: Actualiza Mapa del nivel con datos de obstáculo <pidPos> colocandolo
//       en la posición <pPosX,Y>
private void NIV_UpdMapObs(short pidObs, short pPosX, short pPosY){
   CIO.OBS_DatLoad(pidObs);

   int idx= pPosY*NIVnumTilW+pPosX;
   int idx2=0;
   for(int j=0; j<CIO.OBSh; j++){
     for(int i=0; i<CIO.OBSw; i++)
       LVL.NIVmap[idx + i] = CIO.OBSdat[idx2++];
     idx +=NIVnumTilW;
   }
}


//------------------------------------------------------------------ NIV_SetMap
private void NIV_InitMap(){
   int idx;
   //Inicializa datos Posiciones de Tablero en función de tiles en NIVmap
   for (int j = 0; j < NIVnumTilH; j++)
     for (int i = 0; i < NIVnumTilW; i++) {
       idx = j * NIVnumTilW + i;
       if (LVL.NIVmap[idx] >= LVL.idBorPixlim)
         NIVidPieza[idx] = -2; //BORDE
       else {
         NIVidPieza[idx] = -1; //sin pieza
         NIVnumPart[idx] = 0;
       }
     }
   //Resetea valores de PIEdat y COMdat
   PIE_ResetAll();
   COM_ResetAll();
   PIE_LoadDef();
   COM_LoadDef();
 }


//--------------------------------------------------------------- NIV_UpdMapPie
//Actualizar datos de piezas y sus partes del NIVel
//-----------------------------------------------------------------------------
private void NIV_UpdMapPie(short pIdPie, short pPosX, short pPosY, byte pTip, byte pOri){
   short i, j, pieW, pieH;
   byte idNumPart;

   pieW = TIPPIEbb[pTip - 1][pOri * 2];
   pieH = TIPPIEbb[pTip - 1][pOri * 2 + 1];
   TIPPIE_ObtenerVal(pTip, pOri);
   for (j = 0; j < pieH; j++)
     for (i = 0; i < pieW; i++) {
       idNumPart = TIPPIErefval[j * 3 + i];
       if (idNumPart > 0) {
         NIVidPieza[ (pPosY + j) * NIVnumTilW + pPosX + i] = pIdPie;
         NIVnumPart[ (pPosY + j) * NIVnumTilW + pPosX + i] = idNumPart;
       }
     }
   VISbUpd = true;
 }

//--------------------------------------------------------------- NIV_UpdMapCom
// Desc: Actualizar datos de las piezas (y sus partes) del Combi <pidCom>
//       el cual se desplaza una pos en la orientación <pOri>
//       (Se asume que no hay obstáculos en el sentido del desplazamiento)
//-----------------------------------------------------------------------------
private void NIV_UpdMapCom(short pidCom, byte pOri){
   short iniX, iniY, finX, finY,i,j, pos;

   iniX = COMdat[pidCom][3];
   iniY = COMdat[pidCom][4];
   finX = (short)(iniX + COMdat[pidCom][5] -1);
   finY = (short)(iniY + COMdat[pidCom][6] -1);

   if (pOri== IZQ || pOri == DER){
       for (j = iniY; j <= finY; j++) {
         if (pOri == IZQ) {
           pos = (short) (j * NIVnumTilW);
           for (i = iniX; i <= finX; i++){
             NIVidPieza[pos + i - 1] = NIVidPieza[pos + i]; //desp izq
             NIVnumPart[pos + i - 1] = NIVnumPart[pos + i]; //desp izq
           }
           NIVidPieza[pos + finX] = -1;
           NIVnumPart[pos + finX] = -1;
         }
         else {
           pos = (short) (j * NIVnumTilW);
           for (i = finX; i >= iniX; i--){
             NIVidPieza[pos + i + 1] = NIVidPieza[pos + i]; //desp der
             NIVnumPart[pos + i + 1] = NIVnumPart[pos + i]; //desp der
           }
           NIVidPieza[pos + iniX] = -1;
           NIVnumPart[pos + iniX] = -1;
         }
       }
   }
   else{
     for (i = iniX; i <= finX; i++) {
       if (pOri == ARR) {
         for (j = iniY; j <= finY; j++){
           NIVidPieza[ (j - 1) * NIVnumTilW + i] = NIVidPieza[j * NIVnumTilW +i]; //desp arr
           NIVnumPart[ (j - 1) * NIVnumTilW + i] = NIVnumPart[j * NIVnumTilW +i]; //desp arr
         }
         NIVidPieza[finY * NIVnumTilW+i] = -1;
         NIVnumPart[finY * NIVnumTilW+i] = 0;
       }
       else {
         for (j = finY; j >= iniY; j--){
           NIVidPieza[ (j + 1) * NIVnumTilW + i] = NIVidPieza[j * NIVnumTilW +i]; //desp aba
           NIVnumPart[ (j + 1) * NIVnumTilW + i] = NIVnumPart[j * NIVnumTilW +i]; //desp aba
         }
         NIVidPieza[iniY * NIVnumTilW + i] = -1;
         NIVnumPart[iniY * NIVnumTilW + i] = -1;
       }
     }
   }
   VISbUpd=true;
 }



//------------------------------------------------------------- NIV_ResetMapPie
// Desc: Resetear datos de piezas y sus partes del NIVel
//-----------------------------------------------------------------------------
private void NIV_ResetMapPie(short pPosX, short pPosY, byte pTip, byte pOri){
   byte idNumPart;

   TIPPIE_ObtenerVal(pTip, pOri);
   short pieW = TIPPIEbb[pTip - 1][pOri * 2];
   short pieH = TIPPIEbb[pTip - 1][pOri * 2 + 1];

   for (byte j = 0; j < pieH; j++)
     for (byte i = 0; i < pieW; i++) {
       idNumPart = TIPPIErefval[j * 3 + i];
       if (idNumPart > 0) {
         NIVidPieza[(pPosY + j) * NIVnumTilW + pPosX + i] = -1;
         NIVnumPart[(pPosY + j) * NIVnumTilW + pPosX + i] = 0;
       }
     }
   VISbUpd=true;
}


//------------------------------------------------------------- NIV_ResetMapCom
// Desc: Resetear datos de piezas y sus partes del NIVel
//-----------------------------------------------------------------------------

private void NIV_ResetMapCom(short piniX, short piniY, short pW, short pH){
   short finX, finY, i, j, pos1, pos2;

   finX = (short)(piniX + pW -1);
   finY = (short)(piniY + pH -1);

   for (j = piniY; j <= finY; j++) {
        pos1 = (short) (j * NIVnumTilW);
        for (i = piniX; i <= finX; i++){
            pos2 = (short)(pos1 + i);
            NIVidPieza[pos2] = -1;
            NIVnumPart[pos2] = 0;
        }
   }
   VISbUpd=true;
}


//----------------------------------------------------------- COMsup_ExistePie
// Desc: Comprueba si existen piezas 'sueltas' (sin estar contenidas en combi)
//       en la superficie-combi pasada por parámetro
//----------------------------------------------------------------------------
private boolean COMsup_ExistePie(short piniX, short piniY, short pW, short pH){
    short i,j,pos1,pos2, pfinX, pfinY;

    pfinX = (short)(piniX + pW - 1);
    pfinY = (short)(piniY + pH - 1);
    for(j=piniY; j<=pfinY; j++){
        pos1 = (short) (j*NIVnumTilW);
        for(i=piniX; i<= pfinX; i++){
            pos2 = (short)(pos1+i);
            if(PIEdat[NIVidPieza[pos2]][4] == -1)
                return true;
        }
    }
    return false;
}

//----------------------------------------------------------- COMsup_GetNumCol
// Desc: Obtiene el número de colores diferentes de las piezas que conforman
//       una superficie-combi
//----------------------------------------------------------------------------
private short COMsup_GetNumCol(short piniX, short piniY, short pW, short pH){
    short i,j,pos1,pos2, pfinX, pfinY;
    byte col[] = new byte[NUMCOLORES];

    pfinX = (short)(piniX + pW - 1);
    pfinY = (short)(piniY + pH - 1);
    for(j=piniY; j<=pfinY; j++){
        pos1 = (short) (j*NIVnumTilW);
        for(i=piniX; i<= pfinX; i++){
            pos2 = (short)(pos1+i);
            col[PIEdat[NIVidPieza[pos2]][3]-1] = 1;
        }
    }
    for(i=0,j=0;i<NUMCOLORES;i++)
        j= (short)(j+ col[i]);
    col = null;
    return j;
}

//----------------------------------------------------------- COMsup_GetTipPie
// Desc: Obtiene el tipo de piezas que conforman una superficie-combi
// Param:
// Objetivo - 0: devolver número de tipos de piezas distintos
//          - >0 (1,2,3,5,7,8): contiene el tipo de pieza? devuelve 1 (T) o 0 (F)
//----------------------------------------------------------------------------
private short COMsup_GetTipPie(short piniX, short piniY, short pW, short pH, short pObj){
    short i,j,pos1,pos2, pfinX, pfinY;
    byte tippie[] = new byte[NUMTIPPIE];

    pfinX = (short)(piniX + pW - 1);
    pfinY = (short)(piniY + pH - 1);
    for(j=piniY; j<=pfinY; j++){
        pos1 = (short) (j*NIVnumTilW);
        for(i=piniX; i<= pfinX; i++){
            pos2 = (short)(pos1+i);
            tippie[PIEdat[NIVidPieza[pos2]][1]-1] = 1;
        }
    }
    //Assumption: Por simplicidad se asume que las piezas inversas son iguales
    //(3y4 se conjuntan en tipo 3, y 5y6 en tipo 5)
    if(tippie[2]==1 || tippie[3]==1){
      tippie[2]=1;
      tippie[3]=0;
    }
    if(tippie[4]==1 || tippie[5]==1){
      tippie[4]=1;
      tippie[5]=0;
    }

    //Calcular resultado en función de objetivo solicitado
    if (pObj == 0) //calcular número de tipos de pieza diferentes contenidos en combi
      for (i = 0, j = 0; i < NUMTIPPIE; i++)
        j = (short) (j + tippie[i]);
    else //Comprobar si existe la pieza de tipo <pObj>-1 en el combi
      j = tippie[pObj-1];

    tippie = null;
    return j;
}


//----------------------------------------------------------- COMsup_GetNumPie
// Desc: Obtiene el número de piezas que conforman una superficie-combi
//       (No tiene en cuenta que las piezas pertenezcan a su vez a otro combi
//        hijo incluido en la superficie-combi evaluada)
//----------------------------------------------------------------------------
private short COMsup_GetNumPie(short piniX, short piniY, short pW, short pH){
    short i,j,k,pos1,pos2, pfinX, pfinY, numPie=0;
    short pie[] = new short[pW*pH];
    boolean bFound;

    pfinX = (short)(piniX + pW - 1);
    pfinY = (short)(piniY + pH - 1);
    for(j=piniY; j<=pfinY; j++){
        pos1 = (short) (j*NIVnumTilW);
        for(i=piniX; i<= pfinX; i++){
            bFound = false;
            pos2 = (short)(pos1+i);
            for(k=0; k<numPie; k++) // para todas las piezas ya encontradas
                if(pie[k] == NIVidPieza[pos2]) //si la pieza encontrada ya está registrada continuamos
                    bFound = true;
            if(!bFound){
                pie[k] = NIVidPieza[pos2];
                numPie++;
            }
        }
    }
    pie=null;
    return numPie;
}


//----------------------------------------------------------- COMsup_GetNumCom
// Desc: Obtiene info sobre los combis hijo de nivel superior que conforman
//       una superficie-combi
// Param
// - Objetivo 1: Obtener número de combis hijo
//            2: Comprobar si todos los combis hijo son iguales.
//----------------------------------------------------------------------------
private short COMsup_GetNumCom(short piniX, short piniY, short pW, short pH, short pObj){
    short i,j,k,pos1,pos2, pfinX, pfinY, numCom=0, idCombi=0;
    short com[] = new short[pW*pH];
    boolean bFound;

    pfinX = (short)(piniX + pW - 1);
    pfinY = (short)(piniY + pH - 1);
    for(j=piniY; j<=pfinY; j++){
        pos1 = (short) (j*NIVnumTilW);
        for(i=piniX; i<= pfinX; i++){
            bFound = false;
            pos2 = (short)(pos1+i);
            if((idCombi = COM_ObtenerComRaiz(PIEdat[NIVidPieza[pos2]][4])) == -1)
              continue;
            for(k=0; k<numCom; k++) // para todos las combis ya encontradas
                if(com[k] == idCombi)  //si el combi encontrado ya está registrado continuamos
                    bFound = true;
            if(!bFound){
                com[k] = idCombi;
                numCom++;
            }
        }
    }


    if(pObj != 1) //si se pide comprobar que todos los combis hijo son iguales
      for (i = 1; i < numCom; i++)
        if ( (COMdat[com[i]][5] != COMdat[com[i - 1]][5]) ||
            (COMdat[com[i]][6] != COMdat[com[i - 1]][6])){
         com = null;
         return -1;
       }

    com = null;
    return numCom;
}




//------------------------------------------------------------------- COM_Crear
private void COM_Crear(){
    COMtmp[0] = COM_GenerarCOM( (byte) COMtmp[2], (byte) COMtmp[3], (byte) COMtmp[4], (byte) COMtmp[5],(byte) COMtmp[1], (byte) COMtmp[6]);
    PIE_AsignarCombi((byte)COMtmp[0]); //Asigna el id del combi a las piezas que lo conforman
    COM_AsignarCombi( (byte) COMtmp[0]); //Asigna el id del combi a los combis padres que sólo contienen otros combis (q no contienen piezas)
}


//------------------------------------------------------------- COM_CalcPunGen
// Desc: Calcula puntuación otorgada por creación de combi en función de las
//       características del mismo.
//----------------------------------------------------------------------------
private short COM_CalcPunGen(short pidCom){
    int punt;

    punt = (LVL.PUNbase+GAMround*50) * COMdat[pidCom][5] * COMdat[pidCom][6]; //100 * pW * pH;
    if(COMdat[pidCom][8] != 0)  //Puntos extra en función del color
        punt *= 2;
    if(COMdat[pidCom][7] == 0)  //Puntos extra si forma cuadrada
      punt += 250;
      //[PTE]: ptos extras si nivel 1 y area mayor que X
    if(punt >32000)
      punt = 32500;
    return (short)punt;
}


//------------------------------------------------------------ COM_CalcCurBonus
//Desc: Cálculo de incremento de contador de uso de modos de Cursor otorgados
//       como bonus por generar combis con ciertas caracteristicas.
//Update: Actualiza vector bonus de modos cursor <CURcontBon>
//----------------------------------------------------------------------------
private byte COM_CalcCurBonus(short pidCom){
    short w = COMdat[pidCom][5] ;
    short h = COMdat[pidCom][6] ;
    int a = w*h;

    int offset=0;
    if(COMdat[pidCom][8]==0) //Si multicolor
      offset = 12;

    CURcontBon = 0; //reset del vector de bonus
    for(int i=offset; i<offset+4; i++)
      if (LVL.OBJrulBonus[i] == 0)
        break;
      else
      if ( (w >= LVL.OBJrulBonus[i] && h >= LVL.OBJrulBonus[i]) ||
          a >= LVL.OBJrulBonus[i + 4]) {
        CURcontBon = (byte) (CURcontBon | LVL.OBJrulBonus[i + 8]); //OR lógico para activar flags de incrementos de modos cursor
        return LVL.OBJincBonus;
      }
    return 0;
}

/*----------------------------------------------- SISTEMA DE BUSQUEDA DE COMBIS
OBJETIVO: Buscar la superficie con área mínima que contiene a una pieza o combi.
PROC: Para todas las áreas posibles en las que está incluida la pieza/combi
      (desde area de piez/combi hasta área de tablero de juego) comprobar si
      todas las superficies con esa área contienen un combi.
OPTIMIZACIÓN: Condiciones de corte del sistema de búsqueda de combis
Corte 1.- A nivel de Área
   No es necesario comprobar una superficie S mayor a la superfice máxima en la
   que no hay conexión de piezas (i.e. hay huecos, u obstáculos)
Corte 2.- A nivel de Superficie (supWxsupH) con área A
   No es necesario comprobar superficies WxH en las que W es tan grande que
   hace imposible encontrar el área buscada.
Corte 3.- A nivel de lado de Superficie (SupW)
   La sup sólo se chequea 1 vez para un mismo valor SupW de las sup SupWxSupH
-----------------------------------------------------------------------------*/


//----------------------------------------------------- COM_DetectarCombiMaxSup
// Desc: Identifica la superficie máxima que puede transformarse en combi a
//       partir del BB de una pieza o de un combi ya existente.
//----------------------------------------------------------------------------
private void COM_DetectarCombiMaxSup(short piniX, short piniY, byte pW, byte pH){
  boolean bFound=false;
  int pos1;

  COMsuplimXini=0;
  COMsuplimXfin=(short)(NIVnumTilW-2);
  COMsuplimYini=0;
  COMsuplimYfin=(short)(NIVnumTilH-2);

  if(piniY+pH >= NIVnumTilH)
    pH -= NIVnumTilH-piniY;

  if(piniX+pW >= NIVnumTilW)
    pW -= NIVnumTilW-piniX;


  //Buscar Y superior
  for(int j=piniY-1; j>0; j--){
    pos1 = (short)(j*NIVnumTilW);
    for (short i=0; i<pW; i++)
      if(NIVidPieza[pos1+piniX+i] < 0){
        COMsuplimYini = (short)j;
        bFound= true;
        break;
      }
    if(bFound)
      break;
  }

  //Buscar y inferior
  bFound=false;
  for(int j=piniY+pH; j<NIVnumTilH; j++){
    pos1 = (short)(j*NIVnumTilW);
    for (short i=0; i<pW; i++)
      if(NIVidPieza[pos1+piniX+i] < 0){
        COMsuplimYfin = (short)j;
        bFound= true;
        break;
      }
    if(bFound)
      break;
  }

  //Buscar X izquierda
  bFound=false;
  for(int i=piniX-1; i>0; i--){
    for (short j=0; j<pH; j++)
      if(NIVidPieza[(piniY+j)*NIVnumTilW+i] < 0){
        COMsuplimXini = (short)i;
        bFound= true;
        break;
      }
    if(bFound)
      break;
  }

  //Buscar X derecha
  bFound=false;
  for(int i=piniX+pW; i<NIVnumTilW; i++){
    for (short j=0; j<pH; j++)
      if(NIVidPieza[(piniY+j)*NIVnumTilW+i] < 0){
        COMsuplimXfin = (short)i;
        bFound= true;
        break;
      }
    if(bFound)
      break;
  }


}
//-------------------------------------------------------- COM_DetectarCombiPie
// Desc: Detecta si se ha generado un combi tras la colocación de una pieza en
//       la posición <piniX,Y> de tipo <ptip> y orientación <pori>
// Param:
// - piniX, piniY: Posición X,Y sup-izq de inicio del BB de la pieza.
// - ptip: Tipo de la pieza
// - pori: Orientación de la pieza
//-----------------------------------------------------------------------------
private boolean COM_DetectarCombiPie(short piniX, short piniY, byte ptip, byte pori){
   short  minA, maxA, area, supW, supH; //IMPORTANTE: No caben en un byte
   short pieW, pieH, limChk;

   pieW = TIPPIEbb[ptip - 1][pori * 2];
   pieH = TIPPIEbb[ptip - 1][pori * 2 + 1];
   COM_DetectarCombiMaxSup(piniX, piniY, (byte)pieW, (byte)pieH);

   minA = (short) Math.max((int)(pieW * pieH), 4);
   maxA = (short)((COMsuplimXfin-COMsuplimXini-1) * (COMsuplimYfin-COMsuplimYini-1));
   limChk = NIVnumTilW>NIVnumTilH?NIVnumTilW:NIVnumTilH;

   //Generar todas las superficies con área comprendida entre minA y minB
   //(buscar superficies genéricas <supW>*<supH> dónde <supH> es mayor o igual que <supW>)
   for (area = minA; area <= maxA; area++) {
     for (supW = 1; supW < area; supW++) { //Ancho de área
       if ((supW * 2 > area) || supW > limChk)
         break; //Corte_2: no existe superficie con lado igual o mayor que <supW> que tenga área <area>
       else {
         supH = supW;
         while ((supW * supH < area) && supH < limChk)
           supH++;
         if (supW * supH == area) { // Encontrada superficie genérica <supW>*<supH> con área <area>
           //System.out.println("PIE-AREA a chk: " + area + "  supW=> " + supW + "  supH=> " + supH);
           // Comprobar si en dicha superficie existe un combi
           if (COM_ExisteCombiEnArea(piniX, piniY, pieW, pieH, supW, supH))
             return true; //Corte_3: La sup sólo se chequea 1 vez para un mismo valor a del binomio supW*supH
         }
       }
     }//Fin chequeo SupW de Superfice de área
   }//Fin chequeo AREA
   return false;
}

//------------------------------------------------------------ COM_DetectarCombi
// Desc: Detecta si se genera un nuevo combi tras la generación previa de
//       otro combi <pidCom>
// Param:
// - piniX, piniY: Posición X,Y sup-izq de inicio del BB de la pieza.
// - ptip: Tipo de la pieza
// - pori: Orientación de la pieza
//-------------------------------------------------------------------------------
private boolean COM_DetectarCombiCom(short pidCom){
   short  minA, maxA, area, supW, supH; //IMPORTANTE: No caben en un byte
   short limChk;
   byte iniColCom;

   short iniX = COMdat[pidCom][3];
   short iniY = COMdat[pidCom][4];
   short comW = COMdat[pidCom][5];
   short comH = COMdat[pidCom][6];
   COMtmp[6] = iniColCom = COMdat[pidCom][8]; //guarda el color del combi

   COM_DetectarCombiMaxSup(iniX, iniY, (byte)comW, (byte)comH);

   minA = (short) (comW * comH + 1); //partimos de un area superior al del combi existente
   maxA = (short)((COMsuplimXfin-COMsuplimXini-1) * (COMsuplimYfin-COMsuplimYini-1));

   //maxA = (short) (NIVnumTilW * NIVnumTilH); //[PTE]:variable que indique área de la mayor superficie rectangular que puede contener el nivel.
   limChk = NIVnumTilW>NIVnumTilH?NIVnumTilW:NIVnumTilH;

   //Generar todas las superficies con área comprendida entre minA y minB
   //(buscar superficies genéricas <supW>*<supH> dónde <supH> es mayor o igual que <supW>)
   for (area = minA; area <= maxA; area++) {
     for (supW = 1; supW < area; supW++) { //Ancho de área
       if ((supW * 2 > area) || supW > limChk)
         break; //Corte: no existe superficie con lado igual o mayor que <supW> que tenga área <area>
       else {
         supH = supW;
         while ((supW * supH < area) && supH < limChk)
           supH++;
         if (supW * supH == area) {
           // Encontrada superficie genérica <supW>*<supH> con área <area>
           //System.out.println("COM-AREA a Chk: " + area + "  supW=> " + supW + "  supH=> " + supH);
           // Comprobar si en dicha superficie existe un combi
           if (COM_ExisteCombiEnArea(iniX, iniY, comW, comH, supW, supH)){
             //Comprobar que los colores de los combis contenidos permiten conformar el nuevo combi
             if (iniColCom != 0) { //Si el combi de partida es multicolor el resultante también lo será
               if (COMtmp[6] == 0) { //si es multicolor averiguar si es porque habia piezas sueltas (sin combi) de otros colores o no
                 if (!COMsup_ExistePie(COMtmp[2], COMtmp[3], COMtmp[4], COMtmp[5])) { //Si no hay piezas sueltas
                   //Si no hay ningún combi gris es que el resto de combis son monocolor con color distinto al del combi inicial
                   if (COMtmp[7] == 0){
                     //System.out.println(" Colores diferentes => no hay combi: X:" + iniX + " Y:"+iniY+ "  supW=> " + supW + "  supH=> " + supH);
                     continue;
                   }
                 }
               }
             }
             return true; //Corte: La sup sólo se chequea 1 vez para un mismo valor a del binomio supW*supH
           }
         }//Fin chequeo supW*supH=area
       }
     }//Fin chequeo SupW de Superfice de área
   }//Fin chequeo AREA
   return false;

}




//---------------------------------------------------------- COM_ExisteCombiEnArea
// Desc: Detectar si existe COMBI en un área de <psupW>*<psupH> (o viceversa) que
//       contenga a la pieza de BB <ppieW>*<ppieH> localizada en <pniX>,<piniY>
//       Deben cumplirse dos cosas:
//       1.- El área que incluye a la pieza es un Combi
//       2.- Si las piezas del combi detectado pertenecen a otro Combi, éste
//           (y sus padres) deben estar contenidos en el combi detectado.
//       Se asegura que las piezas no forman parte de otro combi (el cual no esté totalmente contenido en el combi detectado)
// Update:
//       Si se detecta combi, los datos de la superficie encontrada quedan
//       registrados en COMtmp.
//-------------------------------------------------------------------------------
private boolean COM_ExisteCombiEnArea(short ppieX, short ppieY, short ppieW, short ppieH, short psupW, short psupH){
    short iniX, iniY, finX, finY, i,j;
    short supfinX, supfinY;
    //Caso HORZ: Para todas las sups con área psupW x psupH que contienen a la pieza
    if(psupW >= ppieW && psupH >= ppieH){
      iniX = (short) (ppieX - (psupW - ppieW));
      if(iniX <0)
        iniX = 0; //no se tienen en cuenta areas que comiencen fuera del mapa
      finX = ppieX;
      iniY = (short) (ppieY - (psupH - ppieH));
      if(iniY <0)
        iniY = 0;    //no se tienen en cuenta areas que comiencen fuera del mapa
      finY = ppieY;
      for (j = iniY; j <= finY; j++)
        for (i = iniX; i <= finX; i++) {
          supfinX = (short) (i + psupW - 1);
          supfinY = (short) (j + psupH - 1);
          //Superficie concreta a evaluar: [i,j]..[supfinX,supfinY]
          if (!NIV_ChkColSup(i, j, supfinX, supfinY)){ //Si no existe colisión de superficie con bordes
            //Chequeo condición 1
            if (COM_ExisteCombiEnSup(i, j, supfinX, supfinY)) {
              //Chequeo condición 2
              COMtmp[1] = 0;
              //System.out.println("  COMBI!!! i=> "+i+" j=> "+j+" supfinX=>"+supfinX+" supfinY=>"+supfinY);
              if (COM_TodosIncluidoEnSup(i, j, supfinX, supfinY)) {
                //registrar datos de la superficie que conformar el combi
                COMtmp[1]++; //incrementar el maximo nivel de los combis incluidos en la sup. en 1
                COMtmp[2] = i;
                COMtmp[3] = j;
                COMtmp[4] = psupW;
                COMtmp[5] = psupH;
                return true;
              }
            }
          }
           //else
           // System.out.println("  ChkColSup: i=> "+i+" j=> "+j+" supW=>"+psupW+" supH=>"+psupH);
        }//Fin bucle chequep sup horz
    }


    //Caso VERT: Para todas las sups con área psupH x psupW que contienen a la pieza
    if(psupH >= ppieW && psupW >= ppieH){ //variables intercambiadas
        iniX = (short) (ppieX - (psupH - ppieW));
        if (iniX < 0)
          iniX = 0; //no se tienen en cuenta areas que comiencen fuera del mapa
        finX = ppieX;
        iniY = (short) (ppieY - (psupW - ppieH));
        if (iniY < 0)
          iniY = 0; //no se tienen en cuenta areas que comiencen fuera del mapa
        finY = ppieY;
        for (j = iniY; j <= finY; j++)
          for (i = iniX; i <= finX; i++) {
            supfinX = (short) (i + psupH - 1);
            supfinY = (short) (j + psupW - 1);
            //Superficie concreta a evaluar: [i,j]..[supfinX,supfinY]
            if (!NIV_ChkColSup(i, j, supfinX, supfinY)) { //Si no existe colisión de superficie con bordes
              //Chequeo condición 1
              if (COM_ExisteCombiEnSup(i, j, supfinX, supfinY)) { //a.-no hay huecos en sup, b.-no hay 'medias' piezas
                //Chequeo condición 2
                //System.out.println("  COMBI!!! i=> " + i + " j=> " + j + " supfinX=>" + supfinX + " supfinY=>" + supfinY);
                if (COM_TodosIncluidoEnSup(i, j, supfinX, supfinY)) {
                  //registrar datos de la superficie que conformar el combi
                  COMtmp[1]++; //incrementar el maximo nivel de los combis incluidos en la sup. en 1
                  COMtmp[2] = i;
                  COMtmp[3] = j;
                  COMtmp[4] = psupH;
                  COMtmp[5] = psupW;
                  return true;
                }
              }
            }
            //else
            //System.out.println("  ChkColSup: i=> "+i+" j=> "+j+" supW=>"+psupW+" supH=>"+psupH);
          }//Fin bucle chequep sup vert
    }
    return false;
}


//---------------------------------------------------------- COM_ExisteCombiEnSup
// Desc: Comprueba que la superfice conforma un Combi
//       1.- Está completamente rellena de piezas (no hay huecos u obstáculos internos)
//       2.- Todas las piezas están incluidas al completo (no hay piezas con media parte fuera de la sup)
// Return: True si la sup conforma combi, False si hay huecos/obs,o medias piezas.
//----------------------------------------------------------------------------
private boolean COM_ExisteCombiEnSup(short piniX, short piniY, short pfinX, short pfinY){
    short pos1, pos2, i,j;

    //Chequear que no hay un solo hueco (tile sin pieza) en toda la superficie
    for(j=piniY; j<=pfinY; j++){
        pos1 = (short) (j*NIVnumTilW);
        for(i=piniX; i<= pfinX; i++){
            pos2 = (short)(pos1+i);
            if(NIVidPieza[pos2] < 0) //bastaria comprobar == -1 (<-1 si chequeamos también obstáculos no bordes)
              return false;
        }
     }

     //Chequear que todas las piezas de la superficie están contenidas en la misma por completo
     //(Que no hay medias piezas)
     //[PTE Optimiz S40]: Evaluar sólo las 3x3 filas/columnas adjuntas a los bordes (Atajo horz si supW>6 (pfinX-piniX+1)>6, idem vert)
     for(j=piniY; j<=pfinY; j++){
        pos1 = (short) (j*NIVnumTilW);
        for(i=piniX; i<= pfinX; i++){
            pos2 = (short)(pos1+i);
            if(! PIE_IncluidoEnSup(NIVidPieza[pos2], NIVnumPart[pos2], i, j, piniX, piniY, pfinX, pfinY))
                return false;
        }
     }
    return true;
}


//---------------------------------------------------------- PIE_EnSup
private boolean PIE_IncluidoEnSup(short pidPie, short pidnumPart, short pX, short pY, short psupiniX, short psupiniY, short psupfinX, short psupfinY){
    short idnumPart, iniX, iniY, finX, finY, pieW, pieH;
    byte tip, ori;

    tip = PIEdat[pidPie][1];
    ori = PIEdat[pidPie][2];
    pieW = TIPPIEbb[tip - 1][ori * 2];
    pieH = TIPPIEbb[tip - 1][ori * 2 + 1];

    //obtener superficie que ocupa la pieza
    iniX = (short)(pX - TIPPIE_ObtenerPosXPart(pidnumPart, tip, ori));
    iniY = (short)(pY - TIPPIE_ObtenerPosYPart(pidnumPart, tip, ori));
    finX = (short)(iniX + pieW -1);
    finY = (short)(iniY + pieH -1);

    //comprobar inclusión del area que oucpa la pieza en la superficie
    if(iniX >= psupiniX && finX <= psupfinX && iniY >= psupiniY && finY <= psupfinY)
      return true;
    else
        return false;
}


//---------------------------------------------------------- COM_TodosIncluidoEnSup
// Desc: Comprueba si los Combis a los que pertenecen todas las piezas de la
//       superficie están contenidos completamente en dicha superficie
//       (Valida tanto los combis padre de las piezas, como los combis padre de
//        dichos combis y sucesivos: jerarquía de combis incluida en la superficie)
//       Además obtiene;
//       - El nivel máximo de los combis contenidos
//       - Si es multicolor o monocolor (y qué color en este caso)
// Update:
// - COMtmp[1]
// - COMtmp[6] (0 si multicolor, 1..Numcolores si monocolor)
//---------------------------------------------------------------------------------
private boolean COM_TodosIncluidoEnSup(short piniX, short piniY, short pfinX, short pfinY){
    byte  idCom, nivmax = 0, inicol;
    short i,j,pos1,pos2;
    boolean bMulticolor = false;

    inicol = (byte)COMtmp[6];  //color de la pieza o del combi desencadenante de la detección del nuevo combi
    if(inicol == 0)
      bMulticolor = true;
    COMtmp[7] = 0; //En caso de que el combi sea multicolor, (por defecto) NO es debido a que otro combi incluido en él también lo sea.

    //[PTE Optimiz S40]: Atajo: Cachear los ids de los combis evaluados (eg. array 50 combis -bytes-)
     for(j=piniY; j<=pfinY; j++){
        pos1 = (short) (j*NIVnumTilW);
        for(i=piniX; i<= pfinX; i++){
            pos2 = (short)(pos1+i);
            idCom = PIEdat[NIVidPieza[pos2]][4];
            if(idCom == -1)
                COMtmp[6] = PIEdat[NIVidPieza[pos2]][3]; //registrar color de pieza
            else{
              while (idCom >= 0) { //Comprobar para toda la jerarquía de combis padres
                //System.out.println(" Chk idCom: "+idCom);
                if (!COM_IncluidoEnSup(idCom, piniX, piniY, pfinX, pfinY))
                  return false;
                //registrar nivel, si es superior al de todos los combis ya identificados en la sup
                if (COMdat[idCom][1] > nivmax)
                  nivmax = COMdat[idCom][1];
                  //registrar color del combi (para quedarse con el color del combi superior)
                COMtmp[6] = COMdat[idCom][8];

                idCom = COMdat[idCom][2]; //Obtener id del Combi padre
              }
              if(COMtmp[6]==0)
                COMtmp[7] = 1; //Sí que se ha encontrado un combi multicolor contenido en el nuevo combi
            }
            //comprobar si sigue siendo monocolor el combi
            if(!bMulticolor)
                if(COMtmp[6] != inicol)
                    bMulticolor = true;
            //registrar el nivel máximo de combi entre todas las piezas pertenecientes a la sup ??

        }
     }
     //almacenar el nivel maximo de los combis incluidos en la superficie
     COMtmp[1] = nivmax;
     //asignar color final
     if(bMulticolor)
         COMtmp[6] = 0; //0: multicolor
     return true;
}



//---------------------------------------------------------- PIE_EnSup
private boolean COM_IncluidoEnSup(byte pidCom, short psupiniX, short psupiniY, short psupfinX, short psupfinY){
    short iniX, iniY, finX, finY;

    //obtener superficie que ocupa el Combi
    iniX = COMdat[pidCom][3];
    iniY = COMdat[pidCom][4];
    finX = (short)(iniX + COMdat[pidCom][5] - 1);
    finY = (short)(iniY + COMdat[pidCom][6] - 1);

    //comprobar inclusión del area que ocupa la pieza en la superficie
    if(iniX >= psupiniX && finX <= psupfinX && iniY >= psupiniY && finY <= psupfinY)
            return true;
    return false;
}




//------------------------------------------------------------ NIV_DesplazarPie
// Desc: Inicar el desplazamiento de la pieza <pIdPie> desde la posición
//       inicial <pPosX,Y> hasta la posición destino <pNewPosX,Y>.
//       La pieza queda en estado temporal para poder continuar su
//       desplazamiento automático hasta la posición de choque.
// Param:
// - pIdPie: Identificador de la pieza en PIEdat
// - pPosX, pPosY: Posición sup-izq inicial de la pieza (de su bb)en el tablero
// - pNewPosX, pNewPosY: Nueva posición sup-izq de la pieza desplazada 1 pos
// Update:
// - PIEdat (cambia el estado de la pieza para indicar que ha sido desplazada)
// - NIVidPieza, NIVnumPart (actualiza mapa de piezas del nivel borrando la pieza desplazada)
// - PIEtmp (Genera pieza temporal con la pieza desplazada en la nueva pos)
//-----------------------------------------------------------------------------

private void NIV_DesplazarPie(short pIdPie, short pPosX, short pPosY, short pNewPosX, short pNewPosY){
   byte tip, ori, pieW, pieH;
   short i,j;

   //Desactiva pieza del array de piezas en tablero
   tip = PIEdat[pIdPie][1];
   ori = PIEdat[pIdPie][2];
   NIV_ResetMapPie(pPosX, pPosY, tip, ori);
 //Cambiar el estado de la pieza
   PIEdat[pIdPie][0] = LIBRE;

//Actualiza pieza temporal con los datos de la pieza a desplazar (sustituye los existentes)
   PIEtmp[0] = ACTIVA;
   PIEtmp[1] = PIE_EN_MOV;
   PIEtmp[2] = pNewPosX;
   PIEtmp[3] = pNewPosY;   // el resto de campos [4..7] se mantienen

   //Activar siguiente estado de la FSM PIETMP
   PIETMPsts = STSPIE_EN_MOV;
}

private void NIV_DesplazarCom(){
   //No es necesario actualizar nada del Nivel (se hará automaticamente a partir próximo tick)
   //Actualiza pieza temporal para activar situación en Movimiento de Combi
   PIEtmp[0] = ACTIVA;
   PIEtmp[1] = COM_EN_MOV;
   //Activar siguiente estado de la FSM PIETMP
   PIETMPsts = STSPIE_EN_MOV;
}


//*****************************************************************************
//************************************************************************* PIE
//*****************************************************************************

//-------------------------------------------------------------------- PIE_Tick
//-----------------------------------------------------------------------------
 private void PIE_Tick(){

   if(GAMsts == PLAY_OUT_PAUSA || GAMsts == PLAY_OUT_CHGNIV || GAMsts == PLAY_OUT_ENDGAM
      || (GAMbNivIntro && !LVL.bTut)) //El tutorial permite jugar al nivel con el texto introductorio mostrandose.
     return;

   TIM_Tick();

//Control Máquina de Estados de PIETMP (FSM PIETMP)
    switch(PIETMPsts){
     case STSPIE_EN_CURTAB:
       switch(PIETMPsbSts){
         case 0: //CURTAB_0: Comprobar si el tiempo de colocación de pieza se ha agotado
           if(TIM_FinPie())
             PIETMPsbSts++;
           break;

         case 1: //CURTAB_1: Tiempo de colocación de pieza agotado. Se suelta/lanza la pieza
           if(GAMsts != PLAY_IN_CURSOR) //evitar forzar soltar la pieza si el Prop está cambiando de borde
             return;
           if(CUR_SoltarPie()) //si soltar pieza implica tener que lanzarla
             if (!CUR_LanzarPie()) { //si hay colisión al lanzar cambiar a estado automático de propulsor
               ACTdstX = ACTposX; //Actualizar pos origen para poder detectar vuelta completa al contorno zona 0
               ACTdstY = ACTposY;
               PRO_Avanzar();
               ACTincX = ACTacuIncX;
               ACTincY = ACTacuIncY;
               GAMsts = PLAY_IN_PROPLAN;
               PIETMPsbSts++;
               break;
             }
           PIETMPsts = STSPIE_EN_MOV;
           break;

           case 2: //CURTAB_2: Mover Prop a derechas e intentar LANZAR pieza. (GAMsts sigue valiendo PLAY_IN_PROPLAN)
             PRO_Avanzar();
             ACTincX = ACTacuIncX;
             ACTincY = ACTacuIncY;
             break;

           case 3: //CURTAB_3: Intentar Colocar Pieza en primer hueco de tablero (GAMsts sigue valiendo PLAY_IN_PROPLAN)
             if(!CUR_DejarPie()){
               GAMsts = PLAY_OUT_ENDGAM;
               GAMsbSts=0;
             }
             else
                 PIETMPsts = STSPIE_EN_MOV;        //?[PTE: EN mov?? (Probar)
             break;
       }
       break;


     case STSPIE_EN_MOV:
        if(PIEtmp[0] == ACTIVA){ //si pieza activa
          if (PIEtmp[1] == PIE_EN_MOV) { //Pieza en movimiento --desplazándose--
            PIE_Desplazar();
          }
          if (PIEtmp[1] == COM_EN_MOV) { //Combi en movimiento --desplazándose--
            COM_Desplazar();
          }
        }
        else
          //Interrelación con FSM GAM
          if(GAMsts == PLAY_IN_CURSOR){ //si ya ha acabado de generar combis
            PIETMPsts = STSPIE_EN_HUD;
            if(CURmodo == ELIMINAR || CURmodo == DESPLAZAR)
               PIETMPsbSts = 2; //NO Tomar pieza
            else
              PIETMPsbSts = 0; //Tomar pieza siguiente
          }
        break;


     case STSPIE_EN_HUD:
       switch(PIETMPsbSts){
         case 0:
           GAM_GetSigPie(); //Generar tipo/ori/color de siguiente pieza y avanza las actuales a nueva posición en cola.
           PIETMPsbSts++;
           break;
         case 1:
           CUR_CogerPie(); //Asignar pieza actual a Propulsor
           GAMbPieSig = true;
           PIETMPsbSts++;
           break;
        case 2:
         //Control Fin de Nivel por Derrota
         if (GAM_ChkFinGam()) {
           //System.out.println("FX =>> Derrota Nivel (GAM_ChkFinGam");
           GAMsts = PLAY_OUT_ENDGAM;
         }
         else {
           TIM_IniPie(); //Arranca timer de pieza
           PIETMPsts = STSPIE_EN_CURTAB;
           PIETMPsbSts = 0;
         }
       }
       break;
    }
 }






 private short PIE_GenerarPie(byte pTip, byte pOri, byte pCol){
         if(PIElastId == 288)
             PIElastId = PIE_GetSigHueco();
         else
             PIElastId++;
         PIEdat[PIElastId][0] = OCUPADO; //estado
         PIEdat[PIElastId][1] = pTip;
         PIEdat[PIElastId][2] = pOri;
         PIEdat[PIElastId][3] = pCol;
         PIEdat[PIElastId][4] = -1; 	//sin combi

         return PIElastId;
 }

private short PIE_GetSigHueco(){
    short i;
    for(i=0; i<PIElastId; i++)
        if(PIEdat[i][0] == LIBRE)
            return i;
    //System.out.println("EX: Maximo numero de Piezas generado");
    return -1;
}


//------------------------------------------------------------------ PIE_Reset
// Inicializa datos de piezas
private void PIE_ResetAll(){
    PIElastId = -1; //en cada nivel se resetea a -1.
    if(PIEdat==null)
      PIEdat = new byte[288][5]; //Limite 2 pantallas: (12*2)*(12*2) / 2 (casillas una pieza) = 288
    for(int i=0; i<288; PIEdat[i][0]=LIBRE, i++);
}

//------------------------------------------------------------------ PIE_Reset
// Desc: Desconecta las piezas del combi <pidCom> cambiando su estado a LIBRE
private void PIE_ResetCom(byte pidCom){
    for(short i=0; i<=PIElastId; i++)
        if(PIEdat[i][4] == pidCom)
            PIEdat[i][0] = LIBRE;
}

//---------------------------------------------------------------- PIE_LoadDef
//Inicializa datos de piezas por defecto, si no están ya creadas
private void PIE_LoadDef(){
    for(int i=0, idx=0 ; i<LVL.PIEnumDef; i++, idx+=5){
      //Comprobar si la pieza está ya creada
      if(!NIV_ChkColPie(LVL.PIEdef[idx], LVL.PIEdef[idx + 1], LVL.PIEdef[idx + 2],LVL.PIEdef[idx + 3]))
        //Actualizar datos de piezas y sus partes del NIVel
         NIV_UpdMapPie(PIE_GenerarPie(LVL.PIEdef[idx + 2], LVL.PIEdef[idx + 3], LVL.PIEdef[idx + 4]),
                       LVL.PIEdef[idx],
                       LVL.PIEdef[idx + 1],
                       LVL.PIEdef[idx + 2],
                       LVL.PIEdef[idx + 3]);
      }
}

//Comprueba si la pieza pasada por parametro es pieza colocada por defecto
private boolean PIE_EsDef(short pPosX, short pPosY, byte pTip, byte pOri){
    for(int i=0, idx=0 ; i<LVL.PIEnumDef; i++, idx+=5)
      if(LVL.PIEdef[idx]==pPosX &&LVL.PIEdef[idx+1]==pPosY &&
         LVL.PIEdef[idx+2]==pTip &&LVL.PIEdef[idx+3]==pOri)
        return true;
    return false;
}

//------------------------------------------------------------ PIE_AsignarCombi
// Desc: Asigna el id del Combi a las pieza del nuevo combi generado <pidCom>
//       No asina id de combi a aquellas piezas que ya pertenecían a otro combi
//-----------------------------------------------------------------------------
private void PIE_AsignarCombi(byte pidCom){
short iniX, iniY, finX, finY, i, j, pos1, pos2, idPie, idComact;


  iniX = COMdat[pidCom][3];
  iniY = COMdat[pidCom][4];
  finX = (short)(iniX + COMdat[pidCom][5]-1);
  finY = (short)(iniY + COMdat[pidCom][6]-1);

  for(j=iniY; j<=finY; j++){
     pos1 = (short) (j*NIVnumTilW);
     for(i=iniX; i<= finX; i++){
       pos2 = (short) (pos1 + i);
       idPie = NIVidPieza[pos2];
       idComact = PIEdat[idPie][4] ;
       if(idComact == -1) //si la pieza no esta ya en otro combi
         PIEdat[idPie][4] = pidCom;
       else //si la pieza ya está incluida en un combi
          if(idComact != pidCom) // si el combi al que pertenece la pieza no es este mismo.
            if (COMdat[idComact][2] == -1) // si ese combi es el de nivel superior (no tiene padre)
                COMdat[idComact][2] = pidCom;

     }
  }
  VISbUpd=true;

}



//--------------------------------------------------------------- PIE_Desplazar
// Desc: Realiza desplazamiento de pieza temporal.
//       Una vez finalice el desplazamiento genera nueva Pieza en la posición destino
//       (Se asume que la pieza temporal está Activa y en Movimiento)
//-----------------------------------------------------------------------------
private void PIE_Desplazar(){
    short newPosX, newPosY;

      //Detectar posición siguiente en el sentido del desplazamiento (PIEtmp[7])
      newPosX = PIEtmp[2];
      newPosY = PIEtmp[3];
      //posible sustitucion por PIE_Mover(newPosX,newPosY);
      //[PTE]: Sin icremento < 12 será necesario mantener actualizadas las coordnedas nivX, Y de la pieza ([9],[10])
      switch(PIEtmp[7]){
        case ARR:
          newPosY--;
          break;
        case ABA:
          newPosY++;
          break;
        case IZQ:
          newPosX--;
          break;
        case DER:
          newPosX++;
          break;
      }

      //Comprobar si hay obstáculo en el sentido del desplazamiento.
      if(!NIV_ChkColPie(newPosX, newPosY, (byte)PIEtmp[4], (byte)PIEtmp[5])){
        PIEtmp[2] = newPosX;
        PIEtmp[3] = newPosY;
      }
      else{
        //System.out.println("FX =>> Pieza ha chocado con tope");
        // Hay obstáculo: Colocar la pieza en esta pos
        NIV_ColocarPie(PIEtmp[2], PIEtmp[3]); //Genera pieza fija en situación actual y desactiva temporal
        GAMsts = PLAY_IN_COMBI; //siguiente subestado: chequear formación Combi y crearlo
        GAMsbSts = 0;
      }
}


//*****************************************************************************
//********************************************************************** PIETMP
//*****************************************************************************



//-------------------------------------------------------- PIETMP_CalcPosModLan
// Desc: Calcula la posicion de la pieza temporal asociada al propulsor
//-----------------------------------------------------------------------------
private void PIETMP_CalcPosModLan(){
   short pieW, pieH;

   if (PROmodo == HORZ) {
     pieH = TIPPIEbb[PIEtmp[4] - 1][PIEtmp[5] * 2 + 1];
     PIEtmp[2] = (short)(PROposX + 1);
     if (PROori == ABA) //Orientación: hacia abajo
       PIEtmp[3] = (short)(PROposY+1);
     else //Orientación: hacia arriba
       PIEtmp[3] = (short) (PROposY - (pieH - 1));
   }
   else { //Modo propulsor: Vertical
     pieW = TIPPIEbb[PIEtmp[4] - 1][PIEtmp[5] * 2];
     PIEtmp[3] = (short)(PROposY +1);
     if (PROori == DER) //Orientación: hacia la derecha
       PIEtmp[2] = (short)(PROposX + 1);
     else //orientación hacia la izquierda
       PIEtmp[2] = (short) (PROposX - (pieW - 1));
   }
}


private void PIETMP_CalcPosModCol(){
    //ASumimos que la pieza puede pasar por encima de las ya colocadas (para llenar hueco por ejemplo)
    //La pieza se displaya un tile por encima y a la izquierda de la flecha del cursor
    PIEtmp[2] = (short)(CURposX - 1);
    PIEtmp[3] = (short)(CURposY - 1);
}



//-------------------------------------------------------- PIETMP_CalcPosModEli
// Desc: Calcula la posicion de la pieza apuntada por el cursor en modo
//       eliminación. La pieza no puede ser parte de un combi.
// Upd:  PIEtmp (con los datos de la pieza apuntada por el cursor,
//       y la posición sup-izq de la misma)
// Ret:  Booleano que indica si el cursor apunta a pieza eliminable.
//-----------------------------------------------------------------------------

private boolean PIETMP_CalcPosModEli(){
    short idPie, idnumPart;
    byte tip, ori;
    //Detectar pieza a la que apunta el cursor
    if((idPie = NIVidPieza[CURposY* NIVnumTilW + CURposX]) > -1){
        if(PIEdat[idPie][4] == -1){  //Si no forma parte de Combi  //DESCOMENTAR: si queremos no seleccionar las piezas interiores de un combi
          //Generar pieza temporal del mismo tipo y orientación, en estado activa y situación fija en tablero
          PIEtmp[0] = ACTIVA;
          PIEtmp[1] = PIE_EN_TAB;
          //Buscar la posX,Y inicial de la pieza (en función de su idpart, y tipo e ori)
          idnumPart = NIVnumPart[CURposY * NIVnumTilW + CURposX];
          tip = PIEdat[idPie][1];
          ori = PIEdat[idPie][2];
          PIEtmp[2] = (short) (CURposX - TIPPIE_ObtenerPosXPart(idnumPart, tip, ori));
          PIEtmp[3] = (short) (CURposY - TIPPIE_ObtenerPosYPart(idnumPart, tip, ori));
          PIEtmp[4] = tip;
          PIEtmp[5] = ori;
          PIEtmp[6] = PIEdat[idPie][3];
          //Idem en coordenadas de nivel
          PIEtmp[8] = idPie;

          CURbHayPieza = true;
          return true;
        }
        else{
          if(PIEtmp[8] != PIEdat[idPie][4]){
            PIEtmp[8] =PIEdat[idPie][4]; //para FX primera vez que se entra en combi
            //System.out.println("FX =>> Pieza forma parte de Combi");
          }
          return false;
        }

    }
    else{
      // Estado inactiva en caso contrario y bCurHayPieza = false
      PIEtmp[0] = INACTIVA;
      PIEtmp[8] = -1;
      return false;
    }
}


//-------------------------------------------------------- PIETMP_CalcPosModDes
// Desc: Calcula la posicion de la pieza apuntada por el cursor en modo
//       desplazamiento.
//       Si la pieza es parte de un combi se seleccionará el COMbi completo
//       Si la pieza es parte de un combi compuesto se seleccionará el combi de mayor nivel.
//       [PTE: POSIBLE desplazar COMBIS compuestos????
//-----------------------------------------------------------------------------

private boolean PIETMP_CalcPosModDes(){
    short idPie, idnumPart, idCombi;
    byte tip, ori;

    if(PIEtmp[0] == ACTIVA && (PIEtmp[1] == PIE_EN_MOV || PIEtmp[1] == COM_EN_MOV))
      return false;

    //Detectar pieza a la que apunta el cursor
    if((idPie = NIVidPieza[CURposY* NIVnumTilW + CURposX]) > -1){

        //La pieza seleccionada NO forma parte de un COMBI
        if(PIEdat[idPie][4] == -1){
            //Comprobar si PIEtmp está ya actualizada con la misma pieza a la que apunta el cursor.
            if(!(PIEtmp[0] == ACTIVA && PIEtmp[1] == PIE_EN_TAB && PIEtmp[8] == idPie)){
              //Generar pieza temporal del mismo tipo y orientación, en estado activa y situación fija en tablero
              PIEtmp[0] = ACTIVA;
              PIEtmp[1] = PIE_EN_TAB;
              //Buscar la posX,Y inicial de la pieza (en función de su idpart, y tipo e ori)
              idnumPart = NIVnumPart[CURposY * NIVnumTilW + CURposX];
              tip = PIEdat[idPie][1];
              ori = PIEdat[idPie][2];
              PIEtmp[2] = (short) (CURposX - TIPPIE_ObtenerPosXPart(idnumPart, tip, ori));
              PIEtmp[3] = (short) (CURposY - TIPPIE_ObtenerPosYPart(idnumPart, tip, ori));
              PIEtmp[4] = tip;
              PIEtmp[5] = ori;
              PIEtmp[6] = PIEdat[idPie][3];

              PIEtmp[8] = idPie;
              CURbHayPieza = true;
              CURbHayCombi = false;
           }
           //Mantener el mismo sentido de desplazamiento (actualizar solo para inicializarlo)
           if(PIEtmp[7] == 0)
             PIEtmp[7] = ARR;
           return true;
        }
        //La pieza seleccionada forma parte de un COMBI
        else{
            //identificar si el combi forma parte de otro combi de mayor nivel (compuesto)
            idCombi = COM_ObtenerComRaiz(PIEdat[idPie][4]); //[PTE]: Desplazamiento de combis compuestos???
            //Comprobar si PIEtmp está ya actualizada con la info del combi a la que apunta el cursor.
            if(!(PIEtmp[0] == ACTIVA && PIEtmp[1] == COM_EN_TAB && PIEtmp[8] == idCombi)){
              //Generar pieza temporal que indique combi
              PIEtmp[0] = ACTIVA;
              PIEtmp[1] = COM_EN_TAB;
              //Buscar la posX,Y inicial del Combi (en función de la idpart de una pieza del mismo)
              PIEtmp[2] = COMdat[idCombi][3]; //posX
              PIEtmp[3] = COMdat[idCombi][4]; //posX
              PIEtmp[4] = PIEdat[idPie][1];   //[PTE]: Guardar la info de pieza, retuilizar ancho/alto del combi, o dejarlo sin utilizar?????
              PIEtmp[5] = PIEdat[idPie][2];   //[PTE]
              PIEtmp[6] = COMdat[idCombi][8]; //color

              PIEtmp[8] = idCombi;

              CURbHayCombi = true;
              CURbHayPieza = false;
            }
            //[PTE]: reutilizar valor anterior, o El sentido de desplazamiento inicial es NORTE
            if(PIEtmp[7] == 0)
              PIEtmp[7] = ARR;
            return true;
        }
    }
    else{
      // Estado inactiva en caso contrario y bCurHayPieza = false
      if(PIEtmp[1] != PIE_EN_MOV && PIEtmp[1] != COM_EN_MOV)
        PIEtmp[0] = INACTIVA;
      return false;
    }

}



//**************************************************************************
//********************************************************************* COMBI
//**************************************************************************

 private byte COM_GenerarCOM(byte pX, byte pY, byte pW, byte pH, byte pidnivel, byte pCol){

     if(COMlastId == 100)
         //System.out.println("EX: Maximo numero de Combis generado");
         COMlastId = COM_GetSigHueco();
     else
         COMlastId++;

     COMdat[COMlastId][0] = OCUPADO; //estado
     COMdat[COMlastId][1] = pidnivel; //nivel
     COMdat[COMlastId][2] = -1; //padre (todavía sin padre)
     COMdat[COMlastId][3] = pX;
     COMdat[COMlastId][4] = pY;
     COMdat[COMlastId][5] = pW;
     COMdat[COMlastId][6] = pH;

     COMdat[COMlastId][7] = (byte)((pW == pH)? 0: 1); //tipo
     COMdat[COMlastId][8] = pCol;

     return COMlastId;
 }

private byte COM_GetSigHueco(){
    byte i;
    for(i=0; i<COMlastId; i++)
        if(COMdat[i][0] == LIBRE)
            return i;
    //System.out.println("EX: Maximo numero de Combis generado");
    return -1;
}


private short COM_ObtenerComRaiz(short pidCombi){
    //Evitar llamadas de piezas sin combi padre
    if(pidCombi == -1)
      return -1;
    //Búsqueda recursiva del combi padre.
    short pidPadre = COMdat[pidCombi][2];
    if(pidPadre == -1) //Si no tiene combi padre finalizar [PTE]: Pos de padre
        return pidCombi;
    else
        return COM_ObtenerComRaiz(pidPadre);
}


private short COM_ObtenerCom(byte pX, byte pY, byte pW, byte pH){
      for(short i=0; i<100; i++)
        if(COMdat[i][0] == OCUPADO && COMdat[i][3] == pX &&
          COMdat[i][4] == pY && COMdat[i][5] == pW && COMdat[i][3] == pH)
         return i;
     return -1;
}


// Desc: Comprueba si el combi <pidCom> es un combi colocado por defecto en
//       la definición del nivel, o contiene a un combi colocado por defecot.
private boolean COM_EsDef(short pidCom){
     if(COMdat[pidCom][1]==1){ //Si el combi es de primer nivel
       for (int i = 0; i < LVL.COMnumDef; i++)
         if (COMdat[pidCom][3] == LVL.COMdef[i * 5] &&
             COMdat[pidCom][4] == LVL.COMdef[i * 5 + 1] &&
             COMdat[pidCom][5] == LVL.COMdef[i * 5 + 2] &&
             COMdat[pidCom][6] == LVL.COMdef[i * 5 + 3])
           return true;
       return false;
     }else{
       //Buscar todos los hijos directos del combi y aplicarles recusivamente esta misma funcion
       for(short i=0; i<100;i++)
         if(COMdat[i][0]==OCUPADO && COMdat[i][2]==pidCom) //Si es un hijo directo
           if(COM_EsDef(i)) //Comprobar si es combi por defecto
             return true;
       return false; //Ninguno de los hijos es combi por defecto
     }
}


//------------------------------------------------------------ COM_AsignarCombi
// Desc: Asigna <pidCom> como padre de los combis inferiores que todavia
//       no tengan padre.
//-----------------------------------------------------------------------------
  private void COM_AsignarCombi(byte pidCom){
  short iniX, iniY, finX, finY, i, j, pos1, pos2, idPie, idComact;
    iniX = COMdat[pidCom][3];
    iniY = COMdat[pidCom][4];
    finX = (short)(iniX + COMdat[pidCom][5]-1);
    finY = (short)(iniY + COMdat[pidCom][6]-1);

    for(j=iniY; j<=finY; j++){
      pos1 = (short) (j * NIVnumTilW);
      for (i = iniX; i <= finX; i++) {
        pos2 = (short) (pos1 + i);
        idPie = NIVidPieza[pos2];
        idComact = COM_ObtenerComRaiz(PIEdat[idPie][4]);
        if(idComact != pidCom)
           COMdat[idComact][2] = pidCom;
      }
    }

  }


//------------------------------------------------------------------ COM_UpdPos
// Desc: Proc recursivo que mueve todos los combis que cuelgan de
//       <pidCom> incluido éste mismo.
//-----------------------------------------------------------------------------
private void COM_UpdPos(byte pidCom, short pincX, short pincY){
    for(byte i=0; i<=COMlastId; i++)
        if(COMdat[i][0] == OCUPADO)
            if(COMdat[i][2] == pidCom) //si el combi es hijo del Combi a mover, actualizar el hijo
                COM_UpdPos(i, pincX, pincY);
    //desplazar el combi
    COMdat[pidCom][3] += pincX;
    COMdat[pidCom][4] += pincY;
}



//---------------------------------------------------------------- COM_ResetAll
private void COM_ResetAll(){
    COMlastId = -1;
    if(COMdat==null)
      COMdat = new byte[100][9];
    for(int i=0; i<100; COMdat[i][0]=LIBRE, i++);
}


//------------------------------------------------------------------- COM_Reset
// Desc: Proc recursivo que cambia a estado libre la jerarquía de
//       combis que cuelgan de <pidCom>, incluido éste mismo.
//-----------------------------------------------------------------------------
private void COM_ResetCom(byte pidCom){
    COMdat[pidCom][0] = LIBRE;
    for(byte i=0; i<=COMlastId; i++)
        if(COMdat[i][2] == pidCom)
            COM_ResetCom(i);
}

//---------------------------------------------------------------- COM_LoadDef
//Inicializa datos de combis por defecto
private void COM_LoadDef(){
  for(int i=0, idx=0; i<LVL.COMnumDef; i++, idx+=5){
    //Comprobar si el combi ya existe antes de crearlo (debido a la regeneración "parcial" de piezas)
    if(COM_ObtenerCom(LVL.COMdef[idx], LVL.COMdef[idx + 1],
                      LVL.COMdef[idx + 2], LVL.COMdef[idx + 3]) == -1)
                    PIE_AsignarCombi(COM_GenerarCOM(LVL.COMdef[idx], LVL.COMdef[idx + 1],
                                                    LVL.COMdef[idx + 2], LVL.COMdef[idx + 3],
                                                    (byte) 1, LVL.COMdef[idx + 4]));
  }
}



//--------------------------------------------------------------- COM_Desplazar
// Desc: Desplazamiento del combi <PIEtmp[8]> en la dirección definida
//       en PIEtmp
//       (Se asume que: el combi <PIEtmp[8]> no tiene padre, y que no
//        hay obstáculo inicial en el sentido de desplazamiento).
//-----------------------------------------------------------------------------
private void COM_Desplazar(){
    short newPosX, newPosY, incX=0, incY=0;
    short iniX, iniY, w, h;
  //Detectar posición siguiente en el sentido del desplazamiento (PIEtmp[7])
  newPosX = PIEtmp[2];
  newPosY = PIEtmp[3];
  switch(PIEtmp[7]){
    case ARR:
      newPosY--;
      incY = -1;
      break;
    case ABA:
      newPosY++;
      incY = 1;
      break;
    case IZQ:
      newPosX--;
      incX = -1;
      break;
    case DER:
      newPosX++;
      incX = 1;
      break;
  }

  //Comprobar si hay obstáculo en el sentido del desplazamiento.
  if(!NIV_ChkColCom(newPosX, newPosY, (byte)COMdat[PIEtmp[8]][5], (byte)COMdat[PIEtmp[8]][6], (byte)PIEtmp[7])){
    //Actualizar la posición de las piezas del área del combi en NIVidPieza y NIVnumPart
    NIV_UpdMapCom((byte)PIEtmp[8], (byte) PIEtmp[7]);
    //Actualizar de forma recursiva los datos de posicionamiento de los combis que cuelgan de <PIEtmp[8]>, incluido este.
    COM_UpdPos((byte)PIEtmp[8], incX, incY); //Actuliza Nivel con datos Combi y refresca fondo

    //conservar nueva posición del combi
    PIEtmp[2] = newPosX;
    PIEtmp[3] = newPosY;
  }
  else{
    // Hay obstáculo: Colocar el combi en esta pos
    //System.out.println("FX =>> Combi ha chocado con tope");
    PIEtmp[0] = INACTIVA;
    //Actualizar COMtmp con id y color del combi para comprobar si hay nueva generación de combis.
    COMtmp[0] = PIEtmp[8]; //Id del combi
    COMtmp[6] = PIEtmp[6]; // Color de combi
    GAMsts = PLAY_IN_COMBI; //siguiente subestado: detectar formación Combi y crearlo
    GAMsbSts = 1; //Subestado 1: origen de combi es otro combi
  }
}


//--------------------------------------------------------------- COM_Eliminar
// Desc: Eliminación del combi <pidCom>
//-----------------------------------------------------------------------------
private void COM_Eliminar(byte pidCom){
    //Resetear datos de piezas (y partes) del combi en el nivel
   NIV_ResetMapCom(COMdat[pidCom][3], COMdat[pidCom][4], COMdat[pidCom][5], COMdat[pidCom][6]);  //También fuerza refresco de BGR por combi eliminado (iniX,iniY,w,h)
   //Liberar entradas en lista de combis (cambiando a estado LIBRE)
   COM_ResetCom(pidCom); //resetea jerarquía de combis
   PIE_ResetCom(pidCom); //Liberar entradas en lista de piezas (cambiando a estado LIBRE)
}



//--------------------------------------------------------------- COM_ChkRulObj
// Desc: comprueba si existe alguna regla que aplique al combi recien generado
//       En caso afirmativo para el control de comprobación a otra función.
//       (Las reglas son exclusivas por color)
//----------------------------------------------------------------------------
private boolean COM_ChkRulObj(){
   boolean brule=false;
   //Buscar idx de la regla que aplica en función del color del combi
   for(int i=0; i<GAMnumRulObj; i++){
     if (LVL.OBJrul[i][0] == TIPOBJ_GENCOM){  //aunque se haya sobrepasado el límite de Gen de ese objetivo igualmente se evalua,ya que dara lugar a eliminacion
       if(LVL.OBJrul[i][2] == -2 ) //si no importa el color sólo existe esta regla que chequear
         brule = COM_ChkRulGen(i);
       else if (LVL.OBJrul[i][2] == -1){ //regla afecta sólo a combis monocolor
         if(COMtmp[6] != 0)
           brule = COM_ChkRulGen(i);
       }
       else if (LVL.OBJrul[i][2] == COMtmp[6]) //color determinado o multicolor
           brule = COM_ChkRulGen(i);
     }

     if(brule)
       return true;
//     else
//       if(LVL.idModo != 0) //Sólo en el modo Tutorial se permite más de una regla por color
//         return false;
     //Chequeo tipo de objetivo TIPOBJ_OCUCOM: Ocupación de área por combis
     //(Siempre retorna falso

   }
   GAMidRul = -1;
   return false;
}


//---------------------------------------------------------------
private boolean COM_ChkRulGen(int idx){
   if(LVL.OBJrul[idx][2] == 0 && LVL.OBJrul[idx][3] != -1) //Chequeo número de colores del combi
    if (COMsup_GetNumCol(COMtmp[2],COMtmp[3],COMtmp[4],COMtmp[5]) < LVL.OBJrul[idx][3])
        return false;

    if(LVL.OBJrul[idx][4] != -1 && LVL.OBJrul[idx][15]==-1) //Chequeo posX
      if(COMtmp[2] != LVL.OBJrul[idx][4])
        return false;
    if(LVL.OBJrul[idx][5] != -1) //Chequeo posY
      if(COMtmp[3] != LVL.OBJrul[idx][5])
        return false;

   if(LVL.OBJrul[idx][6] != -1) //Chequeo anchura mínima
     if(COMtmp[4] < LVL.OBJrul[idx][6])
       return false;
   if(LVL.OBJrul[idx][7] != -1) //Chequeo altura minima
     if(COMtmp[5] < LVL.OBJrul[idx][7])
       return false;

   if(LVL.OBJrul[idx][8] != -1) //Chequeo área mínima
     if((COMtmp[4]*COMtmp[5]) < LVL.OBJrul[idx][8])
       return false;

   if(LVL.OBJrul[idx][9] == 1) //Chequeo forma cuadrada
     if(COMtmp[4] != COMtmp[5])
       return false;
   if(LVL.OBJrul[idx][9] == 0) //Chequeo forma rectangular
     if(COMtmp[4] == COMtmp[5])
       return false;

   if(LVL.OBJrul[idx][10] == 0) //Chequeo num mínimo de piezas
     if(COMsup_GetNumPie(COMtmp[2],COMtmp[3],COMtmp[4],COMtmp[5]) < LVL.OBJrul[idx][11])
       return false;
   if(LVL.OBJrul[idx][10] == 1) //Chequeo num exacto de piezas
     if(COMsup_GetNumPie(COMtmp[2],COMtmp[3],COMtmp[4],COMtmp[5]) != LVL.OBJrul[idx][11])
       return false;

   if(LVL.OBJrul[idx][12] == 0) //Chequear que combi es simple (nivel 1)
     if((COMtmp[1] != 1))
       return false;
   if(LVL.OBJrul[idx][12] > 0) //Chequeo nivel
     if((COMtmp[1]) < LVL.OBJrul[idx][12])
       return false;

   if(LVL.OBJrul[idx][13] != -1) //Chequeo composición a base de piezas simples
    if(COMsup_ExistePie(COMtmp[2],COMtmp[3],COMtmp[4],COMtmp[5])){ //existen piezas sueltas
      if (LVL.OBJrul[idx][13] == 0) //si no se admiten piezas sueltas
        return false;
    }else if (LVL.OBJrul[idx][13] == 1) //si se requieren piezas sueltas
      return false;



  if(LVL.OBJrul[idx][14] > 0)  //Chequeo composición de número exacto de combis
    //Se puede usar en conjunción con [13] para NO admitir combis con piezas sueltas
    if(COMsup_GetNumCom(COMtmp[2], COMtmp[3], COMtmp[4], COMtmp[5],(short) 1) != LVL.OBJrul[idx][14])
      return false;
   if(LVL.OBJrul[idx][14] == 0) //Chequeo composición a base de combis de dimensiones iguales
     //Se puede usar en conjunción con [13] para NO admitir combis con piezas sueltas
      if ( COMsup_GetNumCom(COMtmp[2], COMtmp[3], COMtmp[4], COMtmp[5],(short) 2) <=1) //existe más de un combi con mismas dimensiones
        return false;

    if(LVL.OBJrul[idx][15] > 0) //Chequeo número exacto de tipo de piezas
      if(COMsup_GetTipPie(COMtmp[2],COMtmp[3],COMtmp[4],COMtmp[5], (short)0) != LVL.OBJrul[idx][15])
        return false;
    if(LVL.OBJrul[idx][15] == 0) //Chequeo existencia de tipo de pieza determinado
      if(COMsup_GetTipPie(COMtmp[2],COMtmp[3],COMtmp[4],COMtmp[5], LVL.OBJrul[idx][4]) == 0)
        return false;


   GAMidRul = (byte)idx;
   return true;
}


//--------------------------------------------------------------- GAM_SupSolap
// Desc: Comprueba si un combi está dentro/fuera/o solapando con una superficie
// Param:
//     - Coor iniciales y finales del combi
//     - coor iniciale y finales de la superficie
//----------------------------------------------------------------------------
 private short GAM_SupSolap(short x1i, short y1i, short x1f, short y1f,
                              short x2i, short y2i, short x2f, short y2f){
     if (y1i >= y2i && y1f <= y2f && x1i >= x2i && x1f <= x2f)
       return 2; //sup 1 incluida totalmente en sup 2
     else if ( (y1i <= y2i && y1i >= y2i) || (y1i <= y2f && y1f >= y2f) ||
              (y1i >= y2i && y1f <= y2f)) //  col vert
       if ( (x1i <= x2i && x1i >= x2i) || (x1i <= x2f && x1f >= x2f) ||
           (x1i >= x2i && x1f <= x2f))
         return 1;
     return 0;
 }


// Desc: Comprueba si el combi recien generado cumple la regla de eliminación
//       por estar contenido/solapando a una superficie Objetivo de generación
//       de Combis.
private boolean COM_ChkRulEliTip0(short pIn){
   boolean bEli=false;
   for(int i=0; i<GAMnumRulObj; i++)
     if (LVL.OBJrul[i][0] == TIPOBJ_GENCOM)
       if(LVL.OBJrul[i][4] != -1 && LVL.OBJrul[i][5] != -1){ //si la regla especifica posX e Y
         //comprobar inclusión de COMtmp en alguna de las superficies Objetivo
         if (GAM_SupSolap(COMtmp[2], COMtmp[3],
                          (short) (COMtmp[2] + COMtmp[4] - 1),
                          (short) (COMtmp[3] + COMtmp[5] - 1),
                          LVL.OBJrul[i][4], LVL.OBJrul[i][5],
                          (short) (LVL.OBJrul[i][4] + LVL.OBJrul[i][6] - 1),
                          (short) (LVL.OBJrul[i][5] + LVL.OBJrul[i][7] - 1)) >=
             pIn) //0 fuera, 1 solap bordes, 2 dentro total
           return false;
         else
           bEli = true;
       }
    return bEli;
}


// Desc: Comprueba que el combi recien generado cumple la regla de eliminación
//       por tipología.
private boolean COM_ChkRulEliTip1(short pcol, short pw, short ph, short ptipdim, short pcomp){
   //comprobar color
   if(pcol == -1){
     if(COMtmp[6]==0) //si solo afecta a monocolor y el combi es multi
       return false;
   }else if (pcol !=-2 && pcol != COMtmp[6]) //si el color importa y no es el mismo que el del combi
     return false;

   //comprobar dimensiones mínimas para ser eliminado.
   if(ptipdim != -1){
     if (ptipdim == 0) { //dimensiones mínimas

       if (pw != 1 && ph != 1) { //basta con que una dim sea menor para ser eliminada
         if ((COMtmp[4] >= pw && COMtmp[5] >= ph) || (COMtmp[5] >= pw && COMtmp[4] >= ph)) //si ningún lado cumple la condición, el combi no se elimina
             return false;
       }
       else
       if (pw != -1 && COMtmp[4] >= pw) //Chequeo anchura mínima
         return false;
       else if (ph != -1 && COMtmp[5] >= ph) //Chequeo altura mínima
         return false;
     }
     else { //dimensiones máximas
       if (pw != 1 && ph != 1) {
         if (ptipdim == 1 ){ //(OR basta con que una dim sea mayor para ser eliminado)
           if ((COMtmp[4] <= pw && COMtmp[5] <= ph) || (COMtmp[5] <= pw && COMtmp[4] <= ph)) //si ningún lado cumple la condición, el combi no se elimina
             return false;
         }else{ //ptipdim=2 (AND): se deben cumplir ambas condiciones para eliminar el combi
           if (COMtmp[4] <= pw || COMtmp[5] <= ph ) //Con que un lado no cumpla la condición, el combi no se elimina
             return false;
         }
       }
       else
       if (pw != -1 && COMtmp[4] <= pw) //Chequeo anchura máxima
         return false;
       else if (ph != -1 && COMtmp[5] <= ph) //Chequeo altura máxima
         return false;
     }
   }

   //comprobar composición (que el combi está formado por 2 combis de igual tamaño.
   if(pcomp != -1){ //Chequeo composición (Más de un combi de igual tamaño)
      if(COMsup_ExistePie(COMtmp[2],COMtmp[3],COMtmp[4],COMtmp[5])) //si existe alguna pieza suelta en el combi
          return false;
      else if ( COMsup_GetNumCom(COMtmp[2], COMtmp[3], COMtmp[4], COMtmp[5],(short)2) == -1) //si el tamaño de los hijos es diff.
          return false;
      }

   return true;
}


//-------------------------------------------------------------- COM_ChkRulEli
// Desc: Detecta si el combi recien creado cumple las reglas de Eliminación de
// combis definidas para el nivel.
 //---------------------------------------------------------------------------
private boolean COM_ChkRulEli(){
   boolean bChk;
   if(LVL.OBJbEliGen && GAMnumComRulGen > 0) //si ha cumplido las reglas de generación y el nivel está definido como reglas equivalentes a Eliminación
     return true;
   else
     for(int i=0; i<LVL.OBJnumRulEli; i++){
       if(LVL.OBJrulEli[i][0] == 0) //tipo 0: inclusión de combi en sup
         bChk = COM_ChkRulEliTip0(LVL.OBJrulEli[i][1]);
       else if(LVL.OBJrulEli[i][0] == 1) //tipo1: tipología combi
         bChk = COM_ChkRulEliTip1(LVL.OBJrulEli[i][1],
                                  LVL.OBJrulEli[i][2],
                                  LVL.OBJrulEli[i][3],
                                  LVL.OBJrulEli[i][4],
                                  LVL.OBJrulEli[i][5]);
       else //tipo2: eliminación por defecto si no cumple condición de generación
         bChk = (GAMnumComRulGen==0);
       if (bChk)
         return true;
     }
   return false;
}




// Desc: Obtener tile pix de la parte del combi a la que pertenece una pieza
private short COM_ObtenerIdPix(short pidCom, int ptilX, int ptilY){
   short idx;
   if(COMdat[pidCom][5]==1){ //Combi de ancho 1
     GAMmanip = 0;
     if (ptilY == COMdat[pidCom][4])
       idx = 2;
     else if (ptilY == COMdat[pidCom][4] + COMdat[pidCom][6] - 1) {
       idx = 2;
       GAMmanip = DirectGraphics.FLIP_VERTICAL;
     }
     else
       idx = 3;
   }else if(COMdat[pidCom][6]==1){//Combi de alto 1
     GAMmanip = DirectGraphics.ROTATE_90;
     if (ptilX == COMdat[pidCom][3])
       idx = 2;
     else if (ptilX == COMdat[pidCom][3] + COMdat[pidCom][5] - 1) {
       idx = 2;
       GAMmanip = DirectGraphics.ROTATE_270;
     }
     else
       idx = 3;
   }else
   //Ancho y alto mayor que 1
   if(ptilX == COMdat[pidCom][3]){ //extremo izq
     if (ptilY == COMdat[pidCom][4]){
       idx = 0;
       GAMmanip = 0;
     }
     else if (ptilY == COMdat[pidCom][4] + COMdat[pidCom][6] - 1){
       idx = 0;
       GAMmanip = DirectGraphics.FLIP_VERTICAL;
     }
     else{
       idx = 1;
       GAMmanip = DirectGraphics.ROTATE_90;
     }
   }else if(ptilX == COMdat[pidCom][3] + COMdat[pidCom][5] - 1){ //extremo der
     if (ptilY == COMdat[pidCom][4]){
       idx = 0;
       GAMmanip = DirectGraphics.FLIP_HORIZONTAL;
     }
     else if (ptilY == COMdat[pidCom][4] + COMdat[pidCom][6] - 1){
       idx = 0;
       GAMmanip = DirectGraphics.FLIP_HORIZONTAL | DirectGraphics.FLIP_VERTICAL;
     }
     else{
       idx = 1;
       GAMmanip = DirectGraphics.ROTATE_270;
     }
   }
   else if (ptilY == COMdat[pidCom][4]){ //extermo superior
     idx = 1;
     GAMmanip = 0;
   }
   else if (ptilY == COMdat[pidCom][4] + COMdat[pidCom][6] - 1){ //extremo inf
     idx = 1;
     GAMmanip = DirectGraphics.FLIP_VERTICAL;
   }
   else
     return -1;

      return (short)(3 + idx); //3: num tiles previos en array de SPR
}


//****************************************************************************
//********************************************************************* TIPPIE
//****************************************************************************

 private void TIPPIE_ObtenerVal(byte pTip, byte pOri){
         int pos = (pTip-1)*4*9 + pOri*9;

         for(short i=0; i<9; i++)
                 TIPPIErefval[i] = TIPPIEval[pos+i];
 }

  private void TIPPIE_ObtenerContVal(byte pTip, byte pOri){
          int pos = (pTip-1)*4*9 + pOri*9;

          for(short i=0; i<9; i++)
                  TIPPIErefval[i] = TIPPIEcontval[pos+i];
  }

  private void TIPPIE_ObtenerContMan(int[] pVal, byte pTip, byte pOri){
          int pos = (pTip-1)*4*9 + pOri*9;

          for(short i=0; i<9; i++)
                  pVal[i] = TIPPIEcontman[pos+i];
  }


//------------------------------------------------------ TIPPIE_ObtenerPosXPart
// Desc: Buca la primera parte de la pieza en el cuadrante de 5x5 tiles que
//       rodea al tile existente en la posición <pX>,<pY>
//-----------------------------------------------------------------------------
  private short TIPPIE_ObtenerPosXPart(short pidPart, byte ptip, byte pori){
      short pieW = TIPPIEbb[ptip - 1][pori * 2];
      short pieH = TIPPIEbb[ptip - 1][pori * 2 + 1];
      TIPPIE_ObtenerVal(ptip, pori);
      for (byte j = 0; j < pieH; j++)
        for (byte i = 0; i < pieW; i++)
          if (TIPPIErefval[j * 3 + i] == pidPart)
            return i;
      //System.out.println("TIPPIE_ObtenerPosXPart =>> Err");
      return -1;
  }

  private short TIPPIE_ObtenerPosYPart(short pidPart, byte ptip, byte pori){
       short pieW = TIPPIEbb[ptip - 1][pori * 2];
       short pieH = TIPPIEbb[ptip - 1][pori * 2 + 1];
       TIPPIE_ObtenerVal(ptip, pori);
       for (byte j = 0; j < pieH; j++)
         for (byte i = 0; i < pieW; i++)
           if (TIPPIErefval[j * 3 + i] == pidPart)
             return j;
       //System.out.println("TIPPIE_ObtenerPosYPart =>> Err");
       return -1;
   }

//---------------------------------------------------------------- TIM_IniPausa
// Desc: Registrar el tiempo (de partida y de pieza) transcurrido hasta el
//       momento de la pausa
//----------------------------------------------------------------------------
private void TIM_IniPausa(){
     //Salvar timer partida
     TIMgamAcu += System.currentTimeMillis() - TIMgamIni;
     if(TIMbPie) //Salvar timer pieza
       TIMpieAcu += (System.currentTimeMillis() - TIMpieIni);
}

//---------------------------------------------------------------- TIM_FinPausa
// Desc: Restaurar timer partida y timer pieza (si aplica)
//----------------------------------------------------------------------------
private void TIM_FinPausa(){
   TIMgamIni = System.currentTimeMillis();
   if(TIMbPie)
     TIMpieIni = System.currentTimeMillis();
}

//------------------------------------------------------------------ TIM_FinPie
// Desc: Compruba si el timer de colocación de pieza se ha cumplido
//       Calcula variable para display en HUD
//-----------------------------------------------------------------------------
private boolean TIM_FinPie(){
    if(TIMbPie){ //Si hay timer de pieza en el nivel en curso
      long TIMpieAct = System.currentTimeMillis() - TIMpieIni;

      if(TIMpieAct + TIMpieAcu >= TIMpieDur){
          HUDtimAct = HUDtimMax;
          //[PTE]: Mostrar mensaje de 'Time Out' en pantalla.
          //System.out.println("FX =>> Tiempo agotado para colocar la pieza: "+ TIMpieAct);
          return true;
      }
      else
          HUDtimAct = (short)((TIMpieAct + TIMpieAcu) * HUDtimMax / TIMpieDur);
    }
    return false;
}

//------------------------------------------------------------------ TIM_IniPie
// Desc: Inicializa el timer para colocación de pieza
//       Calcula variable para display en HUD
//-----------------------------------------------------------------------------

private void TIM_IniPie(){
    if(TIMbPie){ //Si hay timer de pieza en el nivel en curso
        TIMpieAcu = 0;
        TIMpieIni = System.currentTimeMillis();
        HUDtimAct = 0;
    }
}


private void TIM_IniGam(){
    TIMgamAcu = 0;
    TIMgamIni = System.currentTimeMillis();
    TIMbGam = true;
}


private void TIM_Tick(){
    if(TIMbGam)
      HUDtimgamAct = (System.currentTimeMillis() - TIMgamIni) + TIMgamAcu;
}

//**************************************************************************
//********************************************************************* HUD
//**************************************************************************

 private void HUD_Dibujar(Graphics g) {
 int x,y;
 int nivYini =14*NUMPIXELS_TILEY; //coordenada y de inicio del HUD

//System.out.println(" => GAM_HUD");
//Dibujo de HUD estático
    if(HUDbIni){
   //System.out.println(" => GAM_HUD_static");
      HUDbScore = HUDbProg = HUDbCurCont = HUDbPie = true;
      g.setClip(0,nivYini, DEVscrPxlW, DEVscrPxlH - nivYini);
      g.setColor(HUDcol);
      g.fillRect(0, nivYini, DEVscrPxlW, DEVscrPxlH - nivYini);
      HUD_DibImg(g,0,nivYini,31);
      HUD_DibImg(g,DEVscrPxlW-8,nivYini,32);
      HUD_DibImg(g,0,DEVscrPxlH-8,33);
      HUD_DibImg(g,DEVscrPxlW-8,DEVscrPxlH-8,34);
      for (int i = 0; i < (DEVscrPxlW - 16) / 8; i++) {
        HUD_DibImg(g,(i<<3)+8,nivYini,37);
        HUD_DibImg(g,(i<<3)+8,DEVscrPxlH - 2,38);
      }
      for (int j = 0; j < (DEVscrPxlH - nivYini - 16) / 8; j++) {
        HUD_DibImg(g,0,(j<<3)+nivYini+8,35);
        HUD_DibImg(g,DEVscrPxlW-2,(j<<3)+nivYini+8,36);
      }
      //Dibujar marcos piezas
      for (int i = 0; i < 3; i++) {
        x = HUDlocpie[i * 2] - 1;
        y = nivYini + 7;
        int j = HUDlocpie[i * 2 + 1];
        HUD_DibImg(g,x,y,j);
        HUD_DibImg(g,x+13,y,j+1);
        HUD_DibImg(g,x,y+13,j+2);
        HUD_DibImg(g,x+13,y+13,j+3);
      }
      //Dibujar marco interno para progreso
      g.setClip(59,175,57,18);
      g.setColor(0xFFF6F3EC);
      g.drawRect(60, 176, 55, 16);
      g.setColor(0xFFA2A092);
      g.drawRect(59, 175, 55, 16);
      //Dibujo iconos estáticos
      HUD_DibImg(g,68,180,21); //progreso
    }


//---- Progreso
     if(HUDbProg){
       //System.out.println(" => GAM_HUD_prog");
       g.setClip(79, nivYini+12, 24, 8);
       g.setColor(HUDcol); //Gris claro
       g.fillRect(79, nivYini+12, 24, 8);
       if(Long.toString(GAMobjProg).length()==1){
         HUD_DibImg(g,87,179,0); //0 por delante
         HUD_DibNum(g,GAMobjProg,95,179);
       }
       else if(GAMobjProg <100)
         HUD_DibNum(g,GAMobjProg,87,179);
       else
         HUD_DibNum(g,GAMobjProg,79,179);
       HUD_DibImg(g,103,179,10); //signo %
     }

//---- Puntuación
      if(HUDbScore){
        //System.out.println(" => GAM_HUD_score");
        g.setColor(HUDcol); //Gris claro
        g.fillRect(59, nivYini+29, 56, 8);
        sTime = Long.toString(GAMscore);
        if((x=sTime.length())>0)
          HUD_DibNum(g,GAMscore,115-(sTime.length()<<3),196);
        for (int i = 0; i < 7 - x; i++)
          HUD_DibImg(g,59+(i<<3),196,0); //0s por delante
      }


//---- Valores uso modos cursor
      if(HUDbCurCont){
        //System.out.println(" => GAM_HUD_curcont");
        g.setClip(119, nivYini+24, 53, 8);
        g.setColor(HUDcol); //Gris claro
        g.fillRect(119, nivYini+24, 53, 8);
       //Contador de modo cursor en Combi
        if (CURmodo == 3) {
          HUD_DibImg(g,119,193,23);
          if (CURcont[4] == -1) {
            HUD_DibImg(g,128,192,11); //Guión
            HUD_DibImg(g,136,192,11); //Guión
          }
          else
            HUD_DibNum(g, CURcont[4], 128,192);
        }
        //Contador de modo cursor en Pieza
        HUD_DibImg(g,146,193,24);
        if (CURcont[CURmodo] == -1) {
          HUD_DibImg(g,156,192,11); //Guión
          HUD_DibImg(g,164,192,11); //Guión
        }
        else
          HUD_DibNum(g, CURcont[CURmodo], 156,192);
      }

      //Iconos de Modos de cursor y selección del modo actual
      if(HUDbIni){
        //System.out.println(" => GAM_HUD_curcamb");
        g.setClip(117, nivYini+8, 56, 14);
        g.setColor(HUDcol); //Gris claro
        g.fillRect(117, nivYini+8, 56, 14);
        g.setColor(0, 0, 0);
        g.drawRect(117 + CURmodo * 14, 176, 13, 13);
        for (int i = 0; i < 4; i++)
          HUD_DibImg(g,118 + i*14,177,12+i+(CURmodo==i?4:0)); //Guión
      }

//Imprimir las 3 piezas empezando por GAMpie[GAMpieLastId]
      if(HUDbPie && !GAMbNivIntro){
        //System.out.println(" => GAM_HUD_pie");
        g.setColor(236,240,249);
        for(int i=0; i<3; i++){
          g.setClip(HUDlocpie[i * 2],nivYini+8,15,15);
          g.fillRect(HUDlocpie[i * 2],nivYini+8,15,15);
          HUD_DibPieImg(g, (GAMpieLastId + i) % 3, HUDlocpie[i * 2], nivYini + 8);
          g.setColor(HUDcol);
        }
      }

         //------------------------------------------ TimePie
         if(TIMbPie){
           //System.out.println(" => GAM_HUD_timpie");
           HUD_DibImg(g,3,196,20); //icono flecha
           g.setClip(14,197,42,7);
           g.setColor(0,0,0);
           g.drawRect(14, 197, 41, 6);
           g.setColor(102,153,255);
           g.fillRect(15, 198, HUDtimMax - HUDtimAct, 5);
           g.setColor(236,240,249);
           g.fillRect(15+HUDtimMax - HUDtimAct, 198, HUDtimAct, 5);
         }else if (TIMbGam){
           g.setColor(HUDcol); //Gris claro
           g.fillRect(8, 196, 42, 8);
           HUD_DibImg(g,25,196,89); //:
           int t_s = (int)(HUDtimgamAct / 1000);
           int t_secs = t_s % 60;
           int t_mins = t_s / 60;
           if(t_mins<10){
             HUD_DibNum(g,t_mins,16,196);
             HUD_DibImg(g,8,196,0); //0s por delante
           }else
             HUD_DibNum(g,t_mins, 8,196);
           if(t_secs<10){
             HUD_DibNum(g,t_secs,38,196);
             HUD_DibImg(g,30,196,0); //0s por delante
           }else
             HUD_DibNum(g,t_secs, 30,196);
         }

         if (GAMsts == PLAY_OUT_PAUSA)
           HUD_DibPausa(g);
        HUDbIni = HUDbScore = HUDbProg = HUDbCurCont = HUDbPie = false;

//System.out.println(" => GAM_HUD (fin)");
}


private void HUD_DibInOut(Graphics g){
     //Pintar espacio del HUD con color de fondo de escenario
     g.setColor(LVL.colbase);
     g.fillRect(0, 14 * NUMPIXELS_TILEY, DEVscrPxlW, DEVscrPxlH - 14 * NUMPIXELS_TILEY);

     //Inicio de partida/nivel
     if ((GAMsts == PLAY_IN_INIT && GAMsbSts == 0) ||
      (GAMsts == PLAY_OUT_CHGNIV && GAMsbSts == 5)) {
         g.setColor(0xFF000000);
         g.fillRect(0, 0, DEVscrPxlW, DEVscrPxlH);
     }
     else if (GAMsts == PLAY_OUT_CHGNIV || GAMsts == PLAY_OUT_ENDGAM){
       MENmsg = GAMsts == PLAY_OUT_CHGNIV ? (byte) 44 : -1;
       MEN_DibKeys(g, (byte) 8);
       if(!LVL.bTut && GAMsbSts==2)
         HUD_DibEnd(g);
     }
     else if (GAMbNivIntro) {
       HUD_DibTut(g);
       if(!LVL.bTut)
         HUD_DibIni(g);
     }//Fin de partida/nivel

}

//----------------------------------------------------------------- HUD_DibIni
// Desc: Muestra ventana diálogo de Inicio de nivel
//----------------------------------------------------------------------------
private void HUD_DibIni(Graphics g){
     int nivYini = 14 * NUMPIXELS_TILEY; //coordenada y de inicio del HUD
     short nivY = 12, nivX=32;
     g.setClip(0, 4, DEVscrPxlW, 110);
     FX_DibRect(g, DEVscrPxlW / 2 - 5 * NUMPIXELS_TILEX,
                DEVscrPxlW / 2 + 5 * NUMPIXELS_TILEX, 8, (short) 8 + 100);

     //Dibujar tipos de piezas del nivel
     g.setClip(0, nivY, DEVscrPxlW, 14);
     g.setFont(fSbt);
     g.setColor(0x224466);
     g.drawString("Piezas:", nivX, nivY, Graphics.TOP | Graphics.LEFT);
     short x=(short)(nivX+54);
     for (byte i = 0; i < 8; i++)
       if(i!=3 && i!=5 && LVL.NIVtipPie[i]==1){
         HUD_DibPieza(g, x, (short)(nivY+2), (short)(i+1));
         x+=10;
       }

     //Dibujar colores del nivel
     g.setClip(0, nivY+14, DEVscrPxlW, 14);
     g.drawString("Colores:", nivX, nivY + 14, Graphics.TOP | Graphics.LEFT);
     x=(short)(nivX+54);
     for (byte i = 0; i < 4; i++)
       if(LVL.NIVcolPie[i]==1){
         HUD_DibImg(g, x, (short)(nivY + 16), (short)(74+i));
         x+=12;
       }

     g.setFont(fCnt);
     g.drawString("Prev.", 70, nivY + 26, Graphics.TOP | Graphics.LEFT);
     HUD_DibModCursor(g, (short)32, (short) (nivY + 32), true);
}

//----------------------------------------------------------------- HUD_DibEnd
// Desc: Muestra ventana diálogo de Fin de nivel
//----------------------------------------------------------------------------
private void HUD_DibEnd(Graphics g){
     MENmsg = GAMsts == PLAY_OUT_CHGNIV ? (byte) 44 : -1;
     MEN_DibKeys(g, (byte) 8);
     short nivY = 40;

     FX_DibRect(g, DEVscrPxlW / 2 - 5 * NUMPIXELS_TILEX,
                DEVscrPxlW / 2 + 5 * NUMPIXELS_TILEX, 32,
                (short) 32 + (LVL.idModo == 1 ? 90 : 40) + (GAMbRecord ? 16 : 0));
     if (GAMbRecord) {
       g.setClip(0, nivY - 4, DEVscrPxlW, 16);
       MEN_DibRect(g, 32, nivY - 4, DEVscrPxlW - 32, nivY + 14, false,
                   0x90336699, 0);
       g.setColor(0xFFFFFF);
       g.setFont(fSbt);
       g.drawString(GAMstr[16], DEVscrPxlW / 2, nivY - 1,
                    Graphics.TOP | Graphics.HCENTER);
       nivY += 16;
     }

     //Modo Arcade: Resumen de nivel se muestra puntuación y valores de modos cursor (son acumulativos)
     if (LVL.idModo == 1) {
       HUD_DibPun(g, (short) 174, (byte) 1); //Imprimir record actual en pie
       HUD_DibPun(g, (short) nivY, (byte) 0); //Imprimir puntuación
       HUD_DibModCursor(g, (short) 32, (short) (nivY + 16), false); //Imprimir contadores finales modos cursor
     }
     else { //Modo Reflexión: Resumen de nivel se muestra Tiempo invertido en completar nivel y puntuación
       HUD_DibTim(g, (short) 174, (byte) LVL.idFase, (byte) LVL.idNiv); //Imprimir tiempo record actual en pie
       HUD_DibTim(g, (short) nivY, (byte) - 1, (byte) 0); //Imprimir tiempo invertido en nivel actual
       HUD_DibPun(g, (short) (nivY + 16), (byte) 0); //Imprimir puntuación actual
     }
   }

private void HUD_DibModCursor(Graphics g, short pnivX, short pnivY, boolean pant){
     short y;

     //Comprobar si arrastramos valores de modos cursor de nivel anterior
     //para decidir sí sólo mostrar los valores de modos cursor por defecto del nivel
     if(pant){
       pant =false;
       for (byte i = 0; i < 5; i++)
         if(CURcont[i]!=-1 && (CURcont[i]-LVL.CURcontInit[i]>0)){
           pant = true;
           break;
         }
     }
     for (byte i = 0; i < 5; i++){
       y=(short)(pnivY+i*12);
       HUD_DibImg(g, pnivX, y, 12 + (i == 4 ? 3 : i)); //icono modo cursor
       HUD_DibImg(g, pnivX+(pant?54:86),y+4,(i==4?23:24)); //icono pieza/combi
       g.setClip(0,y,DEVscrPxlW,14);
       if(pant){
         g.setFont(fCnt);
         g.setColor(0x224466);
         g.drawString((CURcont[i]==-1?"--":Integer.toString(LVL.CURcontInit[i])),pnivX+76,y+1,Graphics.TOP | Graphics.RIGHT);
         g.setColor(0x738AAB);
         if(CURcont[i]!=-1 && (CURcont[i]-LVL.CURcontInit[i]>0))
           g.drawString("(+"+Integer.toString(CURcont[i]-LVL.CURcontInit[i])+")",pnivX+98,y+1,Graphics.TOP | Graphics.RIGHT);
       }
       g.setColor(0x224466);
       g.setFont(fSbt);
       g.drawString(CURcont[i]==-1?"--":Integer.toString(CURcont[i]),DEVscrPxlW-32,y+1,Graphics.TOP | Graphics.RIGHT);
       g.setColor(0x738AAB);
       g.drawLine(pnivX+16, y+10, pant?pnivX+50:DEVscrPxlW-62, y+10);
     }
}


//------------------------------------------------------------- HUD_DibNum
private void HUD_DibNum(Graphics g, int pval, int px, int py){
   sTime = Long.toString(pval);
   for (int i = 0; i < sTime.length(); i++)
     HUD_DibImg(g,px+(i<<3),py, sTime.charAt(i)-'0');
}

//----------------------------------------------------------- HUD_DibujarPieImg
private void HUD_DibPieImg(Graphics g, int pidx, int px, int py){
     int nivX, nivY=0;

     TIPPIE_ObtenerVal((byte)GAMpie[pidx][0], (byte)GAMpie[pidx][1]);
     for (int j = 0; j < 3; j++) {
       nivX=0;
       for (int i = 0 ; i < 3; i++) {
         if (TIPPIErefval[j * 3 + i] > 0)
           HUD_DibImg (g,px+nivX, py+nivY, GAMpie[pidx][2]+46 ); //#46: idx pix previo a piezas de colores
         nivX +=5;
       }
       nivY +=5;
     }
}


private void HUD_DibPieza(Graphics g, short pnivX, short pnivY, short pnum){
     if (pnum  > 3) { //Solo se tratan piezas 1,2,3,5,7,8 (convertir a ordinal)
       if (pnum > 5)
         pnum = (byte) (pnum - 2);
       else
         pnum--;
     }
     HUD_DibImg(g, pnivX, pnivY, pnum + 81);
}

//---------------------------------------------------------------- HUD_DibImg

private void HUD_DibImg(Graphics g, int px, int py, int idpix){
     idpix *=4;
     g.setClip(px, py, IMGdat[idpix+2], IMGdat[idpix+3]);
     g.drawImage(imgHud, px-IMGdat[idpix], py-IMGdat[idpix+1], Graphics.TOP | Graphics.LEFT);
}




//---------------------------------------------------------------- HUD_DibPausa
private void HUD_DibPausa(Graphics g){
     short i1=-1,i2=-1;
    //Fondo
    g.setColor(0x88AACC);    //color fondo azul
    g.setClip(0, 0, DEVscrPxlW, DEVscrPxlH);
    g.fillRect(0,0,DEVscrPxlW,DEVscrPxlH);
    //Área cabecera
    g.setColor(0xFDAC8E); //Color salmon
    g.fillRect(1,1,DEVscrPxlW-2,18);
    //Marco cabecera
    g.setColor(0x3B465A); //azul fuerte
    g.drawRect(0, 0, DEVscrPxlW-1, 19);
    //Imprimir título Nivel
    g.setFont(fTit);
    g.setColor(0x224466);
    g.drawString(GAMstr[LVL.idxStrTit],4,4,Graphics.TOP | Graphics.LEFT);
    g.drawString("Nivel:"+ Integer.toString(LVL.idNiv+1), DEVscrPxlW-4, 4, Graphics.TOP | Graphics.RIGHT);

    //Pie
    if (MENid >= 2 || MENid==-1)
      MEN_DibRect(g, 1, 43, DEVscrPxlW - 2, 189, true, 0xFFA6BDD3, 0xFF738AAB);
    else
      MEN_DibRect(g, 8, 148, DEVscrPxlW - 9, 189, true, 0xFFA6BDD3, 0xFF738AAB);
    if(MENid==-1){ //confirmación de abandono de partida
      MEN_DibKeys(g, (byte) 6);
      g.setFont(fCnt);
      g.setColor(0x224466);
      g.drawString(GAMstr[31], DEVscrPxlW / 2, 78, Graphics.TOP | Graphics.HCENTER);
      return;
    }
    else{
      MENmsg = -1;
      MEN_DibKeys(g,(byte)7);
      //Imprimir indicadores cambio de página
      HUD_DibImg(g, 84, 192, MENid == 0 ? 80 : 29);
      HUD_DibImg(g, 84, 200, MENid == LVL.TXTstr.length/10 +1+ (LVL.TXTstr.length%10>0?1:0) ? 81 : 30);
    }

    //Imprimir título sección
    g.setClip(0, 0, DEVscrPxlW, DEVscrPxlH);
    g.setColor(0xFF738AAB); //azul morado
    g.fillRect(0,20,DEVscrPxlW,19);
    g.setFont(fSbt);
    g.setColor(0x224466);
    g.drawString(GAMstr[MENpauCnf[(MENid>2?2:MENid)*4+3]],DEVscrPxlW/2,23,Graphics.TOP | Graphics.HCENTER);

    //Imprimir texto debajo de título sección
    if(MENid==1){
      i1=70;
      i2=65;
    }else if (MENid==0){
      if(LVL.idModo==0){
        if(LVL.OBJnumRul>0 && LVL.OBJrul[0][0] == TIPOBJ_POSPIE){//Pte: TIPOBJ_POSPROP
          i1=60;
          i2=61;
        }else{
          i1=62;
          i2=63;
        }
      }
      else if(LVL.idModo==2 && LVL.idFase==3){ //COmbicracia
        if(LVL.OBJrul[0][3] == 1){ //ocupación por número
          i1 = 64;
          i2 = 65;
        }else{ //ocupación por área
          i1 = 66;
          i2 = 67;
        }
      }
      else{
        i1=68;
        i2=69;
      }
    }
    g.setColor(0x224466);
    g.setFont(fCnt);
    g.drawString(i1!=-1?GAMstr[i1]:"", 4, 41,Graphics.TOP | Graphics.LEFT);
    g.drawString(i2!=-1?GAMstr[i2]:"", 4, 55,Graphics.TOP | Graphics.LEFT);

   //Imprimir contenido de cada sección pantalla
   if (MENid == 0){
     if(LVL.idModo!=0)
       if(LVL.OBJrul[0][0] != TIPOBJ_OCUCOM || LVL.OBJrul[0][3] != -1)
         HUD_DibPauSec0(g);  //Objetivos del nivel
   }
   else if (MENid == 1)
     HUD_DibPauSec1(g); //Reglas de Eliminación de combis
   else //MENid = 2..N
     HUD_DibTxt(g, (short) 46, (byte) ((MENid-2)*10), (byte) 10, 0x224466);
}



//------------------------------------------------------------ HUD_DibPauSec0
// Desc: Imprime contenido de la sección 0 (Objetivos) de la pantalla de Pausa.
//-----------------------------------------------------------------------------
private void HUD_DibPauSec0(Graphics g){
   int offset;
   byte a = -1, b = -1;
   int y=62;
   int nivY= y+30;

   //Dibujar fondo en la posición de la opción seleccionada
   offset=MENpauCnf[MENid*4+1]+MENop*2;
   MEN_DibRect(g,MENpauPos[offset],y+7,MENpauPos[offset]+MENpauPos[offset+1], y+23, true, 0xFFA6BDD3, 0xFF738AAB);

   //Imprimir iconos cabecera objetivos
   HUD_DibImg(g, 1, 168, 27);
   HUD_DibImg(g, DEVscrPxlW - 7, 168, 28);
   HUD_DibImg(g, 89, y+12, 88);
   HUD_DibImg(g, 103, y+12, 78);
   HUD_DibImg(g, 117, y+12, 23);
   HUD_DibImg(g, 131, y+12, 79);
   HUD_DibImg(g, 145, y+12, 24);
   HUD_DibImg(g, 162, y+12, 21);
   g.setClip(0, 0, DEVscrPxlW, DEVscrPxlH);

   //Imprimir cabeceras objetivos
   g.setColor(0x3B465A); //azul fuerte
   g.drawLine(1,y+26,22,y+26);
   g.drawLine(27,y+26,80,y+26);
   g.drawLine(86,y+26,154,y+26);
   g.drawLine(158,y+26,174,y+26);
   g.setColor(0xFFFFFF);
   g.drawString("Col", 4, y + 10, Graphics.TOP | Graphics.LEFT);
   g.drawString("Dim", 30, y + 10, Graphics.TOP | Graphics.LEFT);
   g.drawString("A", 57, y + 10, Graphics.TOP | Graphics.LEFT);
   g.drawString("F", 71, y + 10, Graphics.TOP | Graphics.LEFT);

   //Imprimir descripción item
   g.setColor(0xFFFFFF);
   g.setFont(fSbt);
   offset=MENpauCnf[MENid*4+2]+ MENop * 3;
   g.drawString(MENpauTxt[offset], 12, 150, Graphics.TOP | Graphics.LEFT);
   g.setColor(0x224466);
   g.setFont(fCnt);
   g.drawString(MENpauTxt[offset + 1], 12, 163,Graphics.TOP | Graphics.LEFT);
   g.drawString(MENpauTxt[offset + 2], 12, 176,Graphics.TOP | Graphics.LEFT);

    //Imprimir contenido objetivos
   offset=MENpauCnf[MENid*4+1];
   g.setFont(fCnt);
   for (int i = 0; i < GAMnumRulObj; i++) {
       g.setColor(0xFFFFFF);
       if (LVL.OBJrul[i][2] != -2)
         HUD_DibImg(g, 5, nivY + 1, 73 + LVL.OBJrul[i][2]);
       g.setClip(0, nivY, DEVscrPxlW, 14);
       if ( (a = LVL.OBJrul[i][3]) != -1 &&LVL.OBJrul[i][0] == TIPOBJ_GENCOM)
         g.drawString(Integer.toString(a), 16, nivY,
                      Graphics.TOP | Graphics.LEFT);

       a = LVL.OBJrul[i][6];
       b = LVL.OBJrul[i][7];
       if (a != -1 || b != -1)
         g.drawString( (a != -1 ? Integer.toString(a) : "?") + "x" +
                      (b != -1 ? Integer.toString(b) : "?"),
                      MENpauPos[offset + 1 * 2], nivY,
                      Graphics.TOP | Graphics.LEFT);

       if ( (a = LVL.OBJrul[i][8]) != -1)
         g.drawString(Integer.toString(a), MENpauPos[offset + 2 * 2] + 1, nivY,
                      Graphics.TOP | Graphics.LEFT);

       if ( (a = LVL.OBJrul[i][9]) != -1)
         g.drawString( (a == 0 ? "R" : "C"), MENpauPos[offset + 3 * 2] + 4,
                      nivY, Graphics.TOP | Graphics.LEFT);

       if ( (a = LVL.OBJrul[i][11]) != -1) {
         if ( (b = LVL.OBJrul[i][10]) == 0)
           a--;
         g.drawString( (b == 0 ? ">" : "=") + Integer.toString(a),
                      MENpauPos[offset + 4 * 2] - (a > 9 ? 4 : 0), nivY,
                      Graphics.TOP | Graphics.LEFT);
       }

       if ( (a = LVL.OBJrul[i][12]) != -1)
         g.drawString( (a == 0 ? "S" : Integer.toString(a - 1)),
                      MENpauPos[offset + 5 * 2] + 4, nivY,
                      Graphics.TOP | Graphics.LEFT);

       if ( (a = LVL.OBJrul[i][13]) != -1)
         g.drawString( (a == 0 ? "N" : "S"), MENpauPos[offset + 6 * 2] + 4,
                      nivY, Graphics.TOP | Graphics.LEFT);

       if ( (a = LVL.OBJrul[i][14]) != -1)
         g.drawString( (a == 0 ? "=" : Integer.toString(a)),
                      MENpauPos[offset + 7 * 2] + 4, nivY,
                      Graphics.TOP | Graphics.LEFT);

       if ( (a = LVL.OBJrul[i][15]) != -1)
         if (a != 0)
           g.drawString(Integer.toString(a), MENpauPos[offset + 8 * 2] + 4,
                        nivY, Graphics.TOP | Graphics.LEFT);
         else {
           HUD_DibPieza(g, (short)145, (short)nivY, LVL.OBJrul[i][4]);
           g.setClip(0, nivY, DEVscrPxlW, 14);
         }

       //imprimir status objetivo (sólo para objetivo de tipo Generación y Ocupación-Número)
       if(LVL.OBJrul[i][0] == TIPOBJ_GENCOM  ||
          (LVL.OBJrul[i][0] == TIPOBJ_OCUCOM && LVL.OBJrul[i][3] == 1)){
         g.setColor(GAMobjSts[i * 2 + 1] >= GAMobjSts[i * 2] ? 0x224466 :
                    0xFF0000);
         g.drawString(Integer.toString(GAMobjSts[i * 2 + 1]) + "/" +
                      Integer.toString(GAMobjSts[i * 2]),
                      DEVscrPxlW-2, nivY,
                      Graphics.TOP | Graphics.RIGHT);
       }

       nivY += 14;
       g.setColor(0x738AAB);
       g.drawLine(2, nivY - 3, 173, nivY - 3);
     }
}


//--------------------------------------------------------------- HUD_DibPauSec1

private void HUD_DibPauSec1(Graphics g){
     int offset, y=70, nivY=y+8;
     boolean bcont=true;
     byte a=-1, b=-1, i1=58,i2=-1,i3=-1;

   //Imprimir reglas de eliminación de tipo Tipología de Combi
   if(LVL.OBJnumRulEli>0){
     int i = 0;
     if (LVL.OBJrulEli[0][0] == 1) {
       nivY= y+23;
       bcont = false;
       //Dibujar fondo en la posición de la opción seleccionada
       offset = MENpauCnf[MENid * 4 + 1] + MENop * 2;
       MEN_DibRect(g, MENpauPos[offset], y,
                   MENpauPos[offset] + MENpauPos[offset + 1], y + 16, true,
                   0xFFA6BDD3, 0xFF738AAB);
       //Imprimir iconos
       HUD_DibImg(g, 1, 168, 27);
       HUD_DibImg(g, DEVscrPxlW - 7, 168, 28);
       g.setClip(0, 0, DEVscrPxlW, DEVscrPxlH);
       //Imprimir cabeceras objetivos
       g.setColor(0x3B465A); //azul fuerte
       g.drawLine(1, y + 19, 22, y + 19);
       g.drawLine(27, y + 19, 174, y + 19);
       g.setColor(0xFFFFFF);
       g.drawString("Col", 4, y + 3, Graphics.TOP | Graphics.LEFT);
       g.drawString("Dimensiones", 30, y + 3, Graphics.TOP | Graphics.LEFT);

       //Imprimir descripción item
       g.setColor(0xFFFFFF);
       g.setFont(fSbt);
       offset = MENpauCnf[MENid * 4 + 2] + MENop * 3;
       g.drawString(MENpauTxt[offset], 12, 150, Graphics.TOP | Graphics.LEFT);
       g.setColor(0x224466);
       g.setFont(fCnt);
       g.drawString(MENpauTxt[offset + 1], 12, 163,
                    Graphics.TOP | Graphics.LEFT);
       g.drawString(MENpauTxt[offset + 2], 12, 176,
                    Graphics.TOP | Graphics.LEFT);

       //Imprimir contenido objetivos
       offset = MENpauCnf[MENid * 4 + 1];
       g.setFont(fCnt);

       for (i = 0; i < LVL.OBJnumRulEli; i++) {
         if (LVL.OBJrulEli[i][0] != 1) {
           bcont = true;
           break;
         }
         g.setColor(0xFFFFFF);
         //Eliminación por color
         if (LVL.OBJrulEli[i][1] != -2)
           HUD_DibImg(g, 5, nivY + 1, 73 + LVL.OBJrulEli[i][1]);
         g.setClip(0, nivY, DEVscrPxlW, 14);
         //Eliminación por dimensiones
         a = LVL.OBJrulEli[i][2];
         b = LVL.OBJrulEli[i][3];
         if (a != -1 || b != -1){
           if(LVL.OBJrulEli[i][4] == 0)
             g.drawString( "<"+(a != -1 ? Integer.toString(a) : "?") + "x" + (b != -1 ? Integer.toString(b) : "?")+
                           (a!=b? (" ó "+ "<"+(b != -1 ? Integer.toString(b) : "?") + "x" + (a != -1 ? Integer.toString(a) : "?")):""),
                           MENpauPos[offset + 1 * 2], nivY,
                           Graphics.TOP | Graphics.LEFT);
             else if (LVL.OBJrulEli[i][4] == 1)
               g.drawString( ">"+(a != -1 ? Integer.toString(a) : "?") + "x" + (b != -1 ? Integer.toString(b) : "?")+
                             (a!=b? (" ó "+ ">"+(b != -1 ? Integer.toString(b) : "?") + "x" + (a != -1 ? Integer.toString(a) : "?")):""),
                             MENpauPos[offset + 1 * 2], nivY,
                             Graphics.TOP | Graphics.LEFT);
              else
                g.drawString( (a != -1 ? "Ancho > "+Integer.toString(a) : "?") + " Y " +
                              (b != -1 ? "Alto > "+Integer.toString(b) : "?"),
                              MENpauPos[offset + 1 * 2], nivY,
                              Graphics.TOP | Graphics.LEFT);

         }

         nivY += 14;
         g.setColor(0x738AAB);
         g.drawLine(2, nivY - 3, 173, nivY - 3);
       }
     }

     //Si hay reglas de eliminación de tipo 0 ó de tipo 2
     if (bcont) {
       if (LVL.OBJrulEli[i][0] == 2) {
         i1=52;
         i2=53;
       }
       else //==0
         if (LVL.OBJrulEli[i][1] == 1){
           i1=54;
           i2=57;
         } else{
           i1=55;
           i2=56;
           i3=57;
         }
     }
   }

   //Si el flag de eliminación por generación está activado
   if (LVL.OBJbEliGen) {
     bcont = true;
     i1=51;
     i2=53;
   }

   if(bcont){
     HUD_DibImg(g, 5, nivY + 4, 90);
     g.setClip(0, nivY, DEVscrPxlW, 42);
     g.setColor(0xFFFFFF);
     g.drawString(GAMstr[i1], 12, nivY,Graphics.TOP | Graphics.LEFT);
     if(i2!=-1)
       g.drawString(GAMstr[i2], 12, nivY+14,Graphics.TOP | Graphics.LEFT);
     if(i3!=-1)
       g.drawString(GAMstr[i3], 12, nivY+28,Graphics.TOP | Graphics.LEFT);
   }

}


//----------------------------------------------------------------- HUD_DibTim
// Desc: Imprime el tiempo de juego en el nivel en curso, o el tiempo récord
//       del nivel.
// Param: Si pfase = -1 se imprime el tiempo actual.
//---------------------------------------------------------------------------
private void HUD_DibTim(Graphics g, short py, byte pfase, byte pniv){
   //Imprimir tiempo de nivel
   if(pfase != -1)
     for(byte i=0; i<pfase; i++)
       pniv += LVL.confFaseNivMod[2][i];
   
   //#if CFGfnt==0    
   g.setClip(0, py, DEVscrPxlW, 14);
   g.setFont(pfase != -1 ? fCnt : fSbt);
   g.setColor(0x224466);
   g.drawString(pfase != -1 ? "Tiempo Record:" : "Tiempo Nivel:", pfase!=-1?24:32, py,
                Graphics.TOP | Graphics.LEFT);
   //#else
//#    TXT_DibLinea(g,pfase != -1 ? "Tiempo Record:" : "Tiempo Nivel:", pfase!=-1?24:32, py,-1);
   //#endif
   
   g.setColor(pfase!=-1?0xFFFFFF:0x224466);
   if (pfase != -1 && COMBIMidlet.M.MENtimRecord[pniv] == 0){
    //#if CFGfnt==0    
    g.drawString("--:--", DEVscrPxlW - 24, py,Graphics.TOP | Graphics.RIGHT);
    //#else
//#     TXT_DibLinea(g, "--:--", DEVscrPxlW - 24, py, 1);   
    //#endif
   }
   else {
     int min = (int) ( (pfase != -1 ? COMBIMidlet.M.MENtimRecord[pniv] :
                        HUDtimgamAct) / 1000) / 60;
     int sec = (int) ( (pfase != -1 ? COMBIMidlet.M.MENtimRecord[pniv] :
                        HUDtimgamAct) / 1000) % 60;
     //#if CFGfnt==0
     g.drawString( (min < 10 ? "0" : "") + Integer.toString(min) + ":" +
                  (sec < 10 ? "0" : "") + Integer.toString(sec),
                  DEVscrPxlW-(pfase!=-1?24:32), py, Graphics.TOP | Graphics.RIGHT);
     //#else
//#      TXT_DibLinea(g, (min < 10 ? "0" : "") + Integer.toString(min) + ":" +
//#                   (sec < 10 ? "0" : "") + Integer.toString(sec),
//#                   DEVscrPxlW-(pfase!=-1?24:32), py, 1);   
     //#endif
   }
 }

//----------------------------------------------------------------- HUD_DibPun
 private void HUD_DibPun(Graphics g, short py, byte pRec){
    //#if CFGfnt==0  
    g.setClip(0, py, DEVscrPxlW, 14);
    g.setFont(pRec==1?fCnt:fSbt);
    g.setColor(0x224466);
    g.drawString(pRec==1?"Record Puntos:":"Puntos:", pRec==1?24:32, py, Graphics.TOP | Graphics.LEFT);
    g.setColor(pRec==1?0xFFFFFF:0x224466);
    g.drawString(Integer.toString((pRec==1?COMBIMidlet.M.MENscoreRecord:GAMscore)),DEVscrPxlW-(pRec==1?24:32),py,Graphics.TOP | Graphics.RIGHT);
    //#else
//#     TXT_DibLinea(g,pRec==1?"Record Puntos:":"Puntos:", pRec==1?24:32, py, -1);   
//#     TXT_DibLinea(g,Integer.toString((pRec==1?COMBIMidlet.M.MENscoreRecord:GAMscore)), DEVscrPxlW-(pRec==1?24:32), py, 1);      
    //#endif
 }

 
 
//////#if CFGfnt==0  
//----------------------------------------------------------------- HUD_DibTxt
// Desc: Imprime líneas de la página del texto correspondiente al nivel
//----------------------------------------------------------------------------
private void HUD_DibTxt(Graphics g, short pY, byte piniLin, byte pnumlin, int pcol){
     
  g.setFont(fCnt);
  g.setColor(pcol);
  for (int i = 0; i < pnumlin; i++) {
    if(piniLin+i >= LVL.TXTstr.length)
      break;
    g.drawString(LVL.TXTstr[piniLin+i], 6, pY, Graphics.TOP | Graphics.LEFT);    
    pY += 14;
  } 
  
}
//////#endif


//----------------------------------------------------------------- HUD_DibTut
// Desc: Dibuja diálogo de Tutorial/Instrucciones inicio de nivel
//----------------------------------------------------------------------------
private void HUD_DibTut(Graphics g){
    int iniX = xPoints[0] = xPoints[3] = 1;
    int finX = xPoints[1] = xPoints[2] = DEVscrPxlW-2;
    int iniY = yPoints[0] = yPoints[1] = MENsit[LVL.sitPag*2];
    int finY = MENsit[LVL.sitPag*2+1];


    //Dibuja ventana
    g.setClip(0, iniY-2, DEVscrPxlW, DEVscrPxlH-iniY+2);
    MEN_DibRect(g,iniX,iniY,finX,iniY + 18, true, 0xC0fceecd, 0x3B465A); //Área cabecera
    MEN_DibRect(g,iniX,iniY+18,finX,finY, true, 0x90336699, 0x3B465A); //Área cuerpo
    //Dibujar marco
    g.setColor(0xFDAC8E); //color salmón
    g.drawRect(iniX-1, iniY-1, finX-iniX+2, finY-iniY+2);

    //Imprimir título de la fase y número de nivel en fase
    //#if CFGfnt==0
    g.setClip(0, iniY+2, DEVscrPxlW, 16);
    g.setFont(fSbt);
    g.setColor(0x224466);    
    g.drawString(GAMstr[LVL.idxStrTit],iniX+5,iniY+4,Graphics.TOP | Graphics.LEFT);
    g.drawString("Nivel: "+Integer.toString(LVL.idNiv+1), finX-2, iniY+4, Graphics.TOP | Graphics.RIGHT);
    //#else
//#     TXT_DibLinea(g,GAMstr[LVL.idxStrTit],iniX+5,iniY+4,-1);
//#     TXT_DibLinea(g,"Nivel: "+Integer.toString(LVL.idNiv+1),finX-2, iniY+4,1);
    //#endif

    //Imprimir líneas de la página
    //#if CFGfnt==0
    //g.setClip(0, iniY, DEVscrPxlW, DEVscrPxlH-iniY);
    //HUD_DibTxt(g, (short)(iniY + NUMPIXELS_TILEY + 8), TXTidLinAct, (byte)4, 0xFFFFFF);
    TXT_DibLineasString(g, LVL.HUDstr, TXTiniPos, TXTnumCars, 6, iniY + NUMPIXELS_TILEY + 8, DEVscrPxlW-12, -1);
    //#else
//#     //TXT_DibLineasString(g, LVL.HUDstr, TXTiniPos, TXTnumCars, DEVscrPxlW/2, iniY + NUMPIXELS_TILEY + 8, DEVscrPxlW-12, 0);
//#     TXT_DibLineasString(g, LVL.HUDstr, TXTiniPos, TXTnumCars, 6, iniY + NUMPIXELS_TILEY + 8, DEVscrPxlW-12, -1);
    //#endif
    
    //Dibuja softkeys para navegación del texto
    MENmsg = -1;
    MEN_DibKeys(g,TXTsig?(byte)9:8); //LVL.bTut);
    if(TXTsig)
      HUD_DibImg(g,6,DEVscrPxlH-12,30);
    if(TXTant)
      HUD_DibImg(g,DEVscrPxlW-14,DEVscrPxlH-10,29);
}




//--------------------------------------------------------------- HUD_SoftLeft
// Desc: Gestionar pulsación teclas SOFTKEYS durante el juego (o tutorial)
//----------------------------------------------------------------------------
private void HUD_SoftLeft(){
    if(GAMbNivIntro){
      if (!TXTsig) { //si estado es CONTINUAR por llegar a final de texto
        GAMbNivIntro = false; //Abandonamos texto introductorio al nivel

        //Comprobar si hay objetivos que cumplir en el nivel
        boolean bHayobjs=false;
        for (int i = 0; i < GAMnumRulObj; i++)
          if (GAMobjSts[i * 2] != 0)
            bHayobjs=true;

        if(!bHayobjs || GAMobjProg==100){ //Si no hay objetivos, o los que hay se han cumplido durante el texto introductorio, pasamos al siguiente nivel
          GAMsts = PLAY_OUT_CHGNIV;
          GAMsbSts = 0;
        }
        else{ //Si hay objetivos mostramos marcador e iniciamos tiempo de juego
          HUDbIni = GAMbHUDon = true;
          TIM_IniGam();
        }
      }
      else
//        //#if CFGfnt==0                    
//        if (TXTidLinAct < (LVL.TXTstr.length-1))
//            TXT_LinUpd(NUMLINTXT);          
//        //#else
        {
            TXTiniPos += TXTnumCars;
            TXTnumCars = TXT_ObtLongTextoFwd(LVL.HUDstr, TXTiniPos, 56, DEVscrPxlW-12);
            TXTsig = (TXTiniPos+TXTnumCars) >= LVL.HUDstr.length()? false:true;
            TXTant = true;
        }
//        //#endif

    }else{
        TIM_IniPausa();
        GAMsts = PLAY_OUT_PAUSA;
        MENid = MENop = 0;
    }
}

//--------------------------------------------------------------- HUD_SoftRight
// Desc: Gestionar pulsación teclas SOFTKEYS durante el juego (o tutorial)
//----------------------------------------------------------------------------
private void HUD_SoftRight(){
    if(GAMbNivIntro){
//        //#if CFGfnt==0                   
//        if(TXTidLinAct > 0)
//          TXT_LinUpd(-NUMLINTXT);
//        //#else
        if(TXTant)
	{
          TXTnumCars = TXT_ObtLongTextoBwd(LVL.HUDstr, TXTiniPos-1, 56, DEVscrPxlW-12);
	  TXTiniPos -= TXTnumCars;
	  TXTsig = true;
	  TXTant = TXTiniPos==0?false:true;		
 	}
//        //#endif
    }else{
        TIM_IniPausa();
        GAMsts = PLAY_OUT_PAUSA;
        MENid = MENop = 0;
    }
}




//#if CFGfnt==0
// Desc: Actualiza línea de la página actual del tutorial
private void TXT_LinUpd(int pInc){
    TXTidLinAct += pInc;
    if((TXTidLinAct + NUMLINTXT) >= LVL.TXTstr.length)
      TXTsig = false; //OK. Fin texto
    else
      TXTsig = true; //continuar a sig pagina

    if(TXTidLinAct == 0){
      TXTant = false; //No hay pag anterior
      HUDbIni= true;
    }
    else
      TXTant = true; //hay pag anterior
}
//#endif



///#if CFGfnt != 0 
//*****************************************************************************
//************************************************************************ TXT
//*****************************************************************************


//--------------------------------------------------------- TXT_ObtLongTextoBwd
// Desc: Idem que Fwd pero hacia atrás
// Return: Logitud en cars de las numlin calculadas, incluyendo espacios y retcarros.

private int TXT_ObtLongTextoBwd(String pStr, int pPosFin, int pLimAlto, int pLimAncho)
{
	int posFinAct =0;
	int pos=0;
	while(posFinAct < pPosFin)            
	{
            pos = posFinAct;
            int numcars = TXT_ObtLongTextoFwd(pStr,pos,pLimAlto,pLimAncho);
            posFinAct = pos + numcars;
	}
	return posFinAct - pos;
}

//--------------------------------------------------------- TXT_ObtLongTextoFwd
// Desc: Determina la longitud total en caracteres de las líneas de texto 
//       consecutivas que caben dentro del cuadrante limitado por 
//       <pLimAlto>x<pLimAncho>, extraidas del string <pStr> a partir de la
//        posición <pPosini> inclusive.
// Return: Logitud en cars de las numlin calculadas, incluyendo espacios y retcarros.

private int TXT_ObtLongTextoFwd(String pStr, int pPosIni, int pLimAlto, int pLimAncho)
{
	int pos=pPosIni;
	int numlinMax = pLimAlto/FNTH;
	int numlin=0;

	while(numlin < numlinMax && pos < pStr.length())
	{
		pos += TXT_CalcLinea(pStr,pos,pLimAncho) + 1; //suma espacio final o /n de la linea
		numlin++;
	}
	return pos-pPosIni;
}


//-------------------------------------------------------------- TXT_CalcLong
private int TXT_CalcLong(String pStr)
{
    //#if CFGfnt != 0
//#     int ancho = 0; 
//#     for(int i=0; i<pStr.length();i++)
//#         ancho += FNTancho[FNT_ObtPos(pStr.charAt(i))]-1;
//#     return ancho;    
    //#else
    char[] c= new char[pStr.length()];
    pStr.getChars(0,pStr.length(),c,0);   
    int ancho = fCnt.charsWidth(c,0,pStr.length());
    return ancho;
    //#endif
    
}

//-------------------------------------------------------------- TXT_CalcLinea
// Desc: Calcula longitud de línea "bien formada" contenida en un string
//       Bien formada: delimitada por espacio final (no corta palabras) o CR
// Param: pStr - string
//        pPosIni - pos del string a partir de la cual empezar a calcular el substr
//        pLimAncho - tamaño máximo de línea en pixels (línea bien troceada)
//        El último espacio tras la última palabra, se incluye en numlineas???????????
// Return: Logitud en cars del substr que conforma la línea
//----------------------------------------------------------------------------
private int TXT_CalcLinea(String pStr, int pPosIni, int pLimAncho)
{
        int numcarsFin = 0;  //Num caracteres final
        int numcarsAcum = 0;  //Num caracteres acumulados
        int anchoAcum = 0;  //Anchura acumulada en pixels 
	boolean bFin = false;

	do
                if(pPosIni >= pStr.length())
                {
                    numcarsFin = numcarsAcum;
                    bFin = true;
                } else
                {
                    char c1 = pStr.charAt(pPosIni);
                    switch(c1)
                    {
                    case 32: // ' '                       
                        numcarsFin = numcarsAcum;
                        numcarsAcum++;
                        //#if CFGfnt != 0
//#                         anchoAcum += FNTancho[FNT_ObtPos(c1)]-1;
                        //#else
                        anchoAcum += fCnt.charWidth(c1);
                        //#endif
                        break;

                    case 10: // '\n'
                        numcarsFin = numcarsAcum;
                        bFin = true;
                        break;

                    default:
                        //#if CFGfnt != 0
//#                         anchoAcum += FNTancho[FNT_ObtPos(c1)]-1;
                        //#else
                        anchoAcum += fCnt.charWidth(c1);
                        //#endif
                        numcarsAcum++;
                        break;
                    }
                    pPosIni++;
                    if(anchoAcum > pLimAncho)
                        bFin = true;
                }
	while(!bFin);
        return numcarsFin;
    }


//--------------------------------------------------------- TXT_DibLineasString
// Desc: Dibuja string en formato multilinea (automáticamente)
// Param: pStr - string
//        pPosIni - pos del string a partir de la cual empezar a dibujar
//        pNumcars- Num caracteres a dibujar a partir de pPosIni, o -1 si hasta fin string 
//        pLimAncho- tamaño máximo de línea en pixels (línea bien troceada)
//----------------------------------------------------------------------------
private void TXT_DibLineasString(Graphics g, String pStr, int pPosIni, int pNumCars, int px, int py, int pLimAncho, int pModo)
{
	int posMax = pNumCars==-1?pStr.length() : pPosIni+pNumCars;
        for(int pos=pPosIni; pos<posMax;)
	{
		int numcarslin = TXT_CalcLinea(pStr, pos, pLimAncho); //+1; //suma espacio final o /n de la linea
		StringBuffer substr = new StringBuffer();
		for(int i=0; i<numcarslin; i++)
			substr.append(pStr.charAt(pos++));
		pos++;
                //#if CFGfnt != 0 
//# 		TXT_DibLinea(g, substr.toString(), px, py, pModo);
//# 		py +=FNTH;	//Alto fuente en pixels: 14	
                //#else
                g.setFont(fCnt);
                g.setClip(px, py, TXT_CalcLong(substr.toString()), fCnt.getHeight());
                g.drawString(substr.toString(), px, py, 20);
                py += fCnt.getHeight();
                //#endif
	}	
}


//#if CFGfnt != 0
//# //--------------------------------------------------------------- TXT_DibLinea
//# // Desc: Dibuja string <pStr> en posiciones globales TXTx, TXTy
//# // Param: pModo- Justificación (-1 izq, 0 centrado, 1 derecha)
//# //----------------------------------------------------------------------------
//# private void TXT_DibLinea(Graphics g, String pStr, int px, int py, int pModo)
//# {
//#         if(pModo == 1){
//#             px -= TXT_CalcLong(pStr);
//#         }
//#         else if (pModo ==0){
//#             px -= TXT_CalcLong(pStr)>>1;
//#         }
//# 	for(int i=0; i<pStr.length(); i++)
//# 	{
//# 		char c = pStr.charAt(i);
//# 		TXT_DibCar(g, c, px, py);
//# 		px += FNTancho[FNT_ObtPos(c)]-1; //-1: continuidad de fuentes		                               
//# 	}
//# }
//# 
//# 
//# //----------------------------------------------------------------- TXT_DibCar
//# // Desc: Dibuja caracter <c> en posiciones globales TXTx, TXTy
//# //----------------------------------------------------------------------------
//# private void TXT_DibCar(Graphics g, char c, int px, int py)
//# {
//# 	int idx = FNT_ObtPos(c);
//# 	int j = idx / 16;  //Num elementos por linea:16 = (imgFnt.getWidth()/anchofuentefijo)
//# 	int i = idx % 16;  //Num elementos por linea:16
//# 	g.setClip(px, py, FNTancho[idx], FNTH); //Alto fuente en pixels:14
//# 	g.drawImage(imgFnt, px-i*FNTW, py-j*FNTH, 20); //Ancho fuente en pixels: 8, alto:14
//# }
//# 
//# 
//# 
//# 
//# //----------------------------------------------------------------- FNT_ObtPos
//# // Desc: Obtiene ancho de pixels de caracter <c>
//# //----------------------------------------------------------------------------
//# private int FNT_ObtPos(char c)
//# {
//# 	int idx = c-32;
//# 	if(c > 122){ //si es un caracter especial
//# 		if(c<125)
//# 			idx -=70; //192(Á)-122(z) (Á y z a continuación en set gráfico)
//# 		else if(c <140)
//# 			idx -=78;
//# 		else if(c <210)
//# 			idx -=118;
//# 		else
//# 			idx -=130;
//# 	}	
//# 	return idx;		
//# }
//#endif
///#endif


//*******************************************************************************************
//********************************************************************* CANVAS
//*******************************************************************************************
public COMBICanvas(COMBIMidlet parent)
{
       System.gc();

//Preinicialización sistema gráfico
       DEVscrPxlH = getHeight();
       DEVscrPxlW = getWidth();
    
//Definición de variables para colocación correcta de pantalla   
       HUD_RI = 0;
       if(DEVscrPxlW == 176){ 
           GAMscrPatchLE=1;
           GAMscrPxlLE = 0;
           GAMscrPxlRI = 0;
       }    
       else{
           GAMscrPatchLE=0;
           if(DEVscrPxlH < 160){ //ALtura mínima para que quepa el HUD entero
               HUD_RI = 1;
               GAMscrPxlLE = 0;
               GAMscrPxlRI = (short)(DEVscrPxlW - GAMscrPxlW);
           }
           else          
            GAMscrPxlLE = GAMscrPxlRI = (short)((DEVscrPxlW - GAMscrPxlW)/2);
       }
           
       
       
       SCRdg = null;

       //[S40]:
       //BGRimg1 = Image.createImage(14*NUMPIXELS_TILEX,14*NUMPIXELS_TILEY);
       //BGRimg2 = Image.createImage(14*NUMPIXELS_TILEX,14*NUMPIXELS_TILEY);
       //[S60]:
       BGRimg1 = Image.createImage((14+GAMscrPatchLE)*NUMPIXELS_TILEX,14*NUMPIXELS_TILEY);
       BGRimg2 = Image.createImage((14+GAMscrPatchLE)*NUMPIXELS_TILEX,14*NUMPIXELS_TILEY);
       //Fin[S60]
       BGRg1 = BGRimg1.getGraphics();
       BGRdg1 = DirectUtils.getDirectGraphics(BGRg1);
       BGRg2 = BGRimg2.getGraphics();
       BGRdg2 = DirectUtils.getDirectGraphics(BGRg2);

       DEVntvFmt = DirectGraphics.TYPE_USHORT_4444_ARGB;
//Fuentes
    fTit = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    fSbt = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_SMALL);
    fCnt = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);


//Sonidos
//S40 (versión ott - sin MMAPI)
/*

    try{
      InputStream is = getClass().getResourceAsStream("/sound.bin");
      SNDdat = new Sound[3];
      for (int i = 0; i < SNDlen.length; i++) {
        byte aSnd[] = new byte[SNDlen[i]];
        is.read(aSnd);
        SNDdat[i] = new Sound(aSnd, 1);
        SNDdat[i].init(aSnd, 1); //requerido en S60 7650
      }
      is.close();
    }catch (Exception ex)
    {
      ex.printStackTrace();
 }

*/



/*
    try{
      byte aSnd[] = new byte[62];
      InputStream is = getClass().getResourceAsStream("/sound.bin");
      is.read(aSnd);
      SNDfx1 = new Sound(aSnd, Sound.FORMAT_TONE);
      System.out.println("SOund cargado");
      is.close();
      is = null;
      aSnd = null;
    }
    catch(Exception ex){ex.printStackTrace();}
*/

 

             
             
//Actualización variables display en pantalla
    HUDtimMax = 40;

    //Carga de información estática
    CIO = new COMBIIO();  //Crea datos descriptivos de obstáculos, objetivos y gráficos
    LVL = new COMBILVL(); //Crea nivel de juego
    //#if CFGtipoSnd == 2
    MUS = new COMBIMusic();
    //#endif
    NIVidPieza = new short[784]; //Escenario de dimensiones máximas 28*28, (14*14* 4 pantallas)
    NIVnumPart = new byte[784]; //Escenario de dimensiones máximas
    xPoints = new int[4];
    yPoints = new int[4];




    VGsts = VGSTS_LOAD;
    GAMsts = 0;
    GAMbsplash=true;
    PIXnum = 6;
    System.gc();
    run();
}

//----------------------------------------------------------------- GAM_Unload
// Desc: Libera memoria antes de salir
public void GAM_Unload(){
   //borrar objetos
   LVL = null;
   CIO = null;
   //Borrar todos los arrays usados para el nivel
   NIVidPieza = null;
   NIVnumPart = null;
   PIEdat = COMdat = null;
   PIEtmp = COMtmp = null;
   //Aux
   GAMpie = null;
   CURcont = null;
   FXdat = null;
   xPoints = null;
   yPoints = null;
   sTime = null;
   rand = null;
   //Pix
   GAMspr = null;
   GAMpiePix = null;
   CURpix = null;
   GAMobjSts = null;
   System.gc();
}

//-------------------------------------------------------------------- LOA_Load
private void LOA_Tick(){
         if(GAMsts==0){
           //Patch: problemas con ciertos S60 al retornar tamaño pantalla
           DEVscrPxlH = getHeight();
           DEVscrPxlW = getWidth();
           // Carga gráficos permanentes
           if(PIXnum>=0){
              GAM_CargarGraficos(); // una llamada por grafico a cargar
             PIXnum--;
           }else
             GAMsts++;
         }
         else if (GAMsts > 0){
           actorAccion = disc_actorAccion;
           if (actorAccion != STOP){
             System.gc();
             //Selección del tipo de juego
             VGsts = VGSTS_MENU;
             MENid = MENop = 0;
             MENmsg = -1;

              
             //S40 (.ott si no hay Mobile Media API (MMAPI) )
             //SND_Play(0);
             //S60 (versión MMAPI)
             if(COMBIMidlet.M.MENbMus)
               //#if CFGtipoSnd  == 2
                MUS.TocaMusica(-1);
                //#else 
                     //#if CFGtipoSnd == 1
//#                        SND_Play(0);             
                    //#endif
                //#endif             
             disc_actorAccion = STOP;
           }
         }
}


private void LOA_Dibujar(Graphics g){
   if (GAMbsplash){
     Image image = null;
     try
     {
         image = Image.createImage("/logo.png");
     }
     catch(Exception exception1) { }
     g.drawImage(image, DEVscrPxlW/2,20,Graphics.TOP | Graphics.HCENTER);
     image = null;
     System.gc();
     GAMbsplash = false;
   }
   g.setFont(fCnt);
   if (GAMsts == 0){
     g.setColor(0x606060);
    //#if CFGtamanyoGfx == 2
//#      g.drawString(GAMstr[29],DEVscrPxlW/2,180,Graphics.TOP | Graphics.HCENTER);
//#      g.drawString(GAMstr[37],30,160,Graphics.TOP | Graphics.LEFT);
     //#else
     g.drawString(GAMstr[29],DEVscrPxlW/2,DEVscrPxlH,Graphics.BOTTOM | Graphics.HCENTER);
     g.drawString(GAMstr[37],30,118,Graphics.BOTTOM | Graphics.LEFT);
     //#endif
     
//#if CFGtamanyoGfx == 2
//# //Dibujar barra de progreso en función de nº de grafico cargado
//#      
//#      g.setColor(0xBBCCBB);
//#      g.drawRect(84,163,63,7);
//#      g.setColor(0xFF0000);
//#      g.fillRect(86,165,(5-PIXnum)*12,4);
     //#endif
   }
   else{ //Dibujar mensaje de pulsar tecla.
     g.setColor(0xFFFFFF);
     //#if CFGtamanyoGfx == 2
//#      g.fillRect(0,160,DEVscrPxlW,DEVscrPxlH-160);
//# 
//#      if((MENid = (byte)((MENid+1)%10))==0)
//#        VISbUpd=!VISbUpd;
//#      g.setColor(VISbUpd?0xe0e0e0:0x000000);
//#      g.setFont(fCnt);
//#      g.drawString(GAMstr[38], DEVscrPxlW / 2, 170, Graphics.TOP | Graphics.HCENTER);
//#      g.setFont(fSbt);
//#      g.setColor(0x224466);
//#      g.drawString("www.imagame.com", DEVscrPxlW / 2, 190, Graphics.TOP | Graphics.HCENTER);
     //#else
         g.drawString(GAMstr[38],DEVscrPxlW / 2,118,Graphics.BOTTOM | Graphics.HCENTER);
     //#endif
   }
}


//------------------------------------------------------------ MEN_ObtIdxLvl
// Desc: Obtiene pos del modo-fase en el array de Niveles abiertos por fase
private byte MEN_ObtIdxLvl(byte pmod, byte pfas){
   byte idx=0;
   if(pmod==1)
     idx = LVL.confFaseMod[0];
   else if(pmod==2)
     idx = (byte)(LVL.confFaseMod[0] + LVL.confFaseMod[1]);
   idx += pfas;
   return idx;
}

//---------------------------------------------------------------- MEN_InitGame
// Param: Indica si inicio de partida o continuación desde otro nivel
private void MEN_InitGame(boolean pstart){
 LVL.CargarNivel();
 NIV_Init(pstart); //Inicializar datos nivel cargado
 VIS_Init();
 VGsts = VGSTS_GAME;
 GAMsts = PLAY_IN_INIT;
 GAMsbSts = 0;
}

//-------------------------------------------------------------------- MEN_Tick
private void MEN_Tick(){
actorAccion = disc_actorAccion;
if (actorAccion != STOP) //reseta mensaje si hay pulsación de tecla
  MENmsg = -1;

//Menú Atrás: aplica a todos los menús
if (actorAccion == SOFTRIGHT) {
  if (MENid == 0)
    MENid = 6;
  else
    MENid = 0;
}

if (MENid == 0) { //Menú principal
  if (actorAccion == FIRE || actorAccion == SOFTLEFT) { //aceptar opción seleccionada
    MENid = (byte) (MENop + 1);
    MENval1 = MENval2 = 0;
    MENsubop = 0;
  }
  else if (actorAccion == ARR) {
    if (--MENop < 0)
      MENop = 5;
  }
  else if (actorAccion == ABA)
    MENop = (byte) ( (MENop + 1) % 6);
}

else if (MENid == 1 || MENid == 2 || MENid == 3) { //Menús Juego
  if (actorAccion == FIRE || actorAccion == SOFTLEFT) {
    if (COMBIMidlet.M.MENnivOpen[MEN_ObtIdxLvl((byte)(MENid - 1), MENval1)] >= MENval2) {
         //#if CFGtamanyoGfx == 2        
//#       imgCab = null;
      //#endif
      System.gc();
      LVL.idModo = (byte) (MENid - 1);
      LVL.idFase = MENval1;
      LVL.idNiv = MENval2;
      //#if CFGtipoSnd == 2
      if(COMBIMidlet.M.MENbMus) //Parar la música durante la partida
       MUS.Stop();
      //#endif
      MEN_InitGame(true);
    }
  }
  else if (actorAccion == ARR) {
    if (--MENsubop < 0)
      MENsubop = 1;
  }
  else if (actorAccion == ABA)
    MENsubop = (byte) ( (MENsubop + 1) % 2);
  else if (actorAccion == IZQ) {
    if (MENsubop == 0 && MENval1 > 0) {
      MENval1--;
      MENval2 = 0;
    }
    else if (MENsubop == 1 && MENval2 > 0)
      MENval2--;
    else
      MENmsg = 24;
  }
  else if (actorAccion == DER) {
    MENmsg = 25;
    if (MENsubop == 0 && MENval1 < LVL.confFaseMod[MENid - 1] - 1) {
      MENval1++;
      MENval2 = 0;
      MENmsg = -1;
    }
    else if (MENsubop == 1) {
      if (MENval2 < LVL.confFaseNivMod[MENid-1][MENval1] - 1) { //si no ha llegado al máximo nivel de la fase
        MENval2++;
        MENmsg = -1;
      }
    }
  }
}

//Menú Opciones
else if (MENid == 4) {
  if (actorAccion == FIRE || actorAccion == SOFTLEFT) {
    if (MENsubop == 0){
      COMBIMidlet.M.MENbMus = !COMBIMidlet.M.MENbMus;
      //#if CFGtipoSnd == 2
      if(COMBIMidlet.M.MENbMus)
        MUS.TocaMusica(-1);
      else
        MUS.Stop();
      //#endif
    }
    else
      COMBIMidlet.M.MENbSnd = !COMBIMidlet.M.MENbSnd;
    COMBIMidlet.M.CNF_Save(false);
  }
  else if (actorAccion == ARR) {
    if (--MENsubop < 0)
      MENsubop = 1;
  }
  else if (actorAccion == ABA)
    MENsubop = (byte) ( (MENsubop + 1) % 2);
}

//Menú Creditos
else if (MENid == 5) {
  if(actorAccion != STOP && MENcheatSts < 6){
    if (MENcheatCode[++MENcheatSts] == actorAccion) {
      //System.out.println("FX: Sound código secuencia correcto");
      if (MENcheatSts == 6) { //CHEAT: Si se acierta la secuencia correcta se desbloquean los niveles
        MENmsg = 15; //Mensaje único al terminar de introducir la secuencia correcta
        byte idx = -1;
        for (byte i = 0; i < 3; i++) //para cada modo
          for (byte j = 0; j < 4; j++) //para cada fase del moo
            if (LVL.confFaseNivMod[i][j] != 0)
              COMBIMidlet.M.MENnivOpen[++idx] = (byte) (LVL.confFaseNivMod[i][j] - 1);
      }
    }
    else {
      //if (MENcheatSts > 0)
      //  System.out.println("FX: Sound código secuencia erróneo");
      MENcheatSts = -1;
    }
  }

}

//Menú Salir
else if (MENid==6 && actorAccion == SOFTLEFT){
  COMBIMidlet.salir();
  VGsts = -1; 
}

disc_actorAccion = STOP;
}


//----------------------------------------------------------------- MEN_Dibujar
private void MEN_Dibujar(Graphics g){
    int offset;
    int MENscrPxlH = DEVscrPxlH-(MENscrPxlUP+MENscrPxlDO+9);
 
    g.setColor(0xFDAC8E); //Color salmon fondo pantalla
    g.setClip(0, 0, DEVscrPxlW, DEVscrPxlH);
    g.fillRect(0,0,DEVscrPxlW,DEVscrPxlH);
 
    g.setColor(0x88AACC);    //color fondo azul area menu
    g.fillRect(5,MENscrPxlUP+4,DEVscrPxlW-9,MENscrPxlH);
    
    g.setColor(0x3B465A); //color azul fuerte bordes pantalla y bordes area menu
    g.drawRect(0,0,DEVscrPxlW-1,DEVscrPxlH-MENscrPxlDO-1); 
    g.drawRect(4,MENscrPxlUP+3,DEVscrPxlW-9,MENscrPxlH); //(64+4+16+5-1)
    

    //Dibujar cabecera gráfico
    //#if CFGtamanyoGfx == 2
//#     try {
//#       if(imgCab==null)
//#         imgCab = Image.createImage("/cab.png");
//#     }
//#     catch (Exception e) {}
//#     g.drawImage(imgCab, DEVscrPxlW/2, 1, Graphics.TOP | Graphics.HCENTER);
    //#else
     g.setColor(0x661100);
     g.setFont(fTit);    
     g.drawString("COMBImanía",DEVscrPxlW/2,2,Graphics.TOP | Graphics.HCENTER);
    //#endif
    
    //Dibujar pie
    MEN_DibKeys(g, MENid);

    //Dibujar título submenú
    //#if CFGtamanyoGfx == 2
//#      MEN_DibRect(g,DEVscrPxlW/2-22,42, DEVscrPxlW-5, 62, false, 0xB0336699, 0);
    //#else
     MEN_DibRect(g,4,15, DEVscrPxlW-5, 29, false, 0xB0336699, 0);
    //#endif
     g.setColor(0xFFFFFF);
    //#if CFGtamanyoGfx == 2
//#      g.setFont(MENid==3?fSbt:fTit);
//#      g.drawString(GAMstr[MENid==0?6:MENid-1], (DEVscrPxlW-5 - (DEVscrPxlW/2-22))/2+(DEVscrPxlW/2-22), 57,  Graphics.BASELINE | Graphics.HCENTER);
    //#else
     g.setFont(fTit);
     g.drawString(GAMstr[MENid==0?6:MENid-1], DEVscrPxlW/2, 16, Graphics.TOP | Graphics.HCENTER);
    //#endif

    
    int MENscrPxlH1 = (MENscrPxlH-(6*MENscrPxlOp+1))/2; 
    int nivY = MENscrPxlUP+4+MENscrPxlH1;
    if(MENid==0){ //Contenido del Menú principal        

      //Dibujar fondo en la posición de la opción seleccionada
      MEN_DibRect(g,12, nivY+MENop*MENscrPxlOp, DEVscrPxlW-12, nivY+(MENop+1)*MENscrPxlOp, true, 0xFFA6BDD3, 0xFF738AAB);
      MEN_DibRect(g,12, nivY+MENop*MENscrPxlOp, 12+MENscrPxlOp, nivY+(MENop+1)*MENscrPxlOp, false, 0xFF738AAB, 0);

      //Dibujar opciones del menú
      g.setColor(0xFFFFFF);
      g.setFont(fSbt);
      //#if CFGtamanyoGfx == 2
//#       int nY=nivY+3;
      //#else
      int nY=nivY+2;
      //#endif
      for (int i = 0; i < 6; i++) {
        g.setColor(i==MENop?0x224466:0xFFFFFF);
      
        //#if CFGtamanyoGfx == 2
            //#if CFGfnt==0
//#             g.setClip(GAMscrPxlLE+44, nY, DEVscrPxlW-24,MENscrPxlOp);
//#             g.drawString(GAMstr[i], GAMscrPxlLE+44, nY, Graphics.TOP | Graphics.LEFT);
            //#else 
//#             TXT_DibLinea(g,GAMstr[i], GAMscrPxlLE+44, nY, -1);
            //#endif
        //#else
            //#if CFGfnt==0
                g.setClip(DEVscrPxlW/2-30, nY, DEVscrPxlW-24,MENscrPxlOp);
                g.drawString(GAMstr[i], DEVscrPxlW/2-30, nY, Graphics.TOP | Graphics.LEFT);
            //#else 
//#             TXT_DibLinea(g,GAMstr[i], DEVscrPxlW/2-30, nY, -1);
            //#endif
        //#endif
        nY += MENscrPxlOp;
      }
      //Dibujar icono combi
      HUD_DibImg(g, 15, (MENscrPxlUP+4+MENscrPxlH1+MENop*MENscrPxlOp)+(MENscrPxlOp-NUMPIXELS_TILEY)/2, 71);
    }
    else
    {
      //Prepara formato instrucciones de submenú
      g.setColor(0x224466);
      g.setFont(fCnt);      
      //Contenido de los modos de tutorial y juego
      if (MENid == 1 || MENid== 2 || MENid==3 ){
        
        //Dibujar texto instrucciones  
        //#if CFGfnt==0 
        g.drawString(GAMstr[27], DEVscrPxlW / 2, nivY, Graphics.TOP | Graphics.HCENTER);
        g.drawString("del "+GAMstr[MENid-1], DEVscrPxlW / 2, nivY+MENscrPxlOp, Graphics.TOP | Graphics.HCENTER);
        //#else
//#         TXT_DibLinea(g,GAMstr[27], DEVscrPxlW / 2, nivY, 0);
//#         TXT_DibLinea(g,"del "+GAMstr[MENid-1], DEVscrPxlW / 2, nivY+MENscrPxlOp,0);
        //#endif

        //Dibujar rectangulo fondo en la pos de la subopción seleccionada
        offset = nivY+MENscrPxlOp*(MENsubop+3);
        //#if CFGtamanyoGfx == 2
//#         offset -=3;
        //#else
        offset -=2;
        //#endif
        MEN_DibRect(g,12,offset,DEVscrPxlW-12, offset+MENscrPxlOp, true, 0xFFA6BDD3, 0xFF738AAB);
        
        //Dibujar fase
        g.setColor(MENsubop==0?0x224466:0xFFFFFF);
        g.setFont(fSbt);
        offset=7; //idx inicio sobre GAMstr
        for(int i=0; i<MENid-1; offset += LVL.confFaseMod[i++]);
        //#if CFGfnt==0 
        g.drawString(GAMstr[MENval1 + offset], DEVscrPxlW / 2, nivY+MENscrPxlOp*3, Graphics.TOP | Graphics.HCENTER);        
        //#else
//#         TXT_DibLinea(g,GAMstr[MENval1 + offset], DEVscrPxlW / 2, nivY+MENscrPxlOp*3,0);
        //#endif
        
        //Dibujar nivel dentro de fase
        boolean babierto;
        if (babierto = (COMBIMidlet.M.MENnivOpen[MEN_ObtIdxLvl((byte)(MENid - 1), MENval1)]  >= MENval2)){ //Si nivel abierto
          g.setColor(MENsubop == 1 ? 0x224466 : 0xFFFFFF);
        }
        else{        //desactivar si nivel no accesible
          g.setColor(MENsubop==1?0xF738AAB:0x90B0D0);
          if(MENmsg != 25)
            MENmsg = 26;
        }
        //#if CFGfnt==0 
        g.drawString(GAMstr[32]+Integer.toString(MENval2+1), DEVscrPxlW / 2, nivY+MENscrPxlOp*4, Graphics.TOP | Graphics.HCENTER);        
        //#else
//#         TXT_DibLinea(g,GAMstr[32]+Integer.toString(MENval2+1), DEVscrPxlW / 2, nivY+MENscrPxlOp*4,0);
        //#endif
        
        //Dibujar flechas
        MEN_DibFlechas(g, MENval1, (byte)(LVL.confFaseMod[MENid-1]-1), 16, nivY+MENscrPxlOp*3+2);
        MEN_DibFlechas(g, MENval2, (byte)(LVL.confFaseNivMod[MENid-1][MENval1]-1), 16, nivY+MENscrPxlOp*4+2);
        //Dibujar Record
        if(babierto){
          if(MENid==3) //Dibujar Records en fases/Niveles abiertos
            HUD_DibTim(g,(short)(nivY+MENscrPxlOp*5), MENval1, MENval2); //.drawString(MENid==1?GAMstr[32]:GAMstr[33],
          else if (MENid==2)
            HUD_DibPun(g,(short)(nivY+MENscrPxlOp*5),(byte)1);
        }

      }
      else if (MENid == 4){ //Opciones
        //#if CFGfnt==0 
        g.drawString(GAMstr[28], DEVscrPxlW / 2, nivY+MENscrPxlOp, Graphics.TOP | Graphics.HCENTER);
        //#else
//#         TXT_DibLinea(g, GAMstr[28], DEVscrPxlW / 2, nivY+MENscrPxlOp, 0);
        //#endif
        
        //Dibujar rectangulo fondo en la pos de la subopción seleccionada
        offset = nivY+MENscrPxlOp*(MENsubop+3);
        //#if CFGtamanyoGfx == 2
//#         offset -=3;
        //#else
        offset -=2;
        //#endif                
        MEN_DibRect(g,12,offset,DEVscrPxlW-12, offset+MENscrPxlOp, true, 0xFFA6BDD3, 0xFF738AAB);
        
        //Dibujar opciones
        //#if CFGfnt==0 
        g.setFont(fSbt);
        g.setColor(MENsubop==0?0x224466:0xFFFFFF);
        g.drawString(GAMstr[COMBIMidlet.M.MENbMus?33:34], DEVscrPxlW / 2, nivY+MENscrPxlOp*3, Graphics.TOP | Graphics.HCENTER);
        g.setColor(MENsubop==1?0x224466:0xFFFFFF);
        g.drawString(GAMstr[COMBIMidlet.M.MENbSnd?35:36], DEVscrPxlW / 2, nivY+MENscrPxlOp*4, Graphics.TOP | Graphics.HCENTER);
        //#else
//#         TXT_DibLinea(g,GAMstr[COMBIMidlet.M.MENbMus?33:34], DEVscrPxlW / 2, nivY+MENscrPxlOp*3,0);
//#         TXT_DibLinea(g,GAMstr[COMBIMidlet.M.MENbSnd?35:36], DEVscrPxlW / 2, nivY+MENscrPxlOp*4,0);        
        //#endif
      }
      else if (MENid == 5) { //Créditos
        //#if CFGfnt==0 
        g.drawString(GAMstr[49], DEVscrPxlW / 2, nivY+MENscrPxlOp, Graphics.TOP | Graphics.HCENTER);
        g.drawString(GAMstr[50], DEVscrPxlW / 2, nivY+MENscrPxlOp*3, Graphics.TOP | Graphics.HCENTER); 
        
        g.setColor(0xFFFFFF);
        g.drawString("Roberto Peña", DEVscrPxlW / 2, nivY+MENscrPxlOp*2, Graphics.TOP | Graphics.HCENTER);
        g.drawString("R. Peña y Lourdes Martín", DEVscrPxlW / 2, nivY+MENscrPxlOp*4, Graphics.TOP | Graphics.HCENTER);
        //#else
//#         TXT_DibLinea(g,GAMstr[49], DEVscrPxlW / 2, nivY+MENscrPxlOp,0);
//#         TXT_DibLinea(g,GAMstr[50], DEVscrPxlW / 2, nivY+MENscrPxlOp*3,0);
//#         TXT_DibLinea(g,"Roberto Peña", DEVscrPxlW / 2, nivY+MENscrPxlOp*2,0);
//#         TXT_DibLinea(g,"R. Peña y Lourdes Martín", DEVscrPxlW / 2, nivY+MENscrPxlOp*4,0);
        //#endif
//        g.drawString("Total Memory: "+Long.toString(Runtime.getRuntime().totalMemory()), DEVscrPxlW / 2, 96, Graphics.TOP | Graphics.HCENTER);
//        g.drawString("Free Memory: "+Long.toString(Runtime.getRuntime().freeMemory()), DEVscrPxlW / 2, 114, Graphics.TOP | Graphics.HCENTER);
//        g.drawString("Sound Count TONE:" +Sound.getConcurrentSoundCount(Sound.FORMAT_TONE), DEVscrPxlW / 2, 132, Graphics.TOP | Graphics.HCENTER);
//        g.drawString("Sound Count WAV:" +Sound.getConcurrentSoundCount(Sound.FORMAT_WAV), DEVscrPxlW / 2, 150, Graphics.TOP | Graphics.HCENTER);
//        g.drawString("MMAPI (JSR 135) vers:"+System.getProperty("microedition.media.version"), DEVscrPxlW / 2, 132, Graphics.TOP | Graphics.HCENTER);
//        g.drawString("Video capture:"+System.getProperty("supports.video.capture"),DEVscrPxlW / 2, 150, Graphics.TOP | Graphics.HCENTER);
        //Runtime.getRuntime().gc();
        System.gc();
        if(MENcheatSts >-1){
          //#if CFGfnt==0 
          g.setFont(fSbt);
          g.setColor(0xFFE0E0E0);          
          g.drawString(MENcheatPwd.substring(0, MENcheatSts + 1), DEVscrPxlW / 2, nivY+MENscrPxlOp*5+MENscrPxlH1/2, Graphics.TOP | Graphics.HCENTER);
          //#else
//#           TXT_DibLinea(g,MENcheatPwd.substring(0, MENcheatSts + 1), DEVscrPxlW / 2, nivY+MENscrPxlOp*5+MENscrPxlH1/2,0);
          //#endif
        }
      }
      else  //MENid == 6
        //#if CFGfnt==0   
        g.drawString(GAMstr[30], DEVscrPxlW / 2, nivY+MENscrPxlOp, Graphics.TOP | Graphics.HCENTER);
        //#else
//#         TXT_DibLinea(g,GAMstr[30], DEVscrPxlW / 2, nivY+MENscrPxlOp,0);
        //#endif
    }
}

private void MEN_DibFlechas(Graphics g, byte pval, byte plim, int px, int py){
    HUD_DibImg(g, px, py, pval>0?27:25);
    HUD_DibImg(g, DEVscrPxlW-px-6, py, pval<plim?28:26);
}

private void MEN_DibKeys(Graphics g, byte pid){
    int i;

    MEN_DibRect(g,0,DEVscrPxlH-MENscrPxlDO,DEVscrPxlW,DEVscrPxlH, false, 0xFF336699, 0);
    g.setFont(fCnt);
    g.setColor(0xFFFFFF);
    if((i=MENkeys[pid*2]) != -1){
        //#if CFGfnt==0 
        g.drawString(GAMstr[i],4,DEVscrPxlH-1, Graphics.BOTTOM | Graphics.LEFT);
       //#else        
//#         TXT_DibLinea(g,GAMstr[i],4,DEVscrPxlH-1-FNTH,-1); //KKtxt
      //#endif
    }
    if((i=MENkeys[pid*2+1]) != -1){
      //#if CFGfnt==0 
      g.drawString(GAMstr[i],DEVscrPxlW-4,DEVscrPxlH-1, Graphics.BOTTOM | Graphics.RIGHT);
      //#else      
//#       TXT_DibLinea(g,GAMstr[i],DEVscrPxlW-4,DEVscrPxlH-1-FNTH,1);  //KKtxt
      //#endif
    }

    g.setColor(0xE1BA6C);
    if(MENmsg != -1){
      //#if CFGfnt==0         
      g.drawString(GAMstr[MENmsg],DEVscrPxlW/2,DEVscrPxlH-1, Graphics.BOTTOM | Graphics.HCENTER);
      //#else      
//#       TXT_DibLinea(g,GAMstr[MENmsg],DEVscrPxlW/2,DEVscrPxlH-1-FNTH,0); //KKtxt
      //#endif      
    }
}


private void MEN_DibRect(Graphics g, int pix, int piy, int pfx, int pfy, boolean pbor, int pcol, int pcolbor){
    g.setClip(pix-1, piy-1, pfx-pix+2, pfy-piy+2); //KKTXT
    
    xPoints[0] = xPoints[3] = pix;
    xPoints[1] = xPoints[2] = pfx;
    yPoints[0] = yPoints[1] = piy;
    yPoints[2] = yPoints[3] = pfy;
    DirectUtils.getDirectGraphics(g).fillPolygon(xPoints,0,yPoints,0,4,pcol);
    /*Sin transparencias
    g.setColor(pcol);
    g.fillRect(pix, piy, pfx-pix, pfy-piy);
     */
    if(pbor){
      g.setColor(pcolbor);
      g.drawRect(pix, piy, pfx-pix, pfy-piy);
    }
}


private void GAM_Dibujar(Graphics g){
   g.setClip(0, 0, DEVscrPxlW, DEVscrPxlH);
   //System.out.println(" => GAM_Dibujar");
    g.setColor(LVL.colbase);
    g.fillRect(14*NUMPIXELS_TILEX,0,DEVscrPxlW-14*NUMPIXELS_TILEX,14*NUMPIXELS_TILEY);
   //System.out.println(" => GAM_BGR");
    BGR_Dibujar(g); //Vuelca BGR a graphics actual
   //System.out.println(" => GAM_FGR");
    FGR_Dibujar(g); //Pinta cursor y pieza asociada
   //System.out.println(" => GAM_HUD");
   if(GAMbHUDon){ //Dibujo marcadores y FX durante la partida
     FX_Dibujar(g);
     HUD_Dibujar(g);
     if(GAMbNivIntro)
        HUD_DibTut(g);
   }
   else{ //Dibujo marcadores y FX antes y después de partida
     HUD_DibInOut(g);
     FX_Dibujar(g);
   }

}



//************************************************************************* run
public void run()
{
/*
//TEMP: para calculo fps
      currTime = System.currentTimeMillis();
      if (currFrameNumber == 128){
          elapsedTime = currTime - frameTime;
          frameTime = currTime;
          currFrameNumber = 0;
          fps = (long) (128000/elapsedTime);
      }
      numFrames = currFrameNumber++;
//fin TEMP
*/

      switch(VGsts){
        case VGSTS_LOAD:
          LOA_Tick();
          break;

        case VGSTS_MENU:
          MEN_Tick();
          break;

        case VGSTS_GAME:
          VISbUpd = false;
  // System.out.println(" => PIE_Tick");
          PIE_Tick(); //Control del juego: FSM de Piezas, y Gestión Tiempo
  //System.out.println(" => GAM_Tick");
          GAM_Tick(); //Control de Acciones directas usuario (Teclado) y autónomas (Engine Combis) para Actualización de pantalla
   //System.out.println(" => CUR_Tick");
          CUR_Tick(); //Actualización cursor y pieza asociada
  // System.out.println(" => FX_Tick");
          FX_Tick();
  // System.out.println(" => VIS_Tick");
          VIS_Tick();
          break;
      }
      repaint();
      serviceRepaints();

      //establecer frame rate constante (Velocidad tick game-loop: de 44 a 63)
      do
        tickTime = System.currentTimeMillis();
      while(tickTime < lastTime + 58L);
      lastTime = tickTime;

       if(COMBIMidlet.M != null)
         Display.getDisplay(COMBIMidlet.M).callSerially(this);
}



 protected void paint( Graphics g ){
   VIS_paint(g);
 }

private synchronized void VIS_paint(Graphics g){
//    if(SCRdg == null) => no tira en 6630
      SCRdg = DirectUtils.getDirectGraphics(g); //Para que funcione en el 6630 debe actualizarse cada tick

    if(VGsts == VGSTS_GAME)
      GAM_Dibujar(g);
    else if (VGsts == VGSTS_MENU)
      MEN_Dibujar(g);
    else if (VGsts == VGSTS_LOAD)
      LOA_Dibujar(g);
}






//*****************************************************************************
//************************************************************** VISUALIZACION
//*****************************************************************************


// Desc: Añade definición de FX a lista de FXs
// Return: id del FX en la lista
private short FX_Add(short ptip, short pid, short pval){
   short i;

//Busca el siguiente FX vacío
  for(i=0; i<NUMFX; i++){
    if (ptip == 6 && FXdat[i][2] != -1 && FXdat[i][0]==6) //sólo se permite un FX de Mensaje a la vez
      return -1;
    if (FXdat[i][2] == -1)
      break;
  }

  //Genera datos del nuevo FX en función de su tipo
  FXdat[i][0] = ptip;
  for (int j=0; j < 4; j++)
    FXdat[i][j+1] = FXtip[(ptip-1)*5+j];
  FXdat[i][5] = pid;
  if(pid != -1){
    FXdat[i][6] = ptip!=8?COMdat[pid][3]:PIEtmp[2]; //x
    FXdat[i][7] = ptip!=8?COMdat[pid][4]:PIEtmp[3]; //y
    FXdat[i][8] = ptip!=8?(short)(COMdat[pid][3]+COMdat[pid][5]):PIEtmp[4]; //x+w
    FXdat[i][9] = ptip!=8?(short)(COMdat[pid][4]+COMdat[pid][6]):PIEtmp[5]; //y+h
    FXdat[i][10] = ptip!=8?COMdat[pid][8]:PIEtmp[6];
  }
  FXdat[i][11] = pval;
  //#if CFGtipoSnd == 2
  if(COMBIMidlet.M.MENbSnd && FXtip[(ptip-1)*5+4] != -1)
    MUS.TocaMusica(FXtip[(ptip-1)*5+4]);
  //#endif
  return i;
}

//--------------------------------------------------------------------- FX_Tick
private void FX_Tick(){
   for (short i = 0; i < NUMFX; i++) {
     if (FXdat[i][2] == 0) { //Si se cumple timing de ejecución del FX
       if(FXdat[i][3] == 0){ //Si ha llegado al final de su duración
         if(FXdat[i][1] == 0) //Si es de tipo Asíncrono
           FXdat[i][2] = -1;
         else
           FXdat[i][2] = -2; //Desactiva FX (SOLO SI SINCRONO, SINO -1)
       }
       else{
         //Ejecución de la acción del FX en el tick actual (ya que FXdat[i][7] ha sido 0)
         FX_ExecIn(i);
         FXdat[i][3]--; //Decrementa duración de ejecución del FX
       }
     }
     else if (FXdat[i][2] >0)
       FXdat[i][2]--;
   }
}


//Ejecución (o preparación de dibujado) del FX en función del tick de
// duración actual.
private void FX_ExecIn(short pIdx){
switch (FXdat[pIdx][0]) {
  case 1: //COMBI Crea
    FXdat[pIdx][4] = (short) ( (FXdat[pIdx][3] / 2) * NUMPIXELS_TILEX); //PTE
    break;
  case 2: //COMBI Elim
  case 8: //PIE Elim
    FXdat[pIdx][4] = (short) (FXdat[pIdx][4] - 2);
    break;
  case 3: //Puntuación (incrementar posY para que bonus se eleve)
  case 4: //Cur Bonus  (incrementar posY para que puntuación se eleve)
  case 9: //Apertura ventana (incrementa proporción ancho ventana para simular apertura)
    FXdat[pIdx][4] = (short) (FXdat[pIdx][4] + 1);
    break;
  case 6: //Mensaje
    short inc = (FXdat[pIdx][10] > FXdat[pIdx][3])?(short)(FXdat[pIdx][10]/FXdat[pIdx][3]):1;

    if(FXdat[pIdx][4]< FXdat[pIdx][10])
      FXdat[pIdx][4]+=inc; //incremento % progreso
    if(FXdat[pIdx][4] > FXdat[pIdx][10])
      FXdat[pIdx][4] = FXdat[pIdx][10];
    break;
  case 7: //Mensaje síncrono
    break;
  case 10: //Transición pantallas(incrementa proporción ancho ventana para fade-out)
    FXdat[pIdx][4] = (short) (FXdat[pIdx][4] + 25);
    break;
}

}

// ---------------------------------------------------------------------- FX_In
// Desc: Retorna el estado de un FX síncrono del tipo pasado por parámetro
// ----------------------------------------------------------------------------
private short FX_In(int pTip){
 for (int i = 0; i < NUMFX; i++)
   if(FXdat[i][0] == pTip && FXdat[i][2] != -1){
     if (FXdat[i][2] == -2) { //si acaba de desactivarse
       FXdat[i][2] = -1;
       return -1; //FX de tipo pTip acaba de finalizar
     }
     return (short)Math.max(1,FXdat[i][3]); //FXdat[i][3]; //Hay FX in progress de tipo pTip
   }
 return 0; //No hay FX activo de tipo pTip
}

// ----------------------------------------------------------------- FX_Dibujar
private void FX_Dibujar(Graphics g){
 //Dibuja los FX activos
 for (byte i = 0; i < NUMFX; i++)
   if (FXdat[i][2] == 0) { //SI FX en estado activo y el Dibujado de FX está activado.

     switch(FXdat[i][0]){
       case 1:
         HUDbIni=true;
         FX_DibComGen(g,i);
         break;
       case 2:
         FX_DibComEli(g,i);
         break;
       case 3:
         FX_DibPun(g,i);
         break;
       case 4:
         FX_DibBonus(g,i);
         break;
       case 5:
         FX_DibContCur(g,i);
         break;
       case 6:
       case 7:
         FX_DibMsg(g,i);
         break;
       case 8:
         FX_DibPieEli(i);
         break;
       case 9:
         FX_DibDialog(g,i);
         break;
       case 10:
         FX_DibTrans(g,i);
         break;
     }
 }

}


private void FX_DibComGen(Graphics g, short pidx){
 short inc = FXdat[pidx][4]; //Obtener ajuste para calcular ancho/alto del rectángulo
// g.setClip(xPoints[0],yPoints[0], xPoints[1]-xPoints[0]+2, yPoints[2]-yPoints[0]+2);
 g.setClip(0,0,DEVscrPxlW,DEVscrPxlH);

 xPoints[0] = xPoints[3] = VIS_ConvNivX(FXdat[pidx][6]) * NUMPIXELS_TILEX - inc; //iniX
 yPoints[0] = yPoints[1] = VIS_ConvNivY(FXdat[pidx][7]) * NUMPIXELS_TILEY - inc; //iniY
 xPoints[1] = xPoints[2] = VIS_ConvNivX(FXdat[pidx][8]) * NUMPIXELS_TILEX + NUMPIXELS_TILEX +inc; //finX
 yPoints[2] = yPoints[3] = VIS_ConvNivY(FXdat[pidx][9]) * NUMPIXELS_TILEY + NUMPIXELS_TILEY +inc; //finY
 g.setColor(0xFFFFFFFF);
 g.drawRect(xPoints[0], yPoints[0], xPoints[1]-xPoints[0], yPoints[2]-yPoints[0]);
 g.drawRect(xPoints[0]+1, yPoints[0]+1, xPoints[1]-xPoints[0]-2, yPoints[2]-yPoints[0]-2);

 xPoints[0] = xPoints[3] +=2;
 xPoints[1] = xPoints[2] -=2;
 yPoints[0] = yPoints[1] +=2;
 yPoints[2] = yPoints[3] -=2;
 SCRdg.fillPolygon(xPoints,0,yPoints,0,4,(COLval[FXdat[pidx][10]] & 0x60FFFFFF));
}


private void FX_DibComEli(Graphics g, short pidx){
  short inc = FXdat[pidx][4]; //Obtener ajuste para calcular ancho/alto del rectángulo
  if(inc == 14){
    xPoints[0] = xPoints[3] = VIS_ConvNivX(FXdat[pidx][6]) * NUMPIXELS_TILEX;
    xPoints[1] = xPoints[2] = VIS_ConvNivX(FXdat[pidx][8]) * NUMPIXELS_TILEX;
    yPoints[0] = yPoints[1] = VIS_ConvNivY(FXdat[pidx][7]) * NUMPIXELS_TILEY;
    yPoints[2] = yPoints[3] = VIS_ConvNivY(FXdat[pidx][9]) * NUMPIXELS_TILEY;
    g.setClip(xPoints[0],yPoints[0], xPoints[1]-xPoints[0], yPoints[2]-yPoints[0]);
    SCRdg.fillPolygon(xPoints, 0, yPoints, 0, 4, 0xFFFFFFFF);
  }
  else{//dibujar rectángulo por cada una de las partes de las piezas
    short inc2 = (short)((NUMPIXELS_TILEX - inc) / 2);
    for (short i = FXdat[pidx][6]; i < FXdat[pidx][8]; i++)
      for (short j = FXdat[pidx][7]; j < FXdat[pidx][9]; j++) {
        xPoints[0] = xPoints[3] = VIS_ConvNivX(i) * NUMPIXELS_TILEX + inc2;
        xPoints[1] = xPoints[2] = xPoints[0]  + inc;
        yPoints[0] = yPoints[1] = VIS_ConvNivY(j) * NUMPIXELS_TILEY + inc2;
        yPoints[2] = yPoints[3] = yPoints[0] + inc;

        g.setClip(xPoints[0],yPoints[0], xPoints[1]-xPoints[0], yPoints[2]-yPoints[0]);
        SCRdg.fillPolygon(xPoints, 0, yPoints, 0, 4, 0x80FFFFFF);
      }
  }
}


private void FX_DibPieEli(short pidx){
  byte idNumPart;
  byte pTip = (byte)FXdat[pidx][8];
  byte pOri = (byte)FXdat[pidx][9];
  short x,y;

  TIPPIE_ObtenerVal(pTip, pOri);
  short pieW = TIPPIEbb[pTip - 1][pOri * 2];
  short pieH = TIPPIEbb[pTip - 1][pOri * 2 + 1];
  short inc2 = (short)((NUMPIXELS_TILEX - FXdat[pidx][4]) / 2);
  for (short j = 0; j < pieH; j++)
    for (short i = 0; i < pieW; i++) {
      idNumPart = TIPPIErefval[j * 3 + i];
      if (idNumPart > 0){
        xPoints[0] = xPoints[3] = VIS_ConvNivX((short)(FXdat[pidx][6]+i)) * NUMPIXELS_TILEX + inc2;
        xPoints[1] = xPoints[2] = xPoints[0] + FXdat[pidx][4];
        yPoints[0] = yPoints[1] = VIS_ConvNivY((short)(FXdat[pidx][7]+j)) * NUMPIXELS_TILEY + inc2;
        yPoints[2] = yPoints[3] = yPoints[0] + FXdat[pidx][4];
        SCRdg.fillPolygon(xPoints, 0, yPoints, 0, 4, 0x80FFFFFF);

      }
    }


}

private void FX_DibPun(Graphics g, short pidx){
  int iniY = VIS_ConvNivY((short)((FXdat[pidx][9]-FXdat[pidx][7])/2+FXdat[pidx][7])) * NUMPIXELS_TILEY - FXdat[pidx][4];
  sTime = Long.toString(FXdat[pidx][11]);
  int i = FXdat[pidx][8]-FXdat[pidx][6] >= (sTime.length()-1) ? (FXdat[pidx][8]-FXdat[pidx][6]-(sTime.length()-1))/2 : (FXdat[pidx][8]-FXdat[pidx][6])/2;
  int iniX = VIS_ConvNivX((short)(i+FXdat[pidx][6])) * NUMPIXELS_TILEX;

  for(i=0; i<sTime.length(); i++)
    HUD_DibImg(g, i*9+iniX, iniY, (sTime.charAt(i) - '0')+51);
}


private void FX_DibBonus(Graphics g, short pidx){
    int iniY = VIS_ConvNivY((short)((FXdat[pidx][9]-FXdat[pidx][7])/2+FXdat[pidx][7]+1)) * NUMPIXELS_TILEY - FXdat[pidx][4]+4;
    int i = FXdat[pidx][8]-FXdat[pidx][6] >= 3 ? (FXdat[pidx][8]-FXdat[pidx][6]-3)/2 : (FXdat[pidx][8]-FXdat[pidx][6])/2;
    int iniX = VIS_ConvNivX((short)(i+FXdat[pidx][6])) * NUMPIXELS_TILEX + 4;

    HUD_DibImg(g, iniX, iniY, 61); //signo +
    iniX +=9;
    HUD_DibImg(g, iniX, iniY, FXdat[pidx][11]+67); //incremento uso cursor
    int idx=0;
    for(i=1; i<=16; i*=2, idx++)
      if((CURcontBon & i)!= 0){  //AND lógico para detectar si el modo cursor (4-idx) va a ser incrementado
        iniX +=9;
        HUD_DibImg(g, iniX, iniY, idx!=0?(4-idx)+12:19); //13+FXdat[pidx][12]
      }
}

private void FX_DibMsg(Graphics g, short pidx){
    FX_DibRect(g,0,DEVscrPxlW,32,52);
    //Texto
    g.setFont(fSbt);
    if(FXdat[pidx][11] == 40){
      g.drawString(GAMstr[FXdat[pidx][11]]+(FXdat[pidx][8]>1?"  (x"+FXdat[pidx][8]+")":""), DEVscrPxlW / 2 - 20, 36, Graphics.TOP | Graphics.HCENTER);
      g.drawString(Integer.toString(FXdat[pidx][9] + FXdat[pidx][4]) + "%", 140, 36, Graphics.TOP | Graphics.LEFT);
    }else
      g.drawString(GAMstr[FXdat[pidx][11]], DEVscrPxlW / 2, 36, Graphics.TOP | Graphics.HCENTER);

}

private void FX_DibDialog(Graphics g, short pidx){
    FX_DibRect(g,
               DEVscrPxlW/2 - FXdat[pidx][4]*NUMPIXELS_TILEX,
               DEVscrPxlW/2 + FXdat[pidx][4]*NUMPIXELS_TILEX,
               32,
               32+FXdat[pidx][11]);
}

private void FX_DibRect(Graphics g, int xi, int xf, int yi, int yf){
    g.setClip(xi-2,yi-2,(xf-xi)+6,(yf-yi)+6);

    xPoints[0] = xPoints[3] = xi;
    xPoints[1] = xPoints[2] = xf;
    yPoints[0] = yPoints[1] = yi;
    yPoints[2] = yPoints[3] = yf;
    SCRdg.fillPolygon(xPoints, 0, yPoints, 0, 4, 0xC0fceecd);
    //Marco sup/inf
    g.setColor(0xE5A332);
    for (int i = 1; i <= 2; i++)
      g.drawRect( xi-i, yi-i, (xf-xi)+2*i, (yf-yi)+2*i);
    //Marco interno
    g.setColor(0x224466);
    g.drawRect(xi-1, yi, (xf-xi)+2, yf-yi);
}

private void FX_DibTrans(Graphics g, short pidx){
    g.setClip(0, 0, DEVscrPxlW, DEVscrPxlH);
    xPoints[0] = xPoints[3] = 0;
    xPoints[1] = xPoints[2] = DEVscrPxlW;
    yPoints[0] = yPoints[1] = 0;
    yPoints[2] = yPoints[3] = DEVscrPxlH;
    int mask = 0x00FFFFFF;
    if(FXdat[pidx][11]==0)
      mask = mask | (FXdat[pidx][4] << 24);
    else
      mask = mask | ((250-FXdat[pidx][4])<<24);
    SCRdg.fillPolygon(xPoints, 0, yPoints, 0, 4, 0xFF000000 & mask);
}

private void FX_DibContCur(Graphics g, short pidx){
    xPoints[0] = xPoints[3] = DEVscrPxlW-36;
    xPoints[1] = xPoints[2] = DEVscrPxlW;
    yPoints[0] = yPoints[1] = 0;
    yPoints[2] = yPoints[3] = yPoints[0] + 20;
    g.setClip(xPoints[0],yPoints[0], xPoints[1]-xPoints[0], yPoints[2]-yPoints[0]+2);
    SCRdg.fillPolygon(xPoints, 0, yPoints, 0, 4, 0xC0fceecd);
    //Marco sup/inf
    g.setColor(0xE5A332);
    g.drawLine(DEVscrPxlW-36,0,DEVscrPxlW,0);
    g.drawLine(DEVscrPxlW-36,20,DEVscrPxlW,20);
    //Decremento contador e icono modo cursor
    HUD_DibImg(g, DEVscrPxlW-34, 4, 11); //signo -
    HUD_DibImg(g, DEVscrPxlW-22, 4, 68); //1
    HUD_DibImg(g, DEVscrPxlW-14, 4, FXdat[pidx][11]!=4?FXdat[pidx][11]+12:19);
}


//*************************************************************************

//-------------------------------------------------------------------- VIS_Tick
//Redibujar el escenario en BGR sólo si se ha producido modificación. Debido a:
//1=> Movimiento del propulsor (en zona de scroll)
//2=> Colocación de pieza (dentro de scr visible)
//3=> Desaparición de pieza
//4=> Creación de combi
//5=> Eliminación de combi.
// En caso contrario y si ha habido scroll generar BGR
//-----------------------------------------------------------------------------
private void VIS_Tick(){
  BGRscroll=0;
  if(VISbUpd){  //si hay que actualizar BGR por motivos no debidos a scroll
    BGR_CalcBGRabs(ACTposX, ACTposY);
    BGR_DibFijoTodo(BGRg1, BGRdg1);
    BGRprin=true;
  }
  else if(ACTacuIncX != 0 || ACTacuIncY !=0){ //Técnica de doble buffering para scroll (solo si inc de tile en tile)
    short BGRantX = BGRnivTilX;
    short BGRantY = BGRnivTilY;
    BGR_CalcBGRabs(ACTposX, ACTposY);

    if(BGRantX != BGRnivTilX){ //Si scroll horizontal
      if(Math.abs(BGRantX - BGRnivTilX) > 1){
        BGR_DibFijoTodo(BGRg1, BGRdg1);
        BGRprin=true;
      }
      else{
        BGRscroll = (byte) (BGRantX < BGRnivTilX ? 1 : -1);
        if (BGRprin) {
          BGR_DibScrollCol(BGRg2, BGRdg2);
          BGRg2.drawImage(BGRimg1, BGRscroll==-1?NUMPIXELS_TILEX:-NUMPIXELS_TILEX, 0, Graphics.TOP | Graphics.LEFT);    
          //BGR_DibScrollRestoCols(BGRg2, BGRimg1);
        }
        else {
          BGR_DibScrollCol(BGRg1, BGRdg1);
          //BGR_DibScrollRestoCols(BGRg1, BGRimg2);
          BGRg1.drawImage(BGRimg2, BGRscroll==-1?NUMPIXELS_TILEX:-NUMPIXELS_TILEX, 0, Graphics.TOP | Graphics.LEFT);
        }
        BGRprin = !BGRprin; //Indica qué buffer ha quedado como activo (el 1 si true, o el 2 si false)
      }
    }

    else if (BGRantY != BGRnivTilY){ //Si scroll vertical
      if(Math.abs(BGRantY - BGRnivTilY) > 1){
        BGR_DibFijoTodo(BGRg1, BGRdg1);
        BGRprin=true;
      }
      else{
        BGRscroll = (byte) (BGRantY < BGRnivTilY ? 2 : -2);
        if (BGRprin) { //si se está utilizando el buffer 1 ahora debe utilizarse el 2
          BGR_DibScrollFil(BGRg2, BGRdg2);
          //BGR_DibScrollRestoFilas(BGRg2, BGRimg1);
          BGRg2.drawImage(BGRimg1, 0, BGRscroll==-2?NUMPIXELS_TILEY:-NUMPIXELS_TILEY, Graphics.TOP | Graphics.LEFT);
        }
        else {
          BGR_DibScrollFil(BGRg1, BGRdg1);
          //BGR_DibScrollRestoFilas(BGRg1, BGRimg2);
          BGRg1.drawImage(BGRimg2, 0, BGRscroll==-2?NUMPIXELS_TILEY:-NUMPIXELS_TILEY, Graphics.TOP | Graphics.LEFT);
        }
        BGRprin = !BGRprin; //Indica qué buffer ha quedado como activo (el 1 si true, o el 2 si false)
      }
    }

    else
      BGRscroll = 0;
  }
}


//	-------------------------------------------------------------------- VIS_Init
//	 Desc: Prepara variables de visualización dependientes del Nivel de juego.
//	       Inicializa valores invariables durante el transcurso del Nivel
//	       actual de juego.
//	-----------------------------------------------------------------------------

private void VIS_Init() {
     VISAreaScrollLimXizq = (short) ( (NUMTILES_SCRX - 2) / 2); //= (14-2)/2=6 pos
     VISAreaScrollLimXder = (short) (NIVnumTilW -((NUMTILES_SCRX - 2) / 2 + 2)); //28-(6+2)=20
     VISAreaScrollLimYarr = (short) ( (NUMTILES_SCRY - 2) / 2); //6
     VISAreaScrollLimYaba = (short) (NIVnumTilH -((NUMTILES_SCRY - 2) / 2 + 2)); //28-(6+2)=20
     BGRnivTilXmax = (short) (NIVnumTilW - NUMTILES_SCRX);
     BGRnivTilYmax = (short) (NIVnumTilH - NUMTILES_SCRY);
     //Prepara el fondo de visualización del nivel
     BGRprin = true;
     BGR_CalcBGRabs(ACTposX, ACTposY);
     BGR_DibFijoTodo(BGRg1, BGRdg1); //se asume que BGRprin = true
 }


 private short VIS_InAreaScrollX(short posX) {
     if(posX > VISAreaScrollLimXizq)
       if(posX < VISAreaScrollLimXder)
          return 0;
       else
          return 1;
     else
       return -1;
 }


   private short VIS_InAreaScrollY(short posY) {
     if (posY > VISAreaScrollLimYarr)
       if (posY < VISAreaScrollLimYaba)
         return 0;
       else
         return 1;
     else
       return -1;
   }

//---------------------------------------------------------------- VIS_ConvNivX
// Desc: Convierte posición global <ptilX> a posición local dentro de View
   private short VIS_ConvNivX(short ptilX){
     switch (VIS_InAreaScrollX(ACTposX)) {
          case 0:
            return (short) (ptilX - BGRnivTilX);
          case -1:
            return ptilX;
          case 1:
            return (short) (ptilX - VISAreaScrollLimXder +  VISAreaScrollLimXizq);
     }
     return -1;
   }


 //	------------------------------------------------------ VIS_ConvNivY
   private short VIS_ConvNivY(short ptilY){
     switch (VIS_InAreaScrollY(ACTposY)) {
          case 0:
            return (short) (ptilY - BGRnivTilY);
          case -1:
            return ptilY;
          case 1:
            return (short) (ptilY - VISAreaScrollLimYaba +  VISAreaScrollLimYarr);
     }
     return -1;
   }


// Desc: Comprueba si parte o la totalidad de la superficie pasada por
//       parámetro es visible en pantalla (FGR).
private boolean VIS_SupEnFgr(short pIniX, short pIniY, int pFinX, int pFinY){
     //Chequeo coordenada X
     if (pIniX < BGRnivTilX && pFinX < BGRnivTilX)
         return false;
     else if (pIniX > (BGRnivTilX + NUMTILES_SCRX-1))
         return false;
     //Chequeo coordenada Y
     if (pIniY < BGRnivTilY && pFinY < BGRnivTilY)
         return false;
     else if (pIniY > (BGRnivTilY + NUMTILES_SCRY-1))
         return false;
     return true;
}


//	-------------------------------------------------------------- BGR_CalcBGRabs
//	 Desc: Calcula coordenadas tiles x,y de inicio del BGR a partir de las
//	       posiciones (en tiles) posX, nivY dentro del Nivel de juego
//	 Upd:
//	 - BGRnivTilX, BGRnivTilY
//	-----------------------------------------------------------------------------
   private void BGR_CalcBGRabs(short posX, short posY) {
       if(posX < VISAreaScrollLimXizq)
         BGRnivTilX = 0;
       else if (posX >= VISAreaScrollLimXder)
         BGRnivTilX = BGRnivTilXmax;
       else
         BGRnivTilX = (short) (posX - VISAreaScrollLimXizq);

       if(posY < VISAreaScrollLimYarr)
         BGRnivTilY = 0;
       else if (posY >= VISAreaScrollLimYaba)
         BGRnivTilY = BGRnivTilYmax;
       else
         BGRnivTilY = (short) (posY - VISAreaScrollLimYarr);
  }

//	************************************************************ LOGICA DE DIBUJO

private void BGR_Dibujar(Graphics g){
    //Imprimir el BGR desde una imagen u otra en función del buffer activo    
    g.drawImage(BGRprin?BGRimg1:BGRimg2, 0, 0, Graphics.TOP | Graphics.LEFT);
}

//	---------------------------------------------------------- BGR_DibScrollCol
// Desc: Dibujar columna (ultima si DER o primera si IZQ) en BGRprin
private void BGR_DibScrollCol(Graphics BGRg,  DirectGraphics BGRdg) {
    short x, y , xtil,idx, idPie, idPix, idCom;
    int initOffset;

    //Calcular columna a dibujar
    if(BGRscroll== -1)
      xtil = 0;
    else{
//[S40]      xtil = (short)(NUMTILES_SCRX-1);
//[S60]
        if(GAMscrPatchLE==1){        
              if(BGRnivTilX == BGRnivTilXmax){
                BGRg.setColor(LVL.colbase);
                BGRg.fillRect(NUMTILES_SCRX*NUMPIXELS_TILEX, 0, NUMPIXELS_TILEX, NUMTILES_SCRY*NUMPIXELS_TILEY);
                return;
              }else
                xtil = (short) (NUMTILES_SCRX);
//end[S60]              
        }else
            //[S40]      
            xtil = (short)(NUMTILES_SCRX-1);

    }
    initOffset = BGRnivTilX + xtil + BGRnivTilY * NIVnumTilW;
    x= (short)(xtil*NUMPIXELS_TILEX);
    y = 0;

    //Dibujar columna
    for (int j = 0; j < NUMTILES_SCRY; j++) {
      idx = (short) (initOffset + j * NIVnumTilW);
      BGR_DibujarScr(BGRg, BGRdg, idx, x, y, xtil, j);
      /*
      idPie = NIVidPieza[idx];
      if(idPie > -1){ //Si hay pieza
        if(PIEdat[idPie][4] == -1  || (PIEdat[idPie][4] !=-1 && COMdat[PIEdat[idPie][4]][0] == IN_FX)) //y no hay combi  => o hay combi y éste está en estaFX
            BGRdg.drawPixels(GAMpiePix, false,
                     (PIEdat[idPie][3] - 1)* 14 * NUMPIXELS_TILE_XY + (NIVnumPart[idx]-1) * NUMPIXELS_TILE_XY,
                     NUMPIXELS_TILEX,
                     x, y,
                     NUMPIXELS_TILEX, NUMPIXELS_TILEY,
                     0, DEVntvFmt);
        else{
          idCom = COM_ObtenerComRaiz(PIEdat[idPie][4]);
          BGRg.setColor(COLval[COMdat[idCom][8]]);
          BGRg.fillRect(x, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
          if((idPix = COM_ObtenerIdPix(idCom, BGRnivTilX+xtil, BGRnivTilY+j)) != -1)
            BGRdg.drawImage(GAMspr[idPix],x,y,Graphics.TOP | Graphics.LEFT,GAMmanip);
        }
      }
      else //dibujar borde o fondo
          if(LVL.NIVmap[idx] == LVL.idBorPixlim){ //[PTE]: NIVidBorPixlim
            BGRg.setColor(LVL.colbase);
            BGRg.fillRect(x, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
          }
          else
              BGRdg.drawPixels(LVL.pix, false, LVL.NIVmap[idx] * NUMPIXELS_TILE_XY,
                           NUMPIXELS_TILEX, x, y, NUMPIXELS_TILEX,
                           NUMPIXELS_TILEY, 0, DEVntvFmt);
       */
      y += NUMPIXELS_TILEY; //[Alto Tile]
    }
}



//	---------------------------------------------------------- BGR_DibScrollFil
private void BGR_DibScrollFil(Graphics BGRg,  DirectGraphics BGRdg) {
    short i, x, y , ytil, idx, idPix, idPie, idCom;
    int initOffset;

    //Calcular fila a dibujar (1ra o ultima)
    if(BGRscroll== -2)
      ytil = 0;
    else
      ytil = (short)(NUMTILES_SCRY-1);
    initOffset = BGRnivTilX + (BGRnivTilY+ytil) * NIVnumTilW;
    y = (short)(ytil*NUMPIXELS_TILEY);
    x = 0;

//[S40]
short limX = NUMTILES_SCRX;
if(GAMscrPatchLE==1){      
//[S60]: Ajuste para relleno 8-pixels extra de pantalla para los q no hay mapa
    limX++; //short limX = NUMTILES_SCRX + 1;
    if (BGRnivTilX == BGRnivTilXmax) {
      limX--;
      BGRg.setColor(LVL.colbase);
      BGRg.fillRect(NUMTILES_SCRX * NUMPIXELS_TILEX, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
      //Prueba: Y
    }
//Fin[S60]
}


    //Dibujar fila
    for (i = 0; i < limX; i++) {
      idx = (short) (initOffset + i);
      BGR_DibujarScr(BGRg, BGRdg, idx, x, y, i, ytil);
      
      /*
      idPie = NIVidPieza[idx];
      if(idPie > -1){ //Si hay pieza
        if(PIEdat[idPie][4] == -1  || (PIEdat[idPie][4] !=-1 && COMdat[PIEdat[idPie][4]][0] == IN_FX)) //y no hay combi  => o hay combi y éste está en estado FX
            BGRdg.drawPixels(GAMpiePix, false,
                     (PIEdat[idPie][3] - 1)* 14 * NUMPIXELS_TILE_XY + (NIVnumPart[idx]-1) * NUMPIXELS_TILE_XY,
                     NUMPIXELS_TILEX,
                     x, y,
                     NUMPIXELS_TILEX, NUMPIXELS_TILEY,
                     0, DEVntvFmt);
        else{
          idCom = COM_ObtenerComRaiz(PIEdat[idPie][4]);
          BGRg.setColor(COLval[COMdat[idCom][8]]);
          BGRg.fillRect(x, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
          if((idPix = COM_ObtenerIdPix(idCom, BGRnivTilX+i, BGRnivTilY+ytil)) != -1)
            BGRdg.drawImage(GAMspr[idPix],x,y,Graphics.TOP | Graphics.LEFT,GAMmanip);
        }
      }
      else //dibujar borde o fondo
          if(LVL.NIVmap[idx] == LVL.idBorPixlim){
            BGRg.setColor(LVL.colbase);
            BGRg.fillRect(x, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
          }
          else
              BGRdg.drawPixels(LVL.pix, false, LVL.NIVmap[idx] * NUMPIXELS_TILE_XY,
                           NUMPIXELS_TILEX, x, y, NUMPIXELS_TILEX,
                           NUMPIXELS_TILEY, 0, DEVntvFmt);
       */
      x += NUMPIXELS_TILEX; //[Alto Tile]
    }

}


//	----------------------------------------------------------- BGR_DibFijoTodo
//	 Desc: Dibuja fondo del nivel GAMcurNiv empezando en NIVidPieza[BGRnumTilX, BGRnumTilY]
//	-----------------------------------------------------------------------------

 private void BGR_DibFijoTodo(Graphics BGRg, DirectGraphics BGRdg) {
     short x,y, idPie, idx, idPix,limX, idCom;
     int initOffset = BGRnivTilX + BGRnivTilY * NIVnumTilW;

     //[S40] limX = NUMTILES_SCRX;
    limX = NUMTILES_SCRX;
    if(GAMscrPatchLE==1){      
    //[S60]: Ajuste para relleno 8-pixels extra de pantalla para los q no hay mapa
         limX++; //limX = NUMTILES_SCRX+1;
         if (BGRnivTilX == BGRnivTilXmax){
           limX--;
           BGRg.setColor(LVL.colbase);
           BGRg.fillRect(NUMTILES_SCRX * NUMPIXELS_TILEX, 0, NUMPIXELS_TILEX,
                         NUMTILES_SCRY * NUMPIXELS_TILEY);
         }
         //Fin[S60]
    }

     x = 0;
     for (int i = 0; i < limX; i++) {
       y = 0;
       for (int j = 0; j < NUMTILES_SCRY; j++) {
         idx = (short) (initOffset + i + j * NIVnumTilW);
         BGR_DibujarScr(BGRg, BGRdg, idx, x, y, i, j);

     /*    
         idPie = NIVidPieza[idx];
         if(idPie > -1){ //Si hay pieza
           if(PIEdat[idPie][4] == -1  || (PIEdat[idPie][4] !=-1 && COMdat[PIEdat[idPie][4]][0] == IN_FX)) //y no hay combi  => o hay combi y éste está en estado FX
               BGRdg.drawPixels(GAMpiePix, false,
                     (PIEdat[idPie][3] - 1)* 14 * NUMPIXELS_TILE_XY + (NIVnumPart[idx]-1) * NUMPIXELS_TILE_XY,
                     NUMPIXELS_TILEX,
                     x, y,
                     NUMPIXELS_TILEX, NUMPIXELS_TILEY,
                     0, DEVntvFmt);
           else{
             idCom = COM_ObtenerComRaiz(PIEdat[idPie][4]);
             BGRg.setColor(COLval[COMdat[idCom][8]]);
             BGRg.fillRect(x, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
             if ( (idPix = COM_ObtenerIdPix(idCom, BGRnivTilX + i,BGRnivTilY + j)) != -1)
               BGRdg.drawImage(GAMspr[idPix], x, y,
                               Graphics.TOP | Graphics.LEFT, GAMmanip);
           }
         }
         else //dibujar borde o fondo
             if(LVL.NIVmap[idx] == LVL.idBorPixlim){
               BGRg.setColor(LVL.colbase);
               BGRg.fillRect(x, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
             }
             else
                 BGRdg.drawPixels(LVL.pix, false, LVL.NIVmap[idx] * NUMPIXELS_TILE_XY,
                              NUMPIXELS_TILEX, x, y, NUMPIXELS_TILEX,
                              NUMPIXELS_TILEY, 0, DEVntvFmt);
      */
         y += NUMPIXELS_TILEY; //[Alto Tile]
       }
      
       x +=NUMPIXELS_TILEX; //[Ancho Tile]
     }
  }

 
 private void BGR_DibujarScr(Graphics BGRg, DirectGraphics BGRdg, short idx, short x, short y, int i, int j){
     short idPie = NIVidPieza[idx];
     //S40Ed1   BGRg.setClip(x,y, NUMPIXELS_TILEX, NUMPIXELS_TILEY); //MIDP 
     if(idPie > -1){ //Si hay pieza
       if(PIEdat[idPie][4] == -1  || (PIEdat[idPie][4] !=-1 && COMdat[PIEdat[idPie][4]][0] == IN_FX)){ //y no hay combi  => o hay combi y éste está en estado FX                             
            BGRdg.drawPixels(GAMpiePix, false,
                     (PIEdat[idPie][3] - 1)* 14 * NUMPIXELS_TILE_XY + (NIVnumPart[idx]-1) * NUMPIXELS_TILE_XY,
                     NUMPIXELS_TILEX,
                     x, y,
                     NUMPIXELS_TILEX, NUMPIXELS_TILEY,
                     0, DEVntvFmt);
       }  
       else{
         short idCom = COM_ObtenerComRaiz(PIEdat[idPie][4]);
         BGRg.setColor(COLval[COMdat[idCom][8]]);           
         BGRg.fillRect(x, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
         short idPix= COM_ObtenerIdPix(idCom, BGRnivTilX + i,BGRnivTilY + j);
         if (idPix != -1)
           BGRdg.drawImage(GAMspr[idPix], x, y, Graphics.TOP | Graphics.LEFT, GAMmanip);
       }
     }
     else //dibujar borde o fondo
         if(LVL.NIVmap[idx] == LVL.idBorPixlim){
           BGRg.setColor(LVL.colbase);
           BGRg.fillRect(x, y, NUMPIXELS_TILEX, NUMPIXELS_TILEY);
         }
         else
            BGRdg.drawPixels(LVL.pix, false, LVL.NIVmap[idx] * NUMPIXELS_TILE_XY,
                              NUMPIXELS_TILEX, x, y, NUMPIXELS_TILEX,
                              NUMPIXELS_TILEY, 0, DEVntvFmt);
 }
 




//************************************************************************************

 protected synchronized void keyReleased(int keyCode) {
         prev_actorAccion = last_actorAccion;
         last_actorAccion = STOP;

         switch (keyCode) {
           case 53: // Canvas.FIRE:
             disc_actorAccion = FIRE;
             break;
           case 49: //1: ROTAR CCW
             disc_actorAccion = AUX1;
             break;
           case 51: //3: ROTAR CW
             disc_actorAccion = AUX2;
             break;
           case -1: //Canvas.UP
           case 50: //"2"
             disc_actorAccion = ARR;
             break;
           case 56: //"4"
           case -2: //Canvas.DOWN
             disc_actorAccion = ABA;
             break;
           case 48: // 0:
             disc_actorAccion = FIRE2;
             break;
           case 55: //7: Cambio color
             disc_actorAccion = AUX3;
             break;
           case -6:
              disc_actorAccion = SOFTLEFT;
              break;
           case -7:
              disc_actorAccion = SOFTRIGHT;
              break;
          case 57: //9: Cambio Pieza
              disc_actorAccion = AUX4;
              break;
           case -4: //Canvas.RIGHT:
           case 54: //"6"
             disc_actorAccion = DER;
             break;
           case -3: //Canvas.LEFT:
           case 52: //"4"
             disc_actorAccion = IZQ;
             break;

           case 42: //"4"
             disc_actorAccion = AUX5;
             break;

           default:
             disc_actorAccion = STOP;
         }

 }


  protected synchronized void keyPressed(int keyCode) {
    //Si flag de deshabilitación de teclado activado entonces
    //return;

    switch(keyCode) {
      case -1: //Canvas.UP
      case 50: //"2"
        last_actorAccion = ARR;
        break;
      case -2: //Canvas.DOWN
      case 56: //"8"
        last_actorAccion = ABA;
        break;
      case -4: //Canvas.RIGHT:
      case 54: //"6"
        last_actorAccion = DER;
        break;
      case -3: //Canvas.LEFT:
      case 52: //"4"
        last_actorAccion = IZQ;
        break;
      case 57: // 9:
      last_actorAccion = AUX3;
    }
  }

  //Forzar modo pausa si estamos en medio de partida y hay interrupción
  protected void hideNotify(){
    if(VGsts == VGSTS_GAME && GAMsts != PLAY_OUT_PAUSA){
      if(!GAMbNivIntro){
          TIM_IniPausa();
          GAMsts = PLAY_OUT_PAUSA;
          MENid=MENop=0;
      }
    }
    else if (VGsts == VGSTS_MENU && COMBIMidlet.M.MENbMus || VGsts == VGSTS_LOAD)
      VGsts = VGSTS_MENU;
      //COMBIMidlet.M.MENbMus = true; //[PTE]: Parar música (release())
  }



//S40: SND versión .ott (sin MMAPI)
//#if CFGtipoSnd == 1
//# private void SND_Play(int pidx)
//# {
//# return;
//# /*TEMP
//#     if(pidx == 0){
//#       if (COMBIMidlet.M.MENbMus) // y no estamos avisados por HideNotify && aR == 0)
//#         SNDdat[0].play(1);
//#     }else if (COMBIMidlet.M.MENbSnd)
//#               SNDdat[pidx].play(1);
//# */
//# }
//# 
//# private void SND_Stop(int pidx){
//# return;
//# /*TEMP    
//#             SNDdat[pidx].stop();
//#  */
//# }
//#endif

 }
