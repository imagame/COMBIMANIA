import javax.microedition.media.*;
import javax.microedition.media.control.VolumeControl;


public final class COMBIMusic {
  public VolumeControl volCtrl;
   private int vol, idSnd;
   private boolean bHayCancion;
   private Player mp;

   public COMBIMusic()
   {
       bHayCancion = true;
       vol = 80;
       idSnd = -2;
   }


   public final void TocaMusica(int pMus){
     if(pMus!= idSnd){
       if (pMus == -1)
         ArrancaPlayer("/m.bin");
       else
         ArrancaPlayer("/s" + pMus + ".bin");
       while (!Preparado()) ;
     }
     Play(pMus==-1?-1:1);
     idSnd = pMus;
   }


   //-------------- Inicia música pasada por parámetro
   public final void ArrancaPlayer(String s)
   {
       if(mp != null)
           DestruyePlayer();
       try
       {
           mp = Manager.createPlayer(getClass().getResourceAsStream(s), "Audio/midi");
           mp.realize();
           mp.prefetch();
           volCtrl = (VolumeControl)mp.getControl("VolumeControl");
           volCtrl.setLevel(vol);
           return;
       }
       catch(Exception exception)
       {
           System.out.println(exception);
       }
       bHayCancion = false;
   }

   public final boolean Preparado()
   {
       return mp.getState() == Player.PREFETCHED; //300;
   }


//----------------------------------------------------------Toca música (i=-1 indefinido)
   public final void Play(int i)
   {
       if(!bHayCancion || mp == null)
           return;
       try
       {
           //if(volCtrl != null)
           //    volCtrl.setLevel(vol);
           mp.setLoopCount(i==-1?255:i);
           mp.start();
           return;
       }
       catch(Exception exception)
       {
           System.out.println(exception);
       }
   }


//-------------------------------------------------------- Para el reproductor
   public final void Stop()
   {
       if(!bHayCancion || mp == null)
           return;
       if(mp.getState() == Player.STARTED)
           try
           {
               mp.stop();
               return;
           }
           catch(Exception exception)
           {
               System.out.println(exception);
           }
   }

   public final void a(int i)
   {
       vol = i;
       if(volCtrl != null)
           volCtrl.setLevel(i);
   }

//-------------------------------------------------------- Destruye reproductor
   public final void DestruyePlayer()
   {
       if(mp != null)
       {
           Stop();
           while(mp.getState() == Player.STARTED); //400
           mp.close();
           mp = null;
       }
   }

}