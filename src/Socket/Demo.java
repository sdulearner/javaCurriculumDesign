package Socket;

import javax.swing.*;

public class Demo
{
    public static void run(final JFrame frame, final int width, final int height)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                frame.setTitle(frame.getClass().getSimpleName());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(width, height);
                frame.setLocation(400, 150);
                frame.setVisible(true);


            }
        });

    }


}
