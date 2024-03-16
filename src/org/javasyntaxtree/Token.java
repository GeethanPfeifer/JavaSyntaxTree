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

import java.util.*;
import java.lang.*;

/*
Function types:
    left-bracket
    right-bracket
    forward-arc
    backward-arc
    bidirectional-arc
    label
    integer
    whitespace
    string
*/

public class Token {
    String function;
    String text;

    public Token(String str) throws Exception {

        int i;                      /* iterator */
        boolean intt = false;       /* trigger integer case */
        StringBuilder gs;           /* generated string for case where enclosed in "" */

        /*
            Basic cases.
        */
        if(str.equals("[") || str.equals("(")) {
            function = "left-bracket";
            text = str;
            return;
        } else if(str.equals("]") || str.equals(")")){
            function = "right-bracket";
            text = str;
            return;
        } else if(str.equals("->")) {
            function = "forward-arc";
            text = str;
            return;
        } else if(str.equals("<-")) {
            function = "backward-arc";
            text = str;
            return;
        } else if(str.equals("<>")) {
            function = "bidirectional-arc";
            text = str;
            return;
        } else if(str.equals(":")) {         /* label: new to javasyntaxtree */
            function = "label";
            text = str;
            return;
        }

        /*
            Integer: getting flag
        */
        if(Character.isDigit(str.charAt(0))) {
            for(i = 0; i < str.length(); i++) {
                intt = Character.isDigit(str.charAt(i));
                /*
                if(!intt) {
                    break;
                }
                */
            }
        }

        if(intt) {
            function = "integer";
            text = str;
            return;
        }

        /*
            Whitespace
            (method from https://www.geeksforgeeks.org/program-to-check-if-a-string-in-java-contains-only-whitespaces/)
        */
        if(str.trim().isEmpty()) {
            function = "whitespace";
            text = str;
            return;
        }

        /*
            Token enclosed in "".

            Very basic escaping. Supports:
                \n                       ->         newline
                \X , all other X         ->         X
        */
        if(str.charAt(0) == '"') {
            /*
                Upperbound for length of generated string is length of original string.
            */
            gs = new StringBuilder(str.length());
            for(i = 1; i < str.length() - 1; i++) {
                if(str.charAt(i) == '\\') {
                    i++;
                    if(str.charAt(i) == 'n') {
                        gs.append('\n');
                    } else {
                        gs.append(str.charAt(i));
                    }
                } else {
                    gs.append(str.charAt(i));
                }
            }
            /*
                Checking that last character indeed is ". Otherwise throws error.
            */
            if(str.charAt(i) != '"') {
                throw new Exception("Invalid token: string not closed.");
            }

            function = "string";
            text = gs.toString();

            return;

        }

        /*
            All other cases: just boring old string.
        */
        function = "string";
        text = str;
        return;


    }

    public String getFunction() {
        return function;

    }
    public String getText() {
        return text;
    }
}
