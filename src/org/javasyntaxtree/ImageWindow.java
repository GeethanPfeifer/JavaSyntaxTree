/*
    This file is part of JavaSyntaxTree.
    Copyright (C) 2024 Geethan Pfeifer

    JavaSyntaxTree is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    JavaSyntaxTree is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package org.javasyntaxtree;

import javax.swing.*;
import java.awt.image.*;
import java.io.File;
import java.lang.*;
import java.awt.event.*;
import javax.imageio.*;

public class ImageWindow extends JFrame {
    JFrame frame;
    BufferedImage image;

    public ImageWindow(BufferedImage image) {
        JMenuBar mb;                /* menu bar */
        JMenu fi;                   /* file menu */
        JMenuItem si;               /* save item */
        JMenuItem ci;               /* close item */

        this.image = image;
        mb = new JMenuBar();
        fi = new JMenu("File");
        si = new JMenuItem("Save");
        ci = new JMenuItem("Close");

        si.addActionListener(new SaveListener());
        ci.addActionListener(new CloseListener());


        fi.add(si);
        fi.add(ci);
        mb.add(fi);




        frame = new JFrame();
        frame.setJMenuBar(mb);

        /* Thanks to https://stackoverflow.com/a/9215300 */
        frame.getContentPane().add(new JScrollPane(new JLabel(new ImageIcon(image))));
        frame.pack();

        frame.setVisible(true);

        /* Thanks to https://stackoverflow.com/a/20950345 */
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public void close() {
        /* Thanks to https://stackoverflow.com/a/1235994 */
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    private class CloseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            close();
        }
    }


    public void save() throws Exception {
        JFileChooser fileChooser;
        File saveLocation;

        fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            saveLocation = fileChooser.getSelectedFile();
            /* TODO: MAKE MORE SOPHISTICATED !!! */
            ImageIO.write(image, "png", saveLocation);
        }
    }

    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {save();}
            catch(Exception ignored){}
        }
    }

}
