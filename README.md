# COMBIMANIA
Combimania J2ME game (Nokia S60)
///////////////////////////////////////////////////////////////////////////////////////////
C O M B I M A N I A 
//////////////////////////////////////////////////////////////////////////////////////////
por Roberto Peña González
www.imagame.com



******************
******************
VERSION SERIES 60
******************
******************

Se relaciona a continuación las caracteríscticas identificativas del producto y las justificaciones de diseño tomadas en cada una de las areas claves del desarrollo:

CÓDIGO: 
-------
Propiedad:
- 100% código propietario. No se hace uso de ninguna API de terceros, únicamente API del proveedor Nokia.
- Modificada/ajustada alguna rutina concreta con información de domino público comentada y discutida en los foros del portal www.j2me.org y www.mobilegd.com
APIS proveedor: 
- Utilizada la API estándar de NOKIA-UI API (para GameCanvas, DirectGraphics, transparencias, y manipulación -rotación/inversión- de imagenes)gráficos)
- Todo el código bajo MIDP1.0 y CLDC1.0 (y compatible con versiones superiores MIDP2.0 y CLDC1.1)
Herramientas y IDE:
- Utilizado Nokia Developer Suite for J2ME 3.0 (NDS 3.0) integrado con Borland JBuilder 9. 
- SDK utilizado: Series 60 MIDP SDK 1.2.1 for Symbian OS, Nokia edition (Seleccionado este SDK frente al 2.0 por su mayor estabilidad y la no necesidad de otras APIs y JSR incluida en versiones posteriores)
Particularidades del código con efecto sobre el usuario:
- USo del Teclado: Se hace uso del teclado numérico (teclas 1,2,3,4,5,6,8, y 0), del cursor de forma opcional y alternativa a las teclas 2,4,5,6 y 8, y de las softkey (derecha e izquierda) estándar de Nokia.
- Velocidad de ejecución del game-loo limitada por código a 63 milisegundos por ciclo. Posibilida de aumentar la velocidad del juego si se requiriera porting especifico a modelos más lentos.



GRÁFICOS:
---------
- Gráficos originales realizados con Adobe Photoshop (formato .psd) y conversiones a formato PNG incluidas en la versión del juego. La salida en .PNG obtenida desde el Photoshop ha sido filtrada a través de la utilidad de dominio público pngout.exe para compactación al máximo del formato.
- Mapas de pantallas originales bocetados en Microsoft PowerPoint.
- Mapas físicos de pantalla, y elementos incluidos en los escenarios (obstáculos,etc) mapeados con la herramienta freeware TileStudio (tilestudio.sourceforge.net) 
- Integración de todos los elementos de los niveles del juego utilizando MicroSoft Excel con hojas adaptadas a cada formato de fichero propietario utilizado en el juego (Mapas, Obstáculos, textos, configuración niveles + combis/piezas por defecto, y configuració sonido). 
 - Decisión de diseño sobre el Tamaño de Pantalla: El juego no asume un tamaño de pantalla específico aunque bien es verdad que ha sido diseñado con el tamaño mayoritario de 176x208 pixels (Debería comprobarse el comportamiento y la jugabilidad del mismo en pantallas de dimensiones diferentes, para evaluar si es necesario una versión especifica con los ajustes mínimos necesarios. Se ha tomado la decisión de no hacer un juego que dinámicamente se adapte a cualquier pantalla por no incurrir en problemas de performance durante el tiempo de ejecución.)
- Colores: Utiliza paleta máxima de 256 colores en cada uno de los niveles, por lo que no hay riesgo de impacto en la calidad visual en dispositivos de diferente número de colores.
- Velocidad de refresco (framerate) dependiente de la decisión de velocidad de ejecución en ticks del game loop mencionada en el apartado anterior, con pruebas prácticas para una jugabilidad aceptable en modelos con distintos índices de velocidad (banda teórica de fps: 15-22 en función de velocidad dispositivo)


MÚSICA:
-------
- Utilización de formato midi para la música (melodia en fase menús) y sonidos (Efectos sonoros in-game), haciendo uso de la Mobile Media API MMAPI (JSR-125) incluida en la gran mayoría de móviles S60.
- Preparada una versión (comentada internamente en el código) que utiliza el formato propietario de sonido NOkia .ott disponibles en todos los dispositivos Nokia. Existe algún dispositivo S60 que no tienen incluida la MMAPI y por tanto la versión primera no funcionaría. Sería necesario --y en este caso directo-- obtener una versión específica para ellos.
- Melodía del juego obtenida de la web midiworld.com (propiedad de Vitaly Skovorodnikov [vsk261@iname.com], libre uso a nivel personal, pero necesario su permiso para su publicación o distribución). En cualquier caso el juego está preparado para de forma transparente poder funcionar con otra melodía en su menú sin necesida de conocer longitud de la misma ni otras características (una simple cambio de nombre del fichero midi bastaría). Esta opción incluso abre posibilidades para las opciones futuras de descargas de temas midi seleccionados a voluntad desde una web.
- Efectos sonoros sacados de diversos website con contenidos freeware midi y retocados utilizando herramientas freeware MIDI. 


TEXTOS:
-------
- Todos los textos informativos del juego (contenido tutorial, y texto descriptivo de cada nivel) han sido extraidos grabados de forma independiente de las clases (ficheros .class) en ficheros binarios con formato UTF-8 (formato estándar para texto en J2ME). Esta decisión se ha tomado pensado en una futura localización a otros idiomas de la gran cantidad de texto informativo que contiene COMBImania. El sistema de presentación del texto está totalmente preparado para poder mostrar mayor o menor cantidad de líneas de texto (caso lógico en la traducció a otro idioma) sin tener que hacer modificación alguna en el código (únicamente fichero de configuración). Esta propiedad permitiría una traducción sin restricción alguna en tamaño ni en  lenguaje.
- Utilizada herramienta desarrollada in-house para la conversión de texto UTF-8 de Microsoft (obtenido con NotePad) a texto UTF-8 específico de Java. 
- Sólo el texto dependiente del código y del tamaño de pantalla (marcador, títulos tablas, nombre opciones menú) han sido guardados en arrays en el código por su alta dependencia con el entorno. Igualmente sería sencilla su localización a otro idioma por estar aislados en el código, pero exigiría restricciones de tamaño en su traducción



TESTING
-------
Emuladores:
- Desarrollado bajo el NDS (Nokia Developer Suite) 
- Se han detectado problemas en el emulador de 
Dispositivos reales:
- El juego ha sido probado de forma satisfactoria durante todo el proceso de desarrollo en:
a) un Nokia 6630
b) un Nokia 6680
En ambos el comportamiento y funcionamiento del juego en todas sus áreas ha sido idéntico.


DECISIONES INMEDIATAS DE FUTURO
-------------------------------
- El código ha quedado (temporalmente) sin ofuscar. Ante una posible distribución comercial del juego sería necesario un paso final de ofuscación con Proguard (http://proguard.sourceforge.net/).
- El tamaño físico del fichero .jar ha quedado menor a 110Kbytes por lo que no existe riesgo de que no pueda correr en todos los dispositivos S60. (con la ofuscación puede ganarse hasta un 30% de espacio).
- Se ha tratado en gran medida de limitar el uso del heap a menos de 400Kb (respecto al límite máximo recomendado de 800Kb para asegurar su funcionamiento en todos los dispostivos de la serie). La decisión de no pasar de los 400Kb y agotar los 800Kb se ha tomado en base a dos hechos: 
1.- teniendo presente la cantidad real -no teórica- de heap libre en los telefonos (menor a la publicada de 800kb en varios teléfonos) sobre todo en los modelos menores o más angiguos de la Series60
2.- Pensando en un futuro porting a dispositivos de otras marcas (Motorola, Samsung, Siemes, SonyEricsson..) con restricciones mayores y límites inferiores en el tamaño del heap. De esa forma la viabilidad del porting a otrar marcas mayoritarias en el mercado queda asegurada sin problemas.









******************
******************
VERSION SERIES 40
******************
******************

La version para los dispositivos de la Serie 40 está todavía en desarrollo.
Se ha decidido dedicar el máximo esfuerzo y tiempo a la versión S60 teniendo en cuenta el alto número de niveles y contenido del juego, para luego más tarde realizar un porting a la serie 40 prescindiendo de las características del juego menos relevantes.

Algunas CARACTERÍSTICAS de la versión s40 actualmente en desarrollo: 
- Formato gráfico propietario (para ganar el máximo espacio)
Se trata de desmenuzar el formato PNG para luego en el proceso de cargar del Midlet volver a componerlo dinámicamente. EL proceso consiste en separar las paletas de colores de todas las imagenes y reducirlas a 256 colores. Cada imagen o sprite convertirlo a 16 colores (codificación con 4 bits por pixel) de entre los 256 de la paleta elegida. 
Este sistema al similar al que utilizan las grandes compañias del sector como Gameloft, TOPGam, etc., y se ha demostrado el más apropiado para la serie60 teniendo en cuenta el reducido espacio medio del midlet (64kk) y las pequeñas dimensiones de la pantalla(dónde no se requieren multitud de colores en los sprites)
- Música en formato NOKIA .ott (
Formato musical propietario de NOkia y compatible con todos los S40 actuales (evitamos problemas de fragmentación entre S40v1,v2 y posteriores)
- Eliminación de las partes más prescindibles de COMBImanía
a) Eliminación del tutorial creando únicamente un mínimo de pantallas de instrucciones
b) Reducción de los tiles gráficos de cada tema de 64 tiles actuales a los mínimos imprescindibles para poder dibujar el decorado.
c) Reducción de FX gráficos (consumen mucho código y a pesar de que aportan vistosidad su ausencia no impacta la jugabilidad)
d) Mayor reutlización de mapas y escenarios, sin afectar a la composición de los 80 niveles jugables del juego.
e) Otras optimizaciones al código que se consideren pertinentes en pro de intentar mantener el 100% del contenido del juego.
d) Máxima uso de las utilidades para comprimir al máximo el código: utilidades pngout.exe para gráficos, Obsfuscators para código.




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
Menú principal con 



CHEATS  <<I M P O R T A N T E>>
------
El juego incorpora un sistema clásico de bloqueo por defecto de niveles avanzados, que pueden ser desbloqueados y por tanto jugados una vez han sido superados los niveles anteriores. (quedándose registrado en el RecordStore el último nivel abierto para permitir el acceso directo en posteriores sesiones de juego)

Por defecto COMBIMANía permite jugar a:
- Tutorial: Los 20 niveles completos están abiertos
- Modo Arcade: Abierto sólo el primer nivel de la Fase Básica. Aparecen cerrados los 9 niveles restantes de la fase, y los 10 niveles de la fase siguiente (Fase Avanzada)
- Modo Reflexión: Abierto sólo el primer nivel de cada una de las 4 fases del modo. Aparecen cerradso por defecto los 19 niveles restantes de la fase 1, los 19 restantes de la fase 2, los 9 restantes de la fase 3 y los 9 restantes de la fase 4.

Para permitiros la evaluación del juego en su totalidad y tener acceso a todo tipo de niveles y opciones de jugabilidad que se ofrecen se ha introducido un mecanismo oculto para forzar el desbloqueo de todos los niveles.
Instrucciones para desbloquear los niveles (válido para la sesión de juego en curso):
> Entrar en la opción de menú "Créditos" del menú principal. Pulsa de forma seguida (y sin equivocarse) la siguiente secuencia de teclas: 4,6,2,4,2,6,3
A medida que se van pulsando esas teclas va apareciendo en pantalla la palabra "IMAGAME" (correspondiente a las letras en las que se encuentran dichas teclas), y acto seguido un mensaje de <Desbloqueado>. Saliendo hacia arriba en el menú se podrá comprobar que todos los niveles son ahora visibles y accesibles. 

- Se tomó la decisión de incluir este cheat con la intención de mantenerlo oculto y permitir desvelarlo en su momento, en el caso de la futura comercialización del juego. Se incluye referencia en el splash inicial de carga a la página web www.imagame.com en la que podrán colgarse pistas, guías y soluciones de los niveles más enrevesados y complejos, así como del cheat en cuestión cuándo se considere oportuno.



FUTURO
------
Mucho Material, ideas sobre nuevos puzzles para cada nivel existente, y nuevos tipos de fases con distintos objetivos pero sin variar la mecánica básica del juego se han tenido que quedar en el tintero por las limitaciones lógicas que presentan los dispositivos móviles. 
En todo caso puede valorarse como una oportunidad para la prolongación de la vida del juego y la creación de una segunda, o sucesivas, partes que aprovechen en engine de gestión de combis creados. El desarrollo modular de COMBImanía ofrece de una forma flexible y sencilla la incorporación únicamente del código específico de nuevos objetivos de juego aprovechando la base de manipulación de piezas y creación y eliminación de bloques de piezas o combis.


