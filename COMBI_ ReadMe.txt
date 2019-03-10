///////////////////////////////////////////////////////////////////////////////////////////
C O M B I M A N I A 
//////////////////////////////////////////////////////////////////////////////////////////
por Roberto Pe�a Gonz�lez
www.imagame.com



******************
******************
VERSION SERIES 60
******************
******************

Se relaciona a continuaci�n las caracter�scticas identificativas del producto y las justificaciones de dise�o tomadas en cada una de las areas claves del desarrollo:

C�DIGO: 
-------
Propiedad:
- 100% c�digo propietario. No se hace uso de ninguna API de terceros, �nicamente API del proveedor Nokia.
- Modificada/ajustada alguna rutina concreta con informaci�n de domino p�blico comentada y discutida en los foros del portal www.j2me.org y www.mobilegd.com
APIS proveedor: 
- Utilizada la API est�ndar de NOKIA-UI API (para GameCanvas, DirectGraphics, transparencias, y manipulaci�n -rotaci�n/inversi�n- de imagenes)gr�ficos)
- Todo el c�digo bajo MIDP1.0 y CLDC1.0 (y compatible con versiones superiores MIDP2.0 y CLDC1.1)
Herramientas y IDE:
- Utilizado Nokia Developer Suite for J2ME 3.0 (NDS 3.0) integrado con Borland JBuilder 9. 
- SDK utilizado: Series 60 MIDP SDK 1.2.1 for Symbian OS, Nokia edition (Seleccionado este SDK frente al 2.0 por su mayor estabilidad y la no necesidad de otras APIs y JSR incluida en versiones posteriores)
Particularidades del c�digo con efecto sobre el usuario:
- USo del Teclado: Se hace uso del teclado num�rico (teclas 1,2,3,4,5,6,8, y 0), del cursor de forma opcional y alternativa a las teclas 2,4,5,6 y 8, y de las softkey (derecha e izquierda) est�ndar de Nokia.
- Velocidad de ejecuci�n del game-loo limitada por c�digo a 63 milisegundos por ciclo. Posibilida de aumentar la velocidad del juego si se requiriera porting especifico a modelos m�s lentos.



GR�FICOS:
---------
- Gr�ficos originales realizados con Adobe Photoshop (formato .psd) y conversiones a formato PNG incluidas en la versi�n del juego. La salida en .PNG obtenida desde el Photoshop ha sido filtrada a trav�s de la utilidad de dominio p�blico pngout.exe para compactaci�n al m�ximo del formato.
- Mapas de pantallas originales bocetados en Microsoft PowerPoint.
- Mapas f�sicos de pantalla, y elementos incluidos en los escenarios (obst�culos,etc) mapeados con la herramienta freeware TileStudio (tilestudio.sourceforge.net) 
- Integraci�n de todos los elementos de los niveles del juego utilizando MicroSoft Excel con hojas adaptadas a cada formato de fichero propietario utilizado en el juego (Mapas, Obst�culos, textos, configuraci�n niveles + combis/piezas por defecto, y configuraci� sonido). 
 - Decisi�n de dise�o sobre el Tama�o de Pantalla: El juego no asume un tama�o de pantalla espec�fico aunque bien es verdad que ha sido dise�ado con el tama�o mayoritario de 176x208 pixels (Deber�a comprobarse el comportamiento y la jugabilidad del mismo en pantallas de dimensiones diferentes, para evaluar si es necesario una versi�n especifica con los ajustes m�nimos necesarios. Se ha tomado la decisi�n de no hacer un juego que din�micamente se adapte a cualquier pantalla por no incurrir en problemas de performance durante el tiempo de ejecuci�n.)
- Colores: Utiliza paleta m�xima de 256 colores en cada uno de los niveles, por lo que no hay riesgo de impacto en la calidad visual en dispositivos de diferente n�mero de colores.
- Velocidad de refresco (framerate) dependiente de la decisi�n de velocidad de ejecuci�n en ticks del game loop mencionada en el apartado anterior, con pruebas pr�cticas para una jugabilidad aceptable en modelos con distintos �ndices de velocidad (banda te�rica de fps: 15-22 en funci�n de velocidad dispositivo)


M�SICA:
-------
- Utilizaci�n de formato midi para la m�sica (melodia en fase men�s) y sonidos (Efectos sonoros in-game), haciendo uso de la Mobile Media API MMAPI (JSR-125) incluida en la gran mayor�a de m�viles S60.
- Preparada una versi�n (comentada internamente en el c�digo) que utiliza el formato propietario de sonido NOkia .ott disponibles en todos los dispositivos Nokia. Existe alg�n dispositivo S60 que no tienen incluida la MMAPI y por tanto la versi�n primera no funcionar�a. Ser�a necesario --y en este caso directo-- obtener una versi�n espec�fica para ellos.
- Melod�a del juego obtenida de la web midiworld.com (propiedad de Vitaly Skovorodnikov [vsk261@iname.com], libre uso a nivel personal, pero necesario su permiso para su publicaci�n o distribuci�n). En cualquier caso el juego est� preparado para de forma transparente poder funcionar con otra melod�a en su men� sin necesida de conocer longitud de la misma ni otras caracter�sticas (una simple cambio de nombre del fichero midi bastar�a). Esta opci�n incluso abre posibilidades para las opciones futuras de descargas de temas midi seleccionados a voluntad desde una web.
- Efectos sonoros sacados de diversos website con contenidos freeware midi y retocados utilizando herramientas freeware MIDI. 


TEXTOS:
-------
- Todos los textos informativos del juego (contenido tutorial, y texto descriptivo de cada nivel) han sido extraidos grabados de forma independiente de las clases (ficheros .class) en ficheros binarios con formato UTF-8 (formato est�ndar para texto en J2ME). Esta decisi�n se ha tomado pensado en una futura localizaci�n a otros idiomas de la gran cantidad de texto informativo que contiene COMBImania. El sistema de presentaci�n del texto est� totalmente preparado para poder mostrar mayor o menor cantidad de l�neas de texto (caso l�gico en la traducci� a otro idioma) sin tener que hacer modificaci�n alguna en el c�digo (�nicamente fichero de configuraci�n). Esta propiedad permitir�a una traducci�n sin restricci�n alguna en tama�o ni en  lenguaje.
- Utilizada herramienta desarrollada in-house para la conversi�n de texto UTF-8 de Microsoft (obtenido con NotePad) a texto UTF-8 espec�fico de Java. 
- S�lo el texto dependiente del c�digo y del tama�o de pantalla (marcador, t�tulos tablas, nombre opciones men�) han sido guardados en arrays en el c�digo por su alta dependencia con el entorno. Igualmente ser�a sencilla su localizaci�n a otro idioma por estar aislados en el c�digo, pero exigir�a restricciones de tama�o en su traducci�n



TESTING
-------
Emuladores:
- Desarrollado bajo el NDS (Nokia Developer Suite) 
- Se han detectado problemas en el emulador de 
Dispositivos reales:
- El juego ha sido probado de forma satisfactoria durante todo el proceso de desarrollo en:
a) un Nokia 6630
b) un Nokia 6680
En ambos el comportamiento y funcionamiento del juego en todas sus �reas ha sido id�ntico.


DECISIONES INMEDIATAS DE FUTURO
-------------------------------
- El c�digo ha quedado (temporalmente) sin ofuscar. Ante una posible distribuci�n comercial del juego ser�a necesario un paso final de ofuscaci�n con Proguard (http://proguard.sourceforge.net/).
- El tama�o f�sico del fichero .jar ha quedado menor a 110Kbytes por lo que no existe riesgo de que no pueda correr en todos los dispositivos S60. (con la ofuscaci�n puede ganarse hasta un 30% de espacio).
- Se ha tratado en gran medida de limitar el uso del heap a menos de 400Kb (respecto al l�mite m�ximo recomendado de 800Kb para asegurar su funcionamiento en todos los dispostivos de la serie). La decisi�n de no pasar de los 400Kb y agotar los 800Kb se ha tomado en base a dos hechos: 
1.- teniendo presente la cantidad real -no te�rica- de heap libre en los telefonos (menor a la publicada de 800kb en varios tel�fonos) sobre todo en los modelos menores o m�s angiguos de la Series60
2.- Pensando en un futuro porting a dispositivos de otras marcas (Motorola, Samsung, Siemes, SonyEricsson..) con restricciones mayores y l�mites inferiores en el tama�o del heap. De esa forma la viabilidad del porting a otrar marcas mayoritarias en el mercado queda asegurada sin problemas.









******************
******************
VERSION SERIES 40
******************
******************

La version para los dispositivos de la Serie 40 est� todav�a en desarrollo.
Se ha decidido dedicar el m�ximo esfuerzo y tiempo a la versi�n S60 teniendo en cuenta el alto n�mero de niveles y contenido del juego, para luego m�s tarde realizar un porting a la serie 40 prescindiendo de las caracter�sticas del juego menos relevantes.

Algunas CARACTER�STICAS de la versi�n s40 actualmente en desarrollo: 
- Formato gr�fico propietario (para ganar el m�ximo espacio)
Se trata de desmenuzar el formato PNG para luego en el proceso de cargar del Midlet volver a componerlo din�micamente. EL proceso consiste en separar las paletas de colores de todas las imagenes y reducirlas a 256 colores. Cada imagen o sprite convertirlo a 16 colores (codificaci�n con 4 bits por pixel) de entre los 256 de la paleta elegida. 
Este sistema al similar al que utilizan las grandes compa�ias del sector como Gameloft, TOPGam, etc., y se ha demostrado el m�s apropiado para la serie60 teniendo en cuenta el reducido espacio medio del midlet (64kk) y las peque�as dimensiones de la pantalla(d�nde no se requieren multitud de colores en los sprites)
- M�sica en formato NOKIA .ott (
Formato musical propietario de NOkia y compatible con todos los S40 actuales (evitamos problemas de fragmentaci�n entre S40v1,v2 y posteriores)
- Eliminaci�n de las partes m�s prescindibles de COMBIman�a
a) Eliminaci�n del tutorial creando �nicamente un m�nimo de pantallas de instrucciones
b) Reducci�n de los tiles gr�ficos de cada tema de 64 tiles actuales a los m�nimos imprescindibles para poder dibujar el decorado.
c) Reducci�n de FX gr�ficos (consumen mucho c�digo y a pesar de que aportan vistosidad su ausencia no impacta la jugabilidad)
d) Mayor reutlizaci�n de mapas y escenarios, sin afectar a la composici�n de los 80 niveles jugables del juego.
e) Otras optimizaciones al c�digo que se consideren pertinentes en pro de intentar mantener el 100% del contenido del juego.
d) M�xima uso de las utilidades para comprimir al m�ximo el c�digo: utilidades pngout.exe para gr�ficos, Obsfuscators para c�digo.




****************
JUEGO
****************

INSTALACION
-----------
.jar
.jad


ARRANQUE
--------
Splash
Men� principal con 



CHEATS  <<I M P O R T A N T E>>
------
El juego incorpora un sistema cl�sico de bloqueo por defecto de niveles avanzados, que pueden ser desbloqueados y por tanto jugados una vez han sido superados los niveles anteriores. (qued�ndose registrado en el RecordStore el �ltimo nivel abierto para permitir el acceso directo en posteriores sesiones de juego)

Por defecto COMBIMAN�a permite jugar a:
- Tutorial: Los 20 niveles completos est�n abiertos
- Modo Arcade: Abierto s�lo el primer nivel de la Fase B�sica. Aparecen cerrados los 9 niveles restantes de la fase, y los 10 niveles de la fase siguiente (Fase Avanzada)
- Modo Reflexi�n: Abierto s�lo el primer nivel de cada una de las 4 fases del modo. Aparecen cerradso por defecto los 19 niveles restantes de la fase 1, los 19 restantes de la fase 2, los 9 restantes de la fase 3 y los 9 restantes de la fase 4.

Para permitiros la evaluaci�n del juego en su totalidad y tener acceso a todo tipo de niveles y opciones de jugabilidad que se ofrecen se ha introducido un mecanismo oculto para forzar el desbloqueo de todos los niveles.
Instrucciones para desbloquear los niveles (v�lido para la sesi�n de juego en curso):
> Entrar en la opci�n de men� "Cr�ditos" del men� principal. Pulsa de forma seguida (y sin equivocarse) la siguiente secuencia de teclas: 4,6,2,4,2,6,3
A medida que se van pulsando esas teclas va apareciendo en pantalla la palabra "IMAGAME" (correspondiente a las letras en las que se encuentran dichas teclas), y acto seguido un mensaje de <Desbloqueado>. Saliendo hacia arriba en el men� se podr� comprobar que todos los niveles son ahora visibles y accesibles. 

- Se tom� la decisi�n de incluir este cheat con la intenci�n de mantenerlo oculto y permitir desvelarlo en su momento, en el caso de la futura comercializaci�n del juego. Se incluye referencia en el splash inicial de carga a la p�gina web www.imagame.com en la que podr�n colgarse pistas, gu�as y soluciones de los niveles m�s enrevesados y complejos, as� como del cheat en cuesti�n cu�ndo se considere oportuno.



FUTURO
------
Mucho Material, ideas sobre nuevos puzzles para cada nivel existente, y nuevos tipos de fases con distintos objetivos pero sin variar la mec�nica b�sica del juego se han tenido que quedar en el tintero por las limitaciones l�gicas que presentan los dispositivos m�viles. 
En todo caso puede valorarse como una oportunidad para la prolongaci�n de la vida del juego y la creaci�n de una segunda, o sucesivas, partes que aprovechen en engine de gesti�n de combis creados. El desarrollo modular de COMBIman�a ofrece de una forma flexible y sencilla la incorporaci�n �nicamente del c�digo espec�fico de nuevos objetivos de juego aprovechando la base de manipulaci�n de piezas y creaci�n y eliminaci�n de bloques de piezas o combis.






