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

public class MainWindow {
    JFrame frame;
    JPanel panel;
    JTextField inputField;
    JButton outputButton;
    /* final Integer width = 800;
    final Integer height = 600; */
    final Integer defaultTextColumns = 80;
    final String outputButtonText = "Generate Syntax Tree";

    public MainWindow() {
        this.inputField = new JTextField(defaultTextColumns);
        this.outputButton = new JButton(outputButtonText);
        this.outputButton.addActionListener(new OutputButtonListener());

        this.panel = new JPanel();

        /* Thanks to: https://stackoverflow.com/a/18073909 */

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.panel.add(this.inputField);
        this.panel.add(this.outputButton);

        frame = new JFrame("JavaSyntaxTree");
        frame.add(this.panel);

        frame.pack();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private class OutputButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tree T;
            try {T = new Tree(inputField.getText());}
            catch (Exception exception) {
                JOptionPane.showMessageDialog(frame, exception.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            TreeImage ti = new TreeImage(T);
            ImageWindow x = new ImageWindow(ti.getImage());
        }
    }


}
