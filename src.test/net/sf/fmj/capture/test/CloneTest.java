package net.sf.fmj.capture.test;

import java.awt.event.*;

import javax.media.*;
import javax.media.protocol.*;
import javax.swing.*;

import net.sf.fmj.ejmf.toolkit.util.*;
import net.sf.fmj.utility.*;

/**
 * Tests cloning data sources
 * 
 * @author Ken Larson
 * 
 */
public class CloneTest
{
    public static void main(String[] args) throws Exception
    {
        // init FMJ/JMF logging/classpath
        FmjStartup.init();

        // create both data sources:
        final DataSource dsVideo = Manager.createDataSource(new MediaLocator(
                "civil:/0"));

        // merge them:
        final DataSource dsCloneable = Manager
                .createCloneableDataSource(dsVideo);
        final DataSource dsClone = ((SourceCloneable) dsCloneable)
                .createClone();

        // if (dsCloneable instanceof CaptureDevice)
        // System.out.println("CAPTURE DEVICE!!!");
        // if (dsClone instanceof CaptureDevice)
        // System.out.println("dsClone CAPTURE DEVICE!!!");

        final DataSource[] ds = new DataSource[] { dsCloneable, dsClone };

        for (int i = 0; i < ds.length; ++i)
        {
            // create a player:
            final Player player = Manager.createRealizedPlayer(ds[i]);

            // create GUI frame, add player's GUI components to it:
            final PlayerPanel playerpanel = new PlayerPanel(player);

            // already realized so this will work:
            playerpanel.addControlComponent();
            playerpanel.addVisualComponent();

            final JFrame frame = new JFrame("A/V Clone Test " + i);

            // exit on close:
            // Allow window to close
            frame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    System.exit(0);
                }
            });

            // Resize frame whenever new Component is added
            playerpanel.getMediaPanel().addContainerListener(
                    new ContainerListener()
                    {
                        public void componentAdded(ContainerEvent e)
                        {
                            frame.pack();
                        }

                        public void componentRemoved(ContainerEvent e)
                        {
                            frame.pack();
                        }
                    });

            // finish constructing window, and open it
            frame.getContentPane().add(playerpanel);

            frame.pack();
            frame.setVisible(true);

            // start playback:
            player.start();

            // TODO: we don't have much in place to handle problems starting, or
            // anything else really
            // after this point...
        }
    }
}
