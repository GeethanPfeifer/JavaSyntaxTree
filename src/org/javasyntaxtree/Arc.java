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
Arcs.
 */
public class Arc {
    Boolean bidirectional = false;
    Integer start = null;
    Integer end = null;

    /* labelled start and end */
    String lstart = null;
    String lend = null;


    public Arc(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }
    public Arc(Integer start, Integer end, Boolean bidirectional) {
        this.start = start;
        this.end = end;
        this.bidirectional = bidirectional;
    }
    public Arc(String start, Integer end) {
        this.lstart = start;
        this.end = end;
    }
    public Arc(Integer start, String end) {
        this.start = start;
        this.lend = end;
    }
    public Arc(Integer start, String end, Boolean bidirectional) {
        this.start = start;
        this.lend = end;
        this.bidirectional = bidirectional;
    }

    public void updateArc(Map<String, Integer> map) throws Exception {
        Integer r;
        if(start == null) {
            r = map.get(lstart);
            if(r == null) {
                throw new Exception("Label does not exist.");
            } else {
                this.start = r;
            }
        }
        if(end == null) {
            r = map.get(lend);
            if(r == null) {
                throw new Exception("Label does not exist.");
            } else {
                this.end = r;
            }
        }
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public Boolean isBidirectional() {
        return bidirectional;
    }
}
