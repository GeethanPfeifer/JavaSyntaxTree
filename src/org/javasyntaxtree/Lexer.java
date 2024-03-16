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


public class Lexer {

    public static ArrayList<Token> lex(String str) throws Exception {
        int i;                                                        /* iterator */
        StringBuilder cs = new StringBuilder();                       /* collecting string */
        ArrayList<Token> rs = new ArrayList<Token>();                 /* result (tokens) */
        boolean en;                                                   /* escape next: for strings enclosed in " */
        boolean pb;                                                   /* proper break: flag for breaking properly */

        for(i = 0; i < str.length(); i++) {
            /* whitespace and unicharacter: []() */
            if(Character.isWhitespace(str.charAt(i)) || str.charAt(i) == '[' || str.charAt(i) == ']' || str.charAt(i) == '(' || str.charAt(i) == ')' || str.charAt(i) == ':') {

                if(cs.length() != 0) { /* no isEmpty method? wow */
                    rs.add(new Token(cs.toString()));
                    rs.add(new Token(Character.toString(str.charAt(i))));
                    cs = new StringBuilder();
                } else { /* only need to add token */
                    rs.add(new Token(Character.toString(str.charAt(i))));
                }

                continue;
            }



            /* two characters: ->, <-, <> */
            if(str.charAt(i) == '-' && str.charAt(i+1) == '>') {
                i++;
                /* clear, and add */
                if (cs.length() != 0) {
                    rs.add(new Token(cs.toString()));
                    rs.add(new Token("->"));
                    cs = new StringBuilder();
                } else {
                    rs.add(new Token("->"));
                }

                continue;
            }
            if(str.charAt(i) == '<' && str.charAt(i+1) == '-') {
                i++;
                if (cs.length() != 0) {
                    rs.add(new Token(cs.toString()));
                    rs.add(new Token("<-"));
                    cs = new StringBuilder();
                } else {
                    rs.add(new Token("<-"));
                }

                continue;
            }
            if(str.charAt(i) == '<' && str.charAt(i+1) == '>') {
                i++;
                if (cs.length() != 0) {
                    rs.add(new Token(cs.toString()));
                    rs.add(new Token("<>"));
                    cs = new StringBuilder();
                } else {
                    rs.add(new Token("<>"));
                }

                continue;
            }



            /* strings enclosed in " */
            if(str.charAt(i) == '"') {
                /* flush cs */
                if(cs.length() != 0) {
                    rs.add(new Token(cs.toString()));
                    cs = new StringBuilder();
                }




                /* no init.
                    breaks when reaches other ", only cares when end-of-string is reached.
                    iterates through string
                */
                en = true;         /* initially false to handle first " */
                pb = false;        /* init to false */
                for(; i < str.length(); i++) {
                    if(!en && str.charAt(i) == '"') {
                        cs.append(str.charAt(i));
                        pb = true;
                        break;
                    }
                    if(!en && str.charAt(i) == '\\') {
                        en = true;
                        continue;
                    }
                    if(!en) {
                        cs.append(str.charAt(i));
                        continue;
                    }

                    if(en) {
                        cs.append(str.charAt(i));
                        en = false;
                        continue;
                    }

                }
                if(!pb) {
                    throw new Exception("Lexer: string not closed.");
                }

                continue;
            }


            /* boring old characters: just append to cs */
            cs.append(str.charAt(i));


        }
        /* add cs if non-empty */
        if(cs.length() != 0) {
            rs.add(new Token(cs.toString()));
        }


        return rs;

    }

}
